package com.whiteboxteam.gliese.data.content.provider;

import com.whiteboxteam.gliese.data.content.provider.impl.*;
import com.whiteboxteam.gliese.data.db.ApplicationDatabaseHelper;


/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 13.11.13
 * Time: 10:57
 */
public class ApplicationContentProvider extends BaseContentProvider {

    @Override
    public boolean onCreate() {
        dbHelper = ApplicationDatabaseHelper.getInstance(getContext());

        providerHelpers.add(new TopicGroupProviderHelper());
        providerHelpers.add(new TopicProviderHelper());
        providerHelpers.add(new CategoryProviderHelper());
        providerHelpers.add(new ValueProviderHelper());
        providerHelpers.add(new DescriptionProviderHelper());
        providerHelpers.add(new PromoProviderHelper());

        return true;
    }

}
