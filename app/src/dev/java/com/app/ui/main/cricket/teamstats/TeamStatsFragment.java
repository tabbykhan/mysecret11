package com.app.ui.main.cricket.teamstats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.model.MatchModel;
import com.app.model.PlayerStatsModel;
import com.app.ui.MyApplication;
import com.app.ui.main.cricket.teamstats.adapter.TeamPlayerEventAdapter;
import com.utilities.DeviceScreenUtil;

public class TeamStatsFragment extends AppBaseFragment {

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
    TextView tv_player_country;
    TextView tv_player_type;

    RecyclerView recycler_view;
    TextView tv_no_item;

    TeamPlayerEventAdapter adapter;
    PlayerStatsModel playerStatsModel;


    public MatchModel getMatchModel() {
        return MyApplication.getInstance().getSelectedMatch();
    }

    public PlayerStatsModel getPlayerStatsModel() {
        return playerStatsModel;
    }

    public void setPlayerStatsModel(PlayerStatsModel playerStatsModel) {
        this.playerStatsModel = playerStatsModel;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getView() == null) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        return getView();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_team_stats;
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();
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
        tv_player_country = getView().findViewById(R.id.tv_player_country);
        tv_player_type = getView().findViewById(R.id.tv_player_type);
        recycler_view = getView().findViewById(R.id.recycler_view);
        tv_no_item = getView().findViewById(R.id.tv_no_item);
        initializeRecyclerView();
        setupData();
    }

    private void initializeRecyclerView() {
        PlayerStatsModel playerStatsModel = getPlayerStatsModel();
        if (playerStatsModel == null) {
            return;
        }
        adapter = new TeamPlayerEventAdapter(getActivity(), playerStatsModel.getPlayer_events()) {
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
            return;
        }
        ((AppBaseActivity) getContext()).loadImage(getContext(), iv_player, pb_image,
                playerStatsModel.getImage(),
                R.drawable.no_image);
        tv_player_name.setText(playerStatsModel.getName());
        tv_selected_by.setText(playerStatsModel.getSelectedByText());
        tv_points.setText(playerStatsModel.getPointsText());
        tv_player_type.setText(playerStatsModel.getPositionShortName());
        tv_player_country.setText(playerStatsModel.getTeamName());
        if (playerStatsModel.isMyPlayer()) {
            iv_your_player.setAlpha(1.0f);
            tv_team_name.setText("Your player");
        } else {
            iv_your_player.setAlpha(0.5f);
            tv_team_name.setText("Not in your team");
        }
        if (getPlayerStatsModel().getPlayer_events() != null &&
                getPlayerStatsModel().getPlayer_events().size() > 0) {
            updateViewVisibitity(tv_no_item, View.GONE);
        } else {
            updateViewVisibitity(tv_no_item, View.VISIBLE);
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
}
