package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseResponseModel;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class PrivateContestFreeResponseModel extends AppBaseResponseModel {


    /**
     * data : {"entry_fees":"672.00"}
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
         * entry_fees : 672.00
         */

        private String entry_fees;

        public String getEntry_fees() {
            return entry_fees;
        }

        public void setEntry_fees(String entry_fees) {
            this.entry_fees = entry_fees;
        }
    }
}
