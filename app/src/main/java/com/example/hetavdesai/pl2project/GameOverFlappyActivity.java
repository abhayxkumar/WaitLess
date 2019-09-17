package com.example.hetavdesai.pl2project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOverFlappyActivity extends AppCompatActivity {

    public FlappyGameView gameview;
    public GameOverFlappyActivity gameOverFlappyActivity;
    public int highScore, score;
    private Button try_again, quit_game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flappy_game_over);

        TextView scoreLabel = findViewById(R.id.scoreLabel);
        TextView highScoreLabel = findViewById(R.id.highScoreLabel);

        score = getIntent().getIntExtra("SCORE", 0);
        scoreLabel.setText(score + "");

        SharedPreferences settings = getSharedPreferences("HIGH_SCORE_FLAPPY", Context.MODE_PRIVATE);
        highScore = settings.getInt("HIGH_SCORE", 0);

        if (score > highScore) {
            highScoreLabel.setText("High Score : " + score);

            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE", score);
            editor.commit();
        } else {
            highScoreLabel.setText("High Score : " + highScore);
        }

        try_again = findViewById(R.id.flappy_try_again);
        quit_game = findViewById(R.id.flappy_quit_game);
        quit_game();
        try_again();
    }

    public void quit_game() {
        quit_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent("com.example.hetavdesai.pl2project.MiniGamesActivity");
                startActivity(intent3);
            }
        });
    }

    public void try_again() {
        try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainFlappyActivity.class));
            }
        });
    }
}
