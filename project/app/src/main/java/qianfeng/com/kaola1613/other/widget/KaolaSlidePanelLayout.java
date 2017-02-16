package qianfeng.com.kaola1613.other.widget;

import android.content.Context;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import qianfeng.com.kaola1613.discover.entity.PagerIndexChanedEvent;

/**
 * Created by liujianping on 2016/10/29.
 */
public class KaolaSlidePanelLayout extends SlidingPaneLayout {

    private int discoverIndex;

    @Subscribe
    public void onEvent(PagerIndexChanedEvent event)
    {
        discoverIndex = event.getCurrIndex();
    }


    public KaolaSlidePanelLayout(Context context) {
        super(context);
    }

    public KaolaSlidePanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        //当前这个类成为订阅者
        EventBus.getDefault().register(this);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

//        if (interceptListener== null)
//        {
//            return super.onInterceptTouchEvent(ev);
//        }

        //如果需要拦截那么按照本身的业务处理
//        if (interceptListener.isIntercept())
//        {
//            return super.onInterceptTouchEvent(ev);
//        }

        if (discoverIndex == 0)
        {
            return super.onInterceptTouchEvent(ev);
        }
        else
        {
            return false;
        }


    }

    public interface IInterceptListener
    {
        boolean isIntercept();
    }

    private IInterceptListener interceptListener;

    public void setInterceptListener(IInterceptListener listener)
    {
        this.interceptListener = listener;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        //注销订阅者
        EventBus.getDefault().unregister(this);
    }
}
