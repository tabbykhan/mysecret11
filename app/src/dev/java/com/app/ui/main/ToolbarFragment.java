package com.app.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.model.UserModel;
import com.app.model.WalletModel;
import com.app.ui.checkuser.CheckUserActivity;
import com.app.ui.dialogs.DialogRevealProperty;
import com.app.ui.dialogs.WalletDialog;
import com.app.ui.forgotpassword.ForgotPasswordActivity;
import com.app.ui.forgotpassword.ForgotPasswordVerifyActivity;
import com.app.ui.login.LoginActivity;
import com.app.ui.main.addcash.AddCashActivity;
import com.app.ui.main.cricket.contest.ContestActivity;
import com.app.ui.main.cricket.contest.ContestActivity1;
import com.app.ui.main.cricket.contest.contestdetail.ContestDetailActivity;
import com.app.ui.main.cricket.contest.joinprivatecontest.JoinPrivateContestActivity;
import com.app.ui.main.cricket.contest.makeyourcontest.createcontest.CreatePrivateContestActivity;
import com.app.ui.main.cricket.dashboard.homenew.HomeFragment;
import com.app.ui.main.cricket.dashboard.mymatches.MyMatchesFragment;
import com.app.ui.main.cricket.matchstats.MatchStatsActivity;
import com.app.ui.main.cricket.mycontest.MyContestActivity;
import com.app.ui.main.cricket.myteam.choosecapitan.ChooseCaptionActivity;
import com.app.ui.main.cricket.myteam.chooseteam.ChooseTeamActivity;
import com.app.ui.main.cricket.myteam.createteam.CreateTeamActivity;
import com.app.ui.main.cricket.myteam.myteams.MyTeamsActivity;
import com.app.ui.main.cricket.myteam.switchteam.SwitchTeamActivity;
import com.app.ui.main.cricket.teamstats.TeamStatsActivity;
import com.app.ui.main.dashboard.DashboardActivityNew;
import com.app.ui.main.dashboard.more.MoreFragment;
import com.app.ui.main.dashboard.more.contestinvite.ContestInviteActivity;
import com.app.ui.main.dashboard.more.helpdesk.HelpDeskActivity;
import com.app.ui.main.dashboard.more.invite.InviteActivity;
import com.app.ui.main.dashboard.more.invite.InviteDetailActivity;
import com.app.ui.main.dashboard.profile.MyProfileActivity;
import com.app.ui.main.dashboard.profile.MyProfileFragment;
import com.app.ui.main.dashboard.profile.changepassword.ChangePasswordActivity;
import com.app.ui.main.dashboard.profile.editavatar.EditAvatarActivity;
import com.app.ui.main.dashboard.profile.myaccount.transactionhistory.TransactionHistoryActivity;
import com.app.ui.main.dashboard.profile.myaccount.withdrawlhistory.WithdrawHistoryActivity;
import com.app.ui.main.dashboard.profile.playerinfo.PlayerInfoActivity;
import com.app.ui.main.dashboard.profile.verification.VerificationActivity;
import com.app.ui.main.editteamname.EditTeamNameActivity;
import com.app.ui.main.notifications.NotificationsActivity;
import com.app.ui.main.webview.WebViewActivity;
import com.app.ui.main.withdrawcash.WithdrawActivity;
import com.app.ui.optverifiction.OtpVerifyActivity;
import com.app.ui.register.RegisterActivity;
import com.base.BaseFragment;
import com.utilities.DeviceScreenUtil;


/**
 * Created by Azher on 1/11/18.
 */
public class ToolbarFragment extends AppBaseFragment {


    private static int toolBarHeight = -1;


    private ImageView iv_back, iv_menu, iv_image_left;
    private TextView tv_title_left, tv_title_center;
    private RelativeLayout rl_notification;
    private TextView tv_notification_counter;
    private LinearLayout ll_wallet;
    private TextView tv_balance;

    private RelativeLayout rl_bg_toolbar;
    private ImageView iv_notification;
    private ImageView in_wallet;

    @Override
    public int getLayoutResourceId() {
        return R.layout.include_toolbar;
    }

    @Override
    public void initializeComponent() {
        if (getActivity() == null || getView() == null)
            return;
        iv_image_left = getView().findViewById(R.id.iv_image_left);
        iv_back = getView().findViewById(R.id.iv_back);
        iv_menu = getView().findViewById(R.id.iv_menu);
        tv_title_left = getView().findViewById(R.id.tv_title_left);
        tv_title_center = getView().findViewById(R.id.tv_title_center);
        rl_notification = getView().findViewById(R.id.rl_notification);
        tv_notification_counter = getView().findViewById(R.id.tv_notification_counter);
        updateViewVisibitity(tv_notification_counter, View.GONE);
        ll_wallet = getView().findViewById(R.id.ll_wallet);
        tv_balance = getView().findViewById(R.id.tv_balance);
        rl_bg_toolbar = getView().findViewById(R.id.rl_bg_toolbar);
        iv_notification = getView().findViewById(R.id.iv_notification);
        in_wallet = getView().findViewById(R.id.iv_wallet);

        ll_wallet.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_menu.setOnClickListener(this);
        rl_notification.setOnClickListener(this);

        if (toolBarHeight == -1) {
            iv_image_left.post(new Runnable() {
                @Override
                public void run() {
                    toolBarHeight = getView().getHeight() - DeviceScreenUtil.getInstance().convertDpToPixel(10.0f);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_image_left.getLayoutParams();
                    layoutParams.width = Math.round((toolBarHeight * 3.5f));
                    iv_image_left.setLayoutParams(layoutParams);
                }
            });
        } else {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_image_left.getLayoutParams();
            layoutParams.width = Math.round((toolBarHeight * 3.5f));
            iv_image_left.setLayoutParams(layoutParams);
        }

        setToolbarView(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserPrefs().addListener(this);
        updateWalletBalance();
        updateNotificationCounter();
    }

    @Override
    public void onPause() {
        super.onPause();
        getUserPrefs().removeListener(this);
    }

    @Override
    public void loggedInUserUpdate(UserModel userModel) {
        super.loggedInUserUpdate(userModel);
        updateWalletBalance();
        updateNotificationCounter();
    }

    private void updateNotificationCounter() {
        if (tv_notification_counter == null) return;
        UserModel userModel = getUserModel();
        if (userModel == null) return;
        if (userModel.getNotification_counter() > 0) {
            updateViewVisibitity(tv_notification_counter, View.VISIBLE);
            tv_notification_counter.setText(userModel.getNotificationCounterText());
        } else {
            updateViewVisibitity(tv_notification_counter, View.GONE);
        }
    }

    private void updateWalletBalance() {
        if (tv_balance == null) return;
        UserModel userModel = getUserModel();
        if (userModel == null) return;
        WalletModel wallet = userModel.getWallet();
        if (wallet == null) {
            tv_balance.setText(currency_symbol + "0.00");
        } else {
            tv_balance.setText(currency_symbol + wallet.getTotalWalletBalanceText());
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (getActivity() == null) return;
                getActivity().onBackPressed();
                break;

            case R.id.ll_wallet:
                if (getActivity() == null) return;
                showWalletDialog();
                break;
            case R.id.rl_notification:
                if (getActivity() == null) return;
                goToNotificationsActivity(null);
                break;
            default:
                if (getActivity() == null) return;
                ((ToolbarFragmentListener) getActivity()).onToolbarItemClick(v);

        }

    }

    private void goToNotificationsActivity(Bundle bundle) {
        Intent intent = new Intent(getActivity(), NotificationsActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        if (getActivity() == null) return;
        getActivity().overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }


    public void setCenterTitle(String title) {
        tv_title_center.setText(title);
    }

    public void setLeftTitle(String title) {
        tv_title_left.setText(title);
    }

    private void showWalletDialog() {
        int point[] = new int[2];
        ll_wallet.getLocationInWindow(point);
        int width = ll_wallet.getWidth();
        int cx = point[0] + Math.round(((width * 0.5f)));
        int cy = 0;
        int dx = Math.max(cx, getView().getWidth() - cx);
        int dy = Math.max(cy, DeviceScreenUtil.getInstance().getHeight(0.50f) - cy);
        float finalRadius = (float) Math.hypot(dx, dy);

        DialogRevealProperty dialogRevealProperty = new DialogRevealProperty();
        dialogRevealProperty.setCenterX(cx);
        dialogRevealProperty.setCenterY(cy);
        dialogRevealProperty.setStartRadius(0);
        dialogRevealProperty.setEndRadius(finalRadius);
        dialogRevealProperty.setRevealDuration(300);


        final WalletDialog walletDialog = new WalletDialog();
        walletDialog.setDialogRevealProperty(dialogRevealProperty);
        walletDialog.setTopMargin(getView().getHeight());
        walletDialog.setWalletDialogListener(new WalletDialog.WalletDialogListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_addmoney:
                        walletDialog.dismiss();
                        ((AppBaseActivity) getActivity()).goToAddCashActivity(null, 0);
                        break;
                    case R.id.tv_withdrawl:
                        walletDialog.dismiss();
                        ((AppBaseActivity) getActivity()).goToWithdrawActivity(null);
                        break;
                }
            }
        });
        if (getActivity() == null) return;
        walletDialog.show(((AppBaseActivity) getActivity()).getFm(), walletDialog.getClass().getSimpleName());
    }


    public void setToolbarView(Activity activity) {
        if (activity == null) return;
        UpdateHeaderTitel(false);
        if (activity instanceof CheckUserActivity) {
            updateViewVisibitity(ll_wallet, View.GONE);
            updateViewVisibitity(rl_notification, View.GONE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("LOG IN");
        } else if (activity instanceof RegisterActivity) {
            updateViewVisibitity(ll_wallet, View.GONE);
            updateViewVisibitity(rl_notification, View.GONE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("REGISTER & PLAY");
        } else if (activity instanceof LoginActivity) {
            updateViewVisibitity(ll_wallet, View.GONE);
            updateViewVisibitity(rl_notification, View.GONE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("LOG IN");
        } else if (activity instanceof OtpVerifyActivity) {
            updateViewVisibitity(ll_wallet, View.GONE);
            updateViewVisibitity(rl_notification, View.GONE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("VERIFY OTP");
        } else if (activity instanceof ForgotPasswordActivity) {
            updateViewVisibitity(ll_wallet, View.GONE);
            updateViewVisibitity(rl_notification, View.GONE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("FORGOT PASSWORD");
        } else if (activity instanceof ForgotPasswordVerifyActivity) {
            updateViewVisibitity(ll_wallet, View.GONE);
            updateViewVisibitity(rl_notification, View.GONE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("RESET PASSWORD");
        } else if (activity instanceof DashboardActivityNew) {


            BaseFragment latestFragment = ((DashboardActivityNew) activity).getLatestFragment(R.id.rl_fragment_container);

            if (latestFragment != null) {
                if (latestFragment instanceof HomeFragment) {
                    updateViewVisibitity(ll_wallet, View.VISIBLE);
                    updateViewVisibitity(rl_notification, View.VISIBLE);
                    updateViewVisibitity(iv_image_left, View.VISIBLE);
                    updateViewVisibitity(tv_title_left, View.GONE);
                    updateViewVisibitity(tv_title_center, View.GONE);
                    updateViewVisibitity(iv_back, View.GONE);
                    updateViewVisibitity(iv_menu, View.GONE);
                    UpdateHeaderTitel(true);


                } else if (latestFragment instanceof MyMatchesFragment) {
                    updateViewVisibitity(ll_wallet, View.VISIBLE);
                    updateViewVisibitity(rl_notification, View.VISIBLE);
                    updateViewVisibitity(iv_image_left, View.GONE);
                    updateViewVisibitity(tv_title_left, View.VISIBLE);
                    updateViewVisibitity(tv_title_center, View.GONE);
                    updateViewVisibitity(iv_back, View.VISIBLE);
                    updateViewVisibitity(iv_menu, View.GONE);
                    tv_title_left.setText("MY MATCHES");
                    UpdateHeaderTitel(false);
                } else if (latestFragment instanceof MoreFragment) {
                    updateViewVisibitity(ll_wallet, View.VISIBLE);
                    updateViewVisibitity(rl_notification, View.VISIBLE);
                    updateViewVisibitity(iv_image_left, View.GONE);
                    updateViewVisibitity(tv_title_left, View.VISIBLE);
                    updateViewVisibitity(tv_title_center, View.GONE);
                    updateViewVisibitity(iv_back, View.VISIBLE);
                    updateViewVisibitity(iv_menu, View.GONE);
                    tv_title_left.setText("MORE");
                    UpdateHeaderTitel(false);
                } else if (latestFragment instanceof MyProfileFragment) {
                    updateViewVisibitity(ll_wallet, View.GONE);
                    updateViewVisibitity(rl_notification, View.VISIBLE);
                    updateViewVisibitity(iv_image_left, View.GONE);
                    updateViewVisibitity(tv_title_left, View.VISIBLE);
                    updateViewVisibitity(tv_title_center, View.GONE);
                    updateViewVisibitity(iv_back, View.VISIBLE);
                    updateViewVisibitity(iv_menu, View.GONE);
                    tv_title_left.setText("PROFILE");
                    UpdateHeaderTitel(false);
                }
            } else {
                updateViewVisibitity(ll_wallet, View.VISIBLE);
                updateViewVisibitity(rl_notification, View.VISIBLE);
                updateViewVisibitity(iv_image_left, View.VISIBLE);
                updateViewVisibitity(tv_title_left, View.GONE);
                updateViewVisibitity(tv_title_center, View.GONE);
                updateViewVisibitity(iv_back, View.GONE);
                updateViewVisibitity(iv_menu, View.GONE);
            }


        } else if (activity instanceof ContestActivity) {
            updateViewVisibitity(ll_wallet, View.VISIBLE);
            updateViewVisibitity(rl_notification, View.VISIBLE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_left, View.VISIBLE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_back, View.VISIBLE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("CONTESTS");

        }else if (activity instanceof ContestActivity1) {
            updateViewVisibitity(ll_wallet, View.VISIBLE);
            updateViewVisibitity(rl_notification, View.VISIBLE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_left, View.VISIBLE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_back, View.VISIBLE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("CONTESTS");

        } else if (activity instanceof ContestDetailActivity) {
            updateViewVisibitity(ll_wallet, View.GONE);
            updateViewVisibitity(rl_notification, View.GONE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_left, View.VISIBLE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_back, View.VISIBLE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("CONTEST DETAIL");

        } else if (activity instanceof MatchStatsActivity) {
            updateViewVisibitity(ll_wallet, View.GONE);
            updateViewVisibitity(rl_notification, View.GONE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_left, View.VISIBLE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_back, View.VISIBLE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("PLAYERS STATS");

        } else if (activity instanceof SwitchTeamActivity) {
            updateViewVisibitity(ll_wallet, View.GONE);
            updateViewVisibitity(rl_notification, View.GONE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_left, View.VISIBLE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_back, View.VISIBLE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("SWITCH TEAM");

        } else if (activity instanceof MyContestActivity) {
            updateViewVisibitity(ll_wallet, View.VISIBLE);
            updateViewVisibitity(rl_notification, View.VISIBLE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_left, View.VISIBLE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_back, View.VISIBLE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("JOIN CONTEST");

        } else if (activity instanceof CreateTeamActivity) {
            updateViewVisibitity(ll_wallet, View.GONE);
            updateViewVisibitity(rl_notification, View.GONE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_left, View.VISIBLE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_back, View.VISIBLE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("CREATE YOUR SQUAD");

        } else if (activity instanceof ChooseCaptionActivity) {
            updateViewVisibitity(ll_wallet, View.GONE);
            updateViewVisibitity(rl_notification, View.GONE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_left, View.VISIBLE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_back, View.VISIBLE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("CHOOSE C & VC");

        } else if (activity instanceof MyTeamsActivity) {
            updateViewVisibitity(ll_wallet, View.GONE);
            updateViewVisibitity(rl_notification, View.GONE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_left, View.VISIBLE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_back, View.VISIBLE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("MY TEAMS");

        } else if (activity instanceof TeamStatsActivity) {
            updateViewVisibitity(ll_wallet, View.GONE);
            updateViewVisibitity(rl_notification, View.GONE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_left, View.VISIBLE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_back, View.VISIBLE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("TEAM STATS");

        } else if (activity instanceof ChooseTeamActivity) {
            updateViewVisibitity(ll_wallet, View.GONE);
            updateViewVisibitity(rl_notification, View.GONE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_left, View.VISIBLE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_back, View.VISIBLE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("SELECT TEAM");

        } else if (activity instanceof AddCashActivity) {
            updateViewVisibitity(ll_wallet, View.VISIBLE);
            updateViewVisibitity(rl_notification, View.GONE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_left, View.VISIBLE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_back, View.VISIBLE);
            updateViewVisibitity(iv_menu, View.GONE);
            if (((AddCashActivity) activity).getPreJoinedModel() != null) {
                tv_title_left.setText("LOW BALANCE");
            } else {
                tv_title_left.setText("ADD CASH");
            }

        } else if (activity instanceof EditTeamNameActivity) {
            updateViewVisibitity(ll_wallet, View.GONE);
            updateViewVisibitity(rl_notification, View.GONE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_left, View.VISIBLE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_back, View.VISIBLE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("SELECT YOUR TEAM NAME");
        } else if (activity instanceof PlayerInfoActivity) {
            updateViewVisibitity(ll_wallet, View.GONE);
            updateViewVisibitity(rl_notification, View.GONE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_left, View.VISIBLE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_back, View.VISIBLE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("EDIT PROFILE");

        } else if (activity instanceof EditAvatarActivity) {
            updateViewVisibitity(ll_wallet, View.GONE);
            updateViewVisibitity(rl_notification, View.GONE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_left, View.VISIBLE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_back, View.VISIBLE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("CHANGE AVATAR");

        } else if (activity instanceof ChangePasswordActivity) {
            updateViewVisibitity(ll_wallet, View.GONE);
            updateViewVisibitity(rl_notification, View.GONE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_left, View.VISIBLE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_back, View.VISIBLE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("CHANGE PASSWORD");

        }  else if (activity instanceof TransactionHistoryActivity) {
            updateViewVisibitity(ll_wallet, View.GONE);
            updateViewVisibitity(rl_notification, View.GONE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_left, View.VISIBLE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_back, View.VISIBLE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("TRANSACTION HISTORY");
        } else if (activity instanceof WithdrawHistoryActivity) {
            updateViewVisibitity(ll_wallet, View.GONE);
            updateViewVisibitity(rl_notification, View.GONE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_left, View.VISIBLE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_back, View.VISIBLE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("WITHDRAWL HISTORY");
        } else if (activity instanceof InviteActivity) {
            updateViewVisibitity(ll_wallet, View.GONE);
            updateViewVisibitity(rl_notification, View.GONE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_left, View.VISIBLE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_back, View.VISIBLE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("INVITE FIRENDS");

        } else if (activity instanceof ContestInviteActivity) {
            updateViewVisibitity(ll_wallet, View.GONE);
            updateViewVisibitity(rl_notification, View.GONE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_left, View.VISIBLE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_back, View.VISIBLE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("CONTEST INVITE CODE");

        } else if (activity instanceof HelpDeskActivity) {
            updateViewVisibitity(ll_wallet, View.GONE);
            updateViewVisibitity(rl_notification, View.GONE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_left, View.VISIBLE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_back, View.VISIBLE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText(((HelpDeskActivity)activity).getTitles());

        }else if (activity instanceof InviteDetailActivity) {
            updateViewVisibitity(ll_wallet, View.GONE);
            updateViewVisibitity(rl_notification, View.GONE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_left, View.VISIBLE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_back, View.VISIBLE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("INVITE FIRENDS");

        } else if (activity instanceof VerificationActivity) {
            updateViewVisibitity(ll_wallet, View.GONE);
            updateViewVisibitity(rl_notification, View.GONE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_left, View.VISIBLE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_back, View.VISIBLE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("VERIFY YOUR ACCOUNT");

        } else if (activity instanceof WithdrawActivity) {
            updateViewVisibitity(ll_wallet, View.GONE);
            updateViewVisibitity(rl_notification, View.GONE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_left, View.VISIBLE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_back, View.VISIBLE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("WITHDRAW");

        } else if (activity instanceof NotificationsActivity) {
            updateViewVisibitity(ll_wallet, View.GONE);
            updateViewVisibitity(rl_notification, View.GONE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_left, View.VISIBLE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_back, View.VISIBLE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("NOTIFICATIONS");

        } else if (activity instanceof WebViewActivity) {
            updateViewVisibitity(ll_wallet, View.GONE);
            updateViewVisibitity(rl_notification, View.GONE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_left, View.VISIBLE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_back, View.VISIBLE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("");

        }  else if (activity instanceof CreatePrivateContestActivity) {
            updateViewVisibitity(ll_wallet, View.GONE);
            updateViewVisibitity(rl_notification, View.GONE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_left, View.VISIBLE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_back, View.VISIBLE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("MAKE YOUR OWN CONTEST");

        }  else if (activity instanceof JoinPrivateContestActivity) {
            updateViewVisibitity(ll_wallet, View.GONE);
            updateViewVisibitity(rl_notification, View.GONE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_left, View.VISIBLE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_back, View.VISIBLE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("JOIN CONTEST");

        } else if (activity instanceof MyProfileActivity) {
            updateViewVisibitity(ll_wallet, View.GONE);
            updateViewVisibitity(rl_notification, View.VISIBLE);
            updateViewVisibitity(iv_image_left, View.GONE);
            updateViewVisibitity(tv_title_left, View.VISIBLE);
            updateViewVisibitity(tv_title_center, View.GONE);
            updateViewVisibitity(iv_back, View.VISIBLE);
            updateViewVisibitity(iv_menu, View.GONE);
            tv_title_left.setText("PROFILE");
        } else {

        }

    }

    public interface ToolbarFragmentListener {
        void onToolbarItemClick(View view);
    }

    private void UpdateHeaderTitel(Boolean aBoolean) {
        if (aBoolean) {
            rl_bg_toolbar.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_header_white));
            ll_wallet.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_red_10radius));
//            tv_notification_counter.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_notification_counter));
//            tv_notification_counter.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
            iv_notification.setColorFilter(getResources().getColor(R.color.colorRed), PorterDuff.Mode.SRC_IN);
            in_wallet.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_IN);
            tv_balance.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
        } else {
            rl_bg_toolbar.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_header));
            ll_wallet.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_white_10radius));
//            tv_notification_counter.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_notification_counter_white));
//            tv_notification_counter.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
            iv_notification.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_IN);
            in_wallet.setColorFilter(getResources().getColor(R.color.colorRed), PorterDuff.Mode.SRC_IN);
            tv_balance.setTextColor(getActivity().getResources().getColor(R.color.colorRed));
        }
    }
}
