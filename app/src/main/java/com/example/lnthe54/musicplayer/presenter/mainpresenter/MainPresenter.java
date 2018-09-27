package com.example.lnthe54.musicplayer.presenter.mainpresenter;

/**
 * @author lnthe54 on 9/6/2018
 * @project MusicPlayer
 */
public class MainPresenter {

    private ViewMain viewMain;

    public MainPresenter(ViewMain viewMain) {
        this.viewMain = viewMain;
    }

    public void addTabLayout() {
        viewMain.addTabLayout();
    }

    public void addViewPager() {
        viewMain.addViewPager();
    }

    public void playPauseMusic() {
        viewMain.playPauseMusic();
    }

    public void addListView() {
        viewMain.addListView();
    }

    public void addGridView() {
        viewMain.addGridView();
    }

    public void showCurrentSong() {
        viewMain.showCurrentSong();
    }

    public void updateSeekBar() {
        viewMain.updateSeekBar();
    }

    public void changeSeekBar() {
        viewMain.changeSeekBar();
    }

    public void clickLayoutCurrentSong() {
        viewMain.clickLayoutCurrentSong();
    }

    public interface ViewMain {

        void addTabLayout();

        void addViewPager();

        void addListView();

        void addGridView();

        void playPauseMusic();

        void showCurrentSong();

        void updateSeekBar();

        void changeSeekBar();

        void clickLayoutCurrentSong();

    }
}
