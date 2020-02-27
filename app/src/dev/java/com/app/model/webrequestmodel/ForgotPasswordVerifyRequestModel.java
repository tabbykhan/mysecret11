package com.app.model.webrequestmodel;

import com.app.appbase.AppBaseRequestModel;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class ForgotPasswordVerifyRequestModel extends AppBaseRequestModel {

    public String otp;
    public String type;
    public String mobile;
    public String mobile_code;
    public String password;

}
