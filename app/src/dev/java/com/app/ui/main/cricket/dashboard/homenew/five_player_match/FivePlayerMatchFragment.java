package com.app.ui.main.cricket.dashboard.homenew.five_player_match;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.R;
import com.app.appbase.AppBaseFragment;
import com.app.model.MatchModel;
import com.app.model.webresponsemodel.UpcomingMatchesResponseModel;
import com.app.ui.MatchTimerListener;
import com.app.ui.MyApplication;
import com.app.ui.main.cricket.contest.ContestActivity1;
import com.app.ui.main.cricket.dashboard.homenew.five_player_match.adapters.FivePlayerMatchAdapter;
import com.google.gson.Gson;
import com.medy.retrofitwrapper.WebRequest;
import com.utilities.ItemClickSupport;

import java.util.List;

public class FivePlayerMatchFragment extends AppBaseFragment implements MatchTimerListener {

    private FivePlayerMatchAdapter adapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (getView() != null) {
            return getView();
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_five_player_match;
    }
    private RecyclerView recycler_view;
    private SwipeRefreshLayout swipe_layout;
    private TextView tv_no_item;
    private TextView tv_upcoming_matches;
    @Override
    public void initializeComponent() {
        super.initializeComponent();
        recycler_view = getView().findViewById(R.id.recycler_view);
        setupSwipeLayout();
        tv_no_item = getView().findViewById(R.id.tv_no_item);
        updateViewVisibitity(tv_no_item, View.GONE);
        tv_upcoming_matches = getView().findViewById(R.id.tv_upcoming_matches);
        updateViewVisibitity(tv_upcoming_matches,View.GONE);
        initializeRecyclerView();

        onPageSelected();
    }
    @Override
    public void onPageSelected() {
        if (swipe_layout == null) return;
        getUpcomingMatches();
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
    private void getUpcomingMatches() {
        swipe_layout.setRefreshing(true);
        updateViewVisibitity(tv_no_item, View.GONE);
        getWebRequestHelper().getFivePlayerMatch("F", this);
    }

    private void initializeRecyclerView() {
        adapter = new FivePlayerMatchAdapter(getActivity(), MyApplication.getInstance().getFivePlayerUpcomingMatches()) {
            @Override
            public int getViewHeight() {
                return Math.round(recycler_view.getWidth() * 0.26f);
            }
        };
        recycler_view.setNestedScrollingEnabled(true);
        recycler_view.setLayoutManager(getVerticalLinearLayoutManager());
        recycler_view.setAdapter(adapter);

        ItemClickSupport.addTo(recycler_view).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                try {
                    MatchModel matchModel = MyApplication.getInstance().getFivePlayerUpcomingMatches().get(position);
                    if (matchModel != null) {
                        if (matchModel.getContest_count() == 0) {
                            showErrorMsg("Contests for this match will open soon. Stay tuned!");
                        } else {
                            MyApplication.getInstance().setSelectedMatch(matchModel);
                            MyApplication.getInstance().setIs_5_player_match(true);
                            Bundle bundle = new Bundle();
                            bundle.putString(DATA, matchModel.getMatch_id());
                            goToContestActivity(bundle);
                        }

                    }
                } catch (Exception ignore) {

                }
            }
        });
    }
    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        if (swipe_layout != null)
            swipe_layout.setRefreshing(false);
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_5_PLAYER_MATCH:
                handleUpcomingMatchesResponse(webRequest);
                break;
        }

    }
    private void handleUpcomingMatchesResponse(WebRequest webRequest) {
        UpcomingMatchesResponseModel responsePojo = webRequest.getResponsePojo(UpcomingMatchesResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            MyApplication.getInstance().setServerDate(responsePojo.getServer_date());
            synchronized (MyApplication.getInstance().getLock()) {
                Log.i("5 match", new Gson().toJson(responsePojo.getData()));
                List<MatchModel> data = responsePojo.getData();
                MyApplication.getInstance().getFivePlayerUpcomingMatches().clear();
                if (data != null && data.size() > 0) {
                    MyApplication.getInstance().getFivePlayerUpcomingMatches().addAll(data);
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
        if (MyApplication.getInstance().getFivePlayerUpcomingMatches().size() > 0) {
            updateViewVisibitity(tv_no_item, View.GONE);
        } else {
            tv_no_item.setText("No Upcoming match available");
            updateViewVisibitity(tv_no_item, View.VISIBLE);
        }
    }
    private void goToContestActivity(Bundle bundle) {
        if (isFinishing()) return;
        Intent intent = new Intent(getActivity(), ContestActivity1.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (isFinishing()) return;
        startActivity(intent);
        if (getActivity() == null) return;
        getActivity().overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    @Override
    public void onMatchTimeUpdate() {
        if (isFinishing()) return;
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }
}
