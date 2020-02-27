package com.app.appbase;

import android.util.Log;

import com.R;
import com.app.fcm.AppNotificationMessagingService;
import com.app.model.CustomIconModel;
import com.app.model.PlayingHistoryModel;
import com.app.model.QuotationDontShowModel;
import com.app.model.ReferEarnModel;
import com.app.model.UserModel;
import com.app.preferences.UserPrefs;
import com.app.ui.main.ToolbarFragment;
import com.base.BaseApplication;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.medy.retrofitwrapper.WebRequest;
import com.medy.retrofitwrapper.WebRequestInterface;
import com.medy.retrofitwrapper.WebServiceResponseListener;
import com.model.DeviceInfoModal;
import com.rest.WebRequestConstants;
import com.rest.WebRequestHelper;
import com.utilities.DeviceUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Manish Kumar
 * @since 29/9/18
 */

public abstract class AppBaseApplication extends BaseApplication implements
        UserPrefs.UserPrefsListener, WebServiceResponseListener, WebRequestConstants {

    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1100;
    public String currency_symbol = "";
    DeviceInfoModal deviceInfoModal;
    String deviceId;
    WebRequestInterface webRequestInterfaceDefault;
    WebRequestHelper webRequestHelper;
    ToolbarFragment toolbarFragment;
    private UserPrefs userPrefs;
    private UserModel userModel;
    private CustomIconModel customIconModel;
    private ReferEarnModel referEarnModel;
    private PlayingHistoryModel playingHistoryModel;
    private QuotationDontShowModel quotationDontShowModel;

    public static AppBaseApplication getInstance() {
        return (AppBaseApplication) instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppNotificationMessagingService.createNotificationChannel(this);
        currency_symbol = getResources().getString(R.string.currency_symbol) + " ";
        userPrefs = new UserPrefs(this);
        userPrefs.addListener(this);
        quotationDontShowModel=userPrefs.getQuotationDoNot();
        userModel = userPrefs.getLoggedInUser();
        referEarnModel = userPrefs.getReferEarnModel();
        playingHistoryModel = userPrefs.getPlayingHistoryModel();
        customIconModel = userPrefs.getAppCustomIcons();

    }

    public DeviceInfoModal getDeviceInfoModal() {
        if (deviceInfoModal == null) {
            deviceInfoModal = new DeviceInfoModal(this);
        }
        return deviceInfoModal;
    }

    public String getDeviceId() {
        if (deviceId == null) {
            deviceId = DeviceUtil.getUniqueDeviceId(this);
            printLog("getDeviceId=",deviceId);
        }
        return deviceId;
    }

    public WebRequestHelper getWebRequestHelper() {
        if (webRequestHelper == null) {
            webRequestHelper = new WebRequestHelper(this);
        }
        return webRequestHelper;
    }

    public WebRequestInterface getWebRequestInterfaceDefault() {
        if (webRequestInterfaceDefault == null) {
            OkHttpClient okHttpClient = getOkHttpClient();
            webRequestInterfaceDefault = new Retrofit.Builder()
                    .baseUrl("http://www.test.com/")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(WebRequestInterface.class);
        }
        return webRequestInterfaceDefault;
    }

    public OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder okClientBuilder = new OkHttpClient.Builder();
        okClientBuilder.connectTimeout(1 * 60 * 1000, TimeUnit.MILLISECONDS);
        okClientBuilder.readTimeout(30 * 1000, TimeUnit.MILLISECONDS);
        okClientBuilder.writeTimeout(15 * 1000, TimeUnit.MILLISECONDS);
        return okClientBuilder.build();
    }

    public QuotationDontShowModel getQuotationDontShowModel() {
        return quotationDontShowModel;
    }

    public void updateQuotationDontShowInPrefs(){
        if(getUserPrefs()!=null){
            getUserPrefs().saveQuotationDonotShow(this.quotationDontShowModel);
        }
    }

    @Override
    public void userLoggedIn(UserModel userModel) {
        this.userModel = userModel;
    }

    @Override
    public void loggedInUserUpdate(UserModel userModel) {
        this.userModel = userModel;
    }

    @Override
    public void loggedInUserClear() {
        this.userModel = null;
    }

    @Override
    public void customIconUpdate(CustomIconModel customIconModel) {
        this.customIconModel = customIconModel;
    }

    public void updateCustomAppIcons(CustomIconModel customIconModel) {
        if (userPrefs != null) {
            this.userPrefs.saveAppCustomIcons(customIconModel);
        }
    }


    public UserPrefs getUserPrefs() {
        return userPrefs;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public ReferEarnModel getReferEarnModel() {
        return referEarnModel;
    }

    public PlayingHistoryModel getPlayingHistoryModel() {
        return playingHistoryModel;
    }

    public CustomIconModel getCustomIconModel() {
        return customIconModel;
    }

    public void updateUserInPrefs() {
        if (this.userModel != null) {
            userPrefs.updateLoggedInUser(userModel);
        }
    }

    public void saveReferEarnInPrefs(ReferEarnModel referEarnModel) {
        if (referEarnModel != null) {
            this.referEarnModel = referEarnModel;
            userPrefs.saveReferEarn(referEarnModel);
        }
    }

    public void savePlayingHistoryInPrefs(PlayingHistoryModel playingHistoryModel) {
        if (playingHistoryModel != null) {
            this.playingHistoryModel = playingHistoryModel;
            userPrefs.savePlayingHistory(playingHistoryModel);
        }
    }


    public int getGooglePlayServicesAvailableStatus() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        return api.isGooglePlayServicesAvailable(this);
    }

    public boolean isPlayServiceUpdated() {
        int resultCode = getGooglePlayServicesAvailableStatus();
        if (resultCode != ConnectionResult.SUCCESS) {
            return false;
        }
        return true;
    }

    public boolean isPlayServiceErrorUserResolvable(AppBaseActivity appBaseActivity) {
        int resultCode = getGooglePlayServicesAvailableStatus();
        GoogleApiAvailability instance = GoogleApiAvailability.getInstance();
        if (instance.isUserResolvableError(resultCode)) {
            instance.getErrorDialog(appBaseActivity,
                    resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            return true;
        }
        return false;
    }


    @Override
    public void onWebRequestCall(WebRequest webRequest) {

        webRequest.addHeader(HEADER_KEY_LANG, HEADER_LANG_VALUE);
        webRequest.addHeader(HEADER_KEY_DEVICE_ID, getDeviceId());
        webRequest.addHeader(HEADER_KEY_DEVICETYPE, HEADER_DEVICETYPE_VALUE);
        webRequest.addHeader(HEADER_KEY_DEVICEINFO, getDeviceInfoModal().toString());
        webRequest.addHeader(HEADER_KEY_APPINFO, getDeviceInfoModal().getAppInfo());

        UserModel userModel = getUserModel();
        if (userModel != null) {
            webRequest.addHeader(HEADER_KEY_TOKEN, userModel.getSlug());
        }
        webRequest.setWebRequestInterface(getWebRequestInterfaceDefault());
    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {

    }

    @Override
    public void onWebRequestPreResponse(WebRequest webRequest) {

    }

    public void printLog(String tag, String response) {
        if (isDebugBuild() && tag != null && response != null) {
            Log.e(tag, response);
        }
    }
}
