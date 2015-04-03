package com.whiteboxteam.gliese.data.helper.merge;

import android.content.Context;
import android.net.Uri;
import com.whiteboxteam.gliese.data.content.ApplicationContentContract;
import com.whiteboxteam.gliese.data.db.ApplicationDatabaseContract;
import com.whiteboxteam.gliese.data.server.ApplicationServerContract;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 23.01.2015
 * Time: 16:25
 */
public final class TopicGroupMergeHelper extends BaseMergeHelper {

    public TopicGroupMergeHelper(Context context, Uri uri) {
        super(context, uri);

        nameMapping.put(ApplicationContentContract.TopicGroup.ORDER, ApplicationServerContract.TopicGroupRecord.ORDER);
        nameMapping.put(ApplicationContentContract.TopicGroup.KEY, ApplicationServerContract.TopicGroupRecord.KEY);
        nameMapping.put(ApplicationContentContract.TopicGroup.NAME, ApplicationServerContract.TopicGroupRecord.NAME);

    }

    @Override
    protected String getTableName() {
        return ApplicationDatabaseContract.TopicGroupEntry.TABLE_NAME;
    }

}
