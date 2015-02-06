package com.whiteboxteam.gliese.data.content.provider.impl;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.net.Uri;
import com.whiteboxteam.gliese.data.content.StatisticContentContract;
import com.whiteboxteam.gliese.data.db.StatisticDatabaseContract;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 11.08.2014
 * Time: 21:32
 */
public class FactProviderHelper extends BaseProviderHelper {

    private static final int FACT_LIST_URI = 1;
    private static final int FACT_ITEM_URI = 2;
    private static final int SESSION_FACT_LIST_URI = 3;
    private static final int SESSION_FACT_ITEM_URI = 4;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(StatisticContentContract.CONTENT_AUTHORITY,
                StatisticContentContract.Facts.FACTS_BASE_PATH, FACT_LIST_URI);
        URI_MATCHER.addURI(StatisticContentContract.CONTENT_AUTHORITY,
                StatisticContentContract.Facts.FACTS_BASE_PATH + "/#", FACT_ITEM_URI);
        URI_MATCHER.addURI(StatisticContentContract.CONTENT_AUTHORITY,
                StatisticContentContract.Sessions.SESSIONS_BASE_PATH + "/#/" +
                        StatisticContentContract.Facts.FACTS_BASE_PATH, SESSION_FACT_LIST_URI
        );
        URI_MATCHER.addURI(StatisticContentContract.CONTENT_AUTHORITY,
                StatisticContentContract.Sessions.SESSIONS_BASE_PATH + "/#/" +
                        StatisticContentContract.Facts.FACTS_BASE_PATH + "/#", SESSION_FACT_ITEM_URI
        );
    }

    @Override
    public boolean isUriMatched(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case FACT_LIST_URI:
            case FACT_ITEM_URI:
            case SESSION_FACT_LIST_URI:
            case SESSION_FACT_ITEM_URI:
                return true;
        }
        return false;
    }

    @Override
    public String getTableName() {
        return StatisticDatabaseContract.FactEntry.TABLE_NAME;
    }

    @Override
    public ContentValues getExtraValuesFromUri(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case SESSION_FACT_LIST_URI:
            case SESSION_FACT_ITEM_URI:
                ContentValues result = new ContentValues();
                result.put(StatisticContentContract.Facts.SESSION_ID, getParentIdFromUri(uri));
                return result;
        }
        return new ContentValues();
    }

    @Override
    public String buildSelectionFromUri(Uri uri, String selection) {
        String condition = "";
        switch (URI_MATCHER.match(uri)) {
            case FACT_LIST_URI:
                break;
            case FACT_ITEM_URI:
                condition = StatisticContentContract.Facts.ID + " = " + uri.getLastPathSegment();
                break;
            case SESSION_FACT_LIST_URI:
                condition = StatisticContentContract.Facts.SESSION_ID + " = " + getParentIdFromUri(uri);
                break;
            case SESSION_FACT_ITEM_URI:
                condition = StatisticContentContract.Facts.ID + " = " + uri.getLastPathSegment() + " AND " +
                        StatisticContentContract.Facts.SESSION_ID + " = " + getParentIdFromUri(uri);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return concatConditionAndSelection(condition, selection);
    }

    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case FACT_LIST_URI:
            case SESSION_FACT_LIST_URI:
                return StatisticContentContract.Facts.CONTENT_TYPE_LIST;
            case FACT_ITEM_URI:
            case SESSION_FACT_ITEM_URI:
                return StatisticContentContract.Facts.CONTENT_TYPE_ITEM;
        }
        return null;
    }
}
