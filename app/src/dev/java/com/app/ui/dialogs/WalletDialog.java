package com.app.ui.dialogs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseDialogFragment;
import com.app.model.SettingsModel;
import com.app.model.UserModel;
import com.app.model.WalletModel;
import com.base.BaseFragment;
import com.rest.WebServices;
import com.tooltip.Tooltip;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.codetail.animation.ViewAnimationUtils;


/**
 * @author Manish Kumar
 * @since 20/10/18
 */

public class WalletDialog extends AppBaseDialogFragment {

    RelativeLayout rl_data_lay;
    TextView tv_total_balance;
    TextView tv_addmoney;
    TextView tv_deposit_balance;
    ImageView iv_info_deposit;
    TextView tv_winning_balance;
    TextView tv_withdrawl;
    ImageView iv_info_winning;
    TextView tv_bonus_balance;
    ImageView iv_info_bonus;
    LinearLayout ll_wallet_message;
    TextView tv_wallet_message;
    ImageView iv_wallet_message_close;
    ImageView iv_wallet_close;
    int topMargin;

    WalletDialogListener walletDialogListener;
    DialogRevealProperty dialogRevealProperty;

    public void setDialogRevealProperty(DialogRevealProperty dialogRevealProperty) {
        this.dialogRevealProperty = dialogRevealProperty;
    }

    public void setWalletDialogListener(WalletDialogListener walletDialogListener) {
        this.walletDialogListener = walletDialogListener;
    }

    public void setTopMargin(int topMargin) {
        this.topMargin = topMargin;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.dialog_wallet;
    }

    @Override
    public int getFragmentContainerResourceId(BaseFragment baseFragment) {
        return 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserPrefs().addListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getUserPrefs().removeListener(this);
    }

    @Override
    public void loggedInUserUpdate(UserModel userModel) {
        super.loggedInUserUpdate(userModel);
        setupWalletData();
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        WindowManager.LayoutParams wlmp = dialog.getWindow().getAttributes();
        wlmp.gravity = Gravity.TOP;
        wlmp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlmp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlmp.dimAmount = 0.6f;
        wlmp.y = topMargin;
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
    }


    @Override
    public void initializeComponent() {
        super.initializeComponent();
        rl_data_lay = getView().findViewById(R.id.rl_data_lay);
        rl_data_lay.setVisibility(View.GONE);

        tv_total_balance = getView().findViewById(R.id.tv_total_balance);
        tv_addmoney = getView().findViewById(R.id.tv_addmoney);
        tv_deposit_balance = getView().findViewById(R.id.tv_deposit_balance);
        //iv_info_deposit = getView().findViewById(R.id.iv_info_deposit);
        tv_winning_balance = getView().findViewById(R.id.tv_winning_balance);
        tv_withdrawl = getView().findViewById(R.id.tv_withdrawl);
        //iv_info_winning = getView().findViewById(R.id.iv_info_winning);
        tv_bonus_balance = getView().findViewById(R.id.tv_bonus_balance);
      //  iv_info_bonus = getView().findViewById(R.id.iv_info_bonus);
        ll_wallet_message = getView().findViewById(R.id.ll_wallet_message);
        tv_wallet_message = getView().findViewById(R.id.tv_wallet_message);
        iv_wallet_message_close = getView().findViewById(R.id.iv_wallet_message_close);
        iv_wallet_close = getView().findViewById(R.id.iv_wallet_close);

        setupWalletData();


        tv_addmoney.setOnClickListener(this);
        //iv_info_deposit.setOnClickListener(this);
        tv_withdrawl.setOnClickListener(this);
        ///iv_info_winning.setOnClickListener(this);
        //iv_info_bonus.setOnClickListener(this);
        iv_wallet_message_close.setOnClickListener(this);
        iv_wallet_close.setOnClickListener(this);
        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getView() != null && getActivity() != null) {
                    playReveal();
                    ((AppBaseActivity) getActivity()).callGetProfile();
                }
            }
        }, 100);


    }

    private void setupDepositToolTip() {
        new Tooltip.Builder(iv_info_deposit)
                .setGravity(Gravity.BOTTOM)
                .setArrowEnabled(true)
                .setText("Money deposited by you that you can use to join any contests but can't withdraw")
                .setTextColor(getContext().getResources().getColor(R.color.colorWhite))
                .setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary))
                .setCancelable(true)
                .setPadding(getResources().getDimensionPixelSize(R.dimen.dp3))
                .setCornerRadius(getResources().getDimension(R.dimen.dp3)).show();

    }

    private void setupWinningToolTip() {
        new Tooltip.Builder(iv_info_winning)
                .setGravity(Gravity.BOTTOM)
                .setArrowEnabled(true)
                .setText("Money that you can withdraw or re-use to join any contests")
                .setTextColor(getContext().getResources().getColor(R.color.colorWhite))
                .setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary))
                .setCancelable(true)
                .setPadding(getResources().getDimensionPixelSize(R.dimen.dp3))
                .setCornerRadius(getResources().getDimension(R.dimen.dp3)).show();
    }

    private void setupBonusToolTip() {
        new Tooltip.Builder(iv_info_bonus)
                .setGravity(Gravity.BOTTOM)
                .setArrowEnabled(true)
                .setText("Money that you can use to join any public contests")
                .setTextColor(getContext().getResources().getColor(R.color.colorWhite))
                .setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary))
                .setCancelable(true)
                .setPadding(getResources().getDimensionPixelSize(R.dimen.dp3))
                .setCornerRadius(getResources().getDimension(R.dimen.dp3)).show();
    }

    private void setupWalletData() {
        UserModel userModel = getUserModel();
        if (userModel != null) {
            SettingsModel settings = userModel.getSettings();
            if (settings != null) {
                tv_wallet_message.setText("Minimum usable bonus = " + settings.getCASH_BONUS_PERCENTAGES_Text() + "% of entry fees. Know more");
                setupWalletMessageButton();
            }
            WalletModel wallet = userModel.getWallet();
            if (wallet != null) {
                tv_total_balance.setText(currency_symbol + wallet.getTotalWalletBalanceText());
                tv_deposit_balance.setText(currency_symbol + wallet.getDeposit_walletText());
                tv_winning_balance.setText(currency_symbol + wallet.getWinning_walletText());
                tv_bonus_balance.setText(currency_symbol + wallet.getBonus_walletText());
                return;
            }
        }

        tv_total_balance.setText(currency_symbol + "0");
        tv_deposit_balance.setText(currency_symbol + "0");
        tv_winning_balance.setText(currency_symbol + "0");
        tv_bonus_balance.setText(currency_symbol + "0");
        tv_wallet_message.setText("Minimum usable bonus = 0% of entry fees. Know more");
        setupWalletMessageButton();

    }


    private void setupWalletMessageButton() {
        String knowmore = "Know more";
        tv_wallet_message.setMovementMethod(LinkMovementMethod.getInstance());
        Pattern termsAndConditionsMatcher = Pattern.compile(knowmore);
        Matcher m1 = termsAndConditionsMatcher.matcher(tv_wallet_message.getText().toString());
        if (m1.find()) {
            URLSpan urlSpan = new URLSpan(m1.group(0)) {
                @Override
                public void onClick(View widget) {
                    dismiss();
                    Bundle bundle = new Bundle();
                    bundle.putString(DATA1, "STAGGERED CASHBONUS");
                    bundle.putString(DATA, WebServices.GetStaggeredCashbonus());
                    goToWebViewActivity(bundle);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setColor(((AppBaseActivity) getActivity()).getColorFromStyle(R.attr.app_link_text_color));
                    ds.setUnderlineText(true);
                }
            };
            SpannableString string = SpannableString.valueOf(tv_wallet_message.getText());
            string.setSpan(urlSpan, m1.start(), m1.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_addmoney:
            case R.id.tv_withdrawl:
                if (walletDialogListener != null) {
                    walletDialogListener.onClick(v);
                }

                break;
           /* case R.id.iv_info_deposit:
                setupDepositToolTip();
                break;

            case R.id.iv_info_winning:
                setupWinningToolTip();
                break;
            case R.id.iv_info_bonus:
                setupBonusToolTip();
                break;*/
            case R.id.iv_wallet_message_close:
                updateViewVisibitity(ll_wallet_message, View.GONE);
                break;
            case R.id.iv_wallet_close:
                dismiss();
                break;
        }
    }


    private void playReveal() {
        rl_data_lay.setVisibility(View.VISIBLE);

        Animator animator =
                ViewAnimationUtils.createCircularReveal(rl_data_lay, dialogRevealProperty.getCenterX(),
                        dialogRevealProperty.getCenterY(), dialogRevealProperty.getStartRadius(),
                        dialogRevealProperty.getEndRadius());
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(dialogRevealProperty.getRevealDuration());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

            }
        });
        animator.start();
    }


    @Override
    public void dismiss() {
        if (dialogRevealProperty == null) {
            super.dismiss();
            return;
        }
        Animator animator =
                ViewAnimationUtils.createCircularReveal(rl_data_lay, dialogRevealProperty.getCenterX(),
                        dialogRevealProperty.getCenterY(), dialogRevealProperty.getEndRadius(),
                        dialogRevealProperty.getStartRadius());
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(dialogRevealProperty.getRevealDuration());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                WalletDialog.super.dismiss();
            }
        });
        animator.start();
    }

    @Override
    public boolean handleOnBackPress() {
        dismiss();
        return true;
    }

    @Override
    public boolean handleOnCancel() {
        dismiss();
        return true;
    }

    public interface WalletDialogListener {
        void onClick(View view);
    }
}
