package com.whiteboxteam.gliese.data.helper.collect;

import android.content.ContentProviderOperation;
import android.content.ContentUris;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import com.whiteboxteam.gliese.data.content.StatisticContentContract;
import com.whiteboxteam.gliese.data.server.StatisticServerContract;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 21.03.2015
 * Time: 15:04
 */
public class CrashReportCollectHelper extends JSONArrayCollectHelper {

    private ArrayList<ContentProviderOperation> batch = new ArrayList<>();

    public CrashReportCollectHelper(Context context) {
        super(context);
    }

    @Override
    protected Cursor getSourceData(Uri parentUri) {
        return context.getContentResolver().query(StatisticContentContract.CrashReport.CONTENT_URI, new
                String[]{StatisticContentContract.CrashReport.ID, StatisticContentContract.CrashReport.NAME,
                StatisticContentContract.CrashReport.VERSION, StatisticContentContract.CrashReport.EXCEPTION,
                StatisticContentContract.CrashReport.CAUSE, StatisticContentContract.CrashReport.STACKTRACE,
                         StatisticContentContract.CrashReport.HAPPENED_AT}, null, null,
                                                  StatisticContentContract.CrashReport.ID + " LIMIT 1");
    }

    @Override
    protected JSONObject getExtractedItem(Cursor source) throws JSONException {
        batch.add(ContentProviderOperation.newDelete(ContentUris.withAppendedId(StatisticContentContract.CrashReport
                .CONTENT_URI, source.getLong(source.getColumnIndex(StatisticContentContract.CrashReport.ID)))).build());
        JSONObject item = new JSONObject();
        item.put(StatisticServerContract.CrashReportData.NAME, source.getString(source.getColumnIndex
                (StatisticContentContract.CrashReport.NAME))).
                put(StatisticServerContract.CrashReportData.VERSION, source.getString(source.getColumnIndex
                        (StatisticContentContract.CrashReport.VERSION))).
                put(StatisticServerContract.CrashReportData.EXCEPTION, source.getString(source.getColumnIndex
                        (StatisticContentContract.CrashReport.EXCEPTION))).
                put(StatisticServerContract.CrashReportData.CAUSE, source.getString(source.getColumnIndex
                        (StatisticContentContract.CrashReport.CAUSE))).
                put(StatisticServerContract.CrashReportData.STACKTRACE, source.getString(source.getColumnIndex
                        (StatisticContentContract.CrashReport.STACKTRACE))).
                put(StatisticServerContract.CrashReportData.HAPPENED_AT, source.getString(source.getColumnIndex
                        (StatisticContentContract.CrashReport.HAPPENED_AT)));
        return item;
    }

    public void complete() throws RemoteException, OperationApplicationException {
        context.getContentResolver().applyBatch(StatisticContentContract.CrashReport.CONTENT_URI.getAuthority(), batch);
    }

}
