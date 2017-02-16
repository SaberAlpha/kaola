package qianfeng.com.kaola1613.other.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by liujianping on 2016/10/29.
 */
public class ShakeSensorListener implements SensorEventListener {

    //传感器实例
    private Sensor sensor;

    private SensorManager sensorManager;

    private long lastTime;

    private long delay = 500;

    private double slop = 15;


    public ShakeSensorListener(Context context)
    {


        //获取传感器服务
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        //获取加速度传感器实例
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //注册监听
        sensorManager.registerListener(this, sensor, Sensor.TYPE_ACCELEROMETER);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float[] values = event.values;
        float x = values[0];
        float y = values[1];
        float z = values[2];

        long currTime = System.currentTimeMillis();

        if (currTime - lastTime < delay)
        {
            return;
        }
        lastTime = currTime;

        double speed = Math.sqrt(x * x + y * y + z * z);


        LogUtil.d("onSensorChanged x = " + x + " , y = " + y + " , z = " + z + " , speed = " + speed);

        if (speed >= slop)
        {
            LogUtil.e("speed = " + speed);
            onShake();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void onShake()
    {

    }

    public void unRegister()
    {
        sensorManager.unregisterListener(this);
    }
}
