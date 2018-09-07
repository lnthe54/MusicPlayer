package com.example.lnthe54.musicplayer.config;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.support.v4.app.ActivityCompat;

/**
 * @author lnthe54 on 9/7/2018
 * @project MusicPlayer
 */
public class AppController extends Application {
    private static AppController controller;

    private Service playMusicService;
    private Activity playMusicActivity;
    private Activity mainActivity;

    public static AppController getInstance() {
        return controller;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        controller = this;
    }

    public Activity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(Activity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public Service getPlayMusicService() {
        return playMusicService;
    }

    public void setPlayMusicService(Service playMusicService) {
        this.playMusicService = playMusicService;
    }

    public Activity getPlayMusicActivity() {
        return playMusicActivity;
    }

    public void setPlayMusicActivity(Activity playMusicActivity) {
        this.playMusicActivity = playMusicActivity;
    }
}
