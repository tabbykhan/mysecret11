package com.app.ui.main.withdrawcash;

import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.model.BankDetailModel;
import com.app.model.SettingsModel;
import com.app.model.UserModel;
import com.app.model.WalletModel;
import com.app.model.webrequestmodel.WithdrawAmountRequestModel;
import com.app.model.webresponsemodel.WithdrawAmountResponseModel;
import com.app.ui.main.ToolbarFragment;
import com.medy.retrofitwrapper.WebRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WithdrawActivity extends AppBaseActivity {
    ToolbarFragment toolbarFragment;


    TextView tv_help_desk;
    TextView tv_winning_balance;
    CardView cv_bank_detail;
    TextView tv_account_number;
    TextView tv_account_name;
    TextView tv_account_ifsc;
    EditText et_amount;
    TextView tv_min_max;
    TextView tv_proceed;

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String data = s.toString().trim();
            if (isValidString(data)) {
                if (data.equals(currency_symbol.trim())) {
                    et_amount.setText("");
                    et_amount.setSelection(0);
                } else if (!data.startsWith(currency_symbol.trim())) {
                    data = currency_symbol + data;
                    et_amount.setText(data);
                    et_amount.setSelection(data.length());
                }
            }
        }
    };


    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_withdraw;
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();
        Fragment toolbar = getFm().findFragmentById(R.id.fragment_toolbar);
        if (toolbar instanceof AppBaseFragment) {
            toolbarFragment = (ToolbarFragment) toolbar;
            toolbarFragment.initializeComponent();
        }

        tv_help_desk = findViewById(R.id.tv_help_desk);
        setupHelpDeskButton();
        tv_winning_balance = findViewById(R.id.tv_winning_balance);
        cv_bank_detail = findViewById(R.id.cv_bank_detail);
        tv_account_number = findViewById(R.id.tv_account_number);
        tv_account_name = findViewById(R.id.tv_account_name);
        tv_account_ifsc = findViewById(R.id.tv_account_ifsc);
        tv_min_max = findViewById(R.id.tv_min_max);

        et_amount = findViewById(R.id.et_amount);
        et_amount.removeTextChangedListener(textWatcher);
        et_amount.addTextChangedListener(textWatcher);

        tv_proceed = findViewById(R.id.tv_proceed);

        tv_proceed.setOnClickListener(this);
        setupData();

    }

    private void setupHelpDeskButton() {
        final String signUp = " Help Desk";
        tv_help_desk.setMovementMethod(LinkMovementMethod.getInstance());
        Pattern termsAndConditionsMatcher = Pattern.compile(signUp);
        Matcher m1 = termsAndConditionsMatcher.matcher(tv_help_desk.getText().toString());
        if (m1.find()) {
            URLSpan urlSpan = new URLSpan(m1.group(0)) {
                @Override
                public void onClick(View widget) {
                    Bundle bundle=new Bundle();
                    bundle.putString(DATA1, signUp.trim());
                    goToHelpDeskActivity(bundle);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setColor(getResources().getColor(R.color.colorRed));
                    ds.setUnderlineText(false);//there show text below line
                }
            };
            SpannableString string = SpannableString.valueOf(tv_help_desk.getText());
            string.setSpan(urlSpan, m1.start(), m1.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

    }

    private void setupData() {
        UserModel userModel = getUserModel();
        if (userModel != null && userModel.isWithdrawAvailable()) {
            SettingsModel settings = userModel.getSettings();
            if (settings != null) {
                tv_min_max.setText("min " + currency_symbol + settings.getWITHDRAW_AMOUNT_MIN_Text() +
                        " & max " + currency_symbol + settings.getWITHDRAW_AMOUNT_MAX_Text() + " allowed per day");
            } else {
                tv_min_max.setText("");
            }
            WalletModel wallet = userModel.getWallet();
            tv_winning_balance.setText(currency_symbol + " " + wallet.getWinning_walletText());
            BankDetailModel bankdetail = userModel.getBankdetail();
            tv_account_number.setText("A/C " + bankdetail.getAccount_number());
            tv_account_name.setText(bankdetail.getAccount_holder_name());
            tv_account_ifsc.setText(bankdetail.getIfsc());

        } else {
            tv_winning_balance.setText(currency_symbol + " 0");
            tv_account_name.setText("");
            tv_account_number.setText("");
            tv_account_ifsc.setText("");
            tv_min_max.setText("");
            tv_proceed.setOnClickListener(null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_proceed:
                callWithdrawAmount();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    private void callWithdrawAmount() {
        String amount = et_amount.getText().toString().trim();
        if (amount.startsWith(currency_symbol) && amount.length() > 1) {
            amount = amount.substring(1).trim();
        }

        if (!isValidString(amount)) {
            showErrorMsg("Please enter amount");
            return;
        }

        if (!checkValidAmount(amount)) {
            showErrorMsg("Please enter valid amount");
            return;
        }
        float amt = Float.parseFloat(amount);

        SettingsModel settings = getUserModel().getSettings();
        if (settings != null) {
            if (amt < settings.getWITHDRAW_AMOUNT_MIN() || amt > settings.getWITHDRAW_AMOUNT_MAX()) {
                showErrorMsg(tv_min_max.getText().toString());
                return;
            }
        }


        WithdrawAmountRequestModel requestModel = new WithdrawAmountRequestModel();
        requestModel.amount = amt;
        displayProgressBar(false);
        getWebRequestHelper().customerWithdrawAmount(requestModel, this);
    }

    private boolean checkValidAmount(String amount) {
        if (isValidString(amount)) {
            try {
                double amt = Double.parseDouble(amount);
                return amt > 0;
            } catch (NumberFormatException e) {

            }
        }

        return false;
    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        dismissProgressBar();
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_WITHDRAW_AMOUNT:
                handleWithdrawAmountResponse(webRequest);
                break;
        }

    }

    private void handleWithdrawAmountResponse(WebRequest webRequest) {
        WithdrawAmountResponseModel responsePojo = webRequest.getResponsePojo(WithdrawAmountResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            if (isFinishing()) return;
            showCustomToast(responsePojo.getMessage());
            setResult(RESULT_OK);
            supportFinishAfterTransition();
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }

    }


}
