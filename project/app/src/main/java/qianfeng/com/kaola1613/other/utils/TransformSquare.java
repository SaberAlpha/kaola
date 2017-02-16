package qianfeng.com.kaola1613.other.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 裁剪正方形
 *
 * Created by liujianping on 2016/10/12.
 */
public class TransformSquare implements Transformation {

    //定义如果是要裁剪正方形的目标边长
    private int size;

    private String key;

    private String url;

    private boolean needCircle;

    private int width, height;

    /**
     * 裁剪正方形
     *
     * @param targetSize 目标正方形边长
     */
    public TransformSquare(String url, int targetSize, String key, boolean needCircle) {
        this.url = url;
        this.size = targetSize;
        this.key = key;
        this.needCircle = needCircle;
    }

    /**
     * 裁剪长方形
     * @param url
     * @param key
     * @param targetWidth 长方形宽度
     * @param targetHeight 长方形高度
     */
    public TransformSquare(String url, String key, int targetWidth, int targetHeight) {
        this.url = url;
        this.key = key;
        this.width = targetWidth;
        this.height = targetHeight;
    }

    /**
     * 修改图片的方法
     *
     * @param source 要加工的图片
     *
     * @return 加工后的图片
     */
    @Override
    public Bitmap transform(Bitmap source) {
        //如果我是要裁剪长方形，那么使用的是第二个构造方法，所以size=0
        if (size == 0)
        {
            return transformRectangle(source);
        }
        //根据图片地址从压缩目录查找有没有对应的处理图片
        String rename = url.hashCode() + "";
        if (needCircle)
        {
            rename += "_circle";
        }
        File targetImage = new File(FileUtil.DIR_IMAGE_TRANSFORMATION, rename);
        //如果文件(图片)已经存在
        if (targetImage.exists())
        {
            source.recycle();
            //直接解析路径获得图片
            return BitmapFactory.decodeFile(targetImage.getAbsolutePath());
        }

        getBitmapMemory(source);
        int sWidth = source.getWidth();
        int sHeight = source.getHeight();

        Bitmap target = null;

        Matrix matrix = new Matrix();

        //如果刚好是正方形
        if (sWidth == sHeight)
        {
            float scale = 1.0f * size / sWidth;//计算缩放比例

            //设置X,Y轴方向绽放比例
            matrix.setScale(scale, scale);
            //根据缩放比例生成一张新的图片
            target = Bitmap.createBitmap(source, 0, 0, sWidth, sHeight, matrix, false);
        }
        else
        {
            int fromX = 0, fromY = 0;
            int fSize = 0;

            fSize = Math.min(sWidth, sHeight);

            fromX = (sWidth - fSize) / 2;
            fromY = (sHeight - fSize) / 2;

            //先剪出一个正方形
            Bitmap bitmap = Bitmap.createBitmap(source, fromX, fromY, fSize, fSize);
            float scale = 1.0f * size / fSize;//缩放比例
            //设置X,Y轴方向绽放比例
            matrix.setScale(scale, scale);
            //缩放生成一张新的图片
            target = Bitmap.createBitmap(bitmap, 0, 0, fSize, fSize, matrix, false);
            bitmap.recycle();
        }

        //在返回加工后的图片之前要释放源图片
        source.recycle();
        getBitmapMemory(target);

        if (needCircle)
        {
            target = transformCircleBitmap(target);
        }

        //保存图片操作
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(targetImage);
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

    /**
     * 裁剪长方形
     *
     * @param source
     * @return
     */
    private Bitmap transformRectangle(Bitmap source) {
        int sWidth = source.getWidth();
        int sHeight = source.getHeight();



        return null;
    }

    /**
     * 加工圆形图片
     *
     * @param source
     *
     * @return
     */
    private Bitmap transformCircleBitmap(Bitmap source)
    {
        int sWidth = source.getWidth();
        int size = sWidth;

        Bitmap background = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(background);
        Paint paint = new Paint();
        canvas.drawCircle(size / 2, size / 2, size / 2, paint);

        //取两个图形的交集
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(source, 0, 0, paint);

        source.recycle();

        return background;
    }

    private void getBitmapMemory(Bitmap bitmap)
    {
        int byteCount = bitmap.getByteCount();
        LogUtil.w("byteCount = " + byteCount);
    }

    @Override
    public String key() {
        return key;
    }
}
