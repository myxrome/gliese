package com.whiteboxteam.gliese.ui.adapter;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.common.base.Strings;
import com.whiteboxteam.gliese.R;
import com.whiteboxteam.gliese.data.content.ApplicationContentContract;
import com.whiteboxteam.gliese.data.entity.ValueEntity;
import com.whiteboxteam.gliese.data.sync.image.ImageUploadService;
import com.whiteboxteam.gliese.ui.custom.RoubleTypefaceSpan;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 10.03.2015
 * Time: 16:36
 */
public class ValueRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final DecimalFormat formatter;
    private final Typeface roubleSupportedTypeface;
    private LayoutInflater inflater;
    private List<ValueEntity> valueEntities = new ArrayList<>();
    private SparseArray<ValueThumbObserver> thumbObservers = new SparseArray<>();

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            int position = msg.what;
            if (valueEntities.size() > position) {
                ValueEntity entity = valueEntities.get(position);
                Uri uri = ContentUris.withAppendedId(ApplicationContentContract.Value.CONTENT_URI, entity.id);
                try {
                    Cursor result = context.getContentResolver().query(uri, new String[]{ApplicationContentContract
                            .Value.LOCAL_THUMB_URI}, null, null, null);
                    if (result.moveToFirst()) {
                        String localThumb = result.getString(result.getColumnIndex(ApplicationContentContract.Value
                                .LOCAL_THUMB_URI));
                        if (!Strings.isNullOrEmpty(localThumb)) {
                            valueEntities.get(position).localThumbUri = localThumb;
                            valueEntities.get(position).thumb = BitmapFactory.decodeFile(localThumb);

                            notifyItemChanged(position);
                        }
                    }
                    result.close();
                } catch (Exception e) {
                }
            }


        }
    };

    public ValueRecyclerViewAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);
        roubleSupportedTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/rouble.ttf");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ValueViewHolder(inflater.inflate(R.layout.item_value, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ValueViewHolder valueViewHolder = (ValueViewHolder) holder;
        valueViewHolder.setValueEntity(valueEntities.get(position));
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        if (valueEntities.isEmpty()) return RecyclerView.NO_ID;
        return valueEntities.get(position).id;
    }

    @Override
    public int getItemCount() {
        return valueEntities.size();
    }

    public void loadCursor(Cursor cursor) {
        valueEntities.clear();
        if (cursor != null) {
            int valueIdColumnIndex = cursor.getColumnIndexOrThrow(ApplicationContentContract.Value.ID);
            int valueNameColumnIndex = cursor.getColumnIndex(ApplicationContentContract.Value.NAME);
            int valueOldPriceColumnIndex = cursor.getColumnIndex(ApplicationContentContract.Value.OLD_PRICE);
            int valueNewPriceColumnIndex = cursor.getColumnIndex(ApplicationContentContract.Value.NEW_PRICE);
            int valueThumbColumnIndex = cursor.getColumnIndex(ApplicationContentContract.Value.LOCAL_THUMB_URI);
            int valueDiscountColumnIndex = cursor.getColumnIndex(ApplicationContentContract.Value.DISCOUNT);
            int valueURLColumnIndex = cursor.getColumnIndex(ApplicationContentContract.Value.URL);

            int position = 0;
            while (cursor.moveToNext()) {
                ValueEntity entity = new ValueEntity();
                entity.id = cursor.getLong(valueIdColumnIndex);
                entity.name = cursor.getString(valueNameColumnIndex);
                entity.oldPrice = cursor.getInt(valueOldPriceColumnIndex);
                entity.discount = cursor.getInt(valueDiscountColumnIndex);
                entity.newPrice = cursor.getInt(valueNewPriceColumnIndex);
                entity.localThumbUri = cursor.getString(valueThumbColumnIndex);
                entity.url = cursor.getString(valueURLColumnIndex);
                entity.thumb = BitmapFactory.decodeFile(entity.localThumbUri);

                if (entity.thumb == null) {
                    if (thumbObservers.indexOfKey(position) < 0) {
                        ValueThumbObserver observer = new ValueThumbObserver(position);
                        thumbObservers.append(position, observer);
                        Uri valueUri = ContentUris.withAppendedId(ApplicationContentContract.Value.CONTENT_URI,
                                entity.id);
                        context.getContentResolver().registerContentObserver(valueUri, false, observer);
                    }
                    ImageUploadService.startForegroundValueThumbUpload(context, entity.id, 1);
                }

                valueEntities.add(entity);
                position += 1;
            }

        }
        notifyDataSetChanged();
    }

    public void unregisterObservers() {
        ContentResolver contentResolver = context.getContentResolver();
        for (int i = 0; i < thumbObservers.size(); i++) {
            int key = thumbObservers.keyAt(i);
            ValueThumbObserver observer = thumbObservers.get(key);
            contentResolver.unregisterContentObserver(observer);
        }
    }

    private class ValueViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView oldPrice;
        private TextView newPrice;
        private ImageView thumb;
        private ProgressBar progressBar;
        private TextView discount;
        private Button buy;

        private ValueEntity valueEntity;

        public ValueViewHolder(View itemView) {
            super(itemView);
            oldPrice = (TextView) itemView.findViewById(R.id.text_old_price);
            oldPrice.setPaintFlags(oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            newPrice = (TextView) itemView.findViewById(R.id.text_new_price);
            thumb = (ImageView) itemView.findViewById(R.id.image_thumb);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
            discount = (TextView) itemView.findViewById(R.id.text_discount);
            buy = (Button) itemView.findViewById(R.id.button_buy);
            buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((valueEntity != null) && !Strings.isNullOrEmpty(valueEntity.url))
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(valueEntity.url)));
                }
            });
        }

        public void setValueEntity(ValueEntity entity) {
            valueEntity = entity;
            applyValueEntry();
        }

        private void applyValueEntry() {
            oldPrice.setText(buildRoubleString(formatter.format(valueEntity.oldPrice)));
            newPrice.setText(buildRoubleString(formatter.format(valueEntity.newPrice)));
            discount.setText("-" + valueEntity.discount + "%");

            thumb.setImageBitmap(valueEntity.thumb);
            progressBar.setVisibility(valueEntity.thumb == null ? View.VISIBLE : View.INVISIBLE);

        }

        private SpannableStringBuilder buildRoubleString(String value) {
            SpannableStringBuilder result = new SpannableStringBuilder(value + " " + '\u20BD');
            RoubleTypefaceSpan typefaceSpan = new RoubleTypefaceSpan(roubleSupportedTypeface);
            int position = value.length() + 1;
            result.setSpan(typefaceSpan, position, position + 1, 0);
            return result;
        }

    }

    private class ValueThumbObserver extends ContentObserver {

        private int position;

        public ValueThumbObserver(int position) {
            super(null);
            this.position = position;
        }

        @Override
        public void onChange(boolean selfChange) {
            handler.sendEmptyMessage(position);
        }
    }

}
