package com.whiteboxteam.gliese.data.content.provider;

import com.whiteboxteam.gliese.data.content.provider.impl.CrashReportProviderHelper;
import com.whiteboxteam.gliese.data.content.provider.impl.FactDetailProviderHelper;
import com.whiteboxteam.gliese.data.content.provider.impl.FactProviderHelper;
import com.whiteboxteam.gliese.data.content.provider.impl.SessionProviderHelper;
import com.whiteboxteam.gliese.data.db.StatisticDatabaseHelper;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 11.08.2014
 * Time: 21:39
 */
public class StatisticsContentProvider extends BaseContentProvider {

    @Override
    public boolean onCreate() {
        dbHelper = new StatisticDatabaseHelper(getContext());

        providerHelpers.add(new SessionProviderHelper());
        providerHelpers.add(new FactProviderHelper());
        providerHelpers.add(new FactDetailProviderHelper());
        providerHelpers.add(new CrashReportProviderHelper());

        return true;
    }

}
