package com.example.hetavdesai.pl2project;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public class FlappySoundPlayer {
    private static SoundPool soundPool;
    private static int tapsound;
    private static int deathsound;
    private static int pointsound;
    final int SOUND_POOL_MAX = 2;
    private AudioAttributes audioAttributes;

    //private static int hitBlackSound;
    public FlappySoundPlayer(Context context) {

        // SoundPool is deprecated in API level 21. (Lollipop)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(SOUND_POOL_MAX)
                    .build();

        } else {
            soundPool = new SoundPool(SOUND_POOL_MAX, AudioManager.STREAM_MUSIC, 0);
        }

        tapsound = soundPool.load(context, R.raw.flappy_tap, 1);
        deathsound = soundPool.load(context, R.raw.flappy_death, 1);
        pointsound = soundPool.load(context, R.raw.flappy_point, 1);
    }

    public void setTapsound() {
        soundPool.play(tapsound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void setDeathsound() {
        soundPool.play(deathsound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void setPointsound() {
        soundPool.play(pointsound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

}
