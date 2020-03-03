package com.app.ui.main.dashboard.profile.myaccount.transactionhistory;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ConstantsFlavor;
import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.appbase.HeaderDecoration;
import com.app.model.TransactionHistoryModel;
import com.app.model.webresponsemodel.TransactionHistoryResponseModel;
import com.app.ui.main.ToolbarFragment;
import com.app.ui.main.dashboard.profile.myaccount.transactionhistory.adapter.TransactionHistoryAdapter;
import com.medy.retrofitwrapper.WebRequest;
import com.utilities.DeviceScreenUtil;
import com.utilities.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Vishnu Gupta on 7/1/19.
 */
public class TransactionHistoryActivity extends AppBaseActivity implements AdapterView.OnItemSelectedListener {

    ToolbarFragment toolbarFragment;

    SwipeRefreshLayout swipe_layout;
    RecyclerView recycler_view;
    TextView tv_no_item;
    TransactionHistoryAdapter adapter;
    Spinner sp_transaction_type;
    String transaction_type = "All";

    List<TransactionHistoryModel> transactionHistoryModels = new ArrayList<>();

    private int totalPages = 1000;
    private int currentPages = 0;
    private boolean loadingNextData = false;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_transaction_history;
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
        sp_transaction_type = findViewById(R.id.sp_transaction_type);
        sp_transaction_type.setOnItemSelectedListener(this);


        setupSwipeLayout();
        initializeRecyclerView();
        setupPagination();
        //loadMoreData();

        setTransactionAdapter();
    }

    private void setTransactionAdapter() {
        if(ConstantsFlavor.type == ConstantsFlavor.Type.sportteam) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.transaction_array, R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
            sp_transaction_type.setAdapter(adapter);
        }else {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.transaction_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_transaction_type.setAdapter(adapter);
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


    private void initializeRecyclerView() {
        recycler_view.setLayoutManager(getVerticalLinearLayoutManager());
        adapter = new TransactionHistoryAdapter(this, transactionHistoryModels) {
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
                    TransactionHistoryModel transactionHistoryModel = transactionHistoryModels.get(position);
                    transactionHistoryModel.setOpened(!transactionHistoryModel.isOpened());
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
            getTransactionHistory();
            return;
        }

        if (totalPages > currentPages) {
            currentPages = currentPages + 1;
            getTransactionHistory();
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

    private void getTransactionHistory() {
        if (getUserModel() != null) {
            updateViewVisibitity(tv_no_item, View.GONE);
            setLoadingNextData(true);
            getWebRequestHelper().customerWalletHistory(currentPages,transaction_type, this);
        }
    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        setLoadingNextData(false);
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_WALLET_HISTORY:
                handleCustomerWalletHistoryResponse(webRequest);
                break;
        }

    }

    private void handleCustomerWalletHistoryResponse(WebRequest webRequest) {
        TransactionHistoryResponseModel responsePojo = webRequest.getResponsePojo(TransactionHistoryResponseModel.class);
        if (responsePojo == null) return;

        if (!responsePojo.isError()) {
            List<TransactionHistoryModel> data = responsePojo.getData();
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
        if(currentPages == 1){
            adapter = new TransactionHistoryAdapter(this, transactionHistoryModels);
            recycler_view.setAdapter(adapter);

        }
        updateNoDataView(responsePojo.getMessage());

    }

    private void addData(List<TransactionHistoryModel> list) {
        transactionHistoryModels.clear();
        if (list != null) {
            transactionHistoryModels.addAll(list);
        }
        if (!isFinishing() && adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void updateData(List<TransactionHistoryModel> list) {
        if (list != null) {
            int previousSize = transactionHistoryModels.size();
            transactionHistoryModels.addAll(list);
            int currentSize = transactionHistoryModels.size();

            if (!isFinishing() && adapter != null && previousSize < currentSize) {
                adapter.notifyItemRangeInserted(previousSize, list.size());
            }
        }

    }

    private void updateNoDataView(String msg) {
        if (transactionHistoryModels.size() > 0) {
            updateViewVisibitity(tv_no_item, View.GONE);
        } else {
            tv_no_item.setText(msg);
            updateViewVisibitity(tv_no_item, View.VISIBLE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sp_transaction_type:
                if(position == 0){
                    transaction_type = "All";
                }else if(position ==1){
                    transaction_type = "Join";
                }else if(position ==2){
                    transaction_type = "Win";
                }else if(position ==3){
                    transaction_type = "Refund";
                }else if(position ==4){
                    transaction_type = "Deposit";
                }else if(position ==5){
                    transaction_type = "Bonus";
                }
                //transaction_type = String.valueOf(parent.getItemAtPosition(position));
                currentPages = 0;
                loadMoreData();
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
