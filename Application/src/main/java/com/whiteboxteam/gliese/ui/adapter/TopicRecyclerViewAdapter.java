package com.whiteboxteam.gliese.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.whiteboxteam.gliese.R;
import com.whiteboxteam.gliese.data.content.ApplicationContentContract;
import com.whiteboxteam.gliese.data.storage.StorageContract;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 13.02.2015
 * Time: 15:16
 */
public abstract class TopicRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TOPIC_GROUP_VIEW_TYPE = 0;
    private static final int TOPIC_VIEW_TYPE = 1;

    private Cursor topicGroupCursor = null;
    private SparseArray<Cursor> topicCursors = new SparseArray<>();

    private int topicGroupIdColumnIndex = -1;
    private int topicIdColumnIndex = -1;

    private LayoutInflater inflater;
    private SharedPreferences preferences;

    private TopicViewHolder selected = null;
    private int activeTextColor;
    private int inactiveTextColor;

    public TopicRecyclerViewAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        activeTextColor = context.getResources().getColor(R.color.primary);
        inactiveTextColor = context.getResources().getColor(R.color.drawer_title_text_color);
    }

    private void onTopicItemClick(TopicViewHolder viewHolder) {
        if (selected != viewHolder) {
            setSelectedTopic(viewHolder);
        }
    }

    private void setSelectedTopic(TopicViewHolder viewHolder) {
        if (selected != null) {
            selected.setActivated(false);
        }

        selected = viewHolder;

        selected.setActivated(true);
        preferences.edit().putLong(StorageContract.LAST_TOPIC_ID, selected.id).apply();
        onTopicSelected(selected.id, selected.name.getText().toString());
    }

    protected abstract void onTopicSelected(long id, String name);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case TOPIC_GROUP_VIEW_TYPE:
                return new TopicGroupViewHolder(inflater.inflate(R.layout.drawer_item_subheader, viewGroup, false));
            case TOPIC_VIEW_TYPE:
                return new TopicViewHolder(inflater.inflate(R.layout.drawer_item, viewGroup, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Pair<Integer, Integer> coordinate = getCoordinate(position);
        if (coordinate.second < 0) {
            topicGroupCursor.moveToPosition(coordinate.first);
            bindTopicGroupViewHolder((TopicGroupViewHolder) viewHolder, topicGroupCursor, position == 0);
        } else {
            Cursor topicCursor = topicCursors.get(coordinate.first);
            topicCursor.moveToPosition(coordinate.second);
            bindTopicViewHolder((TopicViewHolder) viewHolder, topicCursor);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Pair<Integer, Integer> coordinate = getCoordinate(position);
        if (coordinate.second < 0) {
            return TOPIC_GROUP_VIEW_TYPE;
        } else {
            return TOPIC_VIEW_TYPE;
        }
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        Pair<Integer, Integer> coordinate = getCoordinate(position);
        if (coordinate.second < 0) {
            topicGroupCursor.moveToPosition(coordinate.first);
            return topicGroupCursor.getLong(topicGroupIdColumnIndex);
        } else {
            Cursor topicCursor = topicCursors.get(coordinate.first);
            topicCursor.moveToPosition(coordinate.second);
            return 1000 * coordinate.first + topicCursor.getLong(topicIdColumnIndex);
        }
    }

    @Override
    public int getItemCount() {
        if (topicGroupCursor == null) return 0;
        int count = topicGroupCursor.getCount();
        for (int i = 0; i < topicCursors.size(); ++i) {
            int key = topicCursors.keyAt(i);
            Cursor cursor = topicCursors.get(key);
            count += cursor.getCount();
        }
        return count;
    }

    private Pair<Integer, Integer> getCoordinate(int position) {
        if (topicGroupCursor != null) {
            int seed = 0;
            for (int i = 0; i < topicGroupCursor.getCount(); ++i) {

                if (seed == position) return new Pair<>(i, -1);

                Cursor cursor = topicCursors.get(i);
                int topicCount = cursor == null ? 0 : cursor.getCount();
                seed += topicCount + 1;
                if (position < seed) return new Pair<>(i, topicCount - (seed - position));
            }
        }
        return new Pair<>(-1, -1);
    }

    private void bindTopicGroupViewHolder(TopicGroupViewHolder viewHolder, Cursor topicGroupCursor, boolean isFirst) {
        viewHolder.name.setText(topicGroupCursor.getString(topicGroupCursor.getColumnIndex(ApplicationContentContract
                .TopicGroup.NAME)));
        int visibility = isFirst ? View.GONE : View.VISIBLE;
        viewHolder.divider.setVisibility(visibility);
    }

    private void bindTopicViewHolder(TopicViewHolder viewHolder, Cursor topicCursor) {
        viewHolder.id = topicCursor.getLong(topicIdColumnIndex);
        viewHolder.name.setText(topicCursor.getString(topicCursor.getColumnIndex(ApplicationContentContract.Topic
                .NAME)));
        if (viewHolder.id == preferences.getLong(StorageContract.LAST_TOPIC_ID, -1)) {
            setSelectedTopic(viewHolder);
        }
    }

    public void changeTopicGroupCursor(Cursor cursor) {
        Cursor old = swapTopicGroupCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    public Cursor swapTopicGroupCursor(Cursor cursor) {
        if (topicGroupCursor == cursor) return null;
        if (topicGroupCursor != null) {
            releaseTopicCursors();
        }
        final Cursor old = topicGroupCursor;
        topicGroupCursor = cursor;
        if (topicGroupCursor != null) {
            topicGroupIdColumnIndex = topicGroupCursor.getColumnIndex("_id");
            initTopicCursorLoaders();
        }
        notifyDataSetChanged();
        return old;
    }

    private void releaseTopicCursors() {
        for (int i = 0; i < topicCursors.size(); ++i) {
            int key = topicCursors.keyAt(i);
            Cursor cursor = topicCursors.get(key);
            cursor.close();
        }
        topicCursors.clear();
    }

    private void initTopicCursorLoaders() {
        topicGroupCursor.moveToPosition(-1);
        while (topicGroupCursor.moveToNext()) {
            initTopicCursorLoader(topicGroupCursor);
        }
    }

    protected abstract void initTopicCursorLoader(Cursor topicGroupCursor);

    public void changeTopicCursor(int position, Cursor cursor) {
        Cursor old = swapTopicCursor(position, cursor);
        if (old != null) {
            old.close();
        }
    }

    private Cursor swapTopicCursor(int position, Cursor cursor) {
        Cursor topicCursor = topicCursors.get(position);
        if (topicCursor == cursor) return null;
        if (topicCursor != null) {
            int count = topicCursor.getCount();
            topicCursors.remove(position);
            topicCursor.close();
            notifyItemRangeRemoved(position + 1, count);
        }
        if (cursor != null) {
            topicCursors.append(position, cursor);
            topicIdColumnIndex = cursor.getColumnIndex("_id");
            notifyItemRangeInserted(position + 1, cursor.getCount());
        }
        return topicCursor;
    }

    private class TopicGroupViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public View divider;

        public TopicGroupViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.drawer_subheader_title);
            divider = itemView.findViewById(R.id.drawer_divider);
        }
    }

    private class TopicViewHolder extends RecyclerView.ViewHolder {

        private final View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTopicItemClick(TopicViewHolder.this);
            }
        };
        public long id;
        public TextView name;
        public View icon;

        public TopicViewHolder(final View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.drawer_title);
            icon = itemView.findViewById(R.id.drawer_icon);
            itemView.setOnClickListener(clickListener);
        }

        public void setActivated(boolean activated) {
            itemView.setActivated(activated);
            name.setTextColor(activated ? activeTextColor : inactiveTextColor);
            icon.setActivated(activated);
        }
    }

}
