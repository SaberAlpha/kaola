package qianfeng.com.kaola1613.discover.ui;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import qianfeng.com.kaola1613.R;
import qianfeng.com.kaola1613.discover.adapter.TypeBottomAdapter;
import qianfeng.com.kaola1613.discover.entity.TypeBottom;
import qianfeng.com.kaola1613.discover.entity.TypeTop;
import qianfeng.com.kaola1613.discover.util.DiscoverHttpUtil;
import qianfeng.com.kaola1613.other.ui.BaseFragment;
import qianfeng.com.kaola1613.other.utils.Contants;
import qianfeng.com.kaola1613.other.utils.HttpUtil;
import qianfeng.com.kaola1613.other.utils.KaolaTask;
import qianfeng.com.kaola1613.other.widget.LoadingView;
import qianfeng.com.kaola1613.other.widget.TypeTopView;

/**
 * 分类
 * Created by liujianping on 2016/10/10.
 */
public class TypeFragment extends BaseFragment {

    private KaolaTask topTask, bottomTask;

    private TypeTopView typeTopView;

    private RecyclerView recyclerView;

    private List<TypeTop> typeTopList = new ArrayList<>();

    private LoadingView loadingView;


    @Override
    protected void loadData() {
        if (typeTopList.isEmpty())
        {
            loadingView.setVisibility(View.VISIBLE);
            showTop();

            showBottom();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_type;
    }

    @Override
    protected void initViews() {
        typeTopView = (TypeTopView) root.findViewById(R.id.type_ttv);
        recyclerView = (RecyclerView) root.findViewById(R.id.type_rv);
        loadingView = (LoadingView) root.findViewById(R.id.type_loadingview);
    }


    private void showBottom() {
        bottomTask = new KaolaTask(new KaolaTask.IRequest() {
            @Override
            public Object doRequest() {
                return HttpUtil.doGet(DiscoverHttpUtil.URL_TYPE_BOTTOM);
            }

            @Override
            public Object parseResult(Object obj) {

                try {
                    JSONObject root = new JSONObject(obj.toString());

                    String message = root.getString(Contants.JSON_FLAG_MESSAGE);

                    String code = root.getString(Contants.JSON_FLAG_CODE);

                    if (Contants.JSON_FLAG_CODE_SUCCESS.equals(code)
                            && Contants.JSON_FLAG_MESSAGE_SUCCESS.equals(message)) {
                        JSONObject result = root.getJSONObject(Contants.JSON_FLAG_RESULT);

                        JSONArray dataList1 = result.getJSONArray(Contants.JSON_FLAG_DATALIST);

                        JSONObject o = (JSONObject) dataList1.get(0);

//                        List<TypeBottom> typeBottomList = TypeBottom.arrayTypeBottomFromData(o.toString(), Contants.JSON_FLAG_DATALIST);

                        JSONArray array2 = o.getJSONArray(Contants.JSON_FLAG_DATALIST);

                        List<TypeBottom> typeBottomList = TypeBottom.arrayTypeBottomFromData(array2.toString());
                        return typeBottomList;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }, new KaolaTask.IRequestCallback() {
            @Override
            public void onSuccess(Object object) {

                loadingView.setVisibility(View.GONE);
                //recyclerView显示Grid效果只能垂直滚动
                //显示表格布局效果，第个参数表示显示多少列
                GridLayoutManager manager = new GridLayoutManager(getActivity(), 4);
                recyclerView.setLayoutManager(manager);

                List<TypeBottom> allList = (List<TypeBottom>) object;


                final List<TypeBottom> list1 = new ArrayList<>();//展开状态的数据源
                final List<TypeBottom> list2 = new ArrayList<>();//收起状态的数据源,12个

                list1.addAll(allList);
                list2.addAll(allList.subList(0, 12));

                //用一个临时变量动态处理数据源
                final List<TypeBottom> tempList = new ArrayList<>();
                //默认是收起
                tempList.addAll(list2);
                final TypeBottomAdapter adapter = new TypeBottomAdapter(getActivity(), tempList);
                recyclerView.setAdapter(adapter);

                adapter.setIOnItemClickListener(new TypeBottomAdapter.IOnItemClickListener() {
                    @Override
                    public void onItemClick(View itemView, int position) {

                        if (position != tempList.size() -1)
                        {
                            //执行正常 跳转
                            return;
                        }

                        //如果现在是展开状态
                        if (isExpand)
                        {
                            //那么需要收起

                            tempList.clear();

                            tempList.addAll(list2);

                            adapter.notifyDataSetChanged();
                        }
                        //如果是收起状态
                        else
                        {
                            //那么需要展开
                            tempList.clear();
                            tempList.addAll(list1);
                            adapter.notifyDataSetChanged();

                        }

                        isExpand = !isExpand;
                    }
                });
            }

            @Override
            public void onError() {
                loadingView.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "bottomTasky请求失败", Toast.LENGTH_SHORT).show();

            }
        });

        bottomTask.execute();
    }

    private void showTop() {
        topTask = new KaolaTask(new KaolaTask.IRequest() {
            @Override
            public Object doRequest() {
                return HttpUtil.doGet(DiscoverHttpUtil.URL_TYPE_TOP);
            }

            @Override
            public Object parseResult(Object obj) {
                try {
                    JSONObject root = new JSONObject(obj.toString());

                    String message = root.getString(Contants.JSON_FLAG_MESSAGE);

                    String code = root.getString(Contants.JSON_FLAG_CODE);

                    if (Contants.JSON_FLAG_CODE_SUCCESS.equals(code)
                            && Contants.JSON_FLAG_MESSAGE_SUCCESS.equals(message))
                    {
                        JSONObject result = root.getJSONObject(Contants.JSON_FLAG_RESULT);

                        List<TypeTop> topList = TypeTop.arrayTypeTopFromData(result.toString(), Contants.JSON_FLAG_DATALIST);
                        typeTopList.addAll(topList);

                        return topList;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }, new KaolaTask.IRequestCallback() {
            @Override
            public void onSuccess(Object object) {
                loadingView.setVisibility(View.GONE);
                //发布一个隐藏事件

                final List<TypeTop> list = (List<TypeTop>) object;

                typeTopView.setTypeTopList(list);

                typeTopView.setITypeOnClickListener(new TypeTopView.ITypeOnClickListener() {
                    @Override
                    public void onClick(int position) {
                        Toast.makeText(getActivity(), list.get(position).getRname(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError() {
                loadingView.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "topTask请求失败", Toast.LENGTH_SHORT).show();
            }
        });

        topTask.execute();
    }

    /**
     * 是否展开
     */
    private boolean isExpand;

    @Override
    public void onDestroyView() {
        super.onDestroyView();


        if (topTask != null)
        {
            topTask.cancel(true);

            topTask = null;
        }

        if (bottomTask != null)
        {
            bottomTask.cancel(true);

            bottomTask = null;
        }


        typeTopList.clear();
    }
}
