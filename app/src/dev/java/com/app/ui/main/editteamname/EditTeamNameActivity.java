package com.app.ui.main.editteamname;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.model.UserModel;
import com.app.model.webrequestmodel.TeamNameUpdateRequestModel;
import com.app.model.webresponsemodel.UpdateTeamNameResponseModel;
import com.app.ui.main.ToolbarFragment;
import com.medy.retrofitwrapper.WebRequest;

/**
 * @author Manish Kumar
 * @since 1/10/18
 */

public class EditTeamNameActivity extends AppBaseActivity {


    ToolbarFragment toolbarFragment;

    EditText et_team_name;
    TextView tv_team_name;
    TextView tv_proceed;


    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_edit_team_name;
    }


    @Override
    public void initializeComponent() {
        super.initializeComponent();

        Fragment toolbar = getFm().findFragmentById(R.id.fragment_toolbar);
        if (toolbar instanceof AppBaseFragment) {
            toolbarFragment = (ToolbarFragment) toolbar;
            toolbarFragment.initializeComponent();
        }

        et_team_name = findViewById(R.id.et_team_name);
        tv_team_name = findViewById(R.id.tv_team_name);

        tv_proceed = findViewById(R.id.tv_proceed);
        tv_proceed.setOnClickListener(this);
        updateUserData();

    }

    public void updateUserData() {
        UserModel userModel = getUserModel();
        if (userModel == null) {
            tv_team_name.setText("");
        } else {
            tv_team_name.setText(userModel.getTeam_name());
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_proceed:
                callUpdateTeamName();
                break;

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }


    private void callUpdateTeamName() {
        String teamName = et_team_name.getText().toString().trim();

        if (!isValidString(teamName)) {
            showErrorMsg("Please enter team name");
            return;
        }
        if (teamName.trim().length() < 6) {
            showErrorMsg("Team name should be minimum 6 characters");
            return;
        }

        TeamNameUpdateRequestModel requestModel = new TeamNameUpdateRequestModel();
        requestModel.team_name = teamName;
        displayProgressBar(false);
        getWebRequestHelper().customerTeamNameUpdate(requestModel, this);
    }


    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        dismissProgressBar();
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_UPDATE_TEAM_NAME:
                handleTeamNameUpdateResponse(webRequest);
                break;
        }

    }

    private void handleTeamNameUpdateResponse(WebRequest webRequest) {
        UpdateTeamNameResponseModel responsePojo = webRequest.getResponsePojo(UpdateTeamNameResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            if (isFinishing()) return;
            UserModel userModel = getUserModel();
            if (userModel == null) return;
            userModel.setTeam_name(et_team_name.getText().toString().trim());
            userModel.setTeam_change("Y");
            updateUserInPrefs();
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
