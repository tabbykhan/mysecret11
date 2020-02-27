package com.app.ui.optverifiction;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.fcm.AppNotificationMessagingService;
import com.app.model.UserModel;
import com.app.model.webrequestmodel.CheckUserRequestModel;
import com.app.model.webrequestmodel.NewUserRequestModel;
import com.app.model.webrequestmodel.VerifyOtpRequestModel;
import com.app.model.webresponsemodel.CheckUserResponseModel;
import com.app.model.webresponsemodel.NewUserResponseModel;
import com.app.model.webresponsemodel.VerifyOtpResponseModel;
import com.app.ui.MyApplication;
import com.app.ui.main.ToolbarFragment;
import com.app.ui.main.dashboard.DashboardActivityNew;
import com.customviews.OtpView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.medy.retrofitwrapper.WebRequest;
import com.utilities.DeviceScreenUtil;

/**
 * @author Manish Kumar
 * @since 3/10/18
 */

public class OtpVerifyActivity extends AppBaseActivity {

    ToolbarFragment toolbarFragment;

    LinearLayout ll_top;
    TextView tv_message;
    OtpView otpView;
    TextView otpTimer;
    TextView tv_resendOtp;
    TextView tv_verify;

    public String getData() {
        Bundle extras = getIntent().getExtras();
        return extras == null ? "" : extras.getString(DATA, "");
    }

    public String getPhone() {
        Bundle extras = getIntent().getExtras();
        return extras == null ? "" : extras.getString(PHONE, "");
    }

    public String getCountryMobileCode() {
        Bundle extras = getIntent().getExtras();
        return extras == null ? "" : extras.getString(COUNTRY_MOBILE_CODE, "");
    }

    public String getOtp() {
        Bundle extras = getIntent().getExtras();
        return extras == null ? "" : extras.getString(OTP, "");
    }

    public String getVerifyFrom() {
        Bundle extras = getIntent().getExtras();
        return extras == null ? "" : extras.getString(VERIFY_FROM, "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppNotificationMessagingService.generateLatestToken();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_verify_otp;
    }


    @Override
    public void initializeComponent() {
        super.initializeComponent();
        Fragment toolbar = getFm().findFragmentById(R.id.fragment_toolbar);
        if (toolbar instanceof AppBaseFragment) {
            toolbarFragment = (ToolbarFragment) toolbar;
            toolbarFragment.initializeComponent();
        }

        ll_top = findViewById(R.id.ll_top);
        tv_message = findViewById(R.id.tv_message);
        otpView = findViewById(R.id.otpView);
        otpTimer = findViewById(R.id.otpTimer);
        tv_resendOtp = findViewById(R.id.tv_resendOtp);
        updateViewVisibitity(tv_resendOtp, View.GONE);
        tv_verify = findViewById(R.id.tv_verify);

        tv_resendOtp.setOnClickListener(this);
        tv_verify.setOnClickListener(this);


        String message="OTP send to <b>" + getCountryMobileCode() + "-" + getPhone()+"</b>";
        tv_message.setText(Html.fromHtml(message));

        setupUi();
        startCounter(OTP_COUNTER);

    }

    private void setupUi() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_top.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.topMargin = DeviceScreenUtil.getInstance().getHeight(0.05f);
        ll_top.setLayoutParams(layoutParams);
    }


    public void startCounter(final long millisecond) {
        otpView.setOTP("");
        MyApplication.getInstance().showOtp(getOtp());
        long remainMin = (millisecond / (60 * 1000));
        long remainSec = (millisecond - (remainMin * 60 * 1000)) / 1000;
        otpTimer.setText(String.format("%02d:%02d", remainMin, remainSec));
        updateViewVisibitity(tv_resendOtp, View.GONE);
        updateViewVisibitity(otpTimer, View.VISIBLE);

        CountDownTimer downTimer = new CountDownTimer(millisecond, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (isFinishing()) {
                    return;
                }
                long remainMin = (millisUntilFinished / (60 * 1000));
                long remainSec = (millisUntilFinished - (remainMin * 60 * 1000)) / 1000;
                otpTimer.setText(String.format("%02d:%02d", remainMin, remainSec));
                updateViewVisibitity(tv_resendOtp, View.GONE);
            }

            @Override
            public void onFinish() {
                if (isFinishing()) {
                    return;
                }
                updateViewVisibitity(otpTimer, View.GONE);
                updateViewVisibitity(tv_resendOtp, View.VISIBLE);
            }
        };
        downTimer.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_resendOtp:
                callResendOtp();
                break;
            case R.id.tv_verify:
                callVerifyOtp();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }


    private void goToDashboardActivity(Bundle bundle) {
        Intent intent = new Intent(this, DashboardActivityNew.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    private void callResendOtp() {
        String data = getData();
        if (!isValidString(data)) return;
        if (getVerifyFrom().equals("V")) {
            try {
                NewUserRequestModel requestModel = new Gson().fromJson(data, NewUserRequestModel.class);
                displayProgressBar(false, "Wait...");
                getWebRequestHelper().newUser(requestModel, this);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }


        } else if (getVerifyFrom().equals("L")) {
            try {
                CheckUserRequestModel requestModel = new Gson().fromJson(data, CheckUserRequestModel.class);
                displayProgressBar(false, "Wait...");
                getWebRequestHelper().checkUser(requestModel, this);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }

    }

    private void callVerifyOtp() {
        String otp = otpView.getOTP();
        if (otp.isEmpty()) {
            showErrorMsg("Please enter OTP.");
            return;
        }

        if (otp.length() < 4) {
            showErrorMsg("Please enter valid OTP.");
            return;
        }


        VerifyOtpRequestModel requestModel = new VerifyOtpRequestModel();
        requestModel.otp = otp;
        requestModel.country_mobile_code = getCountryMobileCode();
        requestModel.phone = getPhone();
        requestModel.type = getVerifyFrom();

        displayProgressBar(false, "Wait...");
        getWebRequestHelper().verifyOtp(requestModel, this);
    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        dismissProgressBar();
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_VERIFY_OTP:
                handleVerifyOtpResponse(webRequest);
                break;
            case ID_NEW_USER:
                handleRegisterResponse(webRequest);
                break;
            case ID_CHECK_USER:
                handleCheckUser(webRequest);
                break;
        }

    }

    private void handleVerifyOtpResponse(WebRequest webRequest) {
        VerifyOtpResponseModel responsePojo = webRequest.getResponsePojo(VerifyOtpResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            UserModel data = responsePojo.getData();
            if (data == null) return;
            getUserPrefs().saveLoggedInUser(data);
            showCustomToast(responsePojo.getMessage());
            goToDashboardActivity(null);
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }

    }

    private void handleRegisterResponse(WebRequest webRequest) {
        NewUserResponseModel responsePojo = webRequest.getResponsePojo(NewUserResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            NewUserResponseModel.DataBean data = responsePojo.getData();
            if (data == null) return;
            NewUserRequestModel requestModel = webRequest.getExtraData(DATA);
            if (requestModel == null) return;
            if (isFinishing()) return;
            Bundle bundle = new Bundle();
            bundle.putString(VERIFY_FROM, "V");
            bundle.putString(OTP, data.getOtp());
            bundle.putString(PHONE, data.getPhone());
            bundle.putString(COUNTRY_MOBILE_CODE, data.getCountry_mobile_code());
            bundle.putString(DATA, new Gson().toJson(requestModel));
            showCustomToast(responsePojo.getMessage());
            getIntent().putExtras(bundle);
            startCounter(OTP_COUNTER);
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }

    }

    private void handleCheckUser(WebRequest webRequest) {
        CheckUserResponseModel responsePojo = webRequest.getResponsePojo(CheckUserResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            CheckUserResponseModel.DataBean data = responsePojo.getData();
            if (data == null) return;
            if (data.needOtpVerify()) {
                CheckUserRequestModel requestModel = webRequest.getExtraData(DATA);
                if (requestModel == null) return;
                if (isFinishing()) return;
                Bundle bundle = new Bundle();
                bundle.putString(VERIFY_FROM, "L");
                bundle.putString(OTP, data.getOtp());
                bundle.putString(PHONE, data.getPhone());
                bundle.putString(COUNTRY_MOBILE_CODE, data.getCountry_mobile_code());
                bundle.putString(DATA, new Gson().toJson(requestModel));
                showCustomToast(responsePojo.getMessage());
                getIntent().putExtras(bundle);
                startCounter(OTP_COUNTER);
            }

        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }
    }


    @Override
    public void otpMessageReceived (String messageText) {
        otpView.setOTP(messageText);
    }

    @Override
    protected void onStart () {
        registerSmsReceiver();
        super.onStart();
    }

    @Override
    protected void onStop () {
        super.onStop();
        unregisterSmsReceiver();
    }


}
