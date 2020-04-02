package com.app.model.webrequestmodel;

import com.app.appbase.AppBaseRequestModel;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class DepositAmountRequestModel extends AppBaseRequestModel {

    public float amount;
   public String paymentmethod;//PAYTM,RAZORPAY
    public String promocode;


}
