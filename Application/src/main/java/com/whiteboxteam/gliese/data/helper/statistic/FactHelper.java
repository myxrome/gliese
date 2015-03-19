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

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 19.03.2015
 * Time: 12:05
 */
public final class FactHelper {

    public static final String TOPIC_GROUP_CONTEXT = "TopicGroup";
    public static final String TOPIC_CONTEXT = "Topic";
    public static final String CATEGORY_CONTEXT = "Category";
    public static final String VALUE_CONTEXT = "Value";

    public static final String APPLICATION_CONTEXT = "Application";
    public static final String SCREEN_CONTEXT = "Screen";
    public static final String BUTTON_CONTEXT = "Button";
    public static final String ORIENTATION_CONTEXT = "Orientation";

    private static final int TIMER_START = 1;
    private static final int TIMER_FINISH = 2;
    private static final int COUNTER = 1;

    private static FactHelper instance = null;
    private final Context context;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);

    private FactHelper(Context context) {
        this.context = context;
    }

    public static FactHelper getInstance(Context context) {
        if (instance == null) instance = new FactHelper(context);
        return instance;
    }

    public FactEntity startTimer(long contextId, String contextType, String eventTag) {
        Uri fact = addFact(contextId, contextType, eventTag, null);
        addFactDetail(fact, TIMER_START);
        return FactEntity.newInstance(Long.parseLong(fact.getLastPathSegment()));
    }

    private Uri addFact(long contextId, String contextType, String eventTag, String externalContext) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(StatisticContentContract.Fact.EVENT, eventTag);
        contentValues.put(StatisticContentContract.Fact.CONTEXT_ID, contextId);
        contentValues.put(StatisticContentContract.Fact.CONTEXT_TYPE, contextType);
        contentValues.put(StatisticContentContract.Fact.EXTERNAL_CONTEXT, externalContext);
        return context.getContentResolver().insert(StatisticContentContract.Fact.getContentUriBySession(SessionHelper.getInstance(context).getCurrent()), contentValues);
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
        if (fact == null) return;
        addFactDetail(ContentUris.withAppendedId(StatisticContentContract.Fact.CONTENT_URI, fact.id), TIMER_FINISH);
    }

    public void increaseCounter(long contextId, String contextType, String eventTag) {
        Uri fact = addFact(contextId, contextType, eventTag, null);
        addFactDetail(fact, COUNTER);
    }

    public void increaseCounter(long contextId, String contextType, String eventTag, String externalContext) {
        Uri fact = addFact(contextId, contextType, eventTag, externalContext);
        addFactDetail(fact, COUNTER);
    }
}
