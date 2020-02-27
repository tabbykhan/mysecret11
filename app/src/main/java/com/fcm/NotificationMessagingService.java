package com.fcm;

/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.base.BaseApplication;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

public abstract class NotificationMessagingService<T> extends FirebaseMessagingService {


    public static void printLog(String msg) {
        if (BaseApplication.instance.isDebugBuild()) {
            Log.e(NotificationMessagingService.class.getSimpleName(), msg);
        }
    }


    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        T notificationModal = getNotificationModal(remoteMessage);
        if (notificationModal == null) {
            return;
        }
        printLog(notificationModal.toString());
        pushMessageReceived(notificationModal);
    }

    public abstract T getNotificationModal(RemoteMessage remoteMessage);

    public abstract void pushMessageReceived(T notificationModal);

    public abstract void generatePushNotification(T notificationModal);

    public boolean isAppIsInBackground() {
        boolean isAppInBackground = true;
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            if (runningProcesses == null) return isAppInBackground;
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(getPackageName())) {
                            isAppInBackground = false;

                        }
                    }
                }
            }
        } else {
            try {
                List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                if (taskInfo == null) return isAppInBackground;
                ComponentName componentInfo = taskInfo.get(0).topActivity;
                if (componentInfo.getPackageName().equals(getPackageName())) {
                    isAppInBackground = false;
                }
            } catch (Exception e) {

            }
        }
        return isAppInBackground;
    }

    public static void createNotificationChannel(Context context,
                                                 String id, String name, int importance, String description) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(id, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        NotificationPrefs.getInstance(getApplicationContext()).saveNotificationToken(s);
        printLog("onNewToken : " + s);
    }


}
