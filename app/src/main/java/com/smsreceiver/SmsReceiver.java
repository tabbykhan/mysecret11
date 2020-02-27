package com.smsreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.base.BaseApplication;


public class SmsReceiver extends BroadcastReceiver {


    private static SmsListener mListener;

    private String SMS_SENDER;

    public void printLog (String msg) {
        if (BaseApplication.instance.isDebugBuild() && msg != null)
            Log.e("SmsReceiver", msg);
    }

    public SmsReceiver (String sms_sender, SmsListener listener) {
        this.SMS_SENDER = sms_sender;
        mListener = listener;
    }

    @Override
    public void onReceive (Context context, Intent intent) {
        if (SMS_SENDER == null) {
            printLog("onReceive SMS_SENDER == null");
            return;
        }
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (Object aPdusObj : pdusObj) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                    String senderAddress = currentMessage.getDisplayOriginatingAddress();
                    printLog("onReceive senderAddress = " + senderAddress);
                    if (senderAddress != null && senderAddress.trim().toLowerCase().
                            contains(SMS_SENDER.toLowerCase())) {
                        String message = currentMessage.getDisplayMessageBody();
                        printLog("onReceive message = " + message);
                        String verificationCode = getVerificationCode(message);
                        printLog("onReceive verificationCode = " + verificationCode);
                        if (verificationCode != null && mListener != null) {
                            mListener.otpMessageReceived(verificationCode);
                        }
                        break;
                    }

                }
            } else {
                printLog("onReceive SMS_SENDER == null");
            }
        } catch (Exception e) {
            e.printStackTrace();
            printLog("onReceive Exception message=" + e.getMessage());
        }
    }

    private String getVerificationCode (String message) {
        String code = null;
        try {
            if (message == null || message.trim().length() < 4) {
                return null;
            } else {

                int lastIndex = message.length() - 1;
                int startIndex = lastIndex - 4;
                code = message.substring(startIndex, lastIndex);
            }
        }catch (Exception ignore){

        }
        return code;
    }

    public interface SmsListener {
        void otpMessageReceived(String messageText);
    }
}
