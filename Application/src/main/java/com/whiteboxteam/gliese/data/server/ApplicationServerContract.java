package com.whiteboxteam.gliese.data.server;

import android.content.Context;
import android.preference.PreferenceManager;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
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
        private static final String SYNC_SERVER_URL = "https://ct.myankaa.com";
        private static final String INITIAL_SYNC_URL = SYNC_SERVER_URL + "/i";
        private static final String DELTA_SYNC_URL = SYNC_SERVER_URL + "/d";

        public static String getSyncUrl(Context context) {
            String lastSyncDate = PreferenceManager.getDefaultSharedPreferences(context).getString(StorageContract
                    .LAST_SYNC_DATE, "");
            if (Strings.isNullOrEmpty(lastSyncDate)) {
                return getInitialSyncUrl(context);
            } else {
                return getDeltaSyncUrl(context, lastSyncDate);
            }
        }

        private static String getInitialSyncUrl(Context context) {
            Joiner joiner = Joiner.on("&k[]=");
            return INITIAL_SYNC_URL + "/?k[]=" + joiner.join(ApplicationContentContract.TopicGroup.KeyValues.getKeys
                    ()) +
                    "&q=" + getDensityValue(context);
        }

        private static String getDeltaSyncUrl(Context context, String lastSyncDate) {
            Joiner joiner = Joiner.on("&k[]=");
            return DELTA_SYNC_URL + "/?k[]=" + joiner.join(ApplicationContentContract.TopicGroup.KeyValues.getKeys()) +
                    "&q=" + getDensityValue(context) + "&u=" + lastSyncDate;
        }

        private static String getDensityValue(Context context) {
            // TODO: Сделать нормальный выбор в зависимости от layout.
            return "x300";
        }

        public static String getImageUrl(String imagePostfix) {
            return SYNC_SERVER_URL + imagePostfix;
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
