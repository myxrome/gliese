package com.whiteboxteam.gliese.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.whiteboxteam.gliese.R;
import com.whiteboxteam.gliese.data.content.ApplicationContentContract;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 10.03.2015
 * Time: 16:36
 */
public class ValueRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Cursor valueCursor;
    private int valueIdColumnIndex;
    private int valueNameColumnIndex;
    private LayoutInflater inflater;

    public ValueRecyclerViewAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ValueViewHolder(inflater.inflate(R.layout.item_value, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ValueViewHolder valueViewHolder = (ValueViewHolder) holder;
        valueCursor.moveToPosition(position);
        valueViewHolder.name.setText(valueCursor.getString(valueNameColumnIndex));
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        if (valueCursor == null) return RecyclerView.NO_ID;
        valueCursor.moveToPosition(position);
        return valueCursor.getLong(valueIdColumnIndex);
    }

    @Override
    public int getItemCount() {
        return valueCursor == null ? 0 : valueCursor.getCount();
    }

    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == valueCursor) {
            return null;
        }
        final Cursor oldCursor = valueCursor;
        valueCursor = newCursor;
        if (valueCursor != null) {
            valueIdColumnIndex = valueCursor.getColumnIndexOrThrow(ApplicationContentContract.Value.ID);
            valueNameColumnIndex = valueCursor.getColumnIndex(ApplicationContentContract.Value.NAME);
            notifyDataSetChanged();
        } else {
            valueIdColumnIndex = -1;
            valueNameColumnIndex = -1;
            notifyDataSetChanged();
        }
        return oldCursor;
    }

    private class ValueViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        public ValueViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.text_value_name);
        }
    }
}
