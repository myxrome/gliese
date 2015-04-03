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

    private Cursor categoryCursor;
    private int categoryIdColumnIndex;
    private int categoryNameColumnIndex;

    public TopicFragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public long getItemId(int position) {
        if (categoryCursor == null) return -1;
        categoryCursor.moveToPosition(position);
        return categoryCursor.getLong(categoryIdColumnIndex);
    }

    public int getPosition(long id) {
        if (categoryCursor != null) {
            categoryCursor.moveToPosition(-1);
            while (categoryCursor.moveToNext()) {
                if (categoryCursor.getLong(categoryIdColumnIndex) == id) return categoryCursor.getPosition();
            }
        }
        return -1;
    }

    @Override
    public Fragment getItem(int position) {
        categoryCursor.moveToPosition(position);
        return CategoryFragment.createFragment(categoryCursor.getLong(categoryIdColumnIndex));
    }

    @Override
    public int getCount() {
        return categoryCursor == null ? 0 : categoryCursor.getCount();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (categoryCursor == null) return "";
        categoryCursor.moveToPosition(position);
        return categoryCursor.getString(categoryNameColumnIndex);
    }

    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    private Cursor swapCursor(Cursor cursor) {
        if (categoryCursor == cursor) return null;
        final Cursor old = categoryCursor;
        categoryCursor = cursor;
        if (categoryCursor != null) {
            categoryIdColumnIndex = categoryCursor.getColumnIndex(ApplicationContentContract.Category.ID);
            categoryNameColumnIndex = categoryCursor.getColumnIndex(ApplicationContentContract.Category.NAME);
        }
        notifyDataSetChanged();
        return old;
    }

}
