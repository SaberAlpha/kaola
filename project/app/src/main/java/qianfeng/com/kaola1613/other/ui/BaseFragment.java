package qianfeng.com.kaola1613.other.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 *
 * 本应用发现模块的Fragment基类
 *
 * Created by Liu Jianping on 2016/10/09.
 */

public abstract class BaseFragment extends Fragment {

    protected View root;

    protected boolean hasLoadData;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && !hasLoadData && root != null)
        {
            toLoadData();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (getLayoutId() == 0)
        {
            Toast.makeText(getActivity(), "检查一下getLayoutId方法是否正确", Toast.LENGTH_SHORT).show();
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        root = inflater.inflate(getLayoutId(), null);

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initViews();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!hasLoadData)
        {
            toLoadData();
        }
    }

    private void toLoadData()
    {
        hasLoadData = true;
        loadData();
    }

    protected abstract void loadData();

    protected abstract int getLayoutId();

    protected abstract void initViews();

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        hasLoadData = false;
    }
}
