package qianfeng.com.kaola1613.other.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import qianfeng.com.kaola1613.R;
import qianfeng.com.kaola1613.discover.entity.Anchor2;
import qianfeng.com.kaola1613.other.utils.DeviceUtil;
import qianfeng.com.kaola1613.other.utils.LogUtil;
import qianfeng.com.kaola1613.other.utils.TransformSquare;

/**
 * 类命名采用驼峰命名法
 *
 * Created by liujianping on 2016/10/13.
 */
public class AnchorItem extends RelativeLayout {

    private ImageView ivhead;
    private TextView tvNickName, tvRecommendReson;

    private int size;

    public AnchorItem(Context context) {
        super(context);
        //把指定的布局加载到当前的容器中
        initView(context);
    }

    public AnchorItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        //把指定的布局加载到当前的容器中
        inflate(context, R.layout.widget_anchor_item, this);

        ivhead = (ImageView) findViewById(R.id.widget_anchor_head_iv);
        tvNickName = (TextView) findViewById(R.id.widget_anchor_nickname_iv);
        tvRecommendReson = (TextView) findViewById(R.id.widget_anchor_recommend_reson_iv);

        int width = DeviceUtil.getScreenWidth(context);

        //计算圆形的直径
        size = (width - (int)DeviceUtil.getPxFromDp(context, 90)) / 3 ;
    }

    public void setAnchor2(final Anchor2 anchor2)
    {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳到播放页面
                Toast.makeText(getContext(), anchor2.getNickName(), Toast.LENGTH_SHORT).show();
            }
        });
        if (anchor2.getAvatar() != null
                &&!anchor2.getAvatar().isEmpty())
        {
            Picasso.with(getContext())
                    .load(anchor2.getAvatar())
                    .placeholder(R.mipmap.ic_default_round_150_150)
                    .error(R.mipmap.no_net_error_chat_icon)
                    .transform(new TransformSquare(anchor2.getAvatar(), size, "AnchorItem", true))
                //.resize(size, size)//同时使用resize和transform，会以transform的计算结果为准
                    .into(ivhead);
        }


        tvNickName.setText(anchor2.getNickName());
        String recommendReson = anchor2.getRecommendReson();
        if (recommendReson == null || recommendReson.isEmpty())
        {
            int likedNum = anchor2.getLikedNum();
            LogUtil.d("likedNum = " + likedNum);
            String text = "";
            if (likedNum > 10000)
            {
                text = likedNum / 10000 + "W+";
            }
            else
            {
                text = likedNum + "";
            }
            tvRecommendReson.setText(text);
        }
        else
        {
            tvRecommendReson.setText(recommendReson);
        }


        int gender = anchor2.getGender();

        //如果是男
        if (gender == 0)
        {
            tvNickName.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.mipmap.man, 0);
        }
        else if (gender == 1)
        {
            //女
            tvNickName.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.mipmap.woman, 0);
        }
        else
        {
            tvNickName.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
        }

    }



}
