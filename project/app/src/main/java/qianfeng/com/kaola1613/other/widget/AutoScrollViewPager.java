package qianfeng.com.kaola1613.other.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import qianfeng.com.kaola1613.R;

/**
 * 自动滚动的ViewPager
 * <p/>
 * Created by liujianping on 2016/10/11.
 */
public class AutoScrollViewPager extends LinearLayout {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private int currItem;

    private int pageCount;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //LogUtil.d("AutoScrollViewPager autoScroll");
            viewPager.setCurrentItem(++currItem);
            autoScroll();
        }
    };

    public AutoScrollViewPager(Context context) {
        super(context);
        initView(context);
    }

    public AutoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        //加载指定的布局到当前的容器中
        inflate(context, R.layout.widget_auto_scroll_viewpager, this);

        viewPager = (ViewPager) findViewById(R.id.widget_atsv_vp);
        int width, height;
        width = context.getResources().getDisplayMetrics().widthPixels;
        height = width * 3 / 8;
        ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
        layoutParams.height = height;

        viewPager.setLayoutParams(layoutParams);

        tabLayout = (TabLayout) findViewById(R.id.widget_atsv_tl);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currItem = position;

                int index = position % pageCount;
                tabLayout.getTabAt(index).select();
                //在这里不要调用自动滚动的方法 autoScroll()
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 显示数据
     *
     * @param adapter
     */
    public void setPagerAdapter(PagerAdapter adapter, int pageCount) {
        this.pageCount = pageCount;

        viewPager.setAdapter(adapter);
        for (int i = 0; i < pageCount; i++) {
            tabLayout.addTab(tabLayout.newTab());
        }

        autoScroll();
    }

    /**
     * 自动滚动
     */
    public void autoScroll() {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(0, 2000);
        }
    }

    /**
     * 当控件从窗口脱离的时候会回调的方法
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (handler!= null)
        {
            handler.removeMessages(0);
            handler = null;
        }

    }
}
