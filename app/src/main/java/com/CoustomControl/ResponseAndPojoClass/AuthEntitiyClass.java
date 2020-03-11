package com.CoustomControl.ResponseAndPojoClass;

import com.google.gson.annotations.SerializedName;

public class AuthEntitiyClass {
    @SerializedName("user_id")
    String userId;
     @SerializedName("password")
    String password;

    public AuthEntitiyClass(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}
