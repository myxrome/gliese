package com.whiteboxteam.gliese.ui.fragment;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.whiteboxteam.gliese.R;
import com.whiteboxteam.gliese.data.content.ApplicationContentContract;
import com.whiteboxteam.gliese.ui.adapter.ValueRecyclerViewAdapter;
import com.whiteboxteam.gliese.ui.custom.CenterItemDecoration;
import com.whiteboxteam.gliese.ui.custom.SnappyLinearLayoutManager;
import com.whiteboxteam.gliese.ui.custom.SnappyRecyclerView;

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
                        .LOCAL_THUMB_URI, ApplicationContentContract.Value.URL}, ApplicationContentContract.Value
                        .ACTIVE + " = ?", new String[]{"1"}, ApplicationContentContract.Value.DISCOUNT);
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (loaderId == loader.getId()) {
                adapter.changeCursor(data);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        categoryId = bundle.getLong(Parameters.CATEGORY_ID);
        loaderId = VALUE_LOADER_ID + (int) categoryId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, null, false);

        SnappyRecyclerView recyclerView = (SnappyRecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new ValueRecyclerViewAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new SnappyLinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new CenterItemDecoration(getActivity()));

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLoaderManager().initLoader(loaderId, null, loaderCallbacks);
    }

    private static final class Parameters {
        public static final String CATEGORY_ID = "category-id";
    }

}
