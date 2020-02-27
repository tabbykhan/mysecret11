package com.app.ui.main.cricket.mycontest.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseRecycleAdapter;
import com.app.model.ContestModel;
import com.app.model.MatchModel;

import java.util.List;

/**
 * Created by Vishnu Gupta on 7/1/19.
 */
public class MyContestAdapter extends AppBaseRecycleAdapter {

    Context context;
    List<ContestModel> list;

    public MyContestAdapter(Context context, List<ContestModel> list) {
        this.context = context;
        this.list = list;
    }

    public boolean isDiscountedCategory(){
        return false;
    }

    @Override
    public BaseViewHolder getViewHolder() {
        return new ViewHolder(inflateLayout(R.layout.item_mycontest_view_adapter));
    }

    @Override
    public int getDataCount() {
        return list == null ? 0 : list.size();
    }

    public MatchModel getMatchModel() {
        return null;
    }

    private class ViewHolder extends BaseViewHolder {

        TextView tv_price_pool;
        LinearLayout ll_winners;
        TextView tv_winners;
        ImageView iv_winners;
        TextView tv_entry;
        LinearLayout ll_join_lay;
        ProgressBar pb_teams;
        TextView tv_teams_left;
        TextView tv_total_teams;
        TextView tv_join;
        View view_seprator;
        View view_seprator1;
        LinearLayout ll_stats_lay;
        TextView tv_joined_teams;
        TextView tv_points;
        TextView tv_rank;
        LinearLayout ll_view_leaderboard;
        ImageView iv_note;
        TextView tv_leaderboard;
        TextView tv_more_entry;
        ImageView iv_discount_image;

        LinearLayout ll_contest_share;
        ImageView iv_contest_share;


        public ViewHolder(View itemView) {
            super(itemView);
            tv_price_pool = itemView.findViewById(R.id.tv_price_pool);
            ll_winners = itemView.findViewById(R.id.ll_winners);
            tv_winners = itemView.findViewById(R.id.tv_winners);
            iv_winners = itemView.findViewById(R.id.iv_winners);
            tv_entry = itemView.findViewById(R.id.tv_entry);
            ll_join_lay = itemView.findViewById(R.id.ll_join_lay);
            pb_teams = itemView.findViewById(R.id.pb_teams);
            tv_teams_left = itemView.findViewById(R.id.tv_teams_left);
            tv_total_teams = itemView.findViewById(R.id.tv_total_teams);
            tv_join = itemView.findViewById(R.id.tv_join);
            view_seprator = itemView.findViewById(R.id.view_seprator);
            view_seprator1 = itemView.findViewById(R.id.view_seprator1);
            ll_stats_lay = itemView.findViewById(R.id.ll_stats_lay);
            tv_joined_teams = itemView.findViewById(R.id.tv_joined_teams);
            tv_points = itemView.findViewById(R.id.tv_points);
            tv_rank = itemView.findViewById(R.id.tv_rank);
            ll_view_leaderboard = itemView.findViewById(R.id.ll_view_leaderboard);
            iv_note = itemView.findViewById(R.id.iv_note);
            tv_leaderboard = itemView.findViewById(R.id.tv_leaderboard);

            ll_contest_share = itemView.findViewById(R.id.ll_contest_share);
            iv_contest_share = itemView.findViewById(R.id.iv_contest_share);
            tv_more_entry = itemView.findViewById(R.id.tv_more_entry);
            iv_discount_image = itemView.findViewById(R.id.iv_discount_image);
        }

        @Override
        public void setData(int position) {
            tv_join.setTag(position);
            ll_winners.setTag(position);
            iv_contest_share.setTag(position);

            if (position == list.size() - 1) {
                updateViewVisibitity(view_seprator, View.INVISIBLE);
            } else {
                updateViewVisibitity(view_seprator, View.VISIBLE);
            }
            ContestModel contestModel = list.get(position);
            if (contestModel != null && getMatchModel() != null) {
                tv_join.setOnClickListener(this);
                ll_winners.setOnClickListener(this);

                tv_price_pool.setText(((AppBaseActivity) context).currency_symbol
                        + contestModel.getTotalPriceText());
                tv_entry.setText(((AppBaseActivity) context).currency_symbol +
                        contestModel.getEntryFeesText());
                tv_winners.setText(String.valueOf(contestModel.getTotal_winners()));
                if (contestModel.getTotal_winners() > 1) {
                    updateViewVisibitity(iv_winners, View.VISIBLE);
                } else {
                    updateViewVisibitity(iv_winners, View.GONE);
                }

                String moreEntryFeesText = contestModel.getMoreEntryFeesText();
                if(isValidString(moreEntryFeesText)){
                    tv_more_entry.setText(((AppBaseActivity) context).currency_symbol
                            + moreEntryFeesText);

                    if(isDiscountedCategory()){
                        updateViewVisibitity(iv_discount_image,View.GONE);
                    }else{
                        int[] discountImageSizeForCategory = contestModel.getDiscountImageSizeForContest();
                        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) iv_discount_image.getLayoutParams();
                        layoutParams1.width=discountImageSizeForCategory[0];
                        layoutParams1.height=discountImageSizeForCategory[1];
                        iv_discount_image.setLayoutParams(layoutParams1);

                        ((AppBaseActivity) context).loadImage(context, iv_discount_image, null,
                                contestModel.getDiscount_image(),
                                R.mipmap.ic_launcher_notification);
                        updateViewVisibitity(iv_discount_image,View.VISIBLE);
                    }

                }else{
                    tv_more_entry.setText("");
                    updateViewVisibitity(iv_discount_image,View.GONE);
                }

                if (getMatchModel().isFixtureMatch()) {

                    updateViewVisibitity(ll_join_lay, View.VISIBLE);
                    updateViewVisibitity(tv_join, View.VISIBLE);
                    updateViewVisibitity(view_seprator1, View.GONE);
                    updateViewVisibitity(ll_stats_lay, View.GONE);
                    updateViewVisibitity(ll_view_leaderboard, View.GONE);

                    pb_teams.setMax(contestModel.getTotal_team());
                    pb_teams.setProgress(contestModel.getTotal_team() - contestModel.getTotal_team_left());
                    tv_teams_left.setText("Only " + contestModel.getTotal_team_left() + " spots left");
                    tv_total_teams.setText(contestModel.getTotal_team() + " Teams");
                    if (!contestModel.isContestFull()) {
                        iv_contest_share.setOnClickListener(this);
                        iv_contest_share.setAlpha(1.0f);
                        if (contestModel.isMoreJoinAvailable()) {
                            if (contestModel.isAtleastOneTeamJoin()) {
                                tv_join.setText("JOIN+");
                            } else {
                                tv_join.setText("JOIN");
                            }
                        } else {
                            tv_join.setText("JOINED");
                        }
                    } else {
                        iv_contest_share.setOnClickListener(null);
                        iv_contest_share.setAlpha(0.2f);
                        if (contestModel.isAtleastOneTeamJoin()) {
                            tv_join.setText("JOINED");
                        } else {
                            tv_join.setText("FULL");
                        }
                    }

                    if (contestModel.isAtleastOneTeamJoin()) {
                        updateViewVisibitity(ll_contest_share, View.VISIBLE);
                    } else {
                        updateViewVisibitity(ll_contest_share, View.GONE);
                    }

                } else {
                    iv_contest_share.setOnClickListener(null);
                    updateViewVisibitity(ll_contest_share, View.GONE);

                    contestModel.calculateData();
                    updateViewVisibitity(ll_join_lay, View.GONE);
                    updateViewVisibitity(tv_join, View.GONE);
                    updateViewVisibitity(view_seprator1, View.VISIBLE);
                    updateViewVisibitity(ll_stats_lay, View.VISIBLE);
                    updateViewVisibitity(ll_view_leaderboard, View.VISIBLE);

                    tv_joined_teams.setText(contestModel.getJoinedWithText());
                    tv_points.setText(contestModel.getTotalPointstext());
                    tv_rank.setText(contestModel.getNewRankText());
                    if (contestModel.getHighest_rank_win_amount() > 0) {
                        updateViewVisibitity(iv_note, View.VISIBLE);
                        tv_leaderboard.setText("Your Winning  " +
                                ((AppBaseActivity) context).currency_symbol + contestModel.getWinAmountText());
                    }else if (contestModel.getHighest_rank_refund_amount() > 0) {
                        updateViewVisibitity(iv_note, View.VISIBLE);
                        tv_leaderboard.setText("Refund  " +
                                ((AppBaseActivity) context).currency_symbol + contestModel.getRefundAmountText());
                    } else {
                        updateViewVisibitity(iv_note, View.GONE);
                        tv_leaderboard.setText("View leaderboard");
                    }
                }

            } else {
                iv_contest_share.setOnClickListener(null);

                updateViewVisibitity(ll_contest_share, View.GONE);
                tv_join.setOnClickListener(null);
                ll_winners.setOnClickListener(null);
                updateViewVisibitity(view_seprator1, View.GONE);
                updateViewVisibitity(ll_join_lay, View.GONE);
                updateViewVisibitity(tv_join, View.GONE);
                updateViewVisibitity(ll_stats_lay, View.GONE);
                updateViewVisibitity(ll_view_leaderboard, View.GONE);
                tv_price_pool.setText("");
                tv_winners.setText("");
                tv_entry.setText("");
                pb_teams.setProgress(0);
                tv_teams_left.setText("");
                tv_total_teams.setText("");
            }

        }


        @Override
        public void onClick(View v) {
            performItemClick((Integer) v.getTag(), v);
        }
    }
}
