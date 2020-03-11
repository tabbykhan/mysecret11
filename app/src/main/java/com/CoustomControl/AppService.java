package com.CoustomControl;

import com.CoustomControl.ResponseAndPojoClass.AuthEntitiyClass;
import com.CoustomControl.ResponseAndPojoClass.AuthResponse;
import com.CoustomControl.ResponseAndPojoClass.LoginEntityClass;
import com.CoustomControl.ResponseAndPojoClass.LoginResponseClass;
import com.CoustomControl.ResponseAndPojoClass.RegistrationMLMP;
import com.CoustomControl.ResponseAndPojoClass.RegistrationMLMResponseClass;
import com.CoustomControl.ResponseAndPojoClass.TeamEntity;
import com.CoustomControl.ResponseAndPojoClass.TeamViewResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AppService {
    @POST("register")
    Call<RegistrationMLMResponseClass> REGISTRATION_RESPONSE_CLASS_CALL(
            @Body RegistrationMLMP registrationPostObject
    );

    @GET("get-team-view")
    Call<RegistrationMLMResponseClass> REGISTRATION_RESPONSE_CLASS_CALL(

    );

    @POST("register")
    Call<LoginResponseClass> Login_CALL(
            @Body LoginEntityClass loginEntityClass
    );

    @POST("get_auth_token")
    Call<AuthResponse> token_CALL(
            @Body AuthEntitiyClass authEntityClass

    );

    @POST("get-team-view")
    Call<TeamViewResponse> GetTeamView_CALL(
            @Body TeamEntity teamEntey
    );
}
