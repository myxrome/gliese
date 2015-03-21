package com.whiteboxteam.gliese.data.sync.statistic.task;

import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;
import com.whiteboxteam.gliese.data.helper.collect.BaseCollectHelper;
import com.whiteboxteam.gliese.data.helper.collect.CrashReportCollectHelper;
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
 * Date: 21.03.2015
 * Time: 14:51
 */
public class CrashReportTask implements Runnable {

    private Context context;

    public CrashReportTask(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        Log.d("[SYNC]", "start crash report");
        if (performCrashReport()) {
            saveLastReportDate();
        }
        Log.d("[SYNC]", "finish crash report");
    }

    private boolean performCrashReport() {
        BaseCollectHelper<JSONObject> deviceCollectHelper = new DeviceCollectHelper(context, new
                CrashReportCollectHelper(context));
        try {
            JSONObject device = deviceCollectHelper.collect();
            if (isContainCrashReport(device)) {
                JSONObject data = new JSONObject();
                data.put(StatisticServerContract.StatisticData.DATA, device);
                ServerHelper.uploadJSONObject(context, StatisticServerContract.Server.getCrashReportUrl(), data);
                deviceCollectHelper.complete();
            }
        } catch (IOException | RemoteException | JSONException | OperationApplicationException e) {
            Log.d("[SYNC]", e.toString());
            return false;
        }
        return true;
    }

    private void saveLastReportDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("gmt"));
        String value = dateFormat.format(new Date());
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(StorageContract
                .LAST_CRASH_REPORT_DATE, value).apply();
    }

    private boolean isContainCrashReport(JSONObject device) throws JSONException {
        JSONArray crash = device.getJSONArray(StatisticServerContract.DeviceData.NESTED_LIST);
        return crash.length() > 0;
    }

}
