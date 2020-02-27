package com.app.ui.main.cricket.myteam.choosecapitan.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseRecycleAdapter;
import com.app.model.PlayerModel;

import java.util.List;

public class ChooseCaptionAdapter extends AppBaseRecycleAdapter implements Filterable {

    private List<PlayerModel> list;
    String caption;
    String vcCaption;
    String trumpPlayer;

    PlayerModel captionModel;
    PlayerModel vccaptionModel;
    PlayerModel treamplaerModel;

    public PlayerModel getCaptionModel() {
        return captionModel;
    }

    public PlayerModel getVccaptionModel() {
        return vccaptionModel;
    }

    public PlayerModel getTreamplaerModel() {
        return treamplaerModel;
    }

    public void setTreamplaerModel(PlayerModel treamplaerModel) {
        this.treamplaerModel = treamplaerModel;
    }

    public ChooseCaptionAdapter(List<PlayerModel> list) {
        this.list = list;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setVcCaption(String vcCaption) {
        this.vcCaption = vcCaption;
    }

    public void setCaptionModel(PlayerModel captionModel) {
        this.captionModel = captionModel;
    }

    public void setVccaptionModel(PlayerModel vccaptionModel) {
        this.vccaptionModel = vccaptionModel;
    }

    public String getTrumpPlayer() {
        return trumpPlayer;
    }

    public void setTrumpPlayer(String trumpPlayer) {
        this.trumpPlayer = trumpPlayer;
    }

    public String getCaption() {
        return caption;
    }

    public String getVcCaption() {
        return vcCaption;
    }

    @Override
    public BaseViewHolder getViewHolder() {
        return new ViewHolder(inflateLayout(R.layout.item_choose_caption_adapter));
    }

    @Override
    public int getDataCount() {
        return list == null ? 0 : list.size();
    }

    public String getPlayerTypeName(long team_id) {
        return "";
    }


    public String getPlayerTypeShortName(int playerType) {
        return "";
    }

    public int getPlayerTeamType(long team_id) {
        return 0;
    }

    public void updateCaption(int position) {
        PlayerModel playerModel = list.get(position);
        if (vcCaption != null && vcCaption.equals(playerModel.getPlayer_id())) {
            vcCaption = null;
            vccaptionModel=null;
        }
        if (trumpPlayer != null && trumpPlayer.equals(playerModel.getPlayer_id())) {
            trumpPlayer = null;
            treamplaerModel=null;
        }
        this.caption = playerModel.getPlayer_id();
        this.captionModel=playerModel;
        notifyDataSetChanged();
    }

    public void updateVcCaption(int position) {
        PlayerModel playerModel = list.get(position);
        if (caption != null && caption.equals(playerModel.getPlayer_id())) {
            caption = null;
            captionModel=null;
        }
        if (trumpPlayer != null && trumpPlayer.equals(playerModel.getPlayer_id())) {
            trumpPlayer = null;
            treamplaerModel=null;
        }
        this.vcCaption = playerModel.getPlayer_id();
        this.vccaptionModel=playerModel;
        notifyDataSetChanged();
    }

    public void updateTrumperPlayer(int position) {
        PlayerModel playerModel = list.get(position);
        if (caption != null && caption.equals(playerModel.getPlayer_id())) {
            caption = null;
            captionModel=null;
        }
        if (vcCaption != null && vcCaption.equals(playerModel.getPlayer_id())) {
            vcCaption = null;
            vccaptionModel=null;
        }
        this.trumpPlayer = playerModel.getPlayer_id();
        this.treamplaerModel=playerModel;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    private class ViewHolder extends BaseViewHolder {
        private RelativeLayout rl_layout;
        private ImageView iv_player_image;
        private ProgressBar pb_image;
        private TextView tv_player_name;
        private TextView tv_team_type;
        private TextView tv_player_points;
        private TextView tv_player_type;
        private TextView tv_captain;
        private TextView tv_vice_captain;
        private TextView tv_captain_selected_by;
        private TextView tv_vice_captain_selected_by;
        private ImageView iv_player_c_vc_image;
        private TextView tv_trump;
        private TextView tv_trump_selected_by;

        public ViewHolder(View itemView) {
            super(itemView);
            rl_layout = itemView.findViewById(R.id.rl_layout);
            iv_player_image = itemView.findViewById(R.id.iv_player_image);
            pb_image = itemView.findViewById(R.id.pb_image);
            tv_player_name = itemView.findViewById(R.id.tv_player_name);
            tv_team_type = itemView.findViewById(R.id.tv_team_type);
            tv_player_type = itemView.findViewById(R.id.tv_player_type);
            tv_player_points = itemView.findViewById(R.id.tv_player_points);
            tv_captain = itemView.findViewById(R.id.tv_captain);
            tv_vice_captain = itemView.findViewById(R.id.tv_vice_captain);
            tv_captain_selected_by = itemView.findViewById(R.id.tv_captain_selected_by);
            iv_player_c_vc_image = itemView.findViewById(R.id.iv_player_c_vc_image);
            tv_vice_captain_selected_by = itemView.findViewById(R.id.tv_vice_captain_selected_by);
            tv_trump = itemView.findViewById(R.id.tv_trump);
            tv_trump_selected_by = itemView.findViewById(R.id.tv_trump_selected_by);
        }

        @Override
        public void setData(int position) {
            if (list == null) return;
            tv_captain.setTag(position);
            tv_captain.setOnClickListener(this);
            tv_vice_captain.setTag(position);
            tv_vice_captain.setOnClickListener(this);
            tv_trump.setTag(position);
            tv_trump.setOnClickListener(this);
            PlayerModel playerModel = list.get(position);

            ((AppBaseActivity) getContext()).loadImage(getContext(), iv_player_image, pb_image,
                    playerModel.getImage(), R.drawable.no_image, 100);


            tv_player_name.setText(playerModel.getName());

            //tv_team_type.setText(Html.fromHtml(getPlayerTypeName(playerModel.getTeam_id())));

            if (getPlayerTeamType(playerModel.getTeam_id()) == 1) {
                tv_team_type.setBackground(tv_team_type.getContext().getResources().getDrawable(R.drawable.bg_white_left2radius));
                tv_team_type.setTextColor(Color.parseColor("#000000"));
            } else {
                tv_team_type.setBackground(tv_team_type.getContext().getResources().getDrawable(R.drawable.bg_black_left2radius));
                tv_team_type.setTextColor(Color.parseColor("#ffffff"));
            }

            tv_player_type.setText(getPlayerTypeShortName(playerModel.getPlayerType()));
            tv_captain_selected_by.setText(playerModel.getCaptionSelected_byText()+"%");
            tv_vice_captain_selected_by.setText(playerModel.getViceCaptionSelected_byText()+"%");
            tv_trump_selected_by.setText(playerModel.getMppSelected_byText()+"%");
            tv_player_points.setText("Points - "+playerModel.getTotalPointsText());

            boolean needShowImage = false;
            if (caption == null) {
                tv_captain.setActivated(false);
                tv_captain.setText("C");
            } else {
                if (playerModel.getPlayer_id().equals(caption)) {
                    tv_captain.setActivated(true);
                    //tv_captain.setText("2x");
                    tv_captain.setText("C");
                    iv_player_c_vc_image.setImageResource(R.drawable.points2x_3x);
                    needShowImage = true;
                } else {
                    tv_captain.setActivated(false);
                    tv_captain.setText("C");

                }
            }

            if (vcCaption == null) {
                tv_vice_captain.setActivated(false);
                tv_vice_captain.setText("VC");
            } else {
                if (playerModel.getPlayer_id().equals(vcCaption)) {
                    tv_vice_captain.setActivated(true);
                    //tv_vice_captain.setText("1.5x");
                    tv_vice_captain.setText("VC");
                    iv_player_c_vc_image.setImageResource(R.drawable.points15x_3x);
                    needShowImage = true;
                } else {
                    tv_vice_captain.setActivated(false);
                    tv_vice_captain.setText("VC");

                }
            }

            if (trumpPlayer == null) {
                tv_trump.setActivated(false);
                tv_trump.setText("MPP");
            } else {
                if (playerModel.getPlayer_id().equals(trumpPlayer)) {
                    tv_trump.setActivated(true);
                    tv_trump.setText("MPP");
                } else {
                    tv_trump.setActivated(false);
                    tv_trump.setText("MPP");

                }
            }

            if (needShowImage) {
                updateViewVisibitity(iv_player_c_vc_image, View.VISIBLE);
            } else {
                updateViewVisibitity(iv_player_c_vc_image, View.INVISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            performItemClick((Integer) v.getTag(), v);
        }
    }
}
