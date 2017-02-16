package qianfeng.com.kaola1613.other.utils;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by liujianping on 2016/10/28.
 */
public class PlayerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (action.equals("com.kaola.play"))
        {
            //继续播放
            MediaPlayerUtil.getInstance().play();
        }
        else if (action.equals("com.kaola.pause"))
        {
            MediaPlayerUtil.getInstance().pause();
        }
        else if (action.equals("com.kaola.close"))
        {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            //清理指定id的通知
            manager.cancel(1234);



//            System.exit(0);//使用这个方法虽然能让应用中止，但是会重启

        }

    }
}
