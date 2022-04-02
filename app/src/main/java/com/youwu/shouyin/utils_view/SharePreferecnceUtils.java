package com.youwu.shouyin.utils_view;

import android.content.Context;
import android.content.SharedPreferences;

import com.youwu.shouyin.app.AppApplication;


public class SharePreferecnceUtils {
    private final static String SHARE_CSMART = "printer";
    private final static String SHARE_CONNECTDEVICE = AppApplication.getInstance().getPackageName() + "share.connectdevice";

    public static void setConnectDevicee(String source) {
        SharedPreferences sp = AppApplication.getInstance().getSharedPreferences(SHARE_CSMART, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(SHARE_CONNECTDEVICE, source).commit();
    }

    public static String getConectDevice() {
        SharedPreferences sp = AppApplication.getInstance().getSharedPreferences(SHARE_CSMART, Context.MODE_PRIVATE);
        return sp.getString(SHARE_CONNECTDEVICE, "");
    }
}
