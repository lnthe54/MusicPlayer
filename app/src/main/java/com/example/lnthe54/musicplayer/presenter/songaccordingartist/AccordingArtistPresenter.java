package com.example.lnthe54.musicplayer.presenter.songaccordingartist;

import com.example.lnthe54.musicplayer.presenter.songaccordingalbum.AccordingAlbumPresenter;

/**
 * @author lnthe54 on 9/5/2018
 * @project MusicPlayer
 */
public class AccordingArtistPresenter {
    private View view;

    public AccordingArtistPresenter(View view) {
        this.view = view;
    }

    public void getDataFromArtistTab() {
        view.getDataIntent();
    }

    public void getMusic() {
        view.getMusic();
    }

    public void showListSong() {
        view.showData();
    }

    public void backAlbumTab() {
        view.backAlbumTab();
    }

    public interface View {
        void getDataIntent();

        void getMusic();

        void showData();

        void backAlbumTab();
    }
}
