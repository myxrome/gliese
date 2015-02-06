package com.whiteboxteam.gliese.data.db.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created with IntelliJ IDEA.
 * User: Aleksey
 * Date: 16.10.13
 * Time: 23:06
 * To change this template use File | Settings | File Templates.
 */
public interface TableHelper {

    public void onCreate(SQLiteDatabase db);

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

}
