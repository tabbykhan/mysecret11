package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseResponseModel;
import com.google.gson.annotations.SerializedName;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class PromoCodeResponseModel extends AppBaseResponseModel {



    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private String id;
        private String recharge;
        private String cach_bonus;
        private String is_use;
        private String is_use_max;
        private String max_recharge;
        private String cash_bonus_type;
        @SerializedName("code")
        private String codeX;
        private String start_date;
        private String end_date;
        private String already_use;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRecharge() {
            return recharge;
        }

        public void setRecharge(String recharge) {
            this.recharge = recharge;
        }

        public String getCach_bonus() {
            return cach_bonus;
        }

        public void setCach_bonus(String cach_bonus) {
            this.cach_bonus = cach_bonus;
        }

        public String getIs_use() {
            return is_use;
        }

        public void setIs_use(String is_use) {
            this.is_use = is_use;
        }

        public String getIs_use_max() {
            return is_use_max;
        }

        public void setIs_use_max(String is_use_max) {
            this.is_use_max = is_use_max;
        }

        public String getMax_recharge() {
            return max_recharge;
        }

        public void setMax_recharge(String max_recharge) {
            this.max_recharge = max_recharge;
        }

        public String getCash_bonus_type() {
            return cash_bonus_type;
        }

        public void setCash_bonus_type(String cash_bonus_type) {
            this.cash_bonus_type = cash_bonus_type;
        }

        public String getCodeX() {
            return getValidString(codeX);
        }

        public void setCodeX(String codeX) {
            this.codeX = codeX;
        }

        public String getStart_date() {
            return start_date;
        }

        public void setStart_date(String start_date) {
            this.start_date = start_date;
        }

        public String getEnd_date() {
            return end_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public String getAlready_use() {
            return already_use;
        }

        public void setAlready_use(String already_use) {
            this.already_use = already_use;
        }

        private String getValidString (String data) {
            return data == null ? "" : data;
        }
    }


}


