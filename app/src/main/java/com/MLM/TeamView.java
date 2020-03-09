package com.MLM;

import com.R;
import com.base.BaseActivity;

public class TeamView extends BaseActivity {



    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_team_view;
    }

    @Override
    public void initializeComponent() {

    }

  /*  private void callGetTeamApi() {
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            //loaderView.setVisibility(View.VISIBLE);
            AppService apiService = ServiceGenerator.createService(AppService.class);
            Call call = apiService.REGISTRATION_RESPONSE_CLASS_CALL(registerRequestModel);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(TeamView.this).clearNonTouchableFlags(TeamView.this);
                    // loaderView.setVisibility(View.GONE);
                    dismissProgressBar();
                    RegistrationMLMResponseClass registrationMLMResponseClass = (RegistrationMLMResponseClass) response.body();
                    if (registerRequestModel != null) {
                        Log.i("roiWithdrawalResponse::", new Gson().toJson(registrationMLMResponseClass));
                        if (registrationMLMResponseClass.getCode() == 200 ) {
                            // getCallWalletApi();
                            displayProgressBar(false, "Wait...");
                            getWebRequestHelper().newUser(requestModel, TeamView.this);
                            AppCommon.getInstance(TeamView.this).setUserObject(new Gson().toJson(registerRequestModel));
                        } else {

                            Toast.makeText(TeamView.this, registrationMLMResponseClass.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        AppCommon.getInstance(TeamView.this).showDialog(TeamView.this, "Server Error");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    dismissProgressBar();
                    AppCommon.getInstance(TeamView.this).clearNonTouchableFlags(TeamView.this);
                    // loaderView.setVisibility(View.GONE);
                    Toast.makeText(TeamView.this, "Please check your internet", Toast.LENGTH_SHORT).show();
                }
            });



        } else {
            // no internet
            dismissProgressBar();
            Toast.makeText(this, "Please check your internet", Toast.LENGTH_SHORT).show();
        }
    }*/
}
