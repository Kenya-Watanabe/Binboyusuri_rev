package com.example.denjo.mywallapp;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private AccelerometerAdapter accelerometerAdapter;
    private GraphCounter thread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //インスタンスの取得
        SensorManager sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        //センサーを受信するためのリスナーの登録
        accelerometerAdapter = new AccelerometerAdapter(sensorManager);
        thread = new GraphCounter(accelerometerAdapter);

        thread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accelerometerAdapter.stopSensor();
        thread.close();
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
                        tAccelX.setText("X"+accelerometerAdapter.getDiffAccelX());
                        tAccelY.setText("Y:"+accelerometerAdapter.getDiffAccelY());
                        tAccelZ.setText("Z:"+accelerometerAdapter.getDiffAccelZ());

                        gAccelX.setDiv(accelerometerAdapter.getDiffAccelX());//graphの値はdiffAccelXの10倍
                        gAccelY.setDiv(accelerometerAdapter.getDiffAccelY());
                        gAccelZ.setDiv(accelerometerAdapter.getDiffAccelZ());
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

