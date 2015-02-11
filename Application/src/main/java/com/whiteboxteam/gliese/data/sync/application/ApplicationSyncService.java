package com.whiteboxteam.gliese.data.sync.application;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.whiteboxteam.gliese.data.sync.application.task.ApplicationSyncTask;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 09.02.2015
 * Time: 13:50
 */
public class ApplicationSyncService extends Service {

    private static final int CORE_POOL_SIZE = 4;
    private static final long KEEP_ALIVE_TIME = 0L;
    private ThreadPoolExecutor executorService;

    public static void startApplicationSync(Context context) {
        Intent intent = new Intent(context, ApplicationSyncService.class);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        executorService = new ThreadPoolExecutor(CORE_POOL_SIZE, CORE_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit
                .MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (executorService.getActiveCount() == 0) {
            executorService.execute(new ApplicationSyncTask(this));
            ApplicationSyncService.scheduleNextApplicationSync(this);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public static void scheduleNextApplicationSync(Context context) {
        Intent serviceIntent = new Intent(context, ApplicationSyncService.class);
        PendingIntent intent = PendingIntent.getService(context, 0, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, AlarmManager.INTERVAL_HOUR * 3, intent);
    }

    @Override
    public void onDestroy() {
        executorService.shutdownNow();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static abstract class SyncResultBroadcast {
        public static final String ACTION = "com.gliese.data.sync.ApplicationSyncService.SYNC_RESULT";

        public static abstract class Parameters {
            public static final String RESULT = "result";
        }
    }

}
