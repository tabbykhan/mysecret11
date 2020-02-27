package com.app.ui.main.cricket.dialogs.playerdetaildialog.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.R;
import com.app.appbase.AppBaseRecycleAdapter;
import com.app.model.PlayerStatsModel;

import java.util.List;

/**
 * Created by Vishnu Gupta on 7/1/19.
 */
public class PlayerStatsAdapter extends AppBaseRecycleAdapter {

    Context context;
    List<PlayerStatsModel> list;

    public PlayerStatsAdapter(Context context, List<PlayerStatsModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public BaseViewHolder getViewHolder() {
        return new ViewHolder(inflateLayout(R.layout.item_players_stats_adapter));
    }

    public int getLastItemBottomMargin() {
        return 0;
    }

    @Override
    public int getDataCount() {
        return list == null ? 0 : list.size();
    }

    private class ViewHolder extends BaseViewHolder {

        LinearLayout ll_data_lay;
        TextView tv_match_name;
        TextView tv_match_date;
        TextView tv_points;
        TextView tv_selected_by;
        View view_seprator;

        public ViewHolder(View itemView) {
            super(itemView);
            ll_data_lay = itemView.findViewById(R.id.ll_data_lay);
            tv_match_name = itemView.findViewById(R.id.tv_match_name);
            tv_match_date = itemView.findViewById(R.id.tv_match_date);
            tv_points = itemView.findViewById(R.id.tv_points);
            tv_selected_by = itemView.findViewById(R.id.tv_selected_by);
            view_seprator = itemView.findViewById(R.id.view_seprator);
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

            PlayerStatsModel playerStatsModel = list.get(position);
            if (playerStatsModel == null) {
                tv_match_name.setText("");
                tv_match_date.setText("");
                tv_points.setText("");
                tv_selected_by.setText("");
            } else {
                tv_match_name.setText(playerStatsModel.getMatch_name());
                tv_match_date.setText(playerStatsModel.getMatchDateText());
                tv_points.setText(playerStatsModel.getPointsText());
                tv_selected_by.setText(playerStatsModel.getSelectedByText());
            }


        }
    }
}
