package com.example.lnthe54.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * @author lnthe54 on 9/6/2018
 * @project MusicPlayer
 */
public class PlayMusicService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
