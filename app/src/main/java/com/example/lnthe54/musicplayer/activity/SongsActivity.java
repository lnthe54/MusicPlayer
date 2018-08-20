package com.example.lnthe54.musicplayer.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lnthe54.musicplayer.R;
import com.example.lnthe54.musicplayer.adapter.SongAdapter;
import com.example.lnthe54.musicplayer.model.Songs;

import java.util.ArrayList;

public class SongsActivity extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView rvListSong;
    private SongAdapter songAdapter;
    private ArrayList<Songs> listSong;

    private OnFragmentInteractionListener mListener;

    public SongsActivity() {

    }

    public static SongsActivity newInstance(String param1, String param2) {
        SongsActivity fragment = new SongsActivity();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_songs, container, false);

        rvListSong = view.findViewById(R.id.rv_songs);
        rvListSong.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        rvListSong.setHasFixedSize(true);

        listSong = new ArrayList<>();

        listSong.add(new Songs("Dung Quen Ten Anh", "Hoa Vinh"));
        listSong.add(new Songs("Ghe Qua", "Dick, Tofu"));
        listSong.add(new Songs("Sai Gon Funky", "Dick, 2Can"));
        listSong.add(new Songs("Co Gai Ban Ben", "Den, Lynk Lee"));
        listSong.add(new Songs("Benh Cua Anh", "Khoi"));
        listSong.add(new Songs("Ghe Qua", "Den"));
        listSong.add(new Songs("Di Theo Bong Mat Troi", "Den, Giang Nguyen"));
        listSong.add(new Songs("Im Lang", "LK, P.A"));
        listSong.add(new Songs("Mo", "Den, Hau Vi"));

        songAdapter = new SongAdapter(listSong);

        rvListSong.setAdapter(songAdapter);
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
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
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
