package com.example.lnthe54.musicplayer.presenter.songaccordingartist;

/**
 * @author lnthe54 on 9/5/2018
 * @project MusicPlayer
 */
public class ViewArtistPresenter {

    private View view;

    public ViewArtistPresenter(View view) {
        this.view = view;
    }

    public void getData() {
        view.getData();
    }

    public void showAccordingArtist(int position) {
        view.showAccordingArtist(position);
    }

    public interface View {
        void getData();

        void showAccordingArtist(int position);
    }
}
