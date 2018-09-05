package com.example.lnthe54.musicplayer.presenter.songaccordingalbum;

/**
 * @author lnthe54 on 9/5/2018
 * @project MusicPlayer
 */
public class AccordingAlbumPresenter {

    private View view;

    public AccordingAlbumPresenter(View view) {
        this.view = view;
    }

    public void getDataFromAlbumTab() {
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
