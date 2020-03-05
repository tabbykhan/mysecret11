package com.app.ui.checkuser;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ConstantsFlavor;
import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.appbase.AppBaseResponseModel;
import com.app.fcm.AppNotificationMessagingService;
import com.app.model.UserModel;
import com.app.model.webrequestmodel.CheckUserRequestModel;
import com.app.model.webrequestmodel.SocialLoginRequestModel;
import com.app.model.webresponsemodel.CheckUserResponseModel;
import com.app.model.webresponsemodel.VerifyOtpResponseModel;
import com.app.ui.MyApplication;
import com.app.ui.dialogs.ConfirmationLocationDialog;
import com.app.ui.login.LoginActivity;
import com.app.ui.main.ToolbarFragment;
import com.app.ui.main.dashboard.DashboardActivityNew;
import com.app.ui.optverifiction.OtpVerifyActivity;
import com.app.ui.register.RegisterActivity;
import com.google.gson.Gson;
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

/**
 * @author Manish Kumar
 * @since 3/10/18
 */

public class CheckUserActivity extends AppBaseActivity {

    ToolbarFragment toolbarFragment;

    EditText et_username;
    TextView tv_proceed;
    RelativeLayout rl_fb_btn;
    RelativeLayout rl_gplus_btn;
    TextView tv_signup;
    TextView tv_signupMLM;
    SocialLoginListener fbLoginListener = new SocialLoginListener() {
        @Override
        public void socialLoginSuccess(SocialData socialData) {
            callSocialLogin(socialData);
        }

        @Override
        public void fbLoginPermissionError() {
            showFbLoginErrorDialog();
        }
    };

    SocialLoginListener gplusLoginListener = new SocialLoginListener() {
        @Override
        public void socialLoginSuccess(SocialData socialData) {
            callSocialLogin(socialData);
        }

        @Override
        public void fbLoginPermissionError() {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        AppNotificationMessagingService.generateLatestToken();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_check_user;
    }


    @Override
    public void initializeComponent() {
        super.initializeComponent();
        socialLogout();

        Fragment toolbar = getFm().findFragmentById(R.id.fragment_toolbar);
        if (toolbar instanceof AppBaseFragment) {
            toolbarFragment = (ToolbarFragment) toolbar;
            toolbarFragment.initializeComponent();
        }

        et_username = findViewById(R.id.et_username);
        tv_proceed = findViewById(R.id.tv_proceed);
        rl_fb_btn = findViewById(R.id.rl_fb_btn);
        rl_gplus_btn = findViewById(R.id.rl_gplus_btn);
        tv_signup = findViewById(R.id.tv_signup);

        tv_proceed.setOnClickListener(this);
        rl_fb_btn.setOnClickListener(this);
        rl_gplus_btn.setOnClickListener(this);
        tv_signup.setOnClickListener(this);
        tv_signupMLM = findViewById(R.id.tv_signupMLM);

        setupSignUpButton();
        if(ConstantsFlavor.type == ConstantsFlavor.Type.vision)
         setupSignUpMLMButton();
        showUnAuth();
    }

    private void showUnAuth() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String unAuth = extras.getString("unAuth", "");
            if (isValidString(unAuth)) {
                getIntent().getExtras().putString("unAuth", "");
                try {
                    AppBaseResponseModel appBaseResponseModel = new Gson().fromJson(unAuth, AppBaseResponseModel.class);
                    if (appBaseResponseModel != null && isValidString(appBaseResponseModel.getMessage())) {
                        showErrorMsg(appBaseResponseModel.getMessage());
                    }
                } catch (Exception ignore) {

                }
            }
        }
    }


    private void socialLogout() {
        MyApplication.getInstance().getFacebookLoginHandler().callLogout();
        MyApplication.getInstance().getGplusLoginHandler().callLogout();
    }


    private void setupSignUpButton() {
        String signUp = "Register";
        tv_signup.setMovementMethod(LinkMovementMethod.getInstance());
        Pattern termsAndConditionsMatcher = Pattern.compile(signUp);
        Matcher m1 = termsAndConditionsMatcher.matcher(tv_signup.getText().toString());
        if (m1.find()) {
            URLSpan urlSpan = new URLSpan(m1.group(0)) {
                @Override
                public void onClick(View widget) {
                    showConfirmationDialog();
                    //goToRegisterActivity(null);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    if (ConstantsFlavor.type == ConstantsFlavor.Type.sportteam){
                        ds.setColor(getResources().getColor(R.color.colorFbBlue));
                        ds.setUnderlineText(false);//there show text below line
                    }else {
                        ds.setColor(getResources().getColor(R.color.colorYellow));
                        ds.setUnderlineText(false);//there show text below line
                    }

                }
            };
            SpannableString string = SpannableString.valueOf(tv_signup.getText());
            string.setSpan(urlSpan, m1.start(), m1.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

    }
    private void setupSignUpMLMButton() {
        String signUp = "MLM Register";   //MLM Register

        tv_signupMLM.setMovementMethod(LinkMovementMethod.getInstance());
        Pattern termsAndConditionsMatcher = Pattern.compile(signUp);
        Matcher m1 = termsAndConditionsMatcher.matcher(tv_signupMLM.getText().toString());
        if (m1.find()) {
            URLSpan urlSpan = new URLSpan(m1.group(0)) {
                @Override
                public void onClick(View widget) {
                    showConfirmationDialog();
                    //goToRegisterActivity(null);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    if (ConstantsFlavor.type == ConstantsFlavor.Type.sportteam){
                        ds.setColor(getResources().getColor(R.color.colorFbBlue));
                        ds.setUnderlineText(false);//there show text below line
                    }else {
                        ds.setColor(getResources().getColor(R.color.colorYellow));
                        ds.setUnderlineText(false);//there show text below line
                    }

                }
            };
            SpannableString string = SpannableString.valueOf(tv_signupMLM.getText());
            string.setSpan(urlSpan, m1.start(), m1.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_proceed:
                callCheckUser();
                break;
            case R.id.rl_fb_btn:
                MyApplication.getInstance().getFacebookLoginHandler().setSocialLoginListner(fbLoginListener);
                MyApplication.getInstance().getFacebookLoginHandler().callLoginWithRead(this,
                        Arrays.asList("public_profile", "email"));

                break;
            case R.id.rl_gplus_btn:
                MyApplication.getInstance().getGplusLoginHandler().setSocialLoginListner(gplusLoginListener);
                MyApplication.getInstance().getGplusLoginHandler().gPlusSignIn(this);
                break;

        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }


    private void showFbLoginErrorDialog() {
        SocialPermissionErrorDialog socialPermissionErrorDialog = new SocialPermissionErrorDialog();
        socialPermissionErrorDialog.setOnClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        MyApplication.getInstance().getFacebookLoginHandler().setSocialLoginListner(fbLoginListener);
                        MyApplication.getInstance().getFacebookLoginHandler().callLoginWithRead(CheckUserActivity.this,
                                Arrays.asList("email"));
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        });
        socialPermissionErrorDialog.show(getFm(),
                socialPermissionErrorDialog.getClass().getSimpleName());
    }

    private void goToOtpVerifyActivity(Bundle bundle) {
        Intent intent = new Intent(this, OtpVerifyActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    private void goToLoginActivity(Bundle bundle) {
        Intent intent = new Intent(this, LoginActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }


    private void goToRegisterActivity(Bundle bundle) {
        Intent intent = new Intent(this, RegisterActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
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


    private void callCheckUser() {
        String username = et_username.getText().toString().trim();
        if (username.isEmpty()) {
            showErrorMsg("Please enter Email / Mobile number.");
            return;
        }
        final CheckUserRequestModel requestModel = new CheckUserRequestModel();
        requestModel.username = username;

        if (Pattern.matches("[0-9]{10}", username)) {
            requestModel.type = "M";
        }
        if (Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            requestModel.type = "E";
        }
        if (requestModel.type == null) {
            showErrorMsg("Please enter valid Email / Mobile number.");
            return;
        }
        if (requestModel.type.equals("M")) {
            requestModel.username = COUNTRY_MOBILE_CODE_VALUE + username;
        }
        if (requestModel.type.equals("M")) {
            if (PermissionHelperNew.needSMSPermission(this, new PermissionHelperNew.OnSpecificPermissionGranted() {
                @Override
                public void onPermissionGranted(boolean isGranted, boolean withNeverAsk, String permission, int requestCode) {
                    displayProgressBar(false, "Wait...");
                    getWebRequestHelper().checkUser(requestModel, CheckUserActivity.this);
                }
            })) {
                return;
            }
        }
        displayProgressBar(false, "Wait...");
        getWebRequestHelper().checkUser(requestModel, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelperNew.onSpecificRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    private void callSocialLogin(SocialData socialData) {
        if (!socialData.isValidEmail()) {
            socialLogout();
            showErrorMsg("Something went wrong");
            return;
        }

        SocialLoginRequestModel requestModel = new SocialLoginRequestModel();
        requestModel.email = socialData.getEmail();
        requestModel.firstname = socialData.getFirstName();
        requestModel.lastname = socialData.getLastName();
        requestModel.social_type = socialData.getLoginFrom();
        requestModel.social_id = socialData.getId();

        displayProgressBar(false, "Wait...");
        getWebRequestHelper().socialLogin(requestModel, this);
    }


    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        dismissProgressBar();
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_CHECK_USER:
                handleCheckUser(webRequest);
                break;
            case ID_SOCIAL_LOGIN:
                handleSocialLoginResponse(webRequest);
                break;
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
                Bundle bundle = new Bundle();
                bundle.putString(VERIFY_FROM, "L");
                bundle.putString(OTP, data.getOtp());
                bundle.putString(PHONE, data.getPhone());
                bundle.putString(COUNTRY_MOBILE_CODE, data.getCountry_mobile_code());
                bundle.putString(DATA, new Gson().toJson(requestModel));
                showCustomToast(responsePojo.getMessage());
                goToOtpVerifyActivity(bundle);
            } else {
                Bundle bundle = new Bundle();
                bundle.putString(EMAIL, data.getEmail());
                goToLoginActivity(bundle);
            }

        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }
    }

    private void handleSocialLoginResponse(WebRequest webRequest) {
        VerifyOtpResponseModel responsePojo = webRequest.getResponsePojo(VerifyOtpResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            UserModel data = responsePojo.getData();
            if (data == null) return;
            getUserPrefs().saveLoggedInUser(data);
            //showCustomToast(responsePojo.getMessage());
            goToDashboardActivity(null);
        } else {
            if (isFinishing()) return;
            socialLogout();
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


    private void showConfirmationDialog() {
        Bundle bundle = new Bundle();
        bundle.putString(MESSAGE, "By joining this contest, you accept "+ getString(R.string.app_name) +" T&amp;C and confirm that you are not a resident of Assam, Odissa, Telangana, Nagaland or Sikkim.");
        bundle.putString(POS_BTN, "OK");
        bundle.putString(NEG_BTN, "NO");
        ConfirmationLocationDialog instance = ConfirmationLocationDialog.getInstance(bundle);
        instance.setOnClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        goToRegisterActivity(null);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        });
        instance.show(getFm(), instance.getClass().getSimpleName());
    }
}
