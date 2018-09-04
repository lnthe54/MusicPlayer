package com.example.lnthe54.musicplayer.presenter.songaccordingalbum;

/**
 * @author lnthe54 on 9/4/2018
 * @project MusicPlayer
 */
public class ViewAlbumPresenter {
    private ViewSongByAlbum viewSongByAlbum;

    public ViewAlbumPresenter(ViewSongByAlbum viewSongByAlbum) {
        this.viewSongByAlbum = viewSongByAlbum;
    }

    public void showSongAccordingAlbum(int position) {
        viewSongByAlbum.showSongAccordingAlbum(position);
    }

    public interface ViewSongByAlbum {
        void showSongAccordingAlbum(int position);
    }
}
