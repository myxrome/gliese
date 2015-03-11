package com.whiteboxteam.gliese.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.whiteboxteam.gliese.R;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 10.03.2015
 * Time: 16:36
 */
public class ValueRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
        valueViewHolder.name.setText(String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    private class ValueViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        public ValueViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.text_value_name);
        }
    }
}
