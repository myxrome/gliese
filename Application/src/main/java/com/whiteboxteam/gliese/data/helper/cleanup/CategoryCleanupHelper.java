package com.whiteboxteam.gliese.data.helper.cleanup;

import android.content.Context;
import com.whiteboxteam.gliese.data.db.ApplicationDatabaseContract;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 23.01.2015
 * Time: 15:38
 */
public class CategoryCleanupHelper extends BaseCleanupHelper {

    public CategoryCleanupHelper(Context context) {
        super(context);
    }

    @Override
    protected String getTableName() {
        return ApplicationDatabaseContract.CategoryEntry.TABLE_NAME;
    }

    @Override
    protected String getParentKeyName() {
        return ApplicationDatabaseContract.CategoryEntry.TOPIC_ID;
    }

    @Override
    protected String getParentTableName() {
        return ApplicationDatabaseContract.TopicEntry.TABLE_NAME;
    }
}
