package com.whiteboxteam.gliese.ui.custom;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 11.03.2015
 * Time: 12:01
 */
public class CenterItemDecoration extends RecyclerView.ItemDecoration {

    private int childWidthPX;
    private int childHeightPX;

    public CenterItemDecoration(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        childWidthPX = Math.round(250 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        childHeightPX = Math.round(400 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildPosition(view);
        outRect.left = (parent.getWidth() / 2) - (childWidthPX / 2);
        if (position == 0) {
            outRect.top = (parent.getHeight() / 2) - (childHeightPX / 2);
        } else if (position == parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = (parent.getHeight() / 2) - (childHeightPX / 2);
        }
    }

}
