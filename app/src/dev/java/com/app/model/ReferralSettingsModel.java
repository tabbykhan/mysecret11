package com.app.model;

import com.app.appbase.AppBaseModel;

public class ReferralSettingsModel extends AppBaseModel {

    String NEW_REGISTRATION;


    public String getNEW_REGISTRATION() {
        return getValidString(NEW_REGISTRATION);
    }

    public void setNEW_REGISTRATION(String NEW_REGISTRATION) {
        this.NEW_REGISTRATION = NEW_REGISTRATION;
    }
}
