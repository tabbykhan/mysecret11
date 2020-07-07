package com.app.ui.main.soccer.team.myteam.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.R;
import com.app.appbase.AppBaseRecycleAdapter;
import com.app.model.CustomerTeamModel;
import com.app.model.PlayerModel;
import com.utilities.DeviceScreenUtil;

import java.util.List;

/**
 * Created by Vishnu Gupta on 7/1/19.
 */
public class SoccerMyTeamsAdapter extends AppBaseRecycleAdapter {

    Context context;

    List<CustomerTeamModel> list;

    public SoccerMyTeamsAdapter(Context context, List<CustomerTeamModel> list) {
        this.context = context;
        this.list = list;

    }

    @Override
    public BaseViewHolder getViewHolder() {
        return new ViewHolder(inflateLayout(R.layout.item_soccer_my_team_adapter));
    }

    @Override
    public int getDataCount() {
        return list == null ? 0 : list.size();
    }


    public int getLastItemBottomMargin() {
        return 0;
    }

    public boolean isCloneAvailable() {
        return true;
    }

    private class ViewHolder extends BaseViewHolder {

        CardView cv_data;

        TextView tv_team_name, /*tv_team_1_name, tv_team_1_players,
                tv_team_2_name, tv_team_2_players,*/ tv_captain_name, tv_vicecaptain_name;
        ImageView iv_edit, iv_clone;//, img_captain, img_vcaptain;
        TextView tv_tot_wk, tv_tot_bat, tv_tot_ar, tv_tot_bow;
        TextView tv_captain_title, tv_vice_captain_title;
        LinearLayout ll_edit;
        LinearLayout ll_clone;
        ImageView iv_share_team;

        LinearLayout ll_wk;
        private TextView tv_wk;
        private TextView tv_bat;
        private TextView tv_all;
        private TextView bowl;
        private TextView tv_mmp_name;
        private TextView tv_mmp_title;


        public ViewHolder(View itemView) {
            super(itemView);
            cv_data = itemView.findViewById(R.id.cv_data);
            tv_team_name = itemView.findViewById(R.id.tv_team_name);
            iv_edit = itemView.findViewById(R.id.iv_edit);
            iv_clone = itemView.findViewById(R.id.iv_clone);
            /*img_captain = itemView.findViewById(R.id.img_captain);
            img_vcaptain = itemView.findViewById(R.id.img_vcaptain);
            tv_team_1_name = itemView.findViewById(R.id.tv_team_1_name);
            tv_team_1_players = itemView.findViewById(R.id.tv_team_1_players);
            tv_team_2_name = itemView.findViewById(R.id.tv_team_2_name);
            tv_team_2_players = itemView.findViewById(R.id.tv_team_2_players);*/
            tv_captain_name = itemView.findViewById(R.id.tv_captain_name);
            tv_vicecaptain_name = itemView.findViewById(R.id.tv_vicecaptain_name);
            tv_mmp_name = itemView.findViewById(R.id.tv_mmp_name);
            tv_mmp_title = itemView.findViewById(R.id.tv_mmp_title);

            tv_tot_wk = itemView.findViewById(R.id.tv_tot_wk);
            tv_tot_bat = itemView.findViewById(R.id.tv_tot_bat);
            tv_tot_ar = itemView.findViewById(R.id.tv_tot_ar);
            tv_tot_bow = itemView.findViewById(R.id.tv_tot_bow);
            iv_share_team = itemView.findViewById(R.id.iv_share_team);

            tv_captain_title = itemView.findViewById(R.id.tv_captain_title);
            tv_vice_captain_title = itemView.findViewById(R.id.tv_vice_captain_title);
            ll_edit = itemView.findViewById(R.id.ll_edit);
            ll_clone = itemView.findViewById(R.id.ll_clone);
            iv_share_team = itemView.findViewById(R.id.iv_share_team);

            ll_wk = itemView.findViewById(R.id.ll_wk);
            tv_wk = itemView.findViewById(R.id.tv_wk);
            tv_bat = itemView.findViewById(R.id.tv_bat);
            tv_all = itemView.findViewById(R.id.tv_all);
            bowl = itemView.findViewById(R.id.bowl);

        }

        @Override
        public void setData(int position) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) cv_data.getLayoutParams();
            if (position == (list.size() - 1)) {
                layoutParams.bottomMargin = getLastItemBottomMargin();
            } else {
                layoutParams.bottomMargin = DeviceScreenUtil.getInstance().convertDpToPixel(5.0f);
            }
            cv_data.setLayoutParams(layoutParams);

            iv_edit.setTag(position);
            iv_edit.setOnClickListener(this);
            ll_edit.setTag(position);
            ll_edit.setOnClickListener(this);
            iv_share_team.setTag(position);
            iv_share_team.setOnClickListener(this);
            iv_clone.setTag(position);
            ll_clone.setTag(position);

            if (isCloneAvailable()) {
                iv_clone.setOnClickListener(this);
                ll_clone.setOnClickListener(this);
                updateViewVisibitity(ll_clone, View.VISIBLE);
            } else {
                iv_clone.setOnClickListener(null);
                ll_clone.setOnClickListener(null);
                updateViewVisibitity(ll_clone, View.GONE);
            }
            CustomerTeamModel customerTeamModel = list.get(position);
            customerTeamModel.updateTeamInfo();
            tv_team_name.setText("TEAM " + customerTeamModel.getName());
            /*tv_team_1_name.setText(customerTeamModel.getTeam1ShortName());
            tv_team_2_name.setText(customerTeamModel.getTeam2ShortName());
            tv_team_1_players.setText(String.valueOf(customerTeamModel.getTeam1Players()));
            tv_team_2_players.setText(String.valueOf(customerTeamModel.getTeam2Players()));*/


            tv_tot_wk.setText(String.valueOf(customerTeamModel.getGoalkeepers().size()) );
            tv_tot_bat.setText(  String.valueOf(customerTeamModel.getDefenders().size()));
            tv_tot_ar.setText( String.valueOf(customerTeamModel.getMidfielders().size()) );
            tv_tot_bow.setText( String.valueOf(customerTeamModel.getForwards().size()));

            PlayerModel captainmodel = customerTeamModel.getCaptain();
            if (captainmodel != null) {
                tv_captain_name.setText(captainmodel.getName());
                /*((AppBaseActivity) context).loadImage(context, img_captain, null,
                        captainmodel.getImage(), R.drawable.no_image);*/
                if (customerTeamModel.isPlayerFromTeam1(captainmodel)) {
                    tv_captain_name.setActivated(true);
                    tv_captain_title.setActivated(true);
                } else {
                    tv_captain_name.setActivated(false);
                    tv_captain_title.setActivated(false);
                }

            } else {
                tv_captain_name.setText("");
                /*img_captain.setImageResource(R.drawable.no_image);*/
                tv_captain_name.setActivated(false);
                tv_captain_title.setActivated(false);
            }

            PlayerModel vise_captainmodel = customerTeamModel.getVise_captain();
            if (vise_captainmodel != null) {
                tv_vicecaptain_name.setText(vise_captainmodel.getName());

                Log.i("my team ", "------");
                /*((AppBaseActivity) context).loadImage(context, img_vcaptain, null,
                        vise_captainmodel.getImage(), R.drawable.no_image);*/
                if (customerTeamModel.isPlayerFromTeam1(vise_captainmodel)) {
                    tv_vicecaptain_name.setActivated(true);
                    tv_vice_captain_title.setActivated(true);
                } else {
                    tv_vicecaptain_name.setActivated(false);
                    tv_vice_captain_title.setActivated(true);
                }
            } else {
                tv_vicecaptain_name.setText("");
                /*img_vcaptain.setImageResource(R.drawable.no_image);*/
              //  tv_vicecaptain_name.setActivated(false);
               tv_vice_captain_title.setActivated(true);
            }
            PlayerModel mpp = customerTeamModel.getTrump();
            if (mpp != null) {
                tv_mmp_name.setText(mpp.getName());
                /*((AppBaseActivity) context).loadImage(context, img_vcaptain, null,
                        vise_captainmodel.getImage(), R.drawable.no_image);*/
                if (customerTeamModel.isPlayerFromTeam1(mpp)) {
                    tv_mmp_name.setActivated(true);
                    tv_mmp_title.setActivated(false);
                } else {
                    tv_mmp_name.setActivated(false);
                    tv_mmp_title.setActivated(false);
                }
            } else {
                tv_mmp_name.setText("");
                /*img_vcaptain.setImageResource(R.drawable.no_image);*/
                tv_mmp_name.setActivated(false);
                tv_mmp_title.setActivated(false);
            }

        }

        @Override
        public void onClick(View v) {
            performItemClick((Integer) v.getTag(), v);
        }
    }
}
