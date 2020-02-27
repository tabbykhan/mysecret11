package com.app.ui.main.dashboard.profile.myaccount.withdrawlhistory;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.appbase.HeaderDecoration;
import com.app.model.WithdrawHistoryModel;
import com.app.model.webresponsemodel.WithdrawHistoryResponseModel;
import com.app.ui.main.ToolbarFragment;
import com.app.ui.main.dashboard.profile.myaccount.withdrawlhistory.adapter.WithdrawHistoryAdapter;
import com.medy.retrofitwrapper.WebRequest;
import com.utilities.DeviceScreenUtil;
import com.utilities.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Vishnu Gupta on 7/1/19.
 */
public class WithdrawHistoryActivity extends AppBaseActivity {

    ToolbarFragment toolbarFragment;

    SwipeRefreshLayout swipe_layout;
    RecyclerView recycler_view;
    TextView tv_no_item;
    WithdrawHistoryAdapter adapter;

    List<WithdrawHistoryModel> withdrawHistoryModels = new ArrayList<>();

    private int totalPages = 1000;
    private int currentPages = 0;
    private boolean loadingNextData = false;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_withdraw_history;
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();
        Fragment toolbar = getFm().findFragmentById(R.id.fragment_toolbar);
        if (toolbar != null && toolbar instanceof AppBaseFragment) {
            toolbarFragment = (ToolbarFragment) toolbar;
            toolbarFragment.initializeComponent();
            toolbarFragment.setToolbarView(this);
        }

        tv_no_item = findViewById(R.id.tv_no_item);
        updateViewVisibitity(tv_no_item, View.GONE);
        recycler_view = findViewById(R.id.recycler_view);

        setupSwipeLayout();
        initializeRecyclerView();
        setupPagination();
        loadMoreData();

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

    private void initializeRecyclerView() {
        recycler_view.setLayoutManager(getVerticalLinearLayoutManager());
        adapter = new WithdrawHistoryAdapter(this, withdrawHistoryModels) {
            @Override
            public int getLastItemBottomMargin() {
                return DeviceScreenUtil.getInstance().convertDpToPixel(10);
            }
        };
        final HeaderDecoration headerDecoration = new HeaderDecoration(adapter);
        recycler_view.addItemDecoration(headerDecoration);


        recycler_view.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headerDecoration.invalidateHeaders();
            }
        });

        new ItemClickSupport(recycler_view).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                try {
                    WithdrawHistoryModel withdrawHistoryModel = withdrawHistoryModels.get(position);
                    withdrawHistoryModel.setOpened(!withdrawHistoryModel.isOpened());
                    adapter.notifyItemChanged(position);
                } catch (ArrayIndexOutOfBoundsException ignore) {

                }
            }
        });

    }


    private void setupPagination() {
        recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView,
                                   int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recycler_view.getLayoutManager() == null) return;
                int totalItemCount = (recycler_view.getLayoutManager()).getItemCount();
                int lastVisibleItem = ((LinearLayoutManager) recycler_view.getLayoutManager()).findLastVisibleItemPosition();
                if (!loadingNextData && totalItemCount <= (lastVisibleItem + 3)) {
                    loadMoreData();
                }
            }
        });
    }

    public void loadMoreData() {
        if (currentPages == 0) {
            currentPages = 1;
            totalPages = 1000;
            getWithdrawHistory();
            return;
        }

        if (totalPages > currentPages) {
            currentPages = currentPages + 1;
            getWithdrawHistory();
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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }


    private void getWithdrawHistory() {
        if (getUserModel() != null) {
            updateViewVisibitity(tv_no_item, View.GONE);
            setLoadingNextData(true);
            getWebRequestHelper().customerWithdrawHistory(currentPages, this);
        }
    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        setLoadingNextData(false);
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_WITHDRAW_HISTORY:
                handleCustomerWithdrawHistoryResponse(webRequest);
                break;
        }

    }

    private void handleCustomerWithdrawHistoryResponse(WebRequest webRequest) {
        WithdrawHistoryResponseModel responsePojo = webRequest.getResponsePojo(WithdrawHistoryResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            List<WithdrawHistoryModel> data = responsePojo.getData();
            if (data.size() == 0) {
                totalPages = currentPages;
            }
            if (currentPages == 1) {
                addData(data);
            } else {
                updateData(data);
            }
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }
        updateNoDataView(responsePojo.getMessage());

    }

    private void addData(List<WithdrawHistoryModel> list) {
        withdrawHistoryModels.clear();
        if (list != null) {
            withdrawHistoryModels.addAll(list);
        }
        if (!isFinishing() && adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void updateData(List<WithdrawHistoryModel> list) {
        if (list != null) {
            int previousSize = withdrawHistoryModels.size();
            withdrawHistoryModels.addAll(list);
            int currentSize = withdrawHistoryModels.size();

            if (!isFinishing() && adapter != null && previousSize < currentSize) {
                adapter.notifyItemRangeInserted(previousSize, list.size());
            }
        }

    }


    private void updateNoDataView(String msg) {
        if (withdrawHistoryModels.size() > 0) {
            updateViewVisibitity(tv_no_item, View.GONE);
        } else {
            tv_no_item.setText(msg);
            updateViewVisibitity(tv_no_item, View.VISIBLE);
        }
    }

}
