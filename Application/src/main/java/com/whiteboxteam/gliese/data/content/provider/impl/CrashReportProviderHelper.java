package com.whiteboxteam.gliese.data.content.provider.impl;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.net.Uri;
import com.whiteboxteam.gliese.data.content.StatisticContentContract;
import com.whiteboxteam.gliese.data.db.StatisticDatabaseContract;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 21.03.2015
 * Time: 13:07
 */
public class CrashReportProviderHelper extends BaseProviderHelper {

    private static final int CRASH_REPORT_LIST_URI = 1;
    private static final int CRASH_REPORT_ITEM_URI = 2;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(StatisticContentContract.CONTENT_AUTHORITY, StatisticContentContract.CrashReport
                .CRASH_REPORTS_BASE_PATH, CRASH_REPORT_LIST_URI);
        URI_MATCHER.addURI(StatisticContentContract.CONTENT_AUTHORITY, StatisticContentContract.CrashReport
                .CRASH_REPORTS_BASE_PATH + "/#", CRASH_REPORT_ITEM_URI);
    }

    @Override
    public boolean isUriMatched(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case CRASH_REPORT_LIST_URI:
            case CRASH_REPORT_ITEM_URI:
                return true;
        }
        return false;
    }

    @Override
    public String buildSelectionFromUri(Uri uri, String selection) {
        String condition = "";
        switch (URI_MATCHER.match(uri)) {
            case CRASH_REPORT_LIST_URI:
                break;
            case CRASH_REPORT_ITEM_URI:
                condition = StatisticDatabaseContract.CrashReportEntry.ID + " = " + uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return concatConditionAndSelection(condition, selection);
    }

    @Override
    public String getTableName() {
        return StatisticDatabaseContract.CrashReportEntry.TABLE_NAME;
    }

    @Override
    public ContentValues getExtraValuesFromUri(Uri uri) {
        return new ContentValues();
    }

    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case CRASH_REPORT_LIST_URI:
                return StatisticContentContract.CrashReport.CONTENT_TYPE_LIST;
            case CRASH_REPORT_ITEM_URI:
                return StatisticContentContract.CrashReport.CONTENT_TYPE_ITEM;
        }
        return null;
    }

    @Override
    public Uri getRootUri(Uri uri) {
        return uri;
    }

}
