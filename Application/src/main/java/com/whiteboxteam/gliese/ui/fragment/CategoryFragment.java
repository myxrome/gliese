package com.whiteboxteam.gliese.ui.fragment;

import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.*;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.whiteboxteam.gliese.R;
import com.whiteboxteam.gliese.data.content.ApplicationContentContract;
import com.whiteboxteam.gliese.data.entity.FactEntity;
import com.whiteboxteam.gliese.data.helper.statistic.FactHelper;
import com.whiteboxteam.gliese.ui.adapter.ValueRecyclerViewAdapter;
import com.whiteboxteam.gliese.ui.custom.CenterItemDecoration;
import com.whiteboxteam.gliese.ui.custom.RoubleTypefaceSpan;
import com.whiteboxteam.gliese.ui.custom.SnappyLinearLayoutManager;
import com.whiteboxteam.gliese.ui.custom.SnappyRecyclerView;
import com.whiteboxteam.gliese.ui.custom.materialdialogs.MaterialDialog;

import java.util.*;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 04.03.2015
 * Time: 16:15
 */
public class CategoryFragment extends Fragment {

    private static final int VALUE_LOADER_ID = 18000;
    private FactHelper               factHelper;
    private FactEntity               categoryTimer;
    private int                      loaderId;
    private long                     categoryId;
    private ValueRecyclerViewAdapter adapter;
    private TextView                 noValueText;
    private ProgressBar              progressBar;

    private List<RecyclerView> recyclerViews = new ArrayList<>();
    private Timer              timer         = new Timer();
    private boolean            scheduled     = false;
    private Typeface roubleSupportedTypeface;
    private int   currentFilterIndex = 0;
    private int[] priceFilters       = new int[] {Integer.MAX_VALUE, 3000, 5000, 10000, 30000};
    private int[] filterContextIds   = new int[] {-1, FactHelper.VirtualContext.FILTER_3000_ID,
                                                  FactHelper.VirtualContext.FILTER_5000_ID,
                                                  FactHelper.VirtualContext.FILTER_10000_ID,
                                                  FactHelper.VirtualContext.FILTER_30000_ID};

    private LoaderManager.LoaderCallbacks<Cursor>   loaderCallbacks      = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (loaderId == id) {
                Uri categoryUri = ContentUris
                        .withAppendedId(ApplicationContentContract.Category.CONTENT_URI, categoryId);
                return new CursorLoader(getActivity(),
                                        ApplicationContentContract.Value.getContentUriByCategory(categoryUri),
                                        new String[] {ApplicationContentContract.Value.ID,
                                                      ApplicationContentContract.Value.NAME,
                                                      ApplicationContentContract.Value.OLD_PRICE,
                                                      ApplicationContentContract.Value.DISCOUNT,
                                                      ApplicationContentContract.Value.NEW_PRICE,
                                                      ApplicationContentContract.Value.LOCAL_THUMB_URI,
                                                      ApplicationContentContract.Value.URL},
                                        ApplicationContentContract.Value.ACTIVE + " = ? AND " +
                                                ApplicationContentContract.Value.NEW_PRICE + " <= ?",
                                        new String[] {"1", String.valueOf(priceFilters[currentFilterIndex])},
                                        ApplicationContentContract.Value.DISCOUNT + " DESC");
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (loaderId == loader.getId()) {
                adapter.changeCursor(data);
                progressBar.setVisibility(View.INVISIBLE);
                int visibility = data.getCount() > 0 ? View.INVISIBLE : View.VISIBLE;
                noValueText.setVisibility(visibility);
                if (getUserVisibleHint()) {
                    scheduleShuffleTimerTask();
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            if (loaderId == loader.getId()) {
                adapter.changeCursor(null);
                progressBar.setVisibility(View.VISIBLE);
            }
        }
    };
    private MaterialDialog.ListCallbackSingleChoice callbackSingleChoice = new MaterialDialog
            .ListCallbackSingleChoice() {
        @Override
        public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
            currentFilterIndex = which;
            if (currentFilterIndex > 0) {
                factHelper.increaseCounter(filterContextIds[currentFilterIndex], FactHelper.ContextType.VIRTUAL_CONTEXT,
                                           FactHelper.EventTag.FILTER_APPLY_COUNTER_EVENT);
            }
            getLoaderManager().restartLoader(loaderId, null, loaderCallbacks);
            return true;
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
        switchCategoryTimer();
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

    private void switchCategoryTimer() {
        if (getUserVisibleHint()) {
            startCategoryTimer();
        } else {
            finishCategoryTimer();
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

    private void startCategoryTimer() {
        if ((factHelper != null) && (categoryTimer == null)) {
            categoryTimer = factHelper.startTimer(categoryId, FactHelper.ContextType.CATEGORY_CONTEXT,
                                                  FactHelper.EventTag.CATEGORY_VIEW_TIMER_EVENT);
        }
    }

    private void finishCategoryTimer() {
        if ((factHelper != null) && (categoryTimer != null)) {
            factHelper.finishTimer(categoryTimer);
            categoryTimer = null;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        factHelper = FactHelper.getInstance(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        roubleSupportedTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/rouble.ttf");
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
        noValueText = (TextView) view.findViewById(R.id.no_value_text);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

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
    public void onResume() {
        super.onResume();
        switchCategoryTimer();
    }

    @Override
    public void onPause() {
        finishCategoryTimer();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        timer.cancel();
        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.actions, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_price_filter:
                new MaterialDialog.Builder(getActivity()).title("Выберите максимальную цену")
                                                         .items(new CharSequence[] {"Нет", buildRoubleString("3 000"),
                                                                                    buildRoubleString("5 000"),
                                                                                    buildRoubleString("10 000"),
                                                                                    buildRoubleString("30 000")})
                                                         .itemsCallbackSingleChoice(currentFilterIndex,
                                                                                    callbackSingleChoice)
                                                         .positiveText("ОК").negativeText("ОТМЕНА").show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private SpannableStringBuilder buildRoubleString(String value) {
        SpannableStringBuilder result = new SpannableStringBuilder(value + " " + '\u20BD');
        RoubleTypefaceSpan typefaceSpan = new RoubleTypefaceSpan(roubleSupportedTypeface);
        int position = value.length() + 1;
        result.setSpan(typefaceSpan, position, position + 1, 0);
        return result;
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
