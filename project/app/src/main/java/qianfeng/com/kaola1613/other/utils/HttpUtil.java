package qianfeng.com.kaola1613.other.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 网络请求工具类
 * <p/>
 * Created by liujianping on 2016/10/10.
 */
public class HttpUtil {


    private static final String TAG = "HttpUtil";

    public static String doPost(String httpUrl, Map<String, String> params)
    {

        //content=wwwww&revieweruid=2754846&resourcetype=1&reviewername=QianFengLaoLiu&commenttype=0&resourceid=1000002787562&

        //在连接之前先把map转化为字符串
        if (httpUrl == null || params == null)
        {
            throw new NullPointerException("httpUrl or params can not be null.");
        }

        Set<Map.Entry<String, String>> entries = params.entrySet();
        //获取迭代器
        Iterator<Map.Entry<String, String>> iterator = entries.iterator();
        StringBuffer buffer = new StringBuffer();
        //开始迭代
        while (iterator.hasNext())
        {
            Map.Entry<String, String> entry = iterator.next();

            String key = entry.getKey();
            buffer.append(key);
            buffer.append("=");
            String value = entry.getValue();
            buffer.append(value);
            buffer.append("&");
        }
        //我们要写入到服务端的参数字符串
        String paramsString = buffer.toString();

        HttpURLConnection conn = null;
        OutputStream os = null;
        InputStream is = null;
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(httpUrl);

            conn = (HttpURLConnection) url.openConnection();
            //设置POST请求方式
            conn.setRequestMethod("POST");

            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            //POST请求需要写入数据
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.connect();

            //在获取返回码之前先写入参数
            os = conn.getOutputStream();
            //把字符串转化成字节数组写到服务端
            os.write(paramsString.getBytes());

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                //获取输入流
                is = conn.getInputStream();
                //字节流转换成字符流
                reader = new InputStreamReader(is);
                //转换缓冲流
                bufferedReader = new BufferedReader(reader);

                String read = null;
                //用来接收读取的结果
                StringBuffer resultBuffer = new StringBuffer();

                while ((read = bufferedReader.readLine()) != null) {
                    //每读取一行，拼接到结果集里
                    resultBuffer.append(read);
                }

                String result = resultBuffer.toString();
                LogUtil.w("请求成功 result = " + result);

                return result;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //如果is!=null表示请求成功了，需要关闭流
            if (is != null) {
                try {
                    is.close();
                    reader.close();
                    bufferedReader.close();
                    conn.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (os != null)
            {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        LogUtil.e("请求失败了");
        return null;

    }


    /**
     * GET请求
     *
     * @param httpUrl 请求地址
     * @return 请求结果
     */
    public static String doGet(String httpUrl) {
        LogUtil.d("doGet httpUrl = " + httpUrl);
        InputStream is = null;
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(httpUrl);

            //打开链接
            conn = (HttpURLConnection) url.openConnection();
            //GET请求方式
            conn.setRequestMethod("GET");//默认就是GET,也可以不用设置
            //连接超时时长
            conn.setConnectTimeout(5000);
            //读取超时时长
            conn.setReadTimeout(5000);
            //开始连接
            conn.connect();
            //获取返回码
            int responseCode = conn.getResponseCode();
            //如果==200表示请求成功
            if (responseCode == HttpURLConnection.HTTP_OK) {
                //获取输入流
                is = conn.getInputStream();
                //字节流转换成字符流
                reader = new InputStreamReader(is);
                //转换缓冲流
                bufferedReader = new BufferedReader(reader);

                String read = null;
                //用来接收读取的结果
                StringBuffer resultBuffer = new StringBuffer();

                while ((read = bufferedReader.readLine()) != null) {
                    //每读取一行，拼接到结果集里
                    resultBuffer.append(read);
                }

                String result = resultBuffer.toString();
                LogUtil.w("请求成功 result = " + result);

                return result;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //如果is!=null表示请求成功了，需要关闭流
            if (is != null) {
                try {
                    is.close();
                    reader.close();
                    bufferedReader.close();
                    conn.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        LogUtil.e("请求失败了");
        return null;
    }

    /**
     * 请求图片
     *
     * @param httpUrl 图片地址
     * @return Bitmap
     */
    public static Bitmap getBitmap(String httpUrl) {

        InputStream is = null;
        HttpURLConnection conn = null;
        //文件输出流用来保存图片
        FileOutputStream fos = null;

        String rename = "" + httpUrl.hashCode();

        File image = new File(FileUtil.DIR_IMAGE, rename);
        //如果图片已经存在
        if (image.exists())
        {
            //返回文件所在路径解析成Bitmap
            return BitmapFactory.decodeFile(image.getAbsolutePath());
        }

        try {
            URL url = new URL(httpUrl);

            conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            conn.connect();

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                is = conn.getInputStream();

                fos = new FileOutputStream(image);

                //用输入流直接解析成图片
                Bitmap bitmap = BitmapFactory.decodeStream(is);

                //保存图片(图片类型，图片质量，输出流)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                LogUtil.w("图片请求成功");

                return bitmap;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (is != null)
            {
                try {
                    is.close();
                    conn.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        LogUtil.e("图片请求失败了");
        return null;
    }

    public static File downloadFile(String httpUrl, File dir, String rename, KaolaTask.IProgress iProgress)
    {
//        LogUtil.w("下载文件 httpUrl = " + httpUrl);
        File file = new File(dir, rename);

        if (file.exists())
        {
//            LogUtil.w("文件已经存在");
            return file;
        }
        InputStream is = null;
        FileOutputStream fos = null;

        HttpURLConnection conn = null;
        try {
            URL url = new URL(httpUrl);

            //打开链接
            conn = (HttpURLConnection) url.openConnection();
            //GET请求方式
            conn.setRequestMethod("GET");//默认就是GET,也可以不用设置
            //连接超时时长
            conn.setConnectTimeout(5000);
            //读取超时时长
            conn.setReadTimeout(5000);
            //开始连接
            conn.connect();
            //获取返回码
            int responseCode = conn.getResponseCode();
            //如果==200表示请求成功
            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                is = conn.getInputStream();
                fos = new FileOutputStream(file);
                //获取文件大小
                long contentLength = conn.getContentLength();

                byte[] buffer = new byte[1024 * 100];

                int read = -1;
                long download = 0;
                while ((read = is.read(buffer)) != -1)
                {
                    fos.write(buffer, 0, read);

                    fos.flush();
                    download += read;

                    int progress = (int)(100 * download / contentLength);

                    if (iProgress != null)
                    {
                        iProgress.updataProgress(progress);
                    }
                    LogUtil.d("文件下载中 progress = " + progress);
                }

//                LogUtil.w("下载文件成功了 file >> " + file.getAbsolutePath());
                return file;
            }
        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //如果is!=null表示请求成功了，需要关闭流
            if (is != null) {
                try {
                    is.close();
                    fos.close();
                    conn.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        LogUtil.e("文件下载失败了");
        return null;
    }
}
