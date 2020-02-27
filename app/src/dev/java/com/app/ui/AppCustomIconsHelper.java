package com.app.ui;

import android.os.Handler;

import com.app.model.CustomIconModel;
import com.app.model.webresponsemodel.AppCustomIconResponseModel;
import com.medy.retrofitwrapper.WebRequest;
import com.medy.retrofitwrapper.WebServiceResponseListener;
import com.rest.WebRequestConstants;
import com.rest.WebRequestHelper;

public class AppCustomIconsHelper implements WebServiceResponseListener {

    final int PAGE_REFRESH_TIME = 1000;
    Handler handler;
    Runnable runnable;


    boolean started;

    public AppCustomIconsHelper() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                getAppCustomIcons();
            }
        };
    }

    @Override
    public void onWebRequestCall(WebRequest webRequest) {
        if (!started) return;
        if (MyApplication.getInstance() != null) {
            MyApplication.getInstance().onWebRequestCall(webRequest);
        }

    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        if (!started) return;
        if (MyApplication.getInstance() != null) {
            MyApplication.getInstance().onWebRequestResponse(webRequest);
            if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) {
                return;
            }

            if (webRequest.getWebRequestId() == WebRequestConstants.ID_GET_APP_CUSTOM_ICONS) {
                AppCustomIconResponseModel responseModel = webRequest.getResponsePojoSimple(AppCustomIconResponseModel.class);
                if (responseModel != null && !responseModel.isError()) {
                    CustomIconModel data = responseModel.getData();
                    if (data != null) {
                        if (MyApplication.getInstance() != null) {
                            CustomIconModel oldData = MyApplication.getInstance().getCustomIconModel();
                            if (oldData != null && oldData.hashCode() == data.hashCode()) {
                                return;
                            }
                            MyApplication.getInstance().updateCustomAppIcons(data);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onWebRequestPreResponse(WebRequest webRequest) {

    }

    public void getAppCustomIcons() {
        if (MyApplication.getInstance() != null) {
            if (!started) return;
            WebRequestHelper webRequestHelper = MyApplication.getInstance().getWebRequestHelper();
            if (webRequestHelper != null) {
                webRequestHelper.getAppCustomIcons(this);
            }
        }
    }

    public void start() {
        started = true;
        if (handler != null && runnable != null)
            handler.removeCallbacks(runnable);
        getAppCustomIcons();
    }

    public void stop() {
        started = false;
        if (handler != null && runnable != null)
            handler.removeCallbacks(runnable);
    }
}
