package qianfeng.com.kaola1613.other.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import qianfeng.com.kaola1613.other.utils.LogUtil;

/**
 * 播放页面图片上面的效果
 *
 * Created by liujianping on 2016/10/23.
 */
public class StackView extends View {

//    private int count = 3;

    private int verticalPadding = 15;//px

    private int horizontalPadding = 15;

    //圆角矩形的高度
    private int rectHeight = 100;

    private int radius = 15;

    private int color = 0x33FFFFFF;//#aa987ea4

    private int width, height;

    //从下往上数，第一个矩形
    private int leftFromX, fromY;
    //动画结束的终点,控件的左下角的位置
    private int endX, endY;

    //每次的变化值
    private int offerSet = 1;

    private int delay = 5;

    private Paint paint = new Paint();

    private boolean isDrawing;

    public StackView(Context context) {
        super(context);
    }

    public StackView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint.setColor(color);
        //在构造方法里直接获取宽高是0,因为还没有调用测量方法
        post(new Runnable() {
            @Override
            public void run() {
                width = getWidth();
                height = getHeight();

                //重置
                reset();

                LogUtil.d("leftFromX = " + leftFromX + " , fromY = " + fromY);

                endX = 0;
                endY = height;

                showAnim();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        RectF rectf4 = new RectF(leftFromX + horizontalPadding * 3, fromY - verticalPadding * 3,
                width - (leftFromX + horizontalPadding * 3), rectHeight + fromY - verticalPadding * 3);

        RectF rectf3 = new RectF(leftFromX + horizontalPadding * 2, fromY - verticalPadding * 2,
                width - (leftFromX + horizontalPadding * 2), rectHeight + fromY - verticalPadding * 2);

        RectF rectf2 = new RectF(leftFromX + horizontalPadding * 1, fromY - verticalPadding * 1,
                width - (leftFromX + horizontalPadding * 1), rectHeight + fromY - verticalPadding * 1);

        RectF rectf1 = new RectF(leftFromX + horizontalPadding * 0, fromY - verticalPadding * 0,
                width - (leftFromX + horizontalPadding * 0), rectHeight + fromY - verticalPadding * 0);

        //画圆 角矩形
        canvas.drawRoundRect(rectf4, radius, radius, paint);
        canvas.drawRoundRect(rectf3, radius, radius, paint);
        canvas.drawRoundRect(rectf2, radius, radius, paint);
        canvas.drawRoundRect(rectf1, radius, radius, paint);
    }

    /**
     * 执行动画
     */
    public void showAnim()
    {
        //正在滑动的时候不能再点了
        if (isDrawing)
        {
            return;
        }
        isDrawing = true;
        scrollDown();
    }

    /**
     * 往下滑动
     */
    private void scrollDown() {

        leftFromX -= offerSet;
        fromY += offerSet;

        invalidate();

        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (needPause())
                {
                    reset();
                }
                else
                {
                    showAnim();
                }
            }
        }, delay);
    }

    /**
     * 是否需要停止
     * @return
     */
    private boolean needPause()
    {
        //控件每次滑动的距离（2格）
        return fromY - verticalPadding == endY;
    }

    /**
     * 重置起始点
     */
    private void reset()
    {
        isDrawing = false;
        leftFromX = horizontalPadding;
        fromY = height - verticalPadding;
    }
}
