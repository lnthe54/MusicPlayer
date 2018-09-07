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

    public void showListSong() {
        view.showData();
    }

    public void backAlbumTab() {
        view.backAlbumTab();
    }

    public void showListSongOfAlbum(int albumID) {
        view.showListSongOfAlbum(albumID);
    }

    public String getCoverArtPath(int albumID) {
        view.getCoverArtPath(albumID);
        return null;
    }

    public interface View {
        void getDataIntent();

        void showData();

        void backAlbumTab();

        void showListSongOfAlbum(int albumID);

        String getCoverArtPath(int albumID);
    }
}
