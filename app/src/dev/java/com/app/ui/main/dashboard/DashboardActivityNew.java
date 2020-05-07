package com.app.ui.main.dashboard;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseApplication;
import com.app.appbase.AppBaseFragment;
import com.app.appupdater.AppUpdateChecker;
import com.app.appupdater.DialogAppUpdater;
import com.app.model.AppVersionModel;
import com.app.model.CustomIconModel;
import com.app.model.GameModel;
import com.app.model.QuotationDontShowModel;
import com.app.model.QuotationModel;
import com.app.model.webresponsemodel.GamesResponseModel;
import com.app.ui.AppCustomIconsHelper;
import com.app.ui.MyApplication;
import com.app.ui.dialogs.quotation.QuotationDialog;
import com.app.ui.main.ToolbarFragment;
import com.app.ui.main.cricket.dashboard.homenew.HomeFragment;
import com.app.ui.main.cricket.dashboard.mymatches.MyMatchesFragment;
import com.app.ui.main.dashboard.more.MoreFragment;
import com.app.ui.main.dashboard.profile.MyProfileFragment;
import com.base.BaseFragment;
import com.google.android.material.tabs.TabLayout;
import com.medy.retrofitwrapper.WebRequest;

import java.util.List;


public class DashboardActivityNew extends AppBaseActivity {


    List<GameModel> gamesList;
    ImageView iv_home;
    TextView txt_home;
    ImageView iv_mycontest;
    TextView txt_mycontest;
    ImageView iv_account;
    TextView txt_account;
    ImageView iv_setting;
    TextView txt_setting;

    ToolbarFragment toolbarFragment;
    LinearLayout ll_home, ll_mycontest, ll_account, ll_setting;
    AppVersionModel appVersionModel;
    DialogAppUpdater dialogAppUpdater;
    private boolean is_home_tab_click = true;


    TabLayout tab_sports;
    RelativeLayout rl_fragment_container;

    Handler backStackHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                BaseFragment baseFragment = getLatestFragment(R.id.rl_fragment_container);
                setFooterIcon();
                if (baseFragment != null) {
                    baseFragment.viewCreateFromBackStack();
                }

            }
        }
    };
    private LinearLayout ll_5_player_match;

    @Override
    protected void onResume() {
        super.onResume();
        if (getUserPrefs() != null) {
            getUserPrefs().addListener(this);
        }
        if (appVersionModel != null && appVersionModel.isForceUpdate()) {
            try {
                if (dialogAppUpdater != null && dialogAppUpdater.isShowing()) {
                    dialogAppUpdater.dismiss();
                }
                dialogAppUpdater = new DialogAppUpdater(DashboardActivityNew.this, appVersionModel);
                dialogAppUpdater.show();
            } catch (Exception e) {

            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getUserPrefs() != null) {
            getUserPrefs().removeListener(this);
        }
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_dashboard_new;
    }


    @Override
    public int getFragmentContainerResourceId(BaseFragment baseFragment) {
        return R.id.rl_fragment_container;
    }

    private void setupBottomTabsUi() {
        if (iv_home == null || isFinishing()) return;
        CustomIconModel customIconModel = MyApplication.getInstance().getCustomIconModel();
        if (customIconModel != null) {
            CustomIconModel.IconModel tab_home = customIconModel.getTab_home();
            if (tab_home != null) {
                txt_home.setText(tab_home.getName());
                loadImage(this, iv_home, null, tab_home.getImage(), R.drawable.tab_icon_home, -1);
            } else {
                txt_home.setText("");
                iv_home.setImageResource(R.drawable.tab_icon_home);
            }
            CustomIconModel.IconModel tab_my_contest = customIconModel.getTab_my_contest();
            if (tab_my_contest != null) {
                txt_mycontest.setText(tab_my_contest.getName());
                loadImage(this, iv_mycontest, null, tab_my_contest.getImage(), R.drawable.tab_icon_mycontest, -1);
            } else {
                txt_mycontest.setText("");
                iv_mycontest.setImageResource(R.drawable.tab_icon_mycontest);
            }

            CustomIconModel.IconModel tab_account = customIconModel.getTab_account();
            if (tab_account != null) {
                txt_account.setText(tab_account.getName());
                loadImage(this, iv_account, null, tab_account.getImage(), R.drawable.profile_icon, -1);
            } else {
                txt_account.setText("");
                iv_account.setImageResource(R.drawable.profile_icon);
            }


            CustomIconModel.IconModel tab_more = customIconModel.getTab_more();
            if (tab_more != null) {
                txt_setting.setText(tab_more.getName());
                loadImage(this, iv_setting, null, tab_more.getImage(), R.drawable.more_icon, -1);
            } else {
                txt_setting.setText("");
                iv_setting.setImageResource(R.drawable.more_icon);
            }
        }

    }

    @Override
    public void customIconUpdate(CustomIconModel customIconModel) {
        super.customIconUpdate(customIconModel);
        setupBottomTabsUi();

    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();

        iv_home = findViewById(R.id.iv_home);
        txt_home = findViewById(R.id.txt_home);
        iv_mycontest = findViewById(R.id.iv_mycontest);
        txt_mycontest = findViewById(R.id.txt_mycontest);
        iv_account = findViewById(R.id.iv_account);
        txt_account = findViewById(R.id.txt_account);
        iv_setting = findViewById(R.id.iv_setting);
        txt_setting = findViewById(R.id.txt_setting);

        setupBottomTabsUi();

        Fragment toolbar = getFm().findFragmentById(R.id.fragment_toolbar);
        if (toolbar instanceof AppBaseFragment) {
            toolbarFragment = (ToolbarFragment) toolbar;
            toolbarFragment.initializeComponent();
        }

        FragmentManager.OnBackStackChangedListener onBackStackChangedListener = new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                backStackHandler.removeMessages(1);
                backStackHandler.sendEmptyMessageDelayed(1, 100);
            }
        };

        getFm().addOnBackStackChangedListener(onBackStackChangedListener);

        ll_home = findViewById(R.id.ll_home);
        ll_mycontest = findViewById(R.id.ll_mycontest);
        ll_account = findViewById(R.id.ll_account);
        ll_setting = findViewById(R.id.ll_setting);
        ll_5_player_match=findViewById(R.id.ll_5_player_match);

        ll_home.setOnClickListener(this);
        ll_mycontest.setOnClickListener(this);
        ll_account.setOnClickListener(this);
        ll_setting.setOnClickListener(this);

        ll_5_player_match.setOnClickListener(this);

        onBackStackChangedListener.onBackStackChanged();

        tab_sports = findViewById(R.id.tab_sports);


        rl_fragment_container = findViewById(R.id.rl_fragment_container);

        callGetProfile();
        new AppCustomIconsHelper().start();

        if (getWebRequestHelper() != null) {
            getWebRequestHelper().getGames(this);
        }

        new AppUpdateChecker(this, new AppUpdateChecker.AppUpdateAvailableListener() {
            @Override
            public void appUpdateAvailable(AppVersionModel appUpdatemodal) {
                DashboardActivityNew.this.appVersionModel = appUpdatemodal;
                if (appUpdatemodal == null) return;
                if (isFinishing()) return;
                try {
                    if (dialogAppUpdater != null && dialogAppUpdater.isShowing()) {
                        dialogAppUpdater.dismiss();
                    }
                    dialogAppUpdater = new DialogAppUpdater(DashboardActivityNew.this, appUpdatemodal);
                    dialogAppUpdater.show();
                } catch (Exception e) {

                }
            }
        }).checkForUpdate();

    }


    private void setupUi(int position) {
        GameModel gameModel = gamesList.get(position);

        MyApplication.getInstance().setCurrentGame(gameModel);

        if (gameModel.isCricket()) {
            HomeFragment sportsCricketFragment = new HomeFragment();
            FragmentManager manager = getFm();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.rl_fragment_container, sportsCricketFragment);
            transaction.commit();
        }

        QuotationDontShowModel quotationDontShowModel = MyApplication.getInstance().getQuotationDontShowModel();
        if (quotationDontShowModel != null) {
            boolean b = quotationDontShowModel.needShowQuotation(gameModel);
            if (b) {
                showQuotationDialog(gameModel);
            }
        }

    }

    private void showQuotationDialog(final GameModel gameModel) {
        try {
            final QuotationModel quotation = gameModel.getQuotation();
            if (!quotation.isValidQuotation()) return;
            final QuotationDialog quotationDialog = QuotationDialog.getInstance(null);
            quotationDialog.setQuotationModel(quotation);
            quotationDialog.setQuotationDialogListener(new QuotationDialog.QuotationDialogListener() {
                @Override
                public void onDontShowClick() {
                    QuotationDontShowModel quotationDontShowModel = MyApplication.getInstance().getQuotationDontShowModel();
                    if (quotationDontShowModel != null) {
                        quotationDontShowModel.addGame(gameModel);
                        MyApplication.getInstance().updateQuotationDontShowInPrefs();
                    }
                    quotationDialog.dismiss();
                }
            });
            quotationDialog.show(getFm(), quotationDialog.getClass().getSimpleName());
            gameModel.setQuotationShowing(true);
        } catch (Exception ignore) {

        }
    }

    public void addMyContestFragment() {
        if (gamesList == null || gamesList.size() == 0) {
            return;
        }
        int selectedTabPosition = tab_sports.getSelectedTabPosition();
        GameModel gameModel = gamesList.get(selectedTabPosition);

        if (gameModel.isCricket()) {
            BaseFragment baseFragment = getLatestFragment(R.id.rl_fragment_container);
            if ((baseFragment instanceof MyMatchesFragment)) return;

            MyMatchesFragment fragment = new MyMatchesFragment();
            changeFragment(fragment, true, true, 0, false);
        }
    }


    private void createTabs() {
        if (gamesList == null) return;
        if (tab_sports == null) return;
        long previousSelectedGameId = MyApplication.getInstance().getPreviousSelectedGameId();
        int previousSelectedPosition = -1;

        if (previousSelectedGameId == -1) {
            previousSelectedPosition = 0;
        } else {
            for (int i = 0; i < gamesList.size(); i++) {
                GameModel gameModel = gamesList.get(i);
                if (previousSelectedGameId == gameModel.getId()) {
                    previousSelectedPosition = i;
                    break;
                }
            }
        }

        if (previousSelectedPosition == -1) {
            previousSelectedPosition = 0;
        }


        for (int i = 0; i < gamesList.size(); i++) {

            tab_sports.addTab(tab_sports.newTab());

            GameModel gameModel = gamesList.get(i);

            TabLayout.Tab tabAt = tab_sports.getTabAt(i);
            View inflate = LayoutInflater.from(this).inflate(R.layout.include_sport_tab, null);
            LinearLayout ll_tab = inflate.findViewById(R.id.ll_tab);
            if (i == 0) {
                ll_tab.setBackground(getResources().getDrawable(R.drawable.tab_selecation_left));
            } else if (i == gamesList.size() - 1) {
                ll_tab.setBackground(getResources().getDrawable(R.drawable.tab_selecation_right));
            } else {
                ll_tab.setBackground(getResources().getDrawable(R.drawable.tab_selecation_center));
            }
            TextView tv_sport_type = inflate.findViewById(R.id.tv_sport_type);
            ImageView iv_sport_type = inflate.findViewById(R.id.iv_sport_type);
            tv_sport_type.setText(gameModel.getName());
            loadImage(this, iv_sport_type, null, gameModel.getImage(), R.mipmap.ic_launcher, -1);

            tabAt.setCustomView(inflate);

            if (i == previousSelectedPosition) {
                inflate.setActivated(true);
            } else {
                inflate.setActivated(false);
            }


        }
        TabLayout.Tab tabAt = tab_sports.getTabAt(previousSelectedPosition);
        tabAt.select();
        if (tab_sports.getTabCount() < 2) {
            updateViewVisibitity(tab_sports, View.GONE);
        } else {
            updateViewVisibitity(tab_sports, View.VISIBLE);
        }
        tab_sports.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int selectedTabPosition = tab_sports.getSelectedTabPosition();
                setupUi(selectedTabPosition);
                if (!is_home_tab_click)
                    addMyContestFragment(); // here for my contest tab work

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        setupUi(previousSelectedPosition);

        backStackHandler.sendEmptyMessageDelayed(1, 100);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ll_home:
                clearFragmentBackStack(0);
                break;
            case R.id.ll_mycontest:
                addMyContestFragment();
                break;

            case R.id.ll_account:
                addMyProfile();
                break;

//            case R.id.ll_5_player_match:
//                break;
            case R.id.ll_setting:
                addMorefragment();
                break;
        }
    }


    public void addMorefragment() {
        BaseFragment baseFragment = getLatestFragment(R.id.rl_fragment_container);
        if ((baseFragment instanceof MoreFragment)) return;

        MoreFragment fragment = new MoreFragment();
        changeFragment(fragment, true, true, 0, false);
    }


    public void addMyProfile() {
        BaseFragment baseFragment = getLatestFragment(R.id.rl_fragment_container);
        if ((baseFragment instanceof MyProfileFragment)) return;
        MyProfileFragment fragment = new MyProfileFragment();
        changeFragment(fragment, true, true, 0, false);


    }


    public void setFooterIcon() {
        BaseFragment baseFragment = getLatestFragment(R.id.rl_fragment_container);
        if (tab_sports.getTabCount() < 2) {
            updateViewVisibitity(tab_sports, View.GONE);
        } else {
            updateViewVisibitity(tab_sports, View.VISIBLE);
        }
        is_home_tab_click = true;
        if (baseFragment != null) {
            if (baseFragment instanceof HomeFragment) {
                ll_home.setActivated(true);
                ll_mycontest.setActivated(false);
                ll_account.setActivated(false);
                ll_setting.setActivated(false);

            } else if (baseFragment instanceof MyMatchesFragment) {
                is_home_tab_click = false;
                ll_home.setActivated(false);
                ll_mycontest.setActivated(true);
                ll_account.setActivated(false);
                ll_setting.setActivated(false);


            } else if (baseFragment instanceof MyProfileFragment) {
                updateViewVisibitity(tab_sports, View.GONE);
                ll_home.setActivated(false);
                ll_mycontest.setActivated(false);
                ll_account.setActivated(true);
                ll_setting.setActivated(false);
            } else if (baseFragment instanceof MoreFragment) {
                updateViewVisibitity(tab_sports, View.GONE);
                ll_home.setActivated(false);
                ll_mycontest.setActivated(false);
                ll_account.setActivated(false);
                ll_setting.setActivated(true);

            }
        }
        if (toolbarFragment != null) {
            toolbarFragment.setToolbarView(this);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().stopTimer();
    }

    @Override
    public void onToolbarItemClick(View view) {
        switch (view.getId()) {

        }
    }


    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        super.onWebRequestResponse(webRequest);
        ((AppBaseApplication) getApplication()).onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_GET_GAMES:
                handleGamesResponse(webRequest);
                break;
        }
    }

    private void handleGamesResponse(WebRequest webRequest) {
        GamesResponseModel responsePojo = webRequest.getResponsePojo(GamesResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            this.gamesList = responsePojo.getData();
            createTabs();
        }
    }
}
