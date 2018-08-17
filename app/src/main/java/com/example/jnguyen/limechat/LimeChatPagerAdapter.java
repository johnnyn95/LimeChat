package com.example.jnguyen.limechat;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

class LimeChatPagerAdapter extends FragmentPagerAdapter {

    public LimeChatPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                RequestsFragment requestsFragment = new RequestsFragment();
                return  requestsFragment;
            case 1 :
                ChatsFragment chatsFragment = new ChatsFragment();
                return chatsFragment;
            case 2:
                FriendsFragment friendsFragment = new FriendsFragment();
                return friendsFragment;
                default: return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
    public CharSequence getPageTitle(int position){
        switch (position){
            case 0 :
                return "Requests";//Resources.getSystem().getString(R.string.tab_requests);
            case 1 :

                return "Chats";//Resources.getSystem().getString(R.string.tab_chats);
            case 2:

                return "Friends";//Resources.getSystem().getString(R.string.tab_friends);
            default: return null;
        }
    }
}
