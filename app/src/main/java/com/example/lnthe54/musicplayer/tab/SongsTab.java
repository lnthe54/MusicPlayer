package com.example.lnthe54.musicplayer.tab;

import android.content.ContentResolver;
import android.content.Context;
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
import com.example.lnthe54.musicplayer.activity.MainActivity;
import com.example.lnthe54.musicplayer.activity.PlayMusicActivity;
import com.example.lnthe54.musicplayer.adapter.SongAdapter;
import com.example.lnthe54.musicplayer.config.Config;
import com.example.lnthe54.musicplayer.model.Songs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.app.Activity.RESULT_OK;

public class SongsTab extends Fragment implements SongAdapter.onCallBack {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public static RecyclerView rvListSong;
    public static SongAdapter songAdapter;
    public static Uri song;
    public static ArrayList<Songs> listSong;

    private OnFragmentInteractionListener mListener;

    public SongsTab() {

    }

//    public static SongsTab newInstance(String param1, String param2) {
//        SongsTab fragment = new SongsTab();
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
        View view = inflater.inflate(R.layout.fragment_songs, container, false);

        rvListSong = view.findViewById(R.id.rv_songs);
        rvListSong.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        rvListSong.setHasFixedSize(true);

        listSong = new ArrayList<>();
        getMusic();
        songAdapter = new SongAdapter(this, listSong);

        rvListSong.setAdapter(songAdapter);

        return view;
    }


    public void getMusic() {
        ContentResolver contentResolver = getContext().getContentResolver();
        song = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(song, null, null, null, null, null);

        if (song != null && songCursor.moveToFirst()) {
            int idColumn = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtists = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

            do {
                long currentId = songCursor.getLong(idColumn);
                String currentTitle = songCursor.getString(songTitle);
                String currentArtists = songCursor.getString(songArtists);

                listSong.add(new Songs(currentId, currentTitle, currentArtists));

                Collections.sort(listSong, new Comparator<Songs>() {
                    @Override
                    public int compare(Songs one, Songs two) {
                        return one.getNameSong().compareTo(two.getNameSong());
                    }
                });
            } while (songCursor.moveToNext());
        }
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

    @Override
    public void onClickSong(int position) {
        Intent openPlayMusic = new Intent(getContext(), PlayMusicActivity.class);

        long songId = listSong.get(position).getId();
        String nameSong = listSong.get(position).getNameSong();
        String nameSinger = listSong.get(position).getAuthor();

        openPlayMusic.putExtra(Config.NAME_SONG, nameSong);
        openPlayMusic.putExtra(Config.NAME_SINGER, nameSinger);
        openPlayMusic.putExtra(Config.ID_SONG, songId);

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Config.LIST_SONG, listSong);

        startActivityForResult(openPlayMusic, Config.REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {

                    String nameSong = data.getStringExtra(Config.NAME_SONG);
                    String singerSong = data.getStringExtra(Config.NAME_SINGER);

                    MainActivity.tvNameSongPlaying.setText(nameSong);
                    MainActivity.tvAuthorSongPlaying.setText(singerSong);
                    MainActivity.ivPause.setVisibility(View.VISIBLE);
                    MainActivity.ivPlay.setVisibility(View.INVISIBLE);
                }
            } else {
                Toast.makeText(getContext(), "Message", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
