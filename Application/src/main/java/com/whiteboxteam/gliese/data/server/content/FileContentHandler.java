package com.whiteboxteam.gliese.data.server.content;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 17.01.14
 * Time: 18:20
 */
public final class FileContentHandler {

    public static File getContent(Context context, byte[] source) {
        if (source != null) {
            File file = new File(context.getFilesDir(), UUID.randomUUID().toString().replace("-", ""));
            try {
                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(source);
                return file;
            } catch (Exception e) {

            }
        }
        return null;
    }
}
