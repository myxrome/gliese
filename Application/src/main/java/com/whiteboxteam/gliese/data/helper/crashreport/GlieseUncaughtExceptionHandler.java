package com.whiteboxteam.gliese.data.helper.crashreport;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.whiteboxteam.gliese.data.content.StatisticContentContract;
import com.whiteboxteam.gliese.data.sync.statistic.CrashReportSyncService;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 21.03.2015
 * Time: 13:15
 */
public class GlieseUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Context                         context;
    private Thread.UncaughtExceptionHandler oldHandler;

    public GlieseUncaughtExceptionHandler(Context context) {
        this.context = context;
        oldHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ContentValues contentValues = new ContentValues();

        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            contentValues.put(StatisticContentContract.CrashReport.NAME, info.packageName);
            contentValues.put(StatisticContentContract.CrashReport.VERSION, info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
        }
        contentValues.put(StatisticContentContract.CrashReport.EXCEPTION, ex.toString());
        String cause = ex.getCause() == null ? "" : ex.getCause().toString();
        contentValues.put(StatisticContentContract.CrashReport.CAUSE, cause);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        ex.printStackTrace(printWriter);
        contentValues.put(StatisticContentContract.CrashReport.STACKTRACE, stringWriter.toString());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("gmt"));

        contentValues.put(StatisticContentContract.CrashReport.HAPPENED_AT, dateFormat.format(new Date()));
        context.getContentResolver().insert(StatisticContentContract.CrashReport.CONTENT_URI, contentValues);
        CrashReportSyncService.startCrashReportSync(context);
        if (oldHandler != null) {
            oldHandler.uncaughtException(thread, ex);
        }
    }

}
