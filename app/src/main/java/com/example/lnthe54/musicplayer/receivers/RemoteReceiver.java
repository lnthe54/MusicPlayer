package com.example.lnthe54.musicplayer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

import com.example.lnthe54.musicplayer.config.AppController;
import com.example.lnthe54.musicplayer.service.PlayMusicService;
import com.example.lnthe54.musicplayer.view.activity.PlayMusicActivity;

/**
 * Created by IceMan on 12/4/2016.
 */

public class RemoteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
            final KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            PlayMusicActivity musicActivity = (PlayMusicActivity) AppController.getInstance().getPlayMusicActivity();
            PlayMusicService musicService = (PlayMusicService) AppController.getInstance().getPlayMusicService();
            if (event != null && event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                        if (musicActivity != null) {
                            //     musicActivity.playPauseMusic();
                        } else {
                            musicService.playPauseMusic();
                        }
                        musicService.setStatePlayPause();
                        //musicService.showNotification(true);
                        break;
                    case KeyEvent.KEYCODE_MEDIA_NEXT:
                        if (musicActivity != null) {
                            //  musicActivity.nextMusic();

                        } else {
                            musicService.nextMusic();
                        }
                        //musicService.showNotification(true);
                        break;
                    case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                        if (musicActivity != null) {
                            //  musicActivity.backMusic();

                        } else {
                            musicService.backMusic();
                        }
                        //musicService.showNotification(true);
                        break;
                }
            }
        }
    }
}
