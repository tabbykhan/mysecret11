package com.CoustomControl.ResponseAndPojoClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegistrationData {
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("sponsor_name")
    @Expose
    private Object sponsorName;
    @SerializedName("password")
    @Expose
    private String password;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Object getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(Object sponsorName) {
        this.sponsorName = sponsorName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

