package com.whiteboxteam.gliese.data.content.provider.impl;

import android.content.ContentUris;
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
public final class DescriptionProviderHelper extends BaseProviderHelper {

    private static final int DESCRIPTION_LIST_URI = 1;
    private static final int DESCRIPTION_ITEM_URI = 2;
    private static final int VALUE_DESCRIPTION_LIST_URI = 3;
    private static final int VALUE_DESCRIPTION_ITEM_URI = 4;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(ApplicationContentContract.CONTENT_AUTHORITY, ApplicationContentContract.Description
                .DESCRIPTION_BASE_PATH, DESCRIPTION_LIST_URI);
        URI_MATCHER.addURI(ApplicationContentContract.CONTENT_AUTHORITY, ApplicationContentContract.Description
                .DESCRIPTION_BASE_PATH + "/#", DESCRIPTION_ITEM_URI);
        URI_MATCHER.addURI(ApplicationContentContract.CONTENT_AUTHORITY, ApplicationContentContract.Value
                .VALUE_BASE_PATH + "/#/" +
                        ApplicationContentContract.Description.DESCRIPTION_BASE_PATH, VALUE_DESCRIPTION_LIST_URI);
        URI_MATCHER.addURI(ApplicationContentContract.CONTENT_AUTHORITY, ApplicationContentContract.Value
                .VALUE_BASE_PATH + "/#/" +
                        ApplicationContentContract.Description.DESCRIPTION_BASE_PATH + "/#",
                VALUE_DESCRIPTION_ITEM_URI);
    }

    @Override
    public boolean isUriMatched(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case DESCRIPTION_LIST_URI:
            case DESCRIPTION_ITEM_URI:
            case VALUE_DESCRIPTION_LIST_URI:
            case VALUE_DESCRIPTION_ITEM_URI:
                return true;
        }
        return false;
    }

    @Override
    public String buildSelectionFromUri(Uri uri, String selection) {
        String condition = "";
        switch (URI_MATCHER.match(uri)) {
            case DESCRIPTION_LIST_URI:
                break;
            case DESCRIPTION_ITEM_URI:
                condition = ApplicationDatabaseContract.BaseEntry.ID + " = " + uri.getLastPathSegment();
                break;
            case VALUE_DESCRIPTION_LIST_URI:
                condition = ApplicationContentContract.Description.VALUE_ID + " = " + getParentIdFromUri(uri);
                break;
            case VALUE_DESCRIPTION_ITEM_URI:
                condition = ApplicationDatabaseContract.BaseEntry.ID + " = " + uri.getLastPathSegment() + " AND " +
                        ApplicationContentContract.Description.VALUE_ID + " = " + getParentIdFromUri(uri);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return concatConditionAndSelection(condition, selection);
    }

    @Override
    public String getTableName() {
        return ApplicationDatabaseContract.DescriptionEntry.TABLE_NAME;
    }

    @Override
    public ContentValues getExtraValuesFromUri(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case VALUE_DESCRIPTION_LIST_URI:
            case VALUE_DESCRIPTION_ITEM_URI:
                ContentValues result = new ContentValues();
                result.put(ApplicationContentContract.Description.VALUE_ID, getParentIdFromUri(uri));
                return result;
        }
        return new ContentValues();
    }

    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case DESCRIPTION_LIST_URI:
            case VALUE_DESCRIPTION_LIST_URI:
                return ApplicationContentContract.Description.CONTENT_TYPE_LIST;
            case DESCRIPTION_ITEM_URI:
            case VALUE_DESCRIPTION_ITEM_URI:
                return ApplicationContentContract.Description.CONTENT_TYPE_ITEM;
        }
        return null;
    }

    @Override
    public Uri getRootUri(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case DESCRIPTION_LIST_URI:
            case VALUE_DESCRIPTION_LIST_URI:
                return ApplicationContentContract.Description.CONTENT_URI;
            case DESCRIPTION_ITEM_URI:
            case VALUE_DESCRIPTION_ITEM_URI:
                return ContentUris.withAppendedId(ApplicationContentContract.Description.CONTENT_URI, Long.parseLong
                        (uri.getLastPathSegment()));
        }
        return null;
    }
}
