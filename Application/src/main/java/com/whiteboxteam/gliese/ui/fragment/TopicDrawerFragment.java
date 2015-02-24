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
import com.whiteboxteam.gliese.R;
import com.whiteboxteam.gliese.data.content.ApplicationContentContract;
import com.whiteboxteam.gliese.ui.adapter.TopicListViewAdapter;

public class TopicDrawerFragment extends Fragment {

    private static final int TOPIC_GROUP_LOADER_ID = -1;
    private TopicListViewAdapter adapter;

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
            switch (id) {
                case TOPIC_GROUP_LOADER_ID:
                    return new CursorLoader(getActivity(), ApplicationContentContract.TopicGroup.CONTENT_URI, new
                            String[]{ApplicationContentContract.TopicGroup.ID, ApplicationContentContract.TopicGroup
                            .NAME}, ApplicationContentContract.TopicGroup.ACTIVE + " = ?", new String[]{"1"},
                            ApplicationContentContract.TopicGroup.ORDER);
                default:
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
                    adapter.setGroupCursor(cursor);
                    break;
                default:
                    adapter.setChildrenCursor(loader.getId(), cursor);
                    break;
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new TopicListViewAdapter(getActivity()) {
            @Override
            protected void initChildrenCursorLoader(Cursor groupCursor) {
                long topicGroupId = groupCursor.getLong(groupCursor.getColumnIndex(ApplicationContentContract
                        .TopicGroup.ID));
                Bundle bundle = new Bundle();
                bundle.putLong(ApplicationContentContract.Topic.TOPIC_GROUP_ID, topicGroupId);
                getLoaderManager().initLoader(groupCursor.getPosition(), bundle, loaderCallbacks);
            }
        };

        final ExpandableListView listView = (ExpandableListView) view.findViewById(R.id.drawer_list_view);
        listView.setAdapter(adapter);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long
                    id) {
                listView.expandGroup(groupPosition);
                boolean result = listView.setSelectedChild(groupPosition, childPosition, true);
                listView.setSelection(listView.getFlatListPosition(ExpandableListView.getPackedPositionForChild
                        (groupPosition, childPosition)));
                Log.d("[CLICK]", String.valueOf(result));
                Log.d("[CLICK]", String.valueOf(listView.getSelectedId()));
//                listView.setItemChecked(listView.getFlatListPosition(ExpandableListView.getPackedPositionForChild
// (groupPosition, childPosition)), true);
                return true;
            }
        });

        getLoaderManager().initLoader(TOPIC_GROUP_LOADER_ID, null, loaderCallbacks);
    }

}
