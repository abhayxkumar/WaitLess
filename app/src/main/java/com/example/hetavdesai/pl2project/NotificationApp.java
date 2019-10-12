package com.example.hetavdesai.pl2project;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class NotificationApp extends Application {

    @Override
    public void onCreate(){
        super.onCreate();

        createChannelNotif();
    }

    public void createChannelNotif(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(MyFirebaseMessagingService.WAITLESS_ID,
                    "WaitLessChannel",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.setDescription("Welcome to waitless ");
            channel.setLightColor(getColor(R.color.colorPrimary));
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100,200,300,400,500,400,200});

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
