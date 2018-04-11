package com.sanketkumbhare.dragobjects.activities;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sanketkumbhare.dragobjects.R;

public class MainActivity extends AppCompatActivity {

    private ViewGroup mainLayout;
    private Button btn;
    private TextView textView;
    ImageView imageView;
    int array[] = new int[2];
    private int xDelta,a,b;
    private int yDelta;




    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = findViewById(R.id.main);
        btn=findViewById(R.id.image);
        textView=findViewById(R.id.textview);
        imageView=findViewById(R.id.imageview);

        imageView.getLocationOnScreen(array);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                a=(int) ev.getRawX();
                b=(int)ev.getRawY();
                textView.setText("Touch at " + ev.getRawX() + ", " + ev.getRawY());
                return true;
            }
        });
        btn.setOnTouchListener(onTouchListener());
    }

    private View.OnTouchListener onTouchListener(){
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams)
                                v.getLayoutParams();

                        xDelta = x - lParams.leftMargin;
                        yDelta = y - lParams.topMargin;



                        if(x == array[0] || y == array[1])
                        btn.setVisibility(View.GONE);
                        break;

                    case MotionEvent.ACTION_UP:
                        Toast.makeText(MainActivity.this,
                                "thanks for new location!", Toast.LENGTH_SHORT)
                                .show();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v
                                .getLayoutParams();
                        layoutParams.leftMargin = x - xDelta;
                        layoutParams.topMargin = y - yDelta;
                        layoutParams.rightMargin = 0;
                        layoutParams.bottomMargin = 0;
                        v.setLayoutParams(layoutParams);
                        if(x == array[0] || y == array[1])


                            btn.setVisibility(View.GONE);

                        break;
                }
                mainLayout.invalidate();
                return  true;
            }
        };
    }



}
