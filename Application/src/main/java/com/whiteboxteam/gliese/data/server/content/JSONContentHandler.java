package com.whiteboxteam.gliese.data.server.content;

import com.google.common.base.Charsets;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 12.11.13
 * Time: 10:26
 */
public final class JSONContentHandler {

    public static Object getContent(byte[] source) {
        Object result = null;
        if (source != null) {
            String json = new String(source, Charsets.UTF_8);
            try {
                result = isJSONObject(json) ? new JSONObject(json) : new JSONArray(json);
            } catch (Exception e) {
            }
        }
        return result;
    }

    private static boolean isJSONObject(String source) {
        return source.startsWith("{");
    }
}
