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

    public void addListView() {
        viewMain.addListView();
    }

    public void addGridView() {
        viewMain.addGridView();
    }

    public interface ViewMain {

        void addTabLayout();

        void addViewPager();

        void addListView();

        void addGridView();
    }
}
