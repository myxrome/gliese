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
 * Date: 24.12.13
 * Time: 13:03
 */
public final class CategoryMergeHelper extends BaseMergeHelper {


    public CategoryMergeHelper(Context context, SyncResult syncResult, Uri uri) {
        super(context, syncResult, uri);

        nameMapping.put(ApplicationContentContract.Category.TOPIC_ID, ApplicationServerContract.CategoryRecord.TOPIC_ID);
        nameMapping.put(ApplicationContentContract.Category.ORDER, ApplicationServerContract.CategoryRecord.ORDER);
        nameMapping.put(ApplicationContentContract.Category.NAME, ApplicationServerContract.CategoryRecord.NAME);

    }

    @Override
    protected String getTableName() {
        return ApplicationDatabaseContract.CategoryEntry.TABLE_NAME;
    }

}
