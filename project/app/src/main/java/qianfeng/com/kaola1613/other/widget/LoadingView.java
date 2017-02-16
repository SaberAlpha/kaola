package qianfeng.com.kaola1613.other.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import qianfeng.com.kaola1613.R;

/**
 * 正在加载中的效果
 *
 * Created by liujianping on 2016/10/26.
 */
public class LoadingView extends RelativeLayout {

    private ImageView ivBg;

    public LoadingView(Context context) {
        super(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setBackgroundColor(getResources().getColor(R.color.public_gray));
        inflate(context, R.layout.widget_loading_view, this);
        ivBg = (ImageView) findViewById(R.id.widget_loading_bg_iv);

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_loading_circle);

        ivBg.startAnimation(animation);

    }


}
