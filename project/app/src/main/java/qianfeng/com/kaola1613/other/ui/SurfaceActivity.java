package qianfeng.com.kaola1613.other.ui;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

import qianfeng.com.kaola1613.R;
import qianfeng.com.kaola1613.other.utils.LogUtil;

public class SurfaceActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    private SurfaceView surfaceView;

    private MediaPlayer mediaPlayer;

    private float fromX, fromY;

    //允许横向或纵向滑动的操作误差
    private final float slop = 100;

    /**
     * 音频管理
     */
    private AudioManager audioManager;

    /**
     * 手势的方向
     */
    enum Orientation
    {
        Top,

        Bottom,

        Left,

        Right,

        //未知方向
        Unknown;

        float distance;

        public float getDistance() {
            return distance;
        }

        public void setDistance(float distance) {
            this.distance = distance;
        }
    }

    private Orientation getOrientation(float endX, float endY)
    {
        Orientation orientation = Orientation.Unknown;
        //如果是向上
        if (Math.abs(fromX - endX) < slop && endY < fromY
                && Math.abs(endY - fromY) > slop)
        {
            //设置方向和滑动的距离
            orientation = Orientation.Top;
            orientation.setDistance(Math.abs(endY - fromY));
        }
        //向下
        else if (Math.abs(fromX - endX) < slop && endY > fromY
                && Math.abs(endY - fromY) > slop)
        {
            //设置方向和滑动的距离
            orientation = Orientation.Bottom;
            orientation.setDistance(Math.abs(endY - fromY));
        }
        //向左
        else if (Math.abs(fromY - endY) < slop && endX < fromX
                && Math.abs(endX - fromX) > slop)
        {
            //设置方向和滑动的距离
            orientation = Orientation.Left;
            orientation.setDistance(Math.abs(endX - fromX));
        }
        //向右
        else if (Math.abs(fromY - endY) < slop && endX > fromX
                && Math.abs(endX - fromX) > slop)
        {
            //设置方向和滑动的距离
            orientation = Orientation.Right;
            orientation.setDistance(Math.abs(endX - fromX));
        }

        return orientation;
    }

    private void toChangeVoice(Orientation orientation, float distance)
    {
        //获取当前媒体音量
        int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        int v = (int)(distance / slop);

        //如果是向下滑动，表示要减小音量
        if (orientation == Orientation.Bottom)
        {
            volume = volume - v * 10;
        }
        else
        {
            volume = volume + v * 10;
        }

        //设置音量(类型，音量大小，播放声音)
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_PLAY_SOUND);
    }

    private void toChangePlayProgress(float percent)
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface);
        //获取音频管理服务
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        surfaceView = (SurfaceView) findViewById(R.id.surface_sv);

        //获取SurfaceHolder
        SurfaceHolder holder = surfaceView.getHolder();

        holder.addCallback(this);

        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash_1);
        try {
            mediaPlayer.setDataSource(this, uri);
            //异步缓冲
            mediaPlayer.prepareAsync();


        } catch (IOException e) {
            e.printStackTrace();
        }

        //设置控件的触摸监听器
        surfaceView.setOnTouchListener(new View.OnTouchListener() {

            /**
             * ACTION_DOWN 和ACTION_UP只会执行一次，ACTION_MOVE可以执行n次
             *
             * @param view
             * @param event
             * @return
             */
            @Override
            public boolean onTouch(View view, MotionEvent event) {


                int action = event.getAction();
                //任何事件都是从按下开始
                if (action == MotionEvent.ACTION_DOWN)
                {
                    //记录按下的坐标
                    fromX = event.getX();
                    fromY = event.getY();

                    //按下事件必须返回true,否则其他的事件都不会执行
                    return true;
                }
                else if (action == MotionEvent.ACTION_MOVE)
                {

                    //记录滑动时刻的坐标
                    float endX = event.getX();
                    float endY = event.getY();

                    //获取滑动的方向
                    Orientation orientation = getOrientation(endX, endY);

                    float distance = orientation.getDistance();

                    if (orientation == Orientation.Top || orientation == Orientation.Bottom)
                    {
                        toChangeVoice(orientation, distance);
                    }
                    else if (orientation == Orientation.Left || orientation == Orientation.Right)
                    {
                        toChangePlayProgress(distance);
                    }
                    else
                    {
                        Toast.makeText(SurfaceActivity.this, "你要干 啥？", Toast.LENGTH_SHORT).show();
                    }

                }

                return false;
            }
        });
    }



    //SurfaceView在创建时的回调
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //设置显示内容的控件
        mediaPlayer.setDisplay(surfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    //SurfaceView在销毁时的回调
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        //停止解码
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        //清除绘制缓存
        surfaceView.destroyDrawingCache();
    }

    /**
     * 切换屏幕
     * @param btn
     */
    public void changeScreen(View btn)
    {
        //获取屏幕的方向
        int orientation = getRequestedOrientation();
        //如果是竖屏，那么切换成横屏
        if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        {
            //设置屏幕方向
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        //如果是横屏，那么切换到竖屏
        else
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    /**
     * 如果在清单文件里配置了Activity的属性 android:configChanges="orientation|screenSize"
     *
     * 那么Activity在切换屏幕的时候不会调用生命周期方法，只会调用当前的onConfigurationChanged
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        LogUtil.d("onConfigurationChanged = " + newConfig.orientation);

    }
}
