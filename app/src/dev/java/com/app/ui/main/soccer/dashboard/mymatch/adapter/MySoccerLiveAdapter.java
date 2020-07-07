package com.app.ui.main.soccer.dashboard.mymatch.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseRecycleAdapter;
import com.app.model.GameTypeModel;
import com.app.model.MatchModel;
import com.app.model.SeriesModel;
import com.app.model.TeamModel;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by Vishnu Gupta on 7/1/19.
 */
public class MySoccerLiveAdapter extends AppBaseRecycleAdapter {

    Context context;
    List<MatchModel> list;


    public MySoccerLiveAdapter(Context context, List<MatchModel> list) {
        this.context = context;
        this.list = list;
    }

    public int getViewHeight() {
        return 0;
    }

    @Override
    public BaseViewHolder getViewHolder() {
        return new ViewHolder(inflateLayout(R.layout.item_upcoming_tournament_adapter));
    }

    @Override
    public int getDataCount() {
        return list == null ? 0 : list.size();
    }

    private class ViewHolder extends BaseViewHolder {

        View view_disable_layer;
        LinearLayout ll_view;
        SimpleDraweeView iv_team1;
        ProgressBar pb_image1;
        TextView tv_match_name;
        TextView tv_match_name_sec;
        TextView tv_team1_name;
        TextView tv_team2_name;
        LinearLayout ll_timer_lay;
        TextView tv_timer_time;
        LinearLayout ll_match_status;
        TextView tv_status;
        SimpleDraweeView iv_team2;
        ProgressBar pb_image2;

        View view_seprator;
        LinearLayout ll_joined_contest;
        TextView tv_joined_contest_count;
        TextView tv_match_squad;

        //CardView cv_timer_lay;


        public ViewHolder(View itemView) {
            super(itemView);
            view_disable_layer = itemView.findViewById(R.id.view_disable_layer);
            ll_view = itemView.findViewById(R.id.ll_view);
            iv_team1 = itemView.findViewById(R.id.iv_team1);
            pb_image1 = itemView.findViewById(R.id.pb_image1);
            tv_match_name = itemView.findViewById(R.id.tv_match_name);
            tv_match_name_sec = itemView.findViewById(R.id.tv_match_name_sec);
            tv_team1_name = itemView.findViewById(R.id.tv_team1_name);
            tv_team2_name = itemView.findViewById(R.id.tv_team2_name);
            ll_timer_lay = itemView.findViewById(R.id.ll_timer_lay);
            tv_timer_time = itemView.findViewById(R.id.tv_timer_time);
            ll_match_status = itemView.findViewById(R.id.ll_match_status);
            tv_status = itemView.findViewById(R.id.tv_status);
            iv_team2 = itemView.findViewById(R.id.iv_team2);
            pb_image2 = itemView.findViewById(R.id.pb_image2);
            view_seprator = itemView.findViewById(R.id.view_seprator);
            ll_joined_contest = itemView.findViewById(R.id.ll_joined_contest);
            tv_joined_contest_count = itemView.findViewById(R.id.tv_joined_contest_count);
            tv_match_squad = itemView.findViewById(R.id.tv_match_squad);
            //cv_timer_lay = itemView.findViewById(R.id.cv_timer_lay);


        }

        @Override
        public void setData(int position) {
            updateViewVisibitity(ll_timer_lay, View.GONE);
            updateViewVisibitity(view_disable_layer, View.GONE);

//            CardView.LayoutParams layoutParams = (CardView.LayoutParams) ll_view.getLayoutParams();
//            layoutParams.height = getViewHeight();
//            ll_view.setLayoutParams(layoutParams);

//            int bg = position % backgrounds.length;
//
//            ll_view.setBackground(context.getResources().getDrawable(backgrounds[bg]));

            MatchModel matchModel = list.get(position);
            if (matchModel == null) return;

            tv_joined_contest_count.setText(String.valueOf(matchModel.getContest_count()) + " Contest Joined");

            tv_match_squad.setText("");

            SeriesModel series = matchModel.getSeries();
            if (series != null) {
                tv_match_name.setText(series.getName());
            } else {
                tv_match_name.setText("");
            }
            GameTypeModel gameType = matchModel.getGametype();
            if (gameType != null) {
                tv_match_name_sec.setText(gameType.getName());
            } else {
                tv_match_name_sec.setText("");
            }
            tv_status.setText(matchModel.getRemainTimeText());
            tv_status.setTextColor(context.getResources().getColor(matchModel
                    .getTimerColor2()));
            //ll_match_status.setBackgroundResource(R.drawable.bg_transparent_5radius_stroke1orange);

            TeamModel team1 = matchModel.getTeam1();
            if (team1 != null) {
                tv_team1_name.setText(team1.getName(1));
               /* ((AppBaseActivity) context).loadImage(context, iv_team1, pb_image1, team1.getImage(),
                        R.mipmap.ic_launcher_round);*/
                ((AppBaseActivity) context).loadImageSDV(context, iv_team1, pb_image1, team1.getImage(),
                        R.mipmap.ic_launcher_round);
            } else {
                updateViewVisibitity(pb_image1, View.INVISIBLE);
                iv_team1.setImageResource(R.mipmap.ic_launcher_round);
                tv_team1_name.setText("");
            }

            TeamModel team2 = matchModel.getTeam2();
            if (team2 != null) {
                tv_team2_name.setText(team2.getName(1));
                ((AppBaseActivity) context).loadImage(context, iv_team2, pb_image2, team2.getImage(),
                        R.mipmap.ic_launcher_round);
            } else {
                updateViewVisibitity(pb_image2, View.INVISIBLE);
                iv_team2.setImageResource(R.mipmap.ic_launcher_round);
                tv_team2_name.setText("");
            }

        }
    }
}
