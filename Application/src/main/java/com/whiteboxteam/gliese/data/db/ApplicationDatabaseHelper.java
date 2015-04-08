package com.whiteboxteam.gliese.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.whiteboxteam.gliese.data.db.table.TableHelper;
import com.whiteboxteam.gliese.data.db.table.impl.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 08.10.13
 * Time: 22:17
 */
public final class ApplicationDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "gliese.db";
    private static final int DB_VERSION = 1;
    private static final Collection<TableHelper> TABLE_HELPERS = new ArrayList<>();
    private static ApplicationDatabaseHelper instance;

    static {
        TABLE_HELPERS.add(new TopicGroupTableHelper());
        TABLE_HELPERS.add(new TopicTableHelper());
        TABLE_HELPERS.add(new CategoryTableHelper());
        TABLE_HELPERS.add(new ValueTableHelper());
        TABLE_HELPERS.add(new DescriptionTableHelper());
        TABLE_HELPERS.add(new PromoTableHelper());
    }

    private ApplicationDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized ApplicationDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new ApplicationDatabaseHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (TableHelper helper : TABLE_HELPERS)
            helper.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (TableHelper helper : TABLE_HELPERS)
            helper.onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys = ON");
    }
}
