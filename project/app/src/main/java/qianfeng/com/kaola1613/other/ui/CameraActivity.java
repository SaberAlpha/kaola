package qianfeng.com.kaola1613.other.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

import qianfeng.com.kaola1613.R;
import qianfeng.com.kaola1613.other.utils.FileUtil;

public class CameraActivity extends AppCompatActivity {

    private ImageView imageView;

    /**
     * 获取缩略图的请求码
     */
    private static final int CODE_ABLUM = 1;

    /**
     * 获取源图的请求码
     */
    private static final int CODE_SOURCE = 2;

    private File image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        imageView = (ImageView) findViewById(R.id.camera_iv);

        image = new File(FileUtil.DIR_IMAGE, "帅老刘.jpg");


    }

    public void getAblum(View btn)
    {
        //打开照相机的页面,默认返回缩略图
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, CODE_ABLUM);
    }

    public void getSource(View btn)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

//        Uri uri = Uri.parse(image.getAbsolutePath());//用这个方法会报错
        Uri uri = Uri.fromFile(image);//从文件解析路径


        //拍的照片保存的路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        intent.setDataAndType(uri, MediaStore.EXTRA_OUTPUT);


        startActivityForResult(intent, CODE_SOURCE);
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);

        if (CODE_ABLUM == requestCode)
        {
            //返回缩略图的标签data固定值
            Bitmap bitmap = result.getParcelableExtra("data");
            imageView.setImageBitmap(bitmap);
        }
        else if (CODE_SOURCE == requestCode)
        {
            //从指定的路径里解析图片
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
            imageView.setImageBitmap(bitmap);

        }
    }
}
