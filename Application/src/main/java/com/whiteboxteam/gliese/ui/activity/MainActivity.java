package com.whiteboxteam.gliese.ui.activity;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.view.View;
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

    private SessionHelper sessionHelper;
    private FactHelper    factHelper;
    private FactEntity    compareScreenTimer;
    private FactEntity    orientationTimer;

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout          drawerLayout;
    private String                title;
    private FragmentManager       fragmentManager;
    private TopicEntity           currentTopic;
    private boolean topicChanged = false;

    private TopicDrawerFragment.TopicFragmentListener fragmentListener = new TopicDrawerFragment
            .TopicFragmentListener() {
        @Override
        public void onTopicSelected(TopicEntity topic) {
            setTitle(title + " - " + topic.name);
            currentTopic = topic;
            if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                TopicFragment fragment = TopicFragment.createFragment(currentTopic);
                fragmentManager.beginTransaction().replace(R.id.topic_fragment, fragment).commit();
            } else {
                topicChanged = true;
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        }
    };
    private DrawerLayout.DrawerListener               drawerListener   = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            drawerToggle.onDrawerSlide(drawerView, slideOffset);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            drawerToggle.onDrawerOpened(drawerView);
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            if (topicChanged) {
                TopicFragment fragment = TopicFragment.createFragment(currentTopic);
                fragmentManager.beginTransaction().replace(R.id.topic_fragment, fragment).commit();
                topicChanged = false;
            }
            drawerToggle.onDrawerClosed(drawerView);
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            drawerToggle.onDrawerStateChanged(newState);
        }
    };

    public static Intent createIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getBoolean(R.bool.force_portrait_mode))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        title = getTitle().toString();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        if (!isLastTopicExist()) drawerLayout.openDrawer(GravityCompat.START);
        drawerLayout.setDrawerListener(drawerListener);

        fragmentManager = getSupportFragmentManager();
        TopicDrawerFragment drawerFragment = (TopicDrawerFragment) fragmentManager.findFragmentById(R.id
                .navigation_drawer);
        drawerFragment.setFragmentListener(fragmentListener);

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
        compareScreenTimer = factHelper.startTimer(FactHelper.VirtualContext.COMPARE_SCREEN_ID, FactHelper
                .ContextType.VIRTUAL_CONTEXT, FactHelper.EventTag.COMPARE_SCREEN_TIMER_EVENT);
        int orientation = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? FactHelper.VirtualContext.LANDSCAPE_ORIENTATION_ID : FactHelper.VirtualContext.PORTRAIT_ORIENTATION_ID;
        orientationTimer = factHelper.startTimer(orientation, FactHelper.ContextType.VIRTUAL_CONTEXT, FactHelper
                .EventTag.ORIENTATION_TIMER_EVENT);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

}
