package com.app.ui.main.dashboard.profile.changepassword;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.model.UserModel;
import com.app.model.webrequestmodel.UpdateProfileRequestModel;
import com.app.model.webresponsemodel.VerifyOtpResponseModel;
import com.app.ui.main.ToolbarFragment;
import com.medy.retrofitwrapper.WebRequest;


/**
 * Created by Vishnu Gupta on 7/1/19.
 */
public class ChangePasswordActivity extends AppBaseActivity {

    ToolbarFragment toolbarFragment;

    TextView tv_name;
    TextView tv_phone;
    ImageView iv_player;
    ProgressBar pb_image;


    EditText et_old_password;
    EditText et_new_password;
    EditText et_confirm_password;

    TextView tv_update;


    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_change_password;
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();
        Fragment toolbar = getFm().findFragmentById(R.id.fragment_toolbar);
        if (toolbar != null && toolbar instanceof AppBaseFragment) {
            toolbarFragment = (ToolbarFragment) toolbar;
            toolbarFragment.initializeComponent();
            toolbarFragment.setToolbarView(this);
        }

        tv_name = findViewById(R.id.tv_name);
        tv_phone = findViewById(R.id.tv_phone);
        iv_player = findViewById(R.id.iv_player);
        pb_image = findViewById(R.id.pb_image);
        et_old_password = findViewById(R.id.et_old_password);
        et_new_password = findViewById(R.id.et_new_password);
        et_confirm_password = findViewById(R.id.et_confirm_password);

        tv_update = findViewById(R.id.tv_update);

        tv_update.setOnClickListener(this);

        updateUserData();

    }

    public void updateUserData() {
        UserModel userModel = getUserModel();
        if (userModel == null) {
            tv_name.setText("");
            tv_phone.setText("");
            iv_player.setImageResource(R.drawable.no_image);
            updateViewVisibitity(pb_image, View.GONE);
        } else {
            tv_name.setText(userModel.getFullName());
            tv_phone.setText(userModel.getFullMobile());
            loadImage(this,
                    iv_player, pb_image, userModel.getImage(), R.drawable.no_image);

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_update:
                callUpdate();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }


    private void callUpdate() {
        UserModel userModel = getUserModel();
        if (userModel == null) return;
        String oldPassword = et_old_password.getText().toString().trim();
        String newPassword = et_new_password.getText().toString().trim();
        String confirmNewPassword = et_confirm_password.getText().toString().trim();

        if (oldPassword.isEmpty()) {
            showErrorMsg("Please enter old password.");
            return;
        }
        if (newPassword.isEmpty()) {
            showErrorMsg("Please enter new password.");
            return;
        }

        if (confirmNewPassword.isEmpty()) {
            showErrorMsg("Please enter confirm password.");
            return;
        }

        if (newPassword.length() < 6) {
            showErrorMsg("New password should be atleast 6 char.");
            return;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            showErrorMsg("Confirm password not match.");
            return;
        }

        UpdateProfileRequestModel requestModel = new UpdateProfileRequestModel();
        requestModel.old_password = oldPassword;
        requestModel.password = newPassword;

        displayProgressBar(false, "Wait...");
        getWebRequestHelper().changePassword(requestModel, this);
    }


    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        dismissProgressBar();
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_CHANGE_PASSWORD:
                handleChangePassword(webRequest);
                break;
        }
    }


    private void handleChangePassword(WebRequest webRequest) {
        VerifyOtpResponseModel responsePojo = webRequest.getResponsePojo(VerifyOtpResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            showCustomToast(responsePojo.getMessage());
            setResult();
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }
    }

    private void setResult() {
        setResult(Activity.RESULT_OK);
        supportFinishAfterTransition();
    }

}
