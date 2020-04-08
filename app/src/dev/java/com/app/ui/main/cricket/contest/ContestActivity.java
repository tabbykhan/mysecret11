package com.app.ui.main.cricket.contest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.model.ContestCategoryModel;
import com.app.model.ContestModel;
import com.app.model.MatchModel;
import com.app.model.UserModel;
import com.app.model.webrequestmodel.CustomerJoinContestRequestModel;
import com.app.model.webresponsemodel.CustomerJoinContestResponseModel;
import com.app.model.webresponsemodel.MatchContestResponseModel;
import com.app.ui.MatchTimerListener;
import com.app.ui.MyApplication;
import com.app.ui.main.ToolbarFragment;
import com.app.ui.main.cricket.contest.adapter.ContestCategoryAdapter;
import com.app.ui.main.cricket.contest.contestdetail.ContestDetailActivity;
import com.app.ui.main.cricket.dialogs.joinconfirmdialog.ConfirmationJoinContestDialog;
import com.app.ui.main.cricket.dialogs.winnerbreakupdialog.WinnerBreakupDialog;
import com.app.ui.main.cricket.mycontest.MyContestActivity;
import com.app.ui.main.cricket.myteam.myteams.MyTeamsActivity;
import com.google.gson.Gson;
import com.medy.retrofitwrapper.WebRequest;
import com.utilities.DeviceScreenUtil;
import com.utilities.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Vishnu Gupta on 7/1/19.
 */
public class ContestActivity extends AppBaseActivity implements MatchTimerListener {

    public static final int REQUEST_CREATE_TEAM = 103;
    public static final int REQUEST_UPDATE_TEAM = 108;
    public static final int REQUEST_JOIN_CONTEST = 104;
    public static final int REQUEST_SWITCH_TEAM = 105;
    public static final int REQUEST_LOW_BALANCE = 106;
    public static final int REQUEST_CHOOSE_PLAYER = 107;
    public static final int REQUEST_CREATE_PRIVATE_CONTEST = 109;
    public static  final int REQUEST_FILTER_CONTEST=110;

    ToolbarFragment toolbarFragment;

    TextView tv_match_name;
    TextView tv_timer_time;
    RelativeLayout rl_bottom_lay;
    LinearLayout ll_new_team;
    TextView tv_create_new_team;

    LinearLayout ll_teams_detail;
    LinearLayout ll_my_teams;
    LinearLayout ll_my_contest;
    TextView tv_my_teams_count;
    TextView tv_my_contest_count;
    LinearLayout ll_enter_invite_code;
    LinearLayout ll_create_contest;

    ContestCategoryAdapter adapter;
    RecyclerView recycler_view;
    private SwipeRefreshLayout swipe_layout;

    List<ContestCategoryModel> contestCategoryModels = new ArrayList<>();
    MatchContestResponseModel.DetailBean detailBean;

    ConfirmationJoinContestDialog confirmationJoinContestDialog;

    long viewMoreCatId = -1;
    int viewMoreCatIdPos = -1;


    public MatchModel getMatchModel() {
        return MyApplication.getInstance().getSelectedMatch();
    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().addMatchTimerListener(this);

        getMatchContest();

    }

    @Override
    public void onPause() {
        super.onPause();
        MyApplication.getInstance().removeMatchTimerListener(this);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_constest;
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

            tv_match_name = findViewById(R.id.tv_match_name);
            tv_timer_time = findViewById(R.id.tv_timer_time);
            rl_bottom_lay = findViewById(R.id.rl_bottom_lay);
            ll_new_team = findViewById(R.id.ll_new_team);
            ll_teams_detail = findViewById(R.id.ll_teams_detail);
            ll_my_teams = findViewById(R.id.ll_my_teams);
            ll_my_contest = findViewById(R.id.ll_my_contest);
            tv_create_new_team = findViewById(R.id.tv_create_new_team);
            tv_my_teams_count = findViewById(R.id.tv_my_teams_count);
            tv_my_contest_count = findViewById(R.id.tv_my_contest_count);
            updateBottomDetail();

            recycler_view = findViewById(R.id.recycler_view);
            ll_enter_invite_code = findViewById(R.id.ll_enter_invite_code);
            ll_create_contest = findViewById(R.id.ll_create_contest);
            setupSwipeLayout();
            setMatchData();
            initializeRecyclerView();
//            getMatchContest();

            tv_create_new_team.setOnClickListener(this);
            ll_enter_invite_code.setOnClickListener(this);
            ll_create_contest.setOnClickListener(this);
        } else {
            onBackPressed();
        }
    }

    private void setupSwipeLayout() {
        swipe_layout = findViewById(R.id.swipe_layout);
        swipe_layout.setColorSchemeResources(R.color.colorOrange,
                R.color.colorPrimary);
        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMatchContest();
            }
        });
    }


    private void initializeRecyclerView() {
        adapter = new ContestCategoryAdapter(this, contestCategoryModels) {
            @Override
            public int getLastItemBottomMargin() {
                return rl_bottom_lay.getHeight() + DeviceScreenUtil.getInstance().convertDpToPixel(10);
            }

            @Override
            public boolean isViewMoreEnable(long catId) {
                return catId == viewMoreCatId;
            }
        };
        recycler_view.setLayoutManager(getVerticalLinearLayoutManager());
        recycler_view.setAdapter(adapter);

        new ItemClickSupport(recycler_view) {
            @Override
            public void onChildItemClicked(RecyclerView recyclerView, int parentPosition, int childPosition, View v) {
                switch (v.getId()) {
                    case R.id.tv_join: {
                        try {
                            ContestModel contestModel = contestCategoryModels.get(parentPosition).getContests().get(childPosition);
                            if (contestModel.isContestFull()) {
                                return;
                            }
                            if (!contestModel.isMoreJoinAvailable()) {
                                return;
                            }
                            if (detailBean == null) return;
                            boolean b = checkContestJoinAvailable(contestModel);
                            if (!b) return;
                            if (detailBean.getTotal_teams() == 0) {
                                Bundle bundle = new Bundle();
                                bundle.putLong(DATA2, contestModel.getMatch_contest_id());
                                goToCreateTeamActivity(bundle);
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putLong(DATA1, contestModel.getMatch_contest_id());
                                bundle.putString(DATA2, contestModel.getJoined_teams());
                                bundle.putBoolean(DATA9, contestModel.isMultiTeamAllowed());
                                goToChooseTeamActivity(bundle);
                            }
                        } catch (Exception ignore) {

                        }

                    }
                    break;
                    case R.id.ll_winners: {
                        try {
                            ContestModel contestModel = contestCategoryModels.get(parentPosition).getContests().get(childPosition);
                            if (contestModel.getTotal_winners() > 1) {
                                Bundle bundle = new Bundle();
                                bundle.putLong(DATA, contestModel.getMatch_contest_id());
                                bundle.putDouble(DATA1, contestModel.getTotal_price());
                                goToContestWinnerBreakup(bundle);
                            }
                        } catch (Exception ignore) {

                        }
                    }
                    break;

                    case R.id.iv_contest_share: {
                        try {
                            ContestModel contestModel = contestCategoryModels.get(parentPosition).getContests().get(childPosition);
                            Bundle bundle = new Bundle();
                            bundle.putString(DATA, String.valueOf(contestModel.getMatch_contest_id()));
                            goToSharePrivateContestDialog(bundle);
                        } catch (Exception ignore) {

                        }
                    }
                    break;

                    default: {
                        try {
                            ContestModel contestModel = contestCategoryModels.get(parentPosition).getContests().get(childPosition);
                            Bundle bundle = new Bundle();
                            bundle.putLong(DATA1, contestModel.getMatch_contest_id());
                            bundle.putString(DATA2, ContestActivity.this.getClass().getSimpleName());
                            goToContestDetailActivity(bundle);
                        } catch (Exception ignore) {

                        }
                    }
                    break;
                }
            }
        }.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                switch (v.getId()) {
                    case R.id.tv_view_more: {
                        try {
                            long id = contestCategoryModels.get(position).getId();
                            if (id == viewMoreCatId) {
                                viewMoreCatId = -1;
                                viewMoreCatIdPos = -1;
                                adapter.notifyItemChanged(position);
                            } else {
                                final int previousViewMorePosition = viewMoreCatIdPos;

                                viewMoreCatId = contestCategoryModels.get(position).getId();
                                viewMoreCatIdPos = position;

                                if (previousViewMorePosition != -1) {
                                    adapter.notifyItemChanged(previousViewMorePosition);
                                }
                                adapter.notifyItemChanged(position);
                            }
                        } catch (Exception ignore) {

                        }
                    }
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

    private void updateBottomDetail() {
        if (detailBean == null) {
            updateViewVisibitity(rl_bottom_lay, View.INVISIBLE);
            return;
        }
        updateViewVisibitity(rl_bottom_lay, View.VISIBLE);
        if (detailBean.getTotal_teams() > 0) {
            updateViewVisibitity(ll_new_team, View.GONE);
            updateViewVisibitity(ll_teams_detail, View.VISIBLE);
            ll_my_teams.setOnClickListener(this);
            ll_my_contest.setOnClickListener(this);
            tv_my_teams_count.setText(String.valueOf(detailBean.getTotal_teams()));
            tv_my_contest_count.setText(String.valueOf(detailBean.getTotal_joined_contest()));
        } else {
            updateViewVisibitity(ll_new_team, View.VISIBLE);
            updateViewVisibitity(ll_teams_detail, View.GONE);
            ll_my_teams.setOnClickListener(null);
            ll_my_contest.setOnClickListener(null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_create_new_team: {
                goToCreateTeamActivity(null);
            }
            break;

            case R.id.ll_my_teams: {
                Bundle bundle = new Bundle();
                bundle.putString(DATA, getMatchModel().getMatch_id());
                goToMyTeamsActivity(bundle);
            }
            break;
            case R.id.ll_my_contest: {
                Bundle bundle = new Bundle();
                bundle.putString(DATA, getMatchModel().getMatch_id());
                bundle.putString(DATA1, ContestActivity.this.getClass().getSimpleName());
                goToMyContestActivity(bundle);
            }
            break;

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    private void goToContestWinnerBreakup(Bundle bundle) {
        WinnerBreakupDialog instance = WinnerBreakupDialog.getInstance(bundle);
        instance.show(getFm(), instance.getClass().getSimpleName());
    }


    private void goToContestDetailActivity(Bundle bundle) {
        Intent intent = new Intent(this, ContestDetailActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }


    private void goToMyTeamsActivity(Bundle bundle) {
        Intent intent = new Intent(this, MyTeamsActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    private void goToMyContestActivity(Bundle bundle) {
        Intent intent = new Intent(this, MyContestActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }




    private void openConfirmJoinContest(final long matchContestId, final long teamId) {
        if (matchContestId == -1 || teamId == -1) return;
        Bundle bundle = new Bundle();
        bundle.putLong(DATA, matchContestId);
        bundle.putString(DATA3, String.valueOf(teamId));
        confirmationJoinContestDialog = ConfirmationJoinContestDialog.getInstance(bundle);
        confirmationJoinContestDialog.setConfirmationJoinContestListener(new ConfirmationJoinContestDialog.ConfirmationJoinContestListener() {
            @Override
            public void onProceed() {
                callJoinContest(matchContestId, teamId);
            }

            @Override
            public void lowBalance(CustomerJoinContestResponseModel.PreJoinedModel preJoinedModel) {
                confirmationJoinContestDialog.dismiss();
                Bundle bundle = new Bundle();
                bundle.putString(DATA, new Gson().toJson(preJoinedModel));
                bundle.putLong(DATA1, teamId);
                bundle.putLong(DATA2, matchContestId);
                goToAddCashActivity(bundle, REQUEST_LOW_BALANCE);
            }
        });
        confirmationJoinContestDialog.show(getFm(), confirmationJoinContestDialog.getClass().getSimpleName());

    }


    private void callJoinContest(long matchContestId, long teamId) {
        if (getMatchModel() != null) {
            displayProgressBar(false);
            CustomerJoinContestRequestModel requestModel = new CustomerJoinContestRequestModel();
            requestModel.customer_team_id = String.valueOf(teamId);
            requestModel.match_unique_id = getMatchModel().getMatch_id();
            requestModel.match_contest_id = matchContestId;
            getWebRequestHelper().customerJoinContest(requestModel, this);

        }
    }

    private void getMatchContest() {
        if (getMatchModel() != null) {
            swipe_layout.setRefreshing(true);
            getWebRequestHelper().getMatchContest(getMatchModel().getId(),
                    getMatchModel().getMatch_id(), this);
        }
    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        swipe_layout.setRefreshing(false);
        if (ID_CUSTOMER_JOIN_CONTEST == webRequest.getWebRequestId()) {
            dismissProgressBar();
        }
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_MATCH_CONTESTS:
                handleMatchContestResponse(webRequest);
                break;
            case ID_CUSTOMER_JOIN_CONTEST:
                handleCustomerJoinContestResponse(webRequest);
                break;
        }

    }

    private void handleMatchContestResponse(WebRequest webRequest) {
        MatchContestResponseModel responsePojo = webRequest.getResponsePojo(MatchContestResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            detailBean = responsePojo.getDetail();
            List<ContestCategoryModel> data = responsePojo.getData();
            contestCategoryModels.clear();
            if (data != null && data.size() > 0) {
                contestCategoryModels.addAll(data);
            }
            if (isFinishing()) return;
            updateBottomDetail();
            adapter.notifyDataSetChanged();
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
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
            getMatchContest();
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
        if (requestCode == REQUEST_CREATE_TEAM) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getExtras() != null) {
                    Bundle extras = data.getExtras();
                    long matchContestId = extras.getLong(DATA2, -1);
                    long teamId = extras.getLong(DATA1, -1);
                    openConfirmJoinContest(matchContestId, teamId);
                }

            }
        } else if (requestCode == REQUEST_JOIN_CONTEST) {
            if (resultCode == RESULT_OK) {

            }
        } else if (requestCode == REQUEST_LOW_BALANCE) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getExtras() != null) {
                    Bundle extras = data.getExtras();
                    long matchContestId = extras.getLong(DATA2, -1);
                    long teamId = extras.getLong(DATA1, -1);
                    openConfirmJoinContest(matchContestId, teamId);
                }
            }
        }
    }


}
