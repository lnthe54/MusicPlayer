package com.example.lnthe54.musicplayer.view.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lnthe54.musicplayer.R;
import com.example.lnthe54.musicplayer.adapter.SongAdapter;
import com.example.lnthe54.musicplayer.config.Config;
import com.example.lnthe54.musicplayer.model.Songs;
import com.example.lnthe54.musicplayer.presenter.playmusic.PlayMusicPresenter;
import com.example.lnthe54.musicplayer.view.activity.PlayMusicActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.app.Activity.RESULT_OK;

public class SongsTab extends Fragment implements SongAdapter.onCallBack, PlayMusicPresenter.PlayMusicActivity {

    public static RecyclerView rvListSong;
    public static SongAdapter songAdapter;
    public static Uri song;
    public static ArrayList<Songs> listSong;

    private PlayMusicPresenter playPresenter;

    public SongsTab() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_songs, container, false);
        playPresenter = new PlayMusicPresenter(this);

        rvListSong = view.findViewById(R.id.rv_songs);
        playPresenter.showData();
        return view;
    }

    @Override
    public void showData() {
        rvListSong.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        rvListSong.setHasFixedSize(true);

        listSong = new ArrayList<>();
        playPresenter.getMusic();
        songAdapter = new SongAdapter(this, listSong);

        rvListSong.setAdapter(songAdapter);
    }

    @Override
    public void getMusic() {
        ContentResolver contentResolver = getContext().getContentResolver();
        song = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(song,
                new String[]{MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID},
                null, null, MediaStore.Audio.Media.TITLE);

        if (song != null && songCursor.moveToFirst()) {
            do {
                long currentId = songCursor.getLong(songCursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String currentTitle = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String currentArtists = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String currentAlbum = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                int currentDuration = songCursor.getInt(songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String albumPath = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                int albumID = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
                String albumArt = playPresenter.getCoverArtPath(albumID);

                listSong.add(new Songs(currentId, currentTitle, currentArtists, currentAlbum, albumArt, currentDuration, albumPath));

                Collections.sort(listSong, new Comparator<Songs>() {
                    @Override
                    public int compare(Songs one, Songs two) {
                        return one.getNameSong().compareTo(two.getNameSong());
                    }
                });
            } while (songCursor.moveToNext());
        }
    }

    @Override
    public String getCoverArtPath(long albumId) {
        Cursor albumCursor = getContext().getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + " = ?",
                new String[]{Long.toString(albumId)},
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
    public void onClickSong(int position) {
        playPresenter.showPlayMusicActivity(position);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {

                    String nameSong = data.getStringExtra(Config.NAME_SONG);
                    String singerSong = data.getStringExtra(Config.NAME_SINGER);

//                    MainActivity.tvNameSongPlaying.setText(nameSong);
//                    MainActivity.tvAuthorSongPlaying.setText(singerSong);
//                    MainActivity.ivPause.setVisibility(View.VISIBLE);
//                    MainActivity.ivPlay.setVisibility(View.INVISIBLE);
                }
            } else {
                Toast.makeText(getContext(), "Message", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showPlayMusicActivity(int position) {
        Intent openPlayMusic = new Intent(getContext(), PlayMusicActivity.class);

        long songId = listSong.get(position).getId();
        String nameSong = listSong.get(position).getNameSong();
        String nameSinger = listSong.get(position).getAuthor();

        openPlayMusic.putExtra(Config.NAME_SONG, nameSong);
        openPlayMusic.putExtra(Config.NAME_SINGER, nameSinger);
        openPlayMusic.putExtra(Config.ID_SONG, songId);

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Config.LIST_SONG, listSong);

        openPlayMusic.putExtras(bundle);
        startActivityForResult(openPlayMusic, Config.REQUEST_CODE);
    }
}
