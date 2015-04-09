package com.whiteboxteam.gliese.data.helper.merge;

import android.content.Context;
import android.content.OperationApplicationException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.net.Uri;
import android.os.RemoteException;
import com.whiteboxteam.gliese.data.content.ApplicationContentContract;
import com.whiteboxteam.gliese.data.db.ApplicationDatabaseContract;
import com.whiteboxteam.gliese.data.db.ApplicationDatabaseHelper;
import com.whiteboxteam.gliese.data.server.ApplicationServerContract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 04.12.13
 * Time: 23:18
 */

public abstract class BaseMergeHelper {

    private static final int SQLITE_MAX_COMPOUND_SELECT = 500;
    private final ApplicationDatabaseHelper dbHelper;
    protected Map<String, String> nameMapping = new HashMap<>();
    protected List<String>        nullColumns = new ArrayList<>();
    protected Context context;
    private   Uri     uri;

    public BaseMergeHelper(Context context, Uri uri) {
        this.context = context;
        this.uri = uri;

        dbHelper = ApplicationDatabaseHelper.getInstance(context);

        nameMapping.put(ApplicationContentContract.Base.ID, ApplicationServerContract.BaseRecord.ID);
        nameMapping.put(ApplicationContentContract.Base.ACTIVE, ApplicationServerContract.BaseRecord.ACTIVE);
        nameMapping.put(ApplicationContentContract.Base.UPDATED_AT, ApplicationServerContract.BaseRecord.UPDATED_AT);

    }

    public void merge(JSONArray json) throws JSONException, RemoteException, OperationApplicationException,
            SQLiteDatabaseLockedException {
        if (json.length() > 0) {
            int index = 0;
            while (index < json.length()) {
                index = applyBatch(json, index);
            }
        }
        context.getContentResolver().notifyChange(uri, null, false);
    }

    private int applyBatch(JSONArray json, int startIndex) throws JSONException, SQLiteDatabaseLockedException {
        StringBuilder sql = new StringBuilder("INSERT OR REPLACE INTO ");
        sql.append(getTableName()).append(" (");

        applyColumnNames(sql);
        sql.append(") ");

        String selectWord = "SELECT";
        while (startIndex < json.length()) {
            sql.append(selectWord);
            selectWord = " UNION ALL SELECT";

            JSONObject remoteEntry = json.getJSONObject(startIndex);
            applyRowValues(remoteEntry, sql);

            startIndex++;
            if (startIndex % SQLITE_MAX_COMPOUND_SELECT == 0) break;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys = OFF");
        db.execSQL(sql.toString());

        return startIndex;
    }

    protected abstract String getTableName();

    private void applyColumnNames(StringBuilder sql) {
        String delimiter = "";
        for (String key : nameMapping.keySet()) {
            sql.append(delimiter).append(key);
            delimiter = ", ";
        }
        for (String column : nullColumns) {
            sql.append(delimiter).append(column);
        }
    }

    private void applyRowValues(JSONObject remoteEntry, StringBuilder sql) throws JSONException {
        String delimiter = " ";
        for (String key : nameMapping.keySet()) {
            String value = getValue(key, remoteEntry);
            sql.append(delimiter).append("'").append(value).append("'");
            delimiter = ", ";
        }
        for (String column : nullColumns) {
            sql.append(delimiter).append("(SELECT ").append(column).append(" FROM ").append(getTableName()).append(" " +
                    "" + "WHERE ").append(ApplicationDatabaseContract.BaseEntry.ID).append(" = ").append(remoteEntry
                    .get(ApplicationServerContract.BaseRecord.ID)).append(")");
        }
    }

    private String getValue(String key, JSONObject remoteEntry) throws JSONException {
        String value = remoteEntry.optString(nameMapping.get(key)).replaceAll("'", "''");
        if (value.equals("true")) return "1";
        if (value.equals("false")) return "0";
        return value;
    }

}
