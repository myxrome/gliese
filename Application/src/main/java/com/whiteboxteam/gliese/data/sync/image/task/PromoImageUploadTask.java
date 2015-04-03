package com.whiteboxteam.gliese.data.sync.image.task;

import android.content.ContentUris;
import android.content.Context;
import com.whiteboxteam.gliese.data.content.ApplicationContentContract;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 31.01.14
 * Time: 15:23
 */
public final class PromoImageUploadTask extends BaseUploadTask {

    public PromoImageUploadTask(Context context, long id, int priority) {
        super(context, ContentUris.withAppendedId(ApplicationContentContract.Promo.CONTENT_URI, id), priority);
    }

    @Override
    protected String getLocalImageKey() {
        return ApplicationContentContract.Promo.LOCAL_IMAGE_URI;
    }

    @Override
    protected String getRemoteImageKey() {
        return ApplicationContentContract.Promo.REMOTE_IMAGE_URI;
    }

}
