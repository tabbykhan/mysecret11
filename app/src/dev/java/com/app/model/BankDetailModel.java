package com.app.model;

import com.app.appbase.AppBaseModel;

public class BankDetailModel extends AppBaseModel {

    long id;
    String account_number;
    String account_holder_name;
    String ifsc;
    String status;
    String reason;
    String image;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccount_number() {
        return getValidString(account_number);
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getAccount_holder_name() {
        return getValidString(account_holder_name);
    }

    public void setAccount_holder_name(String account_holder_name) {
        this.account_holder_name = account_holder_name;
    }

    public String getIfsc() {
        return getValidString(ifsc);
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getStatus() {
        return getValidString(status);
    }

    public String getReason() {
        return getValidString(reason);
    }

    public boolean isInReview() {
        return getStatus().equals("P");
    }

    public boolean isApproved() {
        return getStatus().equals("A");
    }

    public boolean isRejected() {
        return getStatus().equals("R");
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return getValidString(image);
    }

    public void setImage(String image) {
        this.image = image;
    }
}
