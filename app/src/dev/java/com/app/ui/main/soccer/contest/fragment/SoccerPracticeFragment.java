package com.app.ui.main.soccer.contest.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.app.model.webresponsemodel.MatchContestResponseModel;
import com.app.ui.main.cricket.contest.ContestActivity1;
import com.app.ui.main.cricket.contest.adapter.ContestCategoryAdapter;
import com.app.ui.main.soccer.contest.contestdetail.SoccerContestDetailActivity;
import com.app.ui.main.soccer.dialog.SoccerWinnerBreakupDialog;
import com.utilities.DeviceScreenUtil;
import com.utilities.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SoccerPracticeFragment extends AppBaseFragment {

    private RecyclerView recycler_view;
    private static ContestCategoryAdapter adapter;

    List<ContestCategoryModel> contestCategoryModels = new ArrayList<>();
    MatchContestResponseModel.DetailBean detailBean;

    long viewMoreCatId = -1;
    int viewMoreCatIdPos = -1;
    private TextView tv_no_item;
    private SwipeRefreshLayout swipe_layout;
    RelativeLayout rl_bottom_lay;
    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_soccer_practice;
    }


    @Override
    public void initializeComponent() {
        super.initializeComponent();
        recycler_view = getView().findViewById(R.id.recycler_view);
        tv_no_item = getView().findViewById(R.id.tv_no_item);
        rl_bottom_lay = getView().findViewById(R.id.rl_bottom_lay);
        initializeRecyclerView(contestCategoryModels);
        setupSwipeLayout();
    }

    public void showRefreshing(){
        if(swipe_layout!=null){
            swipe_layout.setRefreshing(true);
        }
    }

    private void initializeRecyclerView(final List<ContestCategoryModel> contestCategoryModels) {
        adapter = new ContestCategoryAdapter(getActivity(), contestCategoryModels);
        recycler_view.setLayoutManager(getVerticalLinearLayoutManager());
        recycler_view.setAdapter(adapter);
        adapter = new ContestCategoryAdapter(getActivity(), contestCategoryModels) {
            @Override
            public int getLastItemBottomMargin() {
                return rl_bottom_lay.getHeight() + DeviceScreenUtil.getInstance().convertDpToPixel(10);
            }

            @Override
            public boolean isViewMoreEnable(long catId) {
                return catId == viewMoreCatId;
            }
        };
        new ItemClickSupport(recycler_view) {
            @Override
            public void onChildItemClicked(RecyclerView recyclerView, int parentPosition, int childPosition, View v) {
                switch (v.getId()) {
                    case R.id.tv_join: {
                        try {
                            ContestModel contestModel = SoccerPracticeFragment.this.contestCategoryModels.get(parentPosition).getContests().get(childPosition);
                            if (contestModel.isContestFull()) {
                                return;
                            }
                            if (!contestModel.isMoreJoinAvailable()) {
                                return;
                            }
                            if (detailBean == null) return;
                            boolean b = ((AppBaseActivity) getActivity()).checkContestJoinAvailable(contestModel);
                            if (!b) return;
                            if (detailBean.getTotal_teams() == 0) {
                                Bundle bundle = new Bundle();
                                bundle.putLong(DATA2, contestModel.getMatch_contest_id());
                                ((AppBaseActivity)getActivity()).goToCreateSoccerTeamActivity(bundle);
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putLong(DATA1, contestModel.getMatch_contest_id());
                                bundle.putString(DATA2, contestModel.getJoined_teams());
                                bundle.putBoolean(DATA9, contestModel.isMultiTeamAllowed());
                                ((AppBaseActivity)getActivity()).goToSoccerChooseTeamActivity(bundle);
                            }
                        } catch (Exception ignore) {

                        }

                    }
                    break;
                    case R.id.ll_winners: {
                        try {
                            ContestModel contestModel = SoccerPracticeFragment.this.contestCategoryModels.get(parentPosition).getContests().get(childPosition);
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
                            ((AppBaseActivity)getActivity()).goToShareSoccerPrivateContestDialog(bundle);
                        } catch (Exception ignore) {

                        }
                    }
                    break;

                    default: {
                        try {
                            ContestModel contestModel = SoccerPracticeFragment.this.contestCategoryModels.get(parentPosition).getContests().get(childPosition);
                            Bundle bundle = new Bundle();
                            bundle.putLong(DATA1, contestModel.getMatch_contest_id());
                            bundle.putString(DATA2, SoccerPracticeFragment.this.getClass().getSimpleName());
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
                            long id = SoccerPracticeFragment.this.contestCategoryModels.get(position).getId();
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

    private void setupSwipeLayout() {
        swipe_layout = getView().findViewById(R.id.swipe_layout);
        swipe_layout.setColorSchemeResources(R.color.colorOrange,
                R.color.colorPrimary);
        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_layout.setRefreshing(true);
                ((ContestActivity1)getActivity()).getMatchContest(null);
            }
        });
    }

    private void goToContestWinnerBreakup(Bundle bundle) {
        SoccerWinnerBreakupDialog instance = SoccerWinnerBreakupDialog.getInstance(bundle);
        instance.show(getChildFragmentManager(), instance.getClass().getSimpleName());
    }

    private void goToContestDetailActivity(Bundle bundle) {
        Intent intent = new Intent(getActivity(), SoccerContestDetailActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    public void setupData(List<ContestCategoryModel> contestCategoryModels) {
        this.contestCategoryModels = contestCategoryModels;
        if(contestCategoryModels.size()>0){
            tv_no_item.setVisibility(View.GONE);
            recycler_view.setVisibility(View.VISIBLE);
        }else {
            tv_no_item.setVisibility(View.VISIBLE);
            recycler_view.setVisibility(View.GONE);
        }
        initializeRecyclerView(contestCategoryModels);
        adapter.notifyDataSetChanged();
        swipe_layout.setRefreshing(false);
    }

    public void setupDetsil(MatchContestResponseModel.DetailBean detailBean) {
        this.detailBean = detailBean;
    }
}
