package com.app.ui.dialogs.playerstatsdialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseDialogFragment;
import com.app.model.MatchModel;
import com.app.model.PlayerStatsModel;
import com.app.ui.MyApplication;
import com.app.ui.dialogs.playerstatsdialog.adapter.PlayerEventsAdapter;
import com.base.BaseFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.utilities.DeviceScreenUtil;

public class PlayerEventsDialog extends AppBaseDialogFragment {
    private BottomSheetBehavior bottomSheetBehavior;
    private RelativeLayout bottom_sheet;

    ImageView iv_close;
    LinearLayout ll_data_lay;
    CardView cv_data;
    LinearLayout ll_title_lay;
    ImageView iv_player;
    ProgressBar pb_image;
    TextView tv_player_name;
    ImageView iv_top_player;
    TextView tv_selected_by;
    TextView tv_points;
    ImageView iv_your_player;
    TextView tv_team_name;

    RecyclerView recycler_view;

    PlayerEventsAdapter adapter;
    PlayerStatsModel playerStatsModel;

    public static PlayerEventsDialog getInstance(Bundle bundle) {
        PlayerEventsDialog playerEventsDialog = new PlayerEventsDialog();
        if (bundle != null)
            playerEventsDialog.setArguments(bundle);
        return playerEventsDialog;
    }

    public MatchModel getMatchModel() {
        return MyApplication.getInstance().getSelectedMatch();
    }
    public PlayerStatsModel getPlayerStatsModel() {
        return playerStatsModel;
    }

    public void setPlayerStatsModel(PlayerStatsModel playerStatsModel) {
        this.playerStatsModel = playerStatsModel;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.dialog_player_events;
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
        wlmp.height = DeviceScreenUtil.getInstance().getHeight(0.75f);
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
        iv_close = getView().findViewById(R.id.iv_close);
        ll_data_lay = getView().findViewById(R.id.ll_data_lay);
        cv_data = getView().findViewById(R.id.cv_data);
        ll_title_lay = getView().findViewById(R.id.ll_title_lay);
        iv_player = getView().findViewById(R.id.iv_player);
        pb_image = getView().findViewById(R.id.pb_image);
        tv_player_name = getView().findViewById(R.id.tv_player_name);
        iv_top_player = getView().findViewById(R.id.iv_top_player);
        tv_selected_by = getView().findViewById(R.id.tv_selected_by);
        tv_points = getView().findViewById(R.id.tv_points);
        iv_your_player = getView().findViewById(R.id.iv_your_player);
        tv_team_name = getView().findViewById(R.id.tv_team_name);
        recycler_view = getView().findViewById(R.id.recycler_view);

        iv_close.setOnClickListener(this);

        initializeRecyclerView();
        setupData();


    }

    private void initializeBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    dismiss();
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }


    private void initializeRecyclerView() {
        adapter = new PlayerEventsAdapter(getActivity(), getPlayerStatsModel().getPlayer_events()) {
            @Override
            public int getLastItemBottomMargin() {
                return DeviceScreenUtil.getInstance().convertDpToPixel(10);
            }
        };
        recycler_view.setLayoutManager(getVerticalLinearLayoutManager());
        recycler_view.setAdapter(adapter);
    }

    private void setupData() {
        PlayerStatsModel playerStatsModel = getPlayerStatsModel();
        if (playerStatsModel == null) {
            dismiss();
            return;
        }
        ((AppBaseActivity) getContext()).loadImage(getContext(), iv_player, pb_image,
                playerStatsModel.getImage(),
                R.drawable.no_image);
        tv_player_name.setText(playerStatsModel.getName());
        tv_selected_by.setText(playerStatsModel.getSelectedByText());
        tv_points.setText(playerStatsModel.getPointsText());
        if (playerStatsModel.isMyPlayer()) {
            iv_your_player.setActivated(true);
            tv_team_name.setText("Your player");
        } else {
            iv_your_player.setActivated(false);
            tv_team_name.setText("Not in your team");
        }

        if (getMatchModel().isPastMatch()) {
            if (playerStatsModel.isDreamTeamPlayer()) {
                updateViewVisibitity(iv_top_player, View.VISIBLE);
            } else {
                updateViewVisibitity(iv_top_player, View.INVISIBLE);
            }

        } else {
            updateViewVisibitity(iv_top_player, View.INVISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
        }
    }
}
