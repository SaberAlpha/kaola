package qianfeng.com.kaola1613.discover.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import qianfeng.com.kaola1613.R;
import qianfeng.com.kaola1613.discover.adapter.EnterAdapter;
import qianfeng.com.kaola1613.discover.entity.Recommend;
import qianfeng.com.kaola1613.discover.entity.Recommend2;
import qianfeng.com.kaola1613.discover.util.DiscoverHttpUtil;
import qianfeng.com.kaola1613.other.adapter.CommImagePagerAdapter;
import qianfeng.com.kaola1613.other.ui.BaseFragment;
import qianfeng.com.kaola1613.other.ui.WebActivity;
import qianfeng.com.kaola1613.other.utils.Contants;
import qianfeng.com.kaola1613.other.utils.FileUtil;
import qianfeng.com.kaola1613.other.utils.HttpUtil;
import qianfeng.com.kaola1613.other.utils.KaolaTask;
import qianfeng.com.kaola1613.other.utils.LogUtil;
import qianfeng.com.kaola1613.other.widget.AutoScrollViewPager;
import qianfeng.com.kaola1613.other.widget.LoadingView;
import qianfeng.com.kaola1613.other.widget.RecommendPanel;

/**
 * 推荐
 *
 * Created by liujianping on 2016/10/10.
 */
public class RecommendFragment extends BaseFragment {

    private LinearLayout llContent;

    private KaolaTask task;

    private int width, height;

    private SwipeRefreshLayout swipeRefreshLayout;

    private LoadingView loadingView;

    @Override
    protected void loadData() {
//        loadingView.setVisibility(View.VISIBLE);
        excuteTask();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recommend;
    }

    @Override
    protected void initViews() {
        swipeRefreshLayout = (SwipeRefreshLayout) root;
        llContent = (LinearLayout) root.findViewById(R.id.recommend_ll);

        //计算ViewPager应该显示的高度
        //获取屏幕的宽度
        width = getActivity().getResources().getDisplayMetrics().widthPixels;
        height = width * 3 / 8;

        //设置加载显示的颜色，最多可以使用5种
        swipeRefreshLayout.setColorSchemeColors(new int[]{Color.RED, Color.BLUE, Color.YELLOW,Color.GREEN,Color.CYAN});

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            //显示正在加载中，做什么?
            @Override
            public void onRefresh() {
                excuteTask();
            }
        });

        loadingView = (LoadingView) root.findViewById(R.id.recommend_loadingview);
    }

    private void excuteTask() {
        task = new KaolaTask(new KaolaTask.IRequest() {
            @Override
            public Object doRequest() {
                return HttpUtil.doGet(DiscoverHttpUtil.URL_RECOMMED);
            }

            @Override
            public Object parseResult(Object obj) {
                try {
                    JSONObject root = new JSONObject(obj.toString());

                    String code = root.getString(Contants.JSON_FLAG_CODE);
                    String message = root.getString(Contants.JSON_FLAG_MESSAGE);

                    if (Contants.JSON_FLAG_CODE_SUCCESS.equals(code)
                            && Contants.JSON_FLAG_MESSAGE_SUCCESS.equals(message)) {
                        JSONObject result = root.getJSONObject(Contants.JSON_FLAG_RESULT);

                        List<Recommend> recommendList = Recommend.arrayRecommendFromData(result.toString(), Contants.JSON_FLAG_DATALIST);

                        LogUtil.d("recommendList.size = " + recommendList.size());

                        return recommendList;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }, new KaolaTask.IRequestCallback() {
            @Override
            public void onSuccess(Object object) {
                //执行刷新的时候，先清空所有的子控件，防止重复添加，
                // 放在解析成功之后执行，可以防止在删掉和添加这个段时间出现空白
                llContent.removeAllViews();

                List<Recommend> recommendList = (List<Recommend>) object;
//                showBanner(recommendList.get(0));
//
//                showEnter(recommendList.get(1));
//
//                addRecommendPanel(recommendList.get(2));
//                addRecommendPanel(recommendList.get(3));
//                addRecommendPanel(recommendList.get(4));

                //如果数据解析成功了，那么隐藏加载动画

                //隐藏正在加载的效果
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), "请求成功", Toast.LENGTH_SHORT).show();
                loadingView.setVisibility(View.GONE);

                for (int i = 0; i < recommendList.size(); i++) {
                    Recommend recommend = recommendList.get(i);
                    int componentType = recommend.getComponentType();

                    if (componentType == Recommend.ComponentType.TYPE_BANNER)
                    {
                        showBanner(recommend);
                    }
                    else if (componentType == Recommend.ComponentType.TYPE_ENTER)
                    {
                        showEnter(recommend);
                    }
                    else if (componentType == Recommend.ComponentType.TYPE_PANEL)
                    {
                        addRecommendPanel(recommend);
                    }
                }

            }

            @Override
            public void onError() {
                loadingView.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
            }
        });

        task.execute();
    }

    /**
     * 显示快捷入口
     *
     * @param recommend
     */
    private void showEnter(Recommend recommend) {
        RecyclerView recyclerView = new RecyclerView(getActivity());

        //线性显示方式
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        //设置横向滚动
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);

        //设置manager
        recyclerView.setLayoutManager(manager);

        EnterAdapter adapter = new EnterAdapter(getActivity(), recommend.getDataList());

        recyclerView.setAdapter(adapter);

        llContent.addView(recyclerView);
    }

    /**
     * 显示广告
     *
     * @param recommend
     */
    private void showBanner(Recommend recommend) {

        AutoScrollViewPager autoScrollViewPager = new AutoScrollViewPager(getActivity());
        final List<Recommend2> dataList = recommend.getDataList();
        List<ImageView> viewList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            ImageView imageView = new ImageView(getActivity());
            viewList.add(imageView);
        }

        CommImagePagerAdapter adapter = new CommImagePagerAdapter(getActivity(), viewList, dataList) {
            @Override
            public void showImage(int position, ImageView imageView) {
                int index = position % dataList.size();
                final Recommend2 recommend2 = dataList.get(index);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String weburl = recommend2.getWeburl();

                        if (!weburl.isEmpty())
                        {
                            Intent intent = new Intent(getActivity(), WebActivity.class);
                            startActivity(intent);
                        }
                    }
                });


                final String pic = recommend2.getPic();
                Picasso.with(getActivity())
                        .load(pic)
                        .transform(new Transformation() {
                            @Override
                            public Bitmap transform(Bitmap source) {

                                File image = new File(FileUtil.DIR_IMAGE_TRANSFORMATION, pic.hashCode() + "");

//                              如果图片存在，直接返回
                                if (image.exists()) {
                                    source.recycle();
                                    return BitmapFactory.decodeFile(image.getAbsolutePath());
                                }

                                int sWidth = source.getWidth();
                                int sHeight = source.getHeight();

                                int fromY = 180;

                                //裁下我们想析那部分
                                Bitmap transformBitmap = Bitmap.createBitmap(source, 0, fromY, sWidth, sHeight - fromY);
                                //对transformBitmap进行绽放就是我们想要的显示的图片
                                int tWidth = transformBitmap.getWidth();
                                //计算缩放比例
                                float scale = 1.0f * width / tWidth;//目标长度/源图的长度
//                                float scale = width / tWidth * 1.0f;//下面的结果始终为0

                                Matrix matrix = new Matrix();
                                matrix.setScale(scale, scale);

                                Bitmap target = Bitmap.createBitmap(transformBitmap, 0, 0,
                                        transformBitmap.getWidth(), transformBitmap.getHeight(), matrix, false);

                                //保存处理后的图片到指定的目录
                                FileOutputStream fos = null;
                                try {
                                    fos = new FileOutputStream(image);

                                    target.compress(Bitmap.CompressFormat.PNG, 100, fos);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                finally {
                                    if (fos != null)
                                    {
                                        try {
                                            fos.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                source.recycle();
                                transformBitmap.recycle();

                                return target;
                            }

                            @Override
                            public String key() {
                                return "RecommendBannerAdapter";
                            }
                        })
//                        .resize(width, height)//如果图片直接进行缩放处理就可以达到目的的话，那使用这个resize方法不会有影响
                        //如果是需要先裁剪再绽放，那么不能resize
                        .into(imageView);
            }
        };

        autoScrollViewPager.setPagerAdapter(adapter, dataList.size());

        llContent.addView(autoScrollViewPager);
    }

    /**
     * 添加RecommendPanel
     * @param recommend
     */
    private void addRecommendPanel(Recommend recommend)
    {
        RecommendPanel recommendPanel = new RecommendPanel(getActivity());

        recommendPanel.setRecommend(recommend);

        llContent.addView(recommendPanel);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (task != null)
        {
            task.cancel(true);
            task = null;
        }
    }
}
