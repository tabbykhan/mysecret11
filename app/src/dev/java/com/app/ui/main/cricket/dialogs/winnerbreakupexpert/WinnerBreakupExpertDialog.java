package com.app.ui.main.cricket.dialogs.winnerbreakupexpert;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseDialogFragment;
import com.app.model.UserModel;
import com.base.BaseFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class WinnerBreakupExpertDialog extends AppBaseDialogFragment {
    private BottomSheetBehavior bottomSheetBehavior;
    private RelativeLayout bottom_sheet;

    CardView cv_data;
    LinearLayout ll_data_lay;
    ImageView iv_close;
    TextView tv_price_pool;
    TextView tv_bottom_message;
    ImageView iv_image;
    TextView tv_join_team;
    TextView tv_entry_free;
    TextView tv_total_team;
    TextView tv_amount_per_team;
    TextView tv_total_amount;
    TextView tv_user_message;


    public static WinnerBreakupExpertDialog getInstance(Bundle bundle) {
        WinnerBreakupExpertDialog winnerBreakupDialog = new WinnerBreakupExpertDialog();
        winnerBreakupDialog.setArguments(bundle);
        return winnerBreakupDialog;
    }

    public String getImagUser() {
        Bundle extras = getArguments();
        return (extras == null ? "" : extras.getString(DATA, ""));
    }

    public String getEnterFree() {
        Bundle extras = getArguments();
        return (extras == null ? "" : extras.getString(DATA1, ""));
    }

    public String getMaltplaryFree() {
        Bundle extras = getArguments();
        return (extras == null ? "" : extras.getString(DATA2, ""));
    }

    public String getTotalJoinTeam() {
        Bundle extras = getArguments();
        return (extras == null ? "" : extras.getString(DATA3, ""));
    }
    public String getTotalFree() {
        Bundle extras = getArguments();
        return (extras == null ? "" : extras.getString(DATA4, ""));
    }
    public String getUserName() {
        Bundle extras = getArguments();
        return (extras == null ? "" : extras.getString(DATA5, ""));
    }


    @Override
    public int getLayoutResourceId() {
        return R.layout.dialog_winner_breakup_expert;
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
        wlmp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlmp.dimAmount = 0.8f;

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();
        //bottom_sheet = getView().findViewById(R.id.bottom_sheet);
        //initializeBottomSheet();
        //ll_data_lay = getView().findViewById(R.id.ll_data_lay);
        cv_data = getView().findViewById(R.id.cv_data);
        //updateViewVisibitity(cv_data, View.INVISIBLE);
        iv_close = getView().findViewById(R.id.iv_close);
        //tv_price_pool = getView().findViewById(R.id.tv_price_pool);
        //tv_bottom_message = getView().findViewById(R.id.tv_bottom_message);
        //tv_price_pool.setText(((AppBaseActivity) getActivity()).currency_symbol + getContestPricePool());
        iv_close.setOnClickListener(this);

        iv_image = getView().findViewById(R.id.iv_image);
        tv_join_team = getView().findViewById(R.id.tv_join_team);
        tv_entry_free = getView().findViewById(R.id.tv_entry_free);
        tv_total_team = getView().findViewById(R.id.tv_total_team);
        tv_amount_per_team = getView().findViewById(R.id.tv_amount_per_team);
        tv_total_amount = getView().findViewById(R.id.tv_total_amount);
        tv_user_message = getView().findViewById(R.id.tv_user_message);


        //updateBottomMessage();
        setupdate();
    }

    private void updateBottomMessage() {
        UserModel userModel = getUserModel();
        if (userModel != null && userModel.getSettings() != null) {
            tv_bottom_message.setText(userModel.getSettings().getWINNING_BREAKUP_MESSAGE());
        } else {
            tv_bottom_message.setText("");
        }
    }

    private void initializeBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    dismiss();
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    public void setupdate() {
        tv_join_team.setText(getTotalJoinTeam());
        ((AppBaseActivity) getActivity()).loadImage(getActivity(), iv_image, null,
                getImagUser(),
                R.drawable.no_image);
        tv_entry_free.setText(currency_symbol + getEnterFree());
        tv_total_team.setText(getMaltplaryFree() + " X");
        tv_amount_per_team.setText(currency_symbol + getEnterFree() + " =");
        tv_total_amount.setText(currency_symbol+getTotalFree());
        tv_user_message.setText("Score more than " +getUserName()+" to become a winner!");

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
        }
    }
}
