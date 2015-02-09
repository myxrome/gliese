package com.whiteboxteam.gliese.data.sync.image;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import com.whiteboxteam.gliese.data.content.ApplicationContentContract;
import com.whiteboxteam.gliese.data.sync.image.task.PromoImageUploadTask;
import com.whiteboxteam.gliese.data.sync.image.task.UploadTaskComparator;
import com.whiteboxteam.gliese.data.sync.image.task.ValueThumbUploadTask;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 31.01.14
 * Time: 15:22
 */
public class ImageUploadService extends Service {

    private static final int DOWNLOAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private static final long KEEP_ALIVE_TIME = 0L;
    private static final int DEFAULT_INITIAL_CAPACITY = 11;
    private static final int DEFAULT_UPLOAD_PRIORITY = 0;

    private ThreadPoolExecutor imageBackgroundPool;
    private ThreadPoolExecutor imageForegroundPool;
    private ThreadPoolExecutor thumbBackgroundPool;
    private ThreadPoolExecutor thumbForegroundPool;

    public static void startBackgroundUpload(Context context) {
        Intent intent = new Intent(context, ImageUploadService.class);
        Bundle bundle = new Bundle();
        bundle.putInt(Parameters.UPLOAD_TYPE, Parameters.UploadTypes.BACKGROUND);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    public static void startForegroundPromoImageUpload(Context context, long id, int priority) {
        Intent intent = new Intent(context, ImageUploadService.class);
        Bundle bundle = new Bundle();
        bundle.putInt(Parameters.UPLOAD_TYPE, Parameters.UploadTypes.FOREGROUND);
        bundle.putInt(Parameters.UPLOAD_ENTITY, Parameters.UploadEntities.PROMO_IMAGE);
        bundle.putLong(Parameters.ENTITY_ID, id);
        bundle.putInt(Parameters.UPLOAD_PRIORITY, priority);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    public static void startForegroundValueThumbUpload(Context context, long id, int priority) {
        Intent intent = new Intent(context, ImageUploadService.class);
        Bundle bundle = new Bundle();
        bundle.putInt(Parameters.UPLOAD_TYPE, Parameters.UploadTypes.FOREGROUND);
        bundle.putInt(Parameters.UPLOAD_ENTITY, Parameters.UploadEntities.VALUE_THUMB);
        bundle.putLong(Parameters.ENTITY_ID, id);
        bundle.putInt(Parameters.UPLOAD_PRIORITY, priority);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        UploadTaskComparator uploadTaskComparator = new UploadTaskComparator();
        imageBackgroundPool = new ThreadPoolExecutor(DOWNLOAD_POOL_SIZE, DOWNLOAD_POOL_SIZE, KEEP_ALIVE_TIME,
                TimeUnit.MILLISECONDS, new PriorityBlockingQueue<>(DEFAULT_INITIAL_CAPACITY, uploadTaskComparator));
        imageForegroundPool = new ThreadPoolExecutor(DOWNLOAD_POOL_SIZE * 2, DOWNLOAD_POOL_SIZE * 2, KEEP_ALIVE_TIME,
                TimeUnit.MILLISECONDS, new PriorityBlockingQueue<>(DEFAULT_INITIAL_CAPACITY, uploadTaskComparator));
        thumbBackgroundPool = new ThreadPoolExecutor(DOWNLOAD_POOL_SIZE, DOWNLOAD_POOL_SIZE, KEEP_ALIVE_TIME,
                TimeUnit.MILLISECONDS, new PriorityBlockingQueue<>(DEFAULT_INITIAL_CAPACITY, uploadTaskComparator));
        thumbForegroundPool = new ThreadPoolExecutor(DOWNLOAD_POOL_SIZE * 2, DOWNLOAD_POOL_SIZE * 2, KEEP_ALIVE_TIME,
                TimeUnit.MILLISECONDS, new PriorityBlockingQueue<>(DEFAULT_INITIAL_CAPACITY, uploadTaskComparator));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle.getInt(Parameters.UPLOAD_TYPE) == Parameters.UploadTypes.FOREGROUND) {

                if (bundle.getInt(Parameters.UPLOAD_ENTITY) == Parameters.UploadEntities.VALUE_THUMB) {

                    Runnable task = new ValueThumbUploadTask(this, bundle.getLong(Parameters.ENTITY_ID), bundle
                            .getInt(Parameters.UPLOAD_PRIORITY));
                    thumbForegroundPool.execute(task);

                } else {

                    Runnable task = new PromoImageUploadTask(this, bundle.getLong(Parameters.ENTITY_ID), bundle
                            .getInt(Parameters.UPLOAD_PRIORITY));
                    imageForegroundPool.execute(task);

                }

            } else {
                Cursor values = getContentResolver().query(ApplicationContentContract.Value.CONTENT_URI, new
                        String[]{ApplicationContentContract.Value.ID, ApplicationContentContract.Value
                        .REMOTE_THUMB_URI}, ApplicationContentContract.Value.LOCAL_THUMB_URI + " IS NULL", null, null);

                while (values.moveToNext()) {
                    Runnable task = new ValueThumbUploadTask(this, values.getLong(0), DEFAULT_UPLOAD_PRIORITY);
                    thumbBackgroundPool.execute(task);
                }

                Cursor promos = getContentResolver().query(ApplicationContentContract.Promo.CONTENT_URI, new
                        String[]{ApplicationContentContract.Promo.ID, ApplicationContentContract.Promo
                        .REMOTE_IMAGE_URI}, ApplicationContentContract.Promo.LOCAL_IMAGE_URI + " IS NULL", null, null);

                while (promos.moveToNext()) {
                    Runnable task = new PromoImageUploadTask(this, promos.getLong(0), DEFAULT_UPLOAD_PRIORITY);
                    imageBackgroundPool.execute(task);
                }
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        thumbForegroundPool.shutdownNow();
        thumbBackgroundPool.shutdownNow();
        imageForegroundPool.shutdownNow();
        imageBackgroundPool.shutdownNow();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static abstract class Parameters {
        public static final String UPLOAD_TYPE = "upload-type";
        public static final String UPLOAD_ENTITY = "upload-entity";
        public static final String ENTITY_ID = "entity-id";
        public static final String UPLOAD_PRIORITY = "upload-priority";

        public static abstract class UploadTypes {
            public static final int BACKGROUND = 0;
            public static final int FOREGROUND = 1;

        }

        public static abstract class UploadEntities {
            public static final int PROMO_IMAGE = 0;
            public static final int VALUE_THUMB = 1;
        }
    }

}
