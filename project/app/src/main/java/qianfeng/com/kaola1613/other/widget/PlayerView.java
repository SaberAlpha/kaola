package qianfeng.com.kaola1613.other.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import qianfeng.com.kaola1613.R;
import qianfeng.com.kaola1613.other.entity.PlayFresh;
import qianfeng.com.kaola1613.other.entity.Player2;
import qianfeng.com.kaola1613.other.utils.ExecutorUtil;

/**
 * Created by liujianping on 2016/10/25.
 */
public class PlayerView extends LinearLayout {

    private ImageView ivCenter, ivPlay;

    private Player2 player2;

    private PlayFresh playFresh;


    public PlayerView(Context context) {
        super(context);

        initView(context);

    }

    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView(context);

    }

    private void initView(Context context) {

        setOrientation(VERTICAL);
        inflate(context, R.layout.layout_play, this);

        ivCenter = (ImageView) findViewById(R.id.player_center_iv);
        ivPlay = (ImageView) findViewById(R.id.player_play_iv);
    }

    public void play()
    {
        //延时一秒播放，防止ViewPager滑动的时候会卡住
        postDelayed(new Runnable() {
            @Override
            public void run() {
                String playUrl = playFresh.getPlayUrl();

                ExecutorUtil.getInstance().work(playUrl);
            }
        }, 1000);

    }

    public void setData(final Player2 player2)
    {
        this.player2 = player2;

        Picasso.with(getContext())
                .load(player2.getAlbumPic())
                .into(ivCenter);

        ivPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });
    }

    /**
     * 直播
     * @param playFresh
     */
    public void setFreshData(final PlayFresh playFresh)
    {
        this.playFresh = playFresh;

        Picasso.with(getContext())
                .load(playFresh.getPic())
                .into(ivCenter);

        ivPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });
    }


}
