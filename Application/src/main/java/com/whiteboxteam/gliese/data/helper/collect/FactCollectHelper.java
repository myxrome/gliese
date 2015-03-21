package com.whiteboxteam.gliese.data.helper.collect;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.whiteboxteam.gliese.data.content.StatisticContentContract;
import com.whiteboxteam.gliese.data.server.StatisticServerContract;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 03.09.2014
 * Time: 21:09
 */
public class FactCollectHelper extends JSONArrayCollectHelper {

    private FactDetailCollectHelper factDetailCollectHelper;

    public FactCollectHelper(Context context) {
        super(context);
        factDetailCollectHelper = new FactDetailCollectHelper(context);
    }

    @Override
    protected Cursor getSourceData(Uri parentUri) {
        return context.getContentResolver().query(StatisticContentContract.Fact.getContentUriBySession(parentUri),
                new String[]{StatisticContentContract.Fact.ID, StatisticContentContract.Fact.EVENT,
                        StatisticContentContract.Fact.CONTEXT_ID, StatisticContentContract.Fact.CONTEXT_TYPE,
                        StatisticContentContract.Fact.EXTERNAL_CONTEXT}, null, null, null);
    }

    @Override
    protected JSONObject getExtractedItem(Cursor source) throws JSONException {
        JSONObject item = new JSONObject();
        item.put(StatisticServerContract.FactData.EVENT, source.getString(source.getColumnIndex
                (StatisticContentContract.Fact.EVENT))).
                put(StatisticServerContract.FactData.CONTEXT_ID, source.getInt(source.getColumnIndex
                        (StatisticContentContract.Fact.CONTEXT_ID))).
                put(StatisticServerContract.FactData.CONTEXT_TYPE, source.getString(source.getColumnIndex
                        (StatisticContentContract.Fact.CONTEXT_TYPE))).
                put(StatisticServerContract.FactData.EXTERNAL_CONTEXT, source.getString(source.getColumnIndex
                        (StatisticContentContract.Fact.EXTERNAL_CONTEXT))).
                put(StatisticServerContract.FactData.FACT_DETAIL_LIST, factDetailCollectHelper.extract(ContentUris
                        .withAppendedId(StatisticContentContract.Fact.CONTENT_URI, source.getLong(source
                                .getColumnIndex(StatisticContentContract.Fact.ID)))));
        return item;
    }

}
