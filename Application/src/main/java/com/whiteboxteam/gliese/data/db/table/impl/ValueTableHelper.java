package com.whiteboxteam.gliese.data.db.table.impl;

import android.database.sqlite.SQLiteDatabase;
import com.whiteboxteam.gliese.data.db.ApplicationDatabaseContract;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 16.10.13
 * Time: 23:25
 */
public final class ValueTableHelper extends BaseTableHelper {

    private static final String CREATE_TABLE_SQL = CREATE_TABLE + ApplicationDatabaseContract.ValueEntry.TABLE_NAME +
            COLUMNS_BEGIN +
            ApplicationDatabaseContract.ValueEntry.ID + INTEGER_PK_TYPE + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.ValueEntry.CATEGORY_ID + INTEGER_TYPE + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.ValueEntry.ACTIVE + INTEGER_TYPE + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.ValueEntry.NAME + TEXT_TYPE + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.ValueEntry.THUMB_REMOTE_URI + TEXT_TYPE + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.ValueEntry.THUMB_LOCAL_URI + TEXT_TYPE + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.ValueEntry.OLD_PRICE + INTEGER_TYPE + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.ValueEntry.DISCOUNT + INTEGER_TYPE + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.ValueEntry.NEW_PRICE + INTEGER_TYPE + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.ValueEntry.URL + TEXT_TYPE + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.ValueEntry.UPDATED_AT + TEXT_TYPE + COLUMNS_SEPARATOR +
            FOREIGN_KEY + COLUMNS_BEGIN + ApplicationDatabaseContract.ValueEntry.CATEGORY_ID + COLUMNS_END +
            REFERENCES +
            ApplicationDatabaseContract.CategoryEntry.TABLE_NAME + COLUMNS_BEGIN + ApplicationDatabaseContract
            .CategoryEntry.ID + COLUMNS_END + ON_DELETE_CASCADE +
            COLUMNS_END;

    private static final String CREATE_INDEX_1_SQL = CREATE_INDEX + ApplicationDatabaseContract.ValueEntry.TABLE_NAME
            + INDEX_SUFFIX + "1" +
            INDEX_FOR_TABLE + ApplicationDatabaseContract.ValueEntry.TABLE_NAME + COLUMNS_BEGIN +
            ApplicationDatabaseContract.ValueEntry.CATEGORY_ID + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.ValueEntry.ACTIVE +
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
