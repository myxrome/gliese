package com.whiteboxteam.gliese.data.content.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 11.08.2014
 * Time: 21:43
 */
public abstract class BaseContentProvider extends ContentProvider {

    protected Collection<ProviderHelper> providerHelpers = new ArrayList<>();
    protected SQLiteOpenHelper dbHelper;

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        ProviderHelper helper = getProviderHelper(uri);

        selection = helper.buildSelectionFromUri(uri, selection);
        String tableName = helper.getTableName();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor result = db.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
        result.setNotificationUri(getContext().getContentResolver(), helper.getRootUri(uri));
        return result;
    }

    @Override
    public String getType(Uri uri) {
        for (ProviderHelper helper : providerHelpers) {
            String type = helper.getType(uri);
            if (type != null) return type;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        ProviderHelper helper = getProviderHelper(uri);

        String tableName = helper.getTableName();
        values.putAll(helper.getExtraValuesFromUri(uri));

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = db.insert(tableName, null, values);
        Uri result = ContentUris.withAppendedId(uri, id);
        getContext().getContentResolver().notifyChange(result, null, false);

        return result;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        ProviderHelper helper = getProviderHelper(uri);

        selection = helper.buildSelectionFromUri(uri, selection);
        String tableName = helper.getTableName();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = db.delete(tableName, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null, false);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        ProviderHelper helper = getProviderHelper(uri);

        selection = helper.buildSelectionFromUri(uri, selection);
        String tableName = helper.getTableName();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = db.update(tableName, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null, false);
        return count;
    }

    private ProviderHelper getProviderHelper(Uri uri) {
        for (ProviderHelper helper : providerHelpers)
            if (helper.isUriMatched(uri)) return helper;
        return null;
    }
}
