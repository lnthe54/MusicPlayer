package com.example.lnthe54.musicplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;

import com.example.lnthe54.musicplayer.R;
import com.example.lnthe54.musicplayer.config.Config;

/**
 * @author lnthe54 on 8/21/2018
 * @project MusicPlayer
 */
public class PlayMusicActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView ivPreviousTrack, ivNextTrack;

    private String nameSong;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        Intent intent = getIntent();
        nameSong = intent.getStringExtra(Config.NAME_SONG);
        initViews();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(nameSong);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ivPreviousTrack = findViewById(R.id.iv_previous_track);
        ivNextTrack = findViewById(R.id.iv_next_track);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_play, menu);
        return true;
    }
}
