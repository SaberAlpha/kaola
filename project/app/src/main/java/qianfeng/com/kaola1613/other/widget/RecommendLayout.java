package qianfeng.com.kaola1613.other.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.List;

import qianfeng.com.kaola1613.R;
import qianfeng.com.kaola1613.discover.entity.Recommend2;

/**
 * Created by liujianping on 2016/10/17.
 */
public class RecommendLayout extends LinearLayout {

    private RecommendItem ri1,ri2,ri3;

    public RecommendLayout(Context context) {
        super(context);

        init(context);
    }

    public RecommendLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context)
    {
        inflate(context, R.layout.widget_recommend_layout, this);
        ri1 = (RecommendItem) findViewById(R.id.recommend_layout_ri1);
        ri2 = (RecommendItem) findViewById(R.id.recommend_layout_ri2);
        ri3 = (RecommendItem) findViewById(R.id.recommend_layout_ri3);

    }

    /**
     * 显示数据
     * @param list
     */
    public void setRecommend2List(List<Recommend2> list)
    {
        ri1.setRecommend2(list.get(0));
        ri2.setRecommend2(list.get(1));
        ri3.setRecommend2(list.get(2));
    }
}
