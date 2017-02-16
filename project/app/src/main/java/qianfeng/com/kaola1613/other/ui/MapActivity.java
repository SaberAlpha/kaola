package qianfeng.com.kaola1613.other.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;

import qianfeng.com.kaola1613.R;
import qianfeng.com.kaola1613.other.utils.LogUtil;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;

    private BaiduMap baiduMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapView = (MapView) findViewById(R.id.map_mv);

        baiduMap = mapView.getMap();

        //设置显示卫星图
        baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);

        //开启交通图
        baiduMap.setTrafficEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();

        //获取地图的比例尺等级,默认是5公里
        int mapLevel = mapView.getMapLevel();
        LogUtil.d("mapLevel = " + mapLevel);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
