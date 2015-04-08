package com.whiteboxteam.gliese.data.helper.statistic;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.whiteboxteam.gliese.data.db.StatisticDatabaseContract;
import com.whiteboxteam.gliese.data.db.StatisticDatabaseHelper;
import com.whiteboxteam.gliese.data.sync.statistic.StatisticSyncService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 19.03.2015
 * Time: 11:55
 */
public final class SessionHelper {

    private static SessionHelper           instance;
    private final  StatisticDatabaseHelper dbHelper;
    private        Context                 context;
    private long current = -1;
    private SimpleDateFormat dateFormat;


    private SessionHelper(Context context) {
        this.context = context;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("gmt"));

        dbHelper = StatisticDatabaseHelper.getInstance(context);
    }

    public static SessionHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SessionHelper(context);
        }
        return instance;
    }

    public long getCurrentId() {
        return current;
    }

    public void start() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(StatisticDatabaseContract.SessionEntry.STARTED_AT, getCurrentDateTimeString());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        current = db.insert(StatisticDatabaseContract.SessionEntry.TABLE_NAME, null, contentValues);
        db.close();
    }

    private String getCurrentDateTimeString() {
        return dateFormat.format(new Date());
    }

    public void finish() {
        if (current < 0)
            return;
        ContentValues contentValues = new ContentValues();
        contentValues.put(StatisticDatabaseContract.SessionEntry.FINISHED_AT, getCurrentDateTimeString());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update(StatisticDatabaseContract.SessionEntry.TABLE_NAME, contentValues,
                  StatisticDatabaseContract.SessionEntry.ID + " = ?", new String[] {String.valueOf(current)});
        current = -1;
        db.close();
        StatisticSyncService.startStatisticSync(context);
    }

}
