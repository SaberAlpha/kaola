package qianfeng.com.kaola1613.other.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

import qianfeng.com.kaola1613.R;

/**
 *
 * 自动滚动的文字
 *
 *
 * Created by liujianping on 2016/10/13.
 */
public class VerticalScrollTextView extends View {
    /**
     * 滑动的两个字符串
     */
    private String text1, text2;

    /**
     * text1在控件中间的位置时的Y坐标
     */
    private float fromY;

    private float fromX;

    /**
     * text1,text2滑动的时候的Y坐标
     */
    private float y1, y2;

    /**
     * 每次向上滑动 2px
     */
    private int offsetY = 1;


    /**
     * 一次滚动完成后，暂停2秒钟
     */
    private final int pauseTime = 2000;

    /**
     * 每一次小的滑动间隔时间
     */
    private int scrollDelayTime;

    private int height;

    private List<String> list;

    /**
     * text1在list中的索引
     */
    private int currIndex;

    private Paint paint = new Paint();

    /**
     * 边框的颜色
     */
    private int strokeColor;

    private boolean needStroke;

    private float strokeWidth;

    private float strokeRadius;

    private int textSize;

    private int textColor;

    private boolean isGravityLeft;

    public VerticalScrollTextView(Context context) {
        super(context);
    }

    public VerticalScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerticalScrollTextView);

        strokeWidth = typedArray.getFloat(R.styleable.VerticalScrollTextView_stroke_width, 2.0f);
        needStroke = typedArray.getBoolean(R.styleable.VerticalScrollTextView_need_stroke, false);
        strokeColor = typedArray.getColor(R.styleable.VerticalScrollTextView_stroke_color, Color.BLACK);
        strokeRadius = typedArray.getFloat(R.styleable.VerticalScrollTextView_stroke_radius, 5.0f);
        textSize = typedArray.getDimensionPixelSize(R.styleable.VerticalScrollTextView_text_size, 16);
        textColor = typedArray.getColor(R.styleable.VerticalScrollTextView_text_color, Color.BLACK);
        isGravityLeft = typedArray.getBoolean(R.styleable.VerticalScrollTextView_is_gravity_left, false);

        typedArray.recycle();

        //设置文字大小和画笔的颜色
        paint.setTextSize(textSize);
        paint.setColor(textColor);

        //设置自定义接口回调
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iOnClickListener!= null)
                {
                    iOnClickListener.onClick(VerticalScrollTextView.this, currIndex % list.size());
                }
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (text1 == null || text2 == null)
        {
            return;
        }
        //绘制文字的时候用实心
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(textColor);

        //画字符串
        canvas.drawText(text1, fromX, y1, paint);
        canvas.drawText(text2, fromX, y2, paint);

        if (needStroke)
        {
            paint.setAntiAlias(true);
            RectF rectF = new RectF(1, 1, getWidth() -1, getHeight() -1);
            //设置画笔是空心的
            paint.setStrokeWidth(strokeWidth);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(strokeColor);

            //绘制圆角矩形
            canvas.drawRoundRect(rectF, strokeRadius, strokeRadius, paint);
        }
    }

    public void setList(List<String> list)
    {
        this.list = list;
        start();
    }

    private int getTextHeight(String text)
    {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        paint.measureText(text);

        int textHeight = rect.height();

        return textHeight;
    }

    private int getTextWidth(String text)
    {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        paint.measureText(text);

        int textWidth = rect.width();

        return textWidth;

    }

    /**
     * 第一次操作
     */
    private void start()
    {
        post(new Runnable() {
            @Override
            public void run() {
                height = getHeight();

                //间隔时间=总时间/绘制次数
                scrollDelayTime = 1000 / (height / offsetY);

                next();
            }
        });

    }

    /**
     * 下一次大的滚动操作
     */
    private void next()
    {
        int index1 = currIndex % list.size();
        text1 = list.get(index1);
        currIndex++;
        int index2 = currIndex % list.size();
        text2 = list.get(index2);

        //起始点Y坐标
        fromY = (height + getTextHeight(text1)) / 2;

        //如果是对齐左边就不用计算起始X
        if (!isGravityLeft)
        {
            fromX = (getWidth() - getTextWidth(text1)) / 2;
        }

        //text1的Y坐标和超始点一样
        y1 = fromY;
        //text的Y坐标比text1的Y坐标大一个控件的高度
        y2 = y1 + height;

        scroll();
    }

    /**
     * 执行一次小的滑动
     */
    private void scroll()
    {
        y1 = y1 - offsetY;
        y2 = y2 - offsetY;

        //重新绘制
        invalidate();

        //延时执行下一次小的滑动
        postDelayed(new Runnable() {
            @Override
            public void run() {
                //判断是否需要暂停
                if(needPause())
                {
                    pause();
                }
                else
                {
                    scroll();
                }
            }
        }, scrollDelayTime);
    }

    /**
     * 暂停
     */
    private void pause()
    {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                next();
            }
        }, pauseTime);

    }

    /**
     * 是否需要暂停
     *
     * 因为文字的高度可能不一样，控件的高度值是奇数
     *
     * @return
     */
    private boolean needPause()
    {
        return y2 <= fromY;
    }

    public int getCurrIndex() {
        return currIndex % list.size();
    }


    /**
     * 自定义控件监听接口
     *
     * 4步
     * 1.接口名称和方法
     * 2.添加接口成员变量
     * 3.添加set方法
     * 4.添加回调业务
     */
    public interface IOnClickListener
    {
        void onClick(VerticalScrollTextView view, int index);
    }

    private IOnClickListener iOnClickListener;

    public void setIOnClickListener(IOnClickListener iOnClickListener)
    {
        this.iOnClickListener = iOnClickListener;
    }
}
