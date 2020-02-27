package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseResponseModel;
import com.app.model.WalletModel;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class DepositWalletResponseModel extends AppBaseResponseModel {

    DataBean data;

    public WalletModel getData() {
        return data == null ? null : data.getWallet();
    }

    public static class DataBean {
        WalletModel wallet;

        public WalletModel getWallet() {
            return wallet;
        }
    }


}
