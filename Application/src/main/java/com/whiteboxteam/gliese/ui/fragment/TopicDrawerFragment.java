package com.whiteboxteam.gliese.ui.fragment;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SimpleCursorTreeAdapter;
import com.whiteboxteam.gliese.R;
import com.whiteboxteam.gliese.data.content.ApplicationContentContract;

public class TopicDrawerFragment extends Fragment {

    private static final String TAG = "[LOADER]";
    private static final int TOPIC_GROUP_LOADER_ID = -1;
    private SimpleCursorTreeAdapter adapter;
    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
            switch (id) {
                case TOPIC_GROUP_LOADER_ID:
                    Log.d(TAG, "create topic group cursor loader");
                    return new CursorLoader(getActivity(), ApplicationContentContract.TopicGroup.CONTENT_URI, new
                            String[]{ApplicationContentContract.TopicGroup.ID, ApplicationContentContract.TopicGroup
                            .NAME}, ApplicationContentContract.TopicGroup.ACTIVE + " = ?", new String[]{"1"},
                            ApplicationContentContract.TopicGroup.ORDER);
                default:
                    Log.d(TAG, "create topic cursor loader with topic group " + String.valueOf(bundle.getLong
                            (ApplicationContentContract.Topic.TOPIC_GROUP_ID)) + " and position " + String.valueOf(id));
                    Uri topicGroupUri = ContentUris.withAppendedId(ApplicationContentContract.TopicGroup.CONTENT_URI,
                            bundle.getLong(ApplicationContentContract.Topic.TOPIC_GROUP_ID));
                    return new CursorLoader(getActivity(), ApplicationContentContract.Topic.getContentUriByTopicGroup
                            (topicGroupUri), new String[]{ApplicationContentContract.Topic.ID,
                            ApplicationContentContract.Topic.NAME}, ApplicationContentContract.Topic.ACTIVE + " = ?",
                            new String[]{"1"}, ApplicationContentContract.Topic.ORDER);
            }
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            switch (loader.getId()) {
                case TOPIC_GROUP_LOADER_ID:
                    Log.d(TAG, "topic group cursor loading is finished");
                    adapter.setGroupCursor(cursor);
                    break;
                default:
                    Log.d(TAG, "topic cursor loading is finished, position " + String.valueOf(loader.getId()));
                    adapter.setChildrenCursor(loader.getId(), cursor);
                    break;
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            Log.d(TAG, "loader reset " + String.valueOf(loader.getId()));
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new SimpleCursorTreeAdapter(getActivity(), null, android.R.layout.simple_expandable_list_item_1,
                new String[]{ApplicationContentContract.TopicGroup.NAME}, new int[]{android.R.id.text1}, android.R
                .layout.simple_expandable_list_item_1, new String[]{ApplicationContentContract.Topic.NAME}, new
                int[]{android.R.id.text1}) {
            @Override
            protected Cursor getChildrenCursor(Cursor groupCursor) {

                long topicGroupId = groupCursor.getLong(groupCursor.getColumnIndex(ApplicationContentContract
                        .TopicGroup.ID));
                Bundle bundle = new Bundle();
                bundle.putLong(ApplicationContentContract.Topic.TOPIC_GROUP_ID, topicGroupId);
                Log.d(TAG, "calling loader manager for topic loading with topic group " + String.valueOf
                        (topicGroupId) + " and position " + String.valueOf(groupCursor.getPosition()));

                Loader loader = getLoaderManager().getLoader(groupCursor.getPosition());
                if (loader == null || loader.isReset()) {
                    getLoaderManager().initLoader(groupCursor.getPosition(), bundle, loaderCallbacks);
                } else {
                    getLoaderManager().restartLoader(groupCursor.getPosition(), bundle, loaderCallbacks);
                }

                return null;
            }
        };
        ExpandableListView listView = (ExpandableListView) view.findViewById(R.id.drawer_list_view);
        listView.setAdapter(adapter);
        Log.d(TAG, "calling loader manager for topic groups");

        Loader loader = getLoaderManager().getLoader(TOPIC_GROUP_LOADER_ID);
        if (loader == null || loader.isReset()) {
            getLoaderManager().initLoader(TOPIC_GROUP_LOADER_ID, null, loaderCallbacks);
        } else {
            getLoaderManager().restartLoader(TOPIC_GROUP_LOADER_ID, null, loaderCallbacks);
        }
    }

}
