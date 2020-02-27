package com.app.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.fcm.AppNotificationMessagingService;
import com.app.model.UserModel;
import com.app.model.webrequestmodel.ForgotPasswordRequestModel;
import com.app.model.webrequestmodel.LoginRequestModel;
import com.app.model.webresponsemodel.ForgotPasswordResponseModel;
import com.app.model.webresponsemodel.VerifyOtpResponseModel;
import com.app.ui.forgotpassword.ForgotPasswordActivity;
import com.app.ui.forgotpassword.ForgotPasswordVerifyActivity;
import com.app.ui.main.ToolbarFragment;
import com.app.ui.main.dashboard.DashboardActivityNew;
import com.customviews.TypefaceEditText;
import com.medy.retrofitwrapper.WebRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Manish Kumar
 * @since 3/10/18
 */

public class LoginActivity extends AppBaseActivity {

    public static final int REQUEST_FORGOT_PASSWORD_OTP_VERIFY = 102;

    ToolbarFragment toolbarFragment;

    TextView tv_email;
    TypefaceEditText et_password;
    TextView tv_login;
    TextView tv_forgot_password;
    TextView tv_login_mobile;
    CheckBox cb_password_show_hide;

    public String getEmail() {
        Bundle extras = getIntent().getExtras();
        return extras == null ? "" : extras.getString(EMAIL, "");
    }


    @Override
    protected void onResume() {
        super.onResume();
        AppNotificationMessagingService.generateLatestToken();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_login;
    }


    @Override
    public void initializeComponent() {
        super.initializeComponent();
        Fragment toolbar = getFm().findFragmentById(R.id.fragment_toolbar);
        if (toolbar instanceof AppBaseFragment) {
            toolbarFragment = (ToolbarFragment) toolbar;
            toolbarFragment.initializeComponent();
        }

        tv_email = findViewById(R.id.tv_email);
        et_password = findViewById(R.id.et_password);
        tv_login = findViewById(R.id.tv_login);
        tv_forgot_password = findViewById(R.id.tv_forgot_password);
        tv_login_mobile = findViewById(R.id.tv_login_mobile);
        cb_password_show_hide = findViewById(R.id.cb_password_show_hide);

        tv_login.setOnClickListener(this);
        tv_forgot_password.setOnClickListener(this);
        tv_login_mobile.setOnClickListener(this);

        tv_email.setText(getEmail());

        setupSignInButton();

        cb_password_show_hide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else {
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                et_password.setCustomFont(getString(R.string.app_font_regular));

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.tv_login:
                callLogin();
                break;

            case R.id.tv_forgot_password:
                callForgotPassword();
                break;
            case R.id.tv_login_mobile:
                finish();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    private void setupSignInButton() {
        String signUp = "Log in";
        tv_login_mobile.setMovementMethod(LinkMovementMethod.getInstance());
        Pattern termsAndConditionsMatcher = Pattern.compile(signUp);
        Matcher m1 = termsAndConditionsMatcher.matcher(tv_login_mobile.getText().toString());
        if (m1.find()) {
            URLSpan urlSpan = new URLSpan(m1.group(0)) {
                @Override
                public void onClick(View widget) {
                    onBackPressed();
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setColor(getColorFromStyle(R.attr.app_link_text_color));
                    ds.setUnderlineText(false);
                }
            };
            SpannableString string = SpannableString.valueOf(tv_login_mobile.getText());
            string.setSpan(urlSpan, m1.start(), m1.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private void goToForgotPasswordActivity(Bundle bundle) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
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

    private void goToForgotPasswordVerifyActivity(Bundle bundle) {
        Intent intent = new Intent(this, ForgotPasswordVerifyActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, REQUEST_FORGOT_PASSWORD_OTP_VERIFY);
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    private void callForgotPassword() {
        ForgotPasswordRequestModel requestModel = new ForgotPasswordRequestModel();
        requestModel.email = getEmail();
        displayProgressBar(false, "Wait...");
        getWebRequestHelper().forgotPassword(requestModel, this);
    }


    private void callLogin() {
        String password = et_password.getText().toString();
        if (password.isEmpty()) {
            showErrorMsg("Please enter password.");
            return;
        }

        LoginRequestModel requestModel = new LoginRequestModel();
        requestModel.email = getEmail();
        requestModel.password = password;

        displayProgressBar(false, "Wait...");
        getWebRequestHelper().login(requestModel, this);
    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        dismissProgressBar();
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_LOGIN:
                handleLoginResponse(webRequest);
                break;
            case ID_FORGOT_PASSWORD:
                handleForgotPasswordResponse(webRequest);
                break;
        }

    }

    private void handleLoginResponse(WebRequest webRequest) {
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
            showErrorMsg(responsePojo.getMessage());
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
            showCustomToast(responsePojo.getMessage());
            goToForgotPasswordVerifyActivity(bundle);
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FORGOT_PASSWORD_OTP_VERIFY) {
            if (resultCode == RESULT_OK) {

            }
        }
    }


}
