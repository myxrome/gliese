package com.whiteboxteam.gliese.data.sync.statistic.task;

import android.content.Context;
import com.whiteboxteam.gliese.data.helper.collect.JSONArrayCollectHelper;
import com.whiteboxteam.gliese.data.helper.collect.SessionCollectHelper;
import com.whiteboxteam.gliese.data.server.StatisticServerContract;
import com.whiteboxteam.gliese.data.storage.StorageContract;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 09.02.2015
 * Time: 14:20
 */
public class StatisticSyncTask extends BaseSyncTask {

    public StatisticSyncTask(Context context) {
        super(context);
    }

    @Override
    protected JSONArrayCollectHelper getContentCollectHelper() {
        return new SessionCollectHelper(context);
    }

    @Override
    protected String getLastStatisticSyncDateKey() {
        return StorageContract.LAST_STATISTIC_SYNC_DATE;
    }

    @Override
    protected String getUploadQueryUrl(Context context, JSONObject object) throws IOException {
        return StatisticServerContract.Server.getStatisticUploadQueryUrl(context, object);
    }

}
