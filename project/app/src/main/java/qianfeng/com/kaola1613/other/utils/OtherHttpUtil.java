package qianfeng.com.kaola1613.other.utils;

/**
 * other模块下所有的网络地址和请求方法
 *
 * Created by liujianping on 2016/10/10.
 */
public class OtherHttpUtil {

    /**
     * 进入应用的广告url
     */
    public static final String URL_BANNER = "http://api.kaolafm.com/api/v4/splashscreen/list?timezone=28800&installid=10000&udid=df89950db6c8b27b94e3112145a9217b&sessionid=df89950db6c8b27b94e3112145a9217b1464231568066&imsi=310260000000000&operator=0&lon=0.0&lat=0.0&network=1&timestamp=1464231568&sign=89f2b488d14f006f11f7fb61199b7cd5&resolution=768*1280&devicetype=0&channel=upgrade&version=4.8.1&appid=0";

    /**
     * 评论列表 resourceid的值是从播放页面传进来的
     */
    public static final String URL_COMMENT_LIST = "http://api.kaolafm.com/api/v4/comment/new?resourcetype=1&pagesize=20&pagenum=1&timezone=28800&installid=0003O7kg&uid=2754846&udid=f50656ba061c85b9c178facfe9a9a241&sessionid=f50656ba061c85b9c178facfe9a9a2411477195095721&imsi=460070021204110&operator=0&lon=0.0&lat=0.0&network=1&timestamp=1477200341&playid=168589ef2e85956ae083bc2352ae6fee&sign=b45e8d9852597194151ecf85694788dc&resolution=720*1280&devicetype=0&channel=A-360&version=4.8.7&appid=0&token=token2ac879f760305708af59d19fc846bab&resourceid=";

    /**
     * 提交评论
     */
    public static final String URL_SUBMIT_COMMENT = "http://api.kaolafm.com/api/v4/comment/send?timezone=28800&installid=10000&uid=2754846&udid=f50656ba061c85b9c178facfe9a9a241&sessionid=f50656ba061c85b9c178facfe9a9a2411477207135844&imsi=460070021204110&operator=0&lon=116.397124&lat=39.90678&network=1&timestamp=1477209554&playid=e28ce2278415551b6222a878e261565c&sign=b45e8d9852597194151ecf85694788dc&resolution=720*1280&devicetype=0&channel=A-360&version=4.8.7&appid=0&token=tokena8995526c2f54df7e4a004b3c7d61a95";

    public static final String URL_PLAYER2 = "http://api.kaolafm.com/api/v4/radioplayinfo/list?radioid=1200000000175&clockid=&timezone=28800&installid=0003Oj2U&udid=378ec8f9dd0d659e6af2f6f28e439422&sessionid=378ec8f9dd0d659e6af2f6f28e4394221477359694591&imsi=460028864885429&operator=1&lon=0.0&lat=0.0&network=1&timestamp=1477359920&playid=406b3a197b8992709c000bcdfe958138&sign=90b13545555d6d67a1d0bca939b73a0c&resolution=1080*1920&devicetype=0&channel=A-360&version=4.8.7&appid=0";

    public static final String URL_PLAYER2_FRESH = " http://api.kaolafm.com/api/v4/broadcast/list?&id=1600000000632&pagesize=20&type=1&timezone=28800&installid=0003Oj2U&udid=378ec8f9dd0d659e6af2f6f28e439422&sessionid=378ec8f9dd0d659e6af2f6f28e4394221477359694591&imsi=460028864885429&operator=1&lon=0.0&lat=0.0&network=1&timestamp=1477359715&sign=90b13545555d6d67a1d0bca939b73a0c&resolution=1080*1920&devicetype=0&channel=A-360&version=4.8.7&appid=0";

    /**
     * 更新版本的url,版本号动态拼接
     */
    public static final String URL_UPGRADE = "http://api.kaolafm.com/api/v4/upgrade/check?timezone=0&installid=0003Fnzk&udid=b2e174e767b9396ac49b9ec080667a3e&sessionid=b2e174e767b9396ac49b9ec080667a3e1471400777673&imsi=310260000000000&operator=0&lon=0.0&lat=0.0&network=1&timestamp=1471400835&sign=a18fff3e0f2343ca5eabcf67568cfa31&resolution=640*864&devicetype=0&channel=A-360&appid=0&version=";


}
