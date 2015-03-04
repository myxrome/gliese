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
 * Date: 03.03.2015
 * Time: 19:03
 */
public class TopicFragment extends Fragment {

    public static TopicFragment newInstance() {
        return new TopicFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_topic, container, false);
    }

}
