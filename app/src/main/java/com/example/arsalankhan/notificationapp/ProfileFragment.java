package com.example.arsalankhan.notificationapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private TextView textViewUserName;
    private CircleImageView userImageView;
    private Button logoutBtn;

    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_profile, container, false);

       mAuth = FirebaseAuth.getInstance();
        final String userId = mAuth.getCurrentUser().getUid();

        mDatabase = FirebaseFirestore.getInstance();

       logoutBtn = view.findViewById(R.id.btn_logout);
       textViewUserName = view.findViewById(R.id.tv_userName);
       userImageView = view.findViewById(R.id.profile_image);


       mDatabase.collection("Users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
           @Override
           public void onSuccess(DocumentSnapshot documentSnapshot) {

               if(documentSnapshot.exists() && documentSnapshot!=null){

                   String name = documentSnapshot.get("name").toString();
                   String profile_url = documentSnapshot.get("profile_image").toString();

                   textViewUserName.setText(name);

                   Picasso.with(container.getContext()).load(profile_url).placeholder(R.mipmap.ic_launcher).into(userImageView);
               }
           }
       });


       logoutBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               mAuth.signOut();

               Intent intent = new Intent(container.getContext(),LoginActivity.class);
               startActivity(intent);
               getActivity().finish();
           }
       });

       return view;
    }

}
