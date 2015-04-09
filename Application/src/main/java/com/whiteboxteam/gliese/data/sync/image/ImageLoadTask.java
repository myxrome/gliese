package com.whiteboxteam.gliese.data.sync.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import com.whiteboxteam.gliese.data.entity.ValueEntity;
import com.whiteboxteam.gliese.data.server.ApplicationServerContract;
import com.whiteboxteam.gliese.data.server.ServerHelper;

import java.io.File;
import java.io.IOException;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 08.04.2015
 * Time: 23:57
 */
public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

    private Context     context;
    private ValueEntity entity;

    public ImageLoadTask(Context context, ValueEntity entity) {
        this.context = context;
        this.entity = entity;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        Bitmap image = null;
        if (UploadTaskLocker.lockTask(entity.remoteThumbUri)) {
            String imageUrl = ApplicationServerContract.Server.getImageUrl(context, entity.remoteThumbUri);
            Uri imageUri = Uri.parse(imageUrl);
            String fileName = imageUri.getLastPathSegment();
            File local = new File(context.getFilesDir() + File.separator + "thumb", fileName);
            local.getParentFile().mkdirs();
            if (local.exists()) {
                UploadTaskLocker.unlockTask(entity.remoteThumbUri);
                return BitmapFactory.decodeFile(local.getAbsolutePath());
            }
            try {
                File file = ServerHelper.downloadFile(context, imageUrl);
                file.renameTo(local);
                image = BitmapFactory.decodeFile(local.getAbsolutePath());
                if (image == null) {
                    local.delete();
                }
            } catch (IOException e) {
            }
            UploadTaskLocker.unlockTask(entity.remoteThumbUri);
        }
        return image;
    }

}
