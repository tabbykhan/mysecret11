package com.app.ui.forgotpassword;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.appbase.AppBaseResponseModel;
import com.app.model.webrequestmodel.ForgotPasswordRequestModel;
import com.app.model.webrequestmodel.VerifyOtpRequestModel;
import com.app.model.webresponsemodel.ForgotPasswordResponseModel;
import com.app.ui.MyApplication;
import com.app.ui.main.ToolbarFragment;
import com.customviews.OtpView;
import com.medy.retrofitwrapper.WebRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Manish Kumar
 * @since 3/10/18
 */

public class ForgotPasswordVerifyActivity extends AppBaseActivity {

    ToolbarFragment toolbarFragment;

    TextView tv_message;
    OtpView otpView;
    TextView otpTimer;
    TextView tv_resendOtp;
    EditText et_password;
    EditText et_confirm_password;
    TextView tv_verify;
    TextView tv_contact_us;


    public String getEmail() {
        Bundle extras = getIntent().getExtras();
        return extras == null ? "" : extras.getString(EMAIL, "");
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
    public int getLayoutResourceId() {
        return R.layout.activity_forgot_password_verify;
    }


    @Override
    public void initializeComponent() {
        super.initializeComponent();
        Fragment toolbar = getFm().findFragmentById(R.id.fragment_toolbar);
        if (toolbar instanceof AppBaseFragment) {
            toolbarFragment = (ToolbarFragment) toolbar;
            toolbarFragment.initializeComponent();
        }

        tv_message = findViewById(R.id.tv_message);
        otpView = findViewById(R.id.otpView);
        otpTimer = findViewById(R.id.otpTimer);
        tv_resendOtp = findViewById(R.id.tv_resendOtp);
        updateViewVisibitity(tv_resendOtp, View.GONE);
        et_password = findViewById(R.id.et_password);
        et_confirm_password = findViewById(R.id.et_confirm_password);
        tv_verify = findViewById(R.id.tv_verify);
        tv_contact_us = findViewById(R.id.tv_contact_us);

        tv_resendOtp.setOnClickListener(this);
        tv_verify.setOnClickListener(this);
        tv_message.setText("OTP send to email: " + getEmail());

        startCounter(OTP_COUNTER);
        setupSignInButton();
    }

    public void startCounter(final long millisecond) {
        otpView.setOTP("");
        et_password.setText("");
        et_confirm_password.setText("");
        MyApplication.getInstance().showOtp(getOtp());
        long remainMin = (millisecond / (60 * 1000));
        long remainSec = (millisecond - (remainMin * 60 * 1000)) / 1000;
        otpTimer.setText(String.format("%02d:%02d", remainMin, remainSec));
        updateViewVisibitity(tv_resendOtp, View.GONE);

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

    private void setupSignInButton() {
        String signUp = "Contact Us";
        tv_contact_us.setMovementMethod(LinkMovementMethod.getInstance());
        Pattern termsAndConditionsMatcher = Pattern.compile(signUp);
        Matcher m1 = termsAndConditionsMatcher.matcher(tv_contact_us.getText().toString());
        if (m1.find()) {
            URLSpan urlSpan = new URLSpan(m1.group(0)) {
                @Override
                public void onClick(View widget) {

                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setColor(getColorFromStyle(R.attr.app_link_text_color));
                    ds.setUnderlineText(false);
                }
            };
            SpannableString string = SpannableString.valueOf(tv_contact_us.getText());
            string.setSpan(urlSpan, m1.start(), m1.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private void callResendOtp() {
        ForgotPasswordRequestModel requestModel = new ForgotPasswordRequestModel();
        requestModel.email = getEmail();
        displayProgressBar(false, "Wait...");
        getWebRequestHelper().forgotPassword(requestModel, this);
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

        String password = et_password.getText().toString();
        if (password.isEmpty()) {
            showErrorMsg("Please enter password.");
            return;
        }
        String confirmPassword = et_confirm_password.getText().toString();
        if (confirmPassword.isEmpty()) {
            showErrorMsg("Please confirm password.");
            return;
        }

        if (!Pattern.matches("[^\\s]{6,15}", password)) {
            showErrorMsg("Password should be 6-15 characters long");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showErrorMsg("Password is not match");
            return;
        }


        VerifyOtpRequestModel requestModel = new VerifyOtpRequestModel();
        requestModel.otp = otp;
        requestModel.email = getEmail();
        requestModel.type = getVerifyFrom();
        requestModel.password = password;

        displayProgressBar(false, "Wait...");
        getWebRequestHelper().verifyOtp(requestModel, this);
    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        dismissProgressBar();
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_FORGOT_PASSWORD:
                handleForgotPasswordResponse(webRequest);
                return;
            case ID_VERIFY_OTP:
                handleVerifyOtpResponse(webRequest);
                return;
        }

    }

    private void handleForgotPasswordResponse(WebRequest webRequest) {
        ForgotPasswordResponseModel responsePojo = webRequest.getResponsePojo(ForgotPasswordResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            ForgotPasswordResponseModel.DataBean data = responsePojo.getData();
            if (data == null) return;
            if (isFinishing()) return;
            Bundle bundle = new Bundle();
            bundle.putString(VERIFY_FROM, "FE");
            bundle.putString(OTP, data.getOtp());
            bundle.putString(EMAIL, data.getEmail());
            getIntent().putExtras(bundle);
            showCustomToast(responsePojo.getMessage());
            startCounter(OTP_COUNTER);
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }

    }


    private void handleVerifyOtpResponse(WebRequest webRequest) {
        AppBaseResponseModel responsePojo = webRequest.getResponsePojo(AppBaseResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            if (isFinishing()) return;
            showCustomToast(responsePojo.getMessage());
            Bundle bundle = new Bundle();
            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            supportFinishAfterTransition();
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }
    }

}