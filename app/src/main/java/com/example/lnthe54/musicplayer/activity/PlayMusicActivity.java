package com.example.lnthe54.musicplayer.activity;

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

import com.example.lnthe54.musicplayer.R;
import com.example.lnthe54.musicplayer.adapter.SongAdapter;
import com.example.lnthe54.musicplayer.config.Config;
import com.example.lnthe54.musicplayer.model.Songs;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author lnthe54 on 8/21/2018
 * @project MusicPlayer
 */
public class PlayMusicActivity extends AppCompatActivity implements SongAdapter.onCallBack, View.OnClickListener {
    private Toolbar toolbar;
    private ImageView ivPreviousTrack, ivNextTrack, ivPlay, ivPause;
    private RecyclerView rvSong;
    private ArrayList<Songs> listSong = new ArrayList<>();
    private SongAdapter songAdapter;

    private String nameSong;
    private String nameSinger;
    private long id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        Intent intent = getIntent();
        nameSong = intent.getStringExtra(Config.NAME_SONG);
        nameSinger = intent.getStringExtra(Config.NAME_SINGER);
        id = intent.getLongExtra(Config.ID_SONG, 0);
        listSong = intent.getParcelableArrayListExtra(Config.LIST_SONG);

        initViews();
        addEvents();
        setPlayMusic();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(nameSong);
        toolbar.setSubtitle(nameSinger);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ivPreviousTrack = findViewById(R.id.iv_previous_track);
        ivNextTrack = findViewById(R.id.iv_next_track);

        rvSong = findViewById(R.id.rv_song);
        rvSong.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvSong.setHasFixedSize(true);

        songAdapter = new SongAdapter(this, listSong);

        rvSong.setAdapter(songAdapter);

        ivPause = findViewById(R.id.iv_pause);
        ivPlay = findViewById(R.id.iv_play);
    }

    public void addEvents() {
        ivPlay.setOnClickListener(this);
        ivPause.setOnClickListener(this);
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
                Intent intent = new Intent();
                intent.putExtra(Config.NAME_SONG, nameSong);
                intent.putExtra(Config.NAME_SINGER, nameSinger);
                setResult(RESULT_OK, intent);
                finish();
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
        Intent intent = new Intent();
        intent.putExtra(Config.NAME_SONG, nameSong);
        intent.putExtra(Config.NAME_SINGER, nameSinger);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_play: {
                ivPlay.setVisibility(View.INVISIBLE);
                ivPause.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.iv_pause: {
                ivPlay.setVisibility(View.VISIBLE);
                ivPause.setVisibility(View.INVISIBLE);
                break;
            }
        }
    }

    public void setPlayMusic() {
        Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(getApplicationContext(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.start();
    }
}
