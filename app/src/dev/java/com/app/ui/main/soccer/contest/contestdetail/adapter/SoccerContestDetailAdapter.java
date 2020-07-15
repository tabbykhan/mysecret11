package com.app.ui.main.soccer.contest.contestdetail.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseRecycleAdapter;
import com.app.model.ContestTeamModel;
import com.app.model.UserModel;

import java.util.List;

/**
 * Created by Vishnu Gupta on 7/1/19.
 */
public class SoccerContestDetailAdapter extends AppBaseRecycleAdapter {
    Context context;
    List<ContestTeamModel> list;

    boolean viewMoreEnable;
    private boolean loadMore = false;

    public boolean isDiscountedCategory() {
        return false;
    }
    public SoccerContestDetailAdapter(Context context, List<ContestTeamModel> list) {
        this.context = context;
        this.list = list;

    }

    public void setLoadMore(boolean isLoading) {
        loadMore = isLoading;
        if (list == null) return;
        if (loadMore) {
            notifyItemInserted(list.size());
        } else {
            notifyItemRemoved(list.size());
        }
    }


    @Override
    public BaseViewHolder getViewHolder() {
        return null;
    }

    @Override
    public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        if (VIEW_TYPE_DATA == viewType) {
            return new ViewHolder(inflateLayout(R.layout.item_contest_detail_adapter));
        }
        return new LoadMoreViewHolder(inflateLayout(R.layout.item_load_more_adapter));

    }

    @Override
    public int getViewType(int position) {
        if (list == null) return VIEW_TYPE_DATA;
        if (loadMore && position == list.size()) {
            return VIEW_TYPE_LOAD_MORE;
        }
        return VIEW_TYPE_DATA;
    }


    @Override
    public int getDataCount() {
        return list == null ? 0 : (loadMore ? list.size() + 1 : list.size());
    }

    public int getLastItemBottomMargin() {
        return 0;
    }

    public String getMatchProgress() {
        return "F";
    }

    public UserModel getUserModel() {
        return null;
    }

    public boolean isFixtureMatch() {
        return getMatchProgress().equals("F");
    }

    public boolean isLiveMatch() {
        return getMatchProgress().equals("L") || getMatchProgress().equals("IR");
    }


    public boolean isMatchAboundant() {
        return getMatchProgress().equals("AB");
    }

    public boolean isMatchInReview() {
        return getMatchProgress().equals("IR");
    }

    public boolean isBeatExpertContest(){
        return false;
    }


    private class ViewHolder extends BaseViewHolder {

        CardView cv_data;
        RelativeLayout rl_player_image;
        ImageView iv_player;
        ProgressBar pb_image;
        TextView tv_player_name;
        TextView tv_bet_expert_entry_fee;
        TextView tv_player_points;

        LinearLayout ll_winner_trophy;
        TextView tv_player_rank;
        TextView tv_player_win;

        ImageView iv_rank;
        ImageView iv_cash;
        View view_seprator;


        public ViewHolder(View itemView) {
            super(itemView);
            cv_data = itemView.findViewById(R.id.cv_data);
            rl_player_image = itemView.findViewById(R.id.rl_player_image);
            iv_player = itemView.findViewById(R.id.iv_player);
            pb_image = itemView.findViewById(R.id.pb_image);
            tv_player_name = itemView.findViewById(R.id.tv_player_name);
            tv_bet_expert_entry_fee = itemView.findViewById(R.id.tv_bet_expert_entry_fee);
            tv_player_points = itemView.findViewById(R.id.tv_player_points);
            ll_winner_trophy = itemView.findViewById(R.id.ll_winner_trophy);
            tv_player_rank = itemView.findViewById(R.id.tv_player_rank);
            tv_player_win = itemView.findViewById(R.id.tv_player_win);
            iv_rank = itemView.findViewById(R.id.iv_rank);
            iv_cash = itemView.findViewById(R.id.iv_cash);
            view_seprator = itemView.findViewById(R.id.view_seprator);
        }

        @Override
        public void setData(int position) {
            rl_player_image.setTag(position);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view_seprator.getLayoutParams();
            if (position == (list.size() - 1)) {
                layoutParams.bottomMargin = getLastItemBottomMargin();
            } else {
                layoutParams.bottomMargin = 0;
            }
            view_seprator.setLayoutParams(layoutParams);

//            if (position % 2 == 0) {
//                cv_data.setCardBackgroundColor(context.getResources().getColor(R.color.colorWhite));
//            } else {
//                cv_data.setCardBackgroundColor(context.getResources().getColor(R.color.colorGray50));
//            }

            ContestTeamModel customerTeamModel = list.get(position);
            if (customerTeamModel == null) {
                iv_player.setImageResource(R.drawable.no_image);
                tv_player_name.setText("");
                tv_bet_expert_entry_fee.setText("");
                tv_player_points.setText("");
                tv_player_rank.setText("");
                tv_player_win.setText("");
                iv_rank.setImageResource(R.drawable.red_icon_3x);
                rl_player_image.setOnClickListener(null);
                updateViewVisibitity(ll_winner_trophy, View.GONE);
                updateViewVisibitity(iv_cash, View.GONE);
            } else {
                rl_player_image.setOnClickListener(this);
                ((AppBaseActivity) context).loadImage(context, iv_player, pb_image, customerTeamModel.getImage(),
                        R.drawable.no_image);
                tv_player_name.setText(customerTeamModel.getFullTeamName());
                tv_bet_expert_entry_fee.setText("ENTRY FEE - "+((AppBaseActivity) context).currency_symbol +customerTeamModel.getUserEntryFeesText());
                tv_player_points.setText(customerTeamModel.getTotalPointstext());
                tv_player_rank.setText(customerTeamModel.getNewRankText());
                if (customerTeamModel.getWin_amount() > 0) {
                    tv_player_win.setText(((AppBaseActivity) context).currency_symbol +
                            customerTeamModel.getWinAmountText());
                } else if (customerTeamModel.getRefund_amount() > 0) {
                    tv_player_win.setText(((AppBaseActivity) context).currency_symbol +
                            customerTeamModel.getRefundAmountText());
                } else {
                    tv_player_win.setText(((AppBaseActivity) context).currency_symbol + "0");
                }

                if (isMatchInReview()) {
                    iv_rank.setImageResource(R.drawable.yellow_icon_3x);
                } else {
                if (customerTeamModel.isNewRankUp()) {
                    iv_rank.setImageResource(R.drawable.green_3x);
                } else if (customerTeamModel.isNewRankDown()) {
                    iv_rank.setImageResource(R.drawable.red_icon_3x);
                } else {
                    iv_rank.setImageResource(R.drawable.yellow_icon_3x);
                }
                }


                if (isFixtureMatch()) {
                    updateViewVisibitity(tv_player_points, View.GONE);
                    updateViewVisibitity(tv_player_win, View.GONE);
                    updateViewVisibitity(ll_winner_trophy, View.GONE);
                    updateViewVisibitity(iv_cash, View.GONE);
                    updateViewVisibitity(iv_rank, View.VISIBLE);
                } else if (isLiveMatch()) {
                    updateViewVisibitity(tv_player_points, View.VISIBLE);
                    updateViewVisibitity(tv_player_win, View.GONE);
                    updateViewVisibitity(ll_winner_trophy, View.GONE);
                    updateViewVisibitity(iv_cash, View.GONE);
                    updateViewVisibitity(iv_rank, View.VISIBLE);
                } else {
                    updateViewVisibitity(iv_rank, View.GONE);
                    updateViewVisibitity(tv_player_points, View.VISIBLE);
                    if (customerTeamModel.getWin_amount() > 0) {
                        updateViewVisibitity(tv_player_win, View.VISIBLE);
                        updateViewVisibitity(iv_cash, View.VISIBLE);
                    } else if (customerTeamModel.getRefund_amount() > 0) {
                        updateViewVisibitity(tv_player_win, View.VISIBLE);
                        updateViewVisibitity(iv_cash, View.VISIBLE);
                    } else {
                        updateViewVisibitity(tv_player_win, View.GONE);
                        updateViewVisibitity(iv_cash, View.INVISIBLE);
                    }

                    if (isMatchAboundant()) {
                        updateViewVisibitity(ll_winner_trophy, View.GONE);
                    } else {
                        if (customerTeamModel.getWin_amount() > 0) {
                            if (customerTeamModel.getNew_rank() == 1) {
                                updateViewVisibitity(ll_winner_trophy, View.VISIBLE);
                            } else {
                                updateViewVisibitity(ll_winner_trophy, View.GONE);
                            }
                        } else {
                            updateViewVisibitity(ll_winner_trophy, View.GONE);
                        }
                    }
                }

                UserModel userModel = getUserModel();
                if (userModel != null) {
                    if (customerTeamModel.isMyTeam(userModel.getId())) {
                        cv_data.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                        if(isBeatExpertContest()){
                            updateViewVisibitity(tv_bet_expert_entry_fee,View.VISIBLE);
                        }else{
                            updateViewVisibitity(tv_bet_expert_entry_fee,View.GONE);
                        }

                    } else {
                        cv_data.setCardBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                        updateViewVisibitity(tv_bet_expert_entry_fee,View.GONE);
                    }
                } else {
                    cv_data.setCardBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                }

            }



        }

        @Override
        public void onClick(View v) {
            performItemClick((Integer) v.getTag(), v);
        }
    }
}
