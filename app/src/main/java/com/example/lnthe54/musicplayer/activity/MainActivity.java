package com.example.lnthe54.musicplayer.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lnthe54.musicplayer.R;
import com.example.lnthe54.musicplayer.adapter.PagerAdapter;
import com.example.lnthe54.musicplayer.config.Config;
import com.example.lnthe54.musicplayer.model.Songs;
import com.example.lnthe54.musicplayer.tab.AlbumsTab;
import com.example.lnthe54.musicplayer.tab.ArtistsTab;
import com.example.lnthe54.musicplayer.tab.SongsTab;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements SongsTab.OnFragmentInteractionListener,
        ArtistsTab.OnFragmentInteractionListener,
        AlbumsTab.OnFragmentInteractionListener, View.OnClickListener, SearchView.OnQueryTextListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private Toolbar toolbar;
    public static ImageView ivPlay, ivPause;
    public static TextView tvNameSongPlaying, tvAuthorSongPlaying;
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkPermissions() == false) {
            return;
        }

        initViews();
        addEvents();
    }

    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String check : permissions) {
                int status = checkSelfPermission(check);
                if (status == PackageManager.PERMISSION_DENIED) {
                    requestPermissions(permissions, 0);
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (checkPermissions() == true) {
            initViews();
        } else {
            finish();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(Config.TITLE_TOOLBAR);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(Config.SONGS));
        tabLayout.addTab(tabLayout.newTab().setText(Config.ALBUMS));
        tabLayout.addTab(tabLayout.newTab().setText(Config.ARTISTS));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = findViewById(R.id.pager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(pagerAdapter.getCount());
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tvNameSongPlaying = findViewById(R.id.tv_name_song_playing);
        tvAuthorSongPlaying = findViewById(R.id.tv_author_song_playing);

        ivPlay = findViewById(R.id.iv_play);
        ivPause = findViewById(R.id.iv_pause);
    }

    public void addEvents() {
        ivPlay.setOnClickListener(this);
        ivPause.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_play: {
                ivPlay.setVisibility(View.INVISIBLE);
                ivPause.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.iv_pause: {
                ivPlay.setVisibility(View.VISIBLE);
                ivPause.setVisibility(View.INVISIBLE);
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_main, menu);
        MenuItem item = menu.findItem(R.id.icon_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.icon_list_view: {
                SongsTab.rvListSong.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
                AlbumsTab.rvListAlbum.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
                ArtistsTab.rvArtist.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
                break;
            }
            case R.id.icon_grid_view: {
                SongsTab.rvListSong.setLayoutManager(new GridLayoutManager(this, Config.NUM_COLUMN));
                AlbumsTab.rvListAlbum.setLayoutManager(new GridLayoutManager(this, Config.NUM_COLUMN));
                ArtistsTab.rvArtist.setLayoutManager(new GridLayoutManager(this, Config.NUM_COLUMN));
                break;
            }
        }
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String text) {
        String userInput = text.toLowerCase();
        List<Songs> newList = new ArrayList<>();
        for (Songs songs : SongsTab.listSong) {
            if (songs.getNameSong().toLowerCase().contains(userInput) ||
                    songs.getAuthor().toLowerCase().contains(userInput)) {
                newList.add(songs);
            }
        }

        SongsTab.songAdapter.updateList(newList);
        return true;
    }
}
