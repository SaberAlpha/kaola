package qianfeng.com.kaola1613.other.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;

import com.se7en.endecryption.DES;
import com.se7en.endecryption.MD5;

import java.io.UnsupportedEncodingException;

import qianfeng.com.kaola1613.R;
import qianfeng.com.kaola1613.other.utils.LogUtil;

public class SecretActivity extends AppCompatActivity {

    private EditText etContent, etKey, etMd5, etBase64, etDes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secret);
        etContent = (EditText) findViewById(R.id.secret_content_et);
        etKey = (EditText) findViewById(R.id.secret_key_et);
        etMd5 = (EditText) findViewById(R.id.secret_md5_et);
        etBase64 = (EditText) findViewById(R.id.secret_base64_et);
        etDes = (EditText) findViewById(R.id.secret_des_et);
    }

    public void byMD5(View btn)
    {
        String content = etContent.getText().toString();
        String text = MD5.md5(content);

        //MD5加密后的字符串总共32位，由数字和小写组成的
        etMd5.setText(text);
    }

    public void byBase64(View btn)
    {
        String content = etContent.getText().toString();

        //Base64.DEFAULT：密钥
        //获取加密后字节数组
        byte[] encode = Base64.encode(content.getBytes(), Base64.DEFAULT);

        try {
            //加密后的内容：
            //长度随着加密内容的长度增加而增加
            //长度和加密前的长度a, 比例：(a%3 == 0), a / 3 * 4,  or --> (a / 3 + 1) * 4
            //结尾=的规律：a % 3 = 0 ,没有=; a % 3 = 1 ,2个=; a % 3 = 2 ,一个=
            String encodeStr = new String(encode, "utf-8");
            etBase64.setText(encodeStr);


            byte[] decode = Base64.decode(encodeStr.getBytes(), Base64.DEFAULT);

            String decodeStr = new String(decode, "utf-8");

            LogUtil.d("Base64 decodeStr = " + decodeStr);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void byDes(View btn)
    {
        String content = etContent.getText().toString();
        String key = etKey.getText().toString();

        byte[] encrypt = DES.encrypt(content.getBytes(), key);

        try {

            //加密出来的结果是乱码
            //密钥的长度至少8位，但是如果超过8位，也只按前面8位计算
            String encodeStr = new String(encrypt, "utf-8");
            etDes.setText(encodeStr);

            byte[] decrypt = DES.decrypt(encodeStr.getBytes(), key);

            String decodeStr = new String(decrypt, "utf-8");
            LogUtil.d("DES decodeStr = " + decodeStr);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
