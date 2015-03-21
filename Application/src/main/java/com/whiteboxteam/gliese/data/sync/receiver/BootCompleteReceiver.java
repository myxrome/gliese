package com.whiteboxteam.gliese.data.sync.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.whiteboxteam.gliese.data.sync.application.ApplicationSyncService;
import com.whiteboxteam.gliese.data.sync.statistic.StatisticSyncService;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("[SYNC]", "boot complete");
        ApplicationSyncService.scheduleNextApplicationSync(context);
        StatisticSyncService.scheduleNextStatisticSync(context);
    }

}
