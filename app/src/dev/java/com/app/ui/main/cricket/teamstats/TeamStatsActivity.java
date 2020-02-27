package com.app.ui.main.cricket.teamstats;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.appbase.ViewPagerAdapter;
import com.app.model.CustomerTeamModel;
import com.app.model.MatchModel;
import com.app.model.PlayerStatsModel;
import com.app.model.webresponsemodel.CustomerTeamDetailResponseModel;
import com.app.ui.MyApplication;
import com.app.ui.main.ToolbarFragment;
import com.medy.retrofitwrapper.WebRequest;
import com.utilities.DeviceScreenUtil;

/**
 * Created by Vishnu Gupta on 7/1/19.
 */
public class TeamStatsActivity extends AppBaseActivity {


    ToolbarFragment toolbarFragment;


    ProgressBar pb_data;
    ViewPager view_pager;
    ViewPagerAdapter viewPagerAdapter;

    CustomerTeamModel customerTeamModel;


    public MatchModel getMatchModel() {
        return MyApplication.getInstance().getSelectedMatch();
    }

    public long getCustomerTeamId() {
        Bundle extras = getIntent().getExtras();
        return (extras == null ? -1 : extras.getLong(DATA1, -1));

    }

    public String getSelectedPlayerId() {
        Bundle extras = getIntent().getExtras();
        return (extras == null ? "" : extras.getString(DATA2, ""));
    }


    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_team_stats;
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();
        if (getMatchModel() != null && getCustomerTeamId() >= 0) {
            Fragment toolbar = getFm().findFragmentById(R.id.fragment_toolbar);
            if (toolbar instanceof AppBaseFragment) {
                toolbarFragment = (ToolbarFragment) toolbar;
                toolbarFragment.initializeComponent();
                toolbarFragment.setToolbarView(this);
            }

            int padding = DeviceScreenUtil.getInstance().convertDpToPixel(30.f);
            view_pager = findViewById(R.id.view_pager);
            view_pager.setClipToPadding(false);
            view_pager.setPadding(padding, 0, padding, Math.round(padding * 0.2f));
            view_pager.setPageMargin(Math.round(padding * 0.4f));

            pb_data = findViewById(R.id.pb_data);
            updateViewVisibitity(pb_data, View.GONE);
            getCustomerTeamStats();
        } else {
            onBackPressed();
        }
    }


    private void initializeViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(getFm());
        if (customerTeamModel != null && customerTeamModel.getPlayers_stats() != null) {
            String selectedPlayerId = getSelectedPlayerId();
            int selectedPlayerPosition = 0;
            for (int i = 0; i < customerTeamModel.getPlayers_stats().size(); i++) {
                PlayerStatsModel playerStatsModel = customerTeamModel.getPlayers_stats().get(i);
                TeamStatsFragment teamStatsFragment = new TeamStatsFragment();
                teamStatsFragment.setPlayerStatsModel(playerStatsModel);
                viewPagerAdapter.addFragment(teamStatsFragment, "");
                if (playerStatsModel.getPlayer_unique_id().equals(selectedPlayerId)) {
                    selectedPlayerPosition = i;
                }
            }

            view_pager.setAdapter(viewPagerAdapter);
            final int finalSelectedPlayerPosition = selectedPlayerPosition;
            view_pager.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        int position = finalSelectedPlayerPosition;
                        if (position > 0) {
                            view_pager.setCurrentItem(position);
                        }
                    }catch (Exception ignore){

                    }
                }
            });

        } else {
            view_pager.setAdapter(viewPagerAdapter);
        }

    }

    private void setupData() {
        if (customerTeamModel == null) {
            toolbarFragment.setLeftTitle("TEAM STATS");
        } else {
            toolbarFragment.setLeftTitle("TEAM STATS");
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    private void getCustomerTeamStats() {
        if (getMatchModel() != null) {
            if (getMatchModel().isFixtureMatch()) {
                showErrorMsg("Player stats available after match start.");
                onBackPressed();
                return;
            }
            setupData();
            updateViewVisibitity(pb_data, View.VISIBLE);

            if (getCustomerTeamId() == 0) {
                getWebRequestHelper().getDreamTeamStats(getMatchModel().getMatch_id(), this);
            } else {
                getWebRequestHelper().getGetCustomerTeamStats(getCustomerTeamId(), this);
            }

        }
    }


    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        updateViewVisibitity(pb_data, View.GONE);

        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_CUSTOMER_TEAM_STATS:
                handleCustomerTeamStatsResponse(webRequest);
                break;
        }

    }

    private void handleCustomerTeamStatsResponse(WebRequest webRequest) {
        CustomerTeamDetailResponseModel responsePojo = webRequest.getResponsePojo(CustomerTeamDetailResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError() && responsePojo.getData() != null) {
            customerTeamModel = responsePojo.getData();
            if (isFinishing()) return;
            setupData();
            initializeViewPager();
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }

    }

}
