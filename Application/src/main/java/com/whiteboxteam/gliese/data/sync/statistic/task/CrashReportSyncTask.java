package com.whiteboxteam.gliese.data.sync.statistic.task;

import android.content.Context;
import com.whiteboxteam.gliese.data.helper.collect.CrashReportCollectHelper;
import com.whiteboxteam.gliese.data.helper.collect.JSONArrayCollectHelper;
import com.whiteboxteam.gliese.data.server.StatisticServerContract;
import com.whiteboxteam.gliese.data.storage.StorageContract;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 21.03.2015
 * Time: 14:51
 */
public class CrashReportSyncTask extends BaseSyncTask {

    public CrashReportSyncTask(Context context) {
        super(context);
    }

    @Override
    protected JSONArrayCollectHelper getContentCollectHelper() {
        return new CrashReportCollectHelper(context);
    }

    @Override
    protected String getLastStatisticSyncDateKey() {
        return StorageContract.LAST_CRASH_REPORT_DATE;
    }

    @Override
    protected String getUploadQueryUrl(Context context, JSONObject object) throws IOException {
        return StatisticServerContract.Server.getCrashReportUploadQueryUrl(context, object);
    }

}
