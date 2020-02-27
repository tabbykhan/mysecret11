package com.app.model.webrequestmodel;

import com.app.appbase.AppBaseRequestModel;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class UpdateProfileRequestModel extends AppBaseRequestModel {


    public String firstname;
    public String lastname;
    public long dob;
    public String addressline1;
    public String addressline2;
    public long country;
    public long state;
    public String city;
    public String pincode;

    public String phone;
    public String email;
    public String is_social;
    public String social_type;
    public String country_mobile_code;

    public String image;
    public String old_password;
    public String password;
    public String fb_image;


}
