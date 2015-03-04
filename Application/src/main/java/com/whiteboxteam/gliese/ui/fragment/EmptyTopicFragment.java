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
 * Time: 15:43
 */
public class EmptyTopicFragment extends Fragment {

    public static EmptyTopicFragment createFragment() {
        return new EmptyTopicFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_empty_topic, container, false);
    }

}
