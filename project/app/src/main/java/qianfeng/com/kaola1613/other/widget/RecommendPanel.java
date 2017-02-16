package qianfeng.com.kaola1613.other.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import qianfeng.com.kaola1613.R;
import qianfeng.com.kaola1613.discover.entity.Recommend;
import qianfeng.com.kaola1613.discover.entity.Recommend2;
import qianfeng.com.kaola1613.other.utils.DeviceUtil;

/**
 * Created by liujianping on 2016/10/17.
 */
public class RecommendPanel extends LinearLayout {

    private TextView tvTitle, tvMore;

    public RecommendPanel(Context context) {
        super(context);

        init(context);
    }

    public RecommendPanel(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        //如果组合控件继承LinearLayout, 并且布局的根标签使用的merge,那么需要加载布局这前设置方向
        setOrientation(VERTICAL);

        setPadding(20, 20, 20, 20);

        LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.topMargin = (int) DeviceUtil.getPxFromDp(getContext(), 20);

        setLayoutParams(params);

        setBackgroundColor(Color.WHITE);
        inflate(context, R.layout.widget_recommend_panel, this);

        tvTitle = (TextView) findViewById(R.id.recommend_panel_title_tv);
        tvMore = (TextView) findViewById(R.id.recommend_panel_more_tv);

    }

    public void setRecommend(Recommend recommend)
    {
        tvTitle.setText(recommend.getName());

        int hasMore = recommend.getHasmore();
        if (hasMore == 0)
        {
            tvMore.setVisibility(GONE);
        }

        List<Recommend2> dataList = recommend.getDataList();

        if (dataList == null)
        {
            return;
        }
        //如果只有3个,只显示一行
        if (dataList.size() == 3)
        {
            RecommendLayout recommendLayout = new RecommendLayout(getContext());

            addView(recommendLayout);

            recommendLayout.setRecommend2List(dataList);
        }

        else if (dataList.size() == 6)
        {
            List<Recommend2> list1 = dataList.subList(0, 3);
            List<Recommend2> list2 = dataList.subList(3, 6);

            RecommendLayout recommendLayout1 = new RecommendLayout(getContext());
            RecommendLayout recommendLayout2 = new RecommendLayout(getContext());

            recommendLayout1.setRecommend2List(list1);
            recommendLayout2.setRecommend2List(list2);

            addView(recommendLayout1);
            addView(recommendLayout2);
        }

    }


}
