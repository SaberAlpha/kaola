package qianfeng.com.kaola1613.other.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.sharesdk.onekeyshare.OnekeyShare;
import qianfeng.com.kaola1613.R;
import qianfeng.com.kaola1613.discover.entity.Recommend2;
import qianfeng.com.kaola1613.offline.db.DownloadManager;
import qianfeng.com.kaola1613.offline.entity.DownloadEntity;
import qianfeng.com.kaola1613.other.utils.FileUtil;
import qianfeng.com.kaola1613.other.utils.HttpUtil;
import qianfeng.com.kaola1613.other.utils.KaolaTask;
import qianfeng.com.kaola1613.other.utils.LogUtil;
import qianfeng.com.kaola1613.other.widget.BlurFrameLayout;
import qianfeng.com.kaola1613.other.widget.StackView;

/**
 *
 */
public class Player1Activity extends AppCompatActivity {

    public static final String TAG_RECOMMED2= "tag_recommed2";

    private Recommend2 recommend2;

    private ImageView ivTop, ivCenter, ivbackground, ivPlayBtn;

    private MediaPlayer mediaPlayer;

    private boolean isPause;

    private TextView tvCurrTime, tvAllTime;

    private SeekBar seekBar;

    private PlayMode mode = PlayMode.SINGLE_LOOP;

    private BlurFrameLayout blurFrameLayout;

    private KaolaTask blurTask;

    private StackView stackView;

    private ImageView ivNext;

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {

            if (mediaPlayer == null)
            {
                return;
            }
            //获取当前的播放时间
            int currentPosition = mediaPlayer.getCurrentPosition();

            int duration = mediaPlayer.getDuration();
            LogUtil.d("currentPosition = " + currentPosition);

            String currTime = getTime(currentPosition);

            //更新时间
            tvCurrTime.setText(currTime);

            //进度条的最大长度
            int max = seekBar.getMax();

            int progress = max * currentPosition / duration;

            //更新进度
            seekBar.setProgress(progress);

            handler.sendEmptyMessageDelayed(0, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player1);

//        blurFrameLayout = (BlurFrameLayout) findViewById(R.id.player1_bfl);

        recommend2 = getIntent().getParcelableExtra(TAG_RECOMMED2);

        ivTop = (ImageView) findViewById(R.id.player1_top_iv);
        ivCenter = (ImageView) findViewById(R.id.player1_center_iv);
        ivbackground = (ImageView) findViewById(R.id.player1_bg_iv);

        ivPlayBtn = (ImageView) findViewById(R.id.play_btn);

        tvAllTime = (TextView) findViewById(R.id.player1_allTime);
        tvCurrTime = (TextView) findViewById(R.id.player1_currentTime);

        seekBar = (SeekBar) findViewById(R.id.player1_seekBar);
        stackView = (StackView) findViewById(R.id.player1_sv);
        ivNext = (ImageView) findViewById(R.id.player1_next_iv);

        findViewById(R.id.player1_download_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                KaolaTask task = new KaolaTask(new KaolaTask.IDownloader() {
                    @Override
                    public Object downloadFile() {
                        String url = recommend2.getMp3PlayUrl();
                        return HttpUtil.downloadFile(url, FileUtil.DIR_AUDIO, url.hashCode() + "", null);
                    }
                }, new KaolaTask.IRequestCallback() {
                    @Override
                    public void onSuccess(Object object) {
                        File mp3 = (File) object;
                        DownloadEntity entity = new DownloadEntity();
                        entity.setImage(recommend2.getPic());
                        entity.setAlbumName(recommend2.getAlbumName());
                        entity.setCount(1);
                        long length = mp3.length();
                        entity.setSize(FileUtil.getFileSize(length));
                        DownloadManager.getInstance().insert(entity);
                    }

                    @Override
                    public void onError() {

                    }
                });

                task.execute();
            }
        });

        findViewById(R.id.player1_comment_iv)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Player1Activity.this, CommentActivity.class);

                        intent.putExtra(TAG_RECOMMED2, recommend2);
                        startActivity(intent);
                    }
                });

        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stackView.showAnim();
            }
        });

        findViewById(R.id.player1_sub_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Player1Activity.this, SurfaceActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.player1_share_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OnekeyShare oks = new OnekeyShare();

                //设置标题
                oks.setTitle(recommend2.getAlbumName());
                //标题的网络链接
                oks.setTitleUrl(recommend2.getMp3PlayUrl());
                //分享的文本
                oks.setText(recommend2.getRname());

                //设置分享的网络图片
                oks.setImageUrl(recommend2.getPic());

                oks.setComment(recommend2.getRvalue());
                //启动分享页面
                oks.show(Player1Activity.this);


            }
        });

        //播放器组件
        mediaPlayer = new MediaPlayer();

        //设置准备监听
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                //开始播放
                mediaPlayer.start();
                //延时一秒钟发送一个空的消息
                handler.sendEmptyMessageDelayed(0, 1000);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //如果是单曲循环
                if (mode == PlayMode.SINGLE_LOOP)
                {
                    mediaPlayer.start();
                }
                //如果是随机播放
                else if (mode == PlayMode.RANDOM)
                {

                }
            }
        });

        //缓冲监听
        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int progress) {
                //设置seekBar第二层进度，通常用来表示视频或者音频缓冲进度
                seekBar.setSecondaryProgress(progress);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();

                int duration = mediaPlayer.getDuration();

                int position = duration * progress / 100;
                //跳到指定的时间点继续播放
                mediaPlayer.seekTo(position);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        Picasso.with(Player1Activity.this)
                .load(recommend2.getPic())
                .placeholder(R.mipmap.ic_default)
                .error(R.mipmap.no_net_error_chat_icon)
//                        .resize(centerParams.width, centerParams.height)
                .into(ivCenter);
//
//        blurFrameLayout.setRadius(25);


        ivPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause(recommend2.getMp3PlayUrl());
            }
        });

        blurTask = new KaolaTask(new KaolaTask.IDownloadImage() {
            @Override
            public Bitmap downloadBitmap() {
                return HttpUtil.getBitmap(recommend2.getPic());
            }
        }, new KaolaTask.IRequestCallback() {
            @Override
            public void onSuccess(Object object) {
                Bitmap source = (Bitmap) object;

                int widthPixels = getResources().getDisplayMetrics().widthPixels;
                int heightPixels = getResources().getDisplayMetrics().heightPixels;

                int sHeight = source.getHeight();
                int sWidth = source.getWidth();

                int ssHeight = sHeight / 2;
                int ssWidth = ssHeight * widthPixels / heightPixels;

                int fromX = (sWidth - ssWidth) / 2;
                int fromY = (sHeight - ssHeight) / 2;

                Bitmap bitmap1 = Bitmap.createBitmap(source, fromX, fromY, ssWidth, ssHeight);

                //显示模糊图片
//                BitmapUtil.blur(Player1Activity.this, bitmap1, ivbackground, 25);

                source.recycle();
            }

            @Override
            public void onError() {
                Toast.makeText(Player1Activity.this, "请求图片失败", Toast.LENGTH_SHORT).show();
            }
        });

        blurTask.execute();

    }

    private void playMp3(String url)
    {
        mediaPlayer.reset();
        //网络路径转本地路径
        Uri uri = Uri.parse(url);
        try {
            //设置播放路径
            mediaPlayer.setDataSource(this, uri);
            //准备播放
            mediaPlayer.prepare();

            //获取总共的播放时长,单位是毫秒
            int duration = mediaPlayer.getDuration();
            LogUtil.d("duration = " + duration);
            String allTime = getTime(duration);

            tvAllTime.setText(allTime);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getTime(int duration) {
        Date date = new Date(duration);

        //12:34
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");

        return dateFormat.format(date);
    }

    private void pause(String url)
    {
        //判断是否正在播放
        if (mediaPlayer.isPlaying())
        {
            //暂停
            mediaPlayer.pause();
            isPause = true;
            ivPlayBtn.setImageResource(R.mipmap.anchor_edit_play);
        }

        else
        {
            if (isPause)
            {
                //如果是暂时状态，那就是继续播放
                mediaPlayer.start();
            }
            else
            {
                playMp3(url);
            }
            isPause = false;
            ivPlayBtn.setImageResource(R.mipmap.anchor_edit_play_pause);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //停止并播放
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;

        handler.removeMessages(0);
        handler = null;

        blurTask.cancel(true);
    }

    /**
     * 播放模式
     */
    public enum PlayMode
    {
        /**
         * 列表循环
         */
        LIST_LOOP,

        /**
         * 单曲循环
         */
        SINGLE_LOOP,

        /**
         * 随机播放
         */
        RANDOM
    }



}
