package com.app.appupdater;

import android.content.Context;

import com.app.appbase.AppBaseApplication;
import com.app.model.AppVersionModel;
import com.app.model.webrequestmodel.CheckAppVersionRequestModel;
import com.app.model.webresponsemodel.CheckAppVersionResponseModel;
import com.medy.retrofitwrapper.WebRequest;
import com.medy.retrofitwrapper.WebServiceResponseListener;

/**
 * Created by ubuntu on 29/5/17.
 */

public class AppUpdateChecker implements WebServiceResponseListener {


    Context context;
    AppUpdateAvailableListener onAppUpdateAvailable;

    public void setOnAppUpdateAvailable(AppUpdateAvailableListener onAppUpdateAvailable) {
        this.onAppUpdateAvailable = onAppUpdateAvailable;
    }

    public AppUpdateChecker(Context context, AppUpdateAvailableListener onAppUpdateAvailable) {
        this.context = context;
        this.onAppUpdateAvailable = onAppUpdateAvailable;
    }

    public void checkForUpdate() {
        try {
            CheckAppVersionRequestModel requestModel = new CheckAppVersionRequestModel();
            requestModel.version_code = AppUpdateUtils.getAppInstalledVersionCode(context).longValue();
            AppBaseApplication.getInstance().getWebRequestHelper().checkAppVersion(requestModel, this);
        } catch (Exception ignore) {

        }
    }


    @Override
    public void onWebRequestCall(WebRequest webRequest) {
        try {
            AppBaseApplication.getInstance().onWebRequestCall(webRequest);
        } catch (Exception ignore) {

        }
    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        AppBaseApplication.getInstance().onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        handleCheckAppVersionResponse(webRequest);
    }

    @Override
    public void onWebRequestPreResponse(WebRequest webRequest) {

    }

    private void handleCheckAppVersionResponse(WebRequest webRequest) {
        CheckAppVersionResponseModel responsePojo = webRequest.getResponsePojo(CheckAppVersionResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            AppVersionModel data = responsePojo.getData();
            if (data == null) return;
            if (onAppUpdateAvailable != null) {
                onAppUpdateAvailable.appUpdateAvailable(data);
            }
        }
    }

    public interface AppUpdateAvailableListener {
        void appUpdateAvailable(AppVersionModel appUpdatemodal);
    }

}
