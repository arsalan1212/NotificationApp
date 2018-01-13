package com.example.arsalankhan.notificationapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationUserDetail extends AppCompatActivity {

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_user_detail);

        textView = findViewById(R.id.tv_notif_userDetail);

        String message = getIntent().getStringExtra("message");
        String id = getIntent().getStringExtra("from_id");

        textView.setText("From: "+id+"\nMessage: "+message);

    }
}
