package com.whiteboxteam.gliese.data.content;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import com.whiteboxteam.gliese.data.db.StatisticDatabaseContract;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 11.08.2014
 * Time: 21:24
 */
public final class StatisticContentContract {

    public static final String CONTENT_AUTHORITY = "com.whiteboxteam.gliese.statistic.content.provider";
    private static final Uri BASE_CONTENT_URI = Uri.parse(ContentResolver.SCHEME_CONTENT + "://" + CONTENT_AUTHORITY);

    public static final class Session {
        public static final String SESSIONS_BASE_PATH = "sessions";

        public static final String ID = StatisticDatabaseContract.SessionEntry.ID;
        public static final String STARTED_AT = StatisticDatabaseContract.SessionEntry.STARTED_AT;
        public static final String FINISHED_AT = StatisticDatabaseContract.SessionEntry.FINISHED_AT;

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(SESSIONS_BASE_PATH).build();

        public static final String CONTENT_TYPE_LIST = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.whiteboxteam" +
                ".gliese.sessions";
        public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.whiteboxteam" +
                ".gliese.session";

    }

    public static final class Fact {
        public static final String FACTS_BASE_PATH = "facts";

        public static final String ID = StatisticDatabaseContract.FactEntry.ID;
        public static final String SESSION_ID = StatisticDatabaseContract.FactEntry.SESSION_ID;
        public static final String EVENT = StatisticDatabaseContract.FactEntry.EVENT;
        public static final String CONTEXT_ID = StatisticDatabaseContract.FactEntry.CONTEXT_ID;
        public static final String CONTEXT_TYPE = StatisticDatabaseContract.FactEntry.CONTEXT_TYPE;
        public static final String EXTERNAL_CONTEXT = StatisticDatabaseContract.FactEntry.EXTERNAL_CONTEXT;

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(FACTS_BASE_PATH).build();
        public static final String CONTENT_TYPE_LIST = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.whiteboxteam" +
                ".gliese.facts";
        public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.whiteboxteam" +
                ".gliese.fact";

        public static Uri getContentUriBySession(Uri sessionUri) {
            return sessionUri.buildUpon().appendPath(FACTS_BASE_PATH).build();
        }

    }

    public static final class FactDetail {
        public static final String FACT_DETAILS_BASE_PATH = "fact-details";

        public static final String ID = StatisticDatabaseContract.FactDetailEntry.ID;
        public static final String FACT_ID = StatisticDatabaseContract.FactDetailEntry.FACT_ID;
        public static final String ORDER = StatisticDatabaseContract.FactDetailEntry.ORDER;
        public static final String HAPPENED_AT = StatisticDatabaseContract.FactDetailEntry.HAPPENED_AT;

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(FACT_DETAILS_BASE_PATH).build();
        public static final String CONTENT_TYPE_LIST = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.whiteboxteam" +
                ".gliese.fact-details";
        public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.whiteboxteam" +
                ".gliese.fact-detail";

        public static Uri getContentUriByFact(Uri factUri) {
            return ContentUris.withAppendedId(Fact.CONTENT_URI, Long.parseLong(factUri.getLastPathSegment())).
                    buildUpon().appendPath(FACT_DETAILS_BASE_PATH).build();
        }

    }

    public static final class CrashReport {
        public static final String CRASH_REPORTS_BASE_PATH = "crash-reports";

        public static final String ID = StatisticDatabaseContract.CrashReportEntry.ID;
        public static final String NAME = StatisticDatabaseContract.CrashReportEntry.NAME;
        public static final String VERSION = StatisticDatabaseContract.CrashReportEntry.VERSION;
        public static final String EXCEPTION = StatisticDatabaseContract.CrashReportEntry.EXCEPTION;
        public static final String CAUSE = StatisticDatabaseContract.CrashReportEntry.CAUSE;
        public static final String STACKTRACE = StatisticDatabaseContract.CrashReportEntry.STACKTRACE;
        public static final String HAPPENED_AT = StatisticDatabaseContract.CrashReportEntry.HAPPENED_AT;

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(CRASH_REPORTS_BASE_PATH).build();
        public static final String CONTENT_TYPE_LIST = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.whiteboxteam" +
                ".gliese.crash-reports";
        public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.whiteboxteam" +
                ".gliese.crash-report";

    }

}
