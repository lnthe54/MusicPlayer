package com.example.lnthe54.musicplayer.presenter.playmusic;

/**
 * @author lnthe54 on 9/4/2018
 * @project MusicPlayer
 */
public class PlayMusicPresenter {
    private PlayMusicActivity viewPlay;

    public PlayMusicPresenter(PlayMusicActivity viewPlay) {
        this.viewPlay = viewPlay;
    }

    public void showPlayMusicActivity(int position) {
        viewPlay.showPlayMusicActivity(position);
    }

    public interface PlayMusicActivity {
        void showPlayMusicActivity(int position);
    }
}
