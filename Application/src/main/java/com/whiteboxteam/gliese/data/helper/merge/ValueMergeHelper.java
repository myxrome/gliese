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
 * Date: 16.01.14
 * Time: 12:07
 */
public final class ValueMergeHelper extends BaseMergeHelper {

    public ValueMergeHelper(Context context, SyncResult syncResult, Uri uri) {
        super(context, syncResult, uri);

        nameMapping.put(ApplicationContentContract.Value.CATEGORY_ID, ApplicationServerContract.ValueRecord
                .CATEGORY_ID);
        nameMapping.put(ApplicationContentContract.Value.NAME, ApplicationServerContract.ValueRecord.NAME);
        nameMapping.put(ApplicationContentContract.Value.REMOTE_THUMB_URI, ApplicationServerContract.ValueRecord
                .THUMB_REMOTE_URI);
        nameMapping.put(ApplicationContentContract.Value.OLD_PRICE, ApplicationServerContract.ValueRecord.OLD_PRICE);
        nameMapping.put(ApplicationContentContract.Value.DISCOUNT, ApplicationServerContract.ValueRecord.DISCOUNT);
        nameMapping.put(ApplicationContentContract.Value.NEW_PRICE, ApplicationServerContract.ValueRecord.NEW_PRICE);
        nameMapping.put(ApplicationContentContract.Value.URL, ApplicationServerContract.ValueRecord.URL);

    }

    @Override
    protected String getTableName() {
        return ApplicationDatabaseContract.ValueEntry.TABLE_NAME;
    }

}
