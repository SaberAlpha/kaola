package qianfeng.com.kaola1613.other.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

import qianfeng.com.kaola1613.R;
import qianfeng.com.kaola1613.other.entity.Comment;

/**
 * Created by liujianping on 2016/10/23.
 */
public class CommentListAdapter extends BaseAdapter {

    private List<Comment> list;
    private LayoutInflater inflater;
    private Context context;

    //构造者模式
    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheOnDisk(true)//磁盘缓存
            .cacheInMemory(true)//是否使用内存缓存
            .bitmapConfig(Bitmap.Config.RGB_565)//图片质量
//            .displayer(new CircleBitmapDisplayer())//显示圆形图片
            .displayer(new RoundedBitmapDisplayer(20))//显示圆角图片
            .showImageOnFail(R.mipmap.no_net_error_chat_icon)//显示错误图片
            .showImageForEmptyUri(R.mipmap.no_net_error_chat_icon)//url为""时候显示图片
            .showImageOnLoading(R.mipmap.ic_default_round_150_150)//默认图片
            .build();


    public CommentListAdapter(Context context, List<Comment> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return list == null ? null : list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CommentListHolder holder = null;
        if (view == null)
        {
            view = inflater.inflate(R.layout.adapter_comment_list, null);
            holder = new CommentListHolder();

            holder.ivHead = (ImageView) view.findViewById(R.id.comment_list_head_iv);
            holder.tvNicname = (TextView) view.findViewById(R.id.comment_list_nickname_tv);
            holder.tvTime = (TextView) view.findViewById(R.id.comment_list_time_tv);
            holder.tvContent = (TextView) view.findViewById(R.id.comment_list_content_tv);
            view.setTag(holder);
        }
        else
        {
            holder = (CommentListHolder) view.getTag();
        }

        Comment comment = list.get(i);

        /*if (!"".equals(comment.getUserImg()))
        {
            Picasso.with(context)
                    .load(comment.getUserImg())
                    .error(R.mipmap.no_net_error_chat_icon)
                    .placeholder(R.mipmap.ic_default_round_150_150)
                    .into(holder.ivHead);
        }*/

        ImageLoader.getInstance().displayImage(comment.getUserImg(), holder.ivHead, options);

        holder.tvNicname.setText(comment.getUserName());
        holder.tvTime.setText(comment.getCreateTime());
        holder.tvContent.setText(comment.getContent());
        return view;
    }

    class CommentListHolder
    {
        TextView tvNicname, tvTime, tvContent;

        ImageView ivHead;
    }
}
