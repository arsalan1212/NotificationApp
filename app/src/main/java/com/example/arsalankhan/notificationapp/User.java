package com.example.arsalankhan.notificationapp;

/**
 * Created by Arsalan khan on 1/12/2018.
 */

public class User {

    String userId;
    String userName;
    String profileURL;

    public User(){

    }

    public User(String userId, String userName, String profileURL) {
        this.userId = userId;
        this.userName = userName;
        this.profileURL = profileURL;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getProfileURL() {
        return profileURL;
    }
}
