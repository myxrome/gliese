package com.whiteboxteam.gliese.data.db;

import android.provider.BaseColumns;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 11.08.2014
 * Time: 20:44
 */
public final class StatisticDatabaseContract {

    public static final class SessionEntry implements BaseColumns {
        public static final String TABLE_NAME = "session";
        public static final String ID = BaseColumns._ID;
        public static final String STARTED_AT = "_started_at";
        public static final String FINISHED_AT = "_finished_at";

    }

    public static final class FactEntry implements BaseColumns {
        public static final String TABLE_NAME = "fact";
        public static final String ID = BaseColumns._ID;
        public static final String SESSION_ID = "_session_id";
        public static final String CONTEXT_ID = "_context_id";
        public static final String CONTEXT_TYPE = "_context_type";
        public static final String EVENT = "_event";

    }

    public static final class FactDetailEntry implements BaseColumns {
        public static final String TABLE_NAME = "fact_detail";
        public static final String ID = BaseColumns._ID;
        public static final String FACT_ID = "_fact_id";
        public static final String ORDER = "_order";
        public static final String HAPPENED_AT = "_happened_at";

    }

}
