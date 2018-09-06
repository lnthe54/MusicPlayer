package com.example.lnthe54.musicplayer.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lnthe54.musicplayer.R;
import com.example.lnthe54.musicplayer.adapter.ArtistsAdapter;
import com.example.lnthe54.musicplayer.config.Config;
import com.example.lnthe54.musicplayer.model.entity.Artists;
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
        rvArtist = view.findViewById(R.id.rv_artists);
        artistPresenter.getData();
        return view;
    }

    @Override
    public void itemClick(int position) {
        artistPresenter.showAccordingArtist(position);
    }

    @Override
    public void getData() {
        rvArtist.setLayoutManager(new GridLayoutManager(getContext(), Config.NUM_COLUMN));
        rvArtist.setHasFixedSize(true);

        listArtist = new ArrayList<>();

        listArtist.add(new Artists("Den", "10 song | 4 album"));
        listArtist.add(new Artists("Hoa Vinh", "3 song | 1 album"));

        artistsAdapter = new ArtistsAdapter(this, listArtist);
        rvArtist.setAdapter(artistsAdapter);
    }

    @Override
    public void showAccordingArtist(int position) {
        Intent openArtist = new Intent(getContext(), SongAccordingArtist.class);

        String nameSinger = listArtist.get(position).getNameArtist();

        openArtist.putExtra(Config.NAME_SINGER, nameSinger);
        startActivityForResult(openArtist, Config.REQUEST_CODE);
    }

}

