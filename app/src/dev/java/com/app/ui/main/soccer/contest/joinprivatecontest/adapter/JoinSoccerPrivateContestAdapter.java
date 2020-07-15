package com.app.ui.main.soccer.contest.joinprivatecontest.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseRecycleAdapter;
import com.app.model.ContestWinnerBreakUpModel;

public class JoinSoccerPrivateContestAdapter extends AppBaseRecycleAdapter {

    Context context;
    ContestWinnerBreakUpModel contestWinnerBreakUpModel;

    public JoinSoccerPrivateContestAdapter(Context context, ContestWinnerBreakUpModel contestWinnerBreakUpModel) {
        this.context = context;
        this.contestWinnerBreakUpModel = contestWinnerBreakUpModel;
    }


    @Override
    public BaseViewHolder getViewHolder() {
        return new ViewHolder(inflateLayout(R.layout.item_private_winner_breakup_adapter));
    }

    @Override
    public int getDataCount() {
        return contestWinnerBreakUpModel == null ? 0 : contestWinnerBreakUpModel.size();
    }

    public View getView(int position) {
        ViewHolder viewHolder = (ViewHolder) getRecyclerView().findViewHolderForAdapterPosition(position);
        if (viewHolder != null) {
            return viewHolder.itemView;
        }
        return null;

    }

    private class ViewHolder extends BaseViewHolder {

        TextView tv_rank;
        TextView tv_rank_percent;
        TextView tv_rank_price;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_rank = itemView.findViewById(R.id.tv_rank);
            tv_rank_percent = itemView.findViewById(R.id.tv_rank_percent);
            tv_rank_price = itemView.findViewById(R.id.tv_rank_price);

        }

        @Override
        public void setData(int position) {


            String minRank = contestWinnerBreakUpModel.getPer_min_p().get(position);
            String maxRank = contestWinnerBreakUpModel.getPer_max_p().get(position);
            String rankPrice = contestWinnerBreakUpModel.getPer_price().get(position);
            if(contestWinnerBreakUpModel.getPer_percent() != null) {
                String rankPercent = contestWinnerBreakUpModel.getPer_percent().get(position);
                tv_rank_percent.setText(rankPercent+"%");
            }
            if (minRank.trim().equals(maxRank.trim())) {
                tv_rank.setText("Rank: " + minRank);
            } else {
                tv_rank.setText("Rank: " + minRank + " - " + maxRank);
            }


            tv_rank_price.setText(((AppBaseActivity) context).currency_symbol + rankPrice);

        }

    }

}
