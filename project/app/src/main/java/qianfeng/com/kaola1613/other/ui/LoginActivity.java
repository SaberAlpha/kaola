package qianfeng.com.kaola1613.other.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import de.greenrobot.event.EventBus;
import qianfeng.com.kaola1613.R;
import qianfeng.com.kaola1613.other.entity.LoginEvent;
import qianfeng.com.kaola1613.other.utils.LogUtil;

/**
 * AppCompatActivity一定要和AppCompat相关的Theme结合使用,否则一定会报错
 *
 *
 * 登录页面
 *
 * Created by Liu Jianping
 *
 * @date : 16/8/26.
 */
public class LoginActivity extends Activity
{
    private Tencent tencent;

    public static final String APP_ID = "1105719573";

    private ImageView ivQQ;

    /**
     * 登录授权监听
     */
    private IUiListener loginListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            LogUtil.d("loginListener onComplete " + o.toString());

            try {
                JSONObject root = new JSONObject(o.toString());

                String openid = root.getString("openid");

                String access_token = root.getString("access_token");

                long expires_in = root.getLong("expires_in");

                tencent.setOpenId(openid);
                tencent.setAccessToken(access_token, ""+expires_in);

                UserInfo userInfo = new UserInfo(LoginActivity.this, tencent.getQQToken());

                getUserInfo(userInfo);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(UiError uiError) {
            LogUtil.e("loginListener onError " + uiError.errorMessage + " , " + uiError.errorDetail);
        }

        @Override
        public void onCancel() {
            LogUtil.w("loginListener onCancel ");
        }
    };

    private void getUserInfo(UserInfo userInfo) {
        //请求用户信息
        userInfo.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object o) {
                LogUtil.d("UserInfo onComplete " + o.toString());
                try {
                    JSONObject root = new JSONObject(o.toString());
                    String headUrl = root.getString("figureurl_qq_2");
                    String nickName = root.getString("nickname");

                    //发布一个事件传递昵称和头像到我的页面
                    EventBus.getDefault().post(new LoginEvent(nickName, headUrl));
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(UiError uiError) {
                LogUtil.e("UserInfo onError " + uiError.errorMessage + " , " + uiError.errorDetail);
            }

            @Override
            public void onCancel() {
                LogUtil.w("UserInfo onCancel ");
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        //创建对象
        tencent = Tencent.createInstance(APP_ID, getApplicationContext());

        ivQQ = (ImageView) findViewById(R.id.login_qq_iv);

        ivQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                loginByQQ();
                  loginQQByShareSDK();

            }
        });

    }

    /**
     * 使用ShareSDK登录QQ
     */
    private void loginQQByShareSDK() {
        //创建一个平台实例
        Platform qq = ShareSDK.getPlatform(QQ.NAME);

        //设置请求回调
        qq.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                LogUtil.d("ShareSDK onComplete " + platform.toString());

                PlatformDb db = platform.getDb();

                final String userName = db.getUserName();//获取用户的名字
                final String userhead = db.getUserIcon();//头像

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //发布一个事件传递昵称和头像到我的页面
                        EventBus.getDefault().post(new LoginEvent(userName, userhead));
                        finish();
                    }
                });

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                LogUtil.d("ShareSDK onError ");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                LogUtil.d("ShareSDK onCancel ");
            }
        });

        //请求授权并获取用户信息
        qq.showUser(null);

    }

    private void loginByQQ() {
        //是否已经登录
        if (tencent.isSessionValid())
        {
            //注销登录
//            tencent.logout(this);
        }
        else
        {
            //调用登录操作
            //第二个参数：登录时需要请求的权限，默认我们使用"all"
            tencent.login(LoginActivity.this, "all", loginListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //从登录页面返回当前页时，需要的回调操作
        Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
    }
}
