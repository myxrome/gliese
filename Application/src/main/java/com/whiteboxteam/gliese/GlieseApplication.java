package com.whiteboxteam.gliese;

import android.app.Application;
import com.whiteboxteam.gliese.data.helper.crashreport.GlieseUncaughtExceptionHandler;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 21.03.2015
 * Time: 13:13
 */
public class GlieseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new GlieseUncaughtExceptionHandler(this));
    }
}
