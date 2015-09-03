package com.example.denjo.mywallapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by denjo on 15/08/29.
 */
public class GraphView extends View {
    private float div=0;
    private int counter =0;
    private static final int DATA_SIZE = 240;

    //過去480回分のデータ
    private int[] counterHistory = new int[DATA_SIZE];

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GraphView(Context context) {
        super(context);
    }

    public GraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setDiv(float div){
        this.div = div;
        this.counter++;
        if(counterHistory.length<=counter){
            counter = 0;
        }
        counterHistory[counter] = (int)this.div *10;
    }


    //グラフィックに描くものを記述
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = this.getWidth();
        int height = this.getHeight();

        int base = height/2;

        Paint paint = new Paint();
        //色指定
        paint.setColor(Color.argb(75, 255, 255, 255));
        paint.setStrokeWidth(1);

        //Graphのgrid線の指定
        for(int y=0;y<height;y+=20){
            canvas.drawLine(0,y,width,y,paint);
        }
        for(int x = 0;x<width;x+=20){
            canvas.drawLine(x,0,x,height,paint);
        }
        //center line(RED)
        paint.setColor(Color.RED);
        canvas.drawLine(0, base, width, base, paint);

        //Graph (Yellow)
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(2);

        for(int i=0;i<counterHistory.length-1;i++){
            canvas.drawLine(4*i,base + counterHistory[i],4*(i+1),base+counterHistory[i+1],paint);
        }

        //現在線を赤線で表示
        paint.setColor(Color.RED);
        canvas.drawLine(4*counter, 0, 4*counter, height, paint);

        Log.i("GraphView", "counter: " + counter);


    }
}
