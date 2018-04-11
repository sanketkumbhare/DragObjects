package com.sanketkumbhare.dragobjects.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.sanketkumbhare.dragobjects.R;

public class Accelerometer extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private AnimatedView mAnimatedView = null;
    Bitmap b,resized;
    boolean flag=true;
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


            b = BitmapFactory.decodeResource(getResources(), R.drawable.bin1);

            resized = Bitmap.createScaledBitmap(b, 200,200 , true);

        mAnimatedView = new AnimatedView(this);
        //Set our content to a view, not like the traditional setting to a layout
        setContentView(mAnimatedView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) { }

    @Override
    public void onSensorChanged(SensorEvent event) {
            if(flag) {
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    mAnimatedView.onSensorEvent(event);
                }
            }
    }

    public class AnimatedView extends View {

        private static final int CIRCLE_RADIUS = 40; //pixels

        private Paint mPaint;
        private int x;
        private int y;
        private int viewWidth;
        private int viewHeight;

        public AnimatedView(Context context) {
            super(context);
            mPaint = new Paint();
            mPaint.setColor(Color.MAGENTA);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            viewWidth = w;
            viewHeight = h;
        }

        public void onSensorEvent (SensorEvent event) {
            x = x - (int) event.values[0];
            y = y + (int) event.values[1];
            //Make sure we do not draw outside the bounds of the view.
            //So the max values we can draw to are the bounds + the size of the circle

            if(x> viewWidth - resized.getWidth()/2 && x< viewWidth
                    && y>viewHeight -resized.getHeight() && y< viewHeight - resized.getHeight()/2){
                x = viewWidth + (CIRCLE_RADIUS*2);
                y= viewHeight + (CIRCLE_RADIUS*2);
                flag=false;
            }
            if(flag) {
                if (x <= 0 + CIRCLE_RADIUS) {
                    x = 0 + CIRCLE_RADIUS;
                }
                if (x >= viewWidth - CIRCLE_RADIUS) {
                    x = viewWidth - CIRCLE_RADIUS;
                }
                if (y <= 0 + CIRCLE_RADIUS) {
                    y = 0 + CIRCLE_RADIUS;
                }
                if (y >= viewHeight - CIRCLE_RADIUS) {
                    y = viewHeight - CIRCLE_RADIUS;
                }

            }
        }

        @SuppressLint("DrawAllocation")
        @Override
        protected void onDraw(Canvas canvas) {

            canvas.drawBitmap(resized,canvas.getWidth()-resized.getWidth(),canvas.getHeight()-resized.getHeight(),mPaint);
            canvas.drawCircle(x, y, CIRCLE_RADIUS, mPaint);
           // canvas.drawColor(0, PorterDuff.Mode.CLEAR);
            if(!flag){
                b = BitmapFactory.decodeResource(getResources(), R.drawable.bin2);
                resized = Bitmap.createScaledBitmap(b, 200,200 , true);
                canvas.drawBitmap(resized,canvas.getWidth()-resized.getWidth(),canvas.getHeight()-resized.getHeight(),mPaint);

            }

            //We need to call invalidate each time, so that the view continuously draws
            invalidate();

        }
    }
}