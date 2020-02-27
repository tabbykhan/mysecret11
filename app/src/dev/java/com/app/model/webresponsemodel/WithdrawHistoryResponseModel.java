package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseResponseModel;
import com.app.model.WithdrawHistoryModel;

import java.util.List;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class WithdrawHistoryResponseModel extends AppBaseResponseModel {

    List<WithdrawHistoryModel> data;

    public List<WithdrawHistoryModel> getData() {
        return data;
    }
}
