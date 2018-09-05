package com.example.lnthe54.musicplayer.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lnthe54.musicplayer.R;
import com.example.lnthe54.musicplayer.adapter.AlbumAdapter;
import com.example.lnthe54.musicplayer.config.Config;
import com.example.lnthe54.musicplayer.model.entity.Albums;
import com.example.lnthe54.musicplayer.presenter.songaccordingalbum.ViewAlbumPresenter;
import com.example.lnthe54.musicplayer.view.activity.SongAccordingAlbum;

import java.util.ArrayList;

public class AlbumsTab extends Fragment implements AlbumAdapter.onCallBack, ViewAlbumPresenter.ViewSongByAlbum {

    public static RecyclerView rvListAlbum;
    private ArrayList<Albums> listAlbum;
    private AlbumAdapter albumAdapter;
    private OnFragmentInteractionListener mListener;
    private ViewAlbumPresenter viewAlbumPresenter;

    public AlbumsTab() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums, container, false);

        viewAlbumPresenter = new ViewAlbumPresenter(this);

        rvListAlbum = view.findViewById(R.id.rv_albums);
        viewAlbumPresenter.addData();
        viewAlbumPresenter.showListAlbum();

        return view;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClickAlbum(int position) {
        viewAlbumPresenter.showSongAccordingAlbum(position);
    }

    @Override
    public void addData() {
        listAlbum = new ArrayList<>();

        listAlbum.add(new Albums(R.drawable.album1, "Di Theo Bong Mat Troi", "Den, Giang Nguyen"));
        listAlbum.add(new Albums(R.drawable.album2, "Dung Quen Ten Anh", "Hoa Vinh"));
        listAlbum.add(new Albums(R.drawable.album3, "Ghe Qua", "Dick, ToFu"));
        listAlbum.add(new Albums(R.drawable.album1, "Co Gai Ban Ben", "Den"));
        listAlbum.add(new Albums(R.drawable.album5, "Benh Cua Anh", "Khoi"));

        albumAdapter = new AlbumAdapter(this, listAlbum);
    }

    @Override
    public void showListAlbum() {
        rvListAlbum.setLayoutManager(new GridLayoutManager(getContext(), Config.NUM_COLUMN));
        rvListAlbum.setHasFixedSize(true);
        rvListAlbum.setAdapter(albumAdapter);
    }

    @Override
    public void showSongAccordingAlbum(int position) {
        Intent openAlbum = new Intent(getContext(), SongAccordingAlbum.class);

        String nameSinger = listAlbum.get(position).getAuthor();
        int image = listAlbum.get(position).getImage();

        openAlbum.putExtra(Config.NAME_SINGER, nameSinger);
        openAlbum.putExtra(Config.IMAGE, image);
        startActivityForResult(openAlbum, Config.REQUEST_CODE);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
