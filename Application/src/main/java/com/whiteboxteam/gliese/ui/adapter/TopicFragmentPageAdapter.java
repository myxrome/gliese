package com.whiteboxteam.gliese.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.whiteboxteam.gliese.ui.fragment.TopicFragment;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 03.03.2015
 * Time: 18:52
 */
public class TopicFragmentPageAdapter extends FragmentStatePagerAdapter {

    public TopicFragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new TopicFragment();
    }

    @Override
    public int getCount() {
        return 23;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Прекрасно #" + String.valueOf(position);
    }

}
