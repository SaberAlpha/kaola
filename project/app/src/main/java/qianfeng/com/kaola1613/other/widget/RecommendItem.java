package qianfeng.com.kaola1613.other.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import qianfeng.com.kaola1613.R;
import qianfeng.com.kaola1613.discover.entity.Recommend2;
import qianfeng.com.kaola1613.other.ui.Player1Activity;
import qianfeng.com.kaola1613.other.ui.Player2Activity;
import qianfeng.com.kaola1613.other.ui.WebActivity;
import qianfeng.com.kaola1613.other.utils.DeviceUtil;

/**
 * Created by liujianping on 2016/10/17.
 */
public class RecommendItem extends RelativeLayout {

    private ImageView ivhead, ivPlay;
    private TextView tvAlbumName,tvRename;
    //正方形图片要显示的宽度
    private int size;

    public RecommendItem(Context context) {
        super(context);
    }

    public RecommendItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.widget_recommend_item, this);

        ivhead = (ImageView) findViewById(R.id.recommend_item_head_iv);
        ivPlay = (ImageView) findViewById(R.id.recommend_item_play_iv);
        tvAlbumName = (TextView) findViewById(R.id.recommend_item_albumName_tv);
        tvRename = (TextView) findViewById(R.id.recommend_item_reName_tv);

        size = (getResources().getDisplayMetrics().widthPixels - (int)DeviceUtil.getPxFromDp(getContext(), 20) - 20)/ 3;
        RelativeLayout.LayoutParams layoutParams = (LayoutParams) ivhead.getLayoutParams();
        layoutParams.width = size;
        layoutParams.height = size;
        ivhead.setLayoutParams(layoutParams);
    }

    public void setRecommend2(final Recommend2 recommend2)
    {
        tvAlbumName.setText(recommend2.getAlbumName());
        tvRename.setText(recommend2.getRname());

        Picasso.with(getContext())
                .load(recommend2.getPic())
                .placeholder(R.mipmap.ic_default_round_150_150)
                .error(R.mipmap.no_net_error_chat_icon)
                .resize(size, size)
                .into(ivhead);

        String mp3PlayUrl = recommend2.getMp3PlayUrl();
        if ("".equals(mp3PlayUrl))
        {
            ivPlay.setVisibility(GONE);
        }

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                int rtype = recommend2.getRtype();

                if (rtype == Recommend2.RType.TYPE_PLAYER1)
                {
                    //跳转到播放1页面
                    Intent intent = new Intent(getContext(), Player1Activity.class);
                    intent.putExtra(Player1Activity.TAG_RECOMMED2, recommend2);
                    getContext().startActivity(intent);

                    //跳到播放3页面
//                    Intent intent = new Intent(getContext(), Player3Activity.class);
//                    intent.putExtra(Player1Activity.TAG_RECOMMED2, recommend2);
//                    getContext().startActivity(intent);
                }
                else if (rtype == Recommend2.RType.TYPE_PLAYER2_1
                        || rtype == Recommend2.RType.TYPE_PLAYER2_2)
                {
                    //跳转到播放1页面
                    Intent intent = new Intent(getContext(), Player2Activity.class);
                    intent.putExtra(Player1Activity.TAG_RECOMMED2, recommend2);
                    getContext().startActivity(intent);
                }
                else if (rtype == Recommend2.RType.TYPE_WEB)
                {
                    //跳转到播放1页面
                    Intent intent = new Intent(getContext(), WebActivity.class);
//                    intent.putExtra(Player1Activity.TAG_RECOMMED2, recommend2);
                    getContext().startActivity(intent);
                }

            }
        });
    }
}
