package com.example.hetavdesai.pl2project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GameOverFlappyActivity extends AppCompatActivity {

    public FlappyGameView gameview;
    public GameOverFlappyActivity gameOverFlappyActivity;
    public int highScore, score;
    private Button try_again, quit_game;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flappy_game_over);

        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        TextView scoreLabel = findViewById(R.id.scoreLabel);
        TextView highScoreLabel = findViewById(R.id.highScoreLabel);
        final TextView gHighScoreLabel = findViewById(R.id.gHighScoreLabel);

        score = getIntent().getIntExtra("SCORE", 0);
        scoreLabel.setText(score + "");

        SharedPreferences settings = getSharedPreferences("HIGH_SCORE_FLAPPY", Context.MODE_PRIVATE);
        highScore = settings.getInt("HIGH_SCORE", 0);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("High Score").child("Game 2");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    HighscoreClass value = dataSnapshot1.getValue(HighscoreClass.class);
                    String highscore1 = value.getHighscore();
                    gHighScoreLabel.setText("High Score : " + value.getHighscore());
                    if (score > Integer.parseInt(highscore1)) {
                        HighscoreClass highscoreClass = new HighscoreClass(acct.getDisplayName(), String.valueOf(score));
                        mDatabaseReference.child("02").setValue(highscoreClass);
                    } else {
                        gHighScoreLabel.setText("High Score : " + highscore1);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", databaseError.toException());
            }
        });

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
                finish();
                Intent intent3 = new Intent("com.example.hetavdesai.pl2project.MiniGamesActivity");
                startActivity(intent3);
            }
        });
    }

    public void try_again() {
        try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), MainFlappyActivity.class));
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent("com.example.hetavdesai.pl2project.MiniGamesActivity");
        startActivity(intent);
        super.onBackPressed();
        return;
    }
}
