package com.app.fcm;

import android.os.Bundle;

import com.fcm.NotificationModal;
import com.google.firebase.messaging.RemoteMessage;


public class AppNotificationModel extends NotificationModal implements AppNotificationType {

    public AppNotificationModel(RemoteMessage remoteMessage) {
        super(remoteMessage);
    }

    public AppNotificationModel(Bundle bundle) {
        super(bundle);
    }

    public String getNotiType() {
        String data = getDataFromKey(AppNotificationMessagingService.KEY_NOTIFICATION_TYPE);
        return data == null ? "" : data;
    }

    public String getTitle() {

        String data = getDataFromKey(AppNotificationMessagingService.KEY_NOTIFICATION_TITLE);
        return data == null ? "" : data;
    }

    public String getMessage() {

        String data = getDataFromKey(AppNotificationMessagingService.KEY_NOTIFICATION_MESSAGE);
        return data == null ? "" : data;
    }

    private IllegalArgumentException getNewIllegalArgumentException(String msg) {
        return new IllegalArgumentException(msg);
    }


    public Long getNotificationTime() throws IllegalArgumentException {
        Long notification_time = null;
        try {
            notification_time = Long.parseLong(getDataFromKey(AppNotificationMessagingService.KEY_NOTIFICATION_TIME));
        } catch (NumberFormatException e) {
            throw getNewIllegalArgumentException("Notification time is not valid");

        }
        return notification_time;
    }

    public String getJsonData() {
        return getDataFromKey(AppNotificationMessagingService.KEY_JSON_DATA);
    }

    public String getImageLarge() {
        return getDataFromKey(AppNotificationMessagingService.KEY_IMAGE_LARGE);
    }

    public String getImageSmall() {
        return getDataFromKey(AppNotificationMessagingService.KEY_IMAGE_SMALL);
    }

    public boolean isAdminAlert () {
        return getNotiType().equals(TYPE_ADMIN_ALERT);
    }

}
