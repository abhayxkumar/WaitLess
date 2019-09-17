package com.example.hetavdesai.pl2project;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.hardware.SensorManager;


public class CharacterFlappySprite {


    public int x, y;
    public int yVelocity2 = 12;
    public int yVelocity = 0;
    public int gravity = 5;
    public int time = 0;
    private Bitmap image;
    private int xVelocity = 10;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;


    public CharacterFlappySprite(Bitmap bmp) {
        image = bmp;
        x = 300;
        y = 100;
    }


    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);

    }

    public void update() {
        yVelocity += gravity;
        y += yVelocity;
        time++;
    }

}





