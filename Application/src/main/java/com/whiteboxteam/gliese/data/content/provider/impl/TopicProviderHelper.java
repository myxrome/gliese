package com.whiteboxteam.gliese.data.content.provider.impl;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.net.Uri;
import com.whiteboxteam.gliese.data.content.ApplicationContentContract;
import com.whiteboxteam.gliese.data.db.ApplicationDatabaseContract;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 24.05.2014
 * Time: 18:26
 */
public final class TopicProviderHelper extends BaseProviderHelper {

    private static final int TOPIC_LIST_URI = 1;
    private static final int TOPIC_ITEM_URI = 2;
    private static final int TOPIC_GROUP_TOPIC_LIST_URI = 3;
    private static final int TOPIC_GROUP_TOPIC_ITEM_URI = 4;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(ApplicationContentContract.CONTENT_AUTHORITY,
                ApplicationContentContract.Topic.TOPIC_BASE_PATH, TOPIC_LIST_URI);
        URI_MATCHER.addURI(ApplicationContentContract.CONTENT_AUTHORITY,
                ApplicationContentContract.Topic.TOPIC_BASE_PATH + "/#", TOPIC_ITEM_URI);
        URI_MATCHER.addURI(ApplicationContentContract.CONTENT_AUTHORITY,
                ApplicationContentContract.TopicGroup.TOPIC_GROUP_BASE_PATH + "/#/" +
                        ApplicationContentContract.Topic.TOPIC_BASE_PATH, TOPIC_GROUP_TOPIC_LIST_URI
        );
        URI_MATCHER.addURI(ApplicationContentContract.CONTENT_AUTHORITY,
                ApplicationContentContract.TopicGroup.TOPIC_GROUP_BASE_PATH + "/#/" +
                        ApplicationContentContract.Topic.TOPIC_BASE_PATH + "/#", TOPIC_GROUP_TOPIC_ITEM_URI
        );
    }

    @Override
    public boolean isUriMatched(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case TOPIC_LIST_URI:
            case TOPIC_ITEM_URI:
            case TOPIC_GROUP_TOPIC_LIST_URI:
            case TOPIC_GROUP_TOPIC_ITEM_URI:
                return true;
        }
        return false;
    }

    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case TOPIC_LIST_URI:
            case TOPIC_GROUP_TOPIC_LIST_URI:
                return ApplicationContentContract.Topic.CONTENT_TYPE_LIST;
            case TOPIC_ITEM_URI:
            case TOPIC_GROUP_TOPIC_ITEM_URI:
                return ApplicationContentContract.Topic.CONTENT_TYPE_ITEM;
        }
        return null;
    }

    @Override
    public ContentValues getExtraValuesFromUri(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case TOPIC_GROUP_TOPIC_LIST_URI:
            case TOPIC_GROUP_TOPIC_ITEM_URI:
                ContentValues result = new ContentValues();
                result.put(ApplicationContentContract.Topic.TOPIC_GROUP_ID, getParentIdFromUri(uri));
                return result;
        }
        return new ContentValues();
    }

    @Override
    public String buildSelectionFromUri(Uri uri, String selection) {
        String condition = "";
        switch (URI_MATCHER.match(uri)) {
            case TOPIC_LIST_URI:
                break;
            case TOPIC_ITEM_URI:
                condition = ApplicationDatabaseContract.BaseEntry.ID + " = " + uri.getLastPathSegment();
                break;
            case TOPIC_GROUP_TOPIC_LIST_URI:
                condition = ApplicationContentContract.Topic.TOPIC_GROUP_ID + " = " + getParentIdFromUri(uri);
                break;
            case TOPIC_GROUP_TOPIC_ITEM_URI:
                condition = ApplicationDatabaseContract.BaseEntry.ID + " = " + uri.getLastPathSegment() + " AND " +
                        ApplicationContentContract.Topic.TOPIC_GROUP_ID + " = " + getParentIdFromUri(uri);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return concatConditionAndSelection(condition, selection);
    }

    @Override
    public String getTableName() {
        return ApplicationDatabaseContract.TopicEntry.TABLE_NAME;
    }
}
