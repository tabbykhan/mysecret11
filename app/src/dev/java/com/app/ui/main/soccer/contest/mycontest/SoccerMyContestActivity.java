package com.app.ui.main.soccer.contest.mycontest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.app.model.CustomerTeamModel;
import com.app.model.MatchModel;
import com.app.model.ScoreModel;
import com.app.model.webresponsemodel.MatchContestResponseModel;
import com.app.model.webresponsemodel.ScoreResponseModel;
import com.app.ui.MatchTimerListener;
import com.app.ui.MyApplication;
import com.app.ui.main.ToolbarFragment;
import com.app.ui.main.cricket.contest.ContestActivity;
import com.app.ui.main.soccer.contest.contestdetail.SoccerContestDetailActivity;
import com.app.ui.main.soccer.contest.mycontest.adapter.SoccerMyContestCategoryAdapter;
import com.app.ui.main.soccer.dialog.SoccerWinnerBreakupDialog;
import com.app.ui.main.soccer.team.playerpreview.SoccerTeamPreviewDialog;
import com.google.gson.Gson;
import com.medy.retrofitwrapper.WebRequest;
import com.utilities.DeviceScreenUtil;
import com.utilities.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Vishnu Gupta on 7/1/19.
 */
public class SoccerMyContestActivity extends AppBaseActivity implements MatchTimerListener {


    ToolbarFragment toolbarFragment;

    TextView tv_match_name;
    TextView tv_timer_time;
    ImageView iv_match_timer;


    LinearLayout ll_score_card;
    LinearLayout ll_score_data;
    TextView tv_team1;
    TextView tv_team1_score;
    TextView tv_team2;
    TextView tv_team2_score;
    TextView tv_win;
    LinearLayout ll_player_stats;

    RelativeLayout rl_dream_team;
    LinearLayout ll_dream_team;

    SoccerMyContestCategoryAdapter adapter;
    RecyclerView recycler_view;
    private SwipeRefreshLayout swipe_layout;

    List<ContestCategoryModel> contestCategoryModels = new ArrayList<>();
    MatchContestResponseModel.DetailBean detailBean;
    ScoreModel scoreModel;
    List<ContestCategoryModel> beatTheExpertModels = new ArrayList<>();


    private TextView tv_more_entry;
    private ImageView iv_discount_image;

    public MatchModel getMatchModel() {
        return MyApplication.getInstance().getSelectedMatch();
    }

    public String getOpenFrom() {
        Bundle extras = getIntent().getExtras();
        return (extras == null ? "" : extras.getString(DATA1, ""));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getMatchModel().isFixtureMatch())
            MyApplication.getInstance().addMatchTimerListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MyApplication.getInstance().removeMatchTimerListener(this);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_my_constest;
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
            iv_match_timer = findViewById(R.id.iv_match_timer);

            ll_score_card = findViewById(R.id.ll_score_card);
            ll_score_data = findViewById(R.id.ll_score_data);
            tv_team1 = findViewById(R.id.tv_team1);
            tv_team1_score = findViewById(R.id.tv_team1_score);
            tv_team2 = findViewById(R.id.tv_team2);
            tv_team2_score = findViewById(R.id.tv_team2_score);
            tv_win = findViewById(R.id.tv_win);
            ll_player_stats = findViewById(R.id.ll_player_stats);
            rl_dream_team = findViewById(R.id.rl_dream_team);
            ll_dream_team = findViewById(R.id.ll_dream_team);


            recycler_view = findViewById(R.id.recycler_view);
            tv_more_entry = findViewById(R.id.tv_more_entry);
            iv_discount_image = findViewById(R.id.iv_discount_image);
            setupSwipeLayout();
            setMatchData();
            setupScoreBoard();
            setupPlayerStats();
            setDreamTeam();
            initializeRecyclerView();
            getCustomerMatchContest();
            getMatchScore();

            ll_player_stats.setOnClickListener(this);
            ll_dream_team.setOnClickListener(this);
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
                getCustomerMatchContest();
                getMatchScore();
            }
        });
    }


    private void setMatchData() {
        if (getMatchModel() != null) {
            tv_match_name.setText(getMatchModel().getShortName());
            tv_timer_time.setText(getMatchModel().getRemainTimeText());
            tv_timer_time.setTextColor(getResources().getColor(getMatchModel()
                    .getTimerColor()));
            if (getMatchModel().isFixtureMatch()) {
                updateViewVisibitity(iv_match_timer, View.VISIBLE);
            } else {
                updateViewVisibitity(iv_match_timer, View.INVISIBLE);
            }
        }
    }
    private void setupScoreBoard() {
        if (getMatchModel() != null) {
            if (getMatchModel().isFixtureMatch()) {
                updateViewVisibitity(ll_score_card, View.GONE);
                Log.d("match ", "------");
            } else {
                if (scoreModel != null) {
                    updateViewVisibitity(ll_score_card, View.VISIBLE);
                    if (scoreModel.isSoccerScoreAvailable()) {
                        tv_team1.setText(scoreModel.getTeam1().getName(1));
                        tv_team1_score.setText(scoreModel.getTeam1().getTeam_goal());
                        tv_team2.setText(scoreModel.getTeam2().getName(1));
                        tv_team2_score.setText(scoreModel.getTeam2().getTeam_goal());
                        updateViewVisibitity(ll_score_data, View.VISIBLE);
                        if(scoreModel.isValidString(scoreModel.getScore_board_notes())){
                            tv_win.setText(scoreModel.getScore_board_notes());
                            updateViewVisibitity(tv_win, View.VISIBLE);
                            Log.d("match 1", "------"+scoreModel.getTeam1().getTeam_goal());
                        }else{
                            Log.d("match 2", "------"+scoreModel.getTeam1().getTeam_goal());
                            updateViewVisibitity(tv_win, View.GONE);
                        }


                    } else {
                        Log.d("match3", "--match for Soccer Contest Activity 3----");
                        updateViewVisibitity(ll_score_data, View.GONE);
                        updateViewVisibitity(tv_win, View.VISIBLE);
                        tv_win.setText("Match is not started yet");
                    }
                } else {
                    Log.d("match 4", "------");
                    updateViewVisibitity(ll_score_card, View.GONE);
                }
            }
        }else{
            Log.d("match 5", "------");
        }
    }

    private void setupPlayerStats() {
        if (getMatchModel() != null) {
            if (getMatchModel().isFixtureMatch()) {
                updateViewVisibitity(ll_player_stats, View.GONE);
            } else {
                updateViewVisibitity(ll_player_stats, View.VISIBLE);
            }
        }
    }

    private void setDreamTeam() {
        if (getMatchModel() != null) {
            if (getMatchModel().isPastMatch()) {
                if (getMatchModel().isAbondentMatch()) {
                    updateViewVisibitity(rl_dream_team, View.GONE);
                } else {
                    updateViewVisibitity(rl_dream_team, View.VISIBLE);
                }
            } else {
                updateViewVisibitity(rl_dream_team, View.GONE);
            }
        }
    }


    private void initializeRecyclerView() {
        adapter = new SoccerMyContestCategoryAdapter(this, contestCategoryModels) {
            @Override
            public int getLastItemBottomMargin() {
                return rl_dream_team.getHeight() + DeviceScreenUtil.getInstance().convertDpToPixel(30);
            }

            @Override
            public MatchModel getMatchModel() {
                return SoccerMyContestActivity.this.getMatchModel();
            }
        };
        recycler_view.setLayoutManager(getVerticalLinearLayoutManager());
        recycler_view.setAdapter(adapter);

        new ItemClickSupport(recycler_view) {
            @Override
            public void onChildItemClicked(RecyclerView recyclerView, int parentPosition, int childPosition, View v) {
                switch (v.getId()) {
                    case R.id.tv_join: {
                        ContestModel contestModel = contestCategoryModels.get(parentPosition).getContests().get(childPosition);
                        if (contestModel.isContestFull()) {
                            return;
                        }
                        if (!contestModel.isMoreJoinAvailable()) {
                            return;
                        }
                        if (detailBean == null) return;
                        Bundle bundle = new Bundle();
                        bundle.putString(DATA, getMatchModel().getMatch_id());
                        bundle.putLong(DATA1, contestModel.getMatch_contest_id());
                        bundle.putString(DATA2, contestModel.getJoined_teams());
                        bundle.putBoolean(DATA9, contestModel.isMultiTeamAllowed());
                        goToSoccerChooseTeamActivity(bundle);

                    }
                    break;
                    case R.id.ll_winners: {
                        ContestModel contestModel = contestCategoryModels.get(parentPosition).getContests().get(childPosition);
                        if (contestModel.getTotal_winners() > 1) {
                            Bundle bundle = new Bundle();
                            bundle.putLong(DATA, contestModel.getMatch_contest_id());
                            bundle.putDouble(DATA1, contestModel.getTotal_price());
                            goToContestWinnerBreakup(bundle);
                        }
                    }
                    break;

                    case R.id.iv_contest_share: {
                        try {
                            ContestModel contestModel = contestCategoryModels.get(parentPosition).getContests().get(childPosition);
                            Bundle bundle = new Bundle();
                            bundle.putString(DATA, String.valueOf(contestModel.getMatch_contest_id()));
                            goToShareSoccerPrivateContestDialog(bundle);
                        } catch (Exception ignore) {

                        }
                    }
                    break;
                    default: {
                        ContestModel contestModel = contestCategoryModels.get(parentPosition).getContests().get(childPosition);
                        Bundle bundle = new Bundle();
                        bundle.putString(DATA, getMatchModel().getMatch_id());
                        bundle.putLong(DATA1, contestModel.getMatch_contest_id());
                        bundle.putString(DATA2, getOpenFrom());
                        goToContestDetailActivity(bundle);
                    }
                    break;

                }
            }
        }.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_player_stats: {
                goToSoocerMatchStatsActivity(null);
            }
            break;
            case R.id.ll_dream_team: {
                Bundle bundle = new Bundle();
                bundle.putLong(DATA1, 0);
                bundle.putLong(DATA2, -1);
                bundle.putString(DATA3, "TOP TEAM");
                openTeamPreviewDialog(bundle);
            }
            break;

            case R.id.ll_beat_the_expert: {
                try {
                    {
                        ContestModel contestModel = beatTheExpertModels.get(0).getContests().get(0);
                        Bundle bundle = new Bundle();
                        bundle.putString(DATA, getMatchModel().getMatch_id());
                        bundle.putLong(DATA1, contestModel.getMatch_contest_id());
                        bundle.putString(DATA2, getOpenFrom());
                        goToContestDetailActivity(bundle);
                    }
                } catch (Exception ignore) {

                }
            }
            break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    private void openTeamPreviewDialog(Bundle bundle) {
        SoccerTeamPreviewDialog instance = SoccerTeamPreviewDialog.getInstance(bundle);
        instance.setCustomerTeamModel(null);
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

    private void goToContestWinnerBreakup(Bundle bundle) {
        SoccerWinnerBreakupDialog instance = SoccerWinnerBreakupDialog.getInstance(bundle);
        instance.show(getFm(), instance.getClass().getSimpleName());
    }


    private void goToContestDetailActivity(Bundle bundle) {
        Intent intent = new Intent(this, SoccerContestDetailActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    private void getCustomerMatchContest() {
        if (getMatchModel() != null) {
            swipe_layout.setRefreshing(true);
            getWebRequestHelper().getCustomerSoccerMatchContest(getMatchModel().getId(),
                    getMatchModel().getMatch_id(), this);
        }
    }

    private void getMatchScore() {
        if (getMatchModel() != null) {
            swipe_layout.setRefreshing(true);
            getWebRequestHelper().getSoccerMatchScore(getMatchModel().getMatch_id(), this);
        }
    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        swipe_layout.setRefreshing(false);
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_GET_CUSTOMER_SOCCER_MATCH_CONTEST:
                handleCustomerMatchContestResponse(webRequest);
                break;
            case ID_SOCCER_MATCH_SCORE:
                handleMatchScoreResponse(webRequest);
                break;
        }

    }

    private void handleCustomerMatchContestResponse(WebRequest webRequest) {
        MatchContestResponseModel responsePojo = webRequest.getResponsePojo(MatchContestResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            detailBean = responsePojo.getDetail();
            List<ContestCategoryModel> data = responsePojo.getData();
            contestCategoryModels.clear();
            beatTheExpertModels.clear();
            if (data != null && data.size() > 0) {
                contestCategoryModels.addAll(data);
            }

            if (isFinishing()) return;
            adapter.notifyDataSetChanged();
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }

    }

    private void handleMatchScoreResponse(WebRequest webRequest) {
        if (!getMatchModel().isFixtureMatch()) {
            ScoreResponseModel responsePojo = webRequest.getResponsePojo(ScoreResponseModel.class);
            if (responsePojo == null) return;
            if (!responsePojo.isError()) {
                scoreModel = responsePojo.getData();
                if (!isFinishing())
                    setupScoreBoard();
            } else {
                if (isFinishing()) return;
//                showErrorMsg(responsePojo.getMessage());
            }
        }
    }

    @Override
    public void onMatchTimeUpdate() {
        setMatchData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ContestActivity.REQUEST_JOIN_CONTEST) {
            if (resultCode == RESULT_OK) {
                getCustomerMatchContest();
            }
        }
    }

}
