package com.whiteboxteam.gliese.ui.fragment;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.whiteboxteam.gliese.R;
import com.whiteboxteam.gliese.data.content.ApplicationContentContract;
import com.whiteboxteam.gliese.ui.adapter.TopicRecyclerViewAdapter;

public class TopicDrawerFragment extends Fragment {

    private static final int TOPIC_GROUP_LOADER_ID = -1;
    private TopicRecyclerViewAdapter adapter;
    private TopicFragmentListener fragmentListener;
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
                    adapter.changeTopicGroupCursor(cursor);
                    break;
                default:
                    adapter.changeTopicCursor(loader.getId(), cursor);
                    break;
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            switch (loader.getId()) {
                case TOPIC_GROUP_LOADER_ID:
                    adapter.changeTopicGroupCursor(null);
                    break;
                default:
                    adapter.changeTopicCursor(loader.getId(), null);
                    break;
            }
        }

    };

    public void setFragmentListener(TopicFragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.drawer_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new TopicRecyclerViewAdapter(getActivity()) {
            @Override
            protected void onTopicSelected(long id, String name) {
                if (fragmentListener != null) {
                    fragmentListener.onTopicSelected(id, name);
                }
            }

            @Override
            protected void initTopicCursorLoader(Cursor groupCursor) {
                long topicGroupId = groupCursor.getLong(groupCursor.getColumnIndex(ApplicationContentContract
                        .TopicGroup.ID));
                Bundle bundle = new Bundle();
                bundle.putLong(ApplicationContentContract.Topic.TOPIC_GROUP_ID, topicGroupId);
                getLoaderManager().initLoader(groupCursor.getPosition(), bundle, loaderCallbacks);
            }
        };
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLoaderManager().initLoader(TOPIC_GROUP_LOADER_ID, null, loaderCallbacks);
    }

    public interface TopicFragmentListener {
        public void onTopicSelected(long id, String name);
    }

}
