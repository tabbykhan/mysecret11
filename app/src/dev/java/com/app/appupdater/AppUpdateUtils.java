package com.app.appupdater;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.base.BaseApplication;


/**
 * Created by ubuntu on 29/5/17.
 */

public class AppUpdateUtils {


    static final String TAG = "AppUpdateUtils";

    public static void printLog(String msg) {
        if (msg == null) return;
        if (BaseApplication.instance.isDebugBuild()) {
            Log.e(TAG, msg);
        }
    }


    public static Integer getAppInstalledVersionCode(Context context) {
        Integer versionCode = 0;

        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionCode;
    }

    public static String getAppInstalledVersionName(Context context) {
        String versionName = "";

        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionName;
    }
}
