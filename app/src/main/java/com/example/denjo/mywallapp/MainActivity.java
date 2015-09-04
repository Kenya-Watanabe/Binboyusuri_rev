package com.example.denjo.mywallapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private AccelerometerAdapter accelerometerAdapter;
    private GraphCounter thread;
    private int vibCount =0;
    private Vibrator vibrator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //インスタンスの取得
        SensorManager sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        //センサーを受信するためのリスナーの登録
//        accelerometerAdapter = new AccelerometerAdapter(sensorManager);
        List<Sensor> sensorsAcce = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if(sensorsAcce.size()>0){
            Sensor sAcce =  sensorsAcce.get(0);
            sensorManager.registerListener(this, sAcce, SensorManager.SENSOR_DELAY_UI);
        }

//        thread = new GraphCounter(accelerometerAdapter);

//        thread.start();
    }

    @Override
    protected void onDestroy() {
        accelerometerAdapter.stopSensor();
//        thread.close();
        super.onDestroy();

    }

//    public void onClickStartService(View view){
//        startService(new Intent(MainActivity.this,ShakeTellService.class));
//    }
//
//    public void onClickStopService(View view) {
//        stopService(new Intent(MainActivity.this,ShakeTellService.class));
//    }

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


    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
            for (int i = 0; i < currentOrientationValues.length; i++) {
                currentOrientationValues[i] = event.values[i] * ALPHA + currentOrientationValues[i] * (1 - ALPHA);
                currentAccelerationValues[i] = event.values[i] - currentOrientationValues[i];
                diffAccelerationValues[i] = currentAccelerationValues[i] - oldAccelerationValues[i];
                oldAccelerationValues[i] = currentAccelerationValues[i];
            }
            if (currentAccelerationValues[1] > 2) {
                Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(200);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public class GraphCounter extends Thread {
        private AccelerometerAdapter accelerometerAdapter;
        private Handler handler = new Handler();

        private TextView tAccelX;
        private TextView tAccelY;
        private TextView tAccelZ;
        private  GraphView gAccelX;
        private GraphView gAccelY;
        private GraphView gAccelZ;

        boolean runFlag = true;

        public  GraphCounter(AccelerometerAdapter accelerometerAdapter){
            this.accelerometerAdapter = accelerometerAdapter;
            tAccelX = (TextView)findViewById(R.id.accelxText);
            tAccelY = (TextView)findViewById(R.id.accelyText);
            tAccelZ = (TextView)findViewById(R.id.accelzText);
            gAccelX = (GraphView)findViewById(R.id.accelxView);
            gAccelY = (GraphView)findViewById(R.id.accelyView);
            gAccelZ = (GraphView)findViewById(R.id.accelzView);
        }

        //描画を止める
        public void close(){
            runFlag = false;
        }

        public void run(){
            while(runFlag){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tAccelX.setText("X"+getCurrentAccelerationValues()[0]);
                        tAccelY.setText("Y:"+getCurrentAccelerationValues()[1]);
                        tAccelZ.setText("Z:"+getCurrentAccelerationValues()[2]);

                        gAccelX.setDiv(getCurrentAccelerationValues()[0]);//graphの値はdiffAccelXの10倍
                        gAccelY.setDiv(getCurrentAccelerationValues()[1]);
                        gAccelZ.setDiv(getCurrentAccelerationValues()[2]);
                        //Graphの再描画
                        gAccelX.invalidate();
                        gAccelY.invalidate();
                        gAccelZ.invalidate();
                    }
                });
                try{
                    Thread.sleep(100);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }


    }


}

