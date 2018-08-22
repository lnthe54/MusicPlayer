package com.example.lnthe54.musicplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lnthe54.musicplayer.R;
import com.example.lnthe54.musicplayer.adapter.SongAdapter;
import com.example.lnthe54.musicplayer.config.Config;
import com.example.lnthe54.musicplayer.model.Songs;

import java.util.ArrayList;

/**
 * @author lnthe54 on 8/22/2018
 * @project MusicPlayer
 */
public class SongAccordingAlbum extends AppCompatActivity implements SongAdapter.onCallBack {

    private Toolbar toolbar;
    private TextView tvNameSinger;
    private ImageView ivAlbum;
    private RecyclerView rvList;
    private String nameSinger;
    private int image;
    private SongAdapter songAdapter;
    private ArrayList<Songs> listSong;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.according_album_activity);

        Intent intent = getIntent();
        nameSinger = intent.getStringExtra(Config.NAME_SINGER);
        image = intent.getIntExtra(Config.IMAGE, 0);
        initViews();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvNameSinger = findViewById(R.id.tv_name_singer);
        tvNameSinger.setText(nameSinger);

        ivAlbum = findViewById(R.id.iv_album);
        ivAlbum.setImageResource(image);

        rvList = findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(SongAccordingAlbum.this, LinearLayoutManager.VERTICAL, false));
        rvList.setHasFixedSize(true);

        listSong = new ArrayList<>();

        listSong.add(new Songs("Co Gai Ban Ben", "Den, Lynk Lee"));
        listSong.add(new Songs("Ghe Qua", "Den"));
        listSong.add(new Songs("Di Theo Bong Mat Troi", "Den, Giang Nguyen"));
        listSong.add(new Songs("Mo", "Den, Hau Vi"));

        songAdapter = new SongAdapter(this, listSong);
        rvList.setAdapter(songAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_album, menu);
        return true;
    }

    @Override
    public void onClickSong(int position) {

    }
}
