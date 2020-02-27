package com.fcm;

import android.content.Context;

import com.preferences.BasePrefs;


/**
 * Created by bitu on 24/9/17.
 */

public class NotificationPrefs extends BasePrefs {

    private static final String Prefsname = "prefs_notification";
    private static NotificationPrefs instance;
    private Context context;

    public static final String KEY_NOTIFICATION_TOKEN = "notification_token";


    private NotificationPrefs (Context context) {
        this.context = context;
    }

    public static NotificationPrefs getInstance (Context context) {
        if (instance == null) {
            instance = new NotificationPrefs(context);
        }
        return instance;
    }

    @Override
    public String getPrefsName () {
        return Prefsname;
    }

    @Override
    public Context getContext () {
        return context;
    }


    public void saveNotificationToken (String data) {
        if (data == null) {
            data = "";
        }
        setStringKeyValuePrefs(KEY_NOTIFICATION_TOKEN, data);
    }

    public String getNotificationToken () {
        return getStringKeyValuePrefs(KEY_NOTIFICATION_TOKEN);
    }


}
