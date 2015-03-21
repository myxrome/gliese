package com.whiteboxteam.gliese.data.helper.collect;

import android.content.Context;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.RemoteException;
import org.json.JSONException;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 04.09.2014
 * Time: 12:36
 */
public abstract class BaseCollectHelper<T> {

    protected Context context;

    public BaseCollectHelper(Context context) {
        this.context = context;
    }

    public T collect() throws JSONException {
        return extract(null);
    }

    public abstract T extract(Uri parentUri) throws JSONException;

    public void complete() throws RemoteException, OperationApplicationException {

    }

}
