package qianfeng.com.kaola1613.discover.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import qianfeng.com.kaola1613.R;
import qianfeng.com.kaola1613.discover.entity.TypeBottom;
import qianfeng.com.kaola1613.other.utils.DeviceUtil;

/**
 * 分页页面下面的adapter
 *
 * Created by liujianping on 2016/10/19.
 */
public class TypeBottomAdapter extends RecyclerView.Adapter<TypeBottomAdapter.TypeBottomHolder>{

    private List<TypeBottom> list;

    private LayoutInflater inflater;

    private Context context;

    private int size;

    public TypeBottomAdapter(Context context, List<TypeBottom> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        size =  ((DeviceUtil.getScreenWidth(context)
                - (int)DeviceUtil.getPxFromDp(context, 20)))/4;
    }

    @Override
    public TypeBottomHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = inflater.inflate(R.layout.adapter_type_bottom, parent, false);
        return new TypeBottomHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TypeBottomHolder holder, final int position) {

        final TypeBottom typeBottom = list.get(position);
        holder.textView.setText(typeBottom.getTitle());

        Picasso.with(context)
                .load(typeBottom.getIcon())
                .into(holder.imageView);

        //添加点击事件

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, typeBottom.getTitle(), Toast.LENGTH_SHORT).show();

                if (onItemClickListener != null)
                {
                    onItemClickListener.onItemClick(holder.itemView, position);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class TypeBottomHolder extends RecyclerView.ViewHolder
    {

        ImageView imageView;

        TextView textView;

        View itemView;

        public TypeBottomHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.type_bottom_iv);
            textView = (TextView) itemView.findViewById(R.id.type_bottom_tv);
            this.itemView = itemView;
            View itemll = itemView.findViewById(R.id.type_bottom_item_ll);
            ViewGroup.LayoutParams layoutParams = itemll.getLayoutParams();
            layoutParams.height = size;
            itemll.setLayoutParams(layoutParams);
        }


    }

    public interface IOnItemClickListener{

        void onItemClick(View itemView, int position);
    }

    private IOnItemClickListener onItemClickListener;

    public void setIOnItemClickListener(IOnItemClickListener listener)
    {
        onItemClickListener = listener;
    }


}
