package com.example.denjo.mywallapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;

import java.util.List;

/**
 * Created by denjo on 15/08/29.
 */
public class AccelerometerAdapter implements SensorEventListener {



    private SensorManager sensorManager;
    private int flag =0;

    //変更前の各軸の加速度
    private float oldAccelX=0;
    private float oldAccelY=0;
    private float oldAccelZ=0;

    //加速度の変化分
    private float diffAccelX=0;
    private float diffAccelY=0;
    private float diffAccelZ=0;

    private float[]  currentOrientationValues = {0.0f,0.0f,0.0f};
    private float[] oldAccelerationValues = {0.0f,0.0f,0.0f};

    public float[] getCurrentAccelerationValues() {
        return currentAccelerationValues;
    }

    public void setCurrentAccelerationValues(float[] currentAccelerationValues) {
        this.currentAccelerationValues = currentAccelerationValues;
    }

    private float[]  currentAccelerationValues ={0.0f,0.0f,0.0f};

    public float[] getDiffAccelerationValues() {
        return diffAccelerationValues;
    }

    private float[]  diffAccelerationValues = {0.0f,0.0f,0.0f};

    private final static float ALPHA = 0.1f;

    //加速度の変化分のNorm
    private  double norm;


    //getter and setter
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

    public double getNorm() {
        return norm;
    }

    public void setNorm(double norm) {
        this.norm = norm;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }


    public AccelerometerAdapter(SensorManager sensorManager){
        List <Sensor> sensorsAcce = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if(sensorsAcce.size()>0){
            Sensor sAcce =  sensorsAcce.get(0);
            sensorManager.registerListener(this,sAcce,SensorManager.SENSOR_DELAY_GAME);
        }

    }

    public void stopSensor(){
        if(sensorManager!=null){
            sensorManager.unregisterListener(this);
        }
        sensorManager=null;
    }

//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        //Sensorの値が変わるごとに差分を計算しなおし、加速度を更新する。
//        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
//            diffAccelX = event.values[0] - oldAccelX;
//            diffAccelY = event.values[1] - oldAccelY;
//            diffAccelZ = event.values[2] - oldAccelZ;
//            norm = Math.sqrt(diffAccelX*diffAccelX+diffAccelY*diffAccelY+diffAccelZ*diffAccelZ);
//            Log.i("AccelerometerAdapter","Norm :"+norm + "accelz :"+diffAccelZ);
//            Log.i("AccelerometerAdapter","flag :"+flag );
//            if(norm >7 && norm <10){
//
//                flag++;
//            }
//        }
//        oldAccelX = event.values[0];
//        oldAccelY = event.values[1];
//        oldAccelZ = event.values[2];
//    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        for (int i= 0;i<currentOrientationValues.length;i++){
            currentOrientationValues[i] = event.values[i]*ALPHA + currentOrientationValues[i]*(1-ALPHA);
            currentAccelerationValues[i] = event.values[i]-currentOrientationValues[i];
            diffAccelerationValues[i] = currentAccelerationValues[i]-oldAccelerationValues[i];
            oldAccelerationValues[i] = currentAccelerationValues[i];
        }
        if(currentAccelerationValues[1]>3){

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void vibrate(Context context, long milliseconds) {
        ((Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(milliseconds);
    }

}
