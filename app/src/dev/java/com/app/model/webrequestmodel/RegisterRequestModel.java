package com.app.model.webrequestmodel;

import com.app.appbase.AppBaseRequestModel;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class RegisterRequestModel extends AppBaseRequestModel {

    public String otp;
    public String type;
    public String f_name;
    public String l_name;
    public String mobile;
    public String mobile_code;
    public String password;
    public String device_id;
    public String device_type;
    public String device_token;
    public String ipaddress;

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
