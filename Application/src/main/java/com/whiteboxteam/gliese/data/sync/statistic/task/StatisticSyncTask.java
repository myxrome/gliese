package com.whiteboxteam.gliese.data.sync.statistic.task;

import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import com.whiteboxteam.gliese.data.helper.collect.BaseCollectHelper;
import com.whiteboxteam.gliese.data.helper.collect.DeviceCollectHelper;
import com.whiteboxteam.gliese.data.server.ServerHelper;
import com.whiteboxteam.gliese.data.server.StatisticServerContract;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 09.02.2015
 * Time: 14:20
 */
public class StatisticSyncTask implements Runnable {

    private Context context;

    public StatisticSyncTask(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        BaseCollectHelper<JSONObject> deviceCollectHelper = new DeviceCollectHelper(context);
        try {
            JSONObject data = new JSONObject();
            data.put(StatisticServerContract.StatisticData.DATA, deviceCollectHelper.collect());
            ServerHelper.uploadJSONObject(context, StatisticServerContract.Server.getStatisticSyncUrl(), data);
            deviceCollectHelper.complete();
        } catch (IOException | RemoteException | JSONException | OperationApplicationException e) {
        }
    }

}
