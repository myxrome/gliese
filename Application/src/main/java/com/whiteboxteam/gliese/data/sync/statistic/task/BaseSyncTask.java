package com.whiteboxteam.gliese.data.sync.statistic.task;

import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import com.whiteboxteam.gliese.data.helper.collect.BaseCollectHelper;
import com.whiteboxteam.gliese.data.helper.collect.DeviceCollectHelper;
import com.whiteboxteam.gliese.data.helper.collect.JSONArrayCollectHelper;
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
 * Date: 11.04.2015
 * Time: 17:24
 */
public abstract class BaseSyncTask implements Runnable {
    protected Context context;

    public BaseSyncTask(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        if (performSync()) {
            saveLastSyncDate();
        }
    }

    private boolean performSync() {
        BaseCollectHelper<JSONObject> deviceCollectHelper = new DeviceCollectHelper(context, getContentCollectHelper());
        try {
            while (true) {
                JSONObject device = deviceCollectHelper.collect();
                if (!isContainData(device)) {
                    break;
                }
                JSONObject data = new JSONObject();
                data.put(StatisticServerContract.StatisticData.DATA, device);
                upload(context, data);
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
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(getLastStatisticSyncDateKey(), value)
                         .apply();
    }

    protected abstract JSONArrayCollectHelper getContentCollectHelper();

    private boolean isContainData(JSONObject device) throws JSONException {
        JSONArray sessions = device.getJSONArray(StatisticServerContract.DeviceData.NESTED_LIST);
        return sessions.length() > 0;
    }

    private void upload(Context context, JSONObject object) throws IOException {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int method = preferences
                .getInt(StorageContract.PREFERRED_SEND_DATA_METHOD, StatisticServerContract.Server.POST);
        try {
            tryUpload(context, method, object);
        } catch (IOException e) {
            method = method == StatisticServerContract.Server.POST ? StatisticServerContract.Server.GET :
                    StatisticServerContract.Server.POST;
            tryUpload(context, method, object);
            preferences.edit().putInt(StorageContract.PREFERRED_SEND_DATA_METHOD, method).apply();
        }
    }

    protected abstract String getLastStatisticSyncDateKey();

    private void tryUpload(Context context, int method, JSONObject object) throws IOException {
        switch (method) {
            case StatisticServerContract.Server.POST:
                ServerHelper
                        .uploadJSONObject(context, StatisticServerContract.Server.getStatisticSyncUrl(context), object);
                break;
            case StatisticServerContract.Server.GET:
                ServerHelper.uploadQueryString(getUploadQueryUrl(context, object));
                break;
        }
    }

    protected abstract String getUploadQueryUrl(Context context, JSONObject object) throws IOException;
}
