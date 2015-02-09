package com.whiteboxteam.gliese.data.helper.cleanup;

import android.content.Context;
import com.whiteboxteam.gliese.data.db.ApplicationDatabaseContract;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 23.01.2015
 * Time: 15:12
 */
public class ValueCleanupHelper extends BaseCleanupHelper {

    public ValueCleanupHelper(Context context) {
        super(context);
    }

    @Override
    protected String getTableName() {
        return ApplicationDatabaseContract.ValueEntry.TABLE_NAME;
    }

    @Override
    protected String getParentKeyName() {
        return ApplicationDatabaseContract.ValueEntry.CATEGORY_ID;
    }

    @Override
    protected String getParentTableName() {
        return ApplicationDatabaseContract.CategoryEntry.TABLE_NAME;
    }
}
