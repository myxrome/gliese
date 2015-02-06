package com.whiteboxteam.gliese.data.helper.merge;

import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.RemoteException;
import com.whiteboxteam.gliese.data.content.ApplicationContentContract;
import com.whiteboxteam.gliese.data.db.ApplicationDatabaseHelper;
import com.whiteboxteam.gliese.data.server.ApplicationServerContract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 04.12.13
 * Time: 23:18
 */

public abstract class BaseMergeHelper {

    protected Map<String, String> nameMapping = new HashMap<>();
    protected Context context;

    private static final int SQLITE_MAX_COMPOUND_SELECT = 500;
    private SyncResult syncResult;
    private Uri uri;
    private SQLiteDatabase db;

    public BaseMergeHelper(Context context, SyncResult syncResult, Uri uri) {
        this.context = context;
        this.syncResult = syncResult;
        this.uri = uri;

        ApplicationDatabaseHelper dbHelper = new ApplicationDatabaseHelper(context);
        db = dbHelper.getWritableDatabase();

        nameMapping.put(ApplicationContentContract.Base.ID, ApplicationServerContract.BaseRecord.ID);
        nameMapping.put(ApplicationContentContract.Base.ACTIVE, ApplicationServerContract.BaseRecord.ACTIVE);
        nameMapping.put(ApplicationContentContract.Base.UPDATED_AT, ApplicationServerContract.BaseRecord.UPDATED_AT);

    }

    public void reconcile(JSONArray json) throws JSONException, RemoteException, OperationApplicationException {
        if (json.length() > 0) {
            int index = 0;
            while (index < json.length()) {
                index = applyBatch(json, index);
            }
        }
        syncResult.stats.numEntries += json.length();
        context.getContentResolver().notifyChange(uri, null, false);
    }

    private int applyBatch(JSONArray json, int startIndex) throws JSONException {
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
            if (startIndex % SQLITE_MAX_COMPOUND_SELECT == 0)
                break;
        }
        db.execSQL(sql.toString());

        return startIndex;
    }

    protected abstract String getTableName();

    private void applyRowValues(JSONObject remoteEntry, StringBuilder sql) throws JSONException {
        String delimiter = " ";
        for (String key : nameMapping.keySet()) {
            String value = getValue(key, remoteEntry).replaceAll("'", "''");
            sql.append(delimiter).append("'").append(value).append("'");
            delimiter = ", ";
        }
    }

    private String getValue(String key, JSONObject remoteEntry) throws JSONException {
        String value = remoteEntry.getString(nameMapping.get(key));
        if (value.equals("true"))
            return "1";
        if (value.equals("false"))
            return "0";
        return value;
    }

    private void applyColumnNames(StringBuilder sql) {
        String delimiter = "";
        for (String key : nameMapping.keySet()) {
            sql.append(delimiter).append(key);
            delimiter = ", ";
        }
    }

}
