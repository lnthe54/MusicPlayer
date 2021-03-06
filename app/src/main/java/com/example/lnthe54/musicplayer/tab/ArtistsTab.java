package com.example.lnthe54.musicplayer.tab;

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
import com.example.lnthe54.musicplayer.activity.SongAccordingArtist;
import com.example.lnthe54.musicplayer.adapter.ArtistsAdapter;
import com.example.lnthe54.musicplayer.config.Config;
import com.example.lnthe54.musicplayer.model.Artists;

import java.util.ArrayList;

public class ArtistsTab extends Fragment implements ArtistsAdapter.OnCallBack {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public static RecyclerView rvArtist;
    private ArrayList<Artists> listArtist;
    private ArtistsAdapter artistsAdapter;

    private OnFragmentInteractionListener mListener;

    public ArtistsTab() {
    }

//    public static ArtistsTab newInstance(String param1, String param2) {
//        ArtistsTab fragment = new ArtistsTab();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artists, container, false);

        rvArtist = view.findViewById(R.id.rv_artists);
        rvArtist.setLayoutManager(new GridLayoutManager(getContext(), Config.NUM_COLUMN));
        rvArtist.setHasFixedSize(true);

        listArtist = new ArrayList<>();

        listArtist.add(new Artists("Den", "10 song | 4 album"));
        listArtist.add(new Artists("Hoa Vinh", "3 song | 1 album"));

        artistsAdapter = new ArtistsAdapter(this, listArtist);
        rvArtist.setAdapter(artistsAdapter);

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
    public void itemClick(int position) {
        Intent openArtist = new Intent(getContext(), SongAccordingArtist.class);

        String nameSinger = listArtist.get(position).getNameArtist();

        openArtist.putExtra(Config.NAME_SINGER, nameSinger);
        startActivityForResult(openArtist, Config.REQUEST_CODE);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

