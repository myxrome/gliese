package com.whiteboxteam.gliese.data.db.table.impl;

import android.database.sqlite.SQLiteDatabase;
import com.whiteboxteam.gliese.data.db.ApplicationDatabaseContract;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 17.10.13
 * Time: 0:10
 */
public final class PromoTableHelper extends BaseTableHelper {

    private static final String CREATE_TABLE_SQL = CREATE_TABLE + ApplicationDatabaseContract.PromoEntry.TABLE_NAME +
            COLUMNS_BEGIN +
            ApplicationDatabaseContract.PromoEntry.ID + INTEGER_PK_TYPE + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.PromoEntry.VALUE_ID + INTEGER_TYPE + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.PromoEntry.ORDER + INTEGER_TYPE + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.ValueEntry.ACTIVE + INTEGER_TYPE + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.PromoEntry.REMOTE_IMAGE_URI + TEXT_TYPE + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.PromoEntry.LOCAL_IMAGE_URI + TEXT_TYPE + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.PromoEntry.UPDATED_AT + TEXT_TYPE + COLUMNS_SEPARATOR +
            FOREIGN_KEY + COLUMNS_BEGIN + ApplicationDatabaseContract.PromoEntry.VALUE_ID + COLUMNS_END + REFERENCES +
            ApplicationDatabaseContract.ValueEntry.TABLE_NAME + COLUMNS_BEGIN + ApplicationDatabaseContract
            .ValueEntry.ID + COLUMNS_END + ON_DELETE_CASCADE +
            COLUMNS_END;

    private static final String CREATE_INDEX_1_SQL = CREATE_INDEX + ApplicationDatabaseContract.PromoEntry.TABLE_NAME
            + INDEX_SUFFIX + "1" +
            INDEX_FOR_TABLE + ApplicationDatabaseContract.PromoEntry.TABLE_NAME + COLUMNS_BEGIN +
            ApplicationDatabaseContract.PromoEntry.VALUE_ID + COLUMNS_SEPARATOR +
            ApplicationDatabaseContract.PromoEntry.ORDER +
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
