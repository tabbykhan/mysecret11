package com.CoustomControl.ResponseAndPojoClass;

import java.io.Serializable;

public class LoginEntityClass implements Serializable {

    private String password , user_id;

    public LoginEntityClass(String email, String password) {
        this.user_id = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
