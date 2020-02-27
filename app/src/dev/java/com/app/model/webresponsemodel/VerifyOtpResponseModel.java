package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseResponseModel;
import com.app.model.UserModel;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class VerifyOtpResponseModel extends AppBaseResponseModel {

    UserModel data;

    public UserModel getData() {
        return data;
    }


}
