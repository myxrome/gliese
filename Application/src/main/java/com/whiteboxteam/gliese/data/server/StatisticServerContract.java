package com.whiteboxteam.gliese.data.server;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 03.09.2014
 * Time: 17:21
 */
public final class StatisticServerContract {

    public static final class Server {
        private static final String SYNC_SERVER_URL = "https://st.myankaa.com";

        public static String getStatisticSyncUrl() {
            return SYNC_SERVER_URL + "/s/";
        }
    }

    public static abstract class StatisticData {
        public static final String DATA = "data";
    }

    public static abstract class DeviceData {
        public static final String UDID = "u";
        public static final String BRAND = "b";
        public static final String MODEL = "m";
        public static final String RELEASE = "r";
        public static final String SDK = "i";

        public static final String SESSION_LIST = "s";
    }

    public static abstract class SessionData {
        public static final String STARTED_AT = "sa";
        public static final String FINISHED_AT = "fa";

        public static final String FACT_LIST = "f";
    }

    public static abstract class FactData {
        public static final String EVENT = "e";
        public static final String CONTEXT_ID = "ci";
        public static final String CONTEXT_TYPE = "ct";
        public static final String EXTERNAL_CONTEXT = "ec";
        public static final String FACT_DETAIL_LIST = "fd";
    }

    public static abstract class FactDetailData {
        public static final String ORDER = "o";
        public static final String HAPPENED_AT = "ht";
    }

}
