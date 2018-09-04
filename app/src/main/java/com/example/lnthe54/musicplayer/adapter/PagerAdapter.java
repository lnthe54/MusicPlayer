package com.example.lnthe54.musicplayer.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.lnthe54.musicplayer.view.fragment.AlbumsTab;
import com.example.lnthe54.musicplayer.view.fragment.ArtistsTab;
import com.example.lnthe54.musicplayer.view.fragment.SongsTab;

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
                SongsTab songs = new SongsTab();
                return songs;
            case 1:
                AlbumsTab albums = new AlbumsTab();
                return albums;
            case 2:
                ArtistsTab artists = new ArtistsTab();
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
