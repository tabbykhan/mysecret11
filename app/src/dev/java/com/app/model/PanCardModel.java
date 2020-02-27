package com.app.model;

import com.app.appbase.AppBaseModel;

public class PanCardModel extends AppBaseModel {
    long id;
    String number;
    String name;
    String dob;
    String status;
    String reason;
    String image;
    StateModel state;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return getValidString(number);
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return getValidString(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return getValidString(dob);
    }

    public void setDob(String dob) {
        this.dob = dob;
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

    public StateModel getState() {
        return state;
    }

    public void setState(StateModel state) {
        this.state = state;
    }
}
