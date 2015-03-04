package com.whiteboxteam.gliese.ui.fragment;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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
import com.whiteboxteam.gliese.ui.adapter.TopicFragmentPageAdapter;
import com.whiteboxteam.gliese.ui.custom.PagerSlidingTabStrip;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 03.03.2015
 * Time: 19:03
 */
public class TopicFragment extends Fragment {

    private static final int CATEGORY_LOADER_ID = 15000;
    private int loaderId;
    private long topicId;
    private TopicFragmentPageAdapter adapter;

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
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            if (loader.getId() == loaderId) {
                adapter.changeCursor(null);
            }
        }
    };

    public static TopicFragment createFragment(long topicId) {
        TopicFragment fragment = new TopicFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(Parameters.TOPIC_ID, topicId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        topicId = bundle.getLong(Parameters.TOPIC_ID);
        loaderId = CATEGORY_LOADER_ID + (int) topicId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic, container, false);

        adapter = new TopicFragmentPageAdapter(getActivity().getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);
        PagerSlidingTabStrip slidingTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.sliding_tabs);
        slidingTabStrip.setViewPager(viewPager);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getSupportLoaderManager().initLoader(loaderId, null, loaderCallbacks);
    }

    private static final class Parameters {
        public static final String TOPIC_ID = "topic-id";
    }
}
