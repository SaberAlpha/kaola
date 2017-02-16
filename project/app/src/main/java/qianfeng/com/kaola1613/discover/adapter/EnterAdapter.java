package qianfeng.com.kaola1613.discover.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import qianfeng.com.kaola1613.R;
import qianfeng.com.kaola1613.discover.entity.Recommend2;

/**
 * Created by liujianping on 2016/10/14.
 */
public class EnterAdapter  extends RecyclerView.Adapter<EnterAdapter.EnterHolder>{

    private LayoutInflater inflater;

    private Context context;

    private List<Recommend2> recommend2List;

    private int width, height;

    public EnterAdapter(Context context, List<Recommend2> recommend2List)
    {
        this.context = context;
        this.recommend2List = recommend2List;
//        inflater = LayoutInflater.from(context);
        //通过获取系统服务的方式获得LayoutInflater
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        width = context.getResources().getDisplayMetrics().widthPixels / 4;

        height = width;
    }

    @Override
    public EnterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.adapter_recommend_enter, parent, false);
        EnterHolder holder = new EnterHolder(itemView);
        return holder;
    }

    /**
     * 显示每一个item的内容
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(EnterHolder holder, int position) {
        Recommend2 recommend2 = recommend2List.get(position);

        Picasso.with(context)
                .load(recommend2.getPic())
                //.transform(new TransformSquare())
                .resize(width,height)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return recommend2List == null ? 0 : recommend2List.size();
    }

    class EnterHolder extends RecyclerView.ViewHolder
    {

        ImageView imageView;

        /**
         *
         * @param itemView RecyclerView item的根布局
         */
        public EnterHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.recommend_enter_iv);
        }
    }
}
