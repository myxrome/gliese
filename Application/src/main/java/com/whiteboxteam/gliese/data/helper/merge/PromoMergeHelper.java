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
 * Date: 30.01.14
 * Time: 13:02
 */
public final class PromoMergeHelper extends BaseMergeHelper {

    public PromoMergeHelper(Context context, SyncResult syncResult, Uri uri) {
        super(context, syncResult, uri);

        nameMapping.put(ApplicationContentContract.Promo.VALUE_ID, ApplicationServerContract.PromoRecord.VALUE_ID);
        nameMapping.put(ApplicationContentContract.Promo.ORDER, ApplicationServerContract.PromoRecord.ORDER);
        nameMapping.put(ApplicationContentContract.Promo.REMOTE_IMAGE_URI, ApplicationServerContract.PromoRecord.REMOTE_IMAGE_URI);

    }

    @Override
    protected String getTableName() {
        return ApplicationDatabaseContract.PromoEntry.TABLE_NAME;
    }

}
