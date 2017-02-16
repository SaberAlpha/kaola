package qianfeng.com.kaola1613.other.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import qianfeng.com.kaola1613.R;
import qianfeng.com.kaola1613.other.adapter.PlayerFreshAdapter;
import qianfeng.com.kaola1613.other.entity.PlayFresh;
import qianfeng.com.kaola1613.other.utils.Contants;
import qianfeng.com.kaola1613.other.utils.ExecutorUtil;
import qianfeng.com.kaola1613.other.utils.HttpUtil;
import qianfeng.com.kaola1613.other.utils.KaolaTask;
import qianfeng.com.kaola1613.other.utils.MediaPlayerUtil;
import qianfeng.com.kaola1613.other.utils.OtherHttpUtil;
import qianfeng.com.kaola1613.other.widget.PlayerView;

public class Player2Activity extends AppCompatActivity {

    private ViewPager viewPager;

    private List<PlayerView> viewList;

    private List<PlayFresh> player2List;

    private KaolaTask task;

    private PlayerFreshAdapter adapter;

    private float maxScale = 0.8f, minScale = 0.6f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player2);

        viewPager  = (ViewPager) findViewById(R.id.player2_vp);

        //参数1:绘制的时候是否按顺序
        //参数2：对每一个page进行修改
        viewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {

                if (position > 1)
                {
                    position = 1;
                }
                else if (position < -1)
                {
                    position = -1;
                }

                float tempPosition = position >= 0 ? (1 - position) : (1 + position);

                //缩放比例
                float scale = minScale + tempPosition * (maxScale - minScale);

                page.setScaleX(scale);
                page.setScaleY(scale);

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //切换页面的时候自动播放

                PlayerView playerView = viewList.get(position);

                playerView.play();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewList = new ArrayList<>();
        player2List = new ArrayList<>();
        adapter = new PlayerFreshAdapter(this, viewList, player2List);

        viewPager.setAdapter(adapter);

        task = new KaolaTask(new KaolaTask.IRequest() {
            @Override
            public Object doRequest() {
                return HttpUtil.doGet(OtherHttpUtil.URL_PLAYER2_FRESH);
            }

            @Override
            public Object parseResult(Object obj) {

                try {
                    JSONObject root = new JSONObject(obj.toString());

                    String message = root.getString(Contants.JSON_FLAG_MESSAGE);

                    String code = root.getString(Contants.JSON_FLAG_CODE);

                    if (Contants.JSON_FLAG_CODE_SUCCESS.equals(code)
                            && Contants.JSON_FLAG_MESSAGE_SUCCESS.equals(message))
                    {
                        JSONObject result = root.getJSONObject(Contants.JSON_FLAG_RESULT);

                        List<PlayFresh> player2List = PlayFresh.arrayPlayFreshFromData(result.toString(), Contants.JSON_FLAG_DATALIST);
                        return player2List;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }, new KaolaTask.IRequestCallback() {
            @Override
            public void onSuccess(Object object) {

                List<PlayFresh> list = (List<PlayFresh>) object;

                for (int i = 0; i < list.size(); i++) {
                    //添加数据
                    player2List.add(list.get(i));
                    //添加布局
//                    View view = getLayoutInflater().inflate(R.layout.layout_play, null);

                    PlayerView playerView = new PlayerView(Player2Activity.this);

                    viewList.add(playerView);

                }

                adapter.notifyDataSetChanged();

                Toast.makeText(Player2Activity.this, "更新成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                Toast.makeText(Player2Activity.this, "请求失败", Toast.LENGTH_SHORT).show();
            }
        });

        task.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (task != null)
        {
            task.cancel(true);
            task = null;
        }
        MediaPlayerUtil.getInstance().stop();

        ExecutorUtil.getInstance().shutdown();
    }
}
