package com.example.lnthe54.musicplayer.view.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.ImageView;

import com.example.lnthe54.musicplayer.R;
import com.example.lnthe54.musicplayer.adapter.SongAdapter;
import com.example.lnthe54.musicplayer.config.Config;
import com.example.lnthe54.musicplayer.model.entity.Songs;
import com.example.lnthe54.musicplayer.presenter.songaccordingartist.AccordingArtistPresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author lnthe54 on 8/27/2018
 * @project MusicPlayer
 */
public class SongAccordingArtist extends AppCompatActivity implements SongAdapter.onCallBack, AccordingArtistPresenter.View {
    private Toolbar toolbar;
    private ImageView ivArtistBG, ivArtist;
    private RecyclerView rvList;
    private String nameSinger;
    private SongAdapter songAdapter;
    private ArrayList<Songs> listSong;
    private AccordingArtistPresenter artistPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_according_artist);

        artistPresenter = new AccordingArtistPresenter(this);
        getData();
        initViews();
    }

    private void getData() {
        artistPresenter.getDataFromArtistTab();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(nameSinger);
        rvList = findViewById(R.id.rv_list);
        artistPresenter.showListSong();
    }

    @Override
    public void getDataIntent() {
        Intent intent = getIntent();
        nameSinger = intent.getStringExtra(Config.NAME_SINGER);
    }

    @Override
    public void getMusic() {
        ContentResolver contentResolver = getContentResolver();
        Uri song = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(song, null, null, null, null, null);

        if (song != null && songCursor.moveToFirst()) {
            int songID = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtists = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

            do {
                long currentId = songCursor.getLong(songID);
                String currentTitle = songCursor.getString(songTitle);
                String currentArtists = songCursor.getString(songArtists);

                listSong.add(new Songs(currentId, currentTitle, currentArtists));

                Collections.sort(listSong, new Comparator<Songs>() {
                    @Override
                    public int compare(Songs lhs, Songs rhs) {
                        return lhs.getNameSong().compareTo(rhs.getNameSong());
                    }
                });
            } while (songCursor.moveToNext());
        }
    }

    @Override
    public void showData() {
        rvList.setLayoutManager(new LinearLayoutManager(SongAccordingArtist.this,
                LinearLayoutManager.VERTICAL, false));
        rvList.setHasFixedSize(true);
        listSong = new ArrayList<>();
        artistPresenter.getMusic();
        songAdapter = new SongAdapter(this, listSong);
        rvList.setAdapter(songAdapter);
    }

    @Override
    public void backAlbumTab() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_album, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                artistPresenter.backAlbumTab();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickSong(int position) {

    }
}
