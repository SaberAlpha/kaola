package qianfeng.com.kaola1613.discover.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import qianfeng.com.kaola1613.R;

/**
 * 电台页面
 *
 * Created by liujianping on 2016/10/10.
 */
public class RadioFragment extends Fragment {

    private PullToRefreshScrollView refreshScrollView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_radio, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        refreshScrollView = (PullToRefreshScrollView) view;

//        refreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
//
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
//
//            }
//        });

        refreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //停止刷新
                        refreshScrollView.onRefreshComplete();
                    }
                }, 3000);
            }
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }
}
