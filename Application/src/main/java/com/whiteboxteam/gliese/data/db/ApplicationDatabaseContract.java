package com.whiteboxteam.gliese.data.db;

import android.provider.BaseColumns;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 16.10.13
 * Time: 22:14
 */
public final class ApplicationDatabaseContract {

    public static abstract class BaseEntry implements BaseColumns {
        public static final String ID = _ID;

        public static final String ACTIVE = "_active";
        public static final String UPDATED_AT = "_updated_at";
    }

    public static abstract class TopicGroupEntry extends BaseEntry {
        public static final String TABLE_NAME = "topic_group";

        public static final String ORDER = "_order";
        public static final String KEY = "_key";
        public static final String NAME = "_name";
    }

    public static abstract class TopicEntry extends BaseEntry {
        public static final String TABLE_NAME = "topic";

        public static final String TOPIC_GROUP_ID = "_topic_group_id";
        public static final String ORDER = "_order";
        public static final String NAME = "_name";
    }

    public static abstract class CategoryEntry extends BaseEntry {
        public static final String TABLE_NAME = "category";

        public static final String TOPIC_ID = "_topic_id";
        public static final String ORDER = "_order";
        public static final String NAME = "_name";
    }

    public static abstract class ValueEntry extends BaseEntry {
        public static final String TABLE_NAME = "value";

        public static final String CATEGORY_ID = "_category_id";
        public static final String NAME = "_name";
        public static final String THUMB_REMOTE_URI = "_thumb_remote_uri";
        public static final String THUMB_LOCAL_URI = "_thumb_local_uri";
        public static final String OLD_PRICE = "_old_price";
        public static final String DISCOUNT = "_discount";
        public static final String NEW_PRICE = "_new_price";
        public static final String URL = "_url";
    }

    public static abstract class ValueChildEntry extends BaseEntry {
        public static final String VALUE_ID = "_value_id";
        public static final String ORDER = "_order";
    }

    public static abstract class DescriptionEntry extends ValueChildEntry {
        public static final String TABLE_NAME = "description";

        public static final String CAPTION = "_caption";
        public static final String TEXT = "_text";
        public static final String RED = "_red";
        public static final String BOLD = "_bold";
    }

    public static abstract class PromoEntry extends ValueChildEntry {
        public static final String TABLE_NAME = "promo";

        public static final String REMOTE_IMAGE_URI = "_remote_image_uri";
        public static final String LOCAL_IMAGE_URI = "_local_image_uri";
    }

}
