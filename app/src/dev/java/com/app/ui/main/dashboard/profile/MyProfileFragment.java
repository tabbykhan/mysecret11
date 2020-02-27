package com.app.ui.main.dashboard.profile;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.model.PlayingHistoryModel;
import com.app.model.UserModel;
import com.app.model.WalletModel;
import com.app.model.webrequestmodel.LogoutRequestModel;
import com.app.model.webresponsemodel.LogoutResponseModel;
import com.app.model.webresponsemodel.PlayingHistoryResponseModel;
import com.app.ui.MyApplication;
import com.app.ui.dialogs.ConfirmationDialog;
import com.app.ui.main.dashboard.profile.changepassword.ChangePasswordActivity;
import com.app.ui.main.dashboard.profile.editavatar.EditAvatarActivity;
import com.app.ui.main.editteamname.EditTeamNameActivity;
import com.medy.retrofitwrapper.WebRequest;


/**
 * Created by Vishnu Gupta on 7/1/19.
 */
public class MyProfileFragment extends AppBaseFragment {

    public static final int REQUEST_UPDATE_PROFILE = 102;

    ImageView iv_player;
    ProgressBar pb_image;
    ImageView iv_change_avatar;
    TextView tv_name;
    TextView tv_phone;
    TextView tv_email;
    ImageView iv_edit_profile;

    RelativeLayout rl_change_passowrd;

    TextView tv_team_name;
    ImageView iv_edit_team_name;

    ProgressBar pb_myaccount;
    TextView tv_deposit_balance;
    TextView tv_winning_balance;
    TextView tv_bonus_balance;
    TextView tv_addmoney;
    TextView tv_withdrawl;

    RelativeLayout rl_transaction_history;
    RelativeLayout rl_withdrawl_amount;

    ProgressBar pb_playing_history;
    TextView tv_playing_contest;
    TextView tv_playing_matches;
    TextView tv_playing_series;
    TextView tv_playing_wins;

    RelativeLayout rl_logout;
    private LinearLayout ll_phone;
    private LinearLayout ll_top_bar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getView() == null) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        return getView();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_my_profile;
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserPrefs().addListener(this);
        updateUserData();
        updateWalletData();
        updatePlayingData();
        callWalletDetail();
        callPlayingHistory();
    }


    @Override
    public void onPageSelected() {
        super.onPageSelected();
        callWalletDetail();
        callPlayingHistory();
    }

    @Override
    public void onPause() {
        super.onPause();
        getUserPrefs().removeListener(this);
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();

        iv_player = getView().findViewById(R.id.iv_player);
        ll_top_bar = getView().findViewById(R.id.ll_top_bar);
        updateViewVisibitity(ll_top_bar,View.GONE);
        pb_image = getView().findViewById(R.id.pb_image);
        iv_change_avatar = getView().findViewById(R.id.iv_change_avatar);
        tv_name = getView().findViewById(R.id.tv_name);
        tv_phone = getView().findViewById(R.id.tv_phone);
        tv_email = getView().findViewById(R.id.tv_email);
        iv_edit_profile = getView().findViewById(R.id.iv_edit_profile);

        rl_change_passowrd = getView().findViewById(R.id.rl_change_passowrd);

        tv_team_name = getView().findViewById(R.id.tv_team_name);
        iv_edit_team_name = getView().findViewById(R.id.iv_edit_team_name);

        pb_myaccount = getView().findViewById(R.id.pb_myaccount);
        tv_deposit_balance = getView().findViewById(R.id.tv_deposit_balance);
        tv_winning_balance = getView().findViewById(R.id.tv_winning_balance);
        tv_bonus_balance = getView().findViewById(R.id.tv_bonus_balance);
        tv_addmoney = getView().findViewById(R.id.tv_addmoney);
        tv_withdrawl = getView().findViewById(R.id.tv_withdrawl);

        rl_transaction_history = getView().findViewById(R.id.rl_transaction_history);
        rl_withdrawl_amount = getView().findViewById(R.id.rl_withdrawl_amount);

        pb_playing_history = getView().findViewById(R.id.pb_playing_history);
        tv_playing_contest = getView().findViewById(R.id.tv_playing_contest);
        tv_playing_matches = getView().findViewById(R.id.tv_playing_matches);
        tv_playing_series = getView().findViewById(R.id.tv_playing_series);
        tv_playing_wins = getView().findViewById(R.id.tv_playing_wins);

        rl_logout = getView().findViewById(R.id.rl_logout);
        ll_phone = getView().findViewById(R.id.ll_phone);


        iv_edit_profile.setOnClickListener(this);
        iv_change_avatar.setOnClickListener(this);
        rl_change_passowrd.setOnClickListener(this);

        iv_edit_team_name.setOnClickListener(this);

        tv_addmoney.setOnClickListener(this);
        tv_withdrawl.setOnClickListener(this);

        rl_transaction_history.setOnClickListener(this);
        rl_withdrawl_amount.setOnClickListener(this);

        rl_logout.setOnClickListener(this);
    }

    public void updateUserData() {
        UserModel userModel = getUserModel();
        if (userModel == null) {
            tv_name.setText("");
            tv_phone.setText("");
            tv_email.setText("");
            tv_team_name.setText("");
            iv_player.setImageResource(R.drawable.no_image);
            updateViewVisibitity(pb_image, View.GONE);
            updateViewVisibitity(iv_edit_team_name, View.INVISIBLE);
        } else {
            tv_name.setText(userModel.getFullName());
            tv_phone.setText(userModel.getFullMobile());
            tv_email.setText(userModel.getEmail());
            tv_team_name.setText(userModel.getTeam_name());
            ((AppBaseActivity) getActivity()).loadImage(this,
                    iv_player, pb_image, userModel.getImage(), R.drawable.no_image);
            if (userModel.isTeamChange()) {
                updateViewVisibitity(iv_edit_team_name, View.INVISIBLE);
            } else {
                updateViewVisibitity(iv_edit_team_name, View.VISIBLE);
            }
            if (!userModel.getFullMobile().equalsIgnoreCase("")) {
                ll_phone.setVisibility(View.VISIBLE);
            } else {
                ll_phone.setVisibility(View.GONE);
            }
        }
    }

    private void updateWalletData() {
        if (!isValidActivity()) return;
        updateViewVisibitity(pb_myaccount, View.GONE);
        UserModel userModel = getUserModel();
        if (userModel != null) {
            WalletModel wallet = userModel.getWallet();
            if (wallet != null) {
                tv_deposit_balance.setText(currency_symbol + wallet.getDeposit_walletText());
                tv_winning_balance.setText(currency_symbol + wallet.getWinning_walletText());
                tv_bonus_balance.setText(currency_symbol + wallet.getBonus_walletText());
            }
        } else {
            tv_deposit_balance.setText(currency_symbol + "0");
            tv_winning_balance.setText(currency_symbol + "0");
            tv_bonus_balance.setText(currency_symbol + "0");
        }
    }

    private void updatePlayingData() {
        if (!isValidActivity()) return;
        updateViewVisibitity(pb_playing_history, View.GONE);
        PlayingHistoryModel playingHistoryModel = getPlayingHistoryModel();
        if (playingHistoryModel != null) {
            tv_playing_contest.setText(String.valueOf(playingHistoryModel.getContests()));
            tv_playing_matches.setText(String.valueOf(playingHistoryModel.getMatches()));
            tv_playing_series.setText(String.valueOf(playingHistoryModel.getSeries()));
            tv_playing_wins.setText(String.valueOf(playingHistoryModel.getWins()));
        } else {
            tv_playing_contest.setText("0");
            tv_playing_matches.setText("0");
            tv_playing_series.setText("0");
            tv_playing_wins.setText("0");
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_change_avatar:
                goToEditAvatarActivity(null);
                break;
            case R.id.iv_edit_profile:
                if (getActivity() == null) return;
                ((AppBaseActivity) getActivity()).goToPlayerInfoActivity(null);
                break;
            case R.id.rl_change_passowrd:
                goToChangePasswordActivity(null);
                break;
            case R.id.iv_edit_team_name:
                goToEditTeamNameActivity(null);
                break;
            case R.id.tv_addmoney:
                if (getActivity() == null) return;
                ((AppBaseActivity) getActivity()).goToAddCashActivity(null, 0);
                break;
            case R.id.tv_withdrawl:
                if (getActivity() == null) return;
                ((AppBaseActivity) getActivity()).goToWithdrawActivity(null);
                break;
            case R.id.rl_transaction_history:
                if (getActivity() == null) return;
                ((AppBaseActivity) getActivity()).goToTransactionHistoryActivity(null);
                break;
            case R.id.rl_withdrawl_amount:
                if (getActivity() == null) return;
                ((AppBaseActivity) getActivity()).goToWithdrawHistoryActivity(null);
                break;
            case R.id.rl_logout:
                showLogoutDialog();
                break;


        }
    }




    private void goToEditAvatarActivity(Bundle bundle) {
        Intent intent = new Intent(getActivity(), EditAvatarActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (isFinishing()) return;
        startActivityForResult(intent, REQUEST_UPDATE_PROFILE);
        if (getActivity() == null) return;
        getActivity().overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    private void goToChangePasswordActivity(Bundle bundle) {
        Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (isFinishing()) return;
        startActivityForResult(intent, REQUEST_UPDATE_PROFILE);
        if (getActivity() == null) return;
        getActivity().overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }




    private void goToEditTeamNameActivity(Bundle bundle) {
        UserModel userModel = getUserModel();
        if (userModel.isTeamChange()) {
            return;
        }
        Intent intent = new Intent(getActivity(), EditTeamNameActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (isFinishing()) return;
        startActivityForResult(intent, REQUEST_UPDATE_PROFILE);
        if (getActivity() == null) return;
        getActivity().overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    private void showLogoutDialog() {
        Bundle bundle = new Bundle();
        bundle.putString(MESSAGE, "Are you sure want to logout?");
        bundle.putString(POS_BTN, "YES");
        bundle.putString(NEG_BTN, "NO");
        ConfirmationDialog instance = ConfirmationDialog.getInstance(bundle);
        instance.setOnClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        callLogout();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        });
        instance.show(getChildFm(), instance.getClass().getSimpleName());
    }

    private void callLogout() {
        displayProgressBar(false);
        LogoutRequestModel requestModel = new LogoutRequestModel();
        requestModel.device_id = MyApplication.getInstance().getDeviceId();
        getWebRequestHelper().logout(requestModel, this);
    }


    private void callWalletDetail() {
        if (this.pb_myaccount == null) return;
        updateViewVisibitity(pb_myaccount, View.VISIBLE);
        if (getActivity() == null) return;
        ((AppBaseActivity) getActivity()).callGetProfile();
    }

    private void callPlayingHistory() {
        if (this.pb_playing_history == null) return;
        updateViewVisibitity(pb_playing_history, View.VISIBLE);
        getWebRequestHelper().getPlayingHistory(this);
    }

    @Override
    public void loggedInUserUpdate(UserModel userModel) {
        super.loggedInUserUpdate(userModel);
        updateWalletData();
    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        dismissProgressBar();
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_LOGOUT:
                handleLogoutResponse(webRequest);
                break;
            case ID_PLAYING_HISTORY:
                handlePlayingHistoryResponse(webRequest);
                break;
        }

    }

    private void handleLogoutResponse(WebRequest webRequest) {
        LogoutResponseModel responsePojo = webRequest.getResponsePojo(LogoutResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            showCustomToast(responsePojo.getMessage());
            MyApplication.getInstance().unAuthorizedResponse(null);
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }
    }

    private void handlePlayingHistoryResponse(WebRequest webRequest) {
        PlayingHistoryResponseModel responsePojo = webRequest.getResponsePojo(PlayingHistoryResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            PlayingHistoryModel data = responsePojo.getData();
            if (data == null) return;
            if (isFinishing()) return;
            MyApplication.getInstance().savePlayingHistoryInPrefs(data);
            updatePlayingData();
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_UPDATE_PROFILE) {
            if (resultCode == Activity.RESULT_OK) {
                // updateUserData();
            }
        }
    }

}
