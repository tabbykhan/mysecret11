package com.app.ui.main.soccer.team.chooseteam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
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
import com.app.model.UserModel;
import com.app.model.webrequestmodel.CreatePrivateContestRequestModel;
import com.app.model.webrequestmodel.CustomerJoinContestRequestModel;
import com.app.model.webresponsemodel.CustomerJoinContestResponseModel;
import com.app.model.webresponsemodel.CustomerTeamsResponseModel;
import com.app.ui.MatchTimerListener;
import com.app.ui.MyApplication;
import com.app.ui.main.ToolbarFragment;
import com.app.ui.main.cricket.contest.ContestActivity;
import com.app.ui.main.soccer.dialog.ConfirmationSoccerJoinContestDialog;
import com.app.ui.main.soccer.team.chooseteam.adapter.ChooseSoccerTeamAdapter;
import com.app.ui.main.soccer.team.playerpreview.SoccerTeamPreviewDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.medy.retrofitwrapper.WebRequest;
import com.utilities.DeviceScreenUtil;
import com.utilities.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Vishnu Gupta on 7/1/19.
 */
public class ChooseSoccerTeamActivity extends AppBaseActivity implements MatchTimerListener {


    ToolbarFragment toolbarFragment;

    TextView tv_match_name;
    TextView tv_timer_time;
    TextView tv_heading;
    LinearLayout rl_bottom_lay;
    LinearLayout ll_new_team;
    TextView tv_create_new_team;
    LinearLayout ll_join_contest;
    TextView tv_join_contest;

    RecyclerView recycler_view;
    ProgressBar pb_data;

    ChooseSoccerTeamAdapter adapter;

    LinearLayout ll_select_all;
    CheckBox cb_select_all;


    String alreadyJoinedTeams = "";
    List<CustomerTeamModel> customerTeamModels = new ArrayList<>();

    ConfirmationSoccerJoinContestDialog confirmationJoinContestDialog;


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

    public String getPrivateContestRequestModel() {
        Bundle extras = getIntent().getExtras();
        return (extras == null ? "" : extras.getString(DATA3, ""));
    }

    public String getEntryFeeSuggestString() {
        Bundle extras = getIntent().getExtras();
        return (extras == null ? "" : extras.getString(DATA4, ""));
    }

    public List<String> getEntryFeeSuggest() {
        Bundle extras = getIntent().getExtras();
        String Siggest = (extras == null ? "" : extras.getString(DATA4, ""));
        if (isValidString(Siggest)) {
            return new Gson().fromJson(Siggest, new TypeToken<List<String>>() {
            }.getType());
        }
        return new ArrayList<>();
    }

    public String getEntryFee() {
        Bundle extras = getIntent().getExtras();
        return (extras == null ? "" : extras.getString(DATA5, ""));
    }

    public String getMaxEntryFee() {
        Bundle extras = getIntent().getExtras();
        return (extras == null ? "" : extras.getString(DATA6, ""));
    }

    public String getMultiplier() {
        Bundle extras = getIntent().getExtras();
        return (extras == null ? "" : extras.getString(DATA7, ""));
    }

    public String getMoreEntryFree() {
        Bundle extras = getIntent().getExtras();
        return (extras == null ? "" : extras.getString(DATA8, ""));
    }

    public boolean isMultiJoinAllowed() {
        Bundle extras = getIntent().getExtras();
        return (extras != null && extras.getBoolean(DATA9, false));
    }

    public CreatePrivateContestRequestModel getPrivateContestRequestModelData() {
        String string = getPrivateContestRequestModel();
        if (isValidString(string)) {
            return new Gson().fromJson(string, CreatePrivateContestRequestModel.class);
        }

        return null;
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
        return R.layout.activity_choose_soccer_team;
    }


    @Override
    public void initializeComponent() {
        super.initializeComponent();
        if (getMatchModel() != null) {
            this.alreadyJoinedTeams = getAlreadyJoinedTeams();
            Fragment toolbar = getFm().findFragmentById(R.id.fragment_toolbar);
            if (toolbar instanceof AppBaseFragment) {
                toolbarFragment = (ToolbarFragment) toolbar;
                toolbarFragment.initializeComponent();
                toolbarFragment.setToolbarView(this);
            }

            ll_select_all = findViewById(R.id.ll_select_all);
            cb_select_all = findViewById(R.id.cb_select_all);


            if (isMultiJoinAllowed()) {
                updateViewVisibitity(ll_select_all, View.VISIBLE);
                cb_select_all.setOnClickListener(this);
                ll_select_all.setOnClickListener(this);
            } else {
                updateViewVisibitity(ll_select_all, View.GONE);
                cb_select_all.setOnClickListener(null);
                ll_select_all.setOnClickListener(null);
            }

            tv_match_name = findViewById(R.id.tv_match_name);
            tv_timer_time = findViewById(R.id.tv_timer_time);
            tv_heading = findViewById(R.id.tv_heading);
            rl_bottom_lay = findViewById(R.id.rl_bottom_lay);

            ll_new_team = findViewById(R.id.ll_new_team);
            tv_create_new_team = findViewById(R.id.tv_create_new_team);

            ll_join_contest = findViewById(R.id.ll_join_contest);
            tv_join_contest = findViewById(R.id.tv_join_contest);

            recycler_view = findViewById(R.id.recycler_view);
            pb_data = findViewById(R.id.pb_data);
            updateViewVisibitity(pb_data, View.GONE);
            updateViewVisibitity(rl_bottom_lay, View.INVISIBLE);
            setHeading();
            setMatchData();
            initializeRecyclerView();
            getCustomerTeams();

            ll_new_team.setOnClickListener(this);
            ll_join_contest.setOnClickListener(this);
        } else {
            onBackPressed();
        }
    }


    private void initializeRecyclerView() {
        adapter = new ChooseSoccerTeamAdapter(this, customerTeamModels) {
            @Override
            public int getLastItemBottomMargin() {
                return rl_bottom_lay.getHeight() + DeviceScreenUtil.getInstance().convertDpToPixel(10);
            }

            @Override
            public boolean isTeamAlreadyJoined(long teamId) {
                return alreadyJoinedTeams.contains(String.valueOf(teamId));
            }

            @Override
            public boolean isMultiJoinAllowed() {
                return ChooseSoccerTeamActivity.this.isMultiJoinAllowed();
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
                                cb_select_all.setChecked(adapter.isAllTeamSelected());
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
        if (alreadyJoinedTeams.trim().isEmpty()) {
            tv_heading.setText("CHOOSE TEAMS TO JOIN THIS CONTEST WITH");
            tv_join_contest.setText("JOIN CONTEST");
        } else {
            tv_heading.setText("CHOOSE TEAMS TO REJOIN THIS CONTEST WITH");
            tv_join_contest.setText("REJOIN");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_new_team: {
                goToCreateSoccerTeamActivity(null);
            }
            break;
            case R.id.ll_join_contest: {
                List<CustomerTeamModel> selectedTeam = adapter.getSelectedTeams();
                if (selectedTeam == null || selectedTeam.size()==0) {
                    showErrorMsg("Please select Team");
                    return;
                }
                String enterFree = getEntryFee();
                String maxEnterFree = getMaxEntryFee();
                openConfirmJoinContest("");
            }
            break;

            case R.id.ll_select_all:
            case R.id.cb_select_all:
                boolean allTeamSelected = adapter.isAllTeamSelected();
                if (allTeamSelected) {
                    adapter.unSelectAll();
                    cb_select_all.setChecked(false);
                } else {
                    adapter.selectAll();
                    cb_select_all.setChecked(true);
                }


                break;

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }


    private void openConfirmJoinContest(final String enterFree) {
        List<CustomerTeamModel> selectedTeamData = adapter.getSelectedTeams();
        String selectedTeam="";
        for (CustomerTeamModel selectedTeamDatum : selectedTeamData) {
            if(selectedTeam.equals("")){
                selectedTeam=String.valueOf(selectedTeamDatum.getId());
            }else{
                selectedTeam+=","+String.valueOf(selectedTeamDatum.getId());
            }
        }


        long matchContestId = getMatchContestId();
        if (matchContestId == -1) return;
        Bundle bundle = new Bundle();
        bundle.putLong(DATA, getMatchContestId());
        bundle.putString(DATA2, getPrivateContestRequestModel());
        bundle.putString(DATA3, selectedTeam);
        bundle.putString(DATA4, enterFree);
        confirmationJoinContestDialog = ConfirmationSoccerJoinContestDialog.getInstance(bundle);
        confirmationJoinContestDialog.setConfirmationJoinContestListener(new ConfirmationSoccerJoinContestDialog.ConfirmationJoinContestListener() {
            @Override
            public void onProceed() {
                callJoinContest(enterFree);
            }

            @Override
            public void lowBalance(CustomerJoinContestResponseModel.PreJoinedModel preJoinedModel) {
                confirmationJoinContestDialog.dismiss();
                Bundle bundle = new Bundle();
                bundle.putString(DATA, new Gson().toJson(preJoinedModel));
                bundle.putString(DATA3, enterFree);
                goToAddCashActivity(bundle, ContestActivity.REQUEST_LOW_BALANCE);
            }
        });
        confirmationJoinContestDialog.show(getFm(), confirmationJoinContestDialog.getClass().getSimpleName());

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

    private void callJoinContest(String enterFree) {
        if (getMatchModel() != null) {
            List<CustomerTeamModel> selectedTeamData = adapter.getSelectedTeams();
            String selectedTeam="";
            for (CustomerTeamModel selectedTeamDatum : selectedTeamData) {
                if(selectedTeam.equals("")){
                    selectedTeam=String.valueOf(selectedTeamDatum.getId());
                }else{
                    selectedTeam+=","+String.valueOf(selectedTeamDatum.getId());
                }
            }

            if (!isValidString(selectedTeam)) return;

            CreatePrivateContestRequestModel privateContestRequestModel = getPrivateContestRequestModelData();
            if (privateContestRequestModel != null) {
                privateContestRequestModel.pre_join = "N";
                privateContestRequestModel.team_id = selectedTeam;
                displayProgressBar(false);
                getWebRequestHelper().createSoccerPrivateContest(privateContestRequestModel, this);
                return;
            }

            long matchContestId = getMatchContestId();
            if (matchContestId == -1) return;

            displayProgressBar(false);
            CustomerJoinContestRequestModel requestModel = new CustomerJoinContestRequestModel();
            requestModel.customer_team_id = selectedTeam;
            requestModel.match_unique_id = getMatchModel().getMatch_id();
            requestModel.match_contest_id = matchContestId;
            if (isValidString(enterFree)) {
                requestModel.entry_fees = enterFree;
            }
            getWebRequestHelper().customerSoccerJoinContest(requestModel, this);

        }
    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        updateViewVisibitity(pb_data, View.GONE);
        if (ID_CUSTOMER_SOCCER_JOIN_CONTEST == webRequest.getWebRequestId()) {
            dismissProgressBar();
        }
        if (ID_CREATE_SOCCER__PRIVATE_CONTEST == webRequest.getWebRequestId()) {
            dismissProgressBar();
        }
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_GET_CUSTOMER_SOCCER_TEAMS:
                handleCustomerTeamsResponse(webRequest);
                break;
            case ID_CUSTOMER_SOCCER_JOIN_CONTEST:
                handleCustomerJoinContestResponse(webRequest);
                break;
            case ID_CREATE_SOCCER__PRIVATE_CONTEST:
                handleCustomerJoinContestResponse(webRequest);
                break;
        }

    }

    private void handleCustomerJoinContestResponse(WebRequest webRequest) {
        CustomerJoinContestResponseModel responsePojo = webRequest.getResponsePojo(CustomerJoinContestResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            if (isFinishing()) return;
            showCustomToast(responsePojo.getMessage());
            if (confirmationJoinContestDialog != null && confirmationJoinContestDialog.getDialog() != null &&
                    confirmationJoinContestDialog.getDialog().isShowing()) {
                confirmationJoinContestDialog.dismiss();
            }
            UserModel userModel = getUserModel();
            if (responsePojo.getData() != null && responsePojo.getData().getWallet() != null && userModel != null) {
                userModel.setWallet(responsePojo.getData().getWallet());
                updateUserInPrefs();
            }
            setResult(responsePojo.getMatch_contest_id());
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
        } else if (requestCode == ContestActivity.REQUEST_LOW_BALANCE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                String entryFee = extras.getString(DATA2, "");
                openConfirmJoinContest(entryFee);
            }
        }
    }


    private void setResult(String matchContestId) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(DATA, matchContestId);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        supportFinishAfterTransition();
    }
}
