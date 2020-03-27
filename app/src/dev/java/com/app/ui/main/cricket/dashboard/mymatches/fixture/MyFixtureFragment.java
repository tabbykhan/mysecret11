package com.app.ui.main.cricket.dashboard.mymatches.fixture;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.R;
import com.app.appbase.AppBaseFragment;
import com.app.model.MatchModel;
import com.app.model.webresponsemodel.UpcomingMatchesResponseModel;
import com.app.ui.MatchTimerListener;
import com.app.ui.MyApplication;
import com.app.ui.main.cricket.dashboard.mymatches.fixture.adapters.MyFixtureAdapter;
import com.app.ui.main.cricket.mycontest.MyContestActivity;
import com.google.gson.Gson;
import com.medy.retrofitwrapper.WebRequest;
import com.utilities.DeviceScreenUtil;
import com.utilities.ItemClickSupport;

import java.util.List;


/**
 * Created by Vishnu Gupta on 10/1/19.
 */
public class MyFixtureFragment extends AppBaseFragment implements MatchTimerListener {
    private RecyclerView recycler_view;
    private SwipeRefreshLayout swipe_layout;
    private TextView tv_no_item;
    private LinearLayout ll_no_item;
    private TextView tv_join_contest;

    private MyFixtureAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getView() != null) {
            return getView();
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fargment_my_fixture;
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();
        recycler_view = getView().findViewById(R.id.recycler_view);
        setupSwipeLayout();
        tv_no_item = getView().findViewById(R.id.tv_no_item);
        ll_no_item = getView().findViewById(R.id.ll_no_item);
        tv_join_contest = getView().findViewById(R.id.tv_join_contest);
        updateViewVisibitity(ll_no_item, View.GONE);
        updateViewVisibitity(tv_no_item, View.GONE);
        initializeRecyclerView();
        onPageSelected();
        tv_join_contest.setOnClickListener(this);
    }

    private void setupSwipeLayout() {
        swipe_layout = getView().findViewById(R.id.swipe_layout);
        swipe_layout.setColorSchemeResources(R.color.colorOrange,
                R.color.colorPrimary);
        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onPageSelected();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_join_contest:
               getActivity().onBackPressed();
                break;
        }
    }

    private void initializeRecyclerView() {
        adapter = new MyFixtureAdapter(getActivity(), MyApplication.getInstance().getMyUpcomingMatches()) {
            @Override
            public int getViewHeight() {
                return Math.round(recycler_view.getWidth() * 0.26f) + DeviceScreenUtil.getInstance().convertDpToPixel(30.0f);
            }
        };
        recycler_view.setNestedScrollingEnabled(true);
        recycler_view.setLayoutManager(getVerticalLinearLayoutManager());
        recycler_view.setAdapter(adapter);

        ItemClickSupport.addTo(recycler_view).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                try {
                    MatchModel matchModel = MyApplication.getInstance().getMyUpcomingMatches().get(position);
                    if (matchModel != null) {
                        MyApplication.getInstance().setSelectedMatch(matchModel);
                        MyApplication.getInstance().setIs_5_player_match(false);
                        Bundle bundle = new Bundle();
                        bundle.putString(DATA, matchModel.getMatch_id());
                        bundle.putString(DATA1, MyFixtureFragment.this.getClass().getSimpleName());
                        goToMyContestActivity(bundle);
                    }
                } catch (Exception ignore) {

                }
            }
        });
    }

    private void goToMyContestActivity(Bundle bundle) {
        Intent intent = new Intent(getActivity(), MyContestActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        if (isFinishing()) return;
        getActivity().overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onPageSelected() {
        if (swipe_layout == null) return;
        getCustomerUpcomingMatches();
    }

    private void getCustomerUpcomingMatches() {
        swipe_layout.setRefreshing(true);
        updateViewVisibitity(ll_no_item, View.GONE);
        updateViewVisibitity(tv_no_item, View.GONE);
        getWebRequestHelper().getCustomerUpcomingMatches(this);
    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        if (swipe_layout != null)
            swipe_layout.setRefreshing(false);
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_CUSTOMER_UPCOMING_MATCHES:
                handleCustomerUpcomingMatchesResponse(webRequest);
                break;
        }

    }

    private void handleCustomerUpcomingMatchesResponse(WebRequest webRequest) {
        UpcomingMatchesResponseModel responsePojo = webRequest.getResponsePojo(UpcomingMatchesResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            MyApplication.getInstance().setServerDate(responsePojo.getServer_date());
            synchronized (MyApplication.getInstance().getLock()) {
                List<MatchModel> data = responsePojo.getData();
                Log.i("fixture match check ", new Gson().toJson(responsePojo.getData()));
                MyApplication.getInstance().getMyUpcomingMatches().clear();
                if (data != null && data.size() > 0) {
                    MyApplication.getInstance().getMyUpcomingMatches().addAll(data);
                }
                if (isFinishing()) return;
                adapter.notifyDataSetChanged();
                updateNoDataView();
                MyApplication.getInstance().startTimer();
            }
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }

    }

    private void updateNoDataView() {
        if (MyApplication.getInstance().getMyUpcomingMatches().size() > 0) {
            updateViewVisibitity(ll_no_item, View.GONE);
            updateViewVisibitity(tv_no_item, View.GONE);
        } else {
            updateViewVisibitity(ll_no_item, View.VISIBLE);
            updateViewVisibitity(tv_no_item, View.VISIBLE);
        }
    }


    @Override
    public void onMatchTimeUpdate() {
        if (isFinishing()) return;
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

}
