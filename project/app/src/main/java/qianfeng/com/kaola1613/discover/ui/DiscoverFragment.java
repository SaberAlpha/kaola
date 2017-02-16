package qianfeng.com.kaola1613.discover.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import qianfeng.com.kaola1613.R;
import qianfeng.com.kaola1613.discover.entity.PagerIndexChanedEvent;
import qianfeng.com.kaola1613.other.adapter.CommFragmentPagerAdapter;

/**
 * 发现页面
 *
 * Created by liujianping on 2016/10/10.
 */
public class DiscoverFragment extends Fragment {

    private TabLayout tabLayout;

    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discover, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tabLayout = (TabLayout) view.findViewById(R.id.discover_tl);
        viewPager = (ViewPager) view.findViewById(R.id.discover_vp);

        tabLayout.addTab(tabLayout.newTab().setText("推荐"));
        tabLayout.addTab(tabLayout.newTab().setText("分类"));
        tabLayout.addTab(tabLayout.newTab().setText("电台"));
        tabLayout.addTab(tabLayout.newTab().setText("主播"));

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<Fragment> fragmentList = new ArrayList<>();

        fragmentList.add(new RecommendFragment());
        fragmentList.add(new TypeFragment());
        fragmentList.add(new RadioFragment());
        fragmentList.add(new AnchorFragment());

        //Fragment嵌套Fragment用getChildFragmentManager
        CommFragmentPagerAdapter pagerAdapter = new CommFragmentPagerAdapter(getChildFragmentManager(), fragmentList);
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();

//                HomeActivity home = (HomeActivity)getActivity();
//                home.setDiscoverPagerIndex(position);

                EventBus.getDefault().post(new PagerIndexChanedEvent(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
}
