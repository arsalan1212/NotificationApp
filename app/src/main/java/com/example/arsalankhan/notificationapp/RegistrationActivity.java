package com.example.arsalankhan.notificationapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity {

    CircleImageView imageView;
    ProgressBar mProgressbar;
    EditText editTextName,editTextEmail,editTextPassword;
    private static int PICK_REQUEST_CODE=100;
    private Uri imageUri =null;
    private String user_id;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDatabase;
    StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextRegEmail);
        editTextPassword = findViewById(R.id.editTextRegPassword);

        imageView = findViewById(R.id.imageView);

        mProgressbar = findViewById(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();

        mStorageRef = FirebaseStorage.getInstance().getReference().child("user_image");

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                PICK_REQUEST_CODE = 100;
                startActivityForResult(intent, PICK_REQUEST_CODE);
            }
        });
    }

    //registration btn
    public void Registration(View view) {
        if(imageUri!=null){

            String name = editTextName.getText().toString();
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();

            if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

                mProgressbar.setVisibility(View.VISIBLE);
                CreateUserAccount(name,email,password);
            }
            else{
                Toast.makeText(this, "Fill the Required Fields", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "Pick Profile Image", Toast.LENGTH_SHORT).show();
        }
    }

    private void CreateUserAccount(final String name, final String email, final String password) {



                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            user_id = mAuth.getCurrentUser().getUid().toString();

                            StorageReference filePath = mStorageRef.child(user_id);
                            filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                    String download_url = task.getResult().getDownloadUrl().toString();

                                    String token_id = FirebaseInstanceId.getInstance().getToken();
                                    Map<String,Object> userMap = new HashMap<>();

                                    userMap.put("name",name);
                                    userMap.put("token_id",token_id);
                                    userMap.put("profile_image",download_url);
                                    mDatabase.collection("Users").document(user_id).set(userMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        mProgressbar.setVisibility(View.GONE);
                                                        finish();
                                                    }
                                                }
                                            });

                                }
                            });
                        }
                        else{
                            mProgressbar.setVisibility(View.GONE);
                            Toast.makeText(RegistrationActivity.this, "Error: "+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== PICK_REQUEST_CODE && resultCode == RESULT_OK){

            imageUri = data.getData();
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                bitmap.compress(Bitmap.CompressFormat.JPEG,80,outputStream);
                Bitmap decode = BitmapFactory.decodeStream(new ByteArrayInputStream(outputStream.toByteArray()));

                imageView.setImageBitmap(decode);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
