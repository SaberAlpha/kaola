package qianfeng.com.kaola1613.other.utils;

import android.content.Context;

/**
 * Created by liujianping on 2016/10/12.
 */
public class DeviceUtil {

    /**
     * 获取屏幕的宽度
     *
     * @param context
     *
     * @return
     */
    public static int getScreenWidth(Context context)
    {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 把dp值转换成px值
     *
     * @param context
     * @param dp
     * @return
     */
    public static float getPxFromDp(Context context, int dp)
    {
        //获取屏幕密度
        float density = context.getResources().getDisplayMetrics().density;

        float px = density * dp + 0.5f;

        return px;
    }
}
