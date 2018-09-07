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

    public void changeCircular() {
        view.changeCircularBar();
    }

    public void getDataIntent() {
        view.getDataIntent();
    }

    public void showListSong() {
        view.showListSong();
    }

    public void playPauseMusic() {
        view.playPauseMusic();
    }

    public void updateHome() {
        view.updateHome();
    }

    public void setAlbumArt() {
        view.setAlbumArt();
    }

    public void playMusic() {
        view.playMusic();
    }

    public interface View {
        void backMainActivity(String nameSong, String nameSinger);

        void getDataIntent();

        void showListSong();

        void changeCircularBar();

        void playPauseMusic();

        void updateHome();

        void setAlbumArt();

        void playMusic();
    }
}
