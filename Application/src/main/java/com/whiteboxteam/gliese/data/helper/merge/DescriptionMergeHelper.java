package com.whiteboxteam.gliese.data.helper.merge;

import android.content.Context;
import android.net.Uri;
import com.whiteboxteam.gliese.data.content.ApplicationContentContract;
import com.whiteboxteam.gliese.data.db.ApplicationDatabaseContract;
import com.whiteboxteam.gliese.data.server.ApplicationServerContract;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 30.01.14
 * Time: 11:46
 */
public final class DescriptionMergeHelper extends BaseMergeHelper {

    public DescriptionMergeHelper(Context context, Uri uri) {
        super(context, uri);

        nameMapping.put(ApplicationContentContract.Description.VALUE_ID, ApplicationServerContract.DescriptionRecord
                .VALUE_ID);
        nameMapping.put(ApplicationContentContract.Description.ORDER, ApplicationServerContract.DescriptionRecord
                .ORDER);
        nameMapping.put(ApplicationContentContract.Description.CAPTION, ApplicationServerContract.DescriptionRecord
                .CAPTION);
        nameMapping.put(ApplicationContentContract.Description.TEXT, ApplicationServerContract.DescriptionRecord.TEXT);
        nameMapping.put(ApplicationContentContract.Description.RED, ApplicationServerContract.DescriptionRecord.RED);
        nameMapping.put(ApplicationContentContract.Description.BOLD, ApplicationServerContract.DescriptionRecord.BOLD);

    }

    @Override
    protected String getTableName() {
        return ApplicationDatabaseContract.DescriptionEntry.TABLE_NAME;
    }
}
