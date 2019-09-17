package com.example.hetavdesai.pl2project;

import android.graphics.Canvas;
import android.view.SurfaceHolder;


public class FlappyMainThread extends Thread {
    public static Canvas canvas;
    private FlappyGameView flappyGameView;
    private SurfaceHolder surfaceHolder;
    private boolean running;
    private int targetFPS = 30;
    private double averageFPS;


    public FlappyMainThread(SurfaceHolder surfaceHolder, FlappyGameView flappyGameView) {


        super();
        this.surfaceHolder = surfaceHolder;
        this.flappyGameView = flappyGameView;


    }

    @Override
    public void run() {

        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        long targetTime = 1000 / targetFPS;


        while (running) {
            startTime = System.nanoTime();
            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.flappyGameView.update();
                    this.flappyGameView.draw(canvas);
                }
            } catch (Exception e) {
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;

            try {
                this.sleep(waitTime);
            } catch (Exception e) {
            }

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == targetFPS) {
                averageFPS = 1000 / ((totalTime / frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println(averageFPS);
            }
        }

    }

    public void setRunning(boolean isRunning) {
        running = isRunning;
    }
}
