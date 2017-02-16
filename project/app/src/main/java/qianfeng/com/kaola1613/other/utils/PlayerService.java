package qianfeng.com.kaola1613.other.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import qianfeng.com.kaola1613.R;

/**
 * Created by liujianping on 2016/10/27.
 */
public class PlayerService extends Service {

    private String url;

    public static final String PLAY_URL = "play_url";

    private NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        LogUtil.d("PlayerService onCreate");

        initNotificationBar();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        url = intent.getStringExtra(PLAY_URL);
        LogUtil.d("PlayerService onStart url = " + url);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        url = intent.getStringExtra(PLAY_URL);
        LogUtil.d("PlayerService onStartCommand url = " + url);

        play();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        LogUtil.d("PlayerService onDestroy");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.d("PlayerService onBind");
        url = intent.getStringExtra(PLAY_URL);
        return new PlayerBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtil.d("PlayerService onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);

        LogUtil.d("PlayerService onRebind");
    }

    public class PlayerBinder extends Binder
    {
        public PlayerService getService(){
            return PlayerService.this;
        }


    }

    public void play()
    {
        MediaPlayerUtil.getInstance().play(url);

//        showNotifi();
    }

    public void pause()
    {
        MediaPlayerUtil.getInstance().pause();
    }

    public void stop()
    {
        MediaPlayerUtil.getInstance().stop();
    }

    public void showNotifi()
    {
        Notification notification = new Notification();

        //通知栏显示布局
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.layout_notify);



        //设置布局
        notification.contentView = contentView;

        Intent intent = new Intent("player.service.play");

        PendingIntent playIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        contentView.setOnClickPendingIntent(R.id.notifi_play_iv, playIntent);



        //设置 清除通知栏的时候不被清除
        notification.flags = Notification.FLAG_NO_CLEAR;

        //显示通知(通知的id:用来取消通知，识别用的)
        notificationManager.notify(1111, notification);

    }



    public void initNotificationBar() {
        Notification notification = new Notification();
        //设置通知的icon,如果有自定义的布局，这个显示不出来的, 如果不设置这个属性，那么不会在通知栏显示该通知
        notification.icon = R.mipmap.ic_launcher;//在通知栏显示的大图标,
        //通知的布局
        RemoteViews contentView = new RemoteViews(getPackageName(),
                R.layout.layout_notify);
        //设置布局
        notification.contentView = contentView;

        //PendingIntent是执行点击事件的意图
        //如果点击通知栏上的控件要发送广播，那么就用getBroadcast生成PendingIntent对象

//        PendingIntent.getService() //开启一个服务
//        PendingIntent.getActivity()//

        //设置点击播放按钮时发送的通知
        Intent intentPlay = new Intent("com.kaola.play");
        PendingIntent playIntent = PendingIntent.getBroadcast(this, 0, intentPlay, 0);
        contentView.setOnClickPendingIntent(R.id.notifi_play_iv, playIntent);

        Intent intentPause = new Intent("com.kaola.pause");
        PendingIntent pauseIntent = PendingIntent.getBroadcast(this, 0, intentPause, 0);
        contentView.setOnClickPendingIntent(R.id.notifi_pause_iv, pauseIntent);

        Intent intentClose = new Intent("com.kaola.close");
        PendingIntent closeIntent = PendingIntent.getBroadcast(this, 0, intentClose, 0);
        contentView.setOnClickPendingIntent(R.id.notifi_close_iv, closeIntent);

        //设置 不能被清除
        notification.flags = Notification.FLAG_NO_CLEAR;
        //发起通知
        notificationManager.notify(1234, notification);

    }


}
