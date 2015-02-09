package com.whiteboxteam.gliese.data.helper.cleanup;

import android.content.Context;
import com.whiteboxteam.gliese.data.db.ApplicationDatabaseContract;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 29.01.2015
 * Time: 13:37
 */
public class PromoCleanupHelper extends BaseCleanupHelper {

    public PromoCleanupHelper(Context context) {
        super(context);
    }

    @Override
    protected String getTableName() {
        return ApplicationDatabaseContract.PromoEntry.TABLE_NAME;
    }

    @Override
    protected String getParentKeyName() {
        return ApplicationDatabaseContract.PromoEntry.VALUE_ID;
    }

    @Override
    protected String getParentTableName() {
        return ApplicationDatabaseContract.ValueEntry.TABLE_NAME;
    }
}
