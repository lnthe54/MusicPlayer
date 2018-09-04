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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author lnthe54 on 8/22/2018
 * @project MusicPlayer
 */
public class SongAccordingAlbum extends AppCompatActivity implements SongAdapter.onCallBack {

    private Toolbar toolbar;
    private ImageView ivAlbumBG, ivAlbum;
    private RecyclerView rvList;
    private String nameSinger;
    private int image;
    private SongAdapter songAdapter;
    private ArrayList<Songs> listSong;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_according_album);

        Intent intent = getIntent();
        nameSinger = intent.getStringExtra(Config.NAME_SINGER);
        image = intent.getIntExtra(Config.IMAGE, 0);
        initViews();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(nameSinger);

        ivAlbumBG = findViewById(R.id.iv_album_bg);
        ivAlbumBG.setImageResource(image);

        ivAlbum = findViewById(R.id.iv_album);
        ivAlbum.setImageResource(image);

        rvList = findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(SongAccordingAlbum.this, LinearLayoutManager.VERTICAL, false));
        rvList.setHasFixedSize(true);

        listSong = new ArrayList<>();

        getMusic();

        songAdapter = new SongAdapter(this, listSong);
        rvList.setAdapter(songAdapter);
    }

    public void getMusic() {
        ContentResolver contentResolver = getContentResolver();
        Uri song = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(song, null, null, null, null, null);

        if (song != null && songCursor.moveToFirst()) {
            int songID = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtists = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

            do {
                long currentID = songCursor.getLong(songID);
                String currentTitle = songCursor.getString(songTitle);
                String currentArtists = songCursor.getString(songArtists);

                listSong.add(new Songs(currentID, currentTitle, currentArtists));

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_album, menu);
        return true;
    }

    @Override
    public void onClickSong(int position) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
