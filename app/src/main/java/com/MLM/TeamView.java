package com.MLM;

import android.app.DatePickerDialog;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.CoustomControl.AppCommon;
import com.CoustomControl.AppService;
import com.CoustomControl.ResponseAndPojoClass.AuthEntitiyClass;
import com.CoustomControl.ResponseAndPojoClass.AuthResponse;
import com.CoustomControl.ResponseAndPojoClass.TeamEntity;
import com.CoustomControl.ResponseAndPojoClass.TeamViewResponse;
import com.CoustomControl.ServiceGenerator;
import com.R;
import com.app.appbase.AppBaseActivity;
import com.google.gson.Gson;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamView extends AppBaseActivity {

    TextView tv_fromdate;
    TextView tv_todate;
    EditText tv_userid;


    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_team_view;
    }

    @Override
    public void initializeComponent() {
        tv_fromdate = findViewById(R.id.tv_fromdate);
        tv_todate = findViewById(R.id.tv_todate);
        tv_userid = findViewById(R.id.tv_userid);
        //  dismissProgressBar();
        if (!AppCommon.getInstance(this).getToken().equals("")) {
            displayProgressBar(false, "Wait...");
            callGetTeamApi(new TeamEntity("0", "10", "", "", "", ""));
        } else {
            displayProgressBar(false, "Wait...");
            callToken();
        }

    }

    private void callToken() {
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            AppService apiService = ServiceGenerator.createService(AppService.class);
            //Call call = apiService.token_CALL(new AuthEntitiyClass(AppCommon.getInstance(this).getUserId(), AppCommon.getInstance(this).getPassword()));
            dismissProgressBar();
          //  Call call = apiService.token_CALL(new AuthEntitiyClass("vp235345@vp11.com", "123456"));
            Call call = apiService.token_CALL(new AuthEntitiyClass("test.side@gmail.com", "123456"));
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(TeamView.this).clearNonTouchableFlags(TeamView.this);
                    dismissProgressBar();
                    AuthResponse authResponse = (AuthResponse) response.body();
                    if (authResponse != null) {
                        Log.i("AuthResponse::", new Gson().toJson(authResponse));
                        if (authResponse.getCode() == 200) {
                            displayProgressBar(false, "Wait...");
                            AppCommon.getInstance(TeamView.this).setToken(authResponse.getData().getAccessToken());
                        } else {
                            Toast.makeText(TeamView.this, authResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
    }

    private void callGetTeamApi(TeamEntity teamEntity) {
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            //loaderView.setVisibility(View.VISIBLE);
            AppService apiService = ServiceGenerator.createService(AppService.class, AppCommon.getInstance(this).getToken());
            Call call = apiService.GetTeamView_CALL(teamEntity);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(TeamView.this).clearNonTouchableFlags(TeamView.this);
                    // loaderView.setVisibility(View.GONE);
                    dismissProgressBar();
                    TeamViewResponse teamViewResponse = (TeamViewResponse) response.body();
                    if (teamViewResponse != null) {
                        Log.i("teamViewResponse::", new Gson().toJson(teamViewResponse));
                        if (teamViewResponse.getCode() == 200) {
                            // getCallWalletApi();

                        } else {

                            Toast.makeText(TeamView.this, teamViewResponse.getMessage(), Toast.LENGTH_SHORT).show();

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
    }

    public void openFromCalender(View view) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        tv_fromdate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void openToCalender(View view) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        tv_todate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    public void filter(View view) {
        String fromDate = tv_fromdate.getText().toString().trim();
        String toDate = tv_todate.getText().toString().trim();
        String userId = tv_userid.getText().toString().trim();
        displayProgressBar(false, "Wait...");
        callGetTeamApi(new TeamEntity("0", "10", userId, fromDate, toDate, "All"));

    }
}
