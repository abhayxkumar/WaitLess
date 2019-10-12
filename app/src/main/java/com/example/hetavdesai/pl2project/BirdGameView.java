package com.example.hetavdesai.pl2project;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.pow;


public class BirdGameView extends View {

    public int score;
    BirdGameMainActivity birdGameMainActivity;
    BirdGameView birdGameView;
    // Canvas
    private int canvasWidth;
    private int canvasHeight;
    // Bird
    //private Bitmap bird;
    private Bitmap bird[] = new Bitmap[2];
    private int birdX = 50;
    private int birdY;
    private int birdSpeed;
    // Blue Ball
    private int blueX;
    private int blueY;
    private int blueSpeed = 15;
    private Paint bluePaint = new Paint();
    private Bitmap blue_ball;
    //Red Ball
    private int redX;
    private int redY;
    private int redSpeed = 25;
    private Paint redPaint = new Paint();
    // Black Ball
    private int blackX[]=new int[100];
    private int blackY[]= new int[100];
    private int blackSpeed[] = new int[100];
    private Paint blackPaint = new Paint();
    // Background
    private Bitmap bgImage;
    // Score
    private Paint scorePaint = new Paint();
    // Level
    private int level;
    public int count=0;
    private Paint levelPaint = new Paint();
    private Paint TopupPaint = new Paint();

    //Pause
    private Paint pausePaint = new Paint();

    // Life
    private Bitmap life[] = new Bitmap[2];
    private int life_count;

    // Status Check
    private boolean touch_flg = false;
    private boolean topupflag = false;
    private boolean Blackflag = true;

    //Animation
    ImageView imageView;

    //Topup
    int x=0;

    @SuppressLint("ResourceAsColor")
    public BirdGameView(Context context) {
        super(context);

        bird[0] = BitmapFactory.decodeResource(getResources(), R.drawable.birdgame_bird1);
        bird[1] = BitmapFactory.decodeResource(getResources(), R.drawable.birdgame_bird2);
        blue_ball = BitmapFactory.decodeResource(getResources(),R.drawable.bullet_ball_blue);

        bluePaint.setColor(Color.BLUE);
       // bluePaint.setShadowLayer(5, 0, 0, Color.BLUE);
        bluePaint.setAntiAlias(false);

        redPaint.setColor(Color.RED);
      //  redPaint.setShadowLayer(5, 0, 0, Color.RED);
        redPaint.setAntiAlias(false);

        blackPaint.setColor(Color.BLACK);
       // blackPaint.setShadowLayer(5, 0, 0, Color.GRAY);
        blackPaint.setAntiAlias(false);

        scorePaint.setColor(Color.BLACK);
        scorePaint.setTextSize(40);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setAntiAlias(true);

        levelPaint.setColor(Color.DKGRAY);
        levelPaint.setTextSize(40);
        levelPaint.setTypeface(Typeface.DEFAULT_BOLD);
        levelPaint.setTextAlign(Paint.Align.CENTER);
        levelPaint.setAntiAlias(true);

        TopupPaint.setColor(Color.parseColor("#FFFFFF"));
        TopupPaint.setTextSize(50);
        TopupPaint.setShadowLayer(30, 0, 0, Color.BLACK);
        TopupPaint.setTypeface(Typeface.DEFAULT_BOLD);
        TopupPaint.setAntiAlias(true);

//        pausePaint.setColor(Color.);
//        pausePaint.setTextSize(60);
//        pausePaint.setTypeface(Typeface.DEFAULT_BOLD);
//        pausePaint.setAntiAlias(true);

        life[0] = BitmapFactory.decodeResource(getResources(), R.drawable.birdgame_heart);
        life[1] = BitmapFactory.decodeResource(getResources(), R.drawable.birdgame_heart_g);

        // First position.
        birdY = 500;
        score = 0;
        life_count = 3;
        level=1;
        blackSpeed[0]=20;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();

//        canvas.drawBitmap(bgImage,0,0,null);

        canvas.drawColor(ContextCompat.getColor(getContext(), R.color.Back_Ground));

        // Bird
        int minBirdY = bird[0].getHeight();
        int maxBirdY = canvasHeight - bird[0].getHeight() * 3;

        birdY += birdSpeed;
        if (birdY < minBirdY){
            birdY = minBirdY;

        }
        if (birdY > maxBirdY){
            birdY = maxBirdY;
        }
        birdSpeed += 2;

        if (touch_flg) {
            // Flap wings.
            canvas.drawBitmap(bird[1], birdX, birdY, null);
            touch_flg = false;
        } else {
            canvas.drawBitmap(bird[0], birdX, birdY, null);
        }


        // Blue
        blueX -= blueSpeed;
        if (hitCheck(blueX, blueY)) {
            score += 10;
            blueX = -100;
        }
        if (blueX < 0) {
            blueX = canvasWidth + 20;
            blueY = (int) Math.floor(Math.random() * (maxBirdY - minBirdY)) + minBirdY;
        }
      //  canvas.drawBitmap(blue_ball, blueX, blueY, null);
       // canvas.drawBitmap(blue_ball, blueX, blueY, bluePaint);
        canvas.drawCircle(blueX, blueY, 10, bluePaint);

        // Red
        redX -= redSpeed;
        if (hitCheck(redX, redY)) {
            score += 15;
            redX = -100;
        }
        if (redX < 0) {
            redX = canvasWidth + 20;
            redY = (int) Math.floor(Math.random() * (maxBirdY - minBirdY)) + minBirdY;
        }
        canvas.drawCircle(redX, redY, 10, redPaint);

        // Black
        for(int i=0;i<level;i++) {
            blackX[i] -= blackSpeed[i];
            if (hitCheck(blackX[i], blackY[i]) && Blackflag) {
                blackX[i] = -100;
                life_count--;
                if (life_count == 0) {
                    // Game Over
                    Log.v("Message", "Game Over");
                    gameOver();
                }
                else {
                    invincibility();
                }
            }
            if (blackX[i] < 0) {
                blackX[i] = canvasWidth + 200;
                blackY[i] = (int) Math.floor(Math.random() * (maxBirdY - minBirdY)) + minBirdY;
            }
            canvas.drawCircle(blackX[i], blackY[i], 20, blackPaint);
        }
        if(topupflag){
            canvas.drawText("+"+x,100,120,TopupPaint);
            canvas.drawText("Level UP",canvasWidth*4/10,canvasHeight - bird[0].getHeight(),TopupPaint);
        }
        // Score
        canvas.drawText("Score : " + score, 20, 60, scorePaint);

        // Level
        canvas.drawText("Level "+level, canvasWidth / 2, 60, levelPaint);

        // Life
        for (int i = 0; i < 3; i++) {
            int x = (int) (canvasWidth*9/10 - life[0].getWidth() * 1.5 * i);
            int y = 30;

            if (i < life_count) {
                canvas.drawBitmap(life[0], x, y, null);
            } else {
                canvas.drawBitmap(life[1], x, y, null);
            }
        }
    }

    public boolean hitCheck(int x, int y) {
        if (birdX < x && x < (birdX + bird[0].getWidth()) &&
                birdY < y && y < (birdY + bird[0].getHeight())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touch_flg = true;
            birdSpeed = -20;
            count++;
            if(count==40) {
                blackSpeed[level] = blackSpeed[level-1] + 4;
                level++;
                x = (int) (pow(2,level-1)*10);
                levelupscore();
                score = score + x;
                count=0;
            }
        }
        return true;
    }

    public void levelupscore(){
        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
                topupflag=true;
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                //Score topup
               topupflag = false;
            }

        }.start();
    }

    public void invincibility(){
        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {

                Blackflag = false;
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                //Score topup
                Blackflag = true;
            }

        }.start();
    }
    public void gameOver() {
        // birdGameMainActivity.setContentView(R.layout.bird_game_main_activity);
        ((Activity)getContext()).finish();
        Intent intent = new Intent("com.example.hetavdesai.pl2project.BirdGameOverActivity");
        intent.putExtra("SCORE", score);
        getContext().startActivity(intent);
    }
}
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event)
//    {
//        if ((keyCode == KeyEvent.KEYCODE_BACK))
//        {
//
//        }
//        return super.onKeyDown(keyCode, event);
//    }

