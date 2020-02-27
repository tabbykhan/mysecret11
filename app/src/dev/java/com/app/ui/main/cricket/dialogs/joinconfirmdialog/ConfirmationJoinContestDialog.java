package com.app.ui.main.cricket.dialogs.joinconfirmdialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.R;
import com.app.appbase.AppBaseDialogFragment;
import com.app.model.MatchModel;
import com.app.model.UserModel;
import com.app.model.webrequestmodel.CreatePrivateContestRequestModel;
import com.app.model.webrequestmodel.CustomerJoinContestRequestModel;
import com.app.model.webresponsemodel.CustomerJoinContestResponseModel;
import com.app.ui.MyApplication;
import com.base.BaseFragment;
import com.google.gson.Gson;
import com.medy.retrofitwrapper.WebRequest;
import com.tooltip.Tooltip;
import com.utilities.DeviceScreenUtil;


/**
 * @author Manish Kumar
 * @since 20/10/18
 */

public class ConfirmationJoinContestDialog extends AppBaseDialogFragment {

    CardView cv_data;
    ImageView iv_close;
    ImageView iv_info;
    TextView tv_remain_balance;
    TextView tv_entry;
    TextView tv_cash_bonus;
    TextView tv_pay;
    TextView tv_join_contest;
    RelativeLayout rl_dialog_wait;
    TextView tv_loader_msg;
    TextView tv_bottom_message;

    CustomerJoinContestResponseModel.PreJoinedModel preJoinedModel;


    ConfirmationJoinContestListener confirmationJoinContestListener;

    public static ConfirmationJoinContestDialog getInstance(Bundle bundle) {
        ConfirmationJoinContestDialog messageDialog = new ConfirmationJoinContestDialog();
        messageDialog.setArguments(bundle);
        return messageDialog;
    }

    public MatchModel getMatchModel() {
        return MyApplication.getInstance().getSelectedMatch();
    }

    public long getMatchContestId() {
        Bundle extras = getArguments();
        return (extras == null ? -1 : extras.getLong(DATA, -1));
    }

    public String getSelectedTeamId(){
        Bundle extras = getArguments();
        return (extras == null ? "" : extras.getString(DATA3, ""));
    }

    public CreatePrivateContestRequestModel getPrivateContestRequestModel() {
        Bundle extras = getArguments();
        String string = extras.getString(DATA2, "");
        if(isValidString(string)){
            return new Gson().fromJson(string,CreatePrivateContestRequestModel.class);
        }

        return null;
    }

    public String getEntryFee(){
        Bundle extras = getArguments();
        return (extras == null ? "" : extras.getString(DATA4, ""));
    }

    public void setConfirmationJoinContestListener(ConfirmationJoinContestListener confirmationJoinContestListener) {
        this.confirmationJoinContestListener = confirmationJoinContestListener;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.dialog_confirm_join_contest;
    }

    @Override
    public int getFragmentContainerResourceId(BaseFragment baseFragment) {
        return 0;
    }


    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        WindowManager.LayoutParams wlmp = dialog.getWindow().getAttributes();
        wlmp.gravity = Gravity.CENTER;
        wlmp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlmp.width = DeviceScreenUtil.getInstance().getWidth(0.90f);
        wlmp.dimAmount = 0.8f;
    }


    @Override
    public void initializeComponent() {
        super.initializeComponent();
        cv_data = getView().findViewById(R.id.cv_data);
        updateViewVisibitity(cv_data, View.INVISIBLE);
        iv_close = getView().findViewById(R.id.iv_close);
        iv_info = getView().findViewById(R.id.iv_info);
        tv_remain_balance = getView().findViewById(R.id.tv_remain_balance);
        tv_entry = getView().findViewById(R.id.tv_entry);
        tv_cash_bonus = getView().findViewById(R.id.tv_cash_bonus);
        tv_pay = getView().findViewById(R.id.tv_pay);
        tv_join_contest = getView().findViewById(R.id.tv_join_contest);
        tv_bottom_message = getView().findViewById(R.id.tv_bottom_message);
        rl_dialog_wait = getView().findViewById(R.id.rl_dialog_wait);
        tv_loader_msg = getView().findViewById(R.id.tv_loader_msg);
        tv_loader_msg.setText("");
        updateViewVisibitity(rl_dialog_wait, View.VISIBLE);

        iv_close.setOnClickListener(this);
        iv_info.setOnClickListener(this);
        tv_join_contest.setOnClickListener(this);
        updateBottomMessage();
        callPreJoinContest();

    }

    private void updateBottomMessage() {
        UserModel userModel = getUserModel();
        if (userModel != null && userModel.getSettings() != null) {
            tv_bottom_message.setText(userModel.getSettings().getJOIN_CONTEST_MESSAGE());
        } else {
            tv_bottom_message.setText("");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.iv_info:
                showCashBonusToolTip();
                break;
            case R.id.tv_join_contest:
                if (confirmationJoinContestListener != null) {
                    confirmationJoinContestListener.onProceed();
                }
                break;
        }
    }

    private void showCashBonusToolTip() {
        new Tooltip.Builder(iv_info)
                .setGravity(Gravity.BOTTOM)
                .setArrowEnabled(true)
                .setText("Money that you can use to join any public contests")
                .setTextColor(getContext().getResources().getColor(R.color.colorWhite))
                .setBackgroundColor(getContext().getResources().getColor(R.color.com_facebook_blue))
                .setCancelable(true)
                .setPadding(getResources().getDimensionPixelSize(R.dimen.dp3))
                .setCornerRadius(getResources().getDimension(R.dimen.dp3)).show();

    }


    private void setupData() {
        if (preJoinedModel != null) {
            if (preJoinedModel.getNeed_pay() > 0) {
                UserModel userModel = getUserModel();
                if (preJoinedModel.getWallet() != null && userModel != null) {
                    userModel.setWallet(preJoinedModel.getWallet());
                    updateUserInPrefs();
                }
                confirmationJoinContestListener.lowBalance(preJoinedModel);
            } else {
                tv_remain_balance.setText("Unutilized Balance + Winnings = " + currency_symbol + preJoinedModel.getRemainBalancetext());
                tv_entry.setText(currency_symbol + preJoinedModel.getEnteryFeestext());
                tv_cash_bonus.setText("- " + currency_symbol + preJoinedModel.getUsedBonusText());
                tv_pay.setText(currency_symbol + preJoinedModel.getToPayText());
                updateViewVisibitity(cv_data, View.VISIBLE);
                updateViewVisibitity(rl_dialog_wait, View.GONE);
            }

        } else {
            dismiss();
        }
    }

    public interface ConfirmationJoinContestListener {
        void onProceed();

        void lowBalance(CustomerJoinContestResponseModel.PreJoinedModel preJoinedModel);
    }

    private void callPreJoinContest() {
        if (getMatchModel() != null) {
            CreatePrivateContestRequestModel privateContestRequestModel = getPrivateContestRequestModel();
            if(privateContestRequestModel!=null && isValidString(getSelectedTeamId())){
                privateContestRequestModel.pre_join="Y";
                privateContestRequestModel.team_id=getSelectedTeamId();
                getWebRequestHelper().createPrivateContest(privateContestRequestModel, this);
                return;
            }
            long matchContestId = getMatchContestId();
            if (matchContestId == -1) {
                dismiss();
                return;
            }
            CustomerJoinContestRequestModel requestModel = new CustomerJoinContestRequestModel();
            requestModel.match_unique_id = getMatchModel().getMatch_id();
            requestModel.match_contest_id = matchContestId;
            requestModel.customer_team_ids = getSelectedTeamId();
            if(isValidString(getEntryFee())){
                requestModel.entry_fees=getEntryFee();
            }
            getWebRequestHelper().customerPreJoinContest(requestModel, this);
        }
    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        if(webRequest.getWebRequestId()!=ID_CREATE_PRIVATE_CONTEST) {
            super.onWebRequestResponse(webRequest);
        }
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412){
            dismiss();
            return;
        }
        switch (webRequest.getWebRequestId()) {
            case ID_CUSTOMER_PRE_JOIN_CONTEST:
                handleCustomerPreJoinContestResponse(webRequest);
                break;

            case ID_CREATE_PRIVATE_CONTEST:
                handleCustomerPreJoinContestResponse(webRequest);
                break;
        }

    }

    private void handleCustomerPreJoinContestResponse(WebRequest webRequest) {
        CustomerJoinContestResponseModel responsePojo = webRequest.getResponsePojo(CustomerJoinContestResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            if (isValidActivity()) {
                preJoinedModel = responsePojo.getData();
                setupData();
            }
        } else {
            if (isValidActivity()) {
                displayErrorDialog(responsePojo.getMessage());
                dismiss();
            }
        }

    }
}
