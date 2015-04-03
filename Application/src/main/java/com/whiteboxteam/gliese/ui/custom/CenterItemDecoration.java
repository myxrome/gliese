package com.whiteboxteam.gliese.ui.custom;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import com.whiteboxteam.gliese.R;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 11.03.2015
 * Time: 12:01
 */
public class CenterItemDecoration extends RecyclerView.ItemDecoration {

    private int childHeightPX;
    private int bottomMagrinPX;

    public CenterItemDecoration(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        childHeightPX = Math.round(400 * displayMetrics.scaledDensity);
        bottomMagrinPX = Math.round(context.getResources().getDimension(R.dimen.bottom_margin_item) * displayMetrics
                .scaledDensity);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildPosition(view);

        int height = view.getHeight() > 0 ? view.getHeight() : childHeightPX;
        if (position == 0) {
            outRect.top = (parent.getHeight() / 2) - (height / 2);
            outRect.bottom = bottomMagrinPX;
        } else if (position == parent.getAdapter().getItemCount() - 1) {
            outRect.top = bottomMagrinPX;
            outRect.bottom = (parent.getHeight() / 2) - (height / 2);
        } else {
            outRect.top = bottomMagrinPX;
            outRect.bottom = bottomMagrinPX;
        }
    }

}
