package com.app.ui.main.cricket.dialogs.recommended.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseRecycleAdapter;
import com.app.model.ContestWinnerBreakUpModel;
import com.app.model.webresponsemodel.PrivateContestWinningBreakupResponseModel;

public class WinnersRecommeendesInnerAdapter extends AppBaseRecycleAdapter {

    Context context;
    PrivateContestWinningBreakupResponseModel.WinnerBreakUpData contestWinnerBreakUpModel;

    public WinnersRecommeendesInnerAdapter(Context context, PrivateContestWinningBreakupResponseModel.WinnerBreakUpData contestWinnerBreakUpModel) {
        this.context = context;
        this.contestWinnerBreakUpModel = contestWinnerBreakUpModel;
    }


    @Override
    public BaseViewHolder getViewHolder() {
        return new ViewHolder(inflateLayout(R.layout.item_winner_recommended_adapter));
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

            ContestWinnerBreakUpModel contest_json = contestWinnerBreakUpModel.getContest_json();

            String minRank = contest_json.getPer_min_p().get(position);
            String maxRank = contest_json.getPer_max_p().get(position);
            String rankPrice = contest_json.getPer_price().get(position);
            String rankPercent = contest_json.getPer_percent().get(position);
            if (minRank.trim().equals(maxRank.trim())) {
                tv_rank.setText("Rank: " + minRank);
            } else {
                tv_rank.setText("Rank: " + minRank + " - " + maxRank);
            }

            tv_rank_percent.setText(rankPercent+"%");
            tv_rank_price.setText(((AppBaseActivity) context).currency_symbol + rankPrice);

        }

    }
}
