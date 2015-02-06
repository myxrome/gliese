package com.whiteboxteam.gliese.data.db.table.impl;

import android.database.sqlite.SQLiteDatabase;
import com.whiteboxteam.gliese.data.db.ApplicationDatabaseContract;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 16.10.13
 * Time: 23:07
 */
public final class CategoryTableHelper extends BaseTableHelper {

    private static final String CREATE_TABLE_SQL = CREATE_TABLE + ApplicationDatabaseContract.CategoryEntry
            .TABLE_NAME + COLUMNS_BEGIN +
            ApplicationDatabaseContract.CategoryEntry.ID + INTEGER_PK_TYPE + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.CategoryEntry.TOPIC_ID + INTEGER_TYPE + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.CategoryEntry.ORDER + INTEGER_TYPE + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.CategoryEntry.ACTIVE + INTEGER_TYPE + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.CategoryEntry.NAME + TEXT_TYPE + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.CategoryEntry.UPDATED_AT + TEXT_TYPE + COLUMNS_SEPARATOR +
            FOREIGN_KEY + COLUMNS_BEGIN + ApplicationDatabaseContract.CategoryEntry.TOPIC_ID + COLUMNS_END +
            REFERENCES +
            ApplicationDatabaseContract.TopicEntry.TABLE_NAME + COLUMNS_BEGIN + ApplicationDatabaseContract
            .TopicEntry.ID + COLUMNS_END + ON_DELETE_CASCADE +
            COLUMNS_END;

    private static final String CREATE_INDEX_1_SQL = CREATE_INDEX + ApplicationDatabaseContract.CategoryEntry
            .TABLE_NAME + INDEX_SUFFIX + "1" +
            INDEX_FOR_TABLE + ApplicationDatabaseContract.CategoryEntry.TABLE_NAME + COLUMNS_BEGIN +
            ApplicationDatabaseContract.CategoryEntry.TOPIC_ID + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.CategoryEntry.ACTIVE + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.CategoryEntry.ORDER +
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
