package com.app.ui.main.dashboard.profile.verification.mobile;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.model.UserModel;
import com.app.model.webrequestmodel.UpdateProfileRequestModel;
import com.app.model.webrequestmodel.VerifyOtpRequestModel;
import com.app.model.webresponsemodel.CheckUserResponseModel;
import com.app.model.webresponsemodel.VerifyOtpResponseModel;
import com.app.ui.MyApplication;
import com.app.ui.main.dashboard.profile.verification.VerificationActivity;
import com.customviews.OtpView;
import com.medy.retrofitwrapper.WebRequest;
import com.permissions.PermissionHelperNew;
import com.sociallogin.FacebookLoginHandler;
import com.sociallogin.GplusLoginHandler;
import com.sociallogin.SocialData;
import com.sociallogin.SocialLoginListener;
import com.sociallogin.SocialPermissionErrorDialog;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobileFragment extends AppBaseFragment {


    CardView cv_mobile;
    CardView cv_mobile_unverify;
    LinearLayout ll_already_mobile;
    TextView tv_message;
    OtpView otpView;
    TextView tv_send_again_otp;
    TextView tv_mobile_verify;
    LinearLayout ll_mobile_enter_lay;
    EditText et_mobile;
    TextView tv_mobile_proceed;


    TextView tv_mobile_number;
    CardView cv_email;
    TextView tv_email;
    CardView cv_email_unverify;
    LinearLayout ll_already_email;
    LinearLayout ll_email_enter_lay;
    TextView tv_send_again;
    RelativeLayout rl_fb_btn;
    RelativeLayout rl_gplus_btn;
    EditText et_email;
    TextView tv_proceed;

    String enteredPhoneNumber = "";


    public String getPhone() {
        return enteredPhoneNumber;
    }

    public String getCountryMobileCode() {
        return COUNTRY_MOBILE_CODE_VALUE;
    }

    public String getVerifyFrom() {
        return "SP";
    }

    SocialLoginListener fbLoginListener = new SocialLoginListener() {
        @Override
        public void socialLoginSuccess(SocialData socialData) {
            callEmailVerify(socialData.getEmail(), "Y", socialData.getLoginFrom());
        }

        @Override
        public void fbLoginPermissionError() {
            showFbLoginErrorDialog();
        }
    };

    SocialLoginListener gplusLoginListener = new SocialLoginListener() {
        @Override
        public void socialLoginSuccess(SocialData socialData) {
            callEmailVerify(socialData.getEmail(), "Y", socialData.getLoginFrom());
        }

        @Override
        public void fbLoginPermissionError() {

        }
    };

    private void showFbLoginErrorDialog() {
        SocialPermissionErrorDialog socialPermissionErrorDialog = new SocialPermissionErrorDialog();
        socialPermissionErrorDialog.setOnClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        MyApplication.getInstance().getFacebookLoginHandler().setSocialLoginListner(fbLoginListener);
                        MyApplication.getInstance().getFacebookLoginHandler().callLoginWithRead(MobileFragment.this,
                                Arrays.asList("email"));
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        });
        socialPermissionErrorDialog.show(getChildFm(),
                socialPermissionErrorDialog.getClass().getSimpleName());
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_mobile;
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();

        cv_mobile = getView().findViewById(R.id.cv_mobile);
        tv_mobile_number = getView().findViewById(R.id.tv_mobile_number);

        cv_mobile_unverify = getView().findViewById(R.id.cv_mobile_unverify);
        ll_already_mobile = getView().findViewById(R.id.ll_already_mobile);
        tv_message = getView().findViewById(R.id.tv_message);
        otpView = getView().findViewById(R.id.otpView);
        tv_send_again_otp = getView().findViewById(R.id.tv_send_again_otp);
        tv_mobile_verify = getView().findViewById(R.id.tv_mobile_verify);
        ll_mobile_enter_lay = getView().findViewById(R.id.ll_mobile_enter_lay);
        et_mobile = getView().findViewById(R.id.et_mobile);
        tv_mobile_proceed = getView().findViewById(R.id.tv_mobile_proceed);

        cv_email = getView().findViewById(R.id.cv_email);
        tv_email = getView().findViewById(R.id.tv_email);

        cv_email_unverify = getView().findViewById(R.id.cv_email_unverify);
        ll_already_email = getView().findViewById(R.id.ll_already_email);
        ll_email_enter_lay = getView().findViewById(R.id.ll_email_enter_lay);
        tv_send_again = getView().findViewById(R.id.tv_send_again);
        rl_fb_btn = getView().findViewById(R.id.rl_fb_btn);
        rl_gplus_btn = getView().findViewById(R.id.rl_gplus_btn);
        et_email = getView().findViewById(R.id.et_email);
        tv_proceed = getView().findViewById(R.id.tv_proceed);

        rl_fb_btn.setOnClickListener(this);
        rl_gplus_btn.setOnClickListener(this);
        tv_proceed.setOnClickListener(this);
        tv_mobile_verify.setOnClickListener(this);
        tv_mobile_proceed.setOnClickListener(this);
        setupSendAgain();
        setupSendAgainMobile();
        setupData();
    }

    private void setupSendAgainMobile() {
        String signUp = "Send Again";
        tv_send_again_otp.setMovementMethod(LinkMovementMethod.getInstance());
        Pattern termsAndConditionsMatcher = Pattern.compile(signUp);
        Matcher m1 = termsAndConditionsMatcher.matcher(tv_send_again_otp.getText().toString());
        if (m1.find()) {
            URLSpan urlSpan = new URLSpan(m1.group(0)) {
                @Override
                public void onClick(View widget) {
                    updateViewVisibitity(ll_already_mobile, View.GONE);
                    updateViewVisibitity(ll_mobile_enter_lay, View.VISIBLE);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setColor(getResources().getColor(R.color.colorWhite));
                 //   ds.setColor(((AppBaseActivity) getActivity()).getColorFromStyle(R.attr.app_link_text_color));
                    ds.setUnderlineText(true);
                }
            };
            SpannableString string = SpannableString.valueOf(tv_send_again_otp.getText());
            string.setSpan(urlSpan, m1.start(), m1.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

    }

    private void setupSendAgain() {
        String signUp = "Send Again";
        tv_send_again.setMovementMethod(LinkMovementMethod.getInstance());
        Pattern termsAndConditionsMatcher = Pattern.compile(signUp);
        Matcher m1 = termsAndConditionsMatcher.matcher(tv_send_again.getText().toString());
        if (m1.find()) {
            URLSpan urlSpan = new URLSpan(m1.group(0)) {
                @Override
                public void onClick(View widget) {
                    updateViewVisibitity(ll_already_email, View.GONE);
                    updateViewVisibitity(ll_email_enter_lay, View.VISIBLE);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setColor(getResources().getColor(R.color.colorWhite));
                  //  ds.setColor(((AppBaseActivity) getActivity()).getColorFromStyle(R.attr.app_link_text_color));
                    ds.setUnderlineText(true);
                }
            };
            SpannableString string = SpannableString.valueOf(tv_send_again.getText());
            string.setSpan(urlSpan, m1.start(), m1.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

    }

    public void setupData() {
        UserModel userModel = getUserModel();
        if (userModel != null) {
            tv_mobile_number.setText(userModel.getFullMobile());

            tv_message.setText("Enter your OTP code send on mobile " + getCountryMobileCode() + "-" + getPhone());
            et_mobile.setText(getPhone());

            tv_email.setText(userModel.getEmail());
            et_email.setText(userModel.getEmail());
            if (userModel.isPhoneVerified()) {
                updateViewVisibitity(cv_mobile, View.VISIBLE);
                updateViewVisibitity(cv_mobile_unverify, View.GONE);
            } else {
                updateViewVisibitity(cv_mobile, View.GONE);
                updateViewVisibitity(cv_mobile_unverify, View.VISIBLE);
                if (isValidString(getPhone())) {
                    updateViewVisibitity(ll_already_mobile, View.VISIBLE);
                    updateViewVisibitity(ll_mobile_enter_lay, View.GONE);

                } else {
                    updateViewVisibitity(ll_already_mobile, View.GONE);
                    updateViewVisibitity(ll_mobile_enter_lay, View.VISIBLE);

                }
            }

            if (userModel.isValidString(userModel.getEmail())) {
                if (userModel.isEmailVerified()) {
                    updateViewVisibitity(cv_email, View.VISIBLE);
                    updateViewVisibitity(cv_email_unverify, View.GONE);
                } else {
                    updateViewVisibitity(cv_email, View.GONE);
                    updateViewVisibitity(cv_email_unverify, View.VISIBLE);
                    updateViewVisibitity(ll_already_email, View.VISIBLE);
                    updateViewVisibitity(ll_email_enter_lay, View.GONE);
                }

            } else {
                updateViewVisibitity(cv_email, View.GONE);
                updateViewVisibitity(cv_email_unverify, View.VISIBLE);
                updateViewVisibitity(ll_already_email, View.GONE);
                updateViewVisibitity(ll_email_enter_lay, View.VISIBLE);

            }
        }

    }

    public void setupDataOtp(String messageText) {
        otpView.setOTP(messageText);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_fb_btn:
                MyApplication.getInstance().getFacebookLoginHandler().setSocialLoginListner(fbLoginListener);
                MyApplication.getInstance().getFacebookLoginHandler().callLoginWithRead(this,
                        Arrays.asList("public_profile", "email"));

                break;
            case R.id.rl_gplus_btn:
                MyApplication.getInstance().getGplusLoginHandler().setSocialLoginListner(gplusLoginListener);
                MyApplication.getInstance().getGplusLoginHandler().gPlusSignIn(this);
                break;
            case R.id.tv_proceed:
                String email = et_email.getText().toString().trim();
                if (email.isEmpty()) {
                    showErrorMsg("Please enter email address.");
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    showErrorMsg("Please enter valid email address.");
                    return;
                }
                callEmailVerify(email, "N", "");
                break;
            case R.id.tv_mobile_proceed:
                final String mobile = et_mobile.getText().toString().trim();
                if (mobile.isEmpty()) {
                    showErrorMsg("Please enter mobile number.");
                    return;
                }
                if (!Pattern.matches("[0-9]{10}", mobile)) {
                    showErrorMsg("Please enter valid mobile number.");
                    return;
                }

                if (PermissionHelperNew.needSMSPermission(getActivity(), new PermissionHelperNew.OnSpecificPermissionGranted() {
                    @Override
                    public void onPermissionGranted(boolean isGranted, boolean withNeverAsk, String permission, int requestCode) {
                        callSendOtpMObile(mobile);
                    }
                })) {
                    return;
                }
                callSendOtpMObile(mobile);
                break;
            case R.id.tv_mobile_verify:
                String otp = otpView.getOTP();
                if (otp.isEmpty()) {
                    showErrorMsg("Please enter OTP.");
                    return;
                }

                if (otp.length() < 4) {
                    showErrorMsg("Please enter valid OTP.");
                    return;
                }

                callUpdateVerifyMobile(otp);
                break;
        }
    }

    private void callEmailVerify(String email, String isSocial, String socialType) {
        if (email.isEmpty()) {
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return;
        }
        UpdateProfileRequestModel requestModel = new UpdateProfileRequestModel();
        requestModel.email = email;
        requestModel.is_social = isSocial;
        requestModel.social_type = socialType;

        displayProgressBar(false, "Wait...");
        getWebRequestHelper().updateVerifyEmail(requestModel, this);
    }

    private void callSendOtpMObile(String mobile) {
        if (mobile.isEmpty()) {
            return;
        }
        if (!Pattern.matches("[0-9]{10}", mobile)) {
            return;
        }
        final UpdateProfileRequestModel requestModel = new UpdateProfileRequestModel();
        requestModel.country_mobile_code = COUNTRY_MOBILE_CODE_VALUE;
        requestModel.phone = mobile;

        displayProgressBar(false, "Wait...");
        getWebRequestHelper().sendOtpMobile(requestModel, this);
    }

    private void callUpdateVerifyMobile(String otp) {

        VerifyOtpRequestModel requestModel = new VerifyOtpRequestModel();
        requestModel.otp = otp;
        requestModel.country_mobile_code = getCountryMobileCode();
        requestModel.phone = getPhone();
        requestModel.type = getVerifyFrom();

        displayProgressBar(false, "Wait...");
        getWebRequestHelper().updateVerifyMobile(requestModel, this);
    }


    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        dismissProgressBar();
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_UPDATE_VERIFY_EMAIL:
                handleUpdateVerifyEmailResponse(webRequest);
                break;
            case ID_SEND_OTP_MOBILE:
                handleSendOtpMobileResponse(webRequest);
                break;
            case ID_UPDATE_VERIFY_MOBILE:
                handleUpdateVerifyMobileResponse(webRequest);
                break;
        }

    }

    private void handleUpdateVerifyEmailResponse(WebRequest webRequest) {
        VerifyOtpResponseModel responsePojo = webRequest.getResponsePojo(VerifyOtpResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            UserModel data = responsePojo.getData();
            if (data == null) return;
            if (isFinishing()) return;
            getUserPrefs().updateLoggedInUser(data);
            showCustomToast(responsePojo.getMessage());
            setupData();
            if (((VerificationActivity) getActivity()).isAutoBack()) {
                getActivity().supportFinishAfterTransition();
            }
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
            MyApplication.getInstance().getFacebookLoginHandler().callLogout();
            MyApplication.getInstance().getGplusLoginHandler().callLogout();
        }
    }

    private void handleSendOtpMobileResponse(WebRequest webRequest) {
        CheckUserResponseModel responsePojo = webRequest.getResponsePojo(CheckUserResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            CheckUserResponseModel.DataBean data = responsePojo.getData();
            if (data == null) return;
            if (isFinishing()) return;
            this.enteredPhoneNumber = data.getPhone();
            MyApplication.getInstance().showOtp(data.getOtp());
            showCustomToast(responsePojo.getMessage());
            setupData();
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }
    }

    private void handleUpdateVerifyMobileResponse(WebRequest webRequest) {
        VerifyOtpResponseModel responsePojo = webRequest.getResponsePojo(VerifyOtpResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            UserModel data = responsePojo.getData();
            if (data == null) return;
            getUserPrefs().updateLoggedInUser(data);
            showCustomToast(responsePojo.getMessage());
            setupData();
            if (((VerificationActivity) getActivity()).isAutoBack()) {
                getActivity().supportFinishAfterTransition();
            }
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GplusLoginHandler.RC_SIGN_IN && resultCode != Activity.RESULT_CANCELED) {
            MyApplication.getInstance().getGplusLoginHandler().onActivityResult(data);
        } else if (requestCode == FacebookLoginHandler.fbLoginRequestCode()) {
            MyApplication.getInstance().getFacebookLoginHandler().onActivityResult(requestCode,
                    resultCode, data);
        }
    }


}
