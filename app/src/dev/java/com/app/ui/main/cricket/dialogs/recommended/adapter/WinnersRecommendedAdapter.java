package com.app.ui.main.cricket.dialogs.recommended.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.app.appbase.AppBaseRecycleAdapter;
import com.app.model.webresponsemodel.PrivateContestWinningBreakupResponseModel;
import com.utilities.ItemClickSupport;

import java.util.List;


public class WinnersRecommendedAdapter extends AppBaseRecycleAdapter {

    Context context;
    List<PrivateContestWinningBreakupResponseModel.WinnerBreakUpData> list;


    public WinnersRecommendedAdapter(Context context, List<PrivateContestWinningBreakupResponseModel.WinnerBreakUpData> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public BaseViewHolder getViewHolder() {
        return new ViewHolder(inflateLayout(R.layout.item_winner_recommended));
    }

    public int getLastItemBottomMargin() {
        return 0;
    }

    public int getPreviousSelectedPosition(){
        return 0;
    }


    @Override
    public int getDataCount() {
        return list == null ? 0 : list.size();
    }

    public View getView(int position) {
        ViewHolder viewHolder = (ViewHolder) getRecyclerView().findViewHolderForAdapterPosition(position);
        if (viewHolder != null) {
            return viewHolder.itemView;
        }
        return null;

    }

    public PrivateContestWinningBreakupResponseModel.WinnerBreakUpData getItem(int position){
        if(position<getDataCount()){
            return list.get(position);
        }

        return null;
    }



    private class ViewHolder extends BaseViewHolder {

        TextView tv_total_winner;
        CheckBox cb_team_name;
        RecyclerView recycler_view_inner;
        WinnersRecommeendesInnerAdapter innerAdapter;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_total_winner = itemView.findViewById(R.id.tv_total_winner);
            cb_team_name = itemView.findViewById(R.id.cb_team_name);
            recycler_view_inner = itemView.findViewById(R.id.recycler_view_inner);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recycler_view_inner.setLayoutManager(layoutManager);


        }

        @Override
        public void setData(int position) {

            cb_team_name.setTag(position);
            cb_team_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    performItemClick((Integer)v.getTag(),v);
                }
            });
            recycler_view_inner.setTag(position);
            ItemClickSupport.addTo(recycler_view_inner).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    performItemClick((Integer)recyclerView.getTag(),v);
                }
            });
            PrivateContestWinningBreakupResponseModel.WinnerBreakUpData item = getItem(position);
            if(item==null){
                tv_total_winner.setText("");
                cb_team_name.setChecked(false);
                innerAdapter = new WinnersRecommeendesInnerAdapter(context,item);
                recycler_view_inner.setAdapter(innerAdapter);
            }else{
                innerAdapter = new WinnersRecommeendesInnerAdapter(context, item);
                recycler_view_inner.setAdapter(innerAdapter);

                tv_total_winner.setText(item.getTotal_winners()+" Winners");

                if(getPreviousSelectedPosition()==position){
                    cb_team_name.setChecked(true);
                }else{
                    cb_team_name.setChecked(false);
                }
            }
        }
    }


}
