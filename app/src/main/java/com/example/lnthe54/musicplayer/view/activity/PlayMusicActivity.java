package com.example.lnthe54.musicplayer.view.activity;

import android.content.ContentUris;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author lnthe54 on 8/21/2018
 * @project MusicPlayer
 */
public class PlayMusicActivity extends AppCompatActivity
        implements SongAdapter.onCallBack, View.OnClickListener, PlayActivityPresenter.View {

    private Toolbar toolbar;
    int totalTime;
    int currentPos;
    boolean isShuffle = false;
    boolean isPlaying = true;
    boolean isSeeking;
    private RecyclerView rvSong;
    private ArrayList<Songs> listSong = new ArrayList<>();
    private SongAdapter songAdapter;
    private CircularSeekBar circularSeekBar;
    private ImageView ivPreviousTrack;
    private String nameSong;
    private String nameSinger;
    private long id;
    private ImageView ivNextTrack;
    private ImageView ivPause;
    private TextView tvTimePlayed;
    private PlayMusicService service;
    private String path;

    private PlayActivityPresenter activityPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

//        AppController.getInstance().setPlayMusicActivity(this);
//        service = (PlayMusicService) AppController.getInstance().getPlayMusicService();
        activityPresenter = new PlayActivityPresenter(this);
        getSongTabIntent();
        initViews();
        addEvent();
    }

    private void getSongTabIntent() {
        activityPresenter.getDataIntent();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(nameSong);
        toolbar.setSubtitle(nameSinger);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTimePlayed = findViewById(R.id.tv_time_played);

        ivPreviousTrack = findViewById(R.id.iv_previous_track);
        ivNextTrack = findViewById(R.id.iv_next_track);

        circularSeekBar = findViewById(R.id.circular_seek_bar);
        activityPresenter.changeCircular();

        ivPause = findViewById(R.id.btn_pause);
        rvSong = findViewById(R.id.rv_song);
        activityPresenter.showListSong();
    }


    private void addEvent() {
        ivPause.setOnClickListener(this);
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
        //TODO
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
                //updateSeekBar();
            }
        });
    }

    @Override
    public void updateHome() {
        Intent intent = new Intent(ConfigService.ACTION_UPDATE_PlAY_STATUS);
        sendBroadcast(intent);
    }

    @Override
    public void setAlbumArt() {

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
        service.setDataForNotification(listSong, currentPos, songs, songs.getAlbumImagePath());
        Intent openService = new Intent(this, PlayMusicService.class);
        startService(openService);
        setName();
        setAlbumArt();
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
        service.changePlayPauseState();
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
            ivPause.setImageResource(R.drawable.pause_button1);
            service.pauseMusic();
            activityPresenter.updateHome();
        }
    }

    public void nextMusic() {
        if (!service.isRepeat()) {
            currentPos = service.getNextPosition();
            path = listSong.get(currentPos).getPath();
        }
        activityPresenter.setAlbumArt();
        activityPresenter.playMusic();
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
        nameSong = intent.getStringExtra(Config.NAME_SONG);
        nameSinger = intent.getStringExtra(Config.NAME_SINGER);
        id = intent.getLongExtra(Config.ID_SONG, 0);
        listSong = intent.getParcelableArrayListExtra(Config.LIST_SONG);
    }

    @Override
    public void showListSong() {
        rvSong.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvSong.setHasFixedSize(true);
        songAdapter = new SongAdapter(this, listSong);
        rvSong.setAdapter(songAdapter);
    }
}
