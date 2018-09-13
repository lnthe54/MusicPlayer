package com.example.lnthe54.musicplayer.view.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lnthe54.musicplayer.R;
import com.example.lnthe54.musicplayer.adapter.SongAdapter;
import com.example.lnthe54.musicplayer.config.AppController;
import com.example.lnthe54.musicplayer.config.Common;
import com.example.lnthe54.musicplayer.config.Config;
import com.example.lnthe54.musicplayer.config.ConfigService;
import com.example.lnthe54.musicplayer.model.Songs;
import com.example.lnthe54.musicplayer.presenter.playmusic.PlayActivityPresenter;
import com.example.lnthe54.musicplayer.service.PlayMusicService;
import com.example.lnthe54.musicplayer.view.custom.CircularSeekBar;

import java.util.ArrayList;

/**
 * @author lnthe54 on 8/21/2018
 * @project MusicPlayer
 */
public class PlayMusicActivity extends AppCompatActivity
        implements SongAdapter.onCallBack, View.OnClickListener, PlayActivityPresenter.View {

    private static final String TAG = "PlayMusicActivity";
    private Toolbar toolbar;
    private int totalTime;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            PlayMusicService.LocalBinder binder = (PlayMusicService.LocalBinder) iBinder;
            service = binder.getInstantBoundService();
            AppController.getInstance().setPlayMusicService(service);
            service.setRepeat(false);
            activityPresenter.playMusic();
            activityPresenter.updateCircularSeekbar();
            totalTime = service.getTotalTime();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
    private int currentPos;
    BroadcastReceiver broadcastReceiverSongCompleted = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            nextMusic();
            totalTime = service.getTotalTime();
            activityPresenter.updateCircularSeekbar();
            activityPresenter.updateHome();
//            service.showNotification(true);
            Common.updateMainActivity();
        }
    };
    BroadcastReceiver broadcastReceiverSwitchSong = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            currentPos = getIntent().getIntExtra(Config.POSITION_SONG, 0);
            path = listSong.get(currentPos).getPath();
            service.setDataForNotification(listSong,
                    currentPos, listSong.get(currentPos));
            activityPresenter.playMusic();
//            service.showNotification(true);
        }
    };
    private RecyclerView rvSong;
    private ArrayList<Songs> listSong = new ArrayList<>();
    private SongAdapter songAdapter;
    private CircularSeekBar circularSeekBar;
    private boolean isShuffle = false;

    private String nameSong;
    private String nameSinger;

    private ImageView ivPause;
    private TextView tvTimePlayed;
    private PlayMusicService service;
    private String path;

    private PlayActivityPresenter activityPresenter;
    private ImageView ivPreviousTrack, ivNextTrack;
    private boolean isPlaying = true;
    private boolean isSeeking;
    private ImageView ivShuffle, ivRepeat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        AppController.getInstance().setPlayMusicActivity(PlayMusicActivity.this);
        service = (PlayMusicService) AppController.getInstance().getPlayMusicService();
        activityPresenter = new PlayActivityPresenter(this);

        getSongTabIntent();
        initViews();

        if (service == null) {
            initPlayService();
        } else {
            activityPresenter.updateCircularSeekbar();
            setName();
            if (!isPlaying) {
                activityPresenter.playMusic();
            }

            updateRepeatButton();
            updateShuffleButton();
            updatePlayPauseButton();

        }

        registerBroadcastSongComplete();
        registerBroadcastSwitchSong();
        addEvent();

        activityPresenter.updateHome();
    }

    public void initPlayService() {
        Intent intent = new Intent(this, PlayMusicService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    private void getSongTabIntent() {
        activityPresenter.getDataIntent();
    }

    private void initViews() {
        setName();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(nameSong);
        toolbar.setSubtitle(nameSinger);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTimePlayed = findViewById(R.id.tv_time_played);

        ivPreviousTrack = findViewById(R.id.iv_previous_track);
        ivNextTrack = findViewById(R.id.iv_next_track);

        ivRepeat = findViewById(R.id.iv_repeat);
        ivShuffle = findViewById(R.id.iv_shuffle);

        circularSeekBar = findViewById(R.id.circular_seek_bar);
        activityPresenter.changeCircular();

        ivPause = findViewById(R.id.btn_pause);
        rvSong = findViewById(R.id.rv_song);
        activityPresenter.showListSong();

        isSeeking = false;
    }

    private void addEvent() {
        ivPause.setOnClickListener(this);
        ivRepeat.setOnClickListener(this);
        ivShuffle.setOnClickListener(this);
        ivNextTrack.setOnClickListener(this);
        ivPreviousTrack.setOnClickListener(this);
    }

    private void registerBroadcastSongComplete() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConfigService.ACTION_COMPLETE_SONG);
        registerReceiver(broadcastReceiverSongCompleted, intentFilter);
    }

    private void unRegisterBroadcastSongComplete() {
        unregisterReceiver(broadcastReceiverSongCompleted);
    }

    private void registerBroadcastSwitchSong() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConfigService.ACTION_SWITCH_SONG);
        registerReceiver(broadcastReceiverSwitchSong, intentFilter);
    }

    private void unRegisterBroadcastSwitchSong() {
        unregisterReceiver(broadcastReceiverSwitchSong);
    }

    @Override
    public void updateCircularSeekbar() {
        circularSeekBar.setMax(totalTime);
        int currentLength = service.getCurrentLength();

        if (!isSeeking) {
            circularSeekBar.setProgress(currentLength);
            tvTimePlayed.setText(Common.miliSecondToString(currentLength));
        }
        Handler musicHandler = new Handler();
        musicHandler.post(new Runnable() {
            @Override
            public void run() {
                updateCircularSeekbar();
            }
        });
    }

    public void changeButton() {
        ivPause.setImageResource(R.drawable.play_button1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_play, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                activityPresenter.backMainActivity(nameSong, nameSinger);
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickSong(int position) {
    }

    @Override
    public void onBackPressed() {
        activityPresenter.backMainActivity(nameSong, nameSinger);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_pause: {
                activityPresenter.playPauseMusic();
                break;
            }

            case R.id.iv_repeat: {
                if (service == null) {
                    return;
                }

                if (service.isRepeat()) {
                    ivRepeat.setImageResource(R.drawable.repeat_button);
                    service.setRepeat(false);
                } else {
                    ivRepeat.setImageResource(R.drawable.repeat_btn1);
                    service.setRepeat(true);
                }

                break;
            }

            case R.id.iv_shuffle: {
                if (service == null) {
                    return;
                }

                if (service.isShuffle()) {
                    ivShuffle.setImageResource(R.drawable.shuffle_button);
                    service.setShuffle(false);
                } else {
                    ivShuffle.setImageResource(R.drawable.shuffle_btn1);
                    service.setShuffle(true);
                }
                break;
            }

            case R.id.iv_next_track: {
                nextMusic();
                break;
            }

            case R.id.iv_previous_track: {
                backMusic();
                break;
            }
        }
    }

    @Override
    public void changeCircularBar() {
        circularSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
                tvTimePlayed.setText(Common.miliSecondToString(progress));
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {
                isSeeking = true;
            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {
                service.seekTo(seekBar.getProgress());
                if (!service.isPlaying()) {
                    service.resumeMusic();
                    ivPause.setImageResource(R.drawable.pause_button1);
                }
                isSeeking = false;
                activityPresenter.updateCircularSeekbar();
            }
        });
    }

    @Override
    public void updateHome() {
        Intent intent = new Intent(ConfigService.ACTION_UPDATE_PlAY_STATUS);
        sendBroadcast(intent);
    }

    private void setName() {
        nameSong = listSong.get(currentPos).getNameSong();
        nameSinger = listSong.get(currentPos).getAuthor();
    }

    @Override
    public void playMusic() {
        service.playMusic(path);
        totalTime = service.getTotalTime();
        Songs songs = listSong.get(currentPos);
        service.setDataForNotification(listSong, currentPos, songs);
        Intent openService = new Intent(this, PlayMusicService.class);
        startService(openService);
        setName();
//        service.showNotification(true);
        activityPresenter.updateHome();
    }

    @Override
    public void playPauseMusic() {
        if (service.isPlaying()) {
            ivPause.setImageResource(R.drawable.play_button1);
            service.pauseMusic();
        } else {
            ivPause.setImageResource(R.drawable.pause_button1);
            service.resumeMusic();
        }
//        service.changePlayPauseState();
        activityPresenter.updateHome();
    }

    public void resumeMusic() {
        if (!service.isPlaying()) {
            ivPause.setImageResource(R.drawable.pause_button1);
            service.resumeMusic();
            activityPresenter.updateHome();
        }
    }

    public void pauseMusic() {
        if (service.isPlaying()) {
            ivPause.setImageResource(R.drawable.play_button1);
            service.pauseMusic();
            activityPresenter.updateHome();
        }
    }

    public void nextMusic() {
        if (!service.isRepeat()) {
            currentPos = service.getNextPosition();
            path = listSong.get(currentPos).getPath();
        }
        activityPresenter.playMusic();
    }

    public void backMusic() {
        currentPos = service.getPrePosition();
        path = listSong.get(currentPos).getPath();
        activityPresenter.playMusic();
    }

    public void updatePlayPauseButton() {
        if (service != null) {
            if (service.isPlaying()) {
                ivPause.setImageResource(R.drawable.pause_button1);
            } else {
                ivPause.setImageResource(R.drawable.play_button1);
            }
        }
    }

    public void updateShuffleButton() {
        if (service.isShuffle()) {
            ivShuffle.setImageResource(R.drawable.shuffle_btn1);
        } else {
            ivShuffle.setImageResource(R.drawable.shuffle_button);
        }
    }

    public void updateRepeatButton() {
        if (service.isRepeat()) {
            ivRepeat.setImageResource(R.drawable.repeat_btn1);
        } else {
            ivRepeat.setImageResource(R.drawable.repeat_button);
        }
    }

    @Override
    public void backMainActivity(String nameSong, String nameSinger) {
        Intent intent = new Intent();
        intent.putExtra(Config.NAME_SONG, nameSong);
        intent.putExtra(Config.NAME_SINGER, nameSinger);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void getDataIntent() {
        Intent intent = getIntent();
        isPlaying = intent.getExtras().getBoolean(Config.IS_PLAYING);
        if (isPlaying) {
            path = service.getCurrentSong().getPath();
            currentPos = service.getCurrentSongPos();
            listSong = service.getListSongPlaying();
            isShuffle = service.isShuffle();
        } else {
            path = intent.getStringExtra(Config.PATH_SONG);
            currentPos = intent.getIntExtra(Config.POSITION_SONG, 0);
            listSong = intent.getParcelableArrayListExtra(Config.LIST_SONG);
        }

    }

    @Override
    public void showListSong() {
        rvSong.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        rvSong.setHasFixedSize(true);
        songAdapter = new SongAdapter(this, listSong);
        rvSong.setAdapter(songAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterBroadcastSongComplete();
        unRegisterBroadcastSwitchSong();
        AppController.getInstance().setPlayMusicActivity(null);
    }
}
