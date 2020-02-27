package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseModel;
import com.app.appbase.AppBaseResponseModel;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class CheckUserResponseModel extends AppBaseResponseModel {

    DataBean data;

    public DataBean getData() {
        return data;
    }

    public class DataBean extends AppBaseModel {
        String type;
        String slug;

        String email;

        String otp;
        String phone;
        String country_mobile_code;


        public String getType() {
            return getValidString(type);
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSlug() {
            return getValidString(slug);
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public String getEmail() {
            return getValidString(email);
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getOtp() {
            return getValidString(otp);
        }

        public void setOtp(String otp) {
            this.otp = otp;
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

        public boolean needOtpVerify() {
            return getType().equals("M");
        }
    }
}
