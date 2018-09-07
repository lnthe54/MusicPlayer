package com.example.lnthe54.musicplayer.view.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lnthe54.musicplayer.R;
import com.example.lnthe54.musicplayer.adapter.AlbumAdapter;
import com.example.lnthe54.musicplayer.config.Config;
import com.example.lnthe54.musicplayer.model.Albums;
import com.example.lnthe54.musicplayer.presenter.songaccordingalbum.ViewAlbumPresenter;
import com.example.lnthe54.musicplayer.view.activity.SongAccordingAlbum;

import java.util.ArrayList;

public class AlbumsTab extends Fragment implements AlbumAdapter.onCallBack, ViewAlbumPresenter.ViewSongByAlbum {

    public static RecyclerView rvListAlbum;
    private ArrayList<Albums> listAlbum = new ArrayList<>();
    private AlbumAdapter albumAdapter;
    private ViewAlbumPresenter viewAlbumPresenter;

    public AlbumsTab() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums, container, false);

        viewAlbumPresenter = new ViewAlbumPresenter(this);

        rvListAlbum = view.findViewById(R.id.rv_albums);
        viewAlbumPresenter.showListAlbum();

        return view;
    }


    @Override
    public void onClickAlbum(int position) {
        viewAlbumPresenter.showSongAccordingAlbum(position);
    }

    @Override
    public void addData() {
        ContentResolver contentResolver = getContext().getContentResolver();
        Uri album = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(album,
                new String[]{MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums.ALBUM_ART,
                        MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ARTIST},
                null, null, MediaStore.Audio.Albums.ALBUM + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
                String pathArt = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums._ID));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST));

                Albums albums = new Albums(id, title, artist, pathArt);
                listAlbum.add(albums);
            } while (cursor.moveToNext());
        }
    }

    @Override
    public void showListAlbum() {
        rvListAlbum.setLayoutManager(new GridLayoutManager(getContext(), Config.NUM_COLUMN));
        rvListAlbum.setHasFixedSize(true);
        viewAlbumPresenter.addData();
        albumAdapter = new AlbumAdapter(getContext(), this, listAlbum);
        rvListAlbum.setAdapter(albumAdapter);
    }

    @Override
    public void showSongAccordingAlbum(int position) {
        Intent openAlbum = new Intent(getContext(), SongAccordingAlbum.class);
        int idAlbum = listAlbum.get(position).getId();
        String nameSinger = listAlbum.get(position).getAuthor();
        String path = listAlbum.get(position).getPathArtAlbum();

        openAlbum.putExtra(Config.ID_ALBUM, idAlbum);
        openAlbum.putExtra(Config.NAME_SINGER, nameSinger);
        openAlbum.putExtra(Config.IMAGE, path);
        startActivityForResult(openAlbum, Config.REQUEST_CODE);
    }
}
