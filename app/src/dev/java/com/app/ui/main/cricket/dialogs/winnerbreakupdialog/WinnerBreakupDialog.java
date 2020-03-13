package com.app.ui.main.cricket.dialogs.winnerbreakupdialog;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseDialogFragment;
import com.app.model.ContestWinnerBreakUpModel;
import com.app.model.UserModel;
import com.app.model.webresponsemodel.ContestWinnerBreakupResponseModel;
import com.app.ui.main.cricket.dialogs.winnerbreakupdialog.adapter.WinnerBreakupAdapter;
import com.base.BaseFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.medy.retrofitwrapper.WebRequest;
import com.utilities.DeviceScreenUtil;

public class WinnerBreakupDialog extends AppBaseDialogFragment {
    private BottomSheetBehavior bottomSheetBehavior;
    private RelativeLayout bottom_sheet;

    CardView cv_data;
    LinearLayout ll_data_lay;
    ImageView iv_close;
    TextView tv_price_pool;
    RecyclerView recycler_view;
    TextView tv_bottom_message;

    WinnerBreakupAdapter adapter;
    ContestWinnerBreakUpModel contestWinnerBreakUpModel;

    public static WinnerBreakupDialog getInstance(Bundle bundle) {
        WinnerBreakupDialog winnerBreakupDialog = new WinnerBreakupDialog();
        winnerBreakupDialog.setArguments(bundle);
        return winnerBreakupDialog;
    }

    private long getContestId() {
        Bundle extras = getArguments();
        return (extras == null ? 0 : extras.getLong(DATA, 0));
    }

    private double getContestPricePool() {
        Bundle extras = getArguments();
        return (extras == null ? 0 : extras.getDouble(DATA1, 0));
    }


    @Override
    public int getLayoutResourceId() {
        return R.layout.dialog_winner_breakup;
    }

    @Override
    public int getFragmentContainerResourceId(BaseFragment baseFragment) {
        return 0;
    }


    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        WindowManager.LayoutParams wlmp = dialog.getWindow().getAttributes();
        wlmp.gravity = Gravity.BOTTOM;
        wlmp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlmp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlmp.dimAmount = 0.8f;

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();
        bottom_sheet = getView().findViewById(R.id.bottom_sheet);
        initializeBottomSheet();
        ll_data_lay = getView().findViewById(R.id.ll_data_lay);
        cv_data = getView().findViewById(R.id.cv_data);
        updateViewVisibitity(cv_data, View.INVISIBLE);
        iv_close = getView().findViewById(R.id.iv_close);
        tv_price_pool = getView().findViewById(R.id.tv_price_pool);
        recycler_view = getView().findViewById(R.id.recycler_view);
        tv_bottom_message = getView().findViewById(R.id.tv_bottom_message);
        tv_price_pool.setText(((AppBaseActivity) getActivity()).currency_symbol + getContestPricePool());
        iv_close.setOnClickListener(this);

        updateBottomMessage();
        getWinnerBreakup();

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

    public void setupPeakHeightBottomSheet() {
        ll_data_lay.post(new Runnable() {
            @Override
            public void run() {
                int defaultPeakHeight = ll_data_lay.getHeight();
                int defaultBottomHeight = tv_bottom_message.getHeight();
                int itemsHeight = 0;
                if (adapter.getDataCount() > 0 && adapter.getView(0) != null) {
                    int item_height = adapter.getView(0).getHeight();
                    itemsHeight = adapter.getDataCount() * item_height;
                }
                int finalHeight = defaultPeakHeight + itemsHeight + defaultBottomHeight;
                int maxHeight = DeviceScreenUtil.getInstance().getHeight(0.60f);
                bottomSheetBehavior.setPeekHeight(Math.min(finalHeight, maxHeight));
            }
        });

    }


    private void initializeRecyclerView() {
        adapter = new WinnerBreakupAdapter(getActivity(), contestWinnerBreakUpModel);
        recycler_view.setLayoutManager(getVerticalLinearLayoutManager());
        recycler_view.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
        }
    }

    private void getWinnerBreakup() {
        if (getActivity() == null) return;
        ((AppBaseActivity) getActivity()).displayProgressBar(false);
        getWebRequestHelper().getContestWinnerBreakUp(getContestId(), this);
    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        if (getActivity() == null) return;
        ((AppBaseActivity) getActivity()).dismissProgressBar();
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) {
            dismiss();
            return;
        }
        switch (webRequest.getWebRequestId()) {
            case ID_CONTEST_WINNER_BREAKUP:
                handleWinnerBreakupResponse(webRequest);
                break;
        }
    }

    private void handleWinnerBreakupResponse(WebRequest webRequest) {
        ContestWinnerBreakupResponseModel responsePojo = webRequest.getResponsePojo(ContestWinnerBreakupResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            if (getDialog() != null && getDialog().isShowing()) {
                contestWinnerBreakUpModel = responsePojo.getData();
                if (contestWinnerBreakUpModel == null) {
                    displayErrorDialog("Invalid data.");
                    dismiss();
                    return;
                }
                UserModel userModel = getUserModel();
                if(userModel!=null && userModel.getSettings()!=null){
                    Log.d("message display",responsePojo.getBottomMessage());
                    userModel.getSettings().setWINNING_BREAKUP_MESSAGE(responsePojo.getBottomMessage());
                    updateUserInPrefs();
                }else{

                }

                updateBottomMessage();
                initializeRecyclerView();
                updateViewVisibitity(cv_data, View.VISIBLE);
                setupPeakHeightBottomSheet();
            }
        } else {
            if (getDialog() != null && getDialog().isShowing()) {
                displayErrorDialog(responsePojo.getMessage());
                dismiss();
            }
        }

    }
}
