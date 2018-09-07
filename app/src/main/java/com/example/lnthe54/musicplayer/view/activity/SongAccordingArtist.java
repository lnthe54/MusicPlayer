package com.example.lnthe54.musicplayer.view.activity;

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

import com.example.lnthe54.musicplayer.R;
import com.example.lnthe54.musicplayer.adapter.SongAdapter;
import com.example.lnthe54.musicplayer.config.Config;
import com.example.lnthe54.musicplayer.model.Songs;
import com.example.lnthe54.musicplayer.presenter.songaccordingartist.AccordingArtistPresenter;

import java.util.ArrayList;

/**
 * @author lnthe54 on 8/27/2018
 * @project MusicPlayer
 */
public class SongAccordingArtist extends AppCompatActivity implements SongAdapter.onCallBack, AccordingArtistPresenter.View {
    private Toolbar toolbar;
    private RecyclerView rvList;
    private String nameSinger;
    private int artistID;
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
        listSong = new ArrayList<>();
        rvList = findViewById(R.id.rv_list);
        artistPresenter.showData();
        artistPresenter.showListSongOfAlbum(artistID);
    }

    @Override
    public void getDataIntent() {
        Intent intent = getIntent();
        artistID = intent.getIntExtra(Config.ID_ARTISTS, 0);
        nameSinger = intent.getStringExtra(Config.NAME_SINGER);
    }

    @Override
    public void showListSongOfAlbum(int artistID) {
        Uri artistUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getContentResolver().query(artistUri,
                new String[]{MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID},
                MediaStore.Audio.Media.ARTIST_ID + "=?",
                new String[]{String.valueOf(artistID)}, MediaStore.Audio.Media.TITLE + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                long songId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                int albumID = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                String albumPath = artistPresenter.coverArtArtist(albumID);
                Songs songs = new Songs(songId, title, artist, album, albumPath, duration, path);
                listSong.add(songs);

            } while (cursor.moveToNext());
        }
    }

    @Override
    public void showData() {
        rvList.setLayoutManager(new LinearLayoutManager(SongAccordingArtist.this, LinearLayoutManager.VERTICAL, false));
        rvList.setHasFixedSize(true);
        songAdapter = new SongAdapter(this, listSong);
        rvList.setAdapter(songAdapter);
    }

    @Override
    public String coverArtArtist(int artistID) {
        Cursor albumCursor = getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + " = ?",
                new String[]{Long.toString(artistID)},
                null
        );
        boolean queryResult = albumCursor.moveToFirst();
        String result = null;
        if (queryResult) {
            result = albumCursor.getString(0);
        }
        albumCursor.close();
        return result;
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
