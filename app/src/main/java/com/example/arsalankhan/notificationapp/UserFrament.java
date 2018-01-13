package com.example.arsalankhan.notificationapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFrament extends Fragment {


    private RecyclerView mRecyclerView;
    private FirebaseFirestore mDatabase;
    private MyAdapter adapter;
    private ArrayList<User> arrayList = new ArrayList<>();
    FirebaseAuth mAuth;
    public UserFrament() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user_frament, container, false);

        mDatabase =FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mRecyclerView = view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        mRecyclerView.setHasFixedSize(true);

        adapter = new MyAdapter(container.getContext(),arrayList);
        mRecyclerView.setAdapter(adapter);

            mDatabase.collection("Users").addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if(arrayList.size()>0){
                        arrayList.clear();
                    }


                        for(DocumentChange documentChange : documentSnapshots.getDocumentChanges()){

                            if(documentChange.getType() == DocumentChange.Type.ADDED){

                                String name = documentChange.getDocument().get("name").toString();
                                String profile = documentChange.getDocument().get("profile_image").toString();
                                String userId = documentChange.getDocument().getId();

                                User user = new User(userId,name,profile);

                                arrayList.add(user);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }


            });



        return view;
    }

}
