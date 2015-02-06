package com.whiteboxteam.gliese.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.whiteboxteam.gliese.data.db.table.TableHelper;
import com.whiteboxteam.gliese.data.db.table.impl.FactDetailTableHelper;
import com.whiteboxteam.gliese.data.db.table.impl.FactTableHelper;
import com.whiteboxteam.gliese.data.db.table.impl.SessionTableHelper;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 11.08.2014
 * Time: 21:09
 */
public final class StatisticDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "statistics.db";
    private static final int DB_VERSION = 1;
    private static final Collection<TableHelper> TABLE_HELPERS = new ArrayList<>();

    static {
        TABLE_HELPERS.add(new SessionTableHelper());
        TABLE_HELPERS.add(new FactTableHelper());
        TABLE_HELPERS.add(new FactDetailTableHelper());
    }

    public StatisticDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
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
}
