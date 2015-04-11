package com.whiteboxteam.gliese.data.server;

import android.content.Context;
import android.util.Base64;
import com.whiteboxteam.gliese.R;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 03.09.2014
 * Time: 17:21
 */
public final class StatisticServerContract {

    public static final class Server {

        public static final int POST = 1;
        public static final int GET = 2;

        public static String getStatisticUploadQueryUrl(Context context, JSONObject object) throws IOException {
            String value = encodeJSONObject(object);
            return getStatisticSyncUrl(context) + "?d=" + value;
        }

        private static String encodeJSONObject(JSONObject object) throws IOException {
            ByteArrayOutputStream arr = new ByteArrayOutputStream();
            OutputStream zipper = new GZIPOutputStream(arr);
            zipper.write(object.toString().getBytes());
            zipper.close();
            return Base64.encodeToString(arr.toByteArray(), Base64.URL_SAFE | Base64.NO_WRAP);
        }

        public static String getStatisticSyncUrl(Context context) {
            return context.getResources().getString(R.string.s) + "/s/";
        }

        public static String getCrashReportUploadQueryUrl(Context context, JSONObject object) throws IOException {
            String value = encodeJSONObject(object);
            return getCrashReportUrl(context) + "?d=" + value;
        }

        public static String getCrashReportUrl(Context context) {
            return context.getResources().getString(R.string.s) + "/c/";
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

        public static final String NESTED_LIST = "s";
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

    public static abstract class CrashReportData {
        public static final String NAME = "n";
        public static final String VERSION = "v";
        public static final String EXCEPTION = "e";
        public static final String CAUSE = "c";
        public static final String STACKTRACE = "s";
        public static final String HAPPENED_AT = "h";
    }

}
