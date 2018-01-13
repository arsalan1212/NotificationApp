package com.example.arsalankhan.notificationapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SendNotificationActivity extends AppCompatActivity {

    TextView textViewName;
    String mUserID;
    FirebaseAuth mAuth;
    FirebaseFirestore mDatabase;
    EditText editTextMessage;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);

        textViewName = findViewById(R.id.tv_notification_userName);
        editTextMessage = findViewById(R.id.et_message);
        progressBar = findViewById(R.id.progressBar_notification);

        mDatabase = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mUserID = getIntent().getStringExtra("user_id");
        String userName = getIntent().getStringExtra("user_name");
        textViewName.setText("Send To "+userName);

    }

    //send message btn
    public void SendMessage(View view) {

        String message = editTextMessage.getText().toString();
        if(!TextUtils.isEmpty(message)){

            progressBar.setVisibility(View.VISIBLE);

            String currentUserID = mAuth.getCurrentUser().getUid();
            Map<String,Object> notificationMap = new HashMap<>();
            notificationMap.put("from",currentUserID);
            notificationMap.put("message",message);

            mDatabase.collection("Users").document(mUserID).collection("notification").add(notificationMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {

                        if(task.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(SendNotificationActivity.this, "Message is send", Toast.LENGTH_SHORT).show();
                        }else{
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(SendNotificationActivity.this, "Error: "+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SendNotificationActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(this, "Fill the Required Field", Toast.LENGTH_SHORT).show();
        }
    }
}
