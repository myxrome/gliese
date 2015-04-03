package com.whiteboxteam.gliese.data.content.provider.impl;

import android.net.Uri;
import android.text.TextUtils;
import com.whiteboxteam.gliese.data.content.provider.ProviderHelper;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 24.05.2014
 * Time: 19:12
 */
public abstract class BaseProviderHelper implements ProviderHelper {

    private static final int PARENT_ID_INDEX = 1;

    protected String getParentIdFromUri(Uri uri) {
        return uri.getPathSegments().get(PARENT_ID_INDEX);
    }

    protected String concatConditionAndSelection(String condition, String selection) {
        if (!TextUtils.isEmpty(condition))
            selection = TextUtils.isEmpty(selection) ? condition : condition + " AND " + selection;
        return selection;
    }

}
