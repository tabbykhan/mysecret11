package com.app.ui.dialogs.playerstatsdialog.adapter;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.R;
import com.app.appbase.AppBaseRecycleAdapter;
import com.app.model.PlayerEventModel;

import java.util.List;

/**
 * Created by Vishnu Gupta on 7/1/19.
 */
public class PlayerEventsAdapter extends AppBaseRecycleAdapter {

    Context context;
    List<PlayerEventModel> list;

    public PlayerEventsAdapter(Context context, List<PlayerEventModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public BaseViewHolder getViewHolder() {
        return new ViewHolder(inflateLayout(R.layout.item_players_event_adapter));
    }

    public int getLastItemBottomMargin() {
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

    private class ViewHolder extends BaseViewHolder {

        TextView tv_event_name;
        TextView tv_event_value;
        TextView tv_event_point;
        View view_seprator;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_event_name = itemView.findViewById(R.id.tv_event_name);
            tv_event_value = itemView.findViewById(R.id.tv_event_value);
            tv_event_point = itemView.findViewById(R.id.tv_event_point);
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

            PlayerEventModel playerEventModel = list.get(position);
            if (playerEventModel == null) {
                tv_event_name.setText("");
                tv_event_value.setText("");
                tv_event_point.setText("");
            } else {
                tv_event_name.setText(playerEventModel.getKey());
                tv_event_value.setText(playerEventModel.getValue());
                tv_event_point.setText(playerEventModel.getPointsText());
            }

        }

    }
}
