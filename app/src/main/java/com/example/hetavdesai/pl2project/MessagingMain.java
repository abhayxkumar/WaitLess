package com.example.hetavdesai.pl2project;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.messaging.FirebaseMessaging;

public class MessagingMain {


    public static int flag=0;
    Context context;


    MessagingMain(){}

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        generalusers();
//        cartusers();
//    }

    public void generalusers()
    {
        flag=0;
        FirebaseMessaging.getInstance().subscribeToTopic("GeneralUsers")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successful";
                        if (!task.isSuccessful()) {
                            msg = "failed";
                        }
//                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void cartusers()
    {
        flag=1;
        FirebaseMessaging.getInstance().subscribeToTopic("CartUsers")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successful";
                        if (!task.isSuccessful()) {
                            msg = "failed";
                        }
//                         Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

