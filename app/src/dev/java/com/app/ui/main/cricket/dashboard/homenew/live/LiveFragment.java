package com.app.ui.main.cricket.dashboard.homenew.live;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.R;
import com.app.appbase.AppBaseFragment;
import com.app.model.MatchModel;
import com.app.model.webresponsemodel.UpcomingMatchesResponseModel;
import com.app.ui.MyApplication;
import com.app.ui.main.cricket.dashboard.homenew.live.adapters.LiveMatchAdapter;
import com.app.ui.main.cricket.mycontest.MyContestActivity;
import com.medy.retrofitwrapper.WebRequest;
import com.utilities.ItemClickSupport;

import java.util.List;


/**
 * Created by Vishnu Gupta on 10/1/19.
 */
public class LiveFragment extends AppBaseFragment {
    private LiveMatchAdapter adapter;
    private RecyclerView recycler_view;
    private SwipeRefreshLayout swipe_layout;
    private TextView tv_no_item;
    private TextView tv_upcoming_matches;


    @Override
    public int getLayoutResourceId() {
        return R.layout.fargment_home;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getView() != null) {
            return getView();
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

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


    private void initializeRecyclerView() {
        adapter = new LiveMatchAdapter(getActivity(), MyApplication.getInstance().getLiveMatches()) {
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
                    MatchModel matchModel = MyApplication.getInstance().getLiveMatches().get(position);
                    if (matchModel != null) {
                        MyApplication.getInstance().setSelectedMatch(matchModel);
                        MyApplication.getInstance().setIs_5_player_match(false);
                        Bundle bundle = new Bundle();
                        bundle.putString(DATA, matchModel.getMatch_id());
                        bundle.putString(DATA1, LiveFragment.this.getClass().getSimpleName());
                        goToMyContestActivity(bundle);
                    }
                } catch (Exception ignore) {

                }
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }


    private void goToMyContestActivity(Bundle bundle) {
        if (isFinishing()) return;
        Intent intent = new Intent(getActivity(), MyContestActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (isFinishing()) return;
        startActivity(intent);
        if (getActivity() == null) return;
        getActivity().overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    @Override
    public void onPageSelected() {
        if (swipe_layout == null) return;
        getUpcomingMatches();
    }

    private void getUpcomingMatches() {
        swipe_layout.setRefreshing(true);
        updateViewVisibitity(tv_no_item, View.GONE);
        getWebRequestHelper().getUpcomingMatches("L", this);
    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        if (swipe_layout != null)
            swipe_layout.setRefreshing(false);
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_UPCOMING_MATCHES:
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
                List<MatchModel> data = responsePojo.getData();
                MyApplication.getInstance().getLiveMatches().clear();
                if (data != null && data.size() > 0) {
                    MyApplication.getInstance().getLiveMatches().addAll(data);
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
        if (MyApplication.getInstance().getLiveMatches().size() > 0) {
            updateViewVisibitity(tv_no_item, View.GONE);
        } else {
            tv_no_item.setText("No Live match available");
            updateViewVisibitity(tv_no_item, View.VISIBLE);
        }
    }

}



