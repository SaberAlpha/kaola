package qianfeng.com.kaola1613.other.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 欢迎页的Adapter
 *
 * Created by liujianping on 2016/10/9.
 */
public class CommFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;

    public CommFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList == null ? null : fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }
}
