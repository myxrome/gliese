package com.whiteboxteam.gliese.data.helper.statistic;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import com.whiteboxteam.gliese.data.content.StatisticContentContract;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 19.03.2015
 * Time: 11:55
 */
public final class SessionHelper {

    private static SessionHelper instance;
    private Context context;
    private Uri current;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);

    private SessionHelper(Context context) {
        this.context = context;
    }

    public static SessionHelper getInstance(Context context) {
        if (instance == null) instance = new SessionHelper(context);
        return instance;
    }

    public Uri getCurrent() {
        return current;
    }

    Uri getOrCreateCurrent() {
        if (current == null) start();
        return current;
    }

    public void start() {
        if (current != null) finish();
        ContentValues contentValues = new ContentValues();
        contentValues.put(StatisticContentContract.Session.STARTED_AT, getCurrentDateTimeString());
        current = context.getContentResolver().insert(StatisticContentContract.Session.CONTENT_URI, contentValues);
    }

    public void finish() {
        if (current == null) return;
        ContentValues contentValues = new ContentValues();
        contentValues.put(StatisticContentContract.Session.FINISHED_AT, getCurrentDateTimeString());
        context.getContentResolver().update(current, contentValues, null, null);
        current = null;
//        StatisticSyncService.runStatisticSync(context);
    }

    private String getCurrentDateTimeString() {
        return dateFormat.format(new Date());
    }

}
