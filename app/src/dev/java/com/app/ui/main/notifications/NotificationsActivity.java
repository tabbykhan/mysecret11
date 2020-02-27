package com.app.ui.main.notifications;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.appbase.HeaderDecoration;
import com.app.fcm.AppNotificationMessagingService;
import com.app.model.NotificationModel;
import com.app.model.UserModel;
import com.app.model.webresponsemodel.NotificationsResponseModel;
import com.app.ui.main.ToolbarFragment;
import com.app.ui.main.notifications.adapter.NotificationsAdapter;
import com.medy.retrofitwrapper.WebRequest;
import com.utilities.DeviceScreenUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Vishnu Gupta on 7/1/19.
 */
public class NotificationsActivity extends AppBaseActivity {


    ToolbarFragment toolbarFragment;


    RecyclerView recycler_view;
    private SwipeRefreshLayout swipe_layout;

    NotificationsAdapter adapter;


    List<NotificationModel> notificationModels = new ArrayList<>();

    private int totalPages = 1000;
    private int currentPages = 0;
    private boolean loadingNextData = false;


    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_notifications;
    }


    @Override
    public void initializeComponent() {
        super.initializeComponent();

        Fragment toolbar = getFm().findFragmentById(R.id.fragment_toolbar);
        if (toolbar instanceof AppBaseFragment) {
            toolbarFragment = (ToolbarFragment) toolbar;
            toolbarFragment.initializeComponent();
            toolbarFragment.setToolbarView(this);
        }


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
        adapter = new NotificationsAdapter(this, notificationModels) {
            @Override
            public int getLastItemBottomMargin() {
                return DeviceScreenUtil.getInstance().convertDpToPixel(12.0f);
            }
        };
        recycler_view.setLayoutManager(getVerticalLinearLayoutManager());

        final HeaderDecoration headerDecoration = new HeaderDecoration(adapter);
        recycler_view.addItemDecoration(headerDecoration);

        recycler_view.setAdapter(adapter);

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headerDecoration.invalidateHeaders();
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
            getNotifications();
            return;
        }

        if (totalPages > currentPages) {
            currentPages = currentPages + 1;
            getNotifications();
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


    private void getNotifications() {
        setLoadingNextData(true);
        getWebRequestHelper().getNotifications(currentPages, this);

    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        setLoadingNextData(false);
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_GET_NOTIFICATIONS:
                handleNotificationsResponse(webRequest);
                break;
        }

    }

    private void handleNotificationsResponse(WebRequest webRequest) {
        NotificationsResponseModel responsePojo = webRequest.getResponsePojo(NotificationsResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            List<NotificationModel> data = responsePojo.getData();
            if (data.size() == 0) {
                totalPages = currentPages;
            }
            if (currentPages == 1) {
                addData(data);
                UserModel userModel = getUserModel();
                if (userModel != null) {
                    userModel.setNotification_counter(0);
                    updateUserInPrefs();
                    AppNotificationMessagingService.cancelAllNotification(this);
                }
            } else {
                updateData(data);
            }
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }

    }


    private void addData(List<NotificationModel> list) {
        notificationModels.clear();
        if (list != null) {
            notificationModels.addAll(list);
        }
        if (!isFinishing() && adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void updateData(List<NotificationModel> list) {
        if (list != null) {
            int previousSize = notificationModels.size();
            notificationModels.addAll(list);
            int currentSize = notificationModels.size();

            if (!isFinishing() && adapter != null && previousSize < currentSize) {
                adapter.notifyItemRangeInserted(previousSize, list.size());
            }
        }

    }
}
