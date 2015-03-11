package com.whiteboxteam.gliese.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.whiteboxteam.gliese.R;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, null, false);

        SnappyRecyclerView recyclerView = (SnappyRecyclerView)view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new SnappyLinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ValueRecyclerViewAdapter(getActivity()));
        recyclerView.addItemDecoration(new CenterItemDecoration(getActivity()));

        return view;
    }
}
