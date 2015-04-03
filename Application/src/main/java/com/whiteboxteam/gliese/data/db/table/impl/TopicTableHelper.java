package com.whiteboxteam.gliese.data.db.table.impl;

import android.database.sqlite.SQLiteDatabase;
import com.whiteboxteam.gliese.data.db.ApplicationDatabaseContract;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 24.05.2014
 * Time: 13:39
 */
public final class TopicTableHelper extends BaseTableHelper {

    private static final String CREATE_TABLE_SQL = CREATE_TABLE + ApplicationDatabaseContract.TopicEntry.TABLE_NAME +
            COLUMNS_BEGIN +
            ApplicationDatabaseContract.TopicEntry.ID + INTEGER_PK_TYPE + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.TopicEntry.TOPIC_GROUP_ID + INTEGER_TYPE + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.TopicEntry.ORDER + INTEGER_TYPE + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.TopicEntry.ACTIVE + INTEGER_TYPE + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.TopicEntry.NAME + TEXT_TYPE + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.TopicEntry.UPDATED_AT + TEXT_TYPE + COLUMNS_SEPARATOR +
            FOREIGN_KEY + COLUMNS_BEGIN + ApplicationDatabaseContract.TopicEntry.TOPIC_GROUP_ID + COLUMNS_END +
            REFERENCES +
            ApplicationDatabaseContract.TopicGroupEntry.TABLE_NAME + COLUMNS_BEGIN + ApplicationDatabaseContract
            .TopicGroupEntry.ID + COLUMNS_END + ON_DELETE_CASCADE +
            COLUMNS_END;

    private static final String CREATE_INDEX_1_SQL = CREATE_INDEX + ApplicationDatabaseContract.TopicEntry.TABLE_NAME
            + INDEX_SUFFIX + "1" +
            INDEX_FOR_TABLE + ApplicationDatabaseContract.TopicEntry.TABLE_NAME + COLUMNS_BEGIN +
            ApplicationDatabaseContract.TopicEntry.TOPIC_GROUP_ID + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.TopicEntry.ORDER +
            COLUMNS_END;

    private static final String CREATE_INDEX_2_SQL = CREATE_INDEX + ApplicationDatabaseContract.TopicEntry.TABLE_NAME
            + INDEX_SUFFIX + "2" +
            INDEX_FOR_TABLE + ApplicationDatabaseContract.TopicEntry.TABLE_NAME + COLUMNS_BEGIN +
            ApplicationDatabaseContract.TopicEntry.ACTIVE +
            COLUMNS_END;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
        db.execSQL(CREATE_INDEX_1_SQL);
        db.execSQL(CREATE_INDEX_2_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
