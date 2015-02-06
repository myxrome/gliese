package com.whiteboxteam.gliese.data.db.table.impl;

import android.database.sqlite.SQLiteDatabase;
import com.whiteboxteam.gliese.data.db.StatisticDatabaseContract;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 25.08.2014
 * Time: 23:39
 */
public final class FactDetailTableHelper extends BaseTableHelper {

    private static final String CREATE_TABLE_SQL = CREATE_TABLE + StatisticDatabaseContract.FactDetailEntry.TABLE_NAME + COLUMNS_BEGIN +
            StatisticDatabaseContract.FactDetailEntry.ID + INTEGER_PK_TYPE + AUTOINCREMENT + COLUMNS_SEPARATOR +
            StatisticDatabaseContract.FactDetailEntry.FACT_ID + INTEGER_TYPE + COLUMNS_SEPARATOR +
            StatisticDatabaseContract.FactDetailEntry.ORDER + INTEGER_TYPE + COLUMNS_SEPARATOR +
            StatisticDatabaseContract.FactDetailEntry.HAPPENED_AT + TEXT_TYPE + COLUMNS_SEPARATOR +
            FOREIGN_KEY + COLUMNS_BEGIN + StatisticDatabaseContract.FactDetailEntry.FACT_ID + COLUMNS_END + REFERENCES +
            StatisticDatabaseContract.FactDetailEntry.TABLE_NAME + COLUMNS_BEGIN + StatisticDatabaseContract.FactEntry.ID + COLUMNS_END + ON_DELETE_CASCADE +
            COLUMNS_END;

    private static final String CREATE_INDEX_1_SQL = CREATE_INDEX + StatisticDatabaseContract.FactDetailEntry.TABLE_NAME + INDEX_SUFFIX + "1" +
            INDEX_FOR_TABLE + StatisticDatabaseContract.FactDetailEntry.TABLE_NAME + COLUMNS_BEGIN +
            StatisticDatabaseContract.FactDetailEntry.FACT_ID +
            COLUMNS_END;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
        db.execSQL(CREATE_INDEX_1_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
