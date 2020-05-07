package com.app.ui.main.cricket.matchstats.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseRecycleAdapter;
import com.app.model.MatchModel;
import com.app.model.PlayerStatsModel;

import java.util.List;

/**
 * Created by Vishnu Gupta on 7/1/19.
 */
public class MatchStatsAdapter extends AppBaseRecycleAdapter {

    Context context;
    List<PlayerStatsModel> list;

    public MatchStatsAdapter(Context context, List<PlayerStatsModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public BaseViewHolder getViewHolder() {
        return new ViewHolder(inflateLayout(R.layout.item_match_stats_adapter));
    }

    public int getLastItemBottomMargin() {
        return 0;
    }

    @Override
    public int getDataCount() {
        return list == null ? 0 : list.size();
    }

    public MatchModel getMatchModel() {
        return null;
    }

    private class ViewHolder extends BaseViewHolder {

        LinearLayout ll_data_lay;
        ImageView iv_player;
        ProgressBar pb_image;
        TextView tv_player_name;
        TextView tv_player_country;
        TextView tv_player_type;
        ImageView iv_your_player;
        ImageView iv_top_player;
        TextView tv_selected_by;
        TextView tv_points;
        View view_seprator;

        public ViewHolder(View itemView) {
            super(itemView);
            ll_data_lay = itemView.findViewById(R.id.ll_data_lay);
            iv_player = itemView.findViewById(R.id.iv_player);
            pb_image = itemView.findViewById(R.id.pb_image);
            tv_player_name = itemView.findViewById(R.id.tv_player_name);
            tv_player_country = itemView.findViewById(R.id.tv_player_country);
            tv_player_type = itemView.findViewById(R.id.tv_player_type);
            iv_your_player = itemView.findViewById(R.id.iv_your_player);
            iv_top_player = itemView.findViewById(R.id.iv_top_player);
            tv_selected_by = itemView.findViewById(R.id.tv_selected_by);
            tv_points = itemView.findViewById(R.id.tv_points);
            view_seprator = itemView.findViewById(R.id.view_seprator);
            updateViewVisibitity(pb_image, View.GONE);
        }

        @Override
        public void setData(int position) {

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view_seprator.getLayoutParams();
            if (position == (list.size() - 1)) {
                layoutParams.bottomMargin = getLastItemBottomMargin();
            } else {
                layoutParams.bottomMargin = 0;
            }
            view_seprator.setLayoutParams(layoutParams);


            PlayerStatsModel playerModel = list.get(position);
            if (playerModel == null || getMatchModel() == null) {
                iv_player.setImageResource(R.drawable.no_image);
                updateViewVisibitity(pb_image, View.GONE);
                updateViewVisibitity(iv_your_player, View.INVISIBLE);
                updateViewVisibitity(iv_top_player, View.INVISIBLE);
                tv_player_name.setText("");
                tv_selected_by.setText("");
                tv_points.setText("");
                tv_player_country.setText("");
                tv_player_type.setText("");
            } else {
                ((AppBaseActivity) context).loadImage(context, iv_player, pb_image, playerModel.getImage(),
                        R.drawable.no_image);
                tv_player_name.setText(playerModel.getName());
                tv_selected_by.setText(playerModel.getSelectedByText());
                tv_points.setText(playerModel.getPointsText());
                tv_player_country.setText(playerModel.getTeamName());
                tv_player_type.setText(playerModel.getPositionShortName());

                if (playerModel.isMyPlayer()) {
                    iv_your_player.setActivated(true);
                    iv_your_player.setSelected(true);
                } else {
                    iv_your_player.setActivated(false);
                    iv_your_player.setSelected(false);
                }
                if (getMatchModel().isPastMatch()) {
                    if (playerModel.isDreamTeamPlayer()) {
                        updateViewVisibitity(iv_top_player, View.VISIBLE);
                        Log.i("playerstate", "---------VISIBLE-------");
                    } else {
                        updateViewVisibitity(iv_top_player, View.INVISIBLE);
                        Log.i("playerstate", "---------INVISIBLE 1-------");
                    }

                } else {
                    updateViewVisibitity(iv_top_player, View.INVISIBLE);
                    Log.i("playerstate", "---------INVISIBLE 2-------");
                }
            }

        }
    }
}
