package com.example.denjo.mywallapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.List;

/**
 * Created by denjo on 15/08/29.
 */
public class AccelerometerAdapter implements SensorEventListener {
    private SensorManager sensorManager;

    //変更前の各軸の加速度
    private float oldAccelX=0;
    private float oldAccelY=0;
    private float oldAccelZ=0;

    //加速度の変化分
    private float diffAccelX=0;
    private float diffAccelY=0;
    private float diffAccelZ=0;

    public float getDiffAccelX() {
        return diffAccelX;
    }

    public void setDiffAccelX(float diffAccelX) {
        this.diffAccelX = diffAccelX;
    }

    public float getDiffAccelY() {
        return diffAccelY;
    }

    public void setDiffAccelY(float diffAccelY) {
        this.diffAccelY = diffAccelY;
    }

    public float getDiffAccelZ() {
        return diffAccelZ;
    }

    public void setDiffAccelZ(float diffAccelZ) {
        this.diffAccelZ = diffAccelZ;
    }

    public AccelerometerAdapter(SensorManager sensorManager){
        List <Sensor> sensorsAcce = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if(sensorsAcce.size()>0){
            Sensor sAcce =  sensorsAcce.get(0);
            sensorManager.registerListener(this,sAcce,SensorManager.SENSOR_DELAY_UI);
        }

    }

    public void stopSensor(){
        if(sensorManager!=null){
            sensorManager.unregisterListener(this);
        }
        sensorManager=null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //Sensorの値が変わるごとに差分を計算しなおし、加速度を更新する。
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            diffAccelX = event.values[0] - oldAccelX;
            diffAccelY = event.values[1] - oldAccelY;
            diffAccelZ = event.values[2] - oldAccelZ;
        }
        oldAccelX = event.values[0];
        oldAccelY = event.values[1];
        oldAccelZ = event.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
