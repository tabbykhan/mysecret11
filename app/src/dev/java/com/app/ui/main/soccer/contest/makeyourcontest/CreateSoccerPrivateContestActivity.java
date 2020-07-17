package com.app.ui.main.soccer.contest.makeyourcontest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.model.ContestCategoryModel;
import com.app.model.MatchModel;
import com.app.model.UserModel;
import com.app.model.webrequestmodel.CreatePrivateContestRequestModel;
import com.app.model.webresponsemodel.AlreadyCreatedTeamCountResponseModel;
import com.app.model.webresponsemodel.CustomerJoinContestResponseModel;
import com.app.model.webresponsemodel.PrivateContestWinningBreakupResponseModel;
import com.app.ui.MatchTimerListener;
import com.app.ui.MyApplication;
import com.app.ui.main.ToolbarFragment;
import com.app.ui.main.cricket.contest.ContestActivity;
import com.app.ui.main.cricket.dialogs.joinconfirmdialog.ConfirmationJoinContestDialog;
import com.app.ui.main.cricket.dialogs.recommended.WinnersRecommendedDialog;
import com.app.ui.main.soccer.contest.makeyourcontest.adapter.CreateSoccerContestActivityAdapter;
import com.google.gson.Gson;
import com.medy.retrofitwrapper.WebRequest;

import java.util.ArrayList;
import java.util.List;

public class CreateSoccerPrivateContestActivity extends AppBaseActivity implements MatchTimerListener {

    ToolbarFragment toolbarFragment;

    TextView tv_match_name;
    TextView tv_timer_time;

    TextView tv_contest_size;
    TextView tv_contest_prize_size;
    TextView tv_contest_entry_fee;
    TextView tv_winner_type;
    TextView tv_winner_type_recomd;

    RecyclerView recycler_view;

    LinearLayout ll_choose_wining_breakup;

    RelativeLayout rl_choose_winner_type;

    CreateSoccerContestActivityAdapter adapter;

    List<PrivateContestWinningBreakupResponseModel.WinnerBreakUpData> contestWinnerBreakUpModels = new ArrayList<>();
    ContestCategoryModel contestCategoryModel;

    int selectedWinnerBreakupPosition = 0;

    ConfirmationJoinContestDialog confirmationJoinContestDialog;


    public MatchModel getMatchModel() {
        return MyApplication.getInstance().getSelectedMatch();
    }

    private String getContestSize() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            return extras.getString(CONTEST_SIZE, "");
        }
        return "";
    }

    private String getContestPrizePool() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            return extras.getString(PRIZE_POOL, "");
        }
        return "";
    }

    private String getContestEntryFee() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            return extras.getString(ENTER_AMOUNT, "");
        }
        return "";
    }

    private String getContestCoolName() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            return extras.getString(CONTEST_NAME, "");
        }
        return "";
    }

    private boolean getIsMultipleJoin() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            return extras.getBoolean(MULTIPLE_CONTEST, false);
        }
        return false;
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
        return R.layout.activity_create_contest;
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();

        if (getMatchModel() != null) {
            Fragment toolbar = getFm().findFragmentById(R.id.fragment_toolbar);
            if (toolbar instanceof AppBaseFragment) {
                toolbarFragment = (ToolbarFragment) toolbar;
                toolbarFragment.initializeComponent();
                toolbarFragment.setToolbarView(this);
            }

            //toolbarFragment.setCenterTitle(getContestCoolName());

            tv_match_name = findViewById(R.id.tv_match_name);
            tv_timer_time = findViewById(R.id.tv_timer_time);

            tv_contest_size = findViewById(R.id.tv_contest_size);
            tv_contest_prize_size = findViewById(R.id.tv_contest_prize_size);
            tv_contest_entry_fee = findViewById(R.id.tv_contest_entry_fee);
            tv_winner_type = findViewById(R.id.tv_winner_type);
            tv_winner_type_recomd = findViewById(R.id.tv_winner_type_recomd);
            recycler_view = findViewById(R.id.recycler_view);
            ll_choose_wining_breakup = findViewById(R.id.ll_choose_wining_breakup);
            rl_choose_winner_type = findViewById(R.id.rl_choose_winner_type);
            rl_choose_winner_type.setOnClickListener(this);
            ll_choose_wining_breakup.setOnClickListener(this);

            setMatchData();
            setupContestData();
            getPrivateContestWinningBreakUp();

        } else {
            onBackPressed();
        }
    }

    @Override
    public void onMatchTimeUpdate() {
        setMatchData();
    }

    private void setupContestData() {
        tv_contest_size.setText(getContestSize());
        tv_contest_prize_size.setText(currency_symbol + " " + getContestPrizePool());
        tv_contest_entry_fee.setText(currency_symbol + " " + getContestEntryFee());
        setupRecyclerView(null);
    }


    private void setMatchData() {
        if (getMatchModel() != null) {
            tv_match_name.setText(getMatchModel().getShortName());
            tv_timer_time.setText(getMatchModel().getRemainTimeText());
            tv_timer_time.setTextColor(getResources().getColor(getMatchModel()
                    .getTimerColor()));
        }
    }

    private void setupRecyclerView(PrivateContestWinningBreakupResponseModel.WinnerBreakUpData contestWinnerBreakUpModel) {
        recycler_view.setLayoutManager(getVerticalLinearLayoutManager());
        adapter = new CreateSoccerContestActivityAdapter(this, contestWinnerBreakUpModel);
        recycler_view.setAdapter(adapter);
    }

    private void updateWinnerBreakupData(int position) {
        this.selectedWinnerBreakupPosition = position;
        PrivateContestWinningBreakupResponseModel.WinnerBreakUpData winnerBreakUpData = contestWinnerBreakUpModels.get(this.selectedWinnerBreakupPosition);
        tv_winner_type.setText(winnerBreakUpData.getTotal_winners() + " Winners");
        if (position == 0) {
            tv_winner_type_recomd.setText("(Recommended)");
        } else {
            tv_winner_type_recomd.setText("");
        }
        setupRecyclerView(winnerBreakUpData);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_choose_winner_type:
                showWinnerBreakupOptionDialog();
                break;

            case R.id.ll_choose_wining_breakup:
                if (contestWinnerBreakUpModels.size() <= selectedWinnerBreakupPosition) {
                    return;
                }
                getAlreadyCreatedTeamCount();
                break;
        }
    }

    private void getPrivateContestWinningBreakUp() {
        displayProgressBar(false);
        getWebRequestHelper().getSoccerPrivateContestWinningBreakUp(getContestSize(),
                getContestPrizePool(), this);

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
            case ID_GET_SOCCER_PRIVATE_CONTEST_WINNING_BREAKUP:
                handlePrivateContestWinningBreakupResponse(webRequest);
                break;

            case ID_GET_SOCCER_ALREADY_CREATED_TEAM_COUNT:
                handleAlreadyCreatedTeamCount(webRequest);
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

    private void handlePrivateContestWinningBreakupResponse(WebRequest webRequest) {
        PrivateContestWinningBreakupResponseModel responsePojo = webRequest.getResponsePojo(PrivateContestWinningBreakupResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            contestCategoryModel = responsePojo.getData().getPrivate_contest_category();

            contestWinnerBreakUpModels.clear();
            if (responsePojo.getData() != null && responsePojo.getData().getWinning_breakups() != null) {
                contestWinnerBreakUpModels.addAll(responsePojo.getData().getWinning_breakups());
            }
            if (contestWinnerBreakUpModels != null && contestWinnerBreakUpModels.size() > 0) {
                updateWinnerBreakupData(0);
            }
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }

    }

    private CreatePrivateContestRequestModel generatePrivateContestRequest() {
        PrivateContestWinningBreakupResponseModel.WinnerBreakUpData winnerBreakUpData = contestWinnerBreakUpModels.get(selectedWinnerBreakupPosition);

        CreatePrivateContestRequestModel requestModel = new CreatePrivateContestRequestModel();
        requestModel.contest_size = getContestSize();
        requestModel.prize_pool = getContestPrizePool();
        requestModel.winning_breakup_id = winnerBreakUpData.getId();
        requestModel.match_id = String.valueOf(getMatchModel().getId());
        requestModel.match_unique_id = String.valueOf(getMatchModel().getMatch_id());
        requestModel.team_id = String.valueOf(0);
        requestModel.is_multiple = getIsMultipleJoin() ? "Y" : "N";
        requestModel.pre_join = "Y";
        return requestModel;
    }

    private void handleAlreadyCreatedTeamCount(WebRequest webRequest) {
        AlreadyCreatedTeamCountResponseModel responsePojo = webRequest.getResponsePojo(AlreadyCreatedTeamCountResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            if (responsePojo.getData() > 0) {
                Bundle bundle = new Bundle();
                bundle.putLong(DATA1, 0);
                bundle.putString(DATA2, "");
                bundle.putString(DATA3, new Gson().toJson(generatePrivateContestRequest()));
                bundle.putBoolean(DATA9, false);
                goToSoccerChooseTeamActivity(bundle);
            } else {
                Bundle bundle = new Bundle();
                bundle.putLong(DATA2, 0);
                goToCreateSoccerTeamActivity(bundle);
            }
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }

    }

    private void showWinnerBreakupOptionDialog() {
        if (contestWinnerBreakUpModels == null || contestWinnerBreakUpModels.size() == 0) return;
        final WinnersRecommendedDialog recommendedDialog = WinnersRecommendedDialog.getInstance(null);
        recommendedDialog.setWinnerBreakUpModels(contestWinnerBreakUpModels);
        recommendedDialog.setPreviousSelectedPosition(selectedWinnerBreakupPosition);
        recommendedDialog.setContestCoolName(getContestCoolName());
        recommendedDialog.setWinnersRecommendedDialogListener(new WinnersRecommendedDialog.WinnersRecommendedDialogListener() {
            @Override
            public void onPositionSelected(int position) {
                recommendedDialog.dismiss();
                updateWinnerBreakupData(position);
            }
        });
        recommendedDialog.show(getFm(), recommendedDialog.getClass().getSimpleName());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ContestActivity.REQUEST_CREATE_TEAM) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getExtras() != null) {
                    Bundle extras = data.getExtras();
                    long teamId = extras.getLong(DATA1, -1);
                    openConfirmJoinContest(teamId);
                }
            }
        } else if (requestCode == ContestActivity.REQUEST_JOIN_CONTEST) {
            if (resultCode == RESULT_OK) {
                setResult(data.getExtras().getString(DATA));
            }
        } else if (requestCode == ContestActivity.REQUEST_LOW_BALANCE) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getExtras() != null) {
                    Bundle extras = data.getExtras();
                    long matchContestId = extras.getLong(DATA2, -1);
                    long teamId = extras.getLong(DATA1, -1);
                    openConfirmJoinContest(teamId);
                }
            }
        }
    }

    private void setResult(String matchContestId){
        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        bundle.putString(DATA,matchContestId);
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        supportFinishAfterTransition();
    }

    private void openConfirmJoinContest(final long teamId) {

        CreatePrivateContestRequestModel requestModel = generatePrivateContestRequest();
        requestModel.team_id = String.valueOf(teamId);

        Bundle bundle = new Bundle();
        bundle.putLong(DATA, 0);
        bundle.putString(DATA2, new Gson().toJson(requestModel));
        bundle.putString(DATA3, String.valueOf(teamId));
        confirmationJoinContestDialog = ConfirmationJoinContestDialog.getInstance(bundle);
        confirmationJoinContestDialog.setConfirmationJoinContestListener(new ConfirmationJoinContestDialog.ConfirmationJoinContestListener() {
            @Override
            public void onProceed() {
                callJoinContest(teamId);
            }

            @Override
            public void lowBalance(CustomerJoinContestResponseModel.PreJoinedModel preJoinedModel) {
                confirmationJoinContestDialog.dismiss();
                Bundle bundle = new Bundle();
                bundle.putString(DATA, new Gson().toJson(preJoinedModel));
                bundle.putLong(DATA1, teamId);
                bundle.putLong(DATA2, 0);
                goToAddCashActivity(bundle, ContestActivity.REQUEST_LOW_BALANCE);
            }
        });
        confirmationJoinContestDialog.show(getFm(), confirmationJoinContestDialog.getClass().getSimpleName());

    }


    private void callJoinContest(long teamId) {
        if (getMatchModel() != null) {
            CreatePrivateContestRequestModel requestModel = generatePrivateContestRequest();
            requestModel.team_id = String.valueOf(teamId);
            requestModel.pre_join = "N";
            displayProgressBar(false);
            getWebRequestHelper().createSoccerPrivateContest(requestModel, this);
        }
    }
}
