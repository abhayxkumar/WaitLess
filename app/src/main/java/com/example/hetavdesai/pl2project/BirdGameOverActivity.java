package com.example.hetavdesai.pl2project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BirdGameOverActivity extends AppCompatActivity {

    public int highScore, score;
    ImageView imageView1, imageView2;
    private Button try_again;
    private Button quit_game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bird_game_over_activity);

        TextView scoreLabel = findViewById(R.id.Score_BirdGame);
        TextView highScoreLabel = findViewById(R.id.high_score);
        imageView1 = findViewById(R.id.Image_Bird1);
        imageView2 = findViewById(R.id.Image_Bird2);
        score = getIntent().getIntExtra("SCORE", 0);
        scoreLabel.setText("SCORE : " + score);

        SharedPreferences settings = getSharedPreferences("HIGH_SCORE_BIRD", Context.MODE_PRIVATE);
        highScore = settings.getInt("HIGH_SCORE", 0);

        if (score > highScore) {
            highScoreLabel.setText("High Score : " + score);

            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE", score);
            editor.commit();
        } else {
            highScoreLabel.setText("High Score : " + highScore);
        }

        try_again = findViewById(R.id.try_again);
        quit_game = findViewById(R.id.quit_game);

        try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BirdGameMainActivity.class));
            }
        });

        quit_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MiniGamesActivity.class);
                //  intent.putExtra("Highscore : ", highScore);
                startActivity(intent);
            }
        });

        ImageView demoImage = findViewById(R.id.Image_Bird1);
        int imagesToShow[] = {R.drawable.birdgame_bird1, R.drawable.birdgame_bird2};

        //used for moving images up and down
//
//        demoImage.setVisibility(View.VISIBLE);
//        Animation mAnimation = new TranslateAnimation(
//                TranslateAnimation.ABSOLUTE, 0f,
//                TranslateAnimation.ABSOLUTE, 0f,
//                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
//                TranslateAnimation.RELATIVE_TO_PARENT, 0.05f);
//        mAnimation.setDuration(1000);
//        mAnimation.setRepeatCount(-1);
//        mAnimation.setRepeatMode(Animation.REVERSE);
//        mAnimation.setInterpolator(new LinearInterpolator());
//        demoImage.setAnimation(mAnimation);

        animate(demoImage, imagesToShow, 0, true);
    }


    //This is to  change images using fading in and out

    private void animate(final ImageView imageView, final int images[], final int imageIndex, final boolean forever) {

        //imageView <-- The View which displays the images
        //images[] <-- Holds R references to the images to display
        //imageIndex <-- index of the first image to show in images[]
        //forever <-- If equals true then after the last image it starts all over again with the first image resulting in an infinite loop. You have been warned.

        int fadeInDuration = 100; // Configure time values here
        int timeBetween = 100;
        int fadeOutDuration = 200;

        imageView.setVisibility(View.VISIBLE);    //Visible or invisible by default - this will apply when the animation ends
        imageView.setImageResource(images[imageIndex]);

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
        fadeIn.setDuration(fadeInDuration);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
        fadeOut.setStartOffset(fadeInDuration + timeBetween);
        fadeOut.setDuration(fadeOutDuration);

        AnimationSet animation = new AnimationSet(false); // change to false
        // animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);
        animation.setRepeatCount(1);
        imageView.setAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                if (images.length - 1 > imageIndex) {
                    animate(imageView, images, imageIndex + 1, forever);//Calls itself until it gets to the end of the array
                } else {
                    if (forever == true) {
                        animate(imageView, images, 0, forever);  //Calls itself to start the animation all over again in a loop if forever = true
                    }
                }
            }

            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }
        });
    }
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_UP) {
//            imageView1.setVisibility(View.INVISIBLE);
//            imageView2.setVisibility(View.VISIBLE);
//        }
//        else {
//            imageView1.setVisibility(View.VISIBLE);
//            imageView2.setVisibility(View.INVISIBLE);
//        }
//        return true;
//    }
}

