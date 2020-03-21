package com.CoustomControl.ResponseAndPojoClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginData {

    @SerializedName("otpmode")
    @Expose
    private String otpmode;
    @SerializedName("mobileverification")
    @Expose
    private String mobileverification;
    @SerializedName("mailotp")
    @Expose
    private String mailotp;
    @SerializedName("google2faauth")
    @Expose
    private String google2faauth;
    @SerializedName("mailverification")
    @Expose
    private String mailverification;
    @SerializedName("access_token")
    @Expose
    private String accessToken;

    public String getOtpmode() {
        return otpmode;
    }

    public void setOtpmode(String otpmode) {
        this.otpmode = otpmode;
    }

    public String getMobileverification() {
        return mobileverification;
    }

    public void setMobileverification(String mobileverification) {
        this.mobileverification = mobileverification;
    }

    public String getMailotp() {
        return mailotp;
    }

    public void setMailotp(String mailotp) {
        this.mailotp = mailotp;
    }

    public String getGoogle2faauth() {
        return google2faauth;
    }

    public void setGoogle2faauth(String google2faauth) {
        this.google2faauth = google2faauth;
    }

    public String getMailverification() {
        return mailverification;
    }

    public void setMailverification(String mailverification) {
        this.mailverification = mailverification;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
