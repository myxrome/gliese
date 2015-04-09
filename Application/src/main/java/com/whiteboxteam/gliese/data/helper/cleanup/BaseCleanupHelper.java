package com.whiteboxteam.gliese.data.helper.cleanup;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.whiteboxteam.gliese.data.db.ApplicationDatabaseContract;
import com.whiteboxteam.gliese.data.db.ApplicationDatabaseHelper;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 09.02.2015
 * Time: 12:35
 */
public abstract class BaseCleanupHelper {

    private final ApplicationDatabaseHelper dbHelper;

    public BaseCleanupHelper(Context context) {
        dbHelper = ApplicationDatabaseHelper.getInstance(context);
    }

    public void applyInactive() {
        StringBuilder sql = new StringBuilder("UPDATE ").append(getTableName()).append(" SET ").
                append(ApplicationDatabaseContract.BaseEntry.ACTIVE).append(" = 0 WHERE ").
                append(ApplicationDatabaseContract.BaseEntry.ACTIVE).append(" = 1 AND ").append(getParentKeyName()).
                append(" IN (SELECT ").append(ApplicationDatabaseContract.BaseEntry.ID).append(" FROM ").
                append(getParentTableName()).append(" WHERE ").
                append(ApplicationDatabaseContract.BaseEntry.ACTIVE).append(" = 0)");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(sql.toString());
    }

    protected abstract String getTableName();

    protected abstract String getParentKeyName();

    protected abstract String getParentTableName();

}
