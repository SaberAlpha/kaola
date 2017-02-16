package qianfeng.com.kaola1613.other.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import cn.sharesdk.onekeyshare.OnekeyShare;
import qianfeng.com.kaola1613.R;
import qianfeng.com.kaola1613.other.utils.FileUtil;
import qianfeng.com.kaola1613.other.utils.LogUtil;

public class WebActivity extends AppCompatActivity {

    public static final String EXTRA_URL = "url";

    private ImageView ivBack, ivShare;

    private TextView tvTitle;

    private ProgressBar progressBar;

    private WebView webView;

    private String url;

    private WebViewClient webViewClient = new WebViewClient()
    {
        /**
         * 页面加载完成的时候
         * @param view
         */
        @Override
        public void onPageFinished(WebView view, String url) {
            //获取网页的标题
            String title = view.getTitle();
            tvTitle.setText(title);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);
            //不会跳到浏览器打开网页
            return true;
        }
    };

    private WebChromeClient webChromeClient = new WebChromeClient()
    {

        /**
         * 显示网页加载进度
         * @param view
         * @param newProgress
         */
        @Override
        public void onProgressChanged(WebView view, int newProgress) {

            if (newProgress == 100)
            {
                progressBar.setVisibility(View.INVISIBLE);
            }
            else
            {
                progressBar.setProgress(newProgress);
            }
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            LogUtil.d("web onJsAlert"+view+","+url+","+message+","+result+")");

            result.confirm();

            return true;
        }
    };


    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web);

        ivBack = (ImageView) findViewById(R.id.web_back_iv);
        ivShare = (ImageView) findViewById(R.id.web_share_iv);
        tvTitle = (TextView) findViewById(R.id.web_title_tv);
        progressBar = (ProgressBar) findViewById(R.id.web_pb);
        webView = (WebView) findViewById(R.id.web_content_wv);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onBackPressed();
                finish();
            }
        });

//        url = getIntent().getStringExtra(EXTRA_URL);

        WebSettings settings = webView.getSettings();
        //允许运行js
        settings.setJavaScriptEnabled(true);


        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(webChromeClient);

        url = "file:///android_asset/web.html";
//        url = "file:///android_asset/baidu.html";

//        url = "https://www.baidu.com/";
        webView.loadUrl(url);//加载网页


        //添加供js调用的接口,参数：定义的类对象。对象名
        webView.addJavascriptInterface(new Contact(), "kaola");

        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareByShareSDK();
            }
        });
    }

    /**
     * 用shareSDK去分享
     */
    private void shareByShareSDK()
    {
        //创建一键分享实例
        OnekeyShare oks = new OnekeyShare();

        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        oks.setTitle("用shareSDK去分享");
        //点击标题的跳转
        oks.setTitleUrl("http://www.baidu.com");

        //分享的文本内容
        oks.setText("免费给百度打广告");

        //分享本地图片
        File image = new File(FileUtil.DIR_IMAGE, "-1054128327");
        oks.setImagePath(image.getAbsolutePath());

        //分享网络图片
        oks.setImageUrl("http://demo.mob.com/wiki/wp-content/uploads/2014/09/071-176x300.png");

        //在微信朋友圈分享一个链接
        oks.setUrl("http://demo.mob.com/wiki/wp-content/uploads/2014/09/071-176x300.png");

        //我对这条分享的评论,可以为空
        oks.setComment("分享的评论");

        oks.setSite("分享此内容网站的名称");

        oks.setSiteUrl("分享此内容网站的地址");

        //显示分享的GUI
        oks.show(this);
    }

    @Override
    public void onBackPressed() {
        //如果不是在第一级网页
        if (webView.canGoBack())
        {
            //让它回到上一级
            webView.goBack();
        }
        else
        {
            super.onBackPressed();
        }
    }


    private final class Contact {


        //Html调用此方法传递数据
        public void showMsg(String msg) {

            // 调用JS中的方法
            webView.loadUrl("javascript:startFromAndroid('来啊，互相伤害啊！')");
        }

        public void toast(String str){
            Toast.makeText(WebActivity.this, "aaaaaaaaaaaa  --- " + str, Toast.LENGTH_LONG).show();
        }
    }


}
