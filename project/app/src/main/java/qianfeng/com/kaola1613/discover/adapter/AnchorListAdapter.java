package qianfeng.com.kaola1613.discover.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import qianfeng.com.kaola1613.R;
import qianfeng.com.kaola1613.discover.entity.Anchor;
import qianfeng.com.kaola1613.other.utils.DeviceUtil;
import qianfeng.com.kaola1613.other.utils.TransformSquare;
import qianfeng.com.kaola1613.other.widget.AnchorItem;

/**
 * 主播页面的adapter
 * <p/>
 * Created by liujianping on 2016/10/11.
 */
public class AnchorListAdapter extends BaseAdapter {

    private List<Anchor> anchorList;

    private LayoutInflater inflater;

    private Context context;

    /**
     * 圆形图片外切正方形的边长
     */
    private int size;

    private TransformSquare transformSquare;

    public AnchorListAdapter(Context context, List<Anchor> anchorList) {
        this.anchorList = anchorList;
        this.context = context;
        inflater = LayoutInflater.from(context);

        int width = DeviceUtil.getScreenWidth(context);

        //计算圆形的直径
        size = (width - (int)DeviceUtil.getPxFromDp(context, 20)) / 3 / 2;

//        transformSquare = new TransformSquare(size, "AnchorListAdapter");
    }

    @Override
    public int getCount() {
        return anchorList == null ? 0 : anchorList.size();
    }

    @Override
    public Object getItem(int position) {
        return anchorList == null ? null : anchorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        AnchorHolder holder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.adapter_anchor_list, null);
            holder = new AnchorHolder(view);
            view.setTag(holder);
        } else {
            holder = (AnchorHolder) view.getTag();
        }

        Anchor anchor = anchorList.get(position);

        holder.tvTitle.setText(anchor.getName());

        holder.anchorItem1.setAnchor2(anchor.getDataList().get(0));
        holder.anchorItem2.setAnchor2(anchor.getDataList().get(1));
        holder.anchorItem3.setAnchor2(anchor.getDataList().get(2));

        return view;
    }

    class AnchorHolder {
        TextView tvTitle, tvMore;

        AnchorItem anchorItem1, anchorItem2, anchorItem3;

        public AnchorHolder(View view) {
            tvTitle = (TextView) view.findViewById(R.id.adapter_anchor_title_tv);
            tvMore = (TextView) view.findViewById(R.id.adapter_anchor_more_tv);

            anchorItem1 = (AnchorItem) view.findViewById(R.id.adapter_anchor_ai1);
            anchorItem2 = (AnchorItem) view.findViewById(R.id.adapter_anchor_ai2);
            anchorItem3 = (AnchorItem) view.findViewById(R.id.adapter_anchor_ai3);
        }
    }
}
