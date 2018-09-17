package com.example.lnthe54.musicplayer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

import com.example.lnthe54.musicplayer.config.AppController;
import com.example.lnthe54.musicplayer.service.PlayMusicService;
import com.example.lnthe54.musicplayer.view.activity.PlayMusicActivity;

/**
 * Created by IceMan on 12/11/2016.
 */

public class HeadSetReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PlayMusicActivity musicActivity = (PlayMusicActivity) AppController.getInstance().getPlayMusicActivity();
        PlayMusicService musicService = (PlayMusicService) AppController.getInstance().getPlayMusicService();
        if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
            if (musicActivity != null) {
                musicActivity.pauseMusic();
            } else {
                musicService.pauseMusic();
            }
            musicService.showNotification(true);
        }

    }
}
