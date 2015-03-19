package com.whiteboxteam.gliese.ui.fragment;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.whiteboxteam.gliese.R;
import com.whiteboxteam.gliese.data.content.ApplicationContentContract;
import com.whiteboxteam.gliese.ui.adapter.ValueRecyclerViewAdapter;
import com.whiteboxteam.gliese.ui.custom.CenterItemDecoration;
import com.whiteboxteam.gliese.ui.custom.SnappyLinearLayoutManager;
import com.whiteboxteam.gliese.ui.custom.SnappyRecyclerView;

import java.util.*;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 04.03.2015
 * Time: 16:15
 */
public class CategoryFragment extends Fragment {

    private static final int VALUE_LOADER_ID = 18000;
    private int loaderId;
    private long categoryId;
    private ValueRecyclerViewAdapter adapter;
    private List<RecyclerView> recyclerViews = new ArrayList<>();
    private Timer timer = new Timer();
    private boolean scheduled = false;

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (loaderId == id) {
                Uri categoryUri = ContentUris.withAppendedId(ApplicationContentContract.Category.CONTENT_URI,
                        categoryId);
                return new CursorLoader(getActivity(), ApplicationContentContract.Value.getContentUriByCategory
                        (categoryUri), new String[]{ApplicationContentContract.Value.ID, ApplicationContentContract
                        .Value.NAME, ApplicationContentContract.Value.OLD_PRICE, ApplicationContentContract.Value
                        .DISCOUNT, ApplicationContentContract.Value.NEW_PRICE, ApplicationContentContract.Value
                        .LOCAL_THUMB_URI, ApplicationContentContract.Value.URL}, ApplicationContentContract.Value.ACTIVE + " = ?", new String[]{"1"}, ApplicationContentContract.Value.DISCOUNT + " DESC");
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (loaderId == loader.getId()) {
                adapter.changeCursor(data);
                if (getUserVisibleHint()) {
                    scheduleShuffleTimerTask();
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            if (loaderId == loader.getId()) {
                adapter.changeCursor(null);
            }
        }
    };

    public static CategoryFragment createFragment(long categoryId) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(Parameters.CATEGORY_ID, categoryId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            scheduleShuffleTimerTask();
        }

    }

    private void scheduleShuffleTimerTask() {
        if (!scheduled && (adapter != null) && (adapter.getItemCount() > 0)) {
            long startTime = 500;
            List<Integer> indexes = randomArray(recyclerViews.size(), adapter.getItemCount());
            for (int i = 0; i < recyclerViews.size(); i++) {
                timer.schedule(new ShuffleRecycleViewsTimerTask(recyclerViews.get(i), indexes.get(i)), startTime);
                startTime += 200;
            }
            scheduled = true;
        }
    }

    private List<Integer> randomArray(int limit, int size) {
        List<Integer> cache = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            cache.add(i);
        }
        Collections.shuffle(cache);
        List<Integer> result = new ArrayList<>();
        for (int j = 0; j < limit; j++) {
            result.add(cache.get(Math.min(j, cache.size() - 1)));
        }
        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        categoryId = bundle.getLong(Parameters.CATEGORY_ID);
        loaderId = VALUE_LOADER_ID + (int) categoryId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, null, false);

        adapter = new ValueRecyclerViewAdapter(getActivity());

        recyclerViews.clear();
        initRecycleView(view.findViewById(R.id.recycler_view_1));
        initRecycleView(view.findViewById(R.id.recycler_view_2));
        initRecycleView(view.findViewById(R.id.recycler_view_3));
//        initRecycleView(view.findViewById(R.id.recycler_view_4));

        return view;
    }

    private void initRecycleView(View view) {
        if (view != null) {
            SnappyRecyclerView recyclerView = (SnappyRecyclerView) view;
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new SnappyLinearLayoutManager(getActivity()));
            recyclerView.addItemDecoration(new CenterItemDecoration(getActivity()));
            recyclerViews.add(recyclerView);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLoaderManager().initLoader(loaderId, null, loaderCallbacks);
    }

    @Override
    public void onDestroyView() {
        timer.cancel();
        super.onDestroyView();
    }

    private static final class Parameters {
        public static final String CATEGORY_ID = "category-id";
    }

    private class ShuffleRecycleViewsTimerTask extends TimerTask {

        private RecyclerView view;
        private int position;

        public ShuffleRecycleViewsTimerTask(RecyclerView view, int position) {
            this.view = view;
            this.position = position;
        }

        @Override
        public void run() {
            view.smoothScrollToPosition(position);
        }
    }

}
