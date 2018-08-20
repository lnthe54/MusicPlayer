package com.example.lnthe54.musicplayer.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.lnthe54.musicplayer.activity.AlbumsActivity;
import com.example.lnthe54.musicplayer.activity.ArtistsActivity;
import com.example.lnthe54.musicplayer.activity.SongsActivity;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    public PagerAdapter(FragmentManager fm, int NumberOfTabs) {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                SongsActivity songs = new SongsActivity();
                return songs;
            case 1:
                AlbumsActivity albums = new AlbumsActivity();
                return albums;
            case 2:
                ArtistsActivity artists = new ArtistsActivity();
                return artists;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
