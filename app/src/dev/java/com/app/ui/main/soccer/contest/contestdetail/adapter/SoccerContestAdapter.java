package com.app.ui.main.soccer.contest.contestdetail.adapter;

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

import java.util.List;

/**
 * Created by Vishnu Gupta on 7/1/19.
 */
public class SoccerContestAdapter extends AppBaseRecycleAdapter {

    Context context;
    List<ContestModel> list;

    boolean viewMoreEnable;

    public SoccerContestAdapter(Context context, List<ContestModel> list, boolean viewMoreEnable) {
        this.context = context;
        this.list = list;
        this.viewMoreEnable = viewMoreEnable;
    }

    public boolean isDiscountedCategory() {
        return false;
    }

    @Override
    public BaseViewHolder getViewHolder() {
        return new ViewHolder(inflateLayout(R.layout.item_contest_view_adapter));
    }

    @Override
    public int getDataCount() {
        return list == null ? 0 : ((list.size() > 3) ? ((viewMoreEnable) ? list.size() : 3) :
                list.size());
    }

    private class ViewHolder extends BaseViewHolder {

        TextView tv_price_pool;
        LinearLayout ll_winners;
        TextView tv_winners;
        ImageView iv_winners;
        TextView tv_entry;
        ProgressBar pb_teams;
        TextView tv_teams_left;
        TextView tv_total_teams;
        TextView tv_join;
        TextView tv_single_multi;
        TextView tv_confirm_win;
        View view_seprator;
        TextView tv_more_entry;
        ImageView iv_discount_image;

        LinearLayout ll_contest_share;
        ImageView iv_contest_share;
        TextView tv_discount;


        public ViewHolder(View itemView) {
            super(itemView);
            tv_single_multi = itemView.findViewById(R.id.tv_single_multi);
            tv_confirm_win = itemView.findViewById(R.id.tv_confirm_win);
            tv_price_pool = itemView.findViewById(R.id.tv_price_pool);
            ll_winners = itemView.findViewById(R.id.ll_winners);
            tv_winners = itemView.findViewById(R.id.tv_winners);
            iv_winners = itemView.findViewById(R.id.iv_winners);
            tv_entry = itemView.findViewById(R.id.tv_entry);
            pb_teams = itemView.findViewById(R.id.pb_teams);
            tv_teams_left = itemView.findViewById(R.id.tv_teams_left);
            tv_total_teams = itemView.findViewById(R.id.tv_total_teams);
            tv_join = itemView.findViewById(R.id.tv_join);
            view_seprator = itemView.findViewById(R.id.view_seprator);
            tv_more_entry = itemView.findViewById(R.id.tv_more_entry);
            iv_discount_image = itemView.findViewById(R.id.iv_discount_image);

            ll_contest_share = itemView.findViewById(R.id.ll_contest_share);
            iv_contest_share = itemView.findViewById(R.id.iv_contest_share);
            tv_discount = itemView.findViewById(R.id.tv_discount);
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
            if (contestModel != null) {
                tv_join.setOnClickListener(this);
                ll_winners.setOnClickListener(this);
                iv_contest_share.setOnClickListener(this);
                tv_price_pool.setText(((AppBaseActivity) context).currency_symbol
                        + contestModel.getTotalPriceText());
                tv_entry.setText(((AppBaseActivity) context).currency_symbol
                        + contestModel.getEntryFeesText());
                tv_winners.setText(String.valueOf(contestModel.getTotal_winners()));
                if (contestModel.getTotal_winners() > 1) {
                    updateViewVisibitity(iv_winners, View.VISIBLE);
                } else {
                    updateViewVisibitity(iv_winners, View.GONE);
                }

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

                if (contestModel.isConfirmWin()) {
                    updateViewVisibitity(tv_confirm_win, View.VISIBLE);
                } else {
                    updateViewVisibitity(tv_confirm_win, View.GONE);
                }

                if (contestModel.isMultiJoinContest()) {
                    tv_single_multi.setText("M");
                    updateViewVisibitity(tv_single_multi, View.VISIBLE);
                } else {
                    tv_single_multi.setText("S");
                    updateViewVisibitity(tv_single_multi, View.VISIBLE);
                }

                String moreEntryFeesText = contestModel.getMoreEntryFeesText();
                if (isValidString(moreEntryFeesText)) {
                    tv_more_entry.setText(((AppBaseActivity) context).currency_symbol
                            + moreEntryFeesText);

                    if (isDiscountedCategory()) {
                        updateViewVisibitity(iv_discount_image, View.GONE);
                    } else {
                        int[] discountImageSizeForCategory = contestModel.getDiscountImageSizeForContest();
                        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) iv_discount_image.getLayoutParams();
                        layoutParams1.width = discountImageSizeForCategory[0];
                        layoutParams1.height = discountImageSizeForCategory[1];
                        iv_discount_image.setLayoutParams(layoutParams1);

                        ((AppBaseActivity) context).loadImage(context, iv_discount_image, null,
                                contestModel.getDiscount_image(),
                                R.mipmap.ic_launcher);
                        updateViewVisibitity(iv_discount_image, View.VISIBLE);
                    }

                } else {
                    tv_more_entry.setText("");
                    updateViewVisibitity(iv_discount_image, View.GONE);
                }

                if (contestModel.getCash_bonus_used_type().equals("F")) {
                    tv_discount.setText("B- " + ((AppBaseActivity) context).currency_symbol+contestModel.getTotalDiscountText());
                } else {
                    tv_discount.setText("B-" + contestModel.getTotalDiscountText() + "%");
                }


            } else {
                updateViewVisibitity(ll_contest_share, View.GONE);
                updateViewVisibitity(tv_confirm_win, View.GONE);
                updateViewVisibitity(tv_single_multi, View.GONE);

                tv_join.setOnClickListener(null);
                ll_winners.setOnClickListener(null);
                iv_contest_share.setOnClickListener(null);
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
