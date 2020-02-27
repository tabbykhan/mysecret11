package com.app.ui.main.cricket.dialogs.recommended;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.app.appbase.AppBaseDialogFragment;
import com.app.model.webresponsemodel.PrivateContestWinningBreakupResponseModel;
import com.app.ui.main.cricket.dialogs.recommended.adapter.WinnersRecommendedAdapter;
import com.base.BaseFragment;
import com.utilities.DeviceScreenUtil;
import com.utilities.ItemClickSupport;

import java.util.List;

public class WinnersRecommendedDialog extends AppBaseDialogFragment {


    WinnersRecommendedDialogListener winnersRecommendedDialogListener;

    ImageView iv_close;

    TextView tv_title;
    TextView tv_price_pool;

    RecyclerView recycler_view;

    WinnersRecommendedAdapter adapter;
    List<PrivateContestWinningBreakupResponseModel.WinnerBreakUpData> contestWinnerBreakUpModels;

    int selectedWinnerBreakupPosition;

    String contestCoolName;

    public void setContestCoolName(String contestCoolName) {
        this.contestCoolName = contestCoolName;
    }

    public String getContestCoolName() {
        return contestCoolName==null?"Hello Kite":contestCoolName;
    }

    public void setWinnerBreakUpModels(List<PrivateContestWinningBreakupResponseModel.WinnerBreakUpData> contestWinnerBreakUpModels) {
        this.contestWinnerBreakUpModels = contestWinnerBreakUpModels;
    }

    public void setPreviousSelectedPosition(int selectedWinnerBreakupPosition) {
        this.selectedWinnerBreakupPosition = selectedWinnerBreakupPosition;
    }

    public void setWinnersRecommendedDialogListener(WinnersRecommendedDialogListener winnersRecommendedDialogListener) {
        this.winnersRecommendedDialogListener = winnersRecommendedDialogListener;
    }

    public interface WinnersRecommendedDialogListener {
        void onPositionSelected(int position);
    }


    public static WinnersRecommendedDialog getInstance(Bundle bundle) {
        WinnersRecommendedDialog playerEventsDialog = new WinnersRecommendedDialog();
        if (bundle != null)
            playerEventsDialog.setArguments(bundle);
        return playerEventsDialog;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.dialog_winner_recommended;
    }

    @Override
    public int getFragmentContainerResourceId(BaseFragment baseFragment) {
        return 0;
    }


    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        WindowManager.LayoutParams wlmp = dialog.getWindow().getAttributes();
        wlmp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlmp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlmp.gravity = Gravity.BOTTOM;
        wlmp.dimAmount = 0.8f;

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();

        iv_close = getView().findViewById(R.id.iv_close);
        tv_title = getView().findViewById(R.id.tv_title);
        tv_price_pool = getView().findViewById(R.id.tv_price_pool);
        recycler_view = getView().findViewById(R.id.recycler_view);

        iv_close.setOnClickListener(this);

        tv_title.setText(getContestCoolName());


        initializeRecyclerView();


    }


    private void initializeRecyclerView() {
        adapter = new WinnersRecommendedAdapter(getActivity(), contestWinnerBreakUpModels) {
            @Override
            public int getLastItemBottomMargin() {
                return DeviceScreenUtil.getInstance().convertDpToPixel(10);
            }

            @Override
            public int getPreviousSelectedPosition() {
                return selectedWinnerBreakupPosition;
            }
        };
        recycler_view.setLayoutManager(getVerticalLinearLayoutManager());
        recycler_view.setAdapter(adapter);

        ItemClickSupport.addTo(recycler_view).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                if (winnersRecommendedDialogListener != null) {
                    winnersRecommendedDialogListener.onPositionSelected(position);
                } else {
                    dismiss();
                }
            }
        });
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
