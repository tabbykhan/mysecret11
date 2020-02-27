package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseResponseModel;
import com.app.model.TransactionHistoryModel;

import java.util.List;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class TransactionHistoryResponseModel extends AppBaseResponseModel {

    List<TransactionHistoryModel> data;

    public List<TransactionHistoryModel> getData() {
        return data;
    }
}
