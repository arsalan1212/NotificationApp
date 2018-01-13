package com.example.arsalankhan.notificationapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Arsalan khan on 1/12/2018.
 */

public class MyAdapter  extends RecyclerView.Adapter<MyViewHolder>{

    ArrayList<User> arrayList;
    Context context;
    public MyAdapter(Context context,ArrayList<User> arrayList){

        this.arrayList = arrayList;
        this.context = context;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_user_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final String user_name=arrayList.get(position).getUserName();
        holder.textViewName.setText(user_name);
        Picasso.with(context).load(arrayList.get(position).getProfileURL()).placeholder(R.mipmap.ic_launcher)
                .into(holder.imageView);

        final String userID = arrayList.get(position).userId;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,SendNotificationActivity.class);
                intent.putExtra("user_id",userID);
                intent.putExtra("user_name",user_name);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}

class  MyViewHolder extends RecyclerView.ViewHolder{

    TextView textViewName;
    CircleImageView imageView;
    View itemView;
    public MyViewHolder(View itemView) {
        super(itemView);

        this.itemView = itemView;
        textViewName = itemView.findViewById(R.id.tvUserName_single_row);
        imageView = itemView.findViewById(R.id.user_imageView);
    }
}
