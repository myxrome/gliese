package com.whiteboxteam.gliese.data.content.provider.impl;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.net.Uri;
import com.whiteboxteam.gliese.data.content.ApplicationContentContract;
import com.whiteboxteam.gliese.data.db.ApplicationDatabaseContract;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 22.12.2014
 * Time: 12:46
 */
public final class TopicGroupProviderHelper extends BaseProviderHelper {

    private static final int TOPIC_GROUP_LIST_URI = 1;
    private static final int TOPIC_GROUP_ITEM_URI = 2;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(ApplicationContentContract.CONTENT_AUTHORITY, ApplicationContentContract.TopicGroup
                .TOPIC_GROUP_BASE_PATH, TOPIC_GROUP_LIST_URI);
        URI_MATCHER.addURI(ApplicationContentContract.CONTENT_AUTHORITY, ApplicationContentContract.TopicGroup
                .TOPIC_GROUP_BASE_PATH + "/#", TOPIC_GROUP_ITEM_URI);
    }

    @Override
    public boolean isUriMatched(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case TOPIC_GROUP_LIST_URI:
            case TOPIC_GROUP_ITEM_URI:
                return true;
        }
        return false;
    }

    @Override
    public String buildSelectionFromUri(Uri uri, String selection) {
        String condition = "";
        switch (URI_MATCHER.match(uri)) {
            case TOPIC_GROUP_LIST_URI:
                break;
            case TOPIC_GROUP_ITEM_URI:
                condition = ApplicationDatabaseContract.BaseEntry.ID + " = " + uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return concatConditionAndSelection(condition, selection);
    }

    @Override
    public String getTableName() {
        return ApplicationDatabaseContract.TopicGroupEntry.TABLE_NAME;
    }

    @Override
    public ContentValues getExtraValuesFromUri(Uri uri) {
        return new ContentValues();
    }

    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case TOPIC_GROUP_LIST_URI:
                return ApplicationContentContract.TopicGroup.CONTENT_TYPE_LIST;
            case TOPIC_GROUP_ITEM_URI:
                return ApplicationContentContract.TopicGroup.CONTENT_TYPE_ITEM;
        }
        return null;
    }


    @Override
    public Uri getRootUri(Uri uri) {
        return uri;
    }

}
