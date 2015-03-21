package com.whiteboxteam.gliese.data.helper.collect;

import android.content.ContentProviderOperation;
import android.content.ContentUris;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import com.whiteboxteam.gliese.data.content.StatisticContentContract;
import com.whiteboxteam.gliese.data.helper.statistic.SessionHelper;
import com.whiteboxteam.gliese.data.server.StatisticServerContract;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 03.09.2014
 * Time: 21:19
 */
public class SessionCollectHelper extends JSONArrayCollectHelper {

    private FactCollectHelper factExtractor;
    private ArrayList<ContentProviderOperation> batch = new ArrayList<>();

    public SessionCollectHelper(Context context) {
        super(context);
        factExtractor = new FactCollectHelper(context);
    }

    @Override
    protected Cursor getSourceData(Uri parentUri) {
        Uri current = SessionHelper.getInstance(context).getCurrent();
        return context.getContentResolver().query(StatisticContentContract.Session.CONTENT_URI, new
                String[]{StatisticContentContract.Session.ID, StatisticContentContract.Session.STARTED_AT,
                StatisticContentContract.Session.FINISHED_AT}, StatisticContentContract.Session.ID + " != ?", new
                String[]{current == null ? "-1" : current.getLastPathSegment()}, null);
    }

    @Override
    protected JSONObject getExtractedItem(Cursor source) throws JSONException {
        batch.add(ContentProviderOperation.newDelete(ContentUris.withAppendedId(StatisticContentContract.Session
                .CONTENT_URI, source.getLong(source.getColumnIndex(StatisticContentContract.Session.ID)))).build());
        JSONObject item = new JSONObject();
        item.put(StatisticServerContract.SessionData.STARTED_AT, source.getString(source.getColumnIndex
                (StatisticContentContract.Session.STARTED_AT))).
                put(StatisticServerContract.SessionData.FINISHED_AT, source.getString(source.getColumnIndex
                        (StatisticContentContract.Session.FINISHED_AT))).
                put(StatisticServerContract.SessionData.FACT_LIST, factExtractor.extract(ContentUris.withAppendedId
                        (StatisticContentContract.Session.CONTENT_URI, source.getLong(source.getColumnIndex
                                (StatisticContentContract.Session.ID)))));

        return item;
    }

    public void complete() throws RemoteException, OperationApplicationException {
        context.getContentResolver().applyBatch(StatisticContentContract.Session.CONTENT_URI.getAuthority(), batch);
    }

}
