package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseResponseModel;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class PrivateContestSettingResponseModel extends AppBaseResponseModel {


    /**
     * data : {"PRIVATE_CONTEST_MAX_CONTEST_SIZE":"500","PRIVATE_CONTEST_MAX_PRIZE_POOL":"10000"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * PRIVATE_CONTEST_MAX_CONTEST_SIZE : 500
         * PRIVATE_CONTEST_MAX_PRIZE_POOL : 10000
         */

        private String PRIVATE_CONTEST_MAX_CONTEST_SIZE;
        private String PRIVATE_CONTEST_MAX_PRIZE_POOL;

        public String getPRIVATE_CONTEST_MAX_CONTEST_SIZE() {
            return PRIVATE_CONTEST_MAX_CONTEST_SIZE;
        }

        public void setPRIVATE_CONTEST_MAX_CONTEST_SIZE(String PRIVATE_CONTEST_MAX_CONTEST_SIZE) {
            this.PRIVATE_CONTEST_MAX_CONTEST_SIZE = PRIVATE_CONTEST_MAX_CONTEST_SIZE;
        }

        public String getPRIVATE_CONTEST_MAX_PRIZE_POOL() {
            return PRIVATE_CONTEST_MAX_PRIZE_POOL;
        }

        public void setPRIVATE_CONTEST_MAX_PRIZE_POOL(String PRIVATE_CONTEST_MAX_PRIZE_POOL) {
            this.PRIVATE_CONTEST_MAX_PRIZE_POOL = PRIVATE_CONTEST_MAX_PRIZE_POOL;
        }
    }
}
