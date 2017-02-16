package qianfeng.com.kaola1613.other.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;

/**
 * Created by liujianping on 2016/10/20.
 */
public class BitmapUtil {

    /**
     * 显示模糊图片
     *
     * @param context
     * @param overlay
     * @param view
     * @param radius
     */
    public static void blur(Context context, Bitmap overlay, View view, float radius) {

        RenderScript rs = RenderScript.create(context);
        Allocation overlayAlloc = Allocation.createFromBitmap(rs, overlay);
        ScriptIntrinsicBlur blur =
                ScriptIntrinsicBlur.create(rs, overlayAlloc.getElement());
        blur.setInput(overlayAlloc);
        blur.setRadius(radius);
        blur.forEach(overlayAlloc);
        overlayAlloc.copyTo(overlay);
        view.setBackground(new BitmapDrawable(context.getResources(), overlay));
        rs.destroy();
    }

}
