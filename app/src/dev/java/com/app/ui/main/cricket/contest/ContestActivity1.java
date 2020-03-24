package com.app.ui.main.cricket.contest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.appbase.ViewPagerAdapter;
import com.app.model.ContestCategoryModel;
import com.app.model.MatchModel;
import com.app.model.UserModel;
import com.app.model.webrequestmodel.CustomerJoinContestRequestModel;
import com.app.model.webresponsemodel.CustomerJoinContestResponseModel;
import com.app.model.webresponsemodel.MatchContestResponseModel;
import com.app.ui.MatchTimerListener;
import com.app.ui.MyApplication;
import com.app.ui.main.ToolbarFragment;
import com.app.ui.main.cricket.contest.contestdetail.ContestDetailActivity;
import com.app.ui.main.cricket.contest.fragments.CashFragment;
import com.app.ui.main.cricket.contest.fragments.PracticeFragment;
import com.app.ui.main.cricket.contest.fragments.PrivateFragment;
import com.app.ui.main.cricket.dialogs.joinconfirmdialog.ConfirmationJoinContestDialog;
import com.app.ui.main.cricket.dialogs.winnerbreakupdialog.WinnerBreakupDialog;
import com.app.ui.main.cricket.mycontest.MyContestActivity;
import com.app.ui.main.cricket.myteam.myteams.MyTeamsActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.medy.retrofitwrapper.WebRequest;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Vishnu Gupta on 7/1/19.
 */
public class ContestActivity1 extends AppBaseActivity implements MatchTimerListener {


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

    RecyclerView recycler_view;

    List<ContestCategoryModel> contestCategoryModels = new ArrayList<>();
    List<ContestCategoryModel> practiceCategoryModels = new ArrayList<>();
    List<ContestCategoryModel> beatTheExpertModels = new ArrayList<>();

    MatchContestResponseModel.DetailBean detailBean;

    ConfirmationJoinContestDialog confirmationJoinContestDialog;


    /*implement tan bar*/
    ViewPager viewpager_match;
    ViewPagerAdapter adapter1;
    TabLayout mymatches_tabs;
    private LinearLayout ll_my_team_create_team_join_contest_layer;
    private LinearLayout ll_create_team;
    private TextView tv_create_team_count;


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
        return R.layout.activity_constest1;
    }


    @Override
    public void initializeComponent() {
        super.initializeComponent();
        viewpager_match = findViewById(R.id.viewpager_match);
        mymatches_tabs = findViewById(R.id.mymatches_tabs);
        setupViewPager();


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
            ll_my_team_create_team_join_contest_layer=
                    findViewById(R.id.ll_my_team_create_team_join_contest_layer);
            ll_create_team=findViewById(R.id.ll_create_team);
            tv_create_team_count=findViewById(R.id.tv_create_team_count);
            ll_create_team.setOnClickListener(this);
            updateBottomDetail();

            recycler_view = findViewById(R.id.recycler_view);
            ll_enter_invite_code = findViewById(R.id.ll_enter_invite_code);
            ll_create_contest = findViewById(R.id.ll_create_contest);
            //setupSwipeLayout();
            setMatchData();
            tv_create_new_team.setOnClickListener(this);
            //initializeRecyclerView();
//            getMatchContest();


            //ll_enter_invite_code.setOnClickListener(this);
            //ll_create_contest.setOnClickListener(this);
        } else {
            onBackPressed();
        }
    }

    private void setupViewPager() {
        adapter1 = new ViewPagerAdapter(getFm());
        adapter1.addFragment(new CashFragment(), "CASH");
        adapter1.addFragment(new PracticeFragment(), "PRACTICE");
        adapter1.addFragment(new PrivateFragment(), "PRIVATE");
        viewpager_match.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                adapter1.getItem(position).onPageSelected();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewpager_match.setAdapter(adapter1);
        mymatches_tabs.setupWithViewPager(viewpager_match);

    }


    private void setMatchData() {
        if (getMatchModel() != null) {
            tv_match_name.setText(getMatchModel().getShortName());
            tv_timer_time.setText(getMatchModel().getRemainTimeText());
            tv_timer_time.setTextColor(getResources().getColor(getMatchModel()
                    .getTimerColor()));
            boolean matchTimeExpire = getMatchModel().isMatchTimeExpire();
            if (matchTimeExpire) {
                showMatchExpireDialog();
            }
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
            ll_create_team.setOnClickListener(this);
            tv_my_teams_count.setText(String.valueOf(detailBean.getTotal_teams()));
            tv_create_team_count.setText(String.valueOf(detailBean.getTotal_teams()+1));
            tv_my_contest_count.setText(String.valueOf(detailBean.getTotal_joined_contest()));
        } else {
            updateViewVisibitity(ll_new_team, View.VISIBLE);
            updateViewVisibitity(ll_teams_detail, View.GONE);
            ll_my_teams.setOnClickListener(null);
            ll_my_contest.setOnClickListener(null);
            ll_create_team.setOnClickListener(null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_create_new_team: {
                goToCreateTeamActivity(null);
            }
            case R.id.ll_create_team:{
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
                bundle.putString(DATA1, ContestActivity1.this.getClass().getSimpleName());
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
            getWebRequestHelper().customerJoinContest(requestModel, this);

        }
    }

    public void getMatchContest() {
        if (getMatchModel() != null) {
            try {
                AppBaseFragment item = adapter1.getItem(0);
                ((CashFragment) item).showRefreshing();


                item = adapter1.getItem(1);
                ((PracticeFragment) item).showRefreshing();
            } catch (Exception e) {

            }

            getWebRequestHelper().getMatchContest(getMatchModel().getId(),
                    getMatchModel().getMatch_id(), this);
        }
    }

    @Override
    public void onWebRequestPreResponse(WebRequest webRequest) {
        super.onWebRequestPreResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_MATCH_CONTESTS:
                MatchContestResponseModel responsePojo = webRequest.getResponsePojo(MatchContestResponseModel.class);
                if (responsePojo != null) {
                    if (responsePojo.getData().size() > 0 && adapter1 != null) {
                        CashFragment item = (CashFragment) adapter1.getItem(0);
                        if(item!=null) {
                            int currentSortType = item.getCurrentSortType();

                            for (ContestCategoryModel contestCategoryModel : responsePojo.getData()) {
                                View currentSortBy = item.getCurrentSortBy();
                                if (currentSortBy == null) {
                                    contestCategoryModel.sortContest(1, 1);
                                } else {
                                    switch (currentSortBy.getId()) {
                                        case R.id.iv_sort_total_winnings: {
                                            contestCategoryModel.sortContest(1, currentSortType);
                                        }
                                        break;

                                        case R.id.iv_sort_entry_fee: {
                                            contestCategoryModel.sortContest(2, currentSortType);
                                        }
                                        break;

                                        case R.id.iv_sort_winners: {
                                            contestCategoryModel.sortContest(3, currentSortType);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    webRequest.setResponsePojo(responsePojo);
                }
                break;
        }
    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        // swipe_layout.setRefreshing(false);
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
        MatchContestResponseModel responsePojo = (MatchContestResponseModel) webRequest.getResponsePojo();
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            detailBean = responsePojo.getDetail();
            List<ContestCategoryModel> data = responsePojo.getData();
            List<ContestCategoryModel> data_para = responsePojo.getPractice();
            List<ContestCategoryModel> beatTheExpert = responsePojo.getBeatTheExpert();

            contestCategoryModels.clear();
            practiceCategoryModels.clear();
            beatTheExpertModels.clear();

            if (data != null && data.size() > 0) {
                contestCategoryModels.addAll(data);
            }

            if (data_para != null && data_para.size() > 0) {
                practiceCategoryModels.addAll(data_para);
            }

            if (beatTheExpert != null && beatTheExpert.size() > 0) {
                beatTheExpertModels.addAll(beatTheExpert);
            }

            if (isFinishing()) return;

            AppBaseFragment item = adapter1.getItem(0);
            ((CashFragment) item).setupDetsil(detailBean);
            ((CashFragment) item).setupData(contestCategoryModels);

            item = adapter1.getItem(1);
            ((PracticeFragment) item).setupDetsil(detailBean);
            ((PracticeFragment) item).setupData(practiceCategoryModels);

            if (isFinishing()) return;
            updateBottomDetail();
            //adapter.notifyDataSetChanged();
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
}
