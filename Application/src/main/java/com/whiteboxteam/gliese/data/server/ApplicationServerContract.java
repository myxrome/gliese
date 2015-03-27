package com.whiteboxteam.gliese.data.server;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.whiteboxteam.gliese.R;
import com.whiteboxteam.gliese.data.content.ApplicationContentContract;
import com.whiteboxteam.gliese.data.storage.StorageContract;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 21.11.13
 * Time: 17:20
 */
public final class ApplicationServerContract {

    public ApplicationServerContract() {

    }

    public static final class Server {

        public static String getSyncUrl(Context context) {
            String lastSyncDate = PreferenceManager.getDefaultSharedPreferences(context).getString(StorageContract
                    .LAST_APPLICATION_SYNC_DATE, "");
            if (Strings.isNullOrEmpty(lastSyncDate)) {
                return getInitialSyncUrl(context);
            } else {
                return getDeltaSyncUrl(context, lastSyncDate);
            }
        }

        private static String getInitialSyncUrl(Context context) {
            Joiner joiner = Joiner.on("&k[]=");
            return context.getResources().getString(R.string.a) + "/i/?k[]=" + joiner.join(ApplicationContentContract.TopicGroup.KeyValues.getKeys
                    ()) +
                    "&q=" + getDensityValue(context);
        }

        private static String getDeltaSyncUrl(Context context, String lastSyncDate) {
            Joiner joiner = Joiner.on("&k[]=");
            return context.getResources().getString(R.string.a) + "/d/?k[]=" + joiner.join(ApplicationContentContract.TopicGroup.KeyValues.getKeys()) +
                    "&q=" + getDensityValue(context) + "&u=" + lastSyncDate;
        }

        private static String getDensityValue(Context context) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int width = Math.round(250 * displayMetrics.scaledDensity);
            width = (((width + 50) / 100)) * 100;
            return "x" + String.valueOf(width);
        }

        public static String getImageUrl(Context context, String imagePostfix) {
            return context.getResources().getString(R.string.a) + imagePostfix;
        }

    }

    public static abstract class ApplicationData {
        public static final String TOPIC_GROUP_LIST = "g";
        public static final String TOPIC_LIST = "t";
        public static final String CATEGORY_LIST = "c";
        public static final String VALUE_LIST = "v";
        public static final String DESCRIPTION_LIST = "d";
        public static final String PROMO_LIST = "p";
    }

    public static abstract class BaseRecord {
        public static final String ID = "id";
        public static final String ACTIVE = "a";
        public static final String UPDATED_AT = "u";
    }

    public static abstract class TopicGroupRecord extends BaseRecord {
        public static final String ORDER = "o";
        public static final String KEY = "k";
        public static final String NAME = "n";
    }

    public static abstract class TopicRecord extends BaseRecord {
        public static final String TOPIC_GROUP_ID = "_id";
        public static final String ORDER = "o";
        public static final String NAME = "n";
    }

    public static abstract class CategoryRecord extends BaseRecord {
        public static final String TOPIC_ID = "_id";
        public static final String ORDER = "o";
        public static final String NAME = "n";
    }

    public static abstract class ValueRecord extends BaseRecord {
        public static final String CATEGORY_ID = "_id";
        public static final String NAME = "n";
        public static final String THUMB_URI = "t";
        public static final String OLD_PRICE = "op";
        public static final String DISCOUNT = "ds";
        public static final String NEW_PRICE = "np";
        public static final String URL = "l";


    }

    public static abstract class DescriptionRecord extends BaseRecord {
        public static final String VALUE_ID = "_id";
        public static final String ORDER = "o";
        public static final String CAPTION = "c";
        public static final String TEXT = "t";
    }

    public static abstract class PromoRecord extends BaseRecord {
        public static final String VALUE_ID = "_id";
        public static final String ORDER = "o";
        public static final String IMAGE_URI = "l";
    }

}
