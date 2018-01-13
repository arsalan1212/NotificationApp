package com.example.arsalankhan.notificationapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Arsalan khan on 1/11/2018.
 */

public class PagerAdapter extends FragmentPagerAdapter {
    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                UserFrament userFrament = new UserFrament();
                return userFrament;

            case 1:
                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;

            case 2:
                NotificationFragment notificationFragment = new NotificationFragment();
                return notificationFragment;
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if(position == 0){
            return "All Users";
        }
        else if(position ==1){
            return "Profile";
        }
        else if(position==2){
            return "Notification";
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
