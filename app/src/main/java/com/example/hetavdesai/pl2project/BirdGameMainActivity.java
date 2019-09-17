package com.example.hetavdesai.pl2project;


import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

public class BirdGameMainActivity extends AppCompatActivity {

    private final static long TIMER_INTERVAL = 30;
    private BirdGameView birdGameView;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //setContentView(R.layout.bird_game_main_activity);

        birdGameView = new BirdGameView(this);
        setContentView(birdGameView);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        birdGameView.invalidate();
                    }
                });
            }
        }, 0, TIMER_INTERVAL);

    }
}
