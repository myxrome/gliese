package com.whiteboxteam.gliese.data.content;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.whiteboxteam.gliese.data.db.ApplicationDatabaseContract;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 23.01.14
 * Time: 15:18
 */
public final class ApplicationContentContract {

    public static final String CONTENT_AUTHORITY = "com.whiteboxteam.gliese.data.content.provider";

    public static abstract class Base {
        public static final String ID = ApplicationDatabaseContract.BaseEntry.ID;
        public static final String ACTIVE = ApplicationDatabaseContract.BaseEntry.ACTIVE;
        public static final String UPDATED_AT = ApplicationDatabaseContract.CategoryEntry.UPDATED_AT;
        protected static final Uri BASE_CONTENT_URI = Uri.parse(ContentResolver.SCHEME_CONTENT + "://" +
                CONTENT_AUTHORITY);

    }

    public static final class TopicGroup extends Base {
        public static final String TOPIC_GROUP_BASE_PATH = "topic_groups";

        public static final String ORDER = ApplicationDatabaseContract.TopicGroupEntry.ORDER;
        public static final String KEY = ApplicationDatabaseContract.TopicGroupEntry.KEY;
        public static final String NAME = ApplicationDatabaseContract.TopicGroupEntry.NAME;

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TOPIC_GROUP_BASE_PATH).build();

        public static final String CONTENT_TYPE_LIST = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.whiteboxteam" +
                ".gliese.topic_groups";
        public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.whiteboxteam" +
                ".gliese.topic_group";

        public static abstract class KeyValues {
            private static final String[] KEYS = new String[]{"da46a5a8de1577cecf9baed38d4fe65c",
                    "009980d31a1931b2f6b363d8fdd36bba", "644827f93d7d92ccf25cf56fd63f3d42"};

            public static String[] getKeys() {
                return KEYS;
            }

        }

    }


    public static final class Topic extends Base {
        public static final String TOPIC_BASE_PATH = "topics";

        public static final String TOPIC_GROUP_ID = ApplicationDatabaseContract.TopicEntry.TOPIC_GROUP_ID;
        public static final String ORDER = ApplicationDatabaseContract.TopicEntry.ORDER;
        public static final String NAME = ApplicationDatabaseContract.TopicEntry.NAME;

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TOPIC_BASE_PATH).build();
        public static final String CONTENT_TYPE_LIST = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.whiteboxteam" +
                ".gliese.topics";
        public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.whiteboxteam" +
                ".gliese.topic";

        public static Uri getContentUriByTopicGroup(Uri topicGroupUri) {
            return topicGroupUri.buildUpon().appendPath(TOPIC_BASE_PATH).build();
        }

        public static boolean isExist(Context context, Uri topicUri) {
            Cursor cursor = context.getContentResolver().query(topicUri, new String[]{Topic.ID}, null, null, null);
            boolean result = cursor.getCount() > 0;
            cursor.close();
            return result;
        }

    }

    public static final class Category extends Base {
        public static final String CATEGORY_BASE_PATH = "categories";

        public static final String TOPIC_ID = ApplicationDatabaseContract.CategoryEntry.TOPIC_ID;
        public static final String ORDER = ApplicationDatabaseContract.CategoryEntry.ORDER;
        public static final String NAME = ApplicationDatabaseContract.CategoryEntry.NAME;

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(CATEGORY_BASE_PATH).build();
        public static final String CONTENT_TYPE_LIST = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.whiteboxteam" +
                ".gliese.categories";
        public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.whiteboxteam" +
                ".gliese.category";

        public static Uri getContentUriByTopic(Uri topicUri) {
            return topicUri.buildUpon().appendPath(CATEGORY_BASE_PATH).build();
        }

    }

    public static final class Value extends Base {
        public static final String VALUE_BASE_PATH = "values";

        public static final String CATEGORY_ID = ApplicationDatabaseContract.ValueEntry.CATEGORY_ID;
        public static final String NAME = ApplicationDatabaseContract.ValueEntry.NAME;
        public static final String REMOTE_THUMB_URI = ApplicationDatabaseContract.ValueEntry.THUMB_REMOTE_URI;
        public static final String LOCAL_THUMB_URI = ApplicationDatabaseContract.ValueEntry.THUMB_LOCAL_URI;
        public static final String OLD_PRICE = ApplicationDatabaseContract.ValueEntry.OLD_PRICE;
        public static final String DISCOUNT = ApplicationDatabaseContract.ValueEntry.DISCOUNT;
        public static final String NEW_PRICE = ApplicationDatabaseContract.ValueEntry.NEW_PRICE;
        public static final String URL = ApplicationDatabaseContract.ValueEntry.URL;

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(VALUE_BASE_PATH).build();
        public static final String CONTENT_TYPE_LIST = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.whiteboxteam" +
                ".gliese.values";
        public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.whiteboxteam" +
                ".gliese.value";

        public static Uri getContentUriByCategory(Uri categoryUri) {
            return categoryUri.buildUpon().appendPath(VALUE_BASE_PATH).build();
        }

    }

    public static final class Description extends Base {
        public static final String DESCRIPTION_BASE_PATH = "descriptions";

        public static final String VALUE_ID = ApplicationDatabaseContract.DescriptionEntry.VALUE_ID;
        public static final String ORDER = ApplicationDatabaseContract.DescriptionEntry.ORDER;
        public static final String CAPTION = ApplicationDatabaseContract.DescriptionEntry.CAPTION;
        public static final String TEXT = ApplicationDatabaseContract.DescriptionEntry.TEXT;

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(DESCRIPTION_BASE_PATH).build();
        public static final String CONTENT_TYPE_LIST = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.whiteboxteam" +
                ".gliese.descriptions";
        public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.whiteboxteam" +
                ".gliese.description";

        public static Uri getContentUriByValue(Uri valueUri) {
            return valueUri.buildUpon().appendPath(DESCRIPTION_BASE_PATH).build();
        }
    }

    public static final class Promo extends Base {
        public static final String PROMO_BASE_PATH = "promos";

        public static final String VALUE_ID = ApplicationDatabaseContract.PromoEntry.VALUE_ID;
        public static final String ORDER = ApplicationDatabaseContract.PromoEntry.ORDER;
        public static final String REMOTE_IMAGE_URI = ApplicationDatabaseContract.PromoEntry.REMOTE_IMAGE_URI;
        public static final String LOCAL_IMAGE_URI = ApplicationDatabaseContract.PromoEntry.LOCAL_IMAGE_URI;

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PROMO_BASE_PATH).build();
        public static final String CONTENT_TYPE_LIST = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.whiteboxteam" +
                ".gliese.promos";
        public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.whiteboxteam" +
                ".gliese.promo";

        public static Uri getContentUriByValue(Uri valueUri) {
            return valueUri.buildUpon().appendPath(PROMO_BASE_PATH).build();
        }
    }

}
