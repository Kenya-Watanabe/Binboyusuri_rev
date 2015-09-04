package com.example.denjo.mywallapp;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;


public class ShakeTellService extends Service implements SensorEventListener {

   private AccelerometerAdapter accelerometerAdapter;



    @Override
    public void onCreate() {
        super.onCreate();

        //インスタンスを取得。この時は必ずgetSystemServiceで取得
        SensorManager sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometerAdapter = new AccelerometerAdapter(sensorManager);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        accelerometerAdapter.stopSensor();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        long [] pattern = {300,100,300,100};
        Log.i("ShakeTellService", "flags :"+accelerometerAdapter.getFlag());
        if(accelerometerAdapter.getFlag()>7 && accelerometerAdapter.getFlag()<30){

            vibrator.vibrate(pattern,-1);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
