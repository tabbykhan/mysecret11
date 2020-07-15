package com.app.ui.main.soccer.team.playerpreview;

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

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseDialogFragment;
import com.app.model.CustomerTeamModel;
import com.app.model.MatchModel;
import com.app.model.PlayerModel;
import com.app.model.TeamModel;
import com.app.model.webresponsemodel.CustomerTeamDetailResponseModel;
import com.app.ui.MyApplication;
import com.app.ui.main.dashboard.DashboardActivityNew;
import com.app.ui.main.soccer.team.createteam.CreateSoccerTeamActivity;
import com.base.BaseFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.medy.retrofitwrapper.WebRequest;

import java.util.List;

/**
 * @author Manish Kumar
 * @since 20/10/18
 */

public class SoccerTeamPreviewDialog extends AppBaseDialogFragment {

    private BottomSheetBehavior bottomSheetBehavior;
    private RelativeLayout bottom_sheet;

    TextView tv_team_name;
    ImageView iv_edit_team;
    ImageView iv_close;
    SoccerPlayerView wicket_keeper;
    SoccerPlayerView batsman;
    SoccerPlayerView allrounder;
    SoccerPlayerView bowler;

    LinearLayout rl_bottom_lay;
    TextView tv_team_rank;
    TextView tv_total_points;

    LinearLayout ll_no_player;
    TextView tv_start_selecting;

    TeamPreviewDialogListener teamPreviewDialogListener;

    public void setTeamPreviewDialogListener(TeamPreviewDialogListener teamPreviewDialogListener) {
        this.teamPreviewDialogListener = teamPreviewDialogListener;
    }


    CustomerTeamModel customerTeamModel;

    public void setCustomerTeamModel(CustomerTeamModel customerTeamModel) {
        this.customerTeamModel = customerTeamModel;
    }

    public static SoccerTeamPreviewDialog getInstance(Bundle bundle) {
        SoccerTeamPreviewDialog messageDialog = new SoccerTeamPreviewDialog();
        if (bundle != null)
            messageDialog.setArguments(bundle);
        return messageDialog;
    }

    public long getCustomerTeamId() {
        Bundle extras = getArguments();
        return (extras == null ? -1 : extras.getLong(DATA1, -1));

    }

    public long getCustomerTeamRank() {
        Bundle extras = getArguments();
        return (extras == null ? -1 : extras.getLong(DATA2, -1));

    }

    public String getCustomerTeamName() {
        Bundle extras = getArguments();
        return (extras == null ? "" : extras.getString(DATA3, ""));

    }

    public MatchModel getMatchModel() {
        return MyApplication.getInstance().getSelectedMatch();
    }


    @Override
    public int getLayoutResourceId() {
        return R.layout.dialog_soccer_team_preview;
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
    }


    @Override
    public void initializeComponent() {
        super.initializeComponent();
        bottom_sheet = getView().findViewById(R.id.bottom_sheet);
        initializeBottomSheet();

        tv_team_name = getView().findViewById(R.id.tv_team_name);
        iv_edit_team = getView().findViewById(R.id.iv_edit_team);
        iv_close = getView().findViewById(R.id.iv_close);
        wicket_keeper = getView().findViewById(R.id.wicket_keeper);
        batsman = getView().findViewById(R.id.batsman);
        allrounder = getView().findViewById(R.id.allrounder);
        bowler = getView().findViewById(R.id.bowler);

        rl_bottom_lay = getView().findViewById(R.id.rl_bottom_lay);
        tv_team_rank = getView().findViewById(R.id.tv_team_rank);
        tv_total_points = getView().findViewById(R.id.tv_total_points);

        ll_no_player = getView().findViewById(R.id.ll_no_player);
        updateViewVisibitity(ll_no_player, View.GONE);
        tv_start_selecting = getView().findViewById(R.id.tv_start_selecting);

        iv_close.setOnClickListener(this);
        tv_start_selecting.setOnClickListener(this);
        iv_edit_team.setOnClickListener(this);

        setupView();

    }

    private void initializeBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        bottomSheetBehavior.setPeekHeight(0);
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


    private void setupView() {
        if (customerTeamModel != null) {
            final View viewById = getView().findViewById(R.id.ll_data_lay);
            viewById.post(new Runnable() {
                @Override
                public void run() {
                    int defaultItemInRow = 5;
                    int width = viewById.getWidth();

                    int totalWkt = customerTeamModel.getGoalkeepers().size();
                    int totalBats = customerTeamModel.getDefenders().size();
                    int totalAllr = customerTeamModel.getMidfielders().size();
                    int totalBowl = customerTeamModel.getForwards().size();

                    if (totalWkt > 0 || totalBats > 0 || totalAllr > 0 || totalBowl > 0) {
                        updateViewVisibitity(ll_no_player, View.GONE);
                    } else {
                        updateViewVisibitity(ll_no_player, View.VISIBLE);
                    }

                    int maxPlayer = Math.max(Math.max(Math.max(totalWkt, totalBats), totalAllr), totalBowl);
                    defaultItemInRow = Math.max(defaultItemInRow, maxPlayer);

                    int itemSize = Math.round(((float) width) / defaultItemInRow);
                    SoccerTeamPreviewDialog.this.wicket_keeper.setPerItemSize(itemSize);
                    SoccerTeamPreviewDialog.this.batsman.setPerItemSize(itemSize);
                    SoccerTeamPreviewDialog.this.allrounder.setPerItemSize(itemSize);
                    SoccerTeamPreviewDialog.this.bowler.setPerItemSize(itemSize);

                    SoccerTeamPreviewDialog.this.wicket_keeper.setupItems(totalWkt);
                    SoccerTeamPreviewDialog.this.batsman.setupItems(totalBats);
                    SoccerTeamPreviewDialog.this.allrounder.setupItems(totalAllr);
                    SoccerTeamPreviewDialog.this.bowler.setupItems(totalBowl);

                    setupData();

                }
            });

        } else {
            getCustomerTeamDetail();
        }

    }

    private void setupData() {
        if (customerTeamModel != null) {
            if (getActivity() instanceof CreateSoccerTeamActivity) {
                updateViewVisibitity(iv_edit_team, View.GONE);
            } else if (getActivity() instanceof DashboardActivityNew) {
                updateViewVisibitity(iv_edit_team, View.GONE);
            } else {
                if (getMatchModel().isFixtureMatch()) {
                    updateViewVisibitity(iv_edit_team, View.VISIBLE);
                } else {
                    updateViewVisibitity(iv_edit_team, View.GONE);
                }
            }



            String teamName = getCustomerTeamName();
            if (!isValidString(teamName)) {
                teamName = "TEAM " + customerTeamModel.getName();
            }
            tv_team_name.setText(teamName);

            long customerTeamRank = getCustomerTeamRank();
            if (customerTeamRank == -1) {
                tv_team_rank.setText("");
                if (getCustomerTeamId() == 0) {
                    tv_total_points.setText("TOTAL POINTS = " + customerTeamModel.getTotalPointsText());
                } else {
                    tv_total_points.setText("");
                }
            } else {
                tv_team_rank.setText("Rank #" + String.valueOf(customerTeamRank));
                tv_total_points.setText("TOTAL POINTS = " + customerTeamModel.getTotalPointsText());
            }

            setupGoalKeeper();
            setUpDefender();
            setUpMidfeld();
            setUpForward();
        } else {
            tv_team_name.setText("TEAM");
            tv_total_points.setText("");
            tv_team_rank.setText("");
        }
    }

    private void setupGoalKeeper() {

        List<PlayerModel> wicketkeeper = customerTeamModel.getGoalkeepers();
        List<View> itemViews = SoccerTeamPreviewDialog.this.wicket_keeper.getItemViews();
        for (int i = 0; i < itemViews.size(); i++) {
            View view = itemViews.get(i);

            ImageView iv_players_image = view.findViewById(R.id.iv_players_image);
            TextView tv_players_name = view.findViewById(R.id.tv_players_name);
            TextView tv_players_points = view.findViewById(R.id.tv_players_points);
            TextView tv_player_type = view.findViewById(R.id.tv_player_type);
            View view_now_playing = view.findViewById(R.id.view_now_playing);

            PlayerModel playerModel = wicketkeeper.get(i);
            view.setOnClickListener(this);
            view.setTag(playerModel.getPlayer_id());
            tv_players_name.setText(playerModel.getName());
            TeamModel team1 = customerTeamModel.getTeam1();
            if (playerModel.getTeam_id() == team1.getId()) {
                tv_players_name.setActivated(true);
            } else {
                tv_players_name.setActivated(false);
            }
            updateViewVisibitity(view_now_playing, View.GONE);
            if ( getActivity() instanceof DashboardActivityNew) {
                tv_players_points.setText(playerModel.getCreditText() + " Cr");
            } else {
                if (getMatchModel().isFixtureMatch()) {
                    tv_players_points.setText(playerModel.getCreditText() + " Cr");
                } else {
                    tv_players_points.setText(playerModel.getPointsText() + " Pts");
                }
            }

            if (playerModel.getPlayer_multiplier() == 2) {
                tv_player_type.setText("C");
                updateViewVisibitity(tv_player_type, View.VISIBLE);
                tv_player_type.setBackgroundResource(R.drawable.bg_orange_oval);
            } else if (playerModel.getPlayer_multiplier() == 1.5f) {
                tv_player_type.setText("VC");
                updateViewVisibitity(tv_player_type, View.VISIBLE);
                tv_player_type.setBackgroundResource(R.drawable.bg_red_oval);
            }/*else if (playerModel.getIs_mpp()!= null && playerModel.getIs_mpp().equals("Y")) {
                tv_player_type.setText("MPP");
                updateViewVisibitity(tv_player_type, View.VISIBLE);
                tv_player_type.setBackgroundResource(R.drawable.bg_appblack_oval);
            }*/ else {
                updateViewVisibitity(tv_player_type, View.GONE);
            }

            ((AppBaseActivity) getActivity()).loadImage(getActivity(), iv_players_image, null,
                    playerModel.getImage(), R.drawable.no_image);

        }
    }

    private void setUpDefender() {
        List<PlayerModel> batsmans = customerTeamModel.getDefenders();
        List<View> itemViews = SoccerTeamPreviewDialog.this.batsman.getItemViews();
        for (int i = 0; i < itemViews.size(); i++) {
            View view = itemViews.get(i);
            ImageView iv_players_image = view.findViewById(R.id.iv_players_image);
            TextView tv_players_name = view.findViewById(R.id.tv_players_name);
            TextView tv_players_points = view.findViewById(R.id.tv_players_points);
            TextView tv_player_type = view.findViewById(R.id.tv_player_type);
            View view_now_playing = view.findViewById(R.id.view_now_playing);

            PlayerModel playerModel = batsmans.get(i);
            view.setOnClickListener(this);
            view.setTag(playerModel.getPlayer_id());
            tv_players_name.setText(playerModel.getName());
            TeamModel team1 = customerTeamModel.getTeam1();
            if (playerModel.getTeam_id() == team1.getId()) {
                tv_players_name.setActivated(true);
            } else {
                tv_players_name.setActivated(false);
            }
            updateViewVisibitity(view_now_playing, View.GONE);
            if (getActivity() instanceof DashboardActivityNew) {
                tv_players_points.setText(playerModel.getCreditText() + " Cr");
            } else {
                if (getMatchModel().isFixtureMatch()) {
                    tv_players_points.setText(playerModel.getCreditText() + " Cr");
                } else {
                    tv_players_points.setText(playerModel.getPointsText() + " Pts");
                }
            }


            if (playerModel.getPlayer_multiplier() == 2) {
                tv_player_type.setText("C");
                tv_player_type.setBackgroundResource(R.drawable.bg_orange_oval);
                updateViewVisibitity(tv_player_type, View.VISIBLE);
            } else if (playerModel.getPlayer_multiplier() == 1.5f) {
                tv_player_type.setText("VC");
                tv_player_type.setBackgroundResource(R.drawable.bg_red_oval);
                updateViewVisibitity(tv_player_type, View.VISIBLE);
            }/*else if (playerModel.getIs_mpp()!= null && playerModel.getIs_mpp().equals("Y")) {
                tv_player_type.setText("MPP");
                updateViewVisibitity(tv_player_type, View.VISIBLE);
                tv_player_type.setBackgroundResource(R.drawable.bg_appblack_oval);
            } */else {
                updateViewVisibitity(tv_player_type, View.GONE);
            }

            ((AppBaseActivity) getActivity()).loadImage(getActivity(), iv_players_image, null,
                    playerModel.getImage(), R.drawable.no_image);

        }
    }

    private void setUpMidfeld() {
        List<PlayerModel> allrounders = customerTeamModel.getMidfielders();
        List<View> itemViews = SoccerTeamPreviewDialog.this.allrounder.getItemViews();
        for (int i = 0; i < itemViews.size(); i++) {
            View view = itemViews.get(i);
            ImageView iv_players_image = view.findViewById(R.id.iv_players_image);
            TextView tv_players_name = view.findViewById(R.id.tv_players_name);
            TextView tv_players_points = view.findViewById(R.id.tv_players_points);
            TextView tv_player_type = view.findViewById(R.id.tv_player_type);
            View view_now_playing = view.findViewById(R.id.view_now_playing);

            PlayerModel playerModel = allrounders.get(i);
            view.setOnClickListener(this);
            view.setTag(playerModel.getPlayer_id());
            tv_players_name.setText(playerModel.getName());
            TeamModel team1 = customerTeamModel.getTeam1();
            if (playerModel.getTeam_id() == team1.getId()) {
                tv_players_name.setActivated(true);
            } else {
                tv_players_name.setActivated(false);
            }
            updateViewVisibitity(view_now_playing, View.GONE);
            if (getActivity() instanceof DashboardActivityNew) {
                tv_players_points.setText(playerModel.getCreditText() + " Cr");
            } else {
                if (getMatchModel().isFixtureMatch()) {
                    tv_players_points.setText(playerModel.getCreditText() + " Cr");
                } else {
                    tv_players_points.setText(playerModel.getPointsText() + " Pts");
                }
            }

            if (playerModel.getPlayer_multiplier() == 2) {
                tv_player_type.setText("C");
                tv_player_type.setBackgroundResource(R.drawable.bg_orange_oval);
                updateViewVisibitity(tv_player_type, View.VISIBLE);
            } else if (playerModel.getPlayer_multiplier() == 1.5f) {
                tv_player_type.setText("VC");
                tv_player_type.setBackgroundResource(R.drawable.bg_red_oval);
                updateViewVisibitity(tv_player_type, View.VISIBLE);
            } /*else if (playerModel.getIs_mpp()!= null && playerModel.getIs_mpp().equals("Y")) {
                tv_player_type.setText("MPP");
                updateViewVisibitity(tv_player_type, View.VISIBLE);
                tv_player_type.setBackgroundResource(R.drawable.bg_appblack_oval);
            }*/else {
                updateViewVisibitity(tv_player_type, View.GONE);
            }

            ((AppBaseActivity) getActivity()).loadImage(getActivity(), iv_players_image, null,
                    playerModel.getImage(), R.drawable.no_image);

        }
    }

    private void setUpForward() {
        List<PlayerModel> bowlers = customerTeamModel.getForwards();
        List<View> itemViews = SoccerTeamPreviewDialog.this.bowler.getItemViews();
        for (int i = 0; i < itemViews.size(); i++) {
            View view = itemViews.get(i);
            ImageView iv_players_image = view.findViewById(R.id.iv_players_image);
            TextView tv_players_name = view.findViewById(R.id.tv_players_name);
            TextView tv_players_points = view.findViewById(R.id.tv_players_points);
            TextView tv_player_type = view.findViewById(R.id.tv_player_type);
            View view_now_playing = view.findViewById(R.id.view_now_playing);

            PlayerModel playerModel = bowlers.get(i);
            view.setOnClickListener(this);
            view.setTag(playerModel.getPlayer_id());
            tv_players_name.setText(playerModel.getName());
            TeamModel team1 = customerTeamModel.getTeam1();
            if (playerModel.getTeam_id() == team1.getId()) {
                tv_players_name.setActivated(true);
            } else {
                tv_players_name.setActivated(false);
            }
            updateViewVisibitity(view_now_playing, View.GONE);
            if (getActivity() instanceof DashboardActivityNew) {
                tv_players_points.setText(playerModel.getCreditText() + " Cr");
            } else {
                if (getMatchModel().isFixtureMatch()) {
                    tv_players_points.setText(playerModel.getCreditText() + " Cr");
                } else {
                    tv_players_points.setText(playerModel.getPointsText() + " Pts");
                }
            }


            if (playerModel.getPlayer_multiplier() == 2) {
                tv_player_type.setText("C");
                tv_player_type.setBackgroundResource(R.drawable.bg_orange_oval);
                updateViewVisibitity(tv_player_type, View.VISIBLE);
            } else if (playerModel.getPlayer_multiplier() == 1.5f) {
                tv_player_type.setText("VC");
                tv_player_type.setBackgroundResource(R.drawable.bg_red_oval);
                updateViewVisibitity(tv_player_type, View.VISIBLE);
            } /*else if (playerModel.getIs_mpp()!= null && playerModel.getIs_mpp().equals("Y")) {
                tv_player_type.setText("MPP");
                updateViewVisibitity(tv_player_type, View.VISIBLE);
                tv_player_type.setBackgroundResource(R.drawable.bg_appblack_oval);
            }*/ else {
                updateViewVisibitity(tv_player_type, View.GONE);
            }

            ((AppBaseActivity) getActivity()).loadImage(getActivity(), iv_players_image, null,
                    playerModel.getImage(), R.drawable.no_image);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.tv_start_selecting:
                dismiss();
                break;
            case R.id.iv_edit_team:
                dismiss();
                if (this.teamPreviewDialogListener != null) {
                    if(customerTeamModel!=null){
                        teamPreviewDialogListener.needTeamEdit(this.customerTeamModel);
                    }

                }
                break;

            case R.id.rl_player_view:
                if (getActivity() == null) return;
                if (getMatchModel().isFixtureMatch()) {
                    ((AppBaseActivity) getActivity()).showErrorMsg("Player stats available after match start.");
                    return;
                }
                if (v.getTag() == null || getActivity() == null) return;
                Bundle bundle = new Bundle();
                bundle.putLong(DATA1, customerTeamModel.getId());
                bundle.putString(DATA2, String.valueOf(v.getTag()));
                ((AppBaseActivity) getActivity()).goToCreateSoccerTeamActivity(bundle);
                break;
        }
    }

    private void getCustomerTeamDetail() {
        if (getMatchModel() != null) {
            setupData();
            displayProgressBar(false);
            if (getCustomerTeamId() == 0) {
                getWebRequestHelper().getSoccerDreamTeamDetail(getMatchModel().getMatch_id(), this);
            } else {
                getWebRequestHelper().getGetCustomerSoccerTeamDetail(getCustomerTeamId(), this);
            }
        } else {
            if (getCustomerTeamId() == -1)
                return;
            getWebRequestHelper().getGetCustomerSoccerTeamDetail(getCustomerTeamId(), this);
        }
    }


    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        dismissProgressBar();
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_CUSTOMER_SOCCER_TEAM_DETAIL:
                handleCustomerTeamDetailResponse(webRequest);
                break;
            case ID_CUSTOMER_SOCCER_DREAM_TEAM_DETAIL:
                handleCustomerTeamDetailResponse(webRequest);
                break;
        }

    }

    private void handleCustomerTeamDetailResponse(WebRequest webRequest) {
        CustomerTeamDetailResponseModel responsePojo = webRequest.getResponsePojo(CustomerTeamDetailResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError() && responsePojo.getData() != null) {
            customerTeamModel = responsePojo.getData();
            if (getActivity() == null) return;
            setupView();
        } else {
            if (getActivity() == null) return;
            showCustomToast(responsePojo.getMessage());
        }

    }

    public interface TeamPreviewDialogListener {
        void needTeamEdit(CustomerTeamModel customerTeamModel);

    }


}
