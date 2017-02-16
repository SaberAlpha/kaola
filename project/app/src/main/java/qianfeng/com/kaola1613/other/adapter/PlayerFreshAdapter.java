package qianfeng.com.kaola1613.other.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import qianfeng.com.kaola1613.other.entity.PlayFresh;
import qianfeng.com.kaola1613.other.widget.PlayerView;

/**
 * Created by liujianping on 2016/10/24.
 */
public class PlayerFreshAdapter extends PagerAdapter {

    private List<PlayFresh> list;

    private LayoutInflater inflater;

    private Context context;

    private List<PlayerView> viewList;

    public PlayerFreshAdapter(Context context, List<PlayerView> viewList, List<PlayFresh> list) {
        this.context = context;
        this.list = list;
        this.viewList = viewList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        PlayerView playerView = viewList.get(position);
        container.addView(playerView);

        playerView.setFreshData(list.get(position));

        return playerView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = viewList.get(position);

        container.removeView(view);
    }
}
