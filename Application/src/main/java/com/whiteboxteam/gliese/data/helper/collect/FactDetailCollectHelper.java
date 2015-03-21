package com.whiteboxteam.gliese.data.helper.collect;

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
 * Time: 20:50
 */
public class FactDetailCollectHelper extends JSONArrayCollectHelper {

    public FactDetailCollectHelper(Context context) {
        super(context);
    }

    @Override
    protected Cursor getSourceData(Uri parentUri) {
        return context.getContentResolver().query(StatisticContentContract.FactDetail.getContentUriByFact(parentUri),
                new String[]{StatisticContentContract.FactDetail.ID, StatisticContentContract.FactDetail.ORDER,
                        StatisticContentContract.FactDetail.HAPPENED_AT}, null, null, null);
    }

    @Override
    protected JSONObject getExtractedItem(Cursor source) throws JSONException {
        JSONObject item = new JSONObject();
        item.put(StatisticServerContract.FactDetailData.ORDER, source.getInt(source.getColumnIndex
                (StatisticContentContract.FactDetail.ORDER))).
                put(StatisticServerContract.FactDetailData.HAPPENED_AT, source.getString(source.getColumnIndex
                        (StatisticContentContract.FactDetail.HAPPENED_AT)));
        return item;
    }

}
