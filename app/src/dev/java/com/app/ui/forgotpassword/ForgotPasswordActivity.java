package com.app.ui.forgotpassword;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.model.webrequestmodel.ForgotPasswordRequestModel;
import com.app.model.webresponsemodel.ForgotPasswordResponseModel;
import com.app.ui.main.ToolbarFragment;
import com.medy.retrofitwrapper.WebRequest;

import java.util.regex.Pattern;

/**
 * @author Manish Kumar
 * @since 1/10/18
 */

public class ForgotPasswordActivity extends AppBaseActivity {

    public static final int REQUEST_FORGOT_PASSWORD_OTP_VERIFY = 102;

    ToolbarFragment toolbarFragment;

    EditText et_mobile;
    TextView tv_proceed;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_forgot_password;
    }


    @Override
    public void initializeComponent() {
        super.initializeComponent();

        Fragment toolbar = getFm().findFragmentById(R.id.fragment_toolbar);
        if (toolbar instanceof AppBaseFragment) {
            toolbarFragment = (ToolbarFragment) toolbar;
            toolbarFragment.initializeComponent();
        }

        et_mobile = findViewById(R.id.et_mobile);
        tv_proceed = findViewById(R.id.tv_proceed);
        tv_proceed.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_proceed:
                callForgotPassword();
                break;

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
        String mobileNumber = et_mobile.getText().toString().trim();
        if (mobileNumber.isEmpty()) {
            showErrorMsg("Please enter mobile number.");
            return;
        }

        if (!Pattern.matches("[0-9]{10}", mobileNumber)) {
            showErrorMsg("Please enter valid mobile number.");
            return;
        }

        ForgotPasswordRequestModel requestModel = new ForgotPasswordRequestModel();
        requestModel.phone = mobileNumber;
        requestModel.country_mobile_code = COUNTRY_MOBILE_CODE_VALUE;
        displayProgressBar(false, "Wait...");
        getWebRequestHelper().forgotPassword(requestModel, this);
    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        dismissProgressBar();
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_FORGOT_PASSWORD:
                handleForgotPasswordResponse(webRequest);
                break;
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
            bundle.putString(VERIFY_FROM, "F");
            bundle.putString(OTP, data.getOtp());
            bundle.putString(PHONE, data.getPhone());
            bundle.putString(COUNTRY_MOBILE_CODE, data.getCountry_mobile_code());
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
                supportFinishAfterTransition();
            }
        }
    }


}
