package com.whiteboxteam.gliese.data.db.table.impl;

import android.database.sqlite.SQLiteDatabase;
import com.whiteboxteam.gliese.data.db.StatisticDatabaseContract;
import com.whiteboxteam.gliese.data.db.table.TableHelper;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 11.08.2014
 * Time: 21:02
 */
public class FactTableHelper extends BaseTableHelper implements TableHelper {

    private static final String CREATE_TABLE_SQL = CREATE_TABLE + StatisticDatabaseContract.FactEntry.TABLE_NAME + COLUMNS_BEGIN +
            StatisticDatabaseContract.FactEntry.ID + INTEGER_PK_TYPE + AUTOINCREMENT + COLUMNS_SEPARATOR +
            StatisticDatabaseContract.FactEntry.SESSION_ID + TEXT_TYPE + COLUMNS_SEPARATOR +
            StatisticDatabaseContract.FactEntry.CONTEXT_ID + INTEGER_TYPE + COLUMNS_SEPARATOR +
            StatisticDatabaseContract.FactEntry.CONTEXT_TYPE + TEXT_TYPE + COLUMNS_SEPARATOR +
            StatisticDatabaseContract.FactEntry.EVENT + TEXT_TYPE + COLUMNS_SEPARATOR +
            FOREIGN_KEY + COLUMNS_BEGIN + StatisticDatabaseContract.FactEntry.SESSION_ID + COLUMNS_END + REFERENCES +
            StatisticDatabaseContract.FactEntry.TABLE_NAME + COLUMNS_BEGIN + StatisticDatabaseContract.SessionEntry.ID + COLUMNS_END + ON_DELETE_CASCADE +
            COLUMNS_END;

    private static final String CREATE_INDEX_1_SQL = CREATE_INDEX + StatisticDatabaseContract.FactEntry.TABLE_NAME + INDEX_SUFFIX + "1" +
            INDEX_FOR_TABLE + StatisticDatabaseContract.FactEntry.TABLE_NAME + COLUMNS_BEGIN +
            StatisticDatabaseContract.FactEntry.SESSION_ID +
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
