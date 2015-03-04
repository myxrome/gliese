package com.whiteboxteam.gliese.ui.adapter;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.whiteboxteam.gliese.data.content.ApplicationContentContract;
import com.whiteboxteam.gliese.ui.fragment.CategoryFragment;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 03.03.2015
 * Time: 18:52
 */
public class TopicFragmentPageAdapter extends FragmentStatePagerAdapter {

    private Cursor data;
    private int categoryNameColumnIndex;

    public TopicFragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new CategoryFragment();
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.getCount();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (data == null) return "";
        data.moveToPosition(position);
        return data.getString(categoryNameColumnIndex);
    }

    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    private Cursor swapCursor(Cursor cursor) {
        if (data == cursor) return null;
        final Cursor old = data;
        data = cursor;
        if (data != null) {
            categoryNameColumnIndex = data.getColumnIndex(ApplicationContentContract.Category.NAME);
        }
        notifyDataSetChanged();
        return old;
    }

}
