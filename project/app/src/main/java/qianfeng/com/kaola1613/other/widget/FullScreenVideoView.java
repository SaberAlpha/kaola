package qianfeng.com.kaola1613.other.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * 全屏播放的VideoView
 *
 * 自定义控件的第三种方式：继承源生控件，重写某些方法或者添加修改某些功能
 *
 * Created by liujianping on 2016/10/9.
 */
public class FullScreenVideoView extends VideoView {

    public FullScreenVideoView(Context context) {
        super(context);
    }

    public FullScreenVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //测量控件的宽和高
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        //设置内容显示的尺寸
        setMeasuredDimension(width, height);
    }
}
