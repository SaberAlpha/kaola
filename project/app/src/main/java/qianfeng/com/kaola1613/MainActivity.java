package qianfeng.com.kaola1613;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import qianfeng.com.kaola1613.other.ui.BannerActivity;
import qianfeng.com.kaola1613.other.ui.GuideActivity;
import qianfeng.com.kaola1613.other.utils.Contants;
import qianfeng.com.kaola1613.other.utils.FileUtil;
import qianfeng.com.kaola1613.other.utils.HttpUtil;
import qianfeng.com.kaola1613.other.utils.KaolaTask;
import qianfeng.com.kaola1613.other.utils.OtherHttpUtil;
import qianfeng.com.kaola1613.other.widget.UpgradeDialog;

/**
 *
 * 本应用的入口
 *
 * Created by Liu Jianping on 2016/10/09.
 */
public class MainActivity extends AppCompatActivity {

    private KaolaTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //进入应用的第一件事，请求版本

        //把本地的版本和服务端的版本进行对比，如果不一样，表示需要更新

        //先获取本地的版本
        PackageManager packageManager = getPackageManager();
        try {
            //本应用的信息
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            //本地的版本
            //如果清单文件和build.gradle文件里都有版本信息，并且他们都不一样，那么会以build.gradle的描述为准
            final String localVersion = packageInfo.versionName;
//            String versionCode = packageInfo.versionCode;//版本号
            task = new KaolaTask(new KaolaTask.IRequest() {
                @Override
                public Object doRequest() {
                    return HttpUtil.doGet(OtherHttpUtil.URL_UPGRADE + localVersion);
                }

                @Override
                public Object parseResult(Object obj) {

                    try {
                        JSONObject root = new JSONObject(obj.toString());
                        String message = root.getString(Contants.JSON_FLAG_MESSAGE);
                        String code = root.getString(Contants.JSON_FLAG_CODE);
                        if (Contants.JSON_FLAG_MESSAGE_SUCCESS.equals(message)
                                && Contants.JSON_FLAG_CODE_SUCCESS.equals(code))
                        {
                            JSONObject result = root.getJSONObject(Contants.JSON_FLAG_RESULT);
                            int updateType = result.getInt("updateType");

                            if (updateType == 0)
                            {
//                                String msg = result.getString("updateInfo");

                                String netVersion = result.getString("updateVersion");

//                                String apkUrl = result.getString("updateUrl");
                                //如果本地版本和网络版本不一样，那么需要下载apk
                                if (!localVersion.equals(netVersion))
                                {
                                    return result;
                                }
                            }
                            else
                            {
                                return "已经是最新版本了";
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }, new KaolaTask.IRequestCallback() {
                @Override
                public void onSuccess(Object object) {


                    //显示更新对话框
                    JSONObject result = null;
                    try {
                        result = (JSONObject) object;
                    }
                    catch (Exception e)
                    {
                        //转型异常说明返回是的"已经是最新版本了"
                        Toast.makeText(MainActivity.this, object.toString(), Toast.LENGTH_SHORT).show();
                        //如果是最新版本，那么不用显示更新对话框
                        nextActivity();
                        return;
                    }

                    try {
//                        result = new JSONObject(object.toString());

                        String msg = result.getString("updateInfo");

                        String apkUrl = result.getString("updateUrl");

                        String netVersion = result.getString("updateVersion");
                        //显示更新信息

                        showUpgradeDialog(msg, netVersion, apkUrl);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError() {
                    Toast.makeText(MainActivity.this, "请求版本失败", Toast.LENGTH_SHORT).show();
                    nextActivity();
                }
            });

            task.execute();

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            nextActivity();
        }
    }

    private void nextActivity() {
        //如果是第一次进入应用，那么跳到欢迎页面
        Intent intent = null;
        if (isFirstUsed())
        {
            //GuideActivity
            intent = new Intent(this, GuideActivity.class);
        }
        else
        {
            //进入到广告页面BannerActivity
            intent = new Intent(this, BannerActivity.class);
        }

        startActivity(intent);
        //进入页面后不能再回到MainActivity
        finish();
    }

    private void showUpgradeDialog(String msg, String netVersion, String apkUrl) {
        //如果点击确定下载，自动进入安装页面

        UpgradeDialog dialog = new UpgradeDialog(this, msg, netVersion, apkUrl)
        {
            @Override
            public void toInstallApk(File apk) {
                FileUtil.installApk(MainActivity.this, apk);
                finish();
            }
        };
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                //进入到广告页面BannerActivity
                Intent intent = new Intent(MainActivity.this, BannerActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.show();

    }

    //Alt+enter修复(导包，快速生成方法的返回值)


//    <isFirstUsed>
//        true
//    </isFirstUsed>
    private boolean isFirstUsed() {
        SharedPreferences preferences = getSharedPreferences(Contants.PREFERENCES_USED, Context.MODE_PRIVATE);

        boolean firstUsed = preferences.getBoolean(Contants.PREFERENCES_USED_FLAG, true);
        return firstUsed;
    }
}
