package com.whiteboxteam.gliese.ui.custom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 10.03.2015
 * Time: 13:42
 */
public final class SnappyRecyclerView extends RecyclerView {

    public SnappyRecyclerView(Context context) {
        super(context);
    }

    public SnappyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SnappyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        if (getAdapter().getItemCount() > 0) {
            int position = ((SnappyLinearLayoutManager) getLayoutManager()).calcCurrentPosition(velocityY);
            super.smoothScrollToPosition(position);
        }
        return true;
    }

}