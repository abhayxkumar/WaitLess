package com.example.hetavdesai.pl2project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainFlappyActivity extends AppCompatActivity implements View.OnClickListener {

    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_flappy_main);
        b = findViewById(R.id.start_button);
    }


    @Override
    public void onClick(View v) {

        setContentView(new FlappyGameView(this));
    }
}

