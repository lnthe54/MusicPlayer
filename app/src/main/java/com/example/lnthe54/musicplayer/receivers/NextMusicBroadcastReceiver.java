package com.example.lnthe54.musicplayer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.lnthe54.musicplayer.config.AppController;
import com.example.lnthe54.musicplayer.service.PlayMusicService;
import com.example.lnthe54.musicplayer.view.activity.PlayMusicActivity;

/**
 * Created by IceMan on 11/29/2016.
 */

public class NextMusicBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PlayMusicActivity musicActivity = (PlayMusicActivity) AppController.getInstance().getPlayMusicActivity();
        PlayMusicService musicService = (PlayMusicService) AppController.getInstance().getPlayMusicService();
        if (musicActivity != null) {
            // musicActivity.nextMusic();

        } else {
            musicService.nextMusic();
        }
        //musicService.showNotification(true);

    }
}
