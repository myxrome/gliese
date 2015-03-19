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
import com.whiteboxteam.gliese.data.entity.FactEntity;
import com.whiteboxteam.gliese.data.entity.TopicEntity;
import com.whiteboxteam.gliese.data.helper.statistic.FactHelper;
import com.whiteboxteam.gliese.data.helper.statistic.SessionHelper;
import com.whiteboxteam.gliese.data.storage.StorageContract;
import com.whiteboxteam.gliese.ui.fragment.EmptyTopicFragment;
import com.whiteboxteam.gliese.ui.fragment.TopicDrawerFragment;
import com.whiteboxteam.gliese.ui.fragment.TopicFragment;


public class MainActivity extends ActionBarActivity {

    private static final String COMPARE_SCREEN_TIMER_EVENT = "COMPARE_SCREEN_TIMER";
    private static final String ORIENTATION_TIMER_EVENT = "ORIENTATION_TIMER";
    private static final int COMPARE_SCREEN_ID = 1;
    private static final int LANDSCAPE_ORIENTATION_ID = 1;
    private static final int PORTRAIT_ORIENTATION_ID = 2;
    private SessionHelper sessionHelper;
    private FactHelper factHelper;
    private FactEntity compareScreenTimer;
    private FactEntity orientationTimer;

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private String title;
    private FragmentManager fragmentManager;
    private TopicEntity currentTopic;

    private TopicDrawerFragment.TopicFragmentListener drawerListener = new TopicDrawerFragment.TopicFragmentListener() {
        @Override
        public void onTopicSelected(TopicEntity topic) {
            setTitle(title + " - " + topic.name);
            currentTopic = topic;
//            if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            TopicFragment fragment = TopicFragment.createFragment(topic);
            fragmentManager.beginTransaction().replace(R.id.topic_fragment, fragment).commit();
//            }
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
        title = getTitle().toString();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(drawerToggle);
        if (!isLastTopicExist()) drawerLayout.openDrawer(GravityCompat.START);

        fragmentManager = getSupportFragmentManager();
        TopicDrawerFragment drawerFragment = (TopicDrawerFragment) fragmentManager.findFragmentById(R.id
                .navigation_drawer);
        drawerFragment.setFragmentListener(drawerListener);

//        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
//        });

        sessionHelper = SessionHelper.getInstance(this);
        factHelper = FactHelper.getInstance(this);

        fragmentManager.beginTransaction().replace(R.id.topic_fragment, EmptyTopicFragment.createFragment()).commit();
    }

    private boolean isLastTopicExist() {
        long topicId = PreferenceManager.getDefaultSharedPreferences(this).getLong(StorageContract.LAST_TOPIC_ID,
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
    protected void onPause() {
        factHelper.finishTimer(orientationTimer);
        factHelper.finishTimer(compareScreenTimer);
        sessionHelper.finish();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sessionHelper.getCurrent() == null) sessionHelper.start();
        compareScreenTimer = factHelper.startTimer(COMPARE_SCREEN_ID, FactHelper.SCREEN_CONTEXT,
                COMPARE_SCREEN_TIMER_EVENT);
        int orientation = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ?
                LANDSCAPE_ORIENTATION_ID : PORTRAIT_ORIENTATION_ID;
        orientationTimer = factHelper.startTimer(orientation, FactHelper.ORIENTATION_CONTEXT, ORIENTATION_TIMER_EVENT);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

}
