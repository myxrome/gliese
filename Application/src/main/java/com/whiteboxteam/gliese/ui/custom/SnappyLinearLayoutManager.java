package com.whiteboxteam.gliese.ui.custom;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 10.03.2015
 * Time: 13:44
 */
public class SnappyLinearLayoutManager extends LinearLayoutManager {

    private static final int MIN_FLING_VELOCITY = 350; // dips
    private int childHeightPX;
    private int currentPosition;
    private int minimumVelocity;

    public SnappyLinearLayoutManager(Context context) {
        super(context);
        initLayoutManager(context);
    }

    private void initLayoutManager(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        childHeightPX = Math.round(400 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        minimumVelocity = (int) (MIN_FLING_VELOCITY * displayMetrics.density);
        currentPosition = 0;
    }

    public SnappyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        initLayoutManager(context);
    }

    public int calcCurrentPosition(int velocity, int itemCount) {
        int newPosition = calcCenterPosition(findFirstVisibleItemPosition(), findLastVisibleItemPosition());
        currentPosition = correctNewPosition(velocity, newPosition, itemCount);
        return currentPosition;
    }

    private int calcCenterPosition(int first, int last) {
        if (first == last) {
            return first;
        }

        int distance = last - first;
        if (distance % 2 == 0) {
            return first + (distance / 2);
        } else {
            distance = (int) Math.floor(distance / 2);
            return calcCenterViewPosition(first + distance, last - distance);
        }

    }

    private int correctNewPosition(int velocity, int newPosition, int itemCount) {
        if (newPosition == currentPosition && Math.abs(velocity) > minimumVelocity) {
            if (velocity > 0) {
                return Math.min(newPosition + 1, itemCount - 1);
            } else return Math.max(newPosition - 1, 0);
        }
        return newPosition;
    }

    private int calcCenterViewPosition(int first, int last) {
        View firstView = findViewByPosition(first);
        View lastView = findViewByPosition(last);
        return isFirstViewNearestToCenter(firstView, lastView) ? first : last;
    }

    private boolean isFirstViewNearestToCenter(View firstView, View lastView) {
        return firstView.getTop() > (getHeight() - (lastView.getTop() + lastView.getHeight()));
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        final LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {

            @Override
            public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
                return (((boxEnd - boxStart) / 2) - ((viewEnd - viewStart) / 2)) - viewStart;
            }

            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return SnappyLinearLayoutManager.this.computeScrollVectorForPosition(targetPosition);
            }
        };
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }

}