package qianfeng.com.kaola1613.other.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import qianfeng.com.kaola1613.discover.entity.TypeTop;
import qianfeng.com.kaola1613.other.utils.DeviceUtil;
import qianfeng.com.kaola1613.other.utils.FileUtil;
import qianfeng.com.kaola1613.other.utils.HttpUtil;
import qianfeng.com.kaola1613.other.utils.KaolaTask;
import qianfeng.com.kaola1613.other.utils.LogUtil;

/**
 *
 * 分类页面
 *
 * 由7个正方形组成的图形
 *
 * Created by liujianping on 2016/10/17.
 */
public class TypeTopView extends View{

    /**
     * 最小的正方形的边长
     */
    private int size;

    /**
     * 白色间距的宽度
     */
    private final int paddingWidth = 5;

    private Map<String, Bitmap> map = new HashMap<>();

    private TypeRect[] typeRectArray ;

    private Paint paint = new Paint();

    private ITypeOnClickListener clickListener;

    public interface ITypeOnClickListener
    {
        void onClick(int position);
    }

    public void setITypeOnClickListener(ITypeOnClickListener listener)
    {
        this.clickListener = listener;
    }

    public TypeTopView(Context context) {
        super(context);
    }

    public TypeTopView(Context context, AttributeSet attrs) {
        super(context, attrs);

        int width = getResources().getDisplayMetrics().widthPixels;

        size = (width - (int)DeviceUtil.getPxFromDp(context, 20) - paddingWidth * 3) / 4;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width  = size * 4 + paddingWidth * 3;
        int height = size * 3 + paddingWidth * 2;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //在setTypeTopList显示数据之前会默认调用一次
        if (typeRectArray == null)
        {
            return;
        }

        for (int i = 0; i < typeRectArray.length; i++) {

            TypeRect typeRect = typeRectArray[i];
            String pic = typeRect.getPic();
            Bitmap bitmap = map.get(pic);
            if (bitmap == null)
            {
                return;
            }
            canvas.drawBitmap(bitmap, typeRect.getFromX(), typeRect.getFromY(), paint);
        }
    }

    public void setTypeTopList(final List<TypeTop> list)
    {
        typeRectArray = new TypeRect[]{
                new TypeRect(size * 0 + paddingWidth * 0, size * 0 + paddingWidth * 0, size * 2 + paddingWidth * 1, size * 2 + paddingWidth * 1,list.get(0).getPic()),
                new TypeRect(size * 2 + paddingWidth * 2, size * 0 + paddingWidth * 0, size * 2 + paddingWidth * 1, size * 1 + paddingWidth * 0,list.get(1).getPic()),
                new TypeRect(size * 2 + paddingWidth * 2, size * 1 + paddingWidth * 1, size * 2 + paddingWidth * 1, size * 1 + paddingWidth * 0,list.get(2).getPic()),
                new TypeRect(size * 0 + paddingWidth * 0, size * 2 + paddingWidth * 2, size * 1 + paddingWidth * 0, size * 1 + paddingWidth * 0,list.get(3).getPic()),
                new TypeRect(size * 1 + paddingWidth * 1, size * 2 + paddingWidth * 2, size * 1 + paddingWidth * 0, size * 1 + paddingWidth * 0,list.get(4).getPic()),
                new TypeRect(size * 2 + paddingWidth * 2, size * 2 + paddingWidth * 2, size * 1 + paddingWidth * 0, size * 1 + paddingWidth * 0,list.get(5).getPic()),
                new TypeRect(size * 3 + paddingWidth * 3, size * 2 + paddingWidth * 2, size * 1 + paddingWidth * 0, size * 1 + paddingWidth * 0,list.get(6).getPic()),
        };

        for (int i = 0; i < list.size(); i++) {

            final TypeTop typeTop = list.get(i);
            final String pic = typeTop.getPic();

            final int position = i;
            KaolaTask task = new KaolaTask(new KaolaTask.IDownloadImage() {
                @Override
                public Bitmap downloadBitmap() {
                    Bitmap source = HttpUtil.getBitmap(pic);

                    return getTranceformBitmap(source, pic, position);
                }
            }, new KaolaTask.IRequestCallback() {
                @Override
                public void onSuccess(Object object) {
                    Bitmap bitmap = (Bitmap) object;
                    map.put(pic, bitmap);

                    invalidate();
                }

                @Override
                public void onError() {
                    LogUtil.e("图片加载失败!");
                }
            });

            task.execute();
        }
    }

    public Bitmap getTranceformBitmap(Bitmap source, String pic, int position)
    {

        if (source == null)
        {
            return source;
        }

        File image = new File(FileUtil.DIR_IMAGE_TRANSFORMATION, "" + pic.hashCode());

        if (image .exists())
        {
            source.recycle();
            return BitmapFactory.decodeFile(image.getAbsolutePath());
        }

        int width = source.getWidth();
        int height = source.getHeight();

        TypeRect typeRect = typeRectArray[position];
        int targetWidth = typeRect.getWidth();
        int targetHeight = typeRect.getHeight();
        Matrix matrix = new Matrix();

        Bitmap target = null;
        switch (position)
        {
            case 0:
            case 3:
            case 4:
            case 5:
            case 6:
                float scale = 1.0f * targetWidth / width;
                matrix.setScale(scale, scale);
                target = Bitmap.createBitmap(source, 0, 0, width, height, matrix, false);

                break;
            case 1:
            case 2:
                //先裁剪,取纵向正中间
                Bitmap bitmap = Bitmap.createBitmap(source, 0, height / 4, width, height / 2);

                //用裁剪后的图片缩放
                float scaleX = 1.0f * targetWidth / bitmap.getWidth();
                float scaleY = 1.0f * targetHeight / bitmap.getHeight();
                matrix.setScale(scaleX, scaleY);

                target = Bitmap.createBitmap(bitmap, 0,0,bitmap.getWidth(), bitmap.getHeight(), matrix, false);
                bitmap.recycle();

                break;
        }

        source.recycle();

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(image);
            target.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (fos != null)
            {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return target;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        //获取触摸在当前控件中的坐标
        float x = event.getX();
        float y = event.getY();

        switch (action)
        {
            //按下事件,所有事件都是从按下开始
            case MotionEvent.ACTION_DOWN:
                return true;

            //弹起
            case MotionEvent.ACTION_UP:
                //当手指抬起的时候就相当于执行一次点击事件

                int touchPosition = getTouchPosition(x, y);

                if (touchPosition == -1)
                {
                    //Toast.makeText(getContext(), "未知区域", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (clickListener != null)
                    {
                        clickListener.onClick(touchPosition);
                    }
                    //Toast.makeText(getContext(), "touchPosition = " + touchPosition, Toast.LENGTH_SHORT).show();
                }
                break;

        }

        return super.onTouchEvent(event);
    }

    private int getTouchPosition(float x, float y)
    {

        for (int i = 0; i < typeRectArray.length; i++) {

            if (typeRectArray[i].isTouched(x, y))
            {
                return i;
            }
        }

        return -1;
    }

    private class TypeRect
    {
        int fromX, fromY, width, height;

        String pic;

        public TypeRect(int fromX, int fromY, int width, int height, String pic) {
            this.fromX = fromX;
            this.fromY = fromY;
            this.width = width;
            this.height = height;
            this.pic = pic;
        }

        /**
         * 是否点中该区域
         * @param x
         * @param y
         * @return
         */
        public boolean isTouched(float x, float y)
        {
            if (x > fromX && x < fromX + width
                    && y > fromY && y < fromY + height)
            {
                return true;
            }

            return false;
        }

        public int getFromX() {
            return fromX;
        }

        public void setFromX(int fromX) {
            this.fromX = fromX;
        }

        public int getFromY() {
            return fromY;
        }

        public void setFromY(int fromY) {
            this.fromY = fromY;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}
