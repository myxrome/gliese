package com.whiteboxteam.gliese.data.helper.collect;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 04.09.2014
 * Time: 12:41
 */
public abstract class JSONArrayCollectHelper extends BaseCollectHelper<JSONArray> {

    public JSONArrayCollectHelper(Context context) {
        super(context);
    }

    @Override
    public JSONArray extract(Uri parentUri) throws JSONException {
        JSONArray result = new JSONArray();
        Cursor source = getSourceData(parentUri);
        while (source.moveToNext()) {
            result.put(getExtractedItem(source));
        }
        return result;
    }

    protected abstract Cursor getSourceData(Uri parentUri);

    protected abstract JSONObject getExtractedItem(Cursor source) throws JSONException;

}
