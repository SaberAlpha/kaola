package qianfeng.com.kaola1613.other.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import qianfeng.com.kaola1613.R;

/**
 * 退出应用Dialog
 *
 * Created by liujianping on 2016/10/27.
 */
public class ExitDialog extends Dialog {

    private Button btnExit, btnMin, btnCancel;

    public ExitDialog(Context context) {
        this(context, R.style.kaola_dialog);

        //去掉标题栏
//        requestWindowFeature(Window.FEATURE_NO_TITLE);

        initViews();
    }

    public ExitDialog(Context context, int themeResId) {
        super(context, themeResId);
        initViews();
    }

    public ExitDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initViews();
    }

    private void initViews() {
        setContentView(R.layout.dialog_exit);

        btnExit = (Button) findViewById(R.id.exit_dialog_exit_btn);
        btnMin = (Button) findViewById(R.id.exit_dialog_min_btn);
        btnCancel = (Button) findViewById(R.id.exit_dialog_cancel_btn);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //退出应用
                dialogFinish();
            }
        });

        btnMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //最小化
                dismiss();

                //执行Home键 操作
                Intent intent = new Intent();
                //设置跳到主页面
                intent.setAction(Intent.ACTION_MAIN);
                //在新的栈里打开
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                getContext().startActivity(intent);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取消
                dismiss();
            }
        });
    }

    /**
     * 退出应用的回调操作
     */
    public void dialogFinish()
    {

    }
}
