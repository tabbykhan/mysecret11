package com.app.ui.main.soccer.team.createteam.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ConstantsFlavor;
import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseRecycleAdapter;
import com.app.model.PlayerModel;
import com.app.model.TeamModel;
import com.app.ui.MyApplication;

import java.util.List;

public class SoccerPlayersAdapter extends AppBaseRecycleAdapter implements Filterable {

    private Context context;
    List<PlayerModel> list;
    private TeamModel teamModel1;
    private TeamModel teamModel2;

    public SoccerPlayersAdapter(Context context, List<PlayerModel> list) {
        this.context = context;
        this.list = list;

        teamModel1=   MyApplication.getInstance().getSelectedMatch().getTeam1();
        teamModel2=MyApplication.getInstance().getSelectedMatch().getTeam2();
        Log.d("team1 name", teamModel1.getSort_name() );
        Log.d("team2 name", teamModel2.getSort_name() );
    }

    @Override
    public BaseViewHolder getViewHolder() {
        return new ViewHolder(inflateLayout(R.layout.item_soccer_players_adapter));
    }

    @Override
    public int getDataCount() {
        return list == null ? 0 : list.size();
    }

    public PlayerModel getItem(int position) {
        return this.list.get(position);
    }

    public boolean isMaxPlayerSelected() {
        return false;
    }

    public boolean isMaxFrom1Team(long teamId) {
        return false;
    }

    public boolean isMaxPlayerType() {
        return false;
    }

    public String getPlayerTypeName(long team_id) {
        return "";
    }

    public int getPlayerTeamType(long team_id) {
        return 0;
    }

    public boolean isMinPlayerFailed() {
        return false;
    }

    public boolean isCreditExceed(float playerCredit) {
        return false;
    }

    @Override
    public Filter getFilter() {
        return null;
    }


    private class ViewHolder extends BaseViewHolder {
        private LinearLayout ll_layout;
        private RelativeLayout rl_player_image;
        private ImageView iv_player_image;
        private ProgressBar pb_image;
        private TextView tv_player_name;
        private TextView tv_team_type;
        private TextView tv_player_points;
        private TextView tv_credit;
        private TextView tv_selected_by;
        private ImageView iv_add_player;
        private View view_now_playing;
        private TextView tv_now_playing;

        public ViewHolder(View itemView) {
            super(itemView);
            ll_layout = itemView.findViewById(R.id.ll_layout);
            rl_player_image = itemView.findViewById(R.id.rl_player_image);
            iv_player_image = itemView.findViewById(R.id.iv_player_image);
            pb_image = itemView.findViewById(R.id.pb_image);
            tv_player_name = itemView.findViewById(R.id.tv_player_name);
            tv_team_type = itemView.findViewById(R.id.tv_team_type);
            tv_player_points = itemView.findViewById(R.id.tv_player_points);
            tv_credit = itemView.findViewById(R.id.tv_credit);
            tv_selected_by = itemView.findViewById(R.id.tv_selected_by);
            iv_add_player = itemView.findViewById(R.id.iv_add_player);
            view_now_playing = itemView.findViewById(R.id.view_now_playing);
            view_now_playing = itemView.findViewById(R.id.view_now_playing);
            tv_now_playing = itemView.findViewById(R.id.tv_now_playing);

        }

        @Override
        public void setData(int position) {
            if (list == null) return;
            rl_player_image.setTag(position);
            rl_player_image.setOnClickListener(this);
            iv_add_player.setTag(position);
            iv_add_player.setOnClickListener(this);
            PlayerModel playerModel = list.get(position);
            ((AppBaseActivity) getContext()).loadImage(getContext(), iv_player_image, pb_image,
                    playerModel.getImage(), R.drawable.no_image);

            tv_player_name.setText(playerModel.getName());
            Log.d("palyer id", "---------"+playerModel.getTeam_id() );
            Log.d("team1 name id", "==========="+teamModel1.getId() );


            Log.d("team2 name id", "==========="+teamModel2.getId() );
            //tv_team_type.setText(Html.fromHtml(getPlayerTypeName(playerModel.getTeam_id())));
            if (playerModel.getTeam_id()==teamModel1.getId()  ) {
                tv_team_type.setText(teamModel1.getSort_name());
            } else if (playerModel.getTeam_id() == teamModel2.getId()) {
                tv_team_type.setText(teamModel2.getSort_name());
            }

            if(ConstantsFlavor.type == ConstantsFlavor.Type.sportteam){
                if (getPlayerTeamType(playerModel.getTeam_id()) == 1) {
                    tv_team_type.setBackgroundColor(context.getResources().getColor(R.color.color_sky));
                    tv_team_type.setTextColor(context.getResources().getColor(R.color.colorWhite));
                } else {

                    tv_team_type.setBackgroundColor(context.getResources().getColor(R.color.colorOrange));
                    tv_team_type.setTextColor(context.getResources().getColor(R.color.colorWhite));
                }
            }else {

                if (getPlayerTeamType(playerModel.getTeam_id()) == 1) {
                    tv_team_type.setBackground(context.getResources().getDrawable(R.drawable.bg_white_top2radius));
                    tv_team_type.setTextColor(context.getResources().getColor(R.color.colorBlack));
                } else {
                    tv_team_type.setBackground(context.getResources().getDrawable(R.drawable.bg_black_top2radius));
                    tv_team_type.setTextColor(context.getResources().getColor(R.color.colorWhite));
                }
            }

            tv_player_points.setText(playerModel.getTotalPointsText());
            tv_credit.setText(playerModel.getCreditText());
            tv_selected_by.setText("Sel By "+playerModel.getSelected_byText()+"%");

            if (playerModel.isSelected()) {
                iv_add_player.setActivated(true);
                ll_layout.setSelected(true);
              //  ll_layout.setActivated(true);
            } else {
                iv_add_player.setActivated(false);
                ll_layout.setSelected(false);
              //  ll_layout.setActivated(false);
            }

            if (playerModel.isInPlayingSquadUpdated()) {
                updateViewVisibitity(view_now_playing, View.VISIBLE);
                updateViewVisibitity(tv_now_playing, View.VISIBLE);
                if (playerModel.isInPlayingSquad()) {
                    tv_now_playing.setText("Playing");
                    view_now_playing.setBackground(context.getResources().getDrawable(R.drawable.bg_green_oval));
                } else {
                    tv_now_playing.setText("Not Playing");
                    view_now_playing.setBackground(context.getResources().getDrawable(R.drawable.bg_red_oval));
                }
            } else {
                updateViewVisibitity(view_now_playing, View.GONE);
                updateViewVisibitity(tv_now_playing, View.GONE);
            }


            if (!playerModel.isSelected() && (isMaxPlayerSelected()
                    || isMaxFrom1Team(playerModel.getTeam_id())
                    || isMaxPlayerType()
                    || isMinPlayerFailed()
                    || isCreditExceed(playerModel.getCredits()))) {
                itemView.setAlpha(0.45f);
            } else {
                itemView.setAlpha(1.0f);
            }
        }

        @Override
        public void onClick(View v) {
            performItemClick((Integer) v.getTag(), v);

        }
    }
}
