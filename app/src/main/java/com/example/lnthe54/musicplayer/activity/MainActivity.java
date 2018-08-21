package com.example.lnthe54.musicplayer.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.example.lnthe54.musicplayer.R;
import com.example.lnthe54.musicplayer.adapter.PagerAdapter;
import com.example.lnthe54.musicplayer.config.Config;
import com.example.lnthe54.musicplayer.tab.AlbumsTab;
import com.example.lnthe54.musicplayer.tab.ArtistsTab;
import com.example.lnthe54.musicplayer.tab.SongsTab;

public class MainActivity extends AppCompatActivity
        implements SongsTab.OnFragmentInteractionListener,
        ArtistsTab.OnFragmentInteractionListener,
        AlbumsTab.OnFragmentInteractionListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_main, menu);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
