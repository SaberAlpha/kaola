package qianfeng.com.kaola1613.other.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created by liujianping on 2016/10/25.
 */
public class MediaPlayerUtil {

    private static Context mContext;

    private MediaPlayer mediaPlayer;

    private PlayMode mode = PlayMode.CUSTOM;

    //播放列表
    private List<String> playList;

    private int playIndex;


    private static MediaPlayerUtil instance;


    private Status status = Status.Reset;

    private String playingUrl;

    /**
     * 初始化， 执行一次就够了
     * @param context
     */
    public static void init(Context context)
    {
        mContext = context;
    }

    private MediaPlayerUtil()
    {
        initPlayer();
    }

    private ICompletionListener completionListener;

    public void setICompletionListener(ICompletionListener listener)
    {
        completionListener = listener;
    }

    public interface ICompletionListener
    {
        void onCompletion(MediaPlayer mediaPlayer, String url);
    }

    /**
     * 初始化操作
     */
    private void initPlayer() {

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

                //单曲循环
                if (mode == PlayMode.SINGLE_LOOP)
                {
                    mediaPlayer.start();
                }
                //列表循环
                else if (mode == PlayMode.LIST_LOOP)
                {
                    playIndex++;

                    String url = playList.get(playIndex);

                    next(url);
                }
                //随机模式
                else if (mode == PlayMode.RANDOM)
                {
                    int size = playList.size();

                    Random random = new Random();
                    int r = random.nextInt(size);

                    String url = playList.get(r);

                    next(url);
                }
                else {
                    if (completionListener != null)
                    {
                        mediaPlayer.stop();
                        completionListener.onCompletion(mediaPlayer, playingUrl);
                    }
                }
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                status = Status.Playing;
                mediaPlayer.start();
            }
        });
    }

    public static MediaPlayerUtil getInstance()
    {
        if (instance == null)
        {
            synchronized (MediaPlayerUtil.class)
            {
                if (instance == null)
                {
                    instance = new MediaPlayerUtil();
                }
            }
        }

        return instance;
    }

    public void play(String url)
    {
        if (mediaPlayer == null)
        {
            initPlayer();
        }

        Uri uri = Uri.parse(url);
        try {
            //保存正在播放的url
            playingUrl = url;
            mediaPlayer.reset();
            status = Status.Reset;

            mediaPlayer.setDataSource(mContext,uri);
            mediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 继续播放
     */
    public void play()
    {
        if (!mediaPlayer.isPlaying())
        {
            mediaPlayer.start();
        }
    }

    public void next(String url)
    {
        play(url);
    }

    public void pause()
    {
        if (mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
            status = Status.Pause;
        }
    }

    public void stop()
    {
        if (mediaPlayer == null)
        {
            return;
        }
        if (mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            status = Status.Stop;
        }
        mediaPlayer.release();
        mediaPlayer = null;
    }

    public Status getStatus()
    {
        return status;
    }

    /**
     * 设置播放列表
     * @param urlList
     */
    public void setPlayerList(List<String> urlList)
    {
        playList = urlList;
    }

    /**
     * 设置播放模式
     * @param mode
     */
    public void setPlayMode(PlayMode mode)
    {
        this.mode = mode;
    }

    /**
     * 播放模式
     */
    public enum PlayMode
    {
        CUSTOM("自定义模式"),
        /**
         * 列表循环
         */
        LIST_LOOP("列表循环"),

        /**
         * 单曲循环
         */
        SINGLE_LOOP("单曲循环"),

        /**
         * 随机播放
         */
        RANDOM("随机播放");

        private String name;

        PlayMode(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public enum Status
    {
        Playing,

        Pause,

        Stop,

        Reset
    }


}
