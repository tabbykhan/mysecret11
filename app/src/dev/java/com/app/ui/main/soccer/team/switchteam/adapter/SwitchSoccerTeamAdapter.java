package com.app.ui.main.soccer.team.switchteam.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseRecycleAdapter;
import com.app.model.CustomerTeamModel;
import com.app.model.PlayerModel;
import com.utilities.DeviceScreenUtil;

import java.util.List;

/**
 * Created by Vishnu Gupta on 7/1/19.
 */
public class SwitchSoccerTeamAdapter extends AppBaseRecycleAdapter {
    Context context;
    List<CustomerTeamModel> list;

    CustomerTeamModel selectedTeam;

    public SwitchSoccerTeamAdapter(Context context, List<CustomerTeamModel> list) {
        this.context = context;
        this.list = list;
    }

    public CustomerTeamModel getSelectedTeam() {
        return selectedTeam;
    }

    public void setSelectedTeam(int position) {
        int previousPosition = -1;
        if (selectedTeam != null) {
            previousPosition = list.indexOf(selectedTeam);
        }
        if(previousPosition==position){
            selectedTeam=null;
            notifyItemChanged(position);
            return;
        }
        selectedTeam = list.get(position);
        if (previousPosition > -1) {
            notifyItemChanged(previousPosition);
        }
        notifyItemChanged(position);
    }

    @Override
    public BaseViewHolder getViewHolder() {
        return new ViewHolder(inflateLayout(R.layout.item_switch_team_adapter));
    }

    @Override
    public int getDataCount() {
        return list == null ? 0 : list.size();
    }

    public int getLastItemBottomMargin() {
        return 0;
    }

    public boolean isTeamAlreadyJoined(long teamId) {
        return false;
    }

    private class ViewHolder extends BaseViewHolder {

        CardView cv_data;
        CheckBox cb_team_name;
        TextView tv_team_name;
        TextView tv_already_added;
        ImageView iv_player;
        ProgressBar pb_image1;
        TextView tv_multiplier;
        TextView tv_player_name;
        View view_seprator1;
        ImageView iv_player2;
        ProgressBar pb_image2;
        TextView tv_multiplier2;
        TextView tv_player_name2;
        LinearLayout ll_edit;
        View view_seprator2;
        LinearLayout ll_preview;
        View view_seprator3;
        LinearLayout ll_clone;


        public ViewHolder(View itemView) {
            super(itemView);
            cv_data = itemView.findViewById(R.id.cv_data);
            cb_team_name = itemView.findViewById(R.id.cb_team_name);
            tv_team_name = itemView.findViewById(R.id.tv_team_name);
            tv_already_added = itemView.findViewById(R.id.tv_already_added);
            updateViewVisibitity(tv_already_added, View.GONE);
            iv_player = itemView.findViewById(R.id.iv_player);
            pb_image1 = itemView.findViewById(R.id.pb_image1);
            tv_multiplier = itemView.findViewById(R.id.tv_multiplier);
            tv_player_name = itemView.findViewById(R.id.tv_player_name);
            view_seprator1 = itemView.findViewById(R.id.view_seprator1);
            iv_player2 = itemView.findViewById(R.id.iv_player2);
            pb_image2 = itemView.findViewById(R.id.pb_image2);
            tv_multiplier2 = itemView.findViewById(R.id.tv_multiplier2);
            tv_player_name2 = itemView.findViewById(R.id.tv_player_name2);
            ll_edit = itemView.findViewById(R.id.ll_edit);
            view_seprator2 = itemView.findViewById(R.id.view_seprator2);
            ll_preview = itemView.findViewById(R.id.ll_preview);
            view_seprator3 = itemView.findViewById(R.id.view_seprator3);
            ll_clone = itemView.findViewById(R.id.ll_clone);

        }

        @Override
        public void setData(int position) {
            cb_team_name.setTag(position);
            ll_edit.setTag(position);
            ll_preview.setTag(position);
            ll_clone.setTag(position);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) cv_data.getLayoutParams();
            if (position == (list.size() - 1)) {
                layoutParams.bottomMargin = getLastItemBottomMargin();
            } else {
                layoutParams.bottomMargin = DeviceScreenUtil.getInstance().convertDpToPixel(10.f);
            }
            cv_data.setLayoutParams(layoutParams);

            CustomerTeamModel customerTeamModel = list.get(position);
            if (customerTeamModel == null) {
                updateViewVisibitity(tv_already_added, View.GONE);
                cv_data.setSelected(false);
                cb_team_name.setChecked(false);
                tv_team_name.setText("");
                iv_player.setImageResource(R.drawable.no_image);
                iv_player2.setImageResource(R.drawable.no_image);
                updateViewVisibitity(pb_image1, View.GONE);
                updateViewVisibitity(pb_image2, View.GONE);
                tv_multiplier.setText("");
                tv_multiplier2.setText("");
                tv_player_name.setText("");
                tv_player_name2.setText("");
                ll_edit.setOnClickListener(null);
                ll_preview.setOnClickListener(null);
                ll_clone.setOnClickListener(null);
                cb_team_name.setOnClickListener(null);
                cb_team_name.setEnabled(false);
            } else {
                if (isTeamAlreadyJoined(customerTeamModel.getId())) {
                    cb_team_name.setEnabled(false);
                    cv_data.setSelected(true);
                    cb_team_name.setChecked(true);
                    cv_data.setAlpha(0.5f);
                    updateViewVisibitity(tv_already_added, View.VISIBLE);
                } else {
                    cb_team_name.setEnabled(true);
                    updateViewVisibitity(tv_already_added, View.GONE);
                    cv_data.setAlpha(1.0f);
                    if (selectedTeam != null) {
                        if (selectedTeam.equals(customerTeamModel)) {
                            cv_data.setSelected(true);
                            cb_team_name.setChecked(true);
                        } else {
                            cv_data.setSelected(false);
                            cb_team_name.setChecked(false);
                        }
                    } else {
                        cv_data.setSelected(false);
                        cb_team_name.setChecked(false);
                    }

                }

                ll_edit.setOnClickListener(this);
                ll_preview.setOnClickListener(this);
                ll_clone.setOnClickListener(this);
                cb_team_name.setOnClickListener(this);


                tv_team_name.setText("TEAM " + customerTeamModel.getName());

                PlayerModel highestPositionPlayer = customerTeamModel.getCaptain();
                if (highestPositionPlayer == null) {
                    iv_player.setImageResource(R.drawable.no_image);
                    tv_multiplier.setText("");
                    tv_player_name.setText("");
                } else {
                    tv_multiplier.setText(highestPositionPlayer.getTotalPointsText());
                    tv_player_name.setText(highestPositionPlayer.getName());
                    ((AppBaseActivity) context).loadImage(context, iv_player, pb_image1, highestPositionPlayer.getImage(),
                            R.drawable.no_image);
                }

                PlayerModel lowestPositionPlayer = customerTeamModel.getVise_captain();
                if (lowestPositionPlayer == null) {
                    iv_player2.setImageResource(R.drawable.no_image);
                    tv_multiplier2.setText("");
                    tv_player_name2.setText("");
                } else {
                    tv_multiplier2.setText(lowestPositionPlayer.getTotalPointsText());
                    tv_player_name2.setText(lowestPositionPlayer.getName());
                    ((AppBaseActivity) context).loadImage(context, iv_player2, pb_image2, lowestPositionPlayer.getImage(),
                            R.drawable.no_image);
                }

            }


        }

        @Override
        public void onClick(View v) {
            performItemClick((Integer) v.getTag(), v);
        }
    }
}
