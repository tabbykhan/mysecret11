package com.app.ui.main.soccer.team.myteam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.app.model.CustomerTeamModel;
import com.app.model.MatchModel;
import com.app.model.webresponsemodel.CustomerTeamsResponseModel;
import com.app.ui.MatchTimerListener;
import com.app.ui.MyApplication;
import com.app.ui.main.ToolbarFragment;
import com.app.ui.main.cricket.contest.ContestActivity;
import com.app.ui.main.soccer.team.myteam.adapter.SoccerMyTeamsAdapter;
import com.app.ui.main.soccer.team.playerpreview.SoccerTeamPreviewDialog;
import com.google.gson.Gson;
import com.medy.retrofitwrapper.WebRequest;
import com.utilities.DeviceScreenUtil;
import com.utilities.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Vishnu Gupta on 4/2/19.
 */
public class SoccerMyTeamsActivity extends AppBaseActivity implements MatchTimerListener {

    ToolbarFragment toolbarFragment;

    TextView tv_match_name;
    TextView tv_timer_time;
    RelativeLayout rl_bottom_lay;
    LinearLayout ll_new_team;
    TextView tv_create_new_team;

    RecyclerView recycler_view;
    private SwipeRefreshLayout swipe_layout;

    SoccerMyTeamsAdapter adapter;


    List<CustomerTeamModel> customerTeamModels = new ArrayList<>();


    public MatchModel getMatchModel() {
        return MyApplication.getInstance().getSelectedMatch();
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
        return R.layout.activity_soccer_my_teams;
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

            tv_create_new_team = findViewById(R.id.tv_create_new_team);

            recycler_view = findViewById(R.id.recycler_view);
            setupSwipeLayout();
            updateViewVisibitity(rl_bottom_lay, View.INVISIBLE);
            setMatchData();
            initializeRecyclerView();
            getCustomerTeams();

            tv_create_new_team.setOnClickListener(this);
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
                getCustomerTeams();
            }
        });
    }


    private void initializeRecyclerView() {
        adapter = new SoccerMyTeamsAdapter(this, customerTeamModels) {
            @Override
            public int getLastItemBottomMargin() {
                return rl_bottom_lay.getHeight() + DeviceScreenUtil.getInstance().convertDpToPixel(10);
            }

            public boolean isCloneAvailable() {
                return customerTeamModels.size() < getMatchModel().getMatch_limit();
            }
        };
        recycler_view.setLayoutManager(getVerticalLinearLayoutManager());
        recycler_view.setAdapter(adapter);

        new ItemClickSupport(recycler_view).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                try {
                    switch (v.getId()) {
                        case R.id.iv_edit:
                        case R.id.ll_edit:{
                            CustomerTeamModel customerTeamModel = customerTeamModels.get(position);
                            Bundle bundle = new Bundle();
                            bundle.putLong(DATA1, customerTeamModel.getId());
                            bundle.putString(DATA, new Gson().toJson(customerTeamModel));
                            goToCreateSoccerTeamActivity(bundle);
                        }
                        break;

                        case R.id.iv_clone:
                        case R.id.ll_clone:{
                            CustomerTeamModel customerTeamModel = customerTeamModels.get(position);
                            Bundle bundle = new Bundle();
                            bundle.putString(DATA, new Gson().toJson(customerTeamModel));
                            goToCreateSoccerTeamActivity(bundle);
                        }
                        break;

                        default: {
                            CustomerTeamModel customerTeamModel = customerTeamModels.get(position);
                            openTeamPreviewDialog(customerTeamModel);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_create_new_team: {
                goToCreateSoccerTeamActivity(null);
            }
            break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
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
            swipe_layout.setRefreshing(true);
            getWebRequestHelper().getGetSoccerCustomerTeams(getMatchModel().getMatch_id(), this);
        }
    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        swipe_layout.setRefreshing(false);
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_GET_CUSTOMER_SOCCER_TEAMS:
                handleCustomerTeamsResponse(webRequest);
                break;
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
            Log.i("team creation limmt", ""+getMatchModel().getMatch_limit());
            tv_create_new_team.setText("CREATE TEAM " + String.valueOf(customerTeamModels.size() + 1));
            if (customerTeamModels.size() < getMatchModel().getMatch_limit()) {
                updateViewVisibitity(rl_bottom_lay, View.VISIBLE);
            } else {
                updateViewVisibitity(rl_bottom_lay, View.GONE);
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
        } else if (requestCode == ContestActivity.REQUEST_UPDATE_TEAM) {
            if (resultCode == RESULT_OK) {
                getCustomerTeams();
            }
        }
    }
}
