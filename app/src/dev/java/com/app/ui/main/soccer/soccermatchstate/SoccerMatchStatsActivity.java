package com.app.ui.main.soccer.soccermatchstate;

import android.view.View;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.model.MatchModel;
import com.app.model.PlayerStatsModel;
import com.app.model.webresponsemodel.PlayerStatsResponseModel;
import com.app.ui.MyApplication;
import com.app.ui.dialogs.playerstatsdialog.PlayerEventsDialog;
import com.app.ui.main.ToolbarFragment;
import com.app.ui.main.soccer.soccermatchstate.adapter.SoccerMatchStatsAdapter;
import com.medy.retrofitwrapper.WebRequest;
import com.utilities.DeviceScreenUtil;
import com.utilities.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vishnu Gupta on 7/1/19.
 */
public class SoccerMatchStatsActivity extends AppBaseActivity {


    ToolbarFragment toolbarFragment;


    SwipeRefreshLayout swipe_layout;
    RecyclerView recycler_view;
    LinearLayout rl_bottom_lay;
    LinearLayout ll_your_players;
    LinearLayout ll_top_players;

    SoccerMatchStatsAdapter adapter;

    List<PlayerStatsModel> playerStatsModels = new ArrayList<>();


    public MatchModel getMatchModel() {
        return MyApplication.getInstance().getSelectedMatch();
    }


    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_match_stats;
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

            setupSwipeLayout();
            rl_bottom_lay = findViewById(R.id.rl_bottom_lay);
            ll_your_players = findViewById(R.id.ll_your_players);
            ll_top_players = findViewById(R.id.ll_top_players);

            recycler_view = findViewById(R.id.recycler_view);
            setMatchData();
            initializeRecyclerView();
            getMatchPlayerStats();
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
                getMatchPlayerStats();
            }
        });
    }


    private void setMatchData() {
        if (getMatchModel() != null) {
            if (getMatchModel().isPastMatch()) {
                updateViewVisibitity(ll_top_players, View.VISIBLE);
            } else {
                updateViewVisibitity(ll_top_players, View.GONE);
            }
        }
    }


    private void initializeRecyclerView() {
        adapter = new SoccerMatchStatsAdapter(this, playerStatsModels) {
            @Override
            public int getLastItemBottomMargin() {
                return rl_bottom_lay.getHeight() + DeviceScreenUtil.getInstance().convertDpToPixel(10);
            }

            @Override
            public MatchModel getMatchModel() {
                return SoccerMatchStatsActivity.this.getMatchModel();
            }
        };
        recycler_view.setLayoutManager(getVerticalLinearLayoutManager());
        recycler_view.setAdapter(adapter);

        ItemClickSupport.addTo(recycler_view).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                try {
                    openPlayerEventsDialog(playerStatsModels.get(position));
                } catch (Exception ignore) {

                }

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    private void openPlayerEventsDialog(PlayerStatsModel playerStatsModel) {
        PlayerEventsDialog instance = PlayerEventsDialog.getInstance(null);
        instance.setPlayerStatsModel(playerStatsModel);
        instance.show(getFm(), instance.getClass().getSimpleName());
    }


    private void getMatchPlayerStats() {
        if (getMatchModel() != null) {
            swipe_layout.setRefreshing(true);
            getWebRequestHelper().getSoccerMatchPlayersStats(getMatchModel().getMatch_id(), this);
        }
    }


    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        swipe_layout.setRefreshing(false);
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_SOCCER_MATCH_PLAYERS_STATS:
                handleMatchPlayersStatsResponse(webRequest);
                break;
        }

    }

    private void handleMatchPlayersStatsResponse(WebRequest webRequest) {
        PlayerStatsResponseModel responsePojo = webRequest.getResponsePojo(PlayerStatsResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            List<PlayerStatsModel> data = responsePojo.getData();
            playerStatsModels.clear();
            if (data != null && data.size() > 0) {
                playerStatsModels.addAll(data);
            }
            if (isFinishing()) return;
            adapter.notifyDataSetChanged();
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }

    }

}
