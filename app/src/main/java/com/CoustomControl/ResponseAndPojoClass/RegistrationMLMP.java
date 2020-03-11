package com.CoustomControl.ResponseAndPojoClass;

import java.io.Serializable;

public class RegistrationMLMP implements Serializable {

    private String confirm_password , email , fullname , position , ref_user_id , mobile , user_id , password , sponsor_name;

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getRef_user_id() {
        return ref_user_id;
    }

    public void setRef_user_id(String ref_user_id) {
        this.ref_user_id = ref_user_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String userId) {
        this.user_id = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSponsor_name() {
        return sponsor_name;
    }

    public void setSponsor_name(String sponsor_name) {
        this.sponsor_name = sponsor_name;
    }
}
