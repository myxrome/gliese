package com.whiteboxteam.gliese.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
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
import com.whiteboxteam.gliese.data.content.ApplicationContentContract;
import com.whiteboxteam.gliese.ui.custom.RoubleTypefaceSpan;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 10.03.2015
 * Time: 16:36
 */
public class ValueRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final DecimalFormat formatter;
    private final Typeface roubleSupportedTypeface;
    private final Context context;
    private Cursor valueCursor;
    private int valueIdColumnIndex;
    private int valueNameColumnIndex;
    private int valueOldPriceColumnIndex;
    private int valueNewPriceColumnIndex;
    private int valueThumbColumnIndex;
    private int valueDiscountColumnIndex;
    private int valueURLColumnIndex;
    private LayoutInflater inflater;

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
        valueCursor.moveToPosition(position);
//        valueViewHolder.name.setText(valueCursor.getString(valueNameColumnIndex));

        valueViewHolder.oldPrice.setText(buildRoubleString(formatter.format(valueCursor.getInt
                (valueOldPriceColumnIndex))));
        valueViewHolder.newPrice.setText(buildRoubleString(formatter.format(valueCursor.getInt
                (valueNewPriceColumnIndex))));

        Bitmap image = BitmapFactory.decodeFile(valueCursor.getString(valueThumbColumnIndex));
        valueViewHolder.thumb.setImageBitmap(image);

        valueViewHolder.discount.setText("-" + valueCursor.getString(valueDiscountColumnIndex) + "%");
        valueViewHolder.url = valueCursor.getString(valueURLColumnIndex);

    }

    private SpannableStringBuilder buildRoubleString(String value) {
        SpannableStringBuilder result = new SpannableStringBuilder(value + " " + '\u20BD');
        RoubleTypefaceSpan typefaceSpan = new RoubleTypefaceSpan(roubleSupportedTypeface);
        int position = value.length() + 1;
        result.setSpan(typefaceSpan, position, position + 1, 0);
        return result;
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
            valueOldPriceColumnIndex = valueCursor.getColumnIndex(ApplicationContentContract.Value.OLD_PRICE);
            valueNewPriceColumnIndex = valueCursor.getColumnIndex(ApplicationContentContract.Value.NEW_PRICE);
            valueThumbColumnIndex = valueCursor.getColumnIndex(ApplicationContentContract.Value.LOCAL_THUMB_URI);
            valueDiscountColumnIndex = valueCursor.getColumnIndex(ApplicationContentContract.Value.DISCOUNT);
            valueURLColumnIndex = valueCursor.getColumnIndex(ApplicationContentContract.Value.URL);
            notifyDataSetChanged();
        } else {
            valueIdColumnIndex = -1;
            valueNameColumnIndex = -1;
            valueOldPriceColumnIndex = -1;
            valueNewPriceColumnIndex = -1;
            valueThumbColumnIndex = -1;
            valueDiscountColumnIndex = -1;
            valueURLColumnIndex = -1;
            notifyDataSetChanged();
        }
        return oldCursor;
    }

    private class ValueViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView oldPrice;
        TextView newPrice;
        ImageView thumb;
        TextView discount;
        Button buy;
        String url;

        public ValueViewHolder(View itemView) {
            super(itemView);
            oldPrice = (TextView) itemView.findViewById(R.id.text_old_price);
            oldPrice.setPaintFlags(oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            newPrice = (TextView) itemView.findViewById(R.id.text_new_price);
            thumb = (ImageView) itemView.findViewById(R.id.image_thumb);
            discount = (TextView) itemView.findViewById(R.id.text_discount);
            buy = (Button) itemView.findViewById(R.id.button_buy);
            buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Strings.isNullOrEmpty(url))
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                }
            });
        }
    }
}
