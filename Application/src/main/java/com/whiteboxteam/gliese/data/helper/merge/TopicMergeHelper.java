package com.whiteboxteam.gliese.data.helper.merge;

import android.content.Context;
import android.content.SyncResult;
import android.net.Uri;
import com.whiteboxteam.gliese.data.content.ApplicationContentContract;
import com.whiteboxteam.gliese.data.db.ApplicationDatabaseContract;
import com.whiteboxteam.gliese.data.server.ApplicationServerContract;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 24.05.2014
 * Time: 23:00
 */
public final class TopicMergeHelper extends BaseMergeHelper {


    public TopicMergeHelper(Context context, SyncResult syncResult, Uri uri) {
        super(context, syncResult, uri);

        nameMapping.put(ApplicationContentContract.Topic.TOPIC_GROUP_ID, ApplicationServerContract.TopicRecord
                .TOPIC_GROUP_ID);
        nameMapping.put(ApplicationContentContract.Topic.ORDER, ApplicationServerContract.TopicRecord.ORDER);
        nameMapping.put(ApplicationContentContract.Topic.NAME, ApplicationServerContract.TopicRecord.NAME);

    }

    @Override
    protected String getTableName() {
        return ApplicationDatabaseContract.TopicEntry.TABLE_NAME;
    }

}
