package com.example.hetavdesai.pl2project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class FlappyGameView extends SurfaceView implements SurfaceHolder.Callback {
    public static int gapHeight = 500;
    public static int velocity = 26;
    public FlappyGameView gameview;
    public FlappyPipeSprite pipe1, pipe2, pipe3;
    // Score
    public int score;
    Intent intent = new Intent("com.example.hetavdesai.pl2project.GameOverFlappyActivity");
    FlappySoundPlayer soundplayer = new FlappySoundPlayer(getContext());
    LinearLayout linearLayout = new LinearLayout(getContext());
    TextView scoretext = new TextView(getContext());
    private FlappyMainThread thread;
    private CharacterFlappySprite characterFlappySprite;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;


    public FlappyGameView(Context context) {
        super(context);

        getHolder().addCallback(this);

        thread = new FlappyMainThread(getHolder(), this);

        setFocusable(true);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        characterFlappySprite.y = characterFlappySprite.y - (characterFlappySprite.yVelocity2 * 15);
        //characterFlappySprite.time=0;
        characterFlappySprite.yVelocity = 0;
        soundplayer.setTapsound();
        return super.onTouchEvent(event);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        makeLevel();
        thread.setRunning(true);
        thread.start();
    }

    private void makeLevel() {

        score = 0;
        //adding score in the game with textView
        scoretext.setText("0");
        scoretext.setTextSize(50);
        linearLayout.addView(scoretext);


        characterFlappySprite = new CharacterFlappySprite
                (getResizedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.flappy_bird), 200, 160));
        Bitmap bmp;
        Bitmap bmp2;
        int y;
        int x;
        bmp = getResizedBitmap(BitmapFactory.decodeResource
                (getResources(), R.drawable.pipe_down), 250, Resources.getSystem().getDisplayMetrics().heightPixels / 2);
        bmp2 = getResizedBitmap
                (BitmapFactory.decodeResource(getResources(), R.drawable.pipe_up), 250, Resources.getSystem().getDisplayMetrics().heightPixels / 2);

        pipe1 = new FlappyPipeSprite(bmp, bmp2, 2000, 100);
        pipe2 = new FlappyPipeSprite(bmp, bmp2, 4500, 100);
        pipe3 = new FlappyPipeSprite(bmp, bmp2, 3200, 100);

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public void update() {
        logic();
        characterFlappySprite.update();
        pipe1.update();
        pipe2.update();
        pipe3.update();
    }

    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);
        if (canvas != null) {
            canvas.drawRGB(0, 100, 205);
            canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.background_flappy), 0, 0, null);
            characterFlappySprite.draw(canvas);
            pipe1.draw(canvas);
            pipe2.draw(canvas);
            pipe3.draw(canvas);
            linearLayout.measure(canvas.getWidth(), canvas.getHeight());
            linearLayout.layout(0, 0, canvas.getWidth(), canvas.getHeight());
            linearLayout.draw(canvas);
        }
    }

    public void logic() {

        List<FlappyPipeSprite> pipes = new ArrayList<>();
        pipes.add(pipe1);
        pipes.add(pipe2);
        pipes.add(pipe3);

        for (int i = 0; i < pipes.size(); i++) {
            //Detect if the character is touching one of the pipes
            if (characterFlappySprite.y < pipes.get(i).yY + (screenHeight / 2) - (gapHeight / 2) && characterFlappySprite.x + 200 > pipes.get(i).xX && characterFlappySprite.x < pipes.get(i).xX + 250) {
                soundplayer.setDeathsound();
                resetLevel();
                intent.putExtra("SCORE", (score / 22));
                ((Activity)getContext()).finish();
                ((MainFlappyActivity) getContext()).startActivity(intent);

            } else if (characterFlappySprite.y + 160 > (screenHeight / 2) + (gapHeight / 2) + pipes.get(i).yY && characterFlappySprite.x + 200 > pipes.get(i).xX && characterFlappySprite.x < pipes.get(i).xX + 250) {
                soundplayer.setDeathsound();
                resetLevel();
                intent.putExtra("SCORE", (score / 22));
                ((Activity)getContext()).finish();
                ((MainFlappyActivity) getContext()).startActivity(intent);

            }

            //score
            if (characterFlappySprite.x > pipes.get(i).xX + 250) {
//                    && characterFlappySprite.x < pipes.get(i).xX + 275) {
                //      soundplayer.setPointsound();
                score++;
                scoretext.setText((score/22) + "");
            }

            //Detect if the pipe has gone off the left of the screen and regenerate further ahead
            if (pipes.get(i).xX + 500 < 0) {
                Random r = new Random();
                int value1 = r.nextInt(500);
                int value2 = r.nextInt(500);
                pipes.get(i).xX = screenWidth + value1 + 1000;
                pipes.get(i).yY = value2 - 250;
            }
        }

        if (characterFlappySprite.y > screenHeight) {
            soundplayer.setDeathsound();
            resetLevel();
            intent.putExtra("SCORE", (score / 22));
            ((Activity)getContext()).finish();
            (getContext()).startActivity(intent);
        }
    }


    public void resetLevel() {
        characterFlappySprite.y = 100;
        pipe1.xX = 2000;
        pipe1.yY = 0;
        pipe2.xX = 4500;
        pipe2.yY = 200;
        pipe3.xX = 3200;
        pipe3.yY = 250;
    }

}







