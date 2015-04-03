package com.whiteboxteam.gliese.data.helper.statistic;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 03.09.2014
 * Time: 17:51
 */
public class DeviceHelper {

    public static String getId(Context context) {
        HashFunction hashFunction = Hashing.md5();
        HashCode hash = hashFunction.newHasher().
                putString(getMACAddress(context), Charsets.UTF_8).
                putString(Build.SERIAL, Charsets.UTF_8).
                putString(Settings.Secure.ANDROID_ID, Charsets.UTF_8).
                hash();

        return hash.toString();
    }

    private static String getMACAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getMacAddress();
    }

    public static String getBrand() {
        return Build.BRAND;
    }

    public static String getModel() {
        return Build.MODEL;
    }

    public static String getRelease() {
        return Build.VERSION.RELEASE;
    }

    public static String getSDK() {
        return String.valueOf(Build.VERSION.SDK_INT);
    }

}
