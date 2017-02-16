package qianfeng.com.kaola1613.offline.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import qianfeng.com.kaola1613.R;

/**
 * 离线页面
 *
 * Created by liujianping on 2016/10/10.
 */
public class OfflineFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offline, null);
    }
}
