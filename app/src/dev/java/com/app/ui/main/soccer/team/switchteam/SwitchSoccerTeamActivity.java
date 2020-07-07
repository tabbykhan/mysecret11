package com.app.ui.main.soccer.team.switchteam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.model.CustomerTeamModel;
import com.app.model.MatchModel;
import com.app.model.webrequestmodel.SwitchTeamRequestModel;
import com.app.model.webresponsemodel.CustomerTeamsResponseModel;
import com.app.model.webresponsemodel.SwitchTeamResponseModel;
import com.app.ui.MatchTimerListener;
import com.app.ui.MyApplication;
import com.app.ui.dialogs.SelectTeamDialog;
import com.app.ui.dialogs.selection.DataDialog;
import com.app.ui.main.ToolbarFragment;
import com.app.ui.main.cricket.contest.ContestActivity;
import com.app.ui.main.cricket.dialogs.joinconfirmdialog.ConfirmationJoinContestDialog;
import com.app.ui.main.soccer.team.playerpreview.SoccerTeamPreviewDialog;
import com.app.ui.main.soccer.team.switchteam.adapter.SwitchSoccerTeamAdapter;
import com.google.gson.Gson;
import com.medy.retrofitwrapper.WebRequest;
import com.utilities.DeviceScreenUtil;
import com.utilities.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Vishnu Gupta on 7/1/19.
 */
public class SwitchSoccerTeamActivity extends AppBaseActivity implements MatchTimerListener {


    ToolbarFragment toolbarFragment;

    TextView tv_match_name;
    TextView tv_timer_time;
    TextView tv_heading;
    TextView tv_switch_team_name;
    ImageView iv_switch_team_name;
    LinearLayout ll_option_team_name;
    LinearLayout rl_bottom_lay;
    LinearLayout ll_new_team;
    TextView tv_create_new_team;
    LinearLayout ll_switch_team;
    TextView tv_switch_team;

    RecyclerView recycler_view;
    ProgressBar pb_data;

    SwitchSoccerTeamAdapter adapter;


    String alreadyJoinedTeams = "";
    String old_team_id = "";
    List<CustomerTeamModel> customerTeamModels = new ArrayList<>();

    ConfirmationJoinContestDialog confirmationJoinContestDialog;


    public MatchModel getMatchModel() {
        return MyApplication.getInstance().getSelectedMatch();
    }

    public long getMatchContestId() {
        Bundle extras = getIntent().getExtras();
        return (extras == null ? -1 : extras.getLong(DATA1, -1));
    }

    public String getAlreadyJoinedTeams() {
        Bundle extras = getIntent().getExtras();
        return (extras == null ? "" : extras.getString(DATA2, ""));
    }


    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().addMatchTimerListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MyApplication.getInstance().removeMatchTimerListener(this);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_soccer_switch_team;
    }


    @Override
    public void initializeComponent() {
        super.initializeComponent();
        if (getMatchModel() != null) {
            this.alreadyJoinedTeams = getAlreadyJoinedTeams();
            this.old_team_id = this.alreadyJoinedTeams.split(",")[0];
            Fragment toolbar = getFm().findFragmentById(R.id.fragment_toolbar);
            if (toolbar instanceof AppBaseFragment) {
                toolbarFragment = (ToolbarFragment) toolbar;
                toolbarFragment.initializeComponent();
                toolbarFragment.setToolbarView(this);
            }

            tv_match_name = findViewById(R.id.tv_match_name);
            tv_timer_time = findViewById(R.id.tv_timer_time);
            tv_heading = findViewById(R.id.tv_heading);
            ll_option_team_name = findViewById(R.id.ll_option_team_name);
            tv_switch_team_name = findViewById(R.id.tv_switch_team_name);
            iv_switch_team_name = findViewById(R.id.iv_switch_team_name);
            rl_bottom_lay = findViewById(R.id.rl_bottom_lay);

            ll_new_team = findViewById(R.id.ll_new_team);
            tv_create_new_team = findViewById(R.id.tv_create_new_team);

            ll_switch_team = findViewById(R.id.ll_switch_team);
            tv_switch_team = findViewById(R.id.tv_switch_team);

            recycler_view = findViewById(R.id.recycler_view);
            pb_data = findViewById(R.id.pb_data);
            updateViewVisibitity(pb_data, View.GONE);
            updateViewVisibitity(rl_bottom_lay, View.INVISIBLE);
            setHeading();
            setMatchData();
            initializeRecyclerView();
            getCustomerTeams();

            ll_option_team_name.setOnClickListener(this);
            ll_new_team.setOnClickListener(this);
            ll_switch_team.setOnClickListener(this);
        } else {
            onBackPressed();
        }
    }


    private void initializeRecyclerView() {
        adapter = new SwitchSoccerTeamAdapter(this, customerTeamModels) {
            @Override
            public int getLastItemBottomMargin() {
                return rl_bottom_lay.getHeight() + DeviceScreenUtil.getInstance().convertDpToPixel(10);
            }

            @Override
            public boolean isTeamAlreadyJoined(long teamId) {
                return alreadyJoinedTeams.contains(String.valueOf(teamId));
            }
        };
        recycler_view.setLayoutManager(getVerticalLinearLayoutManager());
        recycler_view.setAdapter(adapter);

        new ItemClickSupport(recycler_view).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                try {
                    CustomerTeamModel customerTeamModel = customerTeamModels.get(position);
                    switch (v.getId()) {
                        case R.id.ll_preview: {
                            openTeamPreviewDialog(customerTeamModel);
                        }
                        break;

                        default: {
                            if (!adapter.isTeamAlreadyJoined(customerTeamModel.getId())) {
                                adapter.setSelectedTeam(position);
                            }
                        }

                        break;
                    }
                } catch (Exception ignore) {

                }
            }
        });

    }

    private void setMatchData() {
        if (getMatchModel() != null) {
            tv_match_name.setText(getMatchModel().getShortName());
            tv_timer_time.setText(getMatchModel().getRemainTimeText());
            tv_timer_time.setTextColor(getResources().getColor(getMatchModel()
                    .getTimerColor()));
        }
    }

    private void setHeading() {
        if (alreadyJoinedTeams.split(",").length > 1) {
            updateViewVisibitity(iv_switch_team_name, View.VISIBLE);
        } else {
            updateViewVisibitity(iv_switch_team_name, View.GONE);
        }

        CustomerTeamModel customerTeamModel = new CustomerTeamModel();
        customerTeamModel.setId(Long.parseLong(old_team_id));
        int i = customerTeamModels.indexOf(customerTeamModel);
        if (i >= 0) {
            customerTeamModel = customerTeamModels.get(i);
        }
        tv_switch_team_name.setText("TEAM " + customerTeamModel.getName());

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_new_team: {
                goToCreateSoccerTeamActivity(null);
            }
            break;
            case R.id.ll_option_team_name: {
                showTeamsDialog();
            }
            break;
            case R.id.ll_switch_team: {
                CustomerTeamModel selectedTeam = adapter.getSelectedTeam();
                if (selectedTeam == null) {
                    showErrorMsg("Please select Team for switch");
                    return;
                }
                callSwitchTeam();
            }
            break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    private void showTeamsDialog() {
        if (alreadyJoinedTeams.split(",").length > 1) {
            hideKeyboard();
            final List<CustomerTeamModel> alreadySelectedTeam = new ArrayList<>();
            for (CustomerTeamModel customerTeamModel : customerTeamModels) {
                if (adapter.isTeamAlreadyJoined(customerTeamModel.getId())) {
                    alreadySelectedTeam.add(customerTeamModel);
                }
            }

            final SelectTeamDialog selectStateDialog = new SelectTeamDialog();
            selectStateDialog.setDataList(alreadySelectedTeam);
            selectStateDialog.setOnItemSelectedListeners(new DataDialog.OnItemSelectedListener() {
                @Override
                public void onItemSelectedListener(int position) {
                    selectStateDialog.dismiss();
                    old_team_id = String.valueOf(alreadySelectedTeam.get(position).getId());
                    setHeading();
                }
            });
            selectStateDialog.show(getFm(), selectStateDialog.getClass().getSimpleName());
        }

    }


    private void openTeamPreviewDialog(CustomerTeamModel customerTeamModel) {
        SoccerTeamPreviewDialog instance = SoccerTeamPreviewDialog.getInstance(null);
        instance.setCustomerTeamModel(customerTeamModel);
        instance.setTeamPreviewDialogListener(new SoccerTeamPreviewDialog.TeamPreviewDialogListener() {
            @Override
            public void needTeamEdit(CustomerTeamModel customerTeamModel) {
                Bundle bundle = new Bundle();
                bundle.putLong(DATA1, customerTeamModel.getId());
                bundle.putString(DATA, new Gson().toJson(customerTeamModel));
                goToCreateSoccerTeamActivity(bundle);
            }

        });
        instance.show(getFm(), instance.getClass().getSimpleName());

    }


    private void getCustomerTeams() {
        if (getMatchModel() != null) {
            updateViewVisibitity(pb_data, View.VISIBLE);
            getWebRequestHelper().getGetSoccerCustomerTeams(getMatchModel().getMatch_id(), this);
        }
    }

    private void callSwitchTeam() {
        if (getMatchModel() != null) {
            long matchContestId = getMatchContestId();
            if (matchContestId == -1) return;
            CustomerTeamModel selectedTeam = adapter.getSelectedTeam();
            if (selectedTeam == null) return;
            displayProgressBar(false);
            SwitchTeamRequestModel requestModel = new SwitchTeamRequestModel();
            requestModel.customer_team_id_old = Long.parseLong(old_team_id);
            requestModel.customer_team_id_new = selectedTeam.getId();
            requestModel.match_unique_id = getMatchModel().getMatch_id();
            requestModel.match_contest_id = matchContestId;
            getWebRequestHelper().customerSoccerSwitchTeam(requestModel, this);

        }
    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        updateViewVisibitity(pb_data, View.GONE);
        if (ID_CUSTOMER_SOCCER_SWITCH_TEAM == webRequest.getWebRequestId()) {
            dismissProgressBar();
        }
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_GET_CUSTOMER_SOCCER_TEAMS:
                handleCustomerTeamsResponse(webRequest);
                break;
            case ID_CUSTOMER_SOCCER_SWITCH_TEAM:
                handleSwitchTeamResponse(webRequest);
                break;
        }

    }

    private void handleSwitchTeamResponse(WebRequest webRequest) {
        SwitchTeamResponseModel responsePojo = webRequest.getResponsePojo(SwitchTeamResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            if (isFinishing()) return;
            showCustomToast(responsePojo.getMessage());
            setResult();
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }

    }

    private void handleCustomerTeamsResponse(WebRequest webRequest) {
        CustomerTeamsResponseModel responsePojo = webRequest.getResponsePojo(CustomerTeamsResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            List<CustomerTeamModel> data = responsePojo.getData();
            customerTeamModels.clear();
            if (data != null && data.size() > 0) {
                customerTeamModels.addAll(data);
            }
            if (isFinishing()) return;
            updateViewVisibitity(rl_bottom_lay, View.VISIBLE);
            tv_create_new_team.setText("CREATE TEAM " + String.valueOf(customerTeamModels.size() + 1));
            if (customerTeamModels.size() < getMatchModel().getMatch_limit()) {
                updateViewVisibitity(ll_new_team, View.VISIBLE);
            } else {
                updateViewVisibitity(ll_new_team, View.GONE);
            }
            adapter.notifyDataSetChanged();
            setHeading();
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }

    }


    @Override
    public void onMatchTimeUpdate() {
        setMatchData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ContestActivity.REQUEST_CREATE_TEAM) {
            if (resultCode == RESULT_OK) {
                getCustomerTeams();
            }
        }
    }


    private void setResult() {
        setResult(RESULT_OK);
        supportFinishAfterTransition();
    }
}
