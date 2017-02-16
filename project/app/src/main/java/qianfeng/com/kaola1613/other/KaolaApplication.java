package qianfeng.com.kaola1613.other;

import android.app.Application;
import android.graphics.Bitmap;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;
import qianfeng.com.kaola1613.offline.db.DownloadManager;
import qianfeng.com.kaola1613.other.utils.FileUtil;
import qianfeng.com.kaola1613.other.utils.MediaPlayerUtil;

/**
 * 一般用来给应用初始化全局的数据
 *
 * Created by liujianping on 2016/10/12.
 */
public class KaolaApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        FileUtil.init(getApplicationContext());

        //配置 picasso
        Picasso picasso = new Picasso.Builder(this)
                .defaultBitmapConfig(Bitmap.Config.RGB_565)//默认图片质量,选择RGB_565内存直接减半
                .memoryCache(new LruCache(20 << 20))//位移 20 * 1024 * 1024
                .downloader(new OkHttpDownloader(FileUtil.DIR_IMAGE))//指定源图存储位置
                .build();

        //设置单例模式
        Picasso.setSingletonInstance(picasso);

        //数据库初始化
        DownloadManager.init(this);

        //获取一个默认的配置
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        //初始化操作
        ImageLoader.getInstance().init(configuration);

        //播放器组件初始化操作
        MediaPlayerUtil.init(this);

        //在ShareSDK任何操作之前都要初始化
        ShareSDK.initSDK(this);

        //极光初始化
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);

        //百度地图的初始化
        SDKInitializer.initialize(getApplicationContext());

    }
}
