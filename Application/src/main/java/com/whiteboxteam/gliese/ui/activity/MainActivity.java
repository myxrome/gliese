package com.whiteboxteam.gliese.ui.activity;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import com.whiteboxteam.gliese.R;
import com.whiteboxteam.gliese.data.content.ApplicationContentContract;
import com.whiteboxteam.gliese.data.storage.StorageContract;
import com.whiteboxteam.gliese.ui.fragment.TopicDrawerFragment;


public class MainActivity extends ActionBarActivity {

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private TopicDrawerFragment.TopicFragmentListener drawerListener = new TopicDrawerFragment.TopicFragmentListener() {
        @Override
        public void onTopicSelected(long id) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    };

    public static Intent createIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(drawerToggle);
        if (!isLastTopicExist()) drawerLayout.openDrawer(GravityCompat.START);

        FragmentManager fragmentManager = getSupportFragmentManager();
        TopicDrawerFragment drawerFragment = (TopicDrawerFragment) fragmentManager.findFragmentById(R.id
                .navigation_drawer);
        drawerFragment.setFragmentListener(drawerListener);
    }

    private boolean isLastTopicExist() {
        long topicId = PreferenceManager.getDefaultSharedPreferences(this).getLong(StorageContract.LAST_TOPIC_INDEX,
                10000l);
        Uri topicUri = ContentUris.withAppendedId(ApplicationContentContract.Topic.CONTENT_URI, topicId);
        return ApplicationContentContract.Topic.isExist(this, topicUri);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

}
