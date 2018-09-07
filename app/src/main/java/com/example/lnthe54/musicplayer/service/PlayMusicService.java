package com.example.lnthe54.musicplayer.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.lnthe54.musicplayer.R;
import com.example.lnthe54.musicplayer.config.AppController;
import com.example.lnthe54.musicplayer.config.ConfigService;
import com.example.lnthe54.musicplayer.model.Songs;
import com.example.lnthe54.musicplayer.view.activity.MainActivity;
import com.example.lnthe54.musicplayer.view.activity.PlayMusicActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static android.content.ContentValues.TAG;

/**
 * @author lnthe54 on 9/6/2018
 * @project MusicPlayer
 */
public class PlayMusicService extends Service {
    public static final String ACTION_STOP_SERVICE = "com.example.lnthe54.musicplayer.ACTION_STOP_SERVICE";
    private static final int NOTIFICATION_ID = 5498;

    private static MediaPlayer mediaPlayer;
    ArrayList<Songs> lstSongPlaying;
    ArrayList<Integer> histories;
    Random rand;
    boolean isShuffle = false;
    int currentSongPos;
    String albumArtPath;
    Songs currentSong;
    RemoteViews bigViews;
    RemoteViews views;
    NotificationManager notificationManager;
    Notification n;
    AudioManager audioManager;
    int result;
    MediaSessionCompat mediaSession;
    AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            PlayMusicActivity activity = (PlayMusicActivity) AppController.getInstance().getPlayMusicActivity();
            Log.d("check", "focusChange->" + focusChange);
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:

                    if (activity == null) {
                        pauseMusic();
                    } else {
//                        activity.pauseMusic();
                        Log.d("check", "AUDIOFOCUS_LOSS_TRANSIENT");
                    }
                    break;
                case AudioManager.AUDIOFOCUS_GAIN:
                    if (activity == null) {
                        resumeMusic();
                    } else {
//                        activity.resumeMusic();
                        Log.d("check", "AUDIOFOCUS_GAIN");
                    }
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
//                    audioManager.abandonAudioFocus(afChangeListener);
                    if (activity == null) {
                        pauseMusic();
                    } else {
//                        activity.pauseMusic();
                        Log.d("check", "AUDIOFOCUS_LOSS");
                    }

                    break;
            }
        }
    };
    private LocalBinder localBinder = new LocalBinder();
    private boolean isRepeat = false;
    private boolean isShowNotification = false;

    public void setDataForNotification(ArrayList<Songs> lstSong, int currentPos, Songs sogCurrent, String albumArtPath) {
        this.lstSongPlaying = lstSong;
        this.currentSongPos = currentPos;
        this.albumArtPath = albumArtPath;
        this.currentSong = sogCurrent;

//        showLockScreen();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Log.d(TAG, "test called to cancel service");
        if (ACTION_STOP_SERVICE.equals(intent.getAction())) {
            PlayMusicActivity musicActivity = (PlayMusicActivity) AppController.getInstance().getPlayMusicActivity();
            MainActivity mainActivity = (MainActivity) AppController.getInstance().getMainActivity();
            Log.d(TAG, "called to cancel service");
            if (musicActivity != null) {
                musicActivity.changeButton();
            }
//                if(mainActivity != null){
//                    mainActivity.updatePlayPauseButton();
//                }
            if (musicActivity == null && mainActivity == null) {
                stopSelf();
            }
            pauseMusic();
            stopForeground(true);
            isShowNotification = false;

        } else {
//                showNotification(isShowNotification());
//                isShowNotification = true;
        }

        setStatePlayPause();
        return START_NOT_STICKY;
    }

    public void setStatePlayPause() {
        if (mediaSession.getController().getPlaybackState().getState() == PlaybackStateCompat.STATE_PLAYING) {
            mediaSession.setPlaybackState(new PlaybackStateCompat.Builder()
                    .setState(PlaybackStateCompat.STATE_PAUSED, 0, 1.0f)
                    .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                    .build());
        } else {
            mediaSession.setPlaybackState(new PlaybackStateCompat.Builder()
                    .setState(PlaybackStateCompat.STATE_PLAYING, 0, 1.0f)
                    .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                    .build());
        }
    }

    private void releaseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            stopMusic();
            mediaPlayer.release();
        }
    }

    public void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            changePlayPauseState();
        }
    }

    public void resumeMusic() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            changePlayPauseState();
        }
    }

    public void nextMusic() {
        currentSongPos = getNextPosition();
        currentSong = lstSongPlaying.get(currentSongPos);
        String path = currentSong.getPath();
        albumArtPath = currentSong.getAlbumImagePath();
        if (AppController.getInstance().getPlayMusicActivity() != null) {
            setAlbumArt();
        }
        histories.add(currentSongPos);
        playMusic(path);
    }

    public void backMusic() {
        if (currentSongPos == 0) {
            currentSongPos = lstSongPlaying.size();
        } else {
            currentSongPos--;
        }
        currentSong = lstSongPlaying.get(currentSongPos);
        String path = currentSong.getPath();
        albumArtPath = currentSong.getAlbumImagePath();
        if (AppController.getInstance().getPlayMusicActivity() != null) {
            setAlbumArt();
        }
        playMusic(path);
    }

    public void playPauseMusic() {
        if (mediaPlayer.isPlaying()) {
            pauseMusic();
        } else {
            resumeMusic();
        }

    }

    public void playMusic(String path) {
        releaseMusic();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (AppController.getInstance().getPlayMusicActivity() != null) {
                    Intent intent = new Intent(ConfigService.ACTION_COMPLETE_SONG);
                    sendBroadcast(intent);
                    //showNotification(true);
                    //Common.updateMainActivity();
                } else {
                    if (isRepeat()) {
                        playMusic(currentSong.getPath());
                    } else {
                        nextMusic();
                    }
                }
            }
        });
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mediaPlayer.start();
        }
    }

    public void stopMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void changePlayPauseState() {
//        if (isPlaying()) {
//            bigViews.setImageViewResource(R.id.btn_play_pause_noti, R.drawable.pb_pause);
//        } else {
//            bigViews.setImageViewResource(R.id.btn_play_pause_noti, R.drawable.pb_play);
//        }
        startForeground(NOTIFICATION_ID, n);
    }

    private void setAlbumArt() {
        Intent intent1 = new Intent(ConfigService.ACTION_CHANGE_ALBUM_ART);
        //intent1.putExtra(FragmentPlay.KEY_ALBUM_PLAY, lstSongPlaying.get(currentSongPos).getAlbumImagePath());
        sendBroadcast(intent1);
    }

    public int getTotalTime() {
        return mediaPlayer.getDuration() / 1000;
    }

    public int getCurrentLength() {
        return mediaPlayer.getCurrentPosition() / 1000;
    }

    public int getNextPosition() {
        if (!isShuffle) {
            if (currentSongPos == lstSongPlaying.size() - 1) {
                return 0;
            }
        }
        if (histories.size() > lstSongPlaying.size() - 1) {
            histories.remove(0);
        }
        if (currentSongPos < 0) {
            return 0;
        }
        if (isShuffle) {
            int newSongPosition = currentSongPos;

            while (newSongPosition == currentSongPos || histories.contains(newSongPosition))
                newSongPosition = rand.nextInt(lstSongPlaying.size());
            return newSongPosition;
        }
        currentSongPos = currentSongPos + 1;
        return currentSongPos;
    }

    public int getPrePosition() {
        if (isShuffle()) {
            int newSongPosition = currentSongPos;
            while (newSongPosition == currentSongPos) {
                newSongPosition = rand.nextInt(lstSongPlaying.size());
            }
            return newSongPosition;
        }
        int newSongPosition;
        if (currentSongPos == 0) {
            currentSongPos = lstSongPlaying.size() - 1;
        } else {
            currentSongPos--;
        }
        newSongPosition = currentSongPos;
        return newSongPosition;
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public void setRepeat(boolean repeat) {
        this.isRepeat = repeat;
    }

    public ArrayList<Songs> getLstSongPlaying() {
        return lstSongPlaying;
    }

    public boolean isShuffle() {
        return isShuffle;
    }

    public void setShuffle(boolean shuffle) {
        this.isShuffle = shuffle;
    }

    public void seekTo(int seconds) {
        mediaPlayer.seekTo(seconds * 1000);
    }

    public int getCurrentSongPos() {
        return currentSongPos;
    }

    public String getAlbumArtPath() {
        return albumArtPath;
    }

    public Songs getCurrentSong() {
        return currentSong;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        audioManager.abandonAudioFocus(afChangeListener);
    }

    public class LocalBinder extends Binder {
        public PlayMusicService getInstantBoundService() {
            return PlayMusicService.this;
        }
    }

}
