package com.whiteboxteam.gliese.data.sync.statistic;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.whiteboxteam.gliese.data.sync.statistic.task.StatisticSyncTask;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class StatisticSyncService extends Service {

    private static final int CORE_POOL_SIZE = 4;
    private static final long KEEP_ALIVE_TIME = 0L;
    private ThreadPoolExecutor executorService;

    public static void startStatisticSync(Context context) {
        Intent intent = new Intent(context, StatisticSyncService.class);
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
            executorService.execute(new StatisticSyncTask(this));
//            ApplicationSyncService.scheduleNextApplicationSync(this);
        }
        return super.onStartCommand(intent, flags, startId);
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
