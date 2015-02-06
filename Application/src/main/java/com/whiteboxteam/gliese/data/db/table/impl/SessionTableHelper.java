package com.whiteboxteam.gliese.data.db.table.impl;

import android.database.sqlite.SQLiteDatabase;
import com.whiteboxteam.gliese.data.db.StatisticDatabaseContract;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 25.08.2014
 * Time: 23:35
 */
public class SessionTableHelper extends BaseTableHelper {

    private static final String CREATE_TABLE_SQL = CREATE_TABLE + StatisticDatabaseContract.SessionEntry.TABLE_NAME + COLUMNS_BEGIN +
            StatisticDatabaseContract.SessionEntry.ID + INTEGER_PK_TYPE + AUTOINCREMENT + COLUMNS_SEPARATOR +
            StatisticDatabaseContract.SessionEntry.STARTED_AT + TEXT_TYPE + COLUMNS_SEPARATOR +
            StatisticDatabaseContract.SessionEntry.FINISHED_AT + TEXT_TYPE +
            COLUMNS_END;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
