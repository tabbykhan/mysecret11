package com.CoustomControl;

import com.CoustomControl.ResponseAndPojoClass.LoginEntityClass;
import com.CoustomControl.ResponseAndPojoClass.LoginResponseClass;
import com.CoustomControl.ResponseAndPojoClass.RegistrationMLMP;
import com.CoustomControl.ResponseAndPojoClass.RegistrationMLMResponseClass;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AppService {
    @POST("register")
    Call<RegistrationMLMResponseClass> REGISTRATION_RESPONSE_CLASS_CALL(
            @Body RegistrationMLMP registrationPostObject
    );
    @GET("register")
    Call<RegistrationMLMResponseClass> REGISTRATION_RESPONSE_CLASS_CALL(

    );
    @POST("register")
    Call<LoginResponseClass> Login_CALL(
            @Body LoginEntityClass loginEntityClass
    );
}
