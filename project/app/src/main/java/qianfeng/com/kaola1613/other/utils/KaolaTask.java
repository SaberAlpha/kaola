package qianfeng.com.kaola1613.other.utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;

/**
 * 网络请求异步任务
 *
 * Created by liujianping on 2016/10/14.
 */
public class KaolaTask extends AsyncTask<Object, Integer, Object> {

    private IRequest iRequest;

    private IRequestCallback iRequestCallback;

    private IDownloadImage downloadImage;

    private IDownloader iDownloader;

    /**
     * 如果是请求json那么使用这个构造方法
     *
     * @param iRequest
     * @param iRequestCallback
     */
    public KaolaTask(IRequest iRequest, IRequestCallback iRequestCallback) {

        if (iRequest == null || iRequestCallback == null)
        {
            throw new NullPointerException("IRequest and IRequestCallback can not be null ...");
        }
        this.iRequest = iRequest;
        this.iRequestCallback = iRequestCallback;
    }

    public KaolaTask(IDownloader downloader, IRequestCallback iRequestCallback)
    {
        if (downloader == null || iRequestCallback == null)
        {
            throw new NullPointerException("IDownloader and IRequestCallback can not be null ...");
        }
        this.iDownloader = downloader;
        this.iRequestCallback = iRequestCallback;
    }

    /**
     * 如果下载图片使用这个构造方法
     *
     * @param downloadImage
     * @param iRequestCallback
     */
    public KaolaTask(IDownloadImage downloadImage, IRequestCallback iRequestCallback)
    {
        if (downloadImage == null || iRequestCallback == null)
        {
            throw new NullPointerException("IDownloadImage and IRequestCallback can not be null ...");
        }
        this.downloadImage = downloadImage;
        this.iRequestCallback = iRequestCallback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * 执行耗时操作
     *
     * @param params
     * @return
     */
    @Override
    protected Object doInBackground(Object[] params) {
        //如果是下载图片的操作，那么返回downloadBitmap的结果
        if (downloadImage != null)
        {
            return downloadImage.downloadBitmap();
        }

        //如果是下载文件的操作，那么返回downloadFile的结果
        if (iDownloader != null)
        {
            return iDownloader.downloadFile();
        }

        //如果是直接传进来的值，我们认为是本地的json,可以直接解析
        if (params != null && params.length > 0)
        {
            //返回解析后的结果
            return iRequest.parseResult(params[0]);
        }

        //执行请求
        Object result = iRequest.doRequest();

        //如果为空，表示请求失败，直接回调结果，不用解析了
        if (result == null)
        {
            return result;
        }
        //返回解析后的结果
        return iRequest.parseResult(result);
    }

    /**
     * 把结果提交给主线程里去执行
     *
     * @param object
     */
    @Override
    protected void onPostExecute(Object object) {
        //如果请求失败，那么结果为空
        if (object == null)
        {
            //回调错误方法
            iRequestCallback.onError();
        }
        else
        {
            //请求成功并返回结果
            iRequestCallback.onSuccess(object);
        }
    }

    /**
     * 更新进度
     *
     * @param values
     */
    @Override
    protected void onProgressUpdate(Integer[] values) {
        super.onProgressUpdate(values);
    }



    /**
     * 请求接口
     */
    public interface IRequest
    {
        /**
         * 具体的请求操作
         *
         * @return 请求结果
         */
        Object doRequest();

        /**
         * 解析结果
         *
         * @param obj json数据
         * @return
         */
        Object parseResult(Object obj);
    }

    /**
     * 下载图片的接口
     */
    public interface IDownloadImage
    {
        /**
         * 具体的下载操作
         *
         * @return
         */
        Bitmap downloadBitmap();
    }

    /**
     * 文件下载接口
     */
    public interface IDownloader
    {
        /**
         * 下载操作
         *
         * @return
         */
        Object downloadFile();


    }

    /**
     * 更新进度接口
     */
    public interface IProgress
    {
        /**
         * 更新进度
         * @param progress
         */
        void updataProgress(int progress);
    }

    /**
     * 请求结果回调接口
     */
    public interface IRequestCallback
    {
        /**
         * 请求成功
         * @param object 请求结果
         */
        void onSuccess(Object object);

        /**
         * 请求失败
         */
        void onError();
    }
}
