package com.example.lnthe54.musicplayer.view.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.lnthe54.musicplayer.R;
import com.example.lnthe54.musicplayer.adapter.PagerAdapter;
import com.example.lnthe54.musicplayer.config.AppController;
import com.example.lnthe54.musicplayer.config.Config;
import com.example.lnthe54.musicplayer.config.ConfigService;
import com.example.lnthe54.musicplayer.model.Songs;
import com.example.lnthe54.musicplayer.presenter.mainpresenter.MainPresenter;
import com.example.lnthe54.musicplayer.service.PlayMusicService;
import com.example.lnthe54.musicplayer.view.fragment.AlbumsTab;
import com.example.lnthe54.musicplayer.view.fragment.ArtistsTab;
import com.example.lnthe54.musicplayer.view.fragment.SongsTab;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, SearchView.OnQueryTextListener, MainPresenter.ViewMain {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private Toolbar toolbar;
    private TextView tvNameSongCurrent, tvAuthorSongCurrent;
    private ImageView ivPlayPause;
    private SeekBar seekBarPlaying;
    private RelativeLayout songPlaying;
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private MainPresenter mainPresenter;
    private PlayMusicService serviceMusic;

    private int totalTime;
    BroadcastReceiver broadcastReceiverUpdatePlaying = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            serviceMusic = (PlayMusicService) AppController.getInstance().getPlayMusicService();
            if (serviceMusic != null) {
                songPlaying.setVisibility(View.VISIBLE);
            } else {
                songPlaying.setVisibility(View.GONE);
            }
            mainPresenter.showCurrentSong();
            if (serviceMusic != null) {
                totalTime = serviceMusic.getTotalTime();
                mainPresenter.updateSeekBar();
                if (serviceMusic.isPlaying()) {
                    ivPlayPause.setImageResource(R.drawable.pause_button);
                } else {
                    ivPlayPause.setImageResource(R.drawable.play_button);
                }
            }
        }
    };
    private boolean isSeeking;

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
        if (checkPermissions()) {
            initViews();
        } else {
            finish();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppController.getInstance().setMainActivity(this);

        if (!checkPermissions()) {
            return;
        }

        mainPresenter = new MainPresenter(this);
        initViews();

        if (serviceMusic != null) {
            mainPresenter.showCurrentSong();
            mainPresenter.updateSeekBar();
        }

        registerBroadcastUpdatePlaying();
        addEvents();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(Config.TITLE_TOOLBAR);
        setSupportActionBar(toolbar);

        serviceMusic = (PlayMusicService) AppController.getInstance().getPlayMusicService();

        tabLayout = findViewById(R.id.tab_layout);
        mainPresenter.addTabLayout();
        viewPager = findViewById(R.id.pager);
        songPlaying = findViewById(R.id.layout_play);
        songPlaying.setOnClickListener(this);
        tvNameSongCurrent = findViewById(R.id.tv_name_song_playing);
        tvAuthorSongCurrent = findViewById(R.id.tv_author_song_playing);
        ivPlayPause = findViewById(R.id.iv_pause);
        seekBarPlaying = findViewById(R.id.seek_bar);
        mainPresenter.addViewPager();

        if (serviceMusic != null) {
            songPlaying.setVisibility(View.VISIBLE);
            totalTime = serviceMusic.getTotalTime();
        } else {
            songPlaying.setVisibility(View.GONE);
        }

        isSeeking = false;
    }

    private void addEvents() {
        ivPlayPause.setOnClickListener(this);
        mainPresenter.changeSeekBar();
    }


    private void registerBroadcastUpdatePlaying() {
        IntentFilter intentFilter = new IntentFilter(ConfigService.ACTION_UPDATE_PlAY_STATUS);
        registerReceiver(broadcastReceiverUpdatePlaying, intentFilter);
    }

    private void unRegisterBroadcastUpdatePlaying() {
        unregisterReceiver(broadcastReceiverUpdatePlaying);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_pause: {
                mainPresenter.playPauseMusic();
                break;
            }

            case R.id.layout_play: {
                mainPresenter.clickLayoutCurrentSong();
                break;
            }
        }
    }

    @Override
    public void showCurrentSong() {
        if (serviceMusic != null) {
            tvNameSongCurrent.setText(serviceMusic.getCurrentSong().getNameSong());
            tvAuthorSongCurrent.setText(serviceMusic.getCurrentSong().getAuthor());
        }
    }

    @Override
    public void updateSeekBar() {
        seekBarPlaying.setMax(totalTime);
        int currentLength = serviceMusic.getCurrentLength();
        if (!isSeeking) {
            seekBarPlaying.setProgress(currentLength);
        }

        Handler musicHandler = new Handler();
        musicHandler.post(new Runnable() {
            @Override
            public void run() {
                mainPresenter.updateSeekBar();
            }
        });
    }

    @Override
    public void changeSeekBar() {
        seekBarPlaying.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                serviceMusic.seekTo(seekBar.getProgress());
                if (!serviceMusic.isPlaying()) {
                    serviceMusic.resumeMusic();
                    ivPlayPause.setImageResource(R.drawable.pause_button);
                }
                isSeeking = false;
                mainPresenter.updateSeekBar();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSeeking = true;
            }
        });
    }

    @Override
    public void clickLayoutCurrentSong() {
        if (serviceMusic != null) {
            Intent openPlay = new Intent(MainActivity.this, PlayMusicActivity.class);
            openPlay.putExtra(Config.IS_PLAYING, true);
            startActivity(openPlay);
        }
    }

    @Override
    public void playPauseMusic() {
        if (serviceMusic.isPlaying()) {
            ivPlayPause.setImageResource(R.drawable.play_button);
            serviceMusic.pauseMusic();
        } else {
            ivPlayPause.setImageResource(R.drawable.pause_button);
            serviceMusic.resumeMusic();
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
                mainPresenter.addListView();
                break;
            }
            case R.id.icon_grid_view: {
                mainPresenter.addGridView();
                break;
            }
        }
        return true;
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

    @Override
    public void addTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText(Config.SONGS));
        tabLayout.addTab(tabLayout.newTab().setText(Config.ALBUMS));
        tabLayout.addTab(tabLayout.newTab().setText(Config.ARTISTS));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    @Override
    public void addViewPager() {
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
    }

    @Override
    public void addListView() {
        SongsTab.rvListSong.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        AlbumsTab.rvListAlbum.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        ArtistsTab.rvArtist.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void addGridView() {
        SongsTab.rvListSong.setLayoutManager(new GridLayoutManager(this, Config.NUM_COLUMN));
        AlbumsTab.rvListAlbum.setLayoutManager(new GridLayoutManager(this, Config.NUM_COLUMN));
        ArtistsTab.rvArtist.setLayoutManager(new GridLayoutManager(this, Config.NUM_COLUMN));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterBroadcastUpdatePlaying();
    }
}
