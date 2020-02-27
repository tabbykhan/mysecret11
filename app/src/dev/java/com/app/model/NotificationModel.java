package com.app.model;

import com.app.appbase.AppBaseModel;

public class NotificationModel extends AppBaseModel {

    String title;
    String notification;
    String sender_type;
    long created;
    String image_thumb;
    String image_large;
    boolean readMore = true;

    public String getTitle() {
        return getValidString(title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotification() {
        return getValidString(notification);
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getSender_type() {
        return getValidString(sender_type);
    }

    public void setSender_type(String sender_type) {
        this.sender_type = sender_type;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getImage_thumb() {
        return getValidString(image_thumb);
    }

    public void setImage_thumb(String image_thumb) {
        this.image_thumb = image_thumb;
    }

    public String getImage_large() {
        return getValidString(image_large);
    }

    public void setImage_large(String image_large) {
        this.image_large = image_large;
    }

    public boolean isReadMore() {
        return readMore;
    }

    public void setReadMore(boolean readMore) {
        this.readMore = readMore;
    }

    public boolean isSendByAdmin() {
        return getSender_type().equals("ADMIN");
    }



    public String getCreatedDateText() {
        return getFormatedDateString(DATE_DDMMYYYY, getCreated());
    }

    public String getCreatedTimeText() {
        return getFormatedDateString(hh_mm_a, getCreated());
    }
}
