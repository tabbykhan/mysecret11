package com.app.ui.main.soccer.contest.contestdetail;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ConstantsFlavor;
import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.model.ContestCategoryModel;
import com.app.model.ContestModel;
import com.app.model.ContestTeamModel;
import com.app.model.CustomerTeamModel;
import com.app.model.MatchModel;
import com.app.model.ScoreModel;
import com.app.model.UserModel;
import com.app.model.webrequestmodel.CustomerJoinContestRequestModel;
import com.app.model.webresponsemodel.ContestPdfResponseModel;
import com.app.model.webresponsemodel.ContestTeamsResponseModel;
import com.app.model.webresponsemodel.CustomerJoinContestResponseModel;
import com.app.model.webresponsemodel.MatchContestResponseModel;
import com.app.model.webresponsemodel.ScoreResponseModel;
import com.app.ui.MatchTimerListener;
import com.app.ui.MyApplication;
import com.app.ui.main.ToolbarFragment;
import com.app.ui.main.cricket.contest.ContestActivity;
import com.app.ui.main.cricket.dialogs.winnerbreakupexpert.WinnerBreakupExpertDialog;
import com.app.ui.main.cricket.myteam.playerpreview.TeamPreviewDialog;
import com.app.ui.main.soccer.contest.contestdetail.adapter.SoccerContestDetailAdapter;
import com.app.ui.main.soccer.dialog.ConfirmationSoccerJoinContestDialog;
import com.app.ui.main.soccer.dialog.SoccerWinnerBreakupDialog;
import com.app.ui.main.soccer.team.switchteam.SwitchSoccerTeamActivity;
import com.google.gson.Gson;
import com.medy.retrofitwrapper.ConnectionDetector;
import com.medy.retrofitwrapper.WebRequest;
import com.utilities.DeviceScreenUtil;
import com.utilities.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Vishnu Gupta on 7/1/19.
 */
public class SoccerContestDetailActivity extends AppBaseActivity implements MatchTimerListener {


    ToolbarFragment toolbarFragment;

    NestedScrollView view_nested_scroll;
    TextView tv_match_name;
    TextView tv_timer_time;
    ImageView iv_match_timer;
    CardView cv_data;
    TextView tv_price_pool;
    LinearLayout ll_winners;
    TextView tv_winners;
    ImageView iv_winners;
    TextView tv_entry;
    LinearLayout ll_joined_teams;
    ImageView iv_contest_share;
    TextView tv_joined_teams;
    TextView tv_switch_team;
    LinearLayout ll_joined_progress;
    ProgressBar pb_teams;
    TextView tv_teams_left;
    TextView tv_total_teams;
    TextView tv_join;
    LinearLayout ll_contest_info;
    LinearLayout ll_confirm_winning;
    LinearLayout ll_multi_join;
    TextView tv_multi_join;
    TextView tv_multi_join_des;
    LinearLayout ll_view_teams;
    TextView tv_view_teams;
    TextView tv_all_teams;

    RecyclerView recycler_view;
    private SwipeRefreshLayout swipe_layout;

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


    SoccerContestDetailAdapter adapter;


    ContestModel contestModel;
    MatchContestResponseModel.DetailBean detailBean;
    ContestCategoryModel contestCategoryModel;

    List<ContestTeamModel> contestTeamModels = new ArrayList<>();

    ScoreModel scoreModel;
    ConfirmationSoccerJoinContestDialog confirmationJoinContestDialog;


    private LinearLayout ll_normal_bet;


    private int totalPages = 1000;
    private int currentPages = 0;
    private boolean loadingNextData = false;
    TextView tv_more_entry;
    ImageView iv_discount_image;


    ContestTeamModel admin_customerTeamModel;


    public MatchModel getMatchModel() {
        return MyApplication.getInstance().getSelectedMatch();
    }


    public long getMatchContestId() {
        Bundle extras = getIntent().getExtras();
        return (extras == null ? -1 : extras.getLong(DATA1, -1));
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
        return R.layout.activity_contest_detail;
    }


    @Override
    public void initializeComponent() {
        super.initializeComponent();

        if (getMatchModel() != null && getMatchContestId() > 0) {
            Fragment toolbar = getFm().findFragmentById(R.id.fragment_toolbar);
            if (toolbar instanceof AppBaseFragment) {
                toolbarFragment = (ToolbarFragment) toolbar;
                toolbarFragment.initializeComponent();
                toolbarFragment.setToolbarView(this);
            }

            view_nested_scroll = findViewById(R.id.view_nested_scroll);
            tv_match_name = findViewById(R.id.tv_match_name);
            tv_timer_time = findViewById(R.id.tv_timer_time);
            iv_match_timer = findViewById(R.id.iv_match_timer);
            cv_data = findViewById(R.id.cv_data);
            tv_price_pool = findViewById(R.id.tv_price_pool);
            ll_winners = findViewById(R.id.ll_winners);
            tv_winners = findViewById(R.id.tv_winners);
            iv_winners = findViewById(R.id.iv_winners);
            tv_entry = findViewById(R.id.tv_entry);
            ll_joined_teams = findViewById(R.id.ll_joined_teams);
            iv_contest_share = findViewById(R.id.iv_contest_share);
            iv_contest_share.setOnClickListener(this);
            tv_joined_teams = findViewById(R.id.tv_joined_teams);
            tv_switch_team = findViewById(R.id.tv_switch_team);
            ll_joined_progress = findViewById(R.id.ll_joined_progress);
            pb_teams = findViewById(R.id.pb_teams);
            tv_teams_left = findViewById(R.id.tv_teams_left);
            tv_total_teams = findViewById(R.id.tv_total_teams);
            tv_join = findViewById(R.id.tv_join);
            ll_contest_info = findViewById(R.id.ll_contest_info);
            ll_confirm_winning = findViewById(R.id.ll_confirm_winning);
            ll_multi_join = findViewById(R.id.ll_multi_join);
            tv_multi_join = findViewById(R.id.tv_multi_join);
            tv_multi_join_des = findViewById(R.id.tv_multi_join_des);
            ll_view_teams = findViewById(R.id.ll_view_teams);
            tv_view_teams = findViewById(R.id.tv_view_teams);
            tv_all_teams = findViewById(R.id.tv_all_teams);
            tv_all_teams.setText("");

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
            recycler_view.setNestedScrollingEnabled(false);


            ll_normal_bet = findViewById(R.id.ll_normal_bet);

            //tv_beat_the_expert = findViewById(R.id.tv_beat_the_expert);


            tv_more_entry = findViewById(R.id.tv_more_entry);
            iv_discount_image = findViewById(R.id.iv_discount_image);


            setupSwipeLayout();
            setMatchData();
            setupScoreBoard();
            setupPlayerStats();
            setContestData();
            setViewTeams();
            setDreamTeam();
            initializeRecyclerView();
            setupPagination();
            loadMoreData();

            tv_view_teams.setOnClickListener(this);
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
                swipe_layout.setRefreshing(true);
                currentPages = 0;
                loadMoreData();
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
            } else {
                if (scoreModel != null) {
                    updateViewVisibitity(ll_score_card, View.VISIBLE);
                    if (scoreModel.isScoreAvailable()) {
                        tv_team1.setText(scoreModel.getTeam1().getName(1));
                        tv_team1_score.setText(scoreModel.getTeam1().getScore());
                        tv_team2.setText(scoreModel.getTeam2().getName(1));
                        tv_team2_score.setText(scoreModel.getTeam2().getScore());

                        updateViewVisibitity(ll_score_data, View.VISIBLE);

                        if (scoreModel.isValidString(scoreModel.getScore_board_notes())) {
                            tv_win.setText(scoreModel.getScore_board_notes());
                            updateViewVisibitity(tv_win, View.VISIBLE);
                        } else {
                            updateViewVisibitity(tv_win, View.GONE);
                        }


                    } else {
                        updateViewVisibitity(ll_score_data, View.GONE);
                        updateViewVisibitity(tv_win, View.VISIBLE);
                        tv_win.setText("Match is not started yet");
                    }
                } else {
                    updateViewVisibitity(ll_score_card, View.GONE);
                }
            }
        }
    }

    private void setupPlayerStats() {
        if (getMatchModel() != null) {
            if (getMatchModel().isFixtureMatch()) {
                updateViewVisibitity(ll_player_stats, View.GONE);
                ll_player_stats.setOnClickListener(null);
            } else {
                updateViewVisibitity(ll_player_stats, View.VISIBLE);
                ll_player_stats.setOnClickListener(this);
            }
        }
    }


    private void setViewTeams() {
        if (getMatchModel() != null) {
            if (getMatchModel().isFixtureMatch()) {
                ll_view_teams.setAlpha(0.5f);
            } else if (getMatchModel().isLiveMatch()) {
                ll_view_teams.setAlpha(1.0f);
            } else {
                ll_view_teams.setAlpha(1.0f);
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

            if (getMatchModel().isFixtureMatch()) {
                updateViewVisibitity(tv_join, View.VISIBLE);
                tv_entry.setOnClickListener(this);
            } else {
                updateViewVisibitity(tv_join, View.GONE);
                tv_entry.setOnClickListener(null);
            }
        }
    }

    private void setContestData() {
        if (contestModel != null) {
            updateViewVisibitity(ll_normal_bet, View.VISIBLE);
            ll_winners.setOnClickListener(this);
            tv_switch_team.setOnClickListener(this);
            tv_join.setOnClickListener(this);

            tv_price_pool.setText(currency_symbol + contestModel.getTotalPriceText());
            tv_entry.setText(currency_symbol + contestModel.getEntryFeesText());
            tv_winners.setText(String.valueOf(contestModel.getTotal_winners()));
            if (contestModel.getTotal_winners() > 1) {
                updateViewVisibitity(iv_winners, View.VISIBLE);
            } else {
                updateViewVisibitity(iv_winners, View.GONE);
            }


            String moreEntryFeesText = contestModel.getMoreEntryFeesText();
            if (isValidString(moreEntryFeesText)) {
                tv_more_entry.setText(currency_symbol + moreEntryFeesText);

                int[] discountImageSizeForCategory = contestModel.getDiscountImageSizeForContestSmall();
                LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) iv_discount_image.getLayoutParams();
                layoutParams1.width = discountImageSizeForCategory[0];
                layoutParams1.height = discountImageSizeForCategory[1];
                iv_discount_image.setLayoutParams(layoutParams1);

                loadImage(this, iv_discount_image, null,
                        contestModel.getDiscount_image(),
                        R.drawable.logo);
                updateViewVisibitity(iv_discount_image, View.VISIBLE);
            } else {
                tv_more_entry.setText("");
                updateViewVisibitity(iv_discount_image, View.GONE);
            }

            if (getMatchModel().isFixtureMatch()) {
                cv_data.setRadius(DeviceScreenUtil.getInstance().convertDpToPixel(5.0f));
                updateViewVisibitity(ll_joined_progress, View.VISIBLE);
                pb_teams.setMax(contestModel.getTotal_team());
                pb_teams.setProgress(contestModel.getTotal_team() - contestModel.getTotal_team_left());
                tv_teams_left.setText("Only " + contestModel.getTotal_team_left() + " spots left");
                tv_total_teams.setText(contestModel.getTotal_team() + " Teams");

                if (!contestModel.isContestFull()) {
                    iv_contest_share.setOnClickListener(this);
                    iv_contest_share.setAlpha(1.0f);
                    if (contestModel.isMoreJoinAvailable()) {
                        if (contestModel.isAtleastOneTeamJoin()) {
                            tv_join.setText("JOIN+");
                        } else {
                            tv_join.setText("JOIN NOW");
                        }
                    } else {
                        tv_join.setText("JOINED");
                    }
                } else {
                    iv_contest_share.setOnClickListener(null);
                    iv_contest_share.setAlpha(0.2f);
                    if (contestModel.isAtleastOneTeamJoin()) {
                        tv_join.setText("JOINED");
                    } else {
                        tv_join.setText("FULL");
                    }
                }
                if (contestModel.isAtleastOneTeamJoin()) {
                    updateViewVisibitity(ll_joined_teams, View.VISIBLE);
                    tv_joined_teams.setText(contestModel.getJoinedWithText());
                } else {
                    updateViewVisibitity(ll_joined_teams, View.GONE);
                    if (ConstantsFlavor.type == ConstantsFlavor.Type.vision)
                        updateViewVisibitity(ll_normal_bet, View.GONE);
                }

                updateViewVisibitity(ll_contest_info, View.VISIBLE);
                updateViewVisibitity(ll_multi_join, View.VISIBLE);
                if (contestModel.isMultiJoinContest()) {
                    tv_multi_join.setText("M");
                    tv_multi_join_des.setText("Join with multiple teams.");
                } else {
                    tv_multi_join.setText("S");
                    tv_multi_join_des.setText("Join with single team.");
                }
                if (contestModel.isConfirmWin()) {
                    updateViewVisibitity(ll_confirm_winning, View.VISIBLE);
                } else {
                    updateViewVisibitity(ll_confirm_winning, View.GONE);
                }
            } else {
                if (contestModel.isBetTheExpert()) {
                    cv_data.setRadius(DeviceScreenUtil.getInstance().convertDpToPixel(10.0f));
                } else {
                    cv_data.setRadius(DeviceScreenUtil.getInstance().convertDpToPixel(30.0f));
                    updateViewVisibitity(ll_joined_teams, View.GONE);
                    updateViewVisibitity(ll_joined_progress, View.GONE);
                    updateViewVisibitity(ll_contest_info, View.GONE);
                }
            }

            if (adapter != null) {
                try {
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {

                }

            }

        } else {
            ll_winners.setOnClickListener(null);
            tv_switch_team.setOnClickListener(null);
            tv_join.setOnClickListener(null);

            tv_price_pool.setText("");
            tv_winners.setText("");
            updateViewVisibitity(iv_winners, View.VISIBLE);
            tv_entry.setText("");
            updateViewVisibitity(ll_joined_teams, View.GONE);
            updateViewVisibitity(ll_joined_progress, View.GONE);
            updateViewVisibitity(ll_contest_info, View.GONE);
        }
    }

    private void initializeRecyclerView() {
        adapter = new SoccerContestDetailAdapter(this, contestTeamModels) {
            @Override
            public int getLastItemBottomMargin() {
                return rl_dream_team.getHeight() + DeviceScreenUtil.getInstance().convertDpToPixel(30);
            }

            @Override
            public String getMatchProgress() {
                if (getMatchModel() != null) {
                    return getMatchModel().getMatch_progress();
                }
                return super.getMatchProgress();
            }

            @Override
            public UserModel getUserModel() {
                return SoccerContestDetailActivity.this.getUserModel();
            }

            @Override
            public boolean isBeatExpertContest() {
                return contestModel != null && contestModel.isBetTheExpert();
            }
        };
        recycler_view.setLayoutManager(getVerticalLinearLayoutManager());
        recycler_view.setAdapter(adapter);

        new ItemClickSupport(recycler_view).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                try {
                    ContestTeamModel customerTeamModel = contestTeamModels.get(position);
                    switch (v.getId()) {
                        default: {
                            MatchModel matchModel = getMatchModel();
                            if (matchModel == null) return;
                            if (matchModel.isFixtureMatch()) {
                                if (!customerTeamModel.isMyTeam(adapter.getUserModel().getId())) {
                                    showErrorMsg("You can't view the teams of other players before deadline.");
                                    return;
                                }
                            }
                            Bundle bundle = new Bundle();
                            bundle.putLong(DATA1, customerTeamModel.getTeam_id());
                            if (!matchModel.isFixtureMatch()) {
                                bundle.putLong(DATA2, customerTeamModel.getNew_rank());
                            }
                            bundle.putString(DATA3, customerTeamModel.getFullTeamName());
                            bundle.putString(DATA9, String.valueOf(customerTeamModel.getCustomer_id()));
                            openTeamPreviewDialog(bundle);

                        }
                        break;

                    }
                } catch (Exception ignore) {

                }
            }
        });

    }


    private void setupPagination() {
        final int scrollOffset = DeviceScreenUtil.getInstance().convertDpToPixel(100);
        view_nested_scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                View view = view_nested_scroll.getChildAt(view_nested_scroll.getChildCount() - 1);
                if (view != null) {
                    int llBottomPosition = view.getBottom();
                    int nsvHeight = view_nested_scroll.getHeight();
                    int nsvScrollY = view_nested_scroll.getScrollY();
                    // int diff = llBottomPosition - (nsvHeight + nsvScrollY);
                    if (!loadingNextData && (nsvHeight + nsvScrollY + scrollOffset) >= llBottomPosition) {
                        loadMoreData();
                    }
                }
            }
        });
//
//        recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView,
//                                   int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (recycler_view.getLayoutManager() == null) return;
//                int totalItemCount = (recycler_view.getLayoutManager()).getItemCount();
//                int lastVisibleItem = ((LinearLayoutManager) recycler_view.getLayoutManager()).findLastVisibleItemPosition();
//                if (!loadingNextData && totalItemCount <= (lastVisibleItem + 3)) {
//                    loadMoreData();
//                }
//            }
//        });
    }

    public void loadMoreData() {
        if (currentPages == 0) {
            currentPages = 1;
            totalPages = 1000;
            getContestDetail();
            getContestTeams();
            getMatchScore();
            return;
        }

        if (totalPages > currentPages) {
            currentPages = currentPages + 1;
            getContestTeams();
        }

    }


    private void setLoadingNextData(boolean isLoading) {
        loadingNextData = isLoading;
        if (swipe_layout.isRefreshing()) {
            swipe_layout.setRefreshing(isLoading);
        } else {
            adapter.setLoadMore(isLoading);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ll_winners: {
                if (contestModel == null) return;
                if (contestModel.getTotal_winners() > 1) {
                    Bundle bundle = new Bundle();
                    bundle.putLong(DATA, contestModel.getMatch_contest_id());
                    bundle.putDouble(DATA1, contestModel.getTotal_price());
                    goToContestWinnerBreakup(bundle);
                }
            }
            break;
            case R.id.iv_contest_share: {
                if (contestModel == null) return;
                try {
                    Bundle bundle = new Bundle();
                    bundle.putString(DATA, String.valueOf(contestModel.getMatch_contest_id()));
                    goToShareSoccerPrivateContestDialog(bundle);
                } catch (Exception ignore) {

                }
            }
            break;
            case R.id.tv_switch_team: {
                Bundle bundle = new Bundle();
                bundle.putString(DATA, getMatchModel().getMatch_id());
                bundle.putLong(DATA1, contestModel.getMatch_contest_id());
                bundle.putString(DATA2, contestModel.getJoined_teams());
                goToSwitchTeamActivity(bundle);
            }
            break;
            case R.id.tv_entry:
            case R.id.tv_join: {
                if (contestModel == null) return;
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
                    if (contestModel.isBetTheExpert()) {
                        bundle.putString((DATA4), new Gson().toJson(contestModel.getEntry_fees_suggest()));
                        bundle.putString((DATA5), String.valueOf(contestModel.getEntry_fees()));
                        bundle.putString((DATA6), String.valueOf(contestModel.getMax_entry_fees()));
                        bundle.putString((DATA7), String.valueOf(contestModel.getEntry_fee_multiplier()));
                        bundle.putString((DATA8), String.valueOf(contestModel.getMore_entry_fees()));
                    }
                    goToCreateTeamActivity(bundle);
                } else {
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
                }
            }
            break;
            case R.id.tv_view_teams: {
                handleViewTeamsClick();
            }
            break;

            case R.id.ll_player_stats: {
                goToMatchStatsActivity(null);
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
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    private void handleViewTeamsClick() {
        MatchModel matchModel = getMatchModel();
        if (matchModel == null) return;
        if (matchModel.isFixtureMatch()) {
            showErrorMsg("Hang on! You'll be able to download teams after the deadline.");
        } else {
            getContestPdf();
        }
    }

    private void openTeamPreviewDialog(Bundle bundle) {
        TeamPreviewDialog instance = TeamPreviewDialog.getInstance(bundle);
        instance.setCustomerTeamModel(null);
        instance.setTeamPreviewDialogListener(new TeamPreviewDialog.TeamPreviewDialogListener() {
            @Override
            public void needTeamEdit(CustomerTeamModel customerTeamModel) {
                Bundle bundle = new Bundle();
                bundle.putLong(DATA1, customerTeamModel.getId());
                bundle.putString(DATA, new Gson().toJson(customerTeamModel));
                goToCreateTeamActivity(bundle);
            }

        });
        instance.show(getFm(), instance.getClass().getSimpleName());

    }

    private void goToContestWinnerBreakup(Bundle bundle) {
        SoccerWinnerBreakupDialog instance = SoccerWinnerBreakupDialog.getInstance(bundle);
        instance.show(getFm(), instance.getClass().getSimpleName());
    }

    private void goToContestWinnerBreakupExpert(Bundle bundle) {
        WinnerBreakupExpertDialog instance = WinnerBreakupExpertDialog.getInstance(bundle);
        instance.show(getFm(), instance.getClass().getSimpleName());
    }


    private void goToSwitchTeamActivity(Bundle bundle) {
        Intent intent = new Intent(this, SwitchSoccerTeamActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, ContestActivity.REQUEST_SWITCH_TEAM);
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }


    private void openConfirmJoinContest(final String entryFee, final long matchContestId, final long teamId) {
        if (matchContestId == -1 || teamId == -1) return;
        Bundle bundle = new Bundle();
        bundle.putLong(DATA, matchContestId);
        bundle.putString(DATA4, entryFee);
        bundle.putString(DATA3, String.valueOf(teamId));
        confirmationJoinContestDialog = ConfirmationSoccerJoinContestDialog.getInstance(bundle);
        confirmationJoinContestDialog.setConfirmationJoinContestListener(new ConfirmationSoccerJoinContestDialog.ConfirmationJoinContestListener() {
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


    private void getContestTeams() {
        if (getMatchModel() != null) {
            setLoadingNextData(true);
            getWebRequestHelper().getSoccerContestTeams(getMatchModel().getMatch_id(),
                    getMatchContestId(), currentPages, this);
        }
    }

    private void getMatchScore() {
        if (getMatchModel() != null) {
            getWebRequestHelper().getSoccerMatchScore(getMatchModel().getMatch_id(), this);
        }
    }

    private void getContestDetail() {
        if (getMatchModel() != null) {
            getWebRequestHelper().getSoccerMatchContestDetail(getMatchContestId(),
                    getMatchModel().getMatch_id(), this);
        }
    }

    private void getContestPdf() {
        if (getMatchModel() != null) {
            displayProgressBar(false);
            getWebRequestHelper().getMatchContestPdf(getMatchContestId(),
                    getMatchModel().getMatch_id(), this);
        }
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


    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        if (webRequest.getWebRequestId() == ID_SOCCER_CONTEST_TEAMS) {
            setLoadingNextData(false);
        }
        if (webRequest.getWebRequestId() == ID_CONTEST_PDF ||
                webRequest.getWebRequestId() == ID_CUSTOMER_JOIN_CONTEST) {
            dismissProgressBar();
        }
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_SOCCER_CONTEST_TEAMS:
                handleContestTeamsResponse(webRequest);
                break;
            case ID_MATCH_SOCCER_CONTESTS_DETAIL:
                handleContestDetailResponse(webRequest);
                break;
            case ID_SOCCER_MATCH_SCORE:
                handleMatchScoreResponse(webRequest);
                break;
            case ID_CONTEST_PDF:
                handleContestPdfResponse(webRequest);
                break;
            case ID_CUSTOMER_JOIN_CONTEST:
                handleCustomerJoinContestResponse(webRequest);
                break;
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
                //showErrorMsg(responsePojo.getMessage());
            }
        }
    }

    private void handleContestTeamsResponse(WebRequest webRequest) {
        ContestTeamsResponseModel responsePojo = webRequest.getResponsePojo(ContestTeamsResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            List<ContestTeamModel> data = responsePojo.getData();
            if (data.size() == 0) {
                totalPages = currentPages;
            }
            if (currentPages == 1) {
                addData(data);
                //setupAdminTeamData(responsePojo.getAdmin_data());
            } else {
                updateData(data);
            }

            tv_all_teams.setText(String.valueOf(responsePojo.getTotal_teams()));
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }

    }

    private void addData(List<ContestTeamModel> list) {
        contestTeamModels.clear();
        if (list != null) {
            contestTeamModels.addAll(list);
        }
        if (!isFinishing() && adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void updateData(List<ContestTeamModel> list) {
        if (list != null) {
            int previousSize = contestTeamModels.size();
            contestTeamModels.addAll(list);
            int currentSize = contestTeamModels.size();

            if (!isFinishing() && adapter != null && previousSize < currentSize) {
                adapter.notifyItemChanged(previousSize - 1);
                adapter.notifyItemRangeInserted(previousSize, list.size());
            }
        }

    }

    private void handleContestDetailResponse(WebRequest webRequest) {
        MatchContestResponseModel responsePojo = webRequest.getResponsePojo(MatchContestResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            detailBean = responsePojo.getDetail();
            List<ContestCategoryModel> data = responsePojo.getData();
            if (data != null && data.size() > 0 && data.get(0).getContests() != null &&
                    data.get(0).getContests().size() > 0) {
                contestModel = data.get(0).getContests().get(0);
                contestCategoryModel = data.get(0);
            }
            if (isFinishing()) return;
            setContestData();
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }

    }

    private void handleContestPdfResponse(WebRequest webRequest) {
        ContestPdfResponseModel responsePojo = webRequest.getResponsePojo(ContestPdfResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError() && responsePojo.getData() != null) {
            ContestModel data = responsePojo.getData();
            if (isValidString(data.getContest_pdf())) {
                startFileDownload(data.getContest_pdf());
            } else {
                if (isFinishing()) return;
                showErrorMsg(responsePojo.getMessage());
            }
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
            getContestDetail();
            getContestTeams();
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
                getContestDetail();
                getContestTeams();
            }
        } else if (requestCode == ContestActivity.REQUEST_SWITCH_TEAM) {
            if (resultCode == RESULT_OK) {
                getContestDetail();
                getContestTeams();
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

    private void startFileDownload(String serverFileUrl) {
        if (!new ConnectionDetector(this).isConnectingToInternet()) {
            showNetWorkErrorMessage();
            return;
        }
        showCustomToast("Contest pdf downloading...");

        Uri urlUri = Uri.parse(serverFileUrl);
        DownloadManager.Request request = new DownloadManager.Request(urlUri);
        String lastPathSegment = urlUri.getLastPathSegment();
        if (lastPathSegment == null) {
            lastPathSegment = "File";
        }
        request.setDescription("Downloading" + " - " + lastPathSegment);
        request.setTitle(lastPathSegment);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }
}
