package com.whiteboxteam.gliese.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.whiteboxteam.gliese.R;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 04.03.2015
 * Time: 16:15
 */
public class CategoryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, null, false);
    }
}
