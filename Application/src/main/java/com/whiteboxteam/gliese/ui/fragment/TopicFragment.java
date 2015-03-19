package com.whiteboxteam.gliese.ui.fragment;

import android.app.Activity;
import android.content.ContentUris;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.whiteboxteam.gliese.R;
import com.whiteboxteam.gliese.data.content.ApplicationContentContract;
import com.whiteboxteam.gliese.data.entity.FactEntity;
import com.whiteboxteam.gliese.data.entity.TopicEntity;
import com.whiteboxteam.gliese.data.helper.statistic.FactHelper;
import com.whiteboxteam.gliese.data.storage.StorageContract;
import com.whiteboxteam.gliese.ui.adapter.TopicFragmentPageAdapter;
import com.whiteboxteam.gliese.ui.custom.PagerSlidingTabStrip;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 03.03.2015
 * Time: 19:03
 */
public class TopicFragment extends Fragment {

    private static final String TOPIC_VIEW_TIMER_EVENT = "TOPIC_VIEW_TIMER";
    private static final String TOPIC_GROUP_VIEW_TIMER_EVENT = "TOPIC_GROUP_VIEW_TIMER";
    private static final int CATEGORY_LOADER_ID = 15000;
    private FactHelper factHelper;
    private FactEntity topicGroupTimer;
    private FactEntity topicTimer;
    private int loaderId;
    private long topicId;
    private long topicGroupId;
    private TopicFragmentPageAdapter adapter;
    private ViewPager viewPager;
    private SharedPreferences preferences;
    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == loaderId) {
                Uri topicUri = ContentUris.withAppendedId(ApplicationContentContract.Topic.CONTENT_URI, topicId);
                return new CursorLoader(getActivity(), ApplicationContentContract.Category.getContentUriByTopic
                        (topicUri), new String[]{ApplicationContentContract.Category.ID, ApplicationContentContract
                        .Category.NAME}, ApplicationContentContract.Category.ACTIVE + "" +
                        " = ?", new String[]{"1"}, ApplicationContentContract.Category.ORDER);

            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (loader.getId() == loaderId) {
                adapter.changeCursor(data);
                int position = adapter.getPosition(preferences.getLong(StorageContract.LAST_CATEGORY_ID + String
                        .valueOf(topicId), -1));
                if (position > -1) {
                    viewPager.setCurrentItem(position);
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            if (loader.getId() == loaderId) {
                adapter.changeCursor(null);
            }
        }
    };
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            long itemId = adapter.getItemId(position);
            preferences.edit().putLong(StorageContract.LAST_CATEGORY_ID + String.valueOf(topicId), itemId).apply();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public static TopicFragment createFragment(TopicEntity topic) {
        TopicFragment fragment = new TopicFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(Parameters.TOPIC_ID, topic.id);
        bundle.putLong(Parameters.TOPIC_GROUP_ID, topic.topicGroupId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        factHelper = FactHelper.getInstance(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        topicId = bundle.getLong(Parameters.TOPIC_ID);
        topicGroupId = bundle.getLong(Parameters.TOPIC_GROUP_ID);
        loaderId = CATEGORY_LOADER_ID + (int) topicId;
        adapter = new TopicFragmentPageAdapter(getChildFragmentManager());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(20);
        PagerSlidingTabStrip slidingTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.sliding_tabs);
        slidingTabStrip.setViewPager(viewPager);
        slidingTabStrip.setOnPageChangeListener(pageChangeListener);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLoaderManager().initLoader(loaderId, null, loaderCallbacks);
    }

    @Override
    public void onResume() {
        super.onResume();
        topicGroupTimer = factHelper.startTimer(topicGroupId, FactHelper.TOPIC_GROUP_CONTEXT,
                TOPIC_GROUP_VIEW_TIMER_EVENT);
        topicTimer = factHelper.startTimer(topicId, FactHelper.TOPIC_CONTEXT, TOPIC_VIEW_TIMER_EVENT);
    }

    @Override
    public void onPause() {
        factHelper.finishTimer(topicTimer);
        factHelper.finishTimer(topicGroupTimer);
        super.onPause();
    }

    private static final class Parameters {
        public static final String TOPIC_ID = "topic-id";
        public static final String TOPIC_GROUP_ID = "topic-group-id";
    }
}
