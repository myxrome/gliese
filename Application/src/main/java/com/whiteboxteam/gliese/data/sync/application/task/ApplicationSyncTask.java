package com.whiteboxteam.gliese.data.sync.application.task;

import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import com.whiteboxteam.gliese.data.content.ApplicationContentContract;
import com.whiteboxteam.gliese.data.helper.cleanup.*;
import com.whiteboxteam.gliese.data.helper.merge.*;
import com.whiteboxteam.gliese.data.server.ApplicationServerContract;
import com.whiteboxteam.gliese.data.server.ServerHelper;
import com.whiteboxteam.gliese.data.storage.StorageContract;
import com.whiteboxteam.gliese.data.sync.application.ApplicationSyncService;
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
public class ApplicationSyncTask implements Runnable {

    private Context context;

    public ApplicationSyncTask(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        boolean result = performApplicationSync();
        if (result) {
            saveLastSyncDate();
        }
        sendSyncResultBroadcast(result);
    }

    private boolean performApplicationSync() {
        try {
            JSONObject json = downloadApplicationData();
            mergeApplicationData(json);
            cleanupApplicationData();
        } catch (IOException | RemoteException | JSONException | OperationApplicationException e) {
            return false;
        }
        return true;
    }

    private void saveLastSyncDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("gmt"));
        String value = dateFormat.format(new Date());
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(StorageContract.LAST_SYNC_DATE,
                value).apply();
    }

    private void sendSyncResultBroadcast(boolean result) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ApplicationSyncService.SyncResultBroadcast.Parameters.RESULT, result);
        Intent intent = new Intent(ApplicationSyncService.SyncResultBroadcast.ACTION);
        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private JSONObject downloadApplicationData() throws IOException {
        String syncUrl = ApplicationServerContract.Server.getSyncUrl(context);
        return ServerHelper.downloadJSONObject(syncUrl);
    }

    private void mergeApplicationData(JSONObject json) throws OperationApplicationException, RemoteException,
            JSONException {
        merge(new TopicGroupMergeHelper(context, ApplicationContentContract.TopicGroup.CONTENT_URI), json
                .getJSONArray(ApplicationServerContract.ApplicationData.TOPIC_GROUP_LIST));
        merge(new TopicMergeHelper(context, ApplicationContentContract.Topic.CONTENT_URI), json.getJSONArray
                (ApplicationServerContract.ApplicationData.TOPIC_LIST));
        merge(new CategoryMergeHelper(context, ApplicationContentContract.Category.CONTENT_URI), json.getJSONArray
                (ApplicationServerContract.ApplicationData.CATEGORY_LIST));
        merge(new ValueMergeHelper(context, ApplicationContentContract.Value.CONTENT_URI), json.getJSONArray
                (ApplicationServerContract.ApplicationData.VALUE_LIST));
        merge(new DescriptionMergeHelper(context, ApplicationContentContract.Description.CONTENT_URI), json
                .getJSONArray(ApplicationServerContract.ApplicationData.DESCRIPTION_LIST));
        merge(new PromoMergeHelper(context, ApplicationContentContract.Promo.CONTENT_URI), json.getJSONArray
                (ApplicationServerContract.ApplicationData.PROMO_LIST));
    }

    private void cleanupApplicationData() {
        cleanup(new TopicCleanupHelper(context));
        cleanup(new CategoryCleanupHelper(context));
        cleanup(new ValueCleanupHelper(context));
        cleanup(new DescriptionCleanupHelper(context));
        cleanup(new PromoCleanupHelper(context));
    }

    private void merge(BaseMergeHelper mergeHelper, JSONArray jsonArray) throws OperationApplicationException,
            RemoteException, JSONException {
        mergeHelper.merge(jsonArray);
    }

    private void cleanup(BaseCleanupHelper cleanupHelper) {
        cleanupHelper.applyInactive();
    }

}
