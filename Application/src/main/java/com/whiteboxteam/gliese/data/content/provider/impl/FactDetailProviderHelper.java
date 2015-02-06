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
 * Time: 0:50
 */
public final class FactDetailProviderHelper extends BaseProviderHelper {

    private static final int FACT_DETAIL_LIST_URI = 1;
    private static final int FACT_DETAIL_ITEM_URI = 2;
    private static final int FACT_FACT_DETAIL_LIST_URI = 3;
    private static final int FACT_FACT_DETAIL_ITEM_URI = 4;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(StatisticContentContract.CONTENT_AUTHORITY, StatisticContentContract.FactDetails
                .FACT_DETAILS_BASE_PATH, FACT_DETAIL_LIST_URI);
        URI_MATCHER.addURI(StatisticContentContract.CONTENT_AUTHORITY, StatisticContentContract.FactDetails
                .FACT_DETAILS_BASE_PATH + "/#", FACT_DETAIL_ITEM_URI);
        URI_MATCHER.addURI(StatisticContentContract.CONTENT_AUTHORITY, StatisticContentContract.Facts.FACTS_BASE_PATH
                + "/#/" +
                        StatisticContentContract.FactDetails.FACT_DETAILS_BASE_PATH, FACT_FACT_DETAIL_LIST_URI);
        URI_MATCHER.addURI(StatisticContentContract.CONTENT_AUTHORITY, StatisticContentContract.Facts.FACTS_BASE_PATH
                + "/#/" +
                        StatisticContentContract.FactDetails.FACT_DETAILS_BASE_PATH + "/#", FACT_FACT_DETAIL_ITEM_URI);
    }

    @Override
    public boolean isUriMatched(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case FACT_DETAIL_LIST_URI:
            case FACT_DETAIL_ITEM_URI:
            case FACT_FACT_DETAIL_LIST_URI:
            case FACT_FACT_DETAIL_ITEM_URI:
                return true;
        }
        return false;
    }

    @Override
    public String buildSelectionFromUri(Uri uri, String selection) {
        String condition = "";
        switch (URI_MATCHER.match(uri)) {
            case FACT_DETAIL_LIST_URI:
                break;
            case FACT_DETAIL_ITEM_URI:
                condition = StatisticContentContract.FactDetails.ID + " = " + uri.getLastPathSegment();
                break;
            case FACT_FACT_DETAIL_LIST_URI:
                condition = StatisticContentContract.FactDetails.FACT_ID + " = " + getParentIdFromUri(uri);
                break;
            case FACT_FACT_DETAIL_ITEM_URI:
                condition = StatisticContentContract.FactDetails.ID + " = " + uri.getLastPathSegment() + " AND " +
                        StatisticContentContract.FactDetails.FACT_ID + " = " + getParentIdFromUri(uri);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return concatConditionAndSelection(condition, selection);
    }

    @Override
    public String getTableName() {
        return StatisticDatabaseContract.FactDetailEntry.TABLE_NAME;
    }

    @Override
    public ContentValues getExtraValuesFromUri(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case FACT_FACT_DETAIL_LIST_URI:
            case FACT_FACT_DETAIL_ITEM_URI:
                ContentValues result = new ContentValues();
                result.put(StatisticContentContract.FactDetails.FACT_ID, getParentIdFromUri(uri));
                return result;
        }
        return new ContentValues();
    }

    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case FACT_DETAIL_LIST_URI:
            case FACT_FACT_DETAIL_LIST_URI:
                return StatisticContentContract.FactDetails.CONTENT_TYPE_LIST;
            case FACT_DETAIL_ITEM_URI:
            case FACT_FACT_DETAIL_ITEM_URI:
                return StatisticContentContract.FactDetails.CONTENT_TYPE_ITEM;
        }
        return null;
    }
}
