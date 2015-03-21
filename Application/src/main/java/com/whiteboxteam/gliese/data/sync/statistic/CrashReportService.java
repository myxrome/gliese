package com.whiteboxteam.gliese.data.sync.statistic;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import com.whiteboxteam.gliese.data.sync.statistic.task.CrashReportTask;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CrashReportService extends Service {

    private static final int  CORE_POOL_SIZE  = 4;
    private static final long KEEP_ALIVE_TIME = 0L;
    private ThreadPoolExecutor executorService;

    public static void startCrashReport(Context context) {
        Intent intent = new Intent(context, CrashReportService.class);
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
        executorService.execute(new CrashReportTask(this));
        CrashReportService.scheduleNextCrashReport(this);
        return super.onStartCommand(intent, flags, startId);
    }

    public static void scheduleNextCrashReport(Context context) {
        Intent serviceIntent = new Intent(context, StatisticSyncService.class);
        PendingIntent intent = PendingIntent.getService(context, 0, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(intent);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() +
                AlarmManager.INTERVAL_DAY, AlarmManager.INTERVAL_DAY, intent);
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
}
