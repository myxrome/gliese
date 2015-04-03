package com.whiteboxteam.gliese.data.db.table.impl;

import android.database.sqlite.SQLiteDatabase;
import com.whiteboxteam.gliese.data.db.StatisticDatabaseContract;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 21.03.2015
 * Time: 11:55
 */
public class CrashReportTableHelper extends BaseTableHelper {

    private static final String CREATE_TABLE_SQL = CREATE_TABLE + StatisticDatabaseContract.CrashReportEntry
            .TABLE_NAME +
            COLUMNS_BEGIN +
            StatisticDatabaseContract.CrashReportEntry.ID + INTEGER_PK_TYPE + AUTOINCREMENT + COLUMNS_SEPARATOR +
            StatisticDatabaseContract.CrashReportEntry.NAME + TEXT_TYPE + COLUMNS_SEPARATOR +
            StatisticDatabaseContract.CrashReportEntry.VERSION + TEXT_TYPE + COLUMNS_SEPARATOR +
            StatisticDatabaseContract.CrashReportEntry.EXCEPTION + TEXT_TYPE + COLUMNS_SEPARATOR +
            StatisticDatabaseContract.CrashReportEntry.CAUSE + TEXT_TYPE + COLUMNS_SEPARATOR +
            StatisticDatabaseContract.CrashReportEntry.STACKTRACE + TEXT_TYPE + COLUMNS_SEPARATOR +
            StatisticDatabaseContract.CrashReportEntry.HAPPENED_AT + TEXT_TYPE +
            COLUMNS_END;


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
