package qianfeng.com.kaola1613.discover.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import qianfeng.com.kaola1613.discover.util.DiscoverHttpUtil;
import qianfeng.com.kaola1613.other.utils.HttpUtil;
import qianfeng.com.kaola1613.other.utils.KaolaTask;

/**
 * Created by liujianping on 2016/11/4.
 */
public class SearchActivity extends AppCompatActivity {

    private EditText editText;

    private KaolaTask searchTask;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //关键字
        String key = editText.getText().toString();
        try {
            key = URLEncoder.encode(key, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        final String text = key;
        searchTask = new KaolaTask(new KaolaTask.IRequest() {
            @Override
            public Object doRequest() {
                return HttpUtil.doGet(DiscoverHttpUtil.URL_SEARCH + text);
            }

            @Override
            public Object parseResult(Object obj) {
                return null;
            }
        }, new KaolaTask.IRequestCallback() {
            @Override
            public void onSuccess(Object object) {

            }

            @Override
            public void onError() {

            }
        });

        searchTask.execute();

        //流式布局

    }


}
