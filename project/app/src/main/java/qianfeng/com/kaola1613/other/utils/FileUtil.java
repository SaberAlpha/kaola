package qianfeng.com.kaola1613.other.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

/**
 * 文件工具类
 * <p/>
 * Created by liujianping on 2016/10/10.
 */
public class FileUtil {

    private static Context mContext;

    public static void init(Context context)
    {
        mContext = context;
    }

    /**
     * 项目根目录
     */
    public static final String APP_DIR = "KaoLa_1613";

    /**
     * 获取 KaoLa_1613/image 目录
     */
    public static final File DIR_IMAGE = getDir("image");

    /**
     * 获取 KaoLa_1613/image_transformation 目录
     */
    public static final File DIR_IMAGE_TRANSFORMATION = getDir("image_transformation");

    /**
     * 音频目录
     */
    public static final File DIR_AUDIO = getDir("audio");

    /**
     * 视频目录
     */
    public static final File DIR_VIDEO = getDir("video");

    /**
     * 直播
     */
    public static final File DIR_M3U8 = getDir("m3u8");

    /**
    * apk
    */
    public static final File DIR_APK = getDir("apk");



    /**
     * 获取sd卡根目录
     *
     * @return
     */
    public static File getSdCardDir() {
        //获取sd卡的状态
        String state = Environment.getExternalStorageState();
        //如果sd卡已挂载
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            //获取根目录
            File dir = Environment.getExternalStorageDirectory();

            return dir;
        }

        LogUtil.e("没有sd卡");
        return null;
    }

    /**
     * 获取sd卡下 KaoLa_1613 目录
     *
     * @return
     */
    public static File getAppDir() {
        File dir = new File(getSdCardDir(), APP_DIR);

        if (!dir.exists()) {
            dir.mkdir();
        }

        return dir;
    }

    /**
     * 获取 KaoLa_1613的子目录
     *
     * @param dirName
     * @return
     */
    public static File getDir(String dirName) {
        File dir = new File(getAppDir(), dirName);

        if (!dir.exists()) {
            dir.mkdir();
        }

        return dir;
    }

    /**
     * 获取指定的SharedPreferences文件里指定标签的内容
     *
     * @param fileName 文件名
     * @param flagName 标签名
     * @return 指定的json
     */
    public static String getCacheFromPreference(String fileName, String flagName) {
        SharedPreferences preferences = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);

        String value = preferences.getString(flagName, "");

        return value;
    }

    /**
     * 获取指定的SharedPreferences文件里指定标签内容保存的时间
     *
     * @param fileName 文件名
     * @param flagName 标签名
     * @return 指定的json
     */
    public static long getCacheTimeFromPreference(String fileName, String flagName) {
        SharedPreferences preferences = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);

        long value = preferences.getLong(flagName, 0);

        return value;
    }

    /**
     * 保存json到指定的SharedPreferences 文件中
     *
     * @param fileName 文件名
     * @param flagName 标签名
     * @param json     要保存的内容
     */
    public static void saveCacheToPreference(String fileName, String flagName, String json) {
        SharedPreferences preferences = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(flagName, json);

        editor.commit();
    }

    /**
     * 保存请求json服务器的时间到指定的SharedPreferences 文件中
     *
     * @param fileName 文件名
     * @param flagName 标签名
     * @param time     服务器时间
     */
    public static void saveCacheTimeToPreference(String fileName, String flagName, long time) {
        SharedPreferences preferences = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();

        editor.putLong(flagName, time);

        editor.commit();
    }

    public static String getFileSize(long length)
    {

        if (length > 1024 && length / 1024 < 1024)
        {
            return length / 1024 + "KB";
        }
        else if (length / 1024 / 1024 > 1 && length / 1024 / 1024 < 1024)
        {
            return length / 1024 / 1024 + "MB";
        }

        return length + "B";

    }

    /**
     * 删除缓存文件
     *
     * @param dir
     * @param rename
     */
    public static void deleteCacheFile(File dir, String rename)
    {
        File target = new File(dir, rename);

        if (target.exists())
        {
            target.delete();
        }
    }

    /**
     * 清空缓存
     *
     * @param dir
     */
    public static void clearCacheFromDir(File dir)
    {
        if (!dir.exists())
        {
            return;
        }
        //清空该目录下面的所有子文件
        File[] files = dir.listFiles();

        for (int i = 0; i < files.length; i++) {
            files[i].delete();
        }
    }

    /**
     * 进入安装页面
     *
     * @param apk apk的存放目录
     */
    public static void installApk(Context context, File apk)
    {
        Uri uri = Uri.fromFile(apk);
        // 核心是下面几句代码
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
