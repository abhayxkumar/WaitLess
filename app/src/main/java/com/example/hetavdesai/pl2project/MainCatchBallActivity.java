package com.example.hetavdesai.pl2project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainCatchBallActivity extends AppCompatActivity {

    // Frame
    private FrameLayout gameFrame;
    private int frameHeight, frameWidth, initialFrameWidth;
    private LinearLayout startLayout;

    // Image
    private ImageView box, black, orange, pink, boxdisplay;
    private Drawable imageBoxRight, imageBoxLeft;

    // Size
    private int boxSize;
    private int count=0;

    //Level
    private int level=1;


    // Position
    private float boxX, boxY;
    private float blackX[]=new float[100];
    private float blackY[]=new float[100];
    private float blackCenterX[]=new float[100];
    private float blackCenterY[]=new float[100];
    private float orangeX, orangeY;
    private float pinkX, pinkY;

    //Button
    private Button pause_btn;
    private Button quit_game;

    // Score
    private TextView scoreLabel, highScoreLabel;
    public TextView gHighScoreLabel;
    private int score, highScore, timeCount;
    private SharedPreferences settings;

    // Class
    private Timer timer;
    private Handler handler = new Handler();
    private SoundCatchBallPlayer soundPlayer;

    // Status
    private boolean start_flg = false;
    private boolean action_flg = false;
    private boolean pink_flg = false;
    private boolean pause_flg = false;
    private boolean pausebtn_flg = false;

    private static final String TAG = "main";
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catch_ball_main);

//        Timer t = new Timer();
//        t.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                level++;
////                Toast.makeText(MainCatchBallActivity.this,"hello",Toast.LENGTH_SHORT).show();
//            }
//        },0,30000);

        soundPlayer = new SoundCatchBallPlayer(this);

        gameFrame = findViewById(R.id.gameFrame);
        startLayout = findViewById(R.id.startLayout);
        box = findViewById(R.id.box);
        black = findViewById(R.id.black);
        orange = findViewById(R.id.orange);
        pink = findViewById(R.id.pink);
        scoreLabel = findViewById(R.id.scoreLabel);
        highScoreLabel = findViewById(R.id.highScoreLabel);
        gHighScoreLabel = findViewById(R.id.gHighScoreLabel3);
        boxdisplay = findViewById(R.id.box_display);
        pause_btn = findViewById(R.id.pause_btn);
        quit_game = findViewById(R.id.quit_game);


        pause_btn.setVisibility(View.INVISIBLE);
        imageBoxLeft = getResources().getDrawable(R.drawable.box_left);
        imageBoxRight = getResources().getDrawable(R.drawable.box_right);

        // High Score
        settings = getSharedPreferences("HIGH_SCORE_CATCHBALL", Context.MODE_PRIVATE);
        highScore = settings.getInt("HIGH_SCORE", 0);
        highScoreLabel.setText("High Score : " + highScore);

        pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausebtn_flg=true;
                setPause_btn();
            }
        });

        quit_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainCatchBallActivity.this,MiniGamesActivity.class);
                startActivity(intent);
            }
        });

        checkHighScore();

    }


    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public void changePos() {

        // Add timeCount
        timeCount += 20;

        // Orange
        orangeY += 20;

        float orangeCenterX = orangeX + orange.getWidth() / 2;
        float orangeCenterY = orangeY + orange.getHeight() / 2;

        if (hitCheck(orangeCenterX, orangeCenterY)) {
            orangeY = frameHeight + 100;
            score += 10;
            soundPlayer.playHitOrangeSound();
        }

        if (orangeY > frameHeight) {
            orangeY = -100;
            orangeX = (float) Math.floor(Math.random() * (frameWidth - orange.getWidth()));
        }
        orange.setX(orangeX);
        orange.setY(orangeY);

        // Pink
        if (!pink_flg && timeCount % 10000 == 0) {
            pink_flg = true;
            pinkY = -20;
            pinkX = (float) Math.floor(Math.random() * (frameWidth - pink.getWidth()));
        }

        if (pink_flg) {
            pinkY += 30;

            float pinkCenterX = pinkX + pink.getWidth() / 2;
            float pinkCenterY = pinkY + pink.getWidth() / 2;

            if (hitCheck(pinkCenterX, pinkCenterY)) {
                pinkY = frameHeight + 30;
                score += 30;
                // Change FrameWidth
                if (initialFrameWidth > frameWidth * 110 / 100) {
                    frameWidth = frameWidth * 110 / 100;
                    changeFrameWidth(frameWidth);
                }
                soundPlayer.playHitPinkSound();
            }

            if (pinkY > frameHeight) pink_flg = false;
            pink.setX(pinkX);
            pink.setY(pinkY);
        }

        // Black
        for(int i=0;i<level;i++) {
            blackY[i] += 30;

            blackCenterX[i] = blackX[i] + black.getWidth() / 2;
            blackCenterY[i] = blackY[i] + black.getHeight() / 2;

            if (hitCheck(blackCenterX[i], blackCenterY[i])) {
                blackY[i] = frameHeight + 100;

                // Change FrameWidth
                frameWidth = frameWidth * 70 / 100;
                changeFrameWidth(frameWidth);
                soundPlayer.playHitBlackSound();
                if (frameWidth <= boxSize) {
                    gameOver();
                }

            }


            if (blackY[i] > frameHeight) {
                blackY[i] = -100;
                blackX[i] = (float) Math.floor(Math.random() * (frameWidth - black.getWidth()));
            }

            black.setX(blackX[i]);
            black.setY(blackY[i]);
        }
        // Move Box
        if (action_flg) {
            // Touching
            boxX += 20;
            box.setImageDrawable(imageBoxRight);
        } else {
            // Releasing
            boxX -= 20;
            box.setImageDrawable(imageBoxLeft);
        }

        // Check box position.
        if (boxX < 0) {
            boxX = 0;
            box.setImageDrawable(imageBoxRight);
        }
        if (frameWidth - boxSize < boxX) {
            boxX = frameWidth - boxSize;
            box.setImageDrawable(imageBoxLeft);
        }

        box.setX(boxX);
        scoreLabel.setText("Score : " + score);

    }

    public boolean hitCheck(float x, float y) {
        if (boxX <= x && x <= boxX + boxSize &&
                boxY <= y && y <= frameHeight) {
            return true;
        }
        return false;
    }

    public void changeFrameWidth(int frameWidth) {
        ViewGroup.LayoutParams params = gameFrame.getLayoutParams();
        params.width = frameWidth;
        gameFrame.setLayoutParams(params);
    }

    public void gameOver() {
        // Stop timer.
        timer.cancel();
        timer = null;
        start_flg = false;
        pause_btn.setVisibility(View.INVISIBLE);
        // Before showing startLayout, sleep 1 second.
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        changeFrameWidth(initialFrameWidth);

        boxdisplay.setVisibility(View.INVISIBLE);
        startLayout.setVisibility(View.VISIBLE);
        box.setVisibility(View.INVISIBLE);
        black.setVisibility(View.INVISIBLE);
        orange.setVisibility(View.INVISIBLE);
        pink.setVisibility(View.INVISIBLE);

        // Update High Score
        if (score > highScore) {
            highScore = score;
            highScoreLabel.setText("Your High Score : " + highScore);

            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE", highScore);
            editor.commit();
        }
        else
        {
            highScoreLabel.setText("Your High Score : " + highScore);
        }

        checkHighScore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (start_flg) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                action_flg = true;

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                action_flg = false;

            }
        }
        return true;
    }

    public void startGame(View view) {
        start_flg = true;
        startLayout.setVisibility(View.INVISIBLE);
        pause_btn.setVisibility(View.VISIBLE);

        if (frameHeight == 0) {
            frameHeight = gameFrame.getHeight();
            frameWidth = gameFrame.getWidth();
            initialFrameWidth = frameWidth;

            boxSize = box.getHeight();
            boxX = box.getX();
            boxY = box.getY();
        }

        frameWidth = initialFrameWidth;

        box.setX(0.0f);
        black.setY(3000.0f);
        orange.setY(3000.0f);
        pink.setY(3000.0f);

        blackY[0] = black.getY();
        orangeY = orange.getY();
        pinkY = pink.getY();

        box.setVisibility(View.VISIBLE);
        black.setVisibility(View.VISIBLE);
        orange.setVisibility(View.VISIBLE);
        pink.setVisibility(View.VISIBLE);

        timeCount = 0;
        score = 0;
        level=1;
        scoreLabel.setText("Score : 0");


        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (start_flg) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }
        }, 0, 20);
    }

    //this function is called from the xml_file
    public void quitGame() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finish();
            Intent intent = new Intent("com.example.hetavdesai.pl2project.MiniGamesActivity");
            startActivity(intent);
        } else {
            finish();
            Intent intent = new Intent("com.example.hetavdesai.pl2project.MiniGamesActivity");
            startActivity(intent);
        }
    }

    //this function is called from the xml_file
    public void setPause_btn(){
        if(!pause_flg & !pausebtn_flg){
            pause_flg = true;
            timer.cancel();
            timer = null;
            pause_btn.setText("Play");
        }
        else if(!pause_flg & pausebtn_flg){
            pause_flg = true;
            timer.cancel();
            timer = null;
            pause_btn.setText("Play");
        }
        else if(pause_flg & !pausebtn_flg){
            pause_flg = true;
            pause_btn.setText("Play");
        }
        else if(pause_flg & pausebtn_flg){
            pause_flg = false;
            pausebtn_flg = false;
            pause_btn.setText("Pause");
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (start_flg) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                changePos();
                            }
                        });
                    }
                }
            }, 0, 20);
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//            Toast.makeText(MainCatchBallActivity.this, "hello", Toast.LENGTH_SHORT).show();
//            timer = new Timer();
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    if (start_flg) {
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                changePos();
//                            }
//                        });
//                    }
//                }
//            }, 0, 20);
//            timer.cancel();
//            timer = null;
//            start_flg = false;
//            finish();
//            Intent intent = new Intent("com.example.hetavdesai.pl2project.MiniGamesActivity");
//            startActivity(intent);
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
    @Override
    public void onStop() {
        super.onStop();
        if (start_flg) {
            pausebtn_flg = false;
            setPause_btn();
        }
        if(!start_flg){
            Toast.makeText(MainCatchBallActivity.this, "quit_game", Toast.LENGTH_SHORT).show();
            quitGame();
        }
    }

    public void checkHighScore()
    {
        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("High Score").child("Game 3");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    HighscoreClass value = dataSnapshot1.getValue(HighscoreClass.class);
                    String highscore1 = value.getHighscore();
                    gHighScoreLabel.setText("Global High Score : " + value.getHighscore());
                    if (highScore > Integer.parseInt(highscore1)) {
                        HighscoreClass highscoreClass = new HighscoreClass(acct.getDisplayName(), String.valueOf(highScore));
                        mDatabaseReference.child("03").setValue(highscoreClass);
                    } else {
                        gHighScoreLabel.setText("Global High Score : " + highscore1);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", databaseError.toException());
            }
        });
    }
}