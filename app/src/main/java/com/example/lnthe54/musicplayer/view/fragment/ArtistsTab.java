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
import com.example.lnthe54.musicplayer.adapter.ArtistsAdapter;
import com.example.lnthe54.musicplayer.config.Config;
import com.example.lnthe54.musicplayer.model.Artists;
import com.example.lnthe54.musicplayer.presenter.songaccordingartist.ViewArtistPresenter;
import com.example.lnthe54.musicplayer.view.activity.SongAccordingArtist;

import java.util.ArrayList;

public class ArtistsTab extends Fragment implements ArtistsAdapter.OnCallBack, ViewArtistPresenter.View {

    public static RecyclerView rvArtist;
    private ArrayList<Artists> listArtist;
    private ArtistsAdapter artistsAdapter;
    private ViewArtistPresenter artistPresenter;

    public ArtistsTab() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artists, container, false);

        artistPresenter = new ViewArtistPresenter(this);
        listArtist = new ArrayList<>();
        rvArtist = view.findViewById(R.id.rv_artists);
        artistPresenter.getData();
        artistPresenter.showData();
        return view;
    }

    @Override
    public void itemClick(int position) {
        artistPresenter.showAccordingArtist(position);
    }

    @Override
    public void getData() {
        ContentResolver contentResolver = getContext().getContentResolver();
        Uri artists = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(artists,
                new String[]{MediaStore.Audio.Artists.ARTIST, MediaStore.Audio.Artists._ID},
                null, null, MediaStore.Audio.Artists.ARTIST + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists._ID));
                String nameArtist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
                Artists artist = new Artists(id, nameArtist);
                listArtist.add(artist);
            } while (cursor.moveToNext());
        }

    }

    @Override
    public void showData() {
        rvArtist.setLayoutManager(new GridLayoutManager(getContext(), Config.NUM_COLUMN));
        rvArtist.setHasFixedSize(true);
        artistsAdapter = new ArtistsAdapter(this, listArtist);
        rvArtist.setAdapter(artistsAdapter);
    }

    @Override
    public void showAccordingArtist(int position) {
        Intent openArtist = new Intent(getContext(), SongAccordingArtist.class);

        int artistID = listArtist.get(position).getId();
        String nameSinger = listArtist.get(position).getNameArtist();

        openArtist.putExtra(Config.ID_ARTISTS, artistID);
        openArtist.putExtra(Config.NAME_SINGER, nameSinger);
        startActivityForResult(openArtist, Config.REQUEST_CODE);
    }

}

