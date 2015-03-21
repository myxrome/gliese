package com.whiteboxteam.gliese.data.helper.collect;

import android.content.Context;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.RemoteException;
import com.whiteboxteam.gliese.data.helper.statistic.DeviceHelper;
import com.whiteboxteam.gliese.data.server.StatisticServerContract;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 03.09.2014
 * Time: 21:52
 */
public class DeviceCollectHelper extends BaseCollectHelper<JSONObject> {

    private JSONArrayCollectHelper collectHelper;

    public DeviceCollectHelper(Context context, JSONArrayCollectHelper collectHelper) {
        super(context);
        this.collectHelper = collectHelper;
    }

    public JSONObject extract(Uri parentUri) throws JSONException {
        JSONObject result = new JSONObject();
        result.put(StatisticServerContract.DeviceData.UDID, DeviceHelper.getId(context)).
                put(StatisticServerContract.DeviceData.BRAND, DeviceHelper.getBrand()).
                put(StatisticServerContract.DeviceData.MODEL, DeviceHelper.getModel()).
                put(StatisticServerContract.DeviceData.RELEASE, DeviceHelper.getRelease()).
                put(StatisticServerContract.DeviceData.SDK, DeviceHelper.getSDK()).
                put(StatisticServerContract.DeviceData.NESTED_LIST, collectHelper.collect());
        return result;
    }

    public void complete() throws RemoteException, OperationApplicationException {
        collectHelper.complete();
    }

}
