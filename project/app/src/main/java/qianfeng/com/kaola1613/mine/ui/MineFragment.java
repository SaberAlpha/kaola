package qianfeng.com.kaola1613.mine.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import qianfeng.com.kaola1613.R;
import qianfeng.com.kaola1613.other.adapter.CommFragmentPagerAdapter;
import qianfeng.com.kaola1613.other.entity.LoginEvent;
import qianfeng.com.kaola1613.other.ui.LoginActivity;
import qianfeng.com.kaola1613.other.utils.TransformSquare;

/**
 * 我的页面
 *
 * Created by liujianping on 2016/10/10.
 */
public class MineFragment extends Fragment {

    private ViewPager viewPager;

    private TabLayout tabLayout;

    private ImageView ivhead;

    private TextView tvNickName;

    @Subscribe
    public void onEvent(LoginEvent event)
    {
        tvNickName.setText(event.getNickName());
        int width = ivhead.getLayoutParams().width;
        Picasso.with(getActivity())
                .load(event.getHeadUrl())
                .transform(new TransformSquare(event.getHeadUrl(), width, "ivhead", true))
                .into(ivhead);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        viewPager = (ViewPager) view.findViewById(R.id.mine_vp);
        tabLayout = (TabLayout) view.findViewById(R.id.mine_tl);
        ivhead = (ImageView) view.findViewById(R.id.mine_camera_iv);
        tvNickName = (TextView) view.findViewById(R.id.mine_name_tv);

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

        ivhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        EventBus.getDefault().register(this);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new HistoryFragment());
        fragmentList.add(new SubFragment());
        fragmentList.add(new CollectFragment());

        tabLayout.addTab(tabLayout.newTab().setText("收听历史"));
        tabLayout.addTab(tabLayout.newTab().setText("订阅"));
        tabLayout.addTab(tabLayout.newTab().setText("收藏"));

        CommFragmentPagerAdapter fragmentPagerAdapter
                = new CommFragmentPagerAdapter(getChildFragmentManager(),
                fragmentList);

        viewPager.setAdapter(fragmentPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
