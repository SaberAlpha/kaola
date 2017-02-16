package qianfeng.com.kaola1613.other.utils;

import android.media.MediaPlayer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by liujianping on 2016/10/25.
 */
public class ExecutorUtil {

    private ExecutorService service;

    private List<String> playList;

    private static ExecutorUtil instance;

    private ExecutorUtil() {
        playList = new ArrayList<>();

        service = Executors.newFixedThreadPool(3);
    }

    public static ExecutorUtil getInstance() {
        if (instance == null) {
            synchronized (ExecutorUtil.class) {
                if (instance == null) {
                    instance = new ExecutorUtil();
                }
            }
        }

        return instance;
    }

    /**
     * 下载开关
     */
    private boolean needDownload = true;



    private TimerTask task = new TimerTask() {
        @Override
        public void run() {

            synchronized (playList)
            {
                if (!playList.isEmpty() && MediaPlayerUtil.getInstance().getStatus() == MediaPlayerUtil.Status.Playing) {
                    return;
                }
                LogUtil.w("-->通过Timer查看播放列表");
                playFromList();
            }

        }
    };

    private Timer timer = new Timer();

    /**
     * 开始工作
     *
     * @param url
     */
    public void work(String url) {
        //执行下载
        download(url);

        //执行播放
        play();
    }

    /**
     * 关闭操作
     */
    public void shutdown() {
        needDownload = false;
        //关闭线程池
        service.shutdownNow();

        task.cancel();
        task = null;

        timer.cancel();
        timer = null;

        FileUtil.clearCacheFromDir(FileUtil.DIR_AUDIO);
    }

    public void play() {
        Thread thread = new Thread() {
            @Override
            public void run() {

                MediaPlayerUtil.getInstance().setICompletionListener(new MediaPlayerUtil.ICompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer, String playingUrl) {
                        LogUtil.w("--> 播放完成 删除已播放的文件playList.size = "+ playList.size() +" , 继续播放下一条");
                        //删掉播放完成的ts文件
                        FileUtil.deleteCacheFile(FileUtil.DIR_AUDIO, playingUrl.hashCode()+"");

                        playFromList();
                    }
                });

                timer.schedule(task, 0, 10000);
            }
        };

        service.submit(thread);
    }

    private int index;

    private void playFromList() {
        if (playList != null && !playList.isEmpty()) {
            index++;
            String firstUri = playList.remove(0);
            LogUtil.d(">>> 播放第 "+ index +" 个片断 url = " + firstUri);

            MediaPlayerUtil.getInstance().play(firstUri);
        }
        else
        {
            LogUtil.e("播放列表为空!!");
        }
    }

    public void download(final String url) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                //持续下载
                while (needDownload) {
                    //获取第一个m3u8文件里的下载地址(也就是第二个m3u8的文件地址)
                    String m3u8Url = downloadM3u81(url);

                    //拼接ts下载地址
                    List<String> tsUrlList = downloadM3u82(m3u8Url);

                    //删除第二个m3u8文件
                    FileUtil.deleteCacheFile(FileUtil.DIR_M3U8, m3u8Url.hashCode() + "");

                    //下载ts文件
                    downloadTs(tsUrlList);
                }
            }
        };

        //把线程添加到线程池里执行
        service.submit(thread);
    }


    /**
     * 下载m3u8File1文件操作
     *
     * @param url
     */
    public String downloadM3u81(String url) {
        //
        File m3u8File1 = HttpUtil.downloadFile(url, FileUtil.DIR_M3U8, url.hashCode() + "", null);

        FileInputStream fis = null;
        BufferedReader bufferedReader = null;
        try {
            fis = new FileInputStream(m3u8File1);

            bufferedReader = new BufferedReader(new InputStreamReader(fis));

            String read = null;

            //获取以第一个m3u8 文件的url
            while ((read = bufferedReader.readLine()) != null) {
                if (read.startsWith("http")) {
                    return read;
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    /**
     * 下载第二个m3u8文件
     *
     * @param url 从第一个m3u8文件里读取出来的
     * @return
     */
    public List<String> downloadM3u82(String url) {
        File m3u8File1 = HttpUtil.downloadFile(url, FileUtil.DIR_M3U8, url.hashCode() + "", null);

        FileInputStream fis = null;

        BufferedReader bufferedReader = null;

        List<String> list = null;
        try {
            fis = new FileInputStream(m3u8File1);

            bufferedReader = new BufferedReader(new InputStreamReader(fis));

            String read = null;

            list = new ArrayList<>();

            //最后一个"/",加一个"/"转译
            int lastIndex = url.lastIndexOf("/");

            //需要保留"/"
            url = url.substring(0, lastIndex + 1);

            //获取以.ts结尾的字符串
            while ((read = bufferedReader.readLine()) != null) {
                if (read.endsWith(".ts")) {
                    //拼接操作
                    String temp = url;
                    temp += read;

                    list.add(temp);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return list;
    }

    /**
     * 下载直播片断文件
     * @param urlList
     */
    public void downloadTs(final List<String> urlList) {

        for (int i = 0; i < urlList.size(); i++) {

            String url = urlList.get(i);

            File file = HttpUtil.downloadFile(url, FileUtil.DIR_AUDIO, url.hashCode() + "", null);
            //添加文件地址（也就是播放路径）到播放列表里面

            String path = file.getAbsolutePath();

            synchronized (playList)
            {
                if (!playList.contains(path))
                {
                    playList.add(path);

                    LogUtil.e("下载ts文件成功 >>> playList.size =  "+ playList.size());
                }
            }

        }

    }

}
