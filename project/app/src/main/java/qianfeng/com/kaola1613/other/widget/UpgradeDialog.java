package qianfeng.com.kaola1613.other.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import qianfeng.com.kaola1613.R;
import qianfeng.com.kaola1613.other.utils.FileUtil;
import qianfeng.com.kaola1613.other.utils.HttpUtil;
import qianfeng.com.kaola1613.other.utils.KaolaTask;

/**
 * 更新对话框
 *
 * Created by liujianping on 2016/11/1.
 */
public class UpgradeDialog extends Dialog {

    private Handler handler = new Handler();

    public UpgradeDialog(final Context context, String msg, final String versionName, final String apkUrl) {
        super(context, R.style.kaola_dialog);

        setContentView(R.layout.dialog_upgrade);



        TextView tvMsg = (TextView) findViewById(R.id.upgrade_dialog_msg_tv);

        tvMsg.setText(msg);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.upgrade_dialog_pb);

        Button btnCancel = (Button) findViewById(R.id.upgrade_dialog_cancel_btn);
        //如果需要强制更新，把下面三个属性加上
        //不能通过取消按键隐藏对话框
//        setCancelable(false);
        //不能点击窗口以外的区域隐藏对话框
//        setCanceledOnTouchOutside(false);
//        btnCancel.setEnabled(false);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        Button btnDownload = (Button) findViewById(R.id.upgrade_dialog_download_btn);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                KaolaTask task = new KaolaTask(new KaolaTask.IDownloader() {
                    @Override
                    public Object downloadFile() {
                        return HttpUtil.downloadFile(apkUrl, FileUtil.DIR_APK, "kaola1613_" + versionName + ".apk",
                                new KaolaTask.IProgress() {
                                    @Override
                                    public void updataProgress(final int progress) {

                                        //downloadFile是在子线程执行的，所以更新进度要放在主线程里执行
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                //更新进度条
                                                progressBar.setProgress(progress);
                                            }
                                        });

                                    }
                                });
                    }
                }, new KaolaTask.IRequestCallback() {
                    @Override
                    public void onSuccess(Object object) {
                        File apk = (File) object;

                        dismiss();
                        toInstallApk(apk);
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(getContext(), "apk 下载失败", Toast.LENGTH_SHORT).show();
                    }
                });

                task.execute();
            }
        });
    }

    public void toInstallApk(File apk)
    {

    }
}
