package com.app.ui.main.dashboard.profile.verification;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.appbase.ViewPagerAdapter;
import com.app.model.UserModel;
import com.app.model.webresponsemodel.VerifyOtpResponseModel;
import com.app.ui.main.ToolbarFragment;
import com.app.ui.main.dashboard.profile.verification.account.BankAccountFragment;
import com.app.ui.main.dashboard.profile.verification.mobile.MobileFragment;
import com.app.ui.main.dashboard.profile.verification.panCard.PanCardFragment;
import com.customviews.customtablayout.CustomTabLayout;
import com.customviews.customtablayout.CustomTabStrip;
import com.medy.retrofitwrapper.WebRequest;


public class VerificationActivity extends AppBaseActivity {

    ToolbarFragment toolbarFragment;

    CustomTabLayout verification_tabs;
    //TabLayout verification_tabs;
    ViewPager view_pager;
    ViewPagerAdapter adapter;
    private SwipeRefreshLayout swipe_layout;

    public boolean isAutoBack() {
        Bundle extras = getIntent().getExtras();
        return extras != null && extras.getBoolean(DATA, false);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_verification;
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


        verification_tabs = findViewById(R.id.verification_tabs);
        view_pager = findViewById(R.id.view_pager);

        setupViewPager();
        setupTabs();
        //callMyProfile();
        setupSwipeLayout();
    }

    private void setupViewPager() {
        adapter = new ViewPagerAdapter(getFm());
        adapter.addFragment(new MobileFragment(), "MOBILE & EMAIL");
        adapter.addFragment(new PanCardFragment(), "PAN");
        adapter.addFragment(new BankAccountFragment(), "BANK");

        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });

        view_pager.setAdapter(adapter);
        verification_tabs.setViewPager(view_pager);
    }

    private void setupTabs() {
        verification_tabs.post(new Runnable() {
            @Override
            public void run() {
                CustomTabStrip tabStrip = verification_tabs.getTabStrip();
                int childCount = tabStrip.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View childAt = tabStrip.getChildAt(i);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) childAt.getLayoutParams();
                    lp.weight = 1.0f;
                    lp.width = -1;
                    childAt.setLayoutParams(lp);
                }

            }
        });
    }

    private void setupSwipeLayout() {
        swipe_layout = findViewById(R.id.swipe_layout);
        swipe_layout.setColorSchemeResources(R.color.colorWhite,
                R.color.colorBlack);
        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callMyProfile();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    private void callMyProfile() {
        swipe_layout.setRefreshing(true);
        //displayProgressBar(false, "Wait...");
        getWebRequestHelper().getProfile(this);
    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        if (swipe_layout != null)
            swipe_layout.setRefreshing(false);
        dismissProgressBar();
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_GET_PROFILE:
                handleProfileResponse(webRequest);
                break;
        }

    }

    private void handleProfileResponse(WebRequest webRequest) {
        VerifyOtpResponseModel responsePojo = webRequest.getResponsePojo(VerifyOtpResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            UserModel data = responsePojo.getData();
            if (data == null) return;
            if (isFinishing()) return;
            // getUserPrefs().updateLoggedInUser(data);
            if (data.isWithdrawAvailable()) {
                goToWithdrawActivity(null);
                supportFinishAfterTransition();
                return;
            }

            AppBaseFragment item = adapter.getItem(0);
            ((MobileFragment) item).setupData();
            item = adapter.getItem(1);
            ((PanCardFragment) item).setupData();
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }
    }


    public void panCardUpdated() {
        if (adapter == null || isFinishing()) return;
        AppBaseFragment item = adapter.getItem(2);
        ((BankAccountFragment) item).setupData();
    }

    @Override
    public void otpMessageReceived (String messageText) {
        AppBaseFragment item = adapter.getItem(0);
        ((MobileFragment) item).setupDataOtp(messageText);
    }

    @Override
    protected void onStart () {
        registerSmsReceiver();
        super.onStart();
    }

    @Override
    protected void onStop () {
        super.onStop();
        unregisterSmsReceiver();
    }

}
