package com.whiteboxteam.gliese.data.sync.image.task;

import android.content.ContentUris;
import android.content.Context;
import com.whiteboxteam.gliese.data.content.ApplicationContentContract;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 26.05.2014
 * Time: 15:11
 */
public final class ValueThumbUploadTask extends BaseUploadTask {

    public ValueThumbUploadTask(Context context, long id, int priority) {
        super(context, ContentUris.withAppendedId(ApplicationContentContract.Value.CONTENT_URI, id), priority);
    }

    @Override
    protected String getLocalImageKey() {
        return ApplicationContentContract.Value.LOCAL_THUMB_URI;
    }

    @Override
    protected String getRemoteImageKey() {
        return ApplicationContentContract.Value.REMOTE_THUMB_URI;
    }

}
