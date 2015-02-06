package com.whiteboxteam.gliese.data.db.table.impl;

import android.database.sqlite.SQLiteDatabase;
import com.whiteboxteam.gliese.data.db.ApplicationDatabaseContract;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 16.12.2014
 * Time: 13:07
 */

public class TopicGroupTableHelper extends BaseTableHelper {

    private static final String CREATE_TABLE_SQL = CREATE_TABLE + ApplicationDatabaseContract.TopicGroupEntry.TABLE_NAME + COLUMNS_BEGIN +
            ApplicationDatabaseContract.TopicGroupEntry.ID + INTEGER_PK_TYPE + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.TopicGroupEntry.ORDER + INTEGER_TYPE + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.TopicGroupEntry.ACTIVE + INTEGER_TYPE + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.TopicGroupEntry.KEY + TEXT_TYPE + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.TopicGroupEntry.NAME + TEXT_TYPE + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.TopicGroupEntry.UPDATED_AT + TEXT_TYPE +
            COLUMNS_END;

    private static final String CREATE_INDEX_1_SQL = CREATE_INDEX + ApplicationDatabaseContract.TopicGroupEntry.TABLE_NAME + INDEX_SUFFIX + "1" +
            INDEX_FOR_TABLE + ApplicationDatabaseContract.TopicGroupEntry.TABLE_NAME + COLUMNS_BEGIN +
            ApplicationDatabaseContract.TopicGroupEntry.ACTIVE +
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
