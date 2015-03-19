package com.whiteboxteam.gliese.data.content.provider;

import android.content.ContentValues;
import android.net.Uri;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 24.05.2014
 * Time: 14:17
 */
public interface ProviderHelper {

    public boolean isUriMatched(Uri uri);
    public String buildSelectionFromUri(Uri uri, String selection);
    public String getTableName();
    public ContentValues getExtraValuesFromUri(Uri uri);
    public String getType(Uri uri);
    public Uri getRootUri(Uri uri);

}
