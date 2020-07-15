package com.app.ui.main.soccer.contest.joinprivatecontest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.model.ContestCategoryModel;
import com.app.model.ContestModel;
import com.app.model.ContestWinnerBreakUpModel;
import com.app.model.MatchModel;
import com.app.model.UserModel;
import com.app.model.webrequestmodel.CustomerJoinContestRequestModel;
import com.app.model.webresponsemodel.AlreadyCreatedTeamCountResponseModel;
import com.app.model.webresponsemodel.CustomerJoinContestResponseModel;
import com.app.model.webresponsemodel.JoinPrivateContestResponseModel;
import com.app.ui.MatchTimerListener;
import com.app.ui.MyApplication;
import com.app.ui.dialogs.ConfirmationDialog;
import com.app.ui.main.ToolbarFragment;
import com.app.ui.main.cricket.contest.ContestActivity;
import com.app.ui.main.cricket.dialogs.joinconfirmdialog.ConfirmationJoinContestDialog;
import com.app.ui.main.soccer.contest.contestdetail.SoccerContestDetailActivity;
import com.app.ui.main.soccer.contest.joinprivatecontest.adapter.JoinSoccerPrivateContestAdapter;
import com.google.gson.Gson;
import com.medy.retrofitwrapper.WebRequest;

import java.util.List;

public class JoinSoccerPrivateContestActivity extends AppBaseActivity implements MatchTimerListener {

    ToolbarFragment toolbarFragment;

    TextView tv_match_name;
    TextView tv_timer_time;

    TextView tv_contest_size;
    TextView tv_contest_prize_size;
    TextView tv_contest_entry_fee;
    TextView tv_winner_type;
    TextView tv_winner_type_recomd;

    RecyclerView recycler_view;

    LinearLayout ll_join_contest;


    ContestModel contestModel;
    ContestWinnerBreakUpModel contestWinnerBreakUpModel;

    ConfirmationJoinContestDialog confirmationJoinContestDialog;
    JoinSoccerPrivateContestAdapter adapter;

    public MatchModel getMatchModel() {
        return MyApplication.getInstance().getSelectedMatch();
    }


    private String getSlug() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            return extras.getString(PRIVATE_SLUG, "");
        }
        return "";
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
        return R.layout.activity_join_private_contest;
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();

        Fragment toolbar = getFm().findFragmentById(R.id.fragment_toolbar);
        if (toolbar instanceof AppBaseFragment) {
            toolbarFragment = (ToolbarFragment) toolbar;
            toolbarFragment.initializeComponent();
            toolbarFragment.setToolbarView(this);
        }

        tv_match_name = findViewById(R.id.tv_match_name);
        tv_timer_time = findViewById(R.id.tv_timer_time);

        tv_contest_size = findViewById(R.id.tv_contest_size);
        tv_contest_prize_size = findViewById(R.id.tv_contest_prize_size);
        tv_contest_entry_fee = findViewById(R.id.tv_contest_entry_fee);
        tv_winner_type = findViewById(R.id.tv_winner_type);
        tv_winner_type_recomd = findViewById(R.id.tv_winner_type_recomd);
        recycler_view = findViewById(R.id.recycler_view);
        ll_join_contest = findViewById(R.id.ll_join_contest);

        ll_join_contest.setOnClickListener(this);

        setMatchData();

        callGetContestDetail();


    }

    @Override
    public void onMatchTimeUpdate() {
        setMatchData();
    }

    private void setupContestData() {
        if (contestModel != null) {
            tv_contest_size.setText(String.valueOf(contestModel.getTotal_team()));
            tv_contest_prize_size.setText(currency_symbol + " " + contestModel.getTotalPriceText());
            tv_contest_entry_fee.setText(currency_symbol + " " + contestModel.getEntryFeesText());
        } else {
            tv_contest_size.setText("");
            tv_contest_prize_size.setText("");
            tv_contest_entry_fee.setText("");
        }
        setupRecyclerView();
    }

    private void setMatchData() {
        if (getMatchModel() != null) {
            tv_match_name.setText(getMatchModel().getShortName());
            tv_timer_time.setText(getMatchModel().getRemainTimeText());
            tv_timer_time.setTextColor(getResources().getColor(getMatchModel()
                    .getTimerColor()));
        } else {
            tv_match_name.setText("");
            tv_timer_time.setText("");
        }
    }

    private void setupRecyclerView() {
        recycler_view.setLayoutManager(getVerticalLinearLayoutManager());
        adapter = new JoinSoccerPrivateContestAdapter(this, contestWinnerBreakUpModel);
        recycler_view.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_join_contest:
                getAlreadyCreatedTeamCount();
                break;
        }
    }

    private void callGetContestDetail() {
        String match_unique_id="0";
        MatchModel matchModel = getMatchModel();
        if(matchModel!=null){
            match_unique_id=matchModel.getMatch_id();
        }
        displayProgressBar(false);
        getWebRequestHelper().getSoccerMatchPrivateContestDetail(getSlug(),match_unique_id, this);
    }

    private void getAlreadyCreatedTeamCount() {
        displayProgressBar(false);
        MatchModel matchModel = getMatchModel();
        if (matchModel == null) return;
        getWebRequestHelper().getSoccerAlreadyCreatedTeamCount(matchModel.getMatch_id(), this);

    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        dismissProgressBar();
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_GET_SOCCER_ALREADY_CREATED_TEAM_COUNT:
                handleAlreadyCreatedTeamCount(webRequest);
                break;
            case ID_CUSTOMER_SOCCER_JOIN_CONTEST:
                handleCustomerJoinContestResponse(webRequest);
                break;
            case ID_GET_SOCCER_MATCH_PRIVATE_CONTEST_DETAIL:
                handlePrivateContestDetail(webRequest);
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
            finish();
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }

    }

    private void handleAlreadyCreatedTeamCount(WebRequest webRequest) {
        AlreadyCreatedTeamCountResponseModel responsePojo = webRequest.getResponsePojo(AlreadyCreatedTeamCountResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            if (responsePojo.getData() > 0) {
                Bundle bundle = new Bundle();
                bundle.putLong(DATA1, contestModel.getMatch_contest_id());
                bundle.putString(DATA2, contestModel.getJoined_teams());
                bundle.putBoolean(DATA9, contestModel.isMultiTeamAllowed());
                if (contestModel.isBetTheExpert()) {
                    bundle.putString((DATA4), new Gson().toJson(contestModel.getEntry_fees_suggest()));
                    bundle.putString((DATA5), String.valueOf(contestModel.getEntry_fees()));
                    bundle.putString((DATA6), String.valueOf(contestModel.getMax_entry_fees()));
                    bundle.putString((DATA7), String.valueOf(contestModel.getEntry_fee_multiplier()));
                    bundle.putString((DATA8), String.valueOf(contestModel.getMore_entry_fees()));
                }
                goToSoccerChooseTeamActivity(bundle);
            } else {
                Bundle bundle = new Bundle();
                bundle.putLong(DATA2, contestModel.getMatch_contest_id());
                if (contestModel.isBetTheExpert()) {
                    bundle.putString((DATA4), new Gson().toJson(contestModel.getEntry_fees_suggest()));
                    bundle.putString((DATA5), String.valueOf(contestModel.getEntry_fees()));
                    bundle.putString((DATA6), String.valueOf(contestModel.getMax_entry_fees()));
                    bundle.putString((DATA7), String.valueOf(contestModel.getEntry_fee_multiplier()));
                    bundle.putString((DATA8), String.valueOf(contestModel.getMore_entry_fees()));
                }
                goToCreateSoccerTeamActivity(bundle);
            }
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ContestActivity.REQUEST_CREATE_TEAM) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getExtras() != null) {
                    Bundle extras = data.getExtras();
                    long matchContestId = extras.getLong(DATA2, -1);
                    long teamId = extras.getLong(DATA1, -1);

                    String entrySuggest = extras.getString(DATA4, "");
                    String minEntry = extras.getString(DATA5, "");
                    String maxEntry = extras.getString(DATA6, "");
                    String multipler = extras.getString(DATA7, "");
                    String moreFree = extras.getString(DATA8, "");
                    openConfirmJoinContest(minEntry, matchContestId, teamId);
                }
            }
        } else if (requestCode == ContestActivity.REQUEST_JOIN_CONTEST) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        } else if (requestCode == ContestActivity.REQUEST_SWITCH_TEAM) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        } else if (requestCode == ContestActivity.REQUEST_LOW_BALANCE) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getExtras() != null) {
                    Bundle extras = data.getExtras();
                    long matchContestId = extras.getLong(DATA, -1);
                    long teamId = extras.getLong(DATA1, -1);
                    String entryFee = extras.getString(DATA2, "");
                    openConfirmJoinContest(entryFee, matchContestId, teamId);
                }
            }
        }
    }



    private void openConfirmJoinContest(final String entryFee, final long matchContestId, final long teamId) {
        if (matchContestId == -1 || teamId == -1) return;
        Bundle bundle = new Bundle();
        bundle.putLong(DATA, matchContestId);
        bundle.putString(DATA4, entryFee);
        bundle.putString(DATA3, String.valueOf(teamId));
        confirmationJoinContestDialog = ConfirmationJoinContestDialog.getInstance(bundle);
        confirmationJoinContestDialog.setConfirmationJoinContestListener(new ConfirmationJoinContestDialog.ConfirmationJoinContestListener() {
            @Override
            public void onProceed() {
                callJoinContest(entryFee, matchContestId, teamId);
            }

            @Override
            public void lowBalance(CustomerJoinContestResponseModel.PreJoinedModel preJoinedModel) {
                confirmationJoinContestDialog.dismiss();
                Bundle bundle = new Bundle();
                bundle.putString(DATA, new Gson().toJson(preJoinedModel));
                bundle.putLong(DATA1, teamId);
                bundle.putLong(DATA2, matchContestId);
                bundle.putString(DATA3, entryFee);
                goToAddCashActivity(bundle, ContestActivity.REQUEST_LOW_BALANCE);
            }
        });
        confirmationJoinContestDialog.show(getFm(), confirmationJoinContestDialog.getClass().getSimpleName());

    }

    private void callJoinContest(final String entryFee, long matchContestId, long teamId) {
        if (getMatchModel() != null) {
            displayProgressBar(false);
            CustomerJoinContestRequestModel requestModel = new CustomerJoinContestRequestModel();
            requestModel.customer_team_id = String.valueOf(teamId);
            requestModel.match_unique_id = getMatchModel().getMatch_id();
            requestModel.match_contest_id = matchContestId;
            if (isValidString(entryFee)) {
                requestModel.entry_fees = entryFee;
            }
            getWebRequestHelper().customerSoccerJoinContest(requestModel, this);

        }
    }

    private void handlePrivateContestDetail(WebRequest webRequest) {
        JoinPrivateContestResponseModel responsePojo = webRequest.getResponsePojo(JoinPrivateContestResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError() && responsePojo.getData()!=null && responsePojo.getData().size()>0) {
            if (getMatchModel() == null) {
                MyApplication.getInstance().setSelectedMatch(responsePojo.getMatch_detail());
            }
            List<ContestCategoryModel> data = responsePojo.getData();
            contestModel = data.get(0).getContests().get(0);
            contestWinnerBreakUpModel = responsePojo.getWinner_breakup();

            try {
                Bundle bundle = new Bundle();
                bundle.putLong(DATA1, contestModel.getMatch_contest_id());
                bundle.putString(DATA2, JoinSoccerPrivateContestActivity.this.getClass().getSimpleName());
                goToContestDetailActivity(bundle);
                finish();
            } catch (Exception ignore) {

            }

            //setupContestData();

        } else {
            if (isFinishing()) return;
            showInvalidContestDialog(responsePojo.getMessage());

        }

    }


    private void goToContestDetailActivity(Bundle bundle) {
        Intent intent = new Intent(this, SoccerContestDetailActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
       overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }


    private void showInvalidContestDialog(String msg) {
        Bundle bundle = new Bundle();
        bundle.putString(MESSAGE, msg);
        bundle.putString(POS_BTN, "OK");
        bundle.putString(NEG_BTN, "");
        ConfirmationDialog instance = ConfirmationDialog.getInstance(bundle);
        instance.setOnClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        finish();
                        break;
                }
            }
        });
        instance.show(getFm(), instance.getClass().getSimpleName());
    }

}
