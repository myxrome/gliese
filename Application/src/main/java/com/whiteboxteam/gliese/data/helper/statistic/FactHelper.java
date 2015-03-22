package com.whiteboxteam.gliese.data.helper.statistic;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import com.whiteboxteam.gliese.data.content.StatisticContentContract;
import com.whiteboxteam.gliese.data.entity.FactEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 19.03.2015
 * Time: 12:05
 */
public final class FactHelper {

    private static FactHelper instance = null;
    private final Context          context;
    private       SimpleDateFormat dateFormat;

    private FactHelper(Context context) {
        this.context = context;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("gmt"));
    }

    public static FactHelper getInstance(Context context) {
        if (instance == null) instance = new FactHelper(context);
        return instance;
    }

    public FactEntity startTimer(long contextId, String contextType, String eventTag) {
        Uri fact = addFact(contextId, contextType, eventTag, null);
        addFactDetail(fact, FactDetailOrder.TIMER_START);
        return FactEntity.newInstance(Long.parseLong(fact.getLastPathSegment()));
    }

    private Uri addFact(long contextId, String contextType, String eventTag, String externalContext) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(StatisticContentContract.Fact.EVENT, eventTag);
        contentValues.put(StatisticContentContract.Fact.CONTEXT_ID, contextId);
        contentValues.put(StatisticContentContract.Fact.CONTEXT_TYPE, contextType);
        contentValues.put(StatisticContentContract.Fact.EXTERNAL_CONTEXT, externalContext);
        return context.getContentResolver().insert(StatisticContentContract.Fact.getContentUriBySession(SessionHelper
                .getInstance(context).getCurrent()), contentValues);
    }

    private void addFactDetail(Uri fact, int order) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(StatisticContentContract.FactDetail.ORDER, order);
        contentValues.put(StatisticContentContract.FactDetail.HAPPENED_AT, getCurrentDateTimeString());
        context.getContentResolver().insert(StatisticContentContract.FactDetail.getContentUriByFact(fact),
                contentValues);
    }

    private String getCurrentDateTimeString() {
        return dateFormat.format(new Date());
    }

    public void finishTimer(FactEntity fact) {
        if (fact == null) {
            return;
        }
        addFactDetail(ContentUris.withAppendedId(StatisticContentContract.Fact.CONTENT_URI, fact.id), FactDetailOrder
                .TIMER_FINISH);
    }

    public void increaseCounter(long contextId, String contextType, String eventTag) {
        Uri fact = addFact(contextId, contextType, eventTag, null);
        addFactDetail(fact, FactDetailOrder.COUNTER);
    }

    public void increaseCounter(long contextId, String contextType, String eventTag, String externalContext) {
        Uri fact = addFact(contextId, contextType, eventTag, externalContext);
        addFactDetail(fact, FactDetailOrder.COUNTER);
    }

    public static abstract class ContextType {

        public static final String TOPIC_GROUP_CONTEXT = "TopicGroup";
        public static final String TOPIC_CONTEXT       = "Topic";
        public static final String CATEGORY_CONTEXT    = "Category";
        public static final String VALUE_CONTEXT       = "Value";
        public static final String VIRTUAL_CONTEXT     = "VirtualContext";

    }

    public static abstract class VirtualContext {
        public static final int APPLICATION_ID           = 1;
        public static final int UPLOAD_SCREEN_ID         = 2;
        public static final int COMPARE_SCREEN_ID        = 3;
        public static final int INFO_SCREEN_ID           = 4;
        public static final int LANDSCAPE_ORIENTATION_ID = 5;
        public static final int PORTRAIT_ORIENTATION_ID  = 6;
        public static final int BUY_BUTTON_ID            = 7;
        public static final int INFO_BUTTON_ID           = 8;
    }

    public static abstract class EventTag {

        public static final String APPLICATION_START_COUNTER_EVENT         = "APPLICATION_START_COUNTER";
        public static final String UPLOAD_SCREEN_TIMER_EVENT               = "UPLOAD_SCREEN_TIMER";
        public static final String COMPARE_SCREEN_TIMER_EVENT              = "COMPARE_SCREEN_TIMER";
        public static final String INFO_SCREEN_TIMER_EVENT                 = "INFO_SCREEN_TIMER";
        public static final String ORIENTATION_TIMER_EVENT                 = "ORIENTATION_TIMER";
        public static final String TOPIC_VIEW_TIMER_EVENT                  = "TOPIC_VIEW_TIMER";
        public static final String TOPIC_GROUP_VIEW_TIMER_EVENT            = "TOPIC_GROUP_VIEW_TIMER";
        public static final String CATEGORY_VIEW_TIMER_EVENT               = "CATEGORY_VIEW_TIMER";
        public static final String VALUE_VIEW_TIMER_EVENT                  = "VALUE_VIEW_TIMER";
        public static final String VALUE_CLICK_COUNTER_EVENT               = "VALUE_CLICK_COUNTER";
        public static final String BUY_BUTTON_CLICK_COUNTER_EVENT          = "BUY_BUTTON_CLICK_COUNTER_COUNTER";
        public static final String INFO_BUTTON_CLICK_COUNTER_COUNTER_EVENT = "INFO_BUTTON_CLICK_COUNTER_COUNTER";
    }

    private static abstract class FactDetailOrder {

        private static final int TIMER_START  = 1;
        private static final int TIMER_FINISH = 2;
        private static final int COUNTER      = 1;
    }
}
