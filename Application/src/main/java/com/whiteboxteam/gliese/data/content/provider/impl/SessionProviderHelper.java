package com.whiteboxteam.gliese.data.content.provider.impl;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.net.Uri;
import com.whiteboxteam.gliese.data.content.StatisticContentContract;
import com.whiteboxteam.gliese.data.db.StatisticDatabaseContract;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 26.08.2014
 * Time: 0:34
 */
public final class SessionProviderHelper extends BaseProviderHelper {

    private static final int SESSION_LIST_URI = 1;
    private static final int SESSION_ITEM_URI = 2;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(StatisticContentContract.CONTENT_AUTHORITY, StatisticContentContract.Session
                .SESSIONS_BASE_PATH, SESSION_LIST_URI);
        URI_MATCHER.addURI(StatisticContentContract.CONTENT_AUTHORITY, StatisticContentContract.Session
                .SESSIONS_BASE_PATH + "/#", SESSION_ITEM_URI);
    }

    @Override
    public boolean isUriMatched(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case SESSION_LIST_URI:
            case SESSION_ITEM_URI:
                return true;
        }
        return false;
    }

    @Override
    public String buildSelectionFromUri(Uri uri, String selection) {
        String condition = "";
        switch (URI_MATCHER.match(uri)) {
            case SESSION_LIST_URI:
                break;
            case SESSION_ITEM_URI:
                condition = StatisticDatabaseContract.SessionEntry.ID + " = " + uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return concatConditionAndSelection(condition, selection);
    }

    @Override
    public String getTableName() {
        return StatisticDatabaseContract.SessionEntry.TABLE_NAME;
    }

    @Override
    public ContentValues getExtraValuesFromUri(Uri uri) {
        return new ContentValues();
    }

    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case SESSION_LIST_URI:
                return StatisticContentContract.Session.CONTENT_TYPE_LIST;
            case SESSION_ITEM_URI:
                return StatisticContentContract.Session.CONTENT_TYPE_ITEM;
        }
        return null;
    }

    @Override
    public Uri getRootUri(Uri uri) {
        return uri;
    }
}
