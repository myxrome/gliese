package com.whiteboxteam.gliese.data.sync.image.task;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import com.whiteboxteam.gliese.data.server.ApplicationServerContract;
import com.whiteboxteam.gliese.data.server.ServerHelper;

import java.io.File;
import java.io.IOException;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 26.05.2014
 * Time: 15:16
 */
public abstract class BaseUploadTask implements Runnable {

    protected Uri uri;
    protected Context context;
    private int priority;

    public BaseUploadTask(Context context, Uri uri, int priority) {
        this.context = context;
        this.uri = uri;
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public void run() {

        if (UploadTaskLocker.lockTask(uri.toString())) {

            Cursor entity = context.getContentResolver().query(uri, new String[]{getLocalImageKey(),
                    getRemoteImageKey()}, null, null, null);
            String localUri = "";
            String remoteUri = "";
            while (entity.moveToNext()) {
                localUri = entity.getString(0);
                remoteUri = entity.getString(1);
            }
            entity.close();

            if (localUri == null || "".equals(localUri)) {
                try {

                    File file = uploadImageFile(remoteUri);
                    if (file != null) {
                        updateLocalUri(file.getAbsolutePath());
                    }

                } catch (IOException e) {
                }
            }

            UploadTaskLocker.unlockTask(uri.toString());
        }

    }

    protected abstract String getLocalImageKey();

    protected abstract String getRemoteImageKey();

    private File uploadImageFile(String remoteUri) throws IOException {
        String imageUrl = ApplicationServerContract.Server.getImageUrl(remoteUri);
        File file = ServerHelper.downloadFile(context, imageUrl);
        File local = new File(context.getFilesDir(), file.getName() + ".jpg");
        file.renameTo(local);
        Bitmap image = BitmapFactory.decodeFile(local.getAbsolutePath());
        if (image == null) {
            local.delete();
            return null;
        }
        return local;
    }

    private void updateLocalUri(String value) {
        ContentValues values = new ContentValues();
        values.put(getLocalImageKey(), value);
        context.getContentResolver().update(uri, values, null, null);
    }

}
