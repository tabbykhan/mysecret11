package com.base;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.multidex.MultiDexApplication;

import com.BuildConfig;
import com.crashlytics.android.Crashlytics;
import com.fcm.NotificationModal;
import com.fcm.PushNotificationHelper;
import com.google.firebase.FirebaseApp;

import java.lang.reflect.Field;

import io.fabric.sdk.android.Fabric;


/**
 * @author Manish Kumar
 * @since 28/9/18
 */

public abstract class BaseApplication extends MultiDexApplication implements PushNotificationHelper.PushNotificationHelperListener {

    public static BaseApplication instance;

    PushNotificationHelper pushNotificationHelper;

    public PushNotificationHelper getPushNotificationHelper () {
        return pushNotificationHelper;
    }

    @Override
    public void onCreate () {
        super.onCreate();
        instance = this;
        FirebaseApp.initializeApp(this);
        Fabric.with(this, new Crashlytics());
        pushNotificationHelper = new PushNotificationHelper(this);
        pushNotificationHelper.addNotificationHelperListener(this);

    }

    public boolean isDebugBuild () {

        try {
            String packageName = getPackageName();

            Bundle bundle = getPackageManager().getApplicationInfo(
                    getPackageName(), PackageManager.GET_META_DATA).metaData;
            String manifest_pkg = null;
            if (bundle != null) {
                manifest_pkg = bundle.getString("manifest_pkg", null);
            }
            if (manifest_pkg != null) {
                packageName = manifest_pkg;
            }
            final Class<?> buildConfig = Class.forName(packageName + ".BuildConfig");
            final Field DEBUG = buildConfig.getField("DEBUG");
            DEBUG.setAccessible(true);
            return DEBUG.getBoolean(null);
        } catch (final Throwable t) {
            final String message = t.getMessage();
            if (message != null && message.contains("BuildConfig")) {
                return false;
            } else {
                return BuildConfig.DEBUG;
            }
        }
    }

    @Override
    public void onPushNotificationReceived (NotificationModal notificationModal) {

    }
}
