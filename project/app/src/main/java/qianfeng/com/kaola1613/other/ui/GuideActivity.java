package qianfeng.com.kaola1613.other.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import qianfeng.com.kaola1613.R;
import qianfeng.com.kaola1613.other.adapter.CommFragmentPagerAdapter;
import qianfeng.com.kaola1613.other.utils.Contants;

/**
 * 欢迎页面
 *
 * @author liujianping
 * @date 2016/10/09
 */
public class GuideActivity extends AppCompatActivity {

    private int[] videoIds = new int[]
            {
                R.raw.splash_1,
                R.raw.splash_2,
                R.raw.splash_4,
                R.raw.splash_5
            };

    private int[] ivLeftIds = new int[]
            {
                R.mipmap.guide_anim_1_2,
                R.mipmap.guide_anim_2_2,
                R.mipmap.guide_anim_4_2,
                R.mipmap.guide_anim_5_2
            };

    private int[] ivRightIds = new int[]
            {
                R.mipmap.guide_anim_1_1,
                R.mipmap.guide_anim_2_1,
                R.mipmap.guide_anim_4_1,
                R.mipmap.guide_anim_5_1
            };

    private int[] ivCoverIds = new int[]
            {
                    R.mipmap.guide_video_static_1,
                    R.mipmap.guide_video_static_2,
                    R.mipmap.guide_video_static_4,
                    R.mipmap.guide_video_static_5
            };

    private ViewPager viewpager;

    private int lastPage;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        viewpager = (ViewPager) findViewById(R.id.guide_vp);

        final List<Fragment> fragmentList = new ArrayList<>();

        for (int i = 0; i < videoIds.length; i++) {
            Fragment fragment = new GuideFragment(i ,videoIds[i], ivLeftIds[i], ivRightIds[i], ivCoverIds[i]);
            fragmentList.add(fragment);
        }

        CommFragmentPagerAdapter adapter = new CommFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewpager.setAdapter(adapter);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //当页面选中的时候调用的方法

                //隐藏上一个页面的图片，停止播放的动画
//                GuideFragment lastFragment = (GuideFragment) fragmentList.get(lastPage);
//                lastFragment.hideImages();

                //显示当前页面的动画，
//                GuideFragment currFragment = (GuideFragment) fragmentList.get(position);
//                currFragment.showImages();
//
//                lastPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void startKaoLa(View view)
    {
        Intent intent = new Intent(GuideActivity.this, HomeActivity.class);

        startActivity(intent);

        //修改使用状态
        SharedPreferences preferences = getSharedPreferences(Contants.PREFERENCES_USED, Context.MODE_PRIVATE);

        //获取editor
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(Contants.PREFERENCES_USED_FLAG, false);

        editor.commit();

        //不能从HomeActivity再回到GuideActivity
        finish();
    }
}
