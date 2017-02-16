package qianfeng.com.kaola1613.other.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import qianfeng.com.kaola1613.R;
import qianfeng.com.kaola1613.other.utils.CacheUtil;
import qianfeng.com.kaola1613.other.utils.FileUtil;
import qianfeng.com.kaola1613.other.utils.HttpUtil;
import qianfeng.com.kaola1613.other.utils.KaolaTask;
import qianfeng.com.kaola1613.other.utils.OtherHttpUtil;

public class BannerActivity extends AppCompatActivity {

    private ImageView imageView;

    //广告显示的时间，默认3秒
    private int showTime = 3;

    private Timer timer = new Timer();

    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            showTime--;
            //倒计时三秒后自动跳转
            if (showTime == 0) {
                toHome(null);
            }
        }
    };

    private KaolaTask jsonTask, imageTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        imageView = (ImageView) findViewById(R.id.banner_top_iv);

        //获取上一次保存的json
        String json = FileUtil.getCacheFromPreference(CacheUtil.CACHE_FILE, CacheUtil.CACHE_FLAG_BANNER);
        //获取上一次保存json的时间
        long serverTime = FileUtil.getCacheTimeFromPreference( CacheUtil.CACHE_FILE, CacheUtil.CACHE_FLAG_BANNER_time);

        //和现在的时间对比，如果超出半小时就请求网络，否则只使用缓存的json
        long currTime = System.currentTimeMillis();
        long time = (currTime - serverTime) / 1000 / 60;

        if ("".equals(json) || time > 5) {
            //表示没有数据，需要请求服务端
            jsonTask = new KaolaTask(new KaolaTask.IRequest() {
                @Override
                public Object doRequest() {
                    return HttpUtil.doGet(OtherHttpUtil.URL_BANNER);
                }

                @Override
                public Object parseResult(Object obj) {
                    String json = (String) obj;
                    return parseJson(json);
                }
            }, new KaolaTask.IRequestCallback() {
                @Override
                public void onSuccess(Object object) {

                    final String img = (String) object;

                    imageTask = new KaolaTask(new KaolaTask.IDownloadImage() {
                        @Override
                        public Bitmap downloadBitmap() {
                            return HttpUtil.getBitmap(img);
                        }
                    }, new KaolaTask.IRequestCallback() {
                        @Override
                        public void onSuccess(Object object) {
                            Bitmap bitmap = (Bitmap) object;
                            imageView.setImageBitmap(bitmap);
                            //请求成功了，三秒后自动跳转到Home
                            startTimer();
                        }

                        @Override
                        public void onError() {
                            Toast.makeText(BannerActivity.this, "请求广告图片失败", Toast.LENGTH_SHORT).show();
                            startTimer();
                        }
                    });

                    imageTask.execute();
                }

                @Override
                public void onError() {
                    Toast.makeText(BannerActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                }
            });
            jsonTask.execute();


        } else {
            parseJson(json);
        }
    }

    private String parseJson(String str) {
        try {
            JSONObject root = new JSONObject(str);

            String code = root.getString("code");
            String message = root.getString("message");

            if ("10000".equals(code) &&
                    "success".equals(message)) {
                //请求成功了，保存json到缓存
                FileUtil.saveCacheToPreference( CacheUtil.CACHE_FILE, CacheUtil.CACHE_FLAG_BANNER, str);
                long time = root.getLong("serverTime");
                FileUtil.saveCacheTimeToPreference(CacheUtil.CACHE_FILE, CacheUtil.CACHE_FLAG_BANNER_time, time);

                JSONObject result = root.getJSONObject("result");

                //广告图片地址
                final String img = result.getString("img");

                showTime = result.getInt("showTime");

                return img;
            }
        } catch (JSONException e) {
            //请求失败了，三秒后自动跳转到Home
            startTimer();
            e.printStackTrace();
        }

        return null;
    }

    private void startTimer() {
        if (timer != null) {
            timer.schedule(task, 0, 3000);
        }
    }

    /**
     * 点击跳过的操作
     *
     * @param view
     */
    public void toHome(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        task.cancel();
        task = null;
        timer.cancel();
        timer = null;

        if (jsonTask != null)
        {
            jsonTask.cancel(true);
            jsonTask = null;
        }

        if (imageTask != null)
        {
            imageTask.cancel(true);
            imageTask = null;
        }
    }
}
