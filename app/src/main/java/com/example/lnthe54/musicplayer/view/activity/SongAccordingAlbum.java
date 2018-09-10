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
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.lnthe54.musicplayer.R;
import com.example.lnthe54.musicplayer.adapter.SongAdapter;
import com.example.lnthe54.musicplayer.config.Config;
import com.example.lnthe54.musicplayer.model.Songs;
import com.example.lnthe54.musicplayer.presenter.songaccordingalbum.AccordingAlbumPresenter;

import java.util.ArrayList;

/**
 * @author lnthe54 on 8/22/2018
 * @project MusicPlayer
 */
public class SongAccordingAlbum extends AppCompatActivity
        implements SongAdapter.onCallBack, AccordingAlbumPresenter.View {

    private Toolbar toolbar;
    private ImageView ivAlbumBG, ivAlbum;
    private RecyclerView rvList;
    private int albumID;
    private String nameSinger;
    private String pathImage;
    private SongAdapter songAdapter;
    private ArrayList<Songs> listSong = new ArrayList<>();
    private AccordingAlbumPresenter albumPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_according_album);

        albumPresenter = new AccordingAlbumPresenter(this);

        getData();
        initViews();
    }

    private void getData() {
        albumPresenter.getDataFromAlbumTab();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(nameSinger);

        ivAlbumBG = findViewById(R.id.iv_album_bg);
        Glide.with(this).load(pathImage).into(ivAlbumBG);


        ivAlbum = findViewById(R.id.iv_album);
        Glide.with(this).load(pathImage).into(ivAlbum);

        rvList = findViewById(R.id.rv_list);

        albumPresenter.showListSong();
        albumPresenter.showListSongOfAlbum(albumID);
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
                albumPresenter.backAlbumTab();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getDataIntent() {
        Intent intent = getIntent();
        albumID = intent.getIntExtra(Config.ID_ALBUM, 0);
        nameSinger = intent.getStringExtra(Config.NAME_SINGER);
        pathImage = intent.getStringExtra(Config.IMAGE);
    }

    @Override
    public void showData() {
        rvList.setLayoutManager(new LinearLayoutManager(SongAccordingAlbum.this, LinearLayoutManager.VERTICAL, false));
        rvList.setHasFixedSize(true);
        songAdapter = new SongAdapter(this, listSong);
        rvList.setAdapter(songAdapter);
    }

    @Override
    public void showListSongOfAlbum(int albumId) {
        Uri albumUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getContentResolver().query(albumUri,
                new String[]{MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID},
                MediaStore.Audio.Media.ALBUM_ID + "=?",
                new String[]{String.valueOf(albumId)}, MediaStore.Audio.Media.TITLE + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                int songId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                int albumID = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                String albumPath = albumPresenter.getCoverArtPath(albumID);
                Songs song = new Songs(songId, title, artist, album, albumPath, duration, path);
                listSong.add(song);

            } while (cursor.moveToNext());
        }
    }

    @Override
    public String getCoverArtPath(int albumID) {
        Cursor albumCursor = getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + " = ?",
                new String[]{Long.toString(albumID)},
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
}
