package com.whiteboxteam.gliese.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.common.base.Strings;
import com.whiteboxteam.gliese.R;
import com.whiteboxteam.gliese.common.StringHelper;
import com.whiteboxteam.gliese.data.content.ApplicationContentContract;
import com.whiteboxteam.gliese.data.entity.ValueEntity;
import com.whiteboxteam.gliese.data.helper.statistic.FactHelper;
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

    private final Object lock = new Object();
    private final Context        context;
    private final DecimalFormat  formatter;
    private final Typeface       roubleSupportedTypeface;
    private       LayoutInflater inflater;
    private       Cursor         valueCursor;
    private List<ValueEntity> valueEntities = new ArrayList<>();

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
        if (valueEntities.isEmpty()) {
            return RecyclerView.NO_ID;
        }
        return valueEntities.get(position).id;
    }

    @Override
    public int getItemCount() {
        return valueEntities.size();
    }

    public void changeCursor(Cursor cursor) {
        Cursor oldCursor = swapCursor(cursor);
        if (oldCursor != null) {
            oldCursor.close();
        }

    }

    public Cursor swapCursor(Cursor cursor) {
        if (cursor == valueCursor) {
            return null;
        }

        Cursor oldCursor = valueCursor;
        valueCursor = cursor;

        synchronized (lock) {
            if (valueCursor != null) {
                int valueIdColumnIndex = valueCursor.getColumnIndexOrThrow(ApplicationContentContract.Value.ID);
                int valueNameColumnIndex = valueCursor.getColumnIndex(ApplicationContentContract.Value.NAME);
                int valueOldPriceColumnIndex = valueCursor.getColumnIndex(ApplicationContentContract.Value.OLD_PRICE);
                int valueNewPriceColumnIndex = valueCursor.getColumnIndex(ApplicationContentContract.Value.NEW_PRICE);
                int valueThumbColumnIndex = valueCursor
                        .getColumnIndex(ApplicationContentContract.Value.LOCAL_THUMB_URI);
                int valueDiscountColumnIndex = valueCursor.getColumnIndex(ApplicationContentContract.Value.DISCOUNT);
                int valueURLColumnIndex = valueCursor.getColumnIndex(ApplicationContentContract.Value.URL);

                valueCursor.moveToPosition(-1);
                int position = 0;
                while (valueCursor.moveToNext()) {
                    ValueEntity entity = new ValueEntity();
                    entity.id = valueCursor.getLong(valueIdColumnIndex);
                    entity.name = valueCursor.getString(valueNameColumnIndex);
                    entity.oldPrice = valueCursor.getInt(valueOldPriceColumnIndex);
                    entity.discount = valueCursor.getInt(valueDiscountColumnIndex);
                    entity.newPrice = valueCursor.getInt(valueNewPriceColumnIndex);
                    entity.url = valueCursor.getString(valueURLColumnIndex);
                    entity.localThumbUri = valueCursor.getString(valueThumbColumnIndex);
                    if (position < valueEntities.size()) {
                        ValueEntity current = valueEntities.get(position);
                        if (Strings.nullToEmpty(current.localThumbUri)
                                   .equals(entity.localThumbUri) && current.thumb != null) {
                            entity.thumb = current.thumb;
                        }
                        valueEntities.set(position, entity);
                    } else {
                        valueEntities.add(entity);
                    }

                    position++;
                }
                if (position < valueEntities.size()) {
                    valueEntities.subList(position, valueEntities.size()).clear();
                }
            }
        }
        notifyDataSetChanged();

        return oldCursor;
    }

    private class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private ValueEntity entity;

        private ImageLoadTask(ValueEntity entity) {
            this.entity = entity;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            return BitmapFactory.decodeFile(entity.localThumbUri);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                synchronized (lock) {
                    int position = valueEntities.indexOf(entity);
                    if (position > -1) {
                        ValueEntity current = valueEntities.get(position);
                        if (current.thumb == null) {
                            current.thumb = bitmap;
                            notifyItemChanged(position);
                        }
                    }
                }
            }
        }

    }

    private class ValueViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView oldPrice;
        private TextView newPrice;
        private ImageView thumb;
        private TextView discount;
        private Button buy;

        private ValueEntity valueEntity;

        public ValueViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.text_name);
            oldPrice = (TextView) itemView.findViewById(R.id.text_old_price);
            oldPrice.setPaintFlags(oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            newPrice = (TextView) itemView.findViewById(R.id.text_new_price);
            thumb = (ImageView) itemView.findViewById(R.id.image_thumb);
            discount = (TextView) itemView.findViewById(R.id.text_discount);
            buy = (Button) itemView.findViewById(R.id.button_buy);
            buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((valueEntity != null) && !Strings.isNullOrEmpty(valueEntity.url)) {
                        String subId = StringHelper.randomString(16);
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(valueEntity.url + "&sub_id=" +
                                                                                               subId)));
                        FactHelper factHelper = FactHelper.getInstance(context);
                        factHelper.increaseCounter(valueEntity.id, FactHelper.ContextType.VALUE_CONTEXT,
                                                   FactHelper.EventTag.VALUE_CLICK_COUNTER_EVENT, subId);
                        factHelper.increaseCounter(FactHelper.VirtualContext.BUY_BUTTON_ID,
                                                   FactHelper.ContextType.VIRTUAL_CONTEXT,
                                                   FactHelper.EventTag.BUY_BUTTON_CLICK_COUNTER_EVENT);
                    }
                }
            });
        }

        public void setValueEntity(ValueEntity entity) {
            valueEntity = entity;
            applyValueEntry();
        }

        private void applyValueEntry() {
            if (Strings.isNullOrEmpty(valueEntity.localThumbUri)) {
                ImageUploadService.startForegroundValueThumbUpload(context, valueEntity.id, 1);
            } else if (valueEntity.thumb == null) {
                ImageLoadTask task = new ImageLoadTask(valueEntity);
                task.execute();
            }
            name.setText(valueEntity.name);
            oldPrice.setText(buildRoubleString(formatter.format(valueEntity.oldPrice)));
            newPrice.setText(buildRoubleString(formatter.format(valueEntity.newPrice)));
            discount.setText("-" + valueEntity.discount + "%");
            thumb.setImageBitmap(valueEntity.thumb);
        }

        private SpannableStringBuilder buildRoubleString(String value) {
            SpannableStringBuilder result = new SpannableStringBuilder(value + " " + '\u20BD');
            RoubleTypefaceSpan typefaceSpan = new RoubleTypefaceSpan(roubleSupportedTypeface);
            int position = value.length() + 1;
            result.setSpan(typefaceSpan, position, position + 1, 0);
            return result;
        }

    }

}
