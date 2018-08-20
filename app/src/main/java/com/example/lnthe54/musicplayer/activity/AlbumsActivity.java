package com.example.lnthe54.musicplayer.activity;

import android.content.Context;
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
import com.example.lnthe54.musicplayer.adapter.SongAdapter;
import com.example.lnthe54.musicplayer.config.Config;
import com.example.lnthe54.musicplayer.model.Albums;
import com.example.lnthe54.musicplayer.model.Songs;

import java.util.ArrayList;

public class AlbumsActivity extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private RecyclerView rvListAlbum;
    private ArrayList<Albums> listAlbum;
    private AlbumAdapter albumAdapter;
    private OnFragmentInteractionListener mListener;

    public AlbumsActivity() {
    }

//    public static AlbumsActivity newInstance(String param1, String param2) {
//        AlbumsActivity fragment = new AlbumsActivity();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums, container, false);

        rvListAlbum = view.findViewById(R.id.rv_albums);
        rvListAlbum.setLayoutManager(new GridLayoutManager(getContext(), Config.NUM_COLUMS));
        rvListAlbum.setHasFixedSize(true);

        listAlbum = new ArrayList<>();

        listAlbum.add(new Albums(R.drawable.album1, "Dung Quen Ten Anh", "Hoa Vinh"));
        listAlbum.add(new Albums(R.drawable.album2, "Dung Quen Ten Anh", "Hoa Vinh"));
        listAlbum.add(new Albums(R.drawable.album3, "Dung Quen Ten Anh", "Hoa Vinh"));
        listAlbum.add(new Albums(R.drawable.album4, "Dung Quen Ten Anh", "Hoa Vinh"));
        listAlbum.add(new Albums(R.drawable.album5, "Dung Quen Ten Anh", "Hoa Vinh"));

        albumAdapter = new AlbumAdapter(listAlbum);

        rvListAlbum.setAdapter(albumAdapter);
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
