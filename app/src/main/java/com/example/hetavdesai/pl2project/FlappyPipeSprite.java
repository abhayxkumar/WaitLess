package com.example.hetavdesai.pl2project;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;


public class FlappyPipeSprite {

    public int xX, yY;
    private Bitmap image;
    private Bitmap image2;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;


    public FlappyPipeSprite(Bitmap bmp, Bitmap bmp2, int x, int y) {
        image = bmp;
        image2 = bmp2;
        yY = y;
        xX = x;
    }


    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, xX, -(FlappyGameView.gapHeight / 2) + yY, null);
        canvas.drawBitmap(image2, xX, ((screenHeight / 2) + (FlappyGameView.gapHeight / 2)) + yY, null);


    }

    public void update() {

        xX -= FlappyGameView.velocity;


    }

}
