package com.whiteboxteam.gliese.data.helper.cleanup;

import android.content.Context;
import com.whiteboxteam.gliese.data.db.ApplicationDatabaseContract;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 29.01.2015
 * Time: 13:37
 */
public class DescriptionCleanupHelper extends BaseCleanupHelper {

    public DescriptionCleanupHelper(Context context) {
        super(context);
    }

    @Override
    protected String getTableName() {
        return ApplicationDatabaseContract.DescriptionEntry.TABLE_NAME;
    }

    @Override
    protected String getParentKeyName() {
        return ApplicationDatabaseContract.DescriptionEntry.VALUE_ID;
    }

    @Override
    protected String getParentTableName() {
        return ApplicationDatabaseContract.ValueEntry.TABLE_NAME;
    }
}
