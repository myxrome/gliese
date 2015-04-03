package com.whiteboxteam.gliese.data.helper.cleanup;

import android.content.Context;
import com.whiteboxteam.gliese.data.db.ApplicationDatabaseContract;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 23.01.2015
 * Time: 16:15
 */
public class TopicCleanupHelper extends BaseCleanupHelper {


    public TopicCleanupHelper(Context context) {
        super(context);
    }

    @Override
    protected String getParentTableName() {
        return ApplicationDatabaseContract.TopicGroupEntry.TABLE_NAME;
    }

    @Override
    protected String getParentKeyName() {
        return ApplicationDatabaseContract.TopicEntry.TOPIC_GROUP_ID;
    }

    @Override
    protected String getTableName() {
        return ApplicationDatabaseContract.TopicEntry.TABLE_NAME;
    }
}
