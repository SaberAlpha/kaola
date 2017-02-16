package qianfeng.com.kaola1613.other.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import qianfeng.com.kaola1613.other.utils.HttpUtil;
import qianfeng.com.kaola1613.other.utils.TransformSquare;

/**
 * 弹幕
 *
 * Created by liujianping on 2016/10/28.
 */
public class BarrageView extends View {

    public static final int ORIENTATION_LEFT = 1;
    public static final int ORIENTATION_RIGHT = 2;

    private List<Msg> mList = new ArrayList<>();

    private List<List<Msg>> superList;

    //每次移动的间隔时间
    private int delay = 100;

    //每一波最长需要移动多少次？
    public int allTimes;

    //起始点在屏幕的最右边
    private static float mFromX;

    private static float mFromY;

    private static float mRowHeight;

    //头像的半径
    private float headRadius;

    private Map<String, Bitmap> headMap = new HashMap<>();

    //当前是第几波
    private int index;
    /**
     * 显示几行？
     */
    private final int row = 5;

    /**
     * 方向
     */
    private static int orientation = ORIENTATION_LEFT;

    private static Random random = new Random();

    private Paint paintBg = new Paint();
    private Paint paintBgStroke = new Paint();

    private Paint paintText = new Paint();

    private ExecutorService service;

    private int width;

    private long lastTime;


    public BarrageView(Context context) {
        super(context);
    }

    public BarrageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        width = getResources().getDisplayMetrics().widthPixels;
        paintBg.setColor(Color.WHITE);
        paintText.setColor(Color.RED);
        paintText.setTextSize(20);
        paintBgStroke.setColor(Color.GREEN);
        paintBgStroke.setStrokeWidth(2);
        paintBgStroke.setStyle(Paint.Style.STROKE);

        service = Executors.newFixedThreadPool(10);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //避免在第一次绘制的时候报空指针异常
        if (mList == null)
        {
            return;
        }

        for (int i = 0; i < mList.size(); i++) {
            Msg msg = mList.get(i);

            String text = msg.getText();
            Rect textRect = getTextRect(text);

            //计算圆角矩形左上角顶点的坐标
            float rfFromX = msg.getFromX() - 10;
            float rfFromY = msg.getFromY() - 10;
            //右下角顶点的坐标
            float rfEndX = rfFromX + textRect.width() + 20 + headRadius * 2;
            float rfEndY = rfFromY + /*textRect.height() + 20 */ headRadius * 2;

            RectF rectF = new RectF(rfFromX, rfFromY, rfEndX, rfEndY);

            canvas.drawRoundRect(rectF, headRadius, headRadius, paintBg);
            canvas.drawRoundRect(rectF, headRadius, headRadius, paintBgStroke);

            //画文字
            canvas.drawText(text, msg.getFromX() + headRadius * 2, msg.getFromY() + textRect.height(), paintText);
            String imageUrl = msg.getImageUrl();
            Bitmap bitmap = headMap.get(imageUrl.hashCode() + "");
            if (bitmap == null)
            {
                return;
            }
            canvas.drawBitmap(bitmap, rfFromX, rfFromY, paintBgStroke);
        }

    }

    private Rect getTextRect(String text)
    {
        Rect rect = new Rect();
        paintText.getTextBounds(text, 0, text.length(), rect);
        return rect;
    }

    /**
     * 设置数据源
     * @param msgList
     */
    public void setDataList(List<Msg> msgList)
    {

        for (int i = 0; i < msgList.size(); i++) {

            Msg msg = msgList.get(i);
            final String url = msg.getImageUrl();
            //后台线程
            service.submit(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = HttpUtil.getBitmap(url);

                    Bitmap target = getTransformBitmap(headRadius * 2, url, bitmap);

                    headMap.put(url.hashCode() + "", target);
                }
            });

            //异步线程
            new Thread()
            {
                @Override
                public void run() {
                    super.run();
                }
            }.start();
        }

        superList = new ArrayList<>();

        int count = msgList.size() / 20;
        int ss = msgList.size() % 20;

        if (ss == 0)
        {
            for (int i = 0; i < count; i++) {

                List<Msg> msgs = msgList.subList(i * 20, i * 20 + 20);
                superList.add(msgs);
            }

            //开始第一波
            post(new Runnable() {
                @Override
                public void run() {

                    mFromX = width;
                    //计算每一行的高度
                    mRowHeight = getHeight() / 5;
                    headRadius = mRowHeight / 4;

                    reset();
                }
            });
        }
    }

    private Bitmap getTransformBitmap(float radius, String url, Bitmap source) {
        TransformSquare square = new TransformSquare(url, (int) radius, "TransformSquare", true);

        return square.transform(source);
    }

    private int getAllTimes()
    {
        float s = mList.get(0).getSpeed();

        //获取速度速度 最慢的
        for (int i = 1; i < mList.size(); i++) {
            Msg msg = mList.get(i);

            if (s > msg.speed)
            {
                s = msg.speed;
            }
        }

        return (int)(width / s);
    }

    public void reset()
    {
        //先加入一波数据
        int k = index % superList.size();
        mList.addAll(superList.get(k));

        allTimes = getAllTimes();
        lastTime = System.currentTimeMillis();

        move();
    }

    private void move() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                invalidate();

                next();
            }
        }, delay);
    }

    public void next()
    {
        for (int i = 0; i < mList.size(); i++) {
            Msg msg = mList.get(i);
            msg.scroll();
        }

        if (showNextList())
        {
            index++;

            if (index > 0 && index % superList.size() == 0)
            {
                for (int i = 0; i <mList.size() ; i++) {
                    mList.get(i).resetFromX();
                }

                mList.clear();
            }

            reset();
        }
        else
        {
            move();
        }
    }

    /**
     * 是否要显示下一波
     * 从当前这一波里面查找到速度最慢的那个
     *
     * 用屏幕宽度/速度 就是总共要延时的时间
     *
     * @return
     */
    private boolean showNextList() {
        long nowTime = System.currentTimeMillis();
        if (nowTime - lastTime >= delay * allTimes)
        {
            return true;
        }

        return false;
    }


    /**
     * 弹幕的消息(包含头像和文字内容)
     */
    public static class Msg
    {
        private String imageUrl;

        private String text;

        //移动的速度
        private float speed;

        private float fromX;

        private float fromY;

        public Msg(String imageUrl, String text) {
            this.imageUrl = imageUrl;
            this.text = text;

            //在第几行？
            this.fromY = mRowHeight * random.nextInt(5) + mRowHeight / 2;
        }

        public void resetFromX()
        {
            speed = 5 + random.nextInt(5)  * 3;
            fromX = mFromX + random.nextInt(300) * 3;
        }

        public void scroll()
        {
            if (orientation == ORIENTATION_LEFT)
            {
                fromX-=speed;
            }
            else
            {
                fromX+=speed;
            }
        }

        public float getFromX() {
            return fromX;
        }

        public void setFromX(float fromX) {
            this.fromX = fromX;
        }

        public float getFromY() {
            return fromY;
        }

        public void setFromY(float fromY) {
            this.fromY = fromY;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public float getSpeed() {
            return speed;
        }

        public void setSpeed(float speed) {
            this.speed = speed;
        }
    }
}
