package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseModel;
import com.app.appbase.AppBaseResponseModel;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class ForgotPasswordResponseModel extends AppBaseResponseModel {

    DataBean data;

    public DataBean getData() {
        return data;
    }

    public class DataBean extends AppBaseModel {
        String otp;
        String email;
        String phone;
        String country_mobile_code;

        public String getOtp() {
            return getValidString(otp);
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }

        public String getEmail() {
            return getValidString(email);
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return getValidString(phone);
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getCountry_mobile_code() {
            return getValidString(country_mobile_code);
        }

        public void setCountry_mobile_code(String country_mobile_code) {
            this.country_mobile_code = country_mobile_code;
        }
    }
}
