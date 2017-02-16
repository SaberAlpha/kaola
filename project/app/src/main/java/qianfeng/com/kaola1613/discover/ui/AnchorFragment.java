package qianfeng.com.kaola1613.discover.ui;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import qianfeng.com.kaola1613.R;
import qianfeng.com.kaola1613.discover.adapter.AnchorListAdapter;
import qianfeng.com.kaola1613.discover.entity.Anchor;
import qianfeng.com.kaola1613.discover.entity.Anchor2;
import qianfeng.com.kaola1613.discover.util.CacheUtil;
import qianfeng.com.kaola1613.discover.util.DiscoverHttpUtil;
import qianfeng.com.kaola1613.other.adapter.CommImagePagerAdapter;
import qianfeng.com.kaola1613.other.ui.BaseFragment;
import qianfeng.com.kaola1613.other.utils.Contants;
import qianfeng.com.kaola1613.other.utils.FileUtil;
import qianfeng.com.kaola1613.other.utils.HttpUtil;
import qianfeng.com.kaola1613.other.utils.KaolaTask;
import qianfeng.com.kaola1613.other.utils.LogUtil;
import qianfeng.com.kaola1613.other.widget.AutoScrollViewPager;
import qianfeng.com.kaola1613.other.widget.LoadingView;
import qianfeng.com.kaola1613.other.widget.VerticalScrollTextView;

/**
 * 主播
 * <p/>
 * Created by liujianping on 2016/10/10.
 */
public class AnchorFragment extends BaseFragment {

    private ListView listView;

    private View header;

    private AutoScrollViewPager viewPager;

    private VerticalScrollTextView vstv1, vstv2;

    private KaolaTask task;

    private int width, height;

    private LoadingView loadingView;

    @Override
    protected void loadData() {
        loadingView.setVisibility(View.VISIBLE);
        showData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_anchor;
    }

    @Override
    protected void initViews() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        header = inflater.inflate(R.layout.listview_header_anchor, null);

        listView = (ListView) root.findViewById(R.id.anchor_lv);

        listView.addHeaderView(header);
        vstv1 = (VerticalScrollTextView) header.findViewById(R.id.anchor_header_vstv1);
        vstv2 = (VerticalScrollTextView) header.findViewById(R.id.anchor_header_vstv2);
        viewPager = (AutoScrollViewPager) header.findViewById(R.id.anchor_header_asvp);

        //计算ViewPager应该显示的高度
        //获取屏幕的宽度
        width = getActivity().getResources().getDisplayMetrics().widthPixels;
        height = width * 3 / 8;

        loadingView = (LoadingView) root.findViewById(R.id.anchor_loadingview);
    }

    private void showData() {
        //获取保存的json
        String anchorJson = FileUtil.getCacheFromPreference(
                CacheUtil.CACHE_FILE_DISCOVER, CacheUtil.CACHE_ANCHOR_FLAG);

        long saveTime = FileUtil.getCacheTimeFromPreference(CacheUtil.CACHE_FILE_DISCOVER,
                CacheUtil.CACHE_ANCHOR_TIME_FLAG);

        long currTime = System.currentTimeMillis();
        long time = (currTime - saveTime) / 1000 / 60;

        task = new KaolaTask(new KaolaTask.IRequest() {
            @Override
            public Object doRequest() {
                return HttpUtil.doGet(DiscoverHttpUtil.URL_ANCHOR);
            }

            @Override
            public Object parseResult(Object obj) {
                return parse(obj);
            }

        }, new KaolaTask.IRequestCallback() {
            @Override
            public void onSuccess(Object object) {
                loadingView.setVisibility(View.GONE);
                showData(object);
            }

            @Override
            public void onError() {
                loadingView.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "请求失败，请检查你的网络是否良好！", Toast.LENGTH_SHORT).show();
            }
        });
        //如果操作时间间隔超过30分钟，需要从服务请求新数据
        if (time > 30) {
            task.execute();
        }
        //如果小于30分钟，直接解析本地的json并显示
        else {
            task.execute(anchorJson);
        }
    }

    @Nullable
    private Object parse(Object obj) {
        String json = obj.toString();
        try {
            JSONObject root = new JSONObject(json);
            String code = root.getString(Contants.JSON_FLAG_CODE);
            String message = root.getString(Contants.JSON_FLAG_MESSAGE);

            if (Contants.JSON_FLAG_CODE_SUCCESS.equals(code)
                    && Contants.JSON_FLAG_MESSAGE_SUCCESS.equals(message)) {
                JSONObject result = root.getJSONObject(Contants.JSON_FLAG_RESULT);
                FileUtil.saveCacheToPreference(CacheUtil.CACHE_FILE_DISCOVER,
                        CacheUtil.CACHE_ANCHOR_FLAG, json);

                FileUtil.saveCacheTimeToPreference( CacheUtil.CACHE_FILE_DISCOVER,
                        CacheUtil.CACHE_ANCHOR_TIME_FLAG, root.getLong(Contants.JSON_FLAG_SERVERTIME));

                final List<Anchor> dataList = Anchor.arrayAnchorFromData(result.toString(), Contants.JSON_FLAG_DATALIST);

                return dataList;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void showData(Object object) {

        List<Anchor> dataList = (List<Anchor>) object;

        LogUtil.w("dataList.size = " + dataList.size());

        //显示滑动广告
        showHeaderPager(dataList.get(0));

        //显示滚动文字
        showScrollText(dataList.get(1));

        //截取从第3条开始剩下的
        List<Anchor> list = dataList.subList(2, dataList.size());

        //显示ListView
        showListView(list);
    }

    /**
     * 显示滚动的文字
     *
     * @param anchor
     */
    private void showScrollText(Anchor anchor) {
        List<Anchor2> dataList = anchor.getDataList();

        final List<String> textList1 = new ArrayList<>();
        final List<String> textList2 = new ArrayList<>();


        for (int i = 0; i < dataList.size(); i++) {
            textList2.add(dataList.get(i).getDes());
            textList1.add(dataList.get(i).getRname().substring(0, 2));
        }

        vstv1.setList(textList1);
        vstv2.setList(textList2);

        vstv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currIndex = vstv1.getCurrIndex();
                Toast.makeText(getActivity(), textList1.get(currIndex), Toast.LENGTH_SHORT).show();
            }
        });
        vstv2.setIOnClickListener(new VerticalScrollTextView.IOnClickListener() {
            @Override
            public void onClick(VerticalScrollTextView view, int index) {
                Toast.makeText(getActivity(), textList2.get(index), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 显示自动滚动的广告
     *
     * @param anchor
     */
    private void showHeaderPager(Anchor anchor) {
        final List<Anchor2> dataList = anchor.getDataList();

        List<ImageView> viewList = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i++) {
            ImageView imageView = new ImageView(getActivity());

            viewList.add(imageView);
        }


        //创建适配器
        CommImagePagerAdapter adapter = new CommImagePagerAdapter(getActivity(), viewList, dataList) {
            @Override
            public void showImage(int position, ImageView imageView) {
                int index = position % dataList.size();
                String url = dataList.get(index).getPic();
                Picasso.with(getActivity())
                .load(url)//加载图片地址

//                .resize(width, height)//显示指定大小的图片
                .into(imageView);//加载到指定的ImageView

            }
        };
        viewPager.setPagerAdapter(adapter, viewList.size());
    }


    private void showListView(List<Anchor> anchorList) {
        AnchorListAdapter anchorListAdapter = new AnchorListAdapter(getActivity(), anchorList);
        listView.setAdapter(anchorListAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //取消任务,true：任务没有执行完也取消，false：让任务执行完

        if (task != null)
        {
            task.cancel(true);
            task = null;
        }

    }
}
