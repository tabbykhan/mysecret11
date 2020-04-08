package com.app.ui.main.cricket.contest.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Filter;
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
import com.app.model.webresponsemodel.MatchContestResponseModel;
import com.app.ui.main.cricket.contest.ContestActivity;
import com.app.ui.main.cricket.contest.ContestActivity1;
import com.app.ui.main.cricket.contest.ContestAllFiltersActivity;
import com.app.ui.main.cricket.contest.adapter.ContestCategoryAdapter;
import com.app.ui.main.cricket.contest.contestdetail.ContestDetailActivity;
import com.app.ui.main.cricket.dialogs.winnerbreakupdialog.WinnerBreakupDialog;
import com.google.android.material.appbar.AppBarLayout;
import com.utilities.DeviceScreenUtil;
import com.utilities.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CashFragment extends AppBaseFragment {

    private RecyclerView recycler_view;
    private static ContestCategoryAdapter adapter;

    List<ContestCategoryModel> contestCategoryModels = new ArrayList<>();
    List<ContestCategoryModel> beatTheExpertModel = new ArrayList<>();
    MatchContestResponseModel.DetailBean detailBean;

    AppBarLayout.OnOffsetChangedListener appBarOnOffsetChangedListener=new AppBarLayout.OnOffsetChangedListener() {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
            if(swipe_layout!=null){
                swipe_layout.setEnabled(i == 0);
            }
        }
    };

    long viewMoreCatId = -1;
    int viewMoreCatIdPos = -1;
    private TextView tv_no_item;
    private AppBarLayout app_bar_layout;
    private SwipeRefreshLayout swipe_layout;
    private RelativeLayout rl_beat_the_expert;
    RelativeLayout rl_bottom_lay;
    private boolean view_info = false;
    TextView tv_more_entry;
    ImageView iv_discount_image;

    LinearLayout ll_filters_lay;
    LinearLayout ll_sort_winnings;
    LinearLayout ll_sort_entry_fee;
    LinearLayout ll_sort_winners;
    ImageView iv_sort_total_winnings;
    ImageView iv_sort_entry_fee;
    ImageView iv_sort_winners;

    View currentSortBy = null;
    int currentSortType = 0;//0 mean asyc 1 mean desc
    private TextView tv_contest_all_filter;

    public Filter getFilter() {
        return filter;
    }

    public View getCurrentSortBy() {
        return currentSortBy;
    }

    public int getCurrentSortType() {
        return currentSortType;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(contestCategoryModels!=null && contestCategoryModels.size()>0){
                for (ContestCategoryModel contestCategoryModel : contestCategoryModels) {
                    switch (currentSortBy.getId()) {
                        case R.id.iv_sort_total_winnings: {
                            contestCategoryModel.sortContest(1,currentSortType);
                        }
                        break;
                        case R.id.iv_sort_entry_fee: {
                            contestCategoryModel.sortContest(2,currentSortType);

                        }
                        break;
                        case R.id.iv_sort_winners: {
                            contestCategoryModel.sortContest(3,currentSortType);
                        }
                        break;
                    }
                }
            }
            return null;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (adapter != null && getActivity() != null) {
                adapter.notifyDataSetChanged();
            }
        }
    };


    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_case;
    }

    @Override
    public void onResume() {
        super.onResume();
        app_bar_layout.addOnOffsetChangedListener(appBarOnOffsetChangedListener);
        ((ContestActivity1) getActivity()).getMatchContest();
    }

    @Override
    public void onPause() {
        super.onPause();
        app_bar_layout.removeOnOffsetChangedListener(appBarOnOffsetChangedListener);
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();
        setupSwipeLayout();
        app_bar_layout = getView().findViewById(R.id.app_bar_layout);
        recycler_view = getView().findViewById(R.id.recycler_view);
        tv_no_item = getView().findViewById(R.id.tv_no_item);
        rl_beat_the_expert = getView().findViewById(R.id.rl_beat_the_expert);
        updateViewVisibitity(rl_beat_the_expert, View.GONE);
        rl_bottom_lay = getView().findViewById(R.id.rl_bottom_lay);
        tv_more_entry = getView().findViewById(R.id.tv_more_entry);
        iv_discount_image = getView().findViewById(R.id.iv_discount_image);
        tv_contest_all_filter=getView().findViewById(R.id.tv_contest_all_filter);
        ll_filters_lay = getView().findViewById(R.id.ll_filters_lay);
        updateViewVisibitity(ll_filters_lay,View.GONE);
        tv_contest_all_filter.setOnClickListener(this);

        ll_sort_winnings = getView().findViewById(R.id.ll_sort_winnings);
        ll_sort_winnings.setOnClickListener(this);

        ll_sort_entry_fee = getView().findViewById(R.id.ll_sort_entry_fee);
        ll_sort_entry_fee.setOnClickListener(this);

        ll_sort_winners = getView().findViewById(R.id.ll_sort_winners);
        ll_sort_winners.setOnClickListener(this);

        iv_sort_total_winnings = getView().findViewById(R.id.iv_sort_total_winnings);
        iv_sort_entry_fee = getView().findViewById(R.id.iv_sort_entry_fee);
        iv_sort_winners = getView().findViewById(R.id.iv_sort_winners);

        updateSortArrow(false);
        initializeRecyclerView(contestCategoryModels);

    }

    private void updateSortArrow(boolean needSorting) {
        if (currentSortBy == null) {
            updateViewVisibitity(iv_sort_total_winnings, View.INVISIBLE);
            updateViewVisibitity(iv_sort_entry_fee, View.INVISIBLE);
            updateViewVisibitity(iv_sort_winners, View.INVISIBLE);
        } else {
            switch (currentSortBy.getId()) {
                case R.id.iv_sort_total_winnings:
                    updateViewVisibitity(iv_sort_total_winnings, View.VISIBLE);
                    updateViewVisibitity(iv_sort_entry_fee, View.INVISIBLE);
                    updateViewVisibitity(iv_sort_winners, View.INVISIBLE);
                    break;
                case R.id.iv_sort_entry_fee:
                    updateViewVisibitity(iv_sort_total_winnings, View.INVISIBLE);
                    updateViewVisibitity(iv_sort_entry_fee, View.VISIBLE);
                    updateViewVisibitity(iv_sort_winners, View.INVISIBLE);
                    break;
                case R.id.iv_sort_winners:
                    updateViewVisibitity(iv_sort_total_winnings, View.INVISIBLE);
                    updateViewVisibitity(iv_sort_entry_fee, View.INVISIBLE);
                    updateViewVisibitity(iv_sort_winners, View.VISIBLE);
                    break;
            }

            if (currentSortType == 0) {
                currentSortBy.setRotation(180);
            } else {
                currentSortBy.setRotation(0);
            }
            if(needSorting&&adapter!=null){
                adapter.getFilter().filter(String.valueOf(currentSortBy.getId()));
            }

        }
    }

    @Override
    public void onPageSelected() {
        if (swipe_layout == null) return;
        ((ContestActivity1) getActivity()).getMatchContest();
    }

    public void showRefreshing(){
        if(swipe_layout!=null){
            swipe_layout.setRefreshing(true);
        }
    }

    private void initializeRecyclerView(final List<ContestCategoryModel> contestCategoryModels) {
        if(currentSortBy==null){
            currentSortBy = iv_sort_total_winnings;
            currentSortType = 1;
            updateSortArrow(false);
        }
        adapter = new ContestCategoryAdapter(getActivity(), contestCategoryModels) {
            @Override
            public int getLastItemBottomMargin() {
                return rl_bottom_lay.getHeight() + DeviceScreenUtil.getInstance().convertDpToPixel(10);
            }

            @Override
            public Filter getFilter() {
                return filter;
            }

            @Override
            public boolean isViewMoreEnable(long catId) {
                return catId == viewMoreCatId;
            }
        };

        recycler_view.setLayoutManager(getVerticalLinearLayoutManager());
        recycler_view.setNestedScrollingEnabled(true);
        recycler_view.setAdapter(adapter);
        new ItemClickSupport(recycler_view) {
            @Override
            public void onChildItemClicked(RecyclerView recyclerView, int parentPosition, int childPosition, View v) {
                switch (v.getId()) {
                    case R.id.tv_join: {
                        try {
                            ContestModel contestModel = CashFragment.this.contestCategoryModels.get(parentPosition).getContests().get(childPosition);
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
                                ((AppBaseActivity) getActivity()).goToCreateTeamActivity(bundle);
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putLong(DATA1, contestModel.getMatch_contest_id());
                                bundle.putString(DATA2, contestModel.getJoined_teams());
                                bundle.putBoolean(DATA9, contestModel.isMultiTeamAllowed());
                                ((AppBaseActivity) getActivity()).goToChooseTeamActivity(bundle);
                            }
                        } catch (Exception ignore) {

                        }

                    }
                    break;
                    case R.id.ll_winners: {
                        try {
                            ContestModel contestModel = CashFragment.this.contestCategoryModels.get(parentPosition).getContests().get(childPosition);
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
                            ((AppBaseActivity) getActivity()).goToSharePrivateContestDialog(bundle);
                        } catch (Exception ignore) {

                        }
                    }
                    break;

                    default: {
                        try {
                            ContestModel contestModel = CashFragment.this.contestCategoryModels.get(parentPosition).getContests().get(childPosition);
                            Bundle bundle = new Bundle();
                            bundle.putLong(DATA1, contestModel.getMatch_contest_id());
                            bundle.putString(DATA2, CashFragment.this.getClass().getSimpleName());
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
                            long id = CashFragment.this.contestCategoryModels.get(position).getId();
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
                ((ContestActivity1) getActivity()).getMatchContest();
            }
        });
    }

    private void goToContestWinnerBreakup(Bundle bundle) {
        WinnerBreakupDialog instance = WinnerBreakupDialog.getInstance(bundle);
        instance.show(getChildFragmentManager(), instance.getClass().getSimpleName());
    }

    private void goToContestDetailActivity(Bundle bundle) {
        Intent intent = new Intent(getActivity(), ContestDetailActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }
    private void goToContestFilterActivity(Bundle bundle) {
        Intent intent = new Intent(getActivity(), ContestAllFiltersActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, ContestActivity.REQUEST_FILTER_CONTEST);
        getActivity().overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    public void setupData(List<ContestCategoryModel> contestCategoryModels) {
        this.contestCategoryModels = contestCategoryModels;
        initializeRecyclerView(contestCategoryModels);
        adapter.notifyDataSetChanged();
        if (contestCategoryModels.size() > 0) {
            view_info = true;
            updateViewVisibitity(recycler_view, View.VISIBLE);
            updateViewVisibitity(ll_filters_lay, View.VISIBLE);
        } else {
            updateViewVisibitity(recycler_view, View.GONE);
            updateViewVisibitity(ll_filters_lay, View.GONE);
        }
        swipe_layout.setRefreshing(false);
        updateView();
    }

    public void setupDetsil(MatchContestResponseModel.DetailBean detailBean) {
        this.detailBean = detailBean;
    }

    private void updateView() {
        if (view_info) {
            updateViewVisibitity(tv_no_item, View.GONE);
        } else {
            updateViewVisibitity(tv_no_item, View.VISIBLE);
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ll_beat_the_expert: {
                try {
                    ContestModel contestModel = CashFragment.this.beatTheExpertModel.get(0).getContests().get(0);
                    Bundle bundle = new Bundle();
                    bundle.putLong(DATA1, contestModel.getMatch_contest_id());
                    bundle.putString(DATA2, CashFragment.this.getClass().getSimpleName());
                    goToContestDetailActivity(bundle);
                } catch (Exception ignore) {

                }
            }

            break;

            case R.id.ll_sort_winnings: {
                if (currentSortBy != null) {
                    if (currentSortBy.getId() != iv_sort_total_winnings.getId()) {
                        currentSortBy = iv_sort_total_winnings;
                        currentSortType = 1;
                    } else {
                        currentSortType = (currentSortType == 0) ? 1 : 0;
                    }
                } else {
                    currentSortBy = iv_sort_total_winnings;
                    currentSortType = 1;
                }
                updateSortArrow(true);
            }
            break;


            case R.id.ll_sort_entry_fee: {
                if (currentSortBy != null) {
                    if (currentSortBy.getId() != iv_sort_entry_fee.getId()) {
                        currentSortBy = iv_sort_entry_fee;
                        currentSortType = 0;
                    } else {
                        currentSortType = (currentSortType == 0) ? 1 : 0;
                    }
                } else {
                    currentSortBy = iv_sort_entry_fee;
                    currentSortType = 0;
                }
                updateSortArrow(true);
            }
            break;


            case R.id.ll_sort_winners: {
                if (currentSortBy != null) {
                    if (currentSortBy.getId() != iv_sort_winners.getId()) {
                        currentSortBy = iv_sort_winners;
                        currentSortType = 1;
                    } else {
                        currentSortType = (currentSortType == 0) ? 1 : 0;
                    }
                } else {
                    currentSortBy = iv_sort_winners;
                    currentSortType = 1;
                }
                updateSortArrow(true);
            }
            break;
            case  R.id.tv_contest_all_filter:
                goToContestFilterActivity(null);
                break;
        }
    }
}
