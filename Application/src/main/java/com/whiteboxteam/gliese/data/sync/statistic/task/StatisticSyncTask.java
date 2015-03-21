package com.whiteboxteam.gliese.data.sync.statistic.task;

import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;
import com.whiteboxteam.gliese.data.helper.collect.BaseCollectHelper;
import com.whiteboxteam.gliese.data.helper.collect.DeviceCollectHelper;
import com.whiteboxteam.gliese.data.server.ServerHelper;
import com.whiteboxteam.gliese.data.server.StatisticServerContract;
import com.whiteboxteam.gliese.data.storage.StorageContract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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
        Log.d("[SYNC]", "start statistic sync");
        if (performStatisticSync()) {
            saveLastSyncDate();
        }
        Log.d("[SYNC]", "finish statistic sync");
    }

    private boolean performStatisticSync() {
        BaseCollectHelper<JSONObject> deviceCollectHelper = new DeviceCollectHelper(context);
        try {
            JSONObject device = deviceCollectHelper.collect();
            if (isContainSession(device)) {
                JSONObject data = new JSONObject();
                data.put(StatisticServerContract.StatisticData.DATA, device);
                ServerHelper.uploadJSONObject(context, StatisticServerContract.Server.getStatisticSyncUrl(), data);
                deviceCollectHelper.complete();
            }
        } catch (IOException | RemoteException | JSONException | OperationApplicationException e) {
            return false;
        }
        return true;
    }

    private void saveLastSyncDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("gmt"));
        String value = dateFormat.format(new Date());
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(StorageContract
                .LAST_STATISTIC_SYNC_DATE, value).apply();
    }

    private boolean isContainSession(JSONObject device) throws JSONException {
        JSONArray sessions = device.getJSONArray(StatisticServerContract.DeviceData.SESSION_LIST);
        return sessions.length() > 0;
    }

}
