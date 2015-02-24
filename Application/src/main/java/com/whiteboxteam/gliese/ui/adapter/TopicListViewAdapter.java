package com.whiteboxteam.gliese.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.whiteboxteam.gliese.data.content.ApplicationContentContract;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 13.02.2015
 * Time: 15:16
 */
public abstract class TopicListViewAdapter extends BaseExpandableListAdapter {

    private Cursor groupCursor;
    private SparseArray<Cursor> childrenCursors = new SparseArray<>();
    private LayoutInflater inflater;
    private int groupIdColumnIndex;
    private int childIdColumnIndex;

    public TopicListViewAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return groupCursor == null ? 0 : groupCursor.getCount();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Cursor children = childrenCursors.get(groupPosition);
        return children == null ? 0 : children.getCount();
    }

    public Cursor getGroup(int groupPosition) {
        if (groupCursor != null) {
            groupCursor.moveToPosition(groupPosition);
            return groupCursor;
        }
        return null;
    }

    public Cursor getChild(int groupPosition, int childPosition) {
        if (groupCursor != null) {
            Cursor children = childrenCursors.get(groupPosition);
            if (children != null) {
                children.moveToPosition(childPosition);
                return children;
            }
        }

        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return getGroup(groupPosition).getLong(groupIdColumnIndex);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return getChild(groupPosition, childPosition).getLong(childIdColumnIndex);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Cursor cursor = getGroup(groupPosition);
        View v;
        if (convertView == null) {
            v = newGroupView(parent);
        } else {
            v = convertView;
        }
        bindGroupView(v, cursor);
        return v;
    }

    private View newGroupView(ViewGroup parent) {
        return inflater.inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
    }

    private void bindGroupView(View v, Cursor cursor) {
        TextView textView = (TextView) v.findViewById(android.R.id.text1);
        textView.setText(cursor.getString(cursor.getColumnIndex(ApplicationContentContract.TopicGroup.NAME)));
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup
            parent) {
        Cursor cursor = getChild(groupPosition, childPosition);
        View v;
        if (convertView == null) {
            v = newChildView(parent);
        } else {
            v = convertView;
        }
        bindChildView(v, cursor);
        return v;
    }

    private View newChildView(ViewGroup parent) {
        return inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
    }

    private void bindChildView(View v, Cursor cursor) {
        TextView textView = (TextView) v.findViewById(android.R.id.text1);
        textView.setText(cursor.getString(cursor.getColumnIndex(ApplicationContentContract.Topic.NAME)));
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setGroupCursor(Cursor cursor) {
        if (groupCursor == cursor) return;
        if (groupCursor != null) {
            releaseChildrenCursors();
            groupCursor.close();
        }
        groupCursor = cursor;

        if (groupCursor != null) {
            groupIdColumnIndex = groupCursor.getColumnIndex("_id");
            initChildrenCursorLoaders();
        }
        notifyDataSetChanged();
    }

    private void releaseChildrenCursors() {
        for (int i = 0; i < childrenCursors.size(); i++) {
            int key = childrenCursors.keyAt(i);
            Cursor child = childrenCursors.get(key);
            child.close();
        }
        childrenCursors.clear();
    }

    private void initChildrenCursorLoaders() {
        groupCursor.moveToPosition(-1);
        while (groupCursor.moveToNext()) {
            initChildrenCursorLoader(groupCursor);
        }
    }

    protected abstract void initChildrenCursorLoader(Cursor groupCursor);

    public void setChildrenCursor(int groupPosition, Cursor cursor) {
        Cursor childrenCursor = childrenCursors.get(groupPosition);
        if (childrenCursor == cursor) return;

        if (childrenCursor != null) {
            childrenCursors.remove(groupPosition);
            childrenCursor.close();
        }

        if (cursor != null) {
            childrenCursors.append(groupPosition, cursor);
            childIdColumnIndex = cursor.getColumnIndex("_id");
        }

        notifyDataSetChanged();
    }

}
