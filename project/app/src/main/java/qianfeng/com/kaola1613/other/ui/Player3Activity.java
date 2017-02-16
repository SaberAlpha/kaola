package qianfeng.com.kaola1613.other.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import qianfeng.com.kaola1613.R;
import qianfeng.com.kaola1613.discover.entity.Recommend2;
import qianfeng.com.kaola1613.other.utils.LogUtil;
import qianfeng.com.kaola1613.other.utils.PlayerService;
import qianfeng.com.kaola1613.other.widget.BarrageView;

/**
 * Created by liujianping on 2016/10/27.
 */
public class Player3Activity extends AppCompatActivity {
    private Recommend2 recommend2;

    private Intent serviceIntent;

    private PlayerService  playerService;

    private NotificationManager notificationManager;

    private BarrageView barrageView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player3);

        recommend2 = getIntent().getParcelableExtra(Player1Activity.TAG_RECOMMED2);
        serviceIntent = new Intent(Player3Activity.this, PlayerService.class);

        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        barrageView = (BarrageView) findViewById(R.id.player3_barrv);

        addMsg();
    }

    private void addMsg() {

        List<BarrageView.Msg> list = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            BarrageView.Msg msg= new BarrageView.Msg("http://img2.imgtn.bdimg.com/it/u=956983069,1106313883&fm=21&gp=0.jpg", "hello>>>" + i);
            list.add(msg);
        }

        barrageView.setDataList(list);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        LogUtil.d("Player3Activity onDestroy ");
    }

    public void start(View btn) {

        String url = null;
        if (recommend2 == null)
        {
            //点击通知跳过来的
            //获取从推送后台推送来的mp3url
            url = getIntent().getExtras().getString("cn.jpush.android.ALERT");
        }
        else
        {
            //从推荐页面点击跳转过来的
            url = recommend2.getMp3PlayUrl();
        }
        serviceIntent.putExtra(PlayerService.PLAY_URL, url);

        //启动服务
        //可以通过Service.stopSelf方法停止,直接调用onDestroy方法
        startService(serviceIntent);

//        playerBinder.play();

//        showNotifi();

    }

    public void stop(View btn) {
        //服务的停止方法
        //只要创建Intent的两个参数一样，即可停止指定的服务
        Intent intent = new Intent(Player3Activity.this, PlayerService.class);
        stopService(intent);

        if (playerService != null)
        {
            playerService.stop();
        }

    }

    public void bind(View btn) {
        //绑定服务
        // 可以是已经启动了，直接执行onBind
        // 如果是没有启动的服务，那么会先调用服务的onCreate,再执行onBind
        //如果activity调用了onDestroy,那么服务会依次调用unBind,onDestroy,那么服务已经停止了
//        bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    public void unbind(View btn) {
        //如果是startService再bindService，那么调用此方法只是解绑，并没有销毁service
        //如果是bindService的方法启动的，那么先onUnbind,再onDestroy
//        unbindService(serviceConnection);
    }

    private int notifiId;

    public void addNotifi(View btn)
    {
        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +getPackageName()
        +"/"+R.raw.alarm);

        Intent intent = new Intent(this, Player1Activity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        //构造一个builder(java设计模式中的构造者模式)
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)//显示在状态栏的小图标(显示手机时间，电量的一栏)
                .setShowWhen(true)//是否显示通知的时间
                .setTicker("你收到了一条消息")
                .setContentTitle("刘艳国给你发送一条消息")
                .setContentText("中午去哪里吃饭？")
                .setAutoCancel(true)//点击是否消失
                .setSound(uri)//设置来通知时播放的音乐
                .setContentIntent(pendingIntent);

        Notification notification = builder.build();

        notificationManager.notify(++notifiId,notification);//不断推送新的通知

    }

}
