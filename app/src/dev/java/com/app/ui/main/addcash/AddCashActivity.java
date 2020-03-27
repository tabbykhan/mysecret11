package com.app.ui.main.addcash;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.model.MatchModel;
import com.app.model.UserModel;
import com.app.model.WalletModel;
import com.app.model.webrequestmodel.DepositAmountRequestModel;
import com.app.model.webrequestmodel.PromoCodeRequestModel;
import com.app.model.webresponsemodel.CustomerJoinContestResponseModel;
import com.app.model.webresponsemodel.DepositWalletResponseModel;
import com.app.model.webresponsemodel.PromoCodeResponseModel;
import com.app.ui.MatchTimerListener;
import com.app.ui.MyApplication;
import com.app.ui.dialogs.paymentdialog.PaymentDialog;
import com.app.ui.main.ToolbarFragment;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.medy.retrofitwrapper.WebRequest;

/**
 * @author Manish Kumar
 * @since 1/10/18
 */

public class AddCashActivity extends AppBaseActivity implements MatchTimerListener {

    ToolbarFragment toolbarFragment;

    LinearLayout ll_contest_detail_lay;
    TextView tv_match_name;
    TextView tv_timer_time;

    TextView tv_remain_balance;
    TextView tv_entry;
    ImageView iv_info;
    TextView tv_cash_bonus;
    TextView tv_heading;


    EditText et_amount;
    TextView tv_amount1;
    TextView tv_amount2;
    TextView tv_amount3;
    TextView tv_proceed;

    LinearLayout ll_promo_code;
    EditText et_promo_code;
    TextView tv_apply_code;
    boolean is_remove = false;


    private CardView cv_paytm;
    private ImageView iv_radio_paytm;
    private CardView cv_razorpay;
    private ImageView iv_radio_razorpay;

    String paymentType = "PAYTM";

    CustomerJoinContestResponseModel.PreJoinedModel preJoinedModel;
    PromoCodeResponseModel promoCodeResponseModel;
    public CustomerJoinContestResponseModel.PreJoinedModel getPreJoinedModel() {
        return preJoinedModel;
    }

    public CustomerJoinContestResponseModel.PreJoinedModel getContestDetail() {
        Bundle extras = getIntent().getExtras();
        String data = (extras == null ? "" : extras.getString(DATA, ""));
        if (isValidString(data)) {
            try {
                return new Gson().fromJson(data, CustomerJoinContestResponseModel.PreJoinedModel.class);
            } catch (JsonSyntaxException e) {

            }
        }
        return null;
    }


    public long getMatchContestId() {
        Bundle extras = getIntent().getExtras();
        return (extras == null) ? -1 : extras.getLong(DATA2, -1);
    }


    public long getTeamId() {
        Bundle extras = getIntent().getExtras();
        return (extras == null) ? -1 : extras.getLong(DATA1, -1);
    }

    public String getEntryFee() {
        Bundle extras = getIntent().getExtras();
        return (extras == null) ? "" : extras.getString(DATA3, "");
    }

    public MatchModel getMatchModel() {
        return MyApplication.getInstance().getSelectedMatch();
    }

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
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().addMatchTimerListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MyApplication.getInstance().removeMatchTimerListener(this);
    }


    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_add_cash;
    }


    @Override
    public void initializeComponent() {
        super.initializeComponent();
        preJoinedModel = getContestDetail();

        Fragment toolbar = getFm().findFragmentById(R.id.fragment_toolbar);
        if (toolbar instanceof AppBaseFragment) {
            toolbarFragment = (ToolbarFragment) toolbar;
            toolbarFragment.initializeComponent();
        }

        ll_contest_detail_lay = findViewById(R.id.ll_contest_detail_lay);
        tv_match_name = findViewById(R.id.tv_match_name);
        tv_timer_time = findViewById(R.id.tv_timer_time);
        iv_info = findViewById(R.id.iv_info);
        tv_remain_balance = findViewById(R.id.tv_remain_balance);
        tv_entry = findViewById(R.id.tv_entry);
        tv_cash_bonus = findViewById(R.id.tv_cash_bonus);


        tv_heading = findViewById(R.id.tv_heading);

        et_amount = findViewById(R.id.et_amount);
        et_amount.removeTextChangedListener(textWatcher);
        et_amount.addTextChangedListener(textWatcher);
        tv_amount1 = findViewById(R.id.tv_amount1);
        tv_amount2 = findViewById(R.id.tv_amount2);
        tv_amount3 = findViewById(R.id.tv_amount3);
        tv_proceed = findViewById(R.id.tv_proceed);
        tv_amount1.setOnClickListener(this);
        tv_amount2.setOnClickListener(this);
        tv_amount3.setOnClickListener(this);
        tv_proceed.setOnClickListener(this);

        tv_amount1.setTag("100");
        tv_amount2.setTag("500");
        tv_amount3.setTag("1000");


        paymentType = "PAYTM";
        cv_paytm = findViewById(R.id.cv_paytm);
        cv_paytm.setOnClickListener(this);
        iv_radio_paytm = findViewById(R.id.iv_radio_paytm);

        cv_razorpay = findViewById(R.id.cv_razorpay);
        cv_razorpay.setOnClickListener(this);
        iv_radio_razorpay = findViewById(R.id.iv_radio_razorpay);

        ll_promo_code = findViewById(R.id.ll_promo_code);
        et_promo_code = findViewById(R.id.et_promo_code);
        tv_apply_code = findViewById(R.id.tv_apply_code);
        tv_apply_code.setOnClickListener(this);

        updatePaymentView();


        if (preJoinedModel != null) {
            updateViewVisibitity(ll_contest_detail_lay, View.VISIBLE);
            tv_remain_balance.setText(currency_symbol + preJoinedModel.getRemainBalancetext());
            tv_entry.setText(currency_symbol + preJoinedModel.getEnteryFeestext());
            tv_cash_bonus.setText("- " + currency_symbol + preJoinedModel.getUsedBonusText());

            tv_heading.setText("Add cash to your account to join this contest");
            if (preJoinedModel.getAmount_suggest() != null && preJoinedModel.getAmount_suggest().size() >= 3) {
                tv_amount1.setTag(String.valueOf(preJoinedModel.getAmount_suggest().get(0)));
                tv_amount2.setTag(String.valueOf(preJoinedModel.getAmount_suggest().get(1)));
                tv_amount3.setTag(String.valueOf(preJoinedModel.getAmount_suggest().get(2)));
            }

        } else {
            updateViewVisibitity(ll_contest_detail_lay, View.GONE);
            tv_heading.setText("ADD CASH TO YOUR ACCOUNT");
        }

        tv_amount1.setText(currency_symbol + String.valueOf(tv_amount1.getTag()));
        tv_amount2.setText(currency_symbol + String.valueOf(tv_amount2.getTag()));
        tv_amount3.setText(currency_symbol + String.valueOf(tv_amount3.getTag()));

        if (preJoinedModel != null) {
            et_amount.setText(String.valueOf(preJoinedModel.getNeed_pay()));
        } else {
            et_amount.setText(String.valueOf(tv_amount1.getTag()));
        }
        setMatchData();


    }

    private void updatePaymentView() {
        switch (paymentType) {
            case "PAYTM":
                iv_radio_paytm.setSelected(true);
                iv_radio_razorpay.setSelected(false);
                break;
            case "RAZORPAY":
                iv_radio_paytm.setSelected(false);
                iv_radio_razorpay.setSelected(true);
                break;
        }
    }

    private void setMatchData() {
        if (getMatchModel() != null && !isFinishing()) {
            tv_match_name.setText(getMatchModel().getShortName());
            tv_timer_time.setText(getMatchModel().getRemainTimeText());
            tv_timer_time.setTextColor(getResources().getColor(getMatchModel()
                    .getTimerColor()));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_amount1:
                et_amount.setText(String.valueOf(tv_amount1.getTag()));
                break;
            case R.id.tv_amount2:
                et_amount.setText(String.valueOf(tv_amount2.getTag()));
                break;
            case R.id.tv_amount3:
                et_amount.setText(String.valueOf(tv_amount3.getTag()));
                break;
            case R.id.tv_proceed:
                callDepositAmount();
                break;
            case R.id.cv_paytm:
                paymentType = "PAYTM";
                updatePaymentView();
                break;
            case R.id.cv_razorpay:
                paymentType = "RAZORPAY";
                updatePaymentView();
                break;
            case R.id.tv_apply_code:
                if(is_remove){
                    promoCodeResponseModel = null;
                    is_remove = false;
                    et_promo_code.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            et_promo_code.setFocusableInTouchMode(true);
                            et_promo_code.setFocusable(true);
                            et_promo_code.setEnabled(true);
                        }
                    },100);

                    et_promo_code.setText("");
                    et_promo_code.append("");
                    tv_apply_code.setText("Apply");
                    return;
                }
                applyPromoCode();
                break;

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }


    private void callDepositAmount() {
        String amount = et_amount.getText().toString().trim();
        String promo_code = "";
        if(promoCodeResponseModel != null && promoCodeResponseModel.getData()!= null){
            if(promoCodeResponseModel.getData().getCodeX() != null)
                promo_code = promoCodeResponseModel.getData().getCodeX();
        }
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
        DepositAmountRequestModel requestModel = new DepositAmountRequestModel();
        requestModel.amount = amt;
       // requestModel.paymentmethod = paymentType;
        requestModel.promocode = promo_code;

        openPaymentDialog(requestModel);
      //  displayProgressBar(false);// commit
      //  getWebRequestHelper().customerDepositAmount(requestModel, this);//commit
    }

    private void applyPromoCode() {
        String amount = et_amount.getText().toString().trim();
        String promo_code = et_promo_code.getText().toString().trim();
        if (amount.startsWith(currency_symbol) && amount.length() > 1) {
            amount = amount.substring(1).trim();
        }
        if (TextUtils.isEmpty(amount)) {
            showErrorMsg("Please enter amount");
            return;
        }
        if (TextUtils.isEmpty(promo_code)) {
            showErrorMsg("Please enter promo code");
            return;
        }
        float amt = Float.parseFloat(amount);
        PromoCodeRequestModel requestModel = new PromoCodeRequestModel();
        requestModel.amount = amt;
        requestModel.promocode = promo_code;

        displayProgressBar(false);
        getWebRequestHelper().applyPromoCode(requestModel, this);

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
    public void onMatchTimeUpdate() {
        setMatchData();
    }


    private void openPaymentDialog(final DepositAmountRequestModel requestModel) {
        final PaymentDialog paymentDialog = new PaymentDialog();
        paymentDialog.setPaymentSuccessListener(new PaymentDialog.PaymentSuccessListener() {
            @Override
            public void onPaymentResponse(final DepositWalletResponseModel responsePojo) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        paymentDialog.dismiss();
                        if (responsePojo != null) {
                            if (!responsePojo.isError()) {
                                UserModel userModel = getUserModel();
                                WalletModel data = responsePojo.getData();
                                if (data != null && !isFinishing() && userModel != null) {
                                    userModel.setWallet(data);
                                    updateUserInPrefs();
                                    showCustomToast(responsePojo.getMessage());
                                    Intent intent = new Intent();
                                    Bundle extras = new Bundle();
                                    long matchContestId = getMatchContestId();
                                    long teamId = getTeamId();
                                    if (matchContestId > 0 && teamId > 0) {
                                        extras.putLong(DATA, matchContestId);
                                        extras.putLong(DATA1, teamId);
                                    }
                                    extras.putString(DATA2, getEntryFee());
                                    intent.putExtras(extras);
                                    setResult(RESULT_OK, intent);
                                    supportFinishAfterTransition();
                                    return;
                                }
                            }
                            if (isFinishing()) return;
                            showErrorMsg(responsePojo.getMessage());
                        }
                    }
                });

            }
        });
        paymentDialog.setDepositAmountRequestModel(requestModel);
        paymentDialog.show(getFm(), PaymentDialog.class.getSimpleName());
    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        dismissProgressBar();
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_DEPOSIT_AMOUNT:
                handleDepositAmountResponse(webRequest);
                break;
            case ID_APPLY_PROMO_CODE:
                handlePromoCodeResponse(webRequest);
                break;
        }

    }

    private void handleDepositAmountResponse(WebRequest webRequest) {
        DepositWalletResponseModel responsePojo = webRequest.getResponsePojo(DepositWalletResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            WalletModel data = responsePojo.getData();
            if (data == null) return;
            if (isFinishing()) return;
            UserModel userModel = getUserModel();
            if (userModel == null) return;
            userModel.setWallet(data);
            updateUserInPrefs();
            showCustomToast(responsePojo.getMessage());
            long matchContestId = getMatchContestId();
            long teamId = getTeamId();
            Intent intent = new Intent();
            Bundle extras = new Bundle();
            if (matchContestId > 0 && teamId > 0) {
                extras.putLong(DATA, matchContestId);
                extras.putLong(DATA1, teamId);
                intent = new Intent();
                intent.putExtras(extras);
            }
            extras.putString(DATA2, getEntryFee());
            setResult(RESULT_OK, intent);
            supportFinishAfterTransition();
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }

    }
    private void handlePromoCodeResponse(WebRequest webRequest) {
        promoCodeResponseModel = webRequest.getResponsePojo(PromoCodeResponseModel.class);
        if (promoCodeResponseModel == null) return;
        if (!promoCodeResponseModel.isError()) {
            showCustomToast(promoCodeResponseModel.getMessage());
            is_remove = true;
            tv_apply_code.setText("Remove");
            et_promo_code.setFocusableInTouchMode(false);
            et_promo_code.setFocusable(false);
            et_promo_code.setEnabled(false);
        } else {
            if (isFinishing()) return;
            showErrorMsg(promoCodeResponseModel.getMessage());
            is_remove = false;
            tv_apply_code.setText("Apply");
            et_promo_code.setFocusableInTouchMode(true);
            et_promo_code.setFocusable(true);
            et_promo_code.setEnabled(true);
        }

    }


}
