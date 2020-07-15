package com.app.ui.main.soccer.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseDialogFragment;
import com.app.model.MatchModel;
import com.app.model.PlayerModel;
import com.app.model.PlayerStatsModel;
import com.app.model.webresponsemodel.PlayerStatsResponseModel;
import com.app.ui.MyApplication;
import com.app.ui.main.soccer.dialog.adapter.SoccerPlayerStatsAdapter;
import com.base.BaseFragment;
import com.medy.retrofitwrapper.WebRequest;
import com.utilities.DeviceScreenUtil;
import com.utilities.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Manish Kumar
 * @since 20/10/18
 */

public class SoccerPlayerDetailDialog extends AppBaseDialogFragment {


    ImageView iv_back;
    TextView tv_title_left;

    ProgressBar pb_data;
    RecyclerView recycler_view;
    LinearLayout rl_bottom_lay;
    TextView tv_add_to_my_team;
    CardView cv_data;
    ImageView iv_player;
    ProgressBar pb_image;
    TextView tv_credit;
    TextView tv_total_player_point;
    TextView tv_bat_type;
    TextView tv_bowl_type;
    TextView tv_nationality;
    TextView tv_dob;
    View view_now_playing;

    SoccerPlayerStatsAdapter adapter;
    List<PlayerStatsModel> playerStatsModels = new ArrayList<>();

    PlayerModel playerModel;
    boolean isFromCreateTeam;
    PlayerDetailDialogListener playerDetailDialogListener;

    public static SoccerPlayerDetailDialog getInstance(Bundle bundle) {
        SoccerPlayerDetailDialog messageDialog = new SoccerPlayerDetailDialog();
        messageDialog.setArguments(bundle);
        return messageDialog;
    }

    public void setPlayerDetailDialogListener(PlayerDetailDialogListener playerDetailDialogListener) {
        this.playerDetailDialogListener = playerDetailDialogListener;
    }

    public MatchModel getMatchModel() {
        return MyApplication.getInstance().getSelectedMatch();
    }

    public void setPlayerModel(PlayerModel playerModel) {
        this.playerModel = playerModel;
    }

    public void setFromCreateTeam(boolean fromCreateTeam) {
        isFromCreateTeam = fromCreateTeam;
    }


    @Override
    public int getLayoutResourceId() {
        return R.layout.dialog_soccer_player_detail;
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
        wlmp.height = WindowManager.LayoutParams.MATCH_PARENT;
        wlmp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlmp.dimAmount = 0.0f;
    }


    @Override
    public void initializeComponent() {
        super.initializeComponent();
        if (this.playerModel != null) {

            iv_back = getView().findViewById(R.id.iv_back);
            tv_title_left = getView().findViewById(R.id.tv_title_left);

            cv_data = getView().findViewById(R.id.cv_data);
            iv_player = getView().findViewById(R.id.iv_player);
            pb_image = getView().findViewById(R.id.pb_image);
            tv_credit = getView().findViewById(R.id.tv_credit);
            tv_total_player_point = getView().findViewById(R.id.tv_total_player_point);
            tv_bat_type = getView().findViewById(R.id.tv_bat_type);
            tv_bowl_type = getView().findViewById(R.id.tv_bowl_type);
            tv_nationality = getView().findViewById(R.id.tv_nationality);
            tv_dob = getView().findViewById(R.id.tv_dob);
            view_now_playing = getView().findViewById(R.id.view_now_playing);
            recycler_view = getView().findViewById(R.id.recycler_view);
            recycler_view.setNestedScrollingEnabled(true);
            pb_data = getView().findViewById(R.id.pb_data);
            updateViewVisibitity(pb_data, View.GONE);
            rl_bottom_lay = getView().findViewById(R.id.rl_bottom_lay);
            updateViewVisibitity(rl_bottom_lay, View.INVISIBLE);
            tv_add_to_my_team = getView().findViewById(R.id.tv_add_to_my_team);

            iv_back.setOnClickListener(this);
            tv_add_to_my_team.setOnClickListener(this);

            setupData();
            initializeRecyclerView();
            getPlayerStats();
        } else {
            dismiss();
        }

    }

    private void setupData() {
        tv_title_left.setText(playerModel.getName());

        ((AppBaseActivity) getActivity()).loadImage(this, iv_player, pb_image, playerModel.getImage(),
                R.drawable.no_image);

        tv_bat_type.setText(playerModel.getBat_type());
        tv_bowl_type.setText(playerModel.getBowl_type());
        tv_nationality.setText(playerModel.getCountry());
        tv_dob.setText(playerModel.getDob());
        tv_credit.setText(playerModel.getCreditText());
        tv_total_player_point.setText(playerModel.getTotalPointsText());

        if (!isFromCreateTeam) {
            updateViewVisibitity(rl_bottom_lay, View.GONE);
            updateViewVisibitity(view_now_playing, View.GONE);
        } else {
            if (playerModel.isSelected()) {
                tv_add_to_my_team.setText("REMOVE FROM MY TEAM");
            } else {
                tv_add_to_my_team.setText("ADD TO MY TEAM");
            }
            updateViewVisibitity(rl_bottom_lay, View.VISIBLE);
            if (playerModel.isInPlayingSquad()) {
                updateViewVisibitity(view_now_playing, View.VISIBLE);
            } else {
                updateViewVisibitity(view_now_playing, View.GONE);
            }
        }

    }


    private void initializeRecyclerView() {
        adapter = new SoccerPlayerStatsAdapter(getActivity(), playerStatsModels) {
            @Override
            public int getLastItemBottomMargin() {
                return rl_bottom_lay.getHeight() + DeviceScreenUtil.getInstance().convertDpToPixel(20);
            }
        };
        recycler_view.setLayoutManager(getVerticalLinearLayoutManager());
        recycler_view.setAdapter(adapter);

        ItemClickSupport.addTo(recycler_view).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                switch (v.getId()) {

                }

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                dismiss();
                break;
            case R.id.tv_add_to_my_team:
                if (playerDetailDialogListener != null) {
                    playerDetailDialogListener.onPlayerSelected(this);
                }
                break;
        }
    }

    private void getPlayerStats() {
        updateViewVisibitity(pb_data, View.VISIBLE);
        getWebRequestHelper().getSoccerSeriesByPlayerStatistics(getMatchModel().getMatch_id(),
                playerModel.getPlayer_id(), this);

    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        updateViewVisibitity(pb_data, View.GONE);
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_GET_SOCCER_PLAYER_STATS:
                handlePlayerStatsResponse(webRequest);
                break;
        }

    }

    private void handlePlayerStatsResponse(WebRequest webRequest) {
        PlayerStatsResponseModel responsePojo = webRequest.getResponsePojo(PlayerStatsResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            List<PlayerStatsModel> data = responsePojo.getData();
            playerStatsModels.clear();
            if (data != null && data.size() > 0) {
                playerStatsModels.addAll(data);
            }
            if (getActivity() == null || getDialog() == null || !getDialog().isShowing()) return;
            adapter.notifyDataSetChanged();
        } else {
            if (getActivity() == null || getDialog() == null || !getDialog().isShowing()) return;
            ((AppBaseActivity) getActivity()).showErrorMsg(responsePojo.getMessage());
        }

    }

    public interface PlayerDetailDialogListener {
        void onPlayerSelected(AppBaseDialogFragment appBaseDialogFragment);
    }


}
