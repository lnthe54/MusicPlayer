package com.example.lnthe54.musicplayer.presenter.songaccordingartist;

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

    public void showListSongOfAlbum(int artistID) {
        view.showListSongOfAlbum(artistID);
    }

    public void showData() {
        view.showData();
    }

    public void backAlbumTab() {
        view.backAlbumTab();
    }

    public String coverArtArtist(int artistID) {
        view.coverArtArtist(artistID);
        return null;
    }

    public interface View {
        void getDataIntent();

        void showListSongOfAlbum(int artistID);

        void showData();

        String coverArtArtist(int artistID);

        void backAlbumTab();
    }
}
