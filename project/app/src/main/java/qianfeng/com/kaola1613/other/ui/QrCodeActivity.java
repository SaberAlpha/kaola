package qianfeng.com.kaola1613.other.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.zxing.activity.CaptureActivity;
import com.zxing.encoding.EncodingHandler;

import qianfeng.com.kaola1613.R;

public class QrCodeActivity extends AppCompatActivity {

    private TextView textView;

    private EditText editText;

    private ImageView imageView;

    private static final int CODE_SCAN = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        textView = (TextView) findViewById(R.id.qrcode_tv);

        editText = (EditText) findViewById(R.id.qrcode_et);

        imageView = (ImageView) findViewById(R.id.qrcode_iv);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                String text = editable.toString();
                String text = editText.getText().toString();

                try {
                    Bitmap qrCode = EncodingHandler.createQRCode(text, 800);

                    imageView.setImageBitmap(qrCode);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void toScan(View view)
    {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, CODE_SCAN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE_SCAN)
        {
            String result = data.getStringExtra("result");

            textView.setText(result);
        }
    }
}
