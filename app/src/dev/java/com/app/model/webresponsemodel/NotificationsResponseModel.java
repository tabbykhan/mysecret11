package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseResponseModel;
import com.app.model.NotificationModel;

import java.util.List;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class NotificationsResponseModel extends AppBaseResponseModel {

    List<NotificationModel> data;

    public List<NotificationModel> getData() {
        return data;
    }
}
