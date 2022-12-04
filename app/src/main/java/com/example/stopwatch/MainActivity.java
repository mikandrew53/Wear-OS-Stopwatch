package com.example.stopwatch;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.wear.widget.BoxInsetLayout;

import com.example.stopwatch.databinding.ActivityMainBinding;

public class MainActivity extends Activity {


private ActivityMainBinding binding;
    Chronometer chronometer;
    ImageButton startBtn, stopBtn;
    BoxInsetLayout main;

    private boolean resume;
    Handler handler;

    long timeMillisecond, timeStart, timeBuff, timeUpdate = 0L;
    int sec, min, millisecond;
    ValueAnimator colorAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     binding = ActivityMainBinding.inflate(getLayoutInflater());
     setContentView(binding.getRoot());

     chronometer = findViewById(R.id.chronometer);
     startBtn = findViewById(R.id.startBtn);
     stopBtn = findViewById(R.id.stopBtn);
     main = findViewById(R.id.main);

    handler = new Handler();
    int redColour= Color.parseColor("#1586fd");
    int blueColour = Color.parseColor("#b43939");



    startBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!resume){
                timeStart = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);
                chronometer.start();
                resume = true;
                stopBtn.setVisibility(View.GONE);
                colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), blueColour, redColour);
                colorAnimation.setDuration(150); // milliseconds
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        main.setBackgroundColor((int) valueAnimator.getAnimatedValue());
                    }
                });
                colorAnimation.start();
                startBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_pause_24));
            }else {
                timeBuff += timeMillisecond;
                handler.removeCallbacks(runnable);
                chronometer.stop();
                resume = false;
                stopBtn.setVisibility(View.VISIBLE);
                startBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_play_arrow_24));
                colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), redColour, blueColour);
                colorAnimation.setDuration(150); // milliseconds
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        main.setBackgroundColor((int) valueAnimator.getAnimatedValue());
                    }
                });
                colorAnimation.start();
            }
        }
    });


    stopBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!resume){
                startBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_play_arrow_24));
                timeMillisecond = 0L;
                timeStart = 0L;
                timeBuff = 0L;
                sec = 0;
                min = 0;
                millisecond = 0;
                chronometer.setText("00:00:00");
            }
        }
    });
    }

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            timeMillisecond = SystemClock.uptimeMillis() - timeStart;
            timeUpdate = timeBuff + timeMillisecond;
            sec = (int) (timeUpdate / 1000);
            min = sec / 60;
            sec = sec % 60;
            millisecond = (int) (timeUpdate % 100);
            chronometer.setText(String.format("%02d", min) + ":" + String.format("%02d", sec) + ":" + String.format("%02d", millisecond));
            handler.postDelayed(this, 60);
        }
    };
}