package com.example.lnthe54.musicplayer.presenter.playmusic;

/**
 * @author lnthe54 on 9/5/2018
 * @project MusicPlayer
 */
public class PlayActivityPresenter {
    private View view;

    public PlayActivityPresenter(View view) {
        this.view = view;
    }

    public void backMainActivity(String nameSong, String nameSinger) {
        view.backMainActivity(nameSong, nameSinger);
    }

    public void getDataIntent() {
        view.getDataIntent();
    }

    public void showListSong() {
        view.showListSong();
    }

    public void setPlayMusic() {
        view.setPlayMusic();
    }

    public interface View {
        void backMainActivity(String nameSong, String nameSinger);

        void getDataIntent();

        void showListSong();

        void setPlayMusic();
    }
}
