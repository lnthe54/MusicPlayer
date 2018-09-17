package com.example.lnthe54.musicplayer.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.widget.RemoteViews;

import com.example.lnthe54.musicplayer.R;
import com.example.lnthe54.musicplayer.config.AppController;
import com.example.lnthe54.musicplayer.config.Common;
import com.example.lnthe54.musicplayer.config.Config;
import com.example.lnthe54.musicplayer.config.ConfigService;
import com.example.lnthe54.musicplayer.model.Songs;
import com.example.lnthe54.musicplayer.receivers.RemoteReceiver;
import com.example.lnthe54.musicplayer.view.activity.MainActivity;
import com.example.lnthe54.musicplayer.view.activity.PlayMusicActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author lnthe54 on 9/6/2018
 * @project MusicPlayer
 */
public class PlayMusicService extends Service {
    public static final String ACTION_STOP_SERVICE = "com.example.lnthe54.musicplayer.ACTION_STOP_SERVICE";
    private static final int NOTIFICATION_ID = 5498;

    private static MediaPlayer mediaPlayer;
    private ArrayList<Songs> lstSongPlaying;
    private ArrayList<Integer> histories;
    private Random rand;
    private boolean isShuffle = false;
    private int currentSongPos;
    private Songs currentSong;
    private RemoteViews bigViews;
    private RemoteViews views;
    private NotificationManager notificationManager;
    private Notification n;
    private AudioManager audioManager;
    private int result;
    private String albumArtPath;
    private LocalBinder localBinder = new LocalBinder();
    private boolean isRepeat = false;
    private boolean isShowNotification = false;

    MediaSessionCompat mediaSession;
    AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            PlayMusicActivity activity = (PlayMusicActivity) AppController.getInstance().getPlayMusicActivity();
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:

                    if (activity == null) {
                        pauseMusic();
                    } else {
                        activity.pauseMusic();
                    }
                    break;
                case AudioManager.AUDIOFOCUS_GAIN:
                    if (activity == null) {
                        resumeMusic();
                    } else {
                        activity.resumeMusic();
                    }
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    if (activity == null) {
                        pauseMusic();
                    } else {
                        activity.pauseMusic();
                    }
                    break;
            }
        }
    };

    public void setDataForNotification(ArrayList<Songs> lstSong, int currentPos, Songs sogCurrent, String albumArtPath) {
        this.lstSongPlaying = lstSong;
        this.currentSongPos = currentPos;
        this.currentSong = sogCurrent;
        this.albumArtPath = albumArtPath;

        showLockScreen();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        result = audioManager.requestAudioFocus(afChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        histories = new ArrayList<>();
        rand = new Random();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (ACTION_STOP_SERVICE.equals(intent.getAction())) {
            PlayMusicActivity musicActivity = (PlayMusicActivity) AppController.getInstance().getPlayMusicActivity();
            MainActivity mainActivity = (MainActivity) AppController.getInstance().getMainActivity();
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
            showNotification(isShowNotification());
            isShowNotification = true;
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

    public void showLockScreen() {
        ComponentName receiver = new ComponentName(getPackageName(), RemoteReceiver.class.getName());
        mediaSession = new MediaSessionCompat(this, "PlayService", receiver, null);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setPlaybackState(new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PAUSED, 0, 1.0f)
                .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                .build());
        mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, currentSong.getAuthor())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, currentSong.getAlbum() + " - " + currentSong.getAuthor())
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, currentSong.getNameSong())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, mediaPlayer.getDuration())
                .build());
        mediaSession.setActive(true);
    }


    public boolean isShowNotification() {
        return isShowNotification;
    }

    public Notification showNotification(boolean isUpdate) {

        bigViews = new RemoteViews(getPackageName(), R.layout.notification);
        views = new RemoteViews(getPackageName(), R.layout.notification);
        Intent intent = new Intent(getApplicationContext(), PlayMusicActivity.class);
        intent.putExtra(Config.IS_PLAYING, true);

        if (isPlaying()) {
            views.setImageViewResource(R.id.iv_pause_notification, R.drawable.pause_notification);
            bigViews.setImageViewResource(R.id.iv_pause_notification, R.drawable.pause_notification);
        } else {
            views.setImageViewResource(R.id.iv_pause_notification, R.drawable.play_notification);
            bigViews.setImageViewResource(R.id.iv_pause_notification, R.drawable.play_notification);
        }


        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentPrev = new Intent(ConfigService.ACTION_PREV);
        intentPrev.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntentPrev = PendingIntent.getBroadcast(getApplicationContext(), 0, intentPrev, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentPlayPause = new Intent(ConfigService.ACTION_PLAY_PAUSE);
        intentPlayPause.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntentPlayPause = PendingIntent.getBroadcast(getApplicationContext(), 0, intentPlayPause, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentNext = new Intent(ConfigService.ACTION_NEXT);
        intentNext.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntentNext = PendingIntent.getBroadcast(getApplicationContext(), 0, intentNext, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentStopSelf = new Intent(this, PlayMusicService.class);
        intentStopSelf.setAction(PlayMusicService.ACTION_STOP_SERVICE);
        PendingIntent pendingIntentStopSelf = PendingIntent.getService(this, 0, intentStopSelf, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setSmallIcon(R.drawable.beats_logo);
        builder.setContentIntent(pendingIntent);
        builder.setContent(views);
        builder.setCustomBigContentView(bigViews);


//        bigViews.setTextViewText(R.id.tv_song_title_noti, currentSong.getTitle());
//        bigViews.setTextViewText(R.id.tv_artist_noti, currentSong.getArtist());
//
//        views.setTextViewText(R.id.tv_song_title_noti, currentSong.getTitle());
//        views.setTextViewText(R.id.tv_artist_noti, currentSong.getArtist());
//
//
        if (albumArtPath != null && !albumArtPath.isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeFile(albumArtPath);
            bigViews.setImageViewBitmap(R.id.iv_notification, bitmap);
            views.setImageViewBitmap(R.id.iv_notification, bitmap);
        } else {
            bigViews.setImageViewResource(R.id.iv_notification, R.drawable.headphones);
            views.setImageViewResource(R.id.iv_notification, R.drawable.headphones);
        }
//
        n = builder.build();
//        bigViews.setOnClickPendingIntent(R.id.btn_close_noti, pendingIntentStopSelf);
//        bigViews.setOnClickPendingIntent(R.id.btn_prev_noti, pendingIntentPrev);
//        bigViews.setOnClickPendingIntent(R.id.btn_next_noti, pendingIntentNext);
//        bigViews.setOnClickPendingIntent(R.id.btn_play_pause_noti, pendingIntentPlayPause);
//
//        views.setOnClickPendingIntent(R.id.btn_close_noti, pendingIntentStopSelf);
//        views.setOnClickPendingIntent(R.id.btn_next_noti, pendingIntentNext);
//        views.setOnClickPendingIntent(R.id.btn_play_pause_noti, pendingIntentPlayPause);

        if (isUpdate) {
            startForeground(NOTIFICATION_ID, n);
        }
        return n;
    }

    public void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
//            changePlayPauseState();
        }
    }

    private void releaseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            stopMusic();
            mediaPlayer.release();
        }
    }

    public void resumeMusic() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
//            changePlayPauseState();
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
                    Common.updateMainActivity();
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

    public void nextMusic() {
        currentSongPos = getNextPosition();
        currentSong = lstSongPlaying.get(currentSongPos);
        String path = currentSong.getPath();
        albumArtPath = currentSong.getAlbumImagePath();
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
        playMusic(path);
    }

    public void playPauseMusic() {
        if (mediaPlayer.isPlaying()) {
            pauseMusic();
        } else {
            resumeMusic();
        }

    }

    public void changePlayPauseState() {
        if (isPlaying()) {
            bigViews.setImageViewResource(R.id.iv_pause_notification, R.drawable.pause_notification);
        } else {
//            bigViews.setImageViewResource(R.id.iv_, R.drawable.pb_play);
        }
        startForeground(NOTIFICATION_ID, n);
    }

    public void stopMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public class LocalBinder extends Binder {
        public PlayMusicService getInstantBoundService() {
            return PlayMusicService.this;
        }
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
        if (isRepeat()) {
            return currentSongPos;
        }
        if (isShuffle) {
            int newSongPosition = currentSongPos;

            while (newSongPosition == currentSongPos || histories.contains(newSongPosition)) {
                newSongPosition = rand.nextInt(lstSongPlaying.size());
            }
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

    public ArrayList<Songs> getListSongPlaying() {
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

    public Songs getCurrentSong() {
        return currentSong;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        audioManager.abandonAudioFocus(afChangeListener);
    }

}
