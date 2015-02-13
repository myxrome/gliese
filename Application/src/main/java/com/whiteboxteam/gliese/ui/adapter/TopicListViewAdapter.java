package com.whiteboxteam.gliese.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 13.02.2015
 * Time: 15:16
 */
public class TopicListViewAdapter extends CursorTreeAdapter {

    public TopicListViewAdapter(Cursor cursor, Context context) {
        super(cursor, context);
    }

    public TopicListViewAdapter(Cursor cursor, Context context, boolean autoRequery) {
        super(cursor, context, autoRequery);
    }

    @Override
    protected Cursor getChildrenCursor(Cursor groupCursor) {
        return null;
    }

    @Override
    protected View newGroupView(Context context, Cursor cursor, boolean isExpanded, ViewGroup parent) {
        return null;
    }

    @Override
    protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {

    }

    @Override
    protected View newChildView(Context context, Cursor cursor, boolean isLastChild, ViewGroup parent) {
        return null;
    }

    @Override
    protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild) {

    }
}
