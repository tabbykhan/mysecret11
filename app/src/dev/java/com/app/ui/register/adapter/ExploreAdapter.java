package com.app.ui.register.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.R;
import com.app.appbase.AppBaseRecycleAdapter;

import java.util.ArrayList;
import java.util.List;

public class ExploreAdapter extends AppBaseRecycleAdapter {

    private Context context;
    private List<String> list;

    public ExploreAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    public void cleanSearch() {
        list.clear();
        notifyDataSetChanged();
    }

    public void updatelist(List<String> data) {
        list.clear();
        list.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public BaseViewHolder getViewHolder() {
        return new ViewHolder(inflateLayout(R.layout.adapter_team_name));
    }

    @Override
    public int getDataCount() {
        return list == null ? 0 : list.size();
    }

    public String getItem(int position) {
        return list.get(position);
    }

    private class ViewHolder extends BaseViewHolder {

        private TextView tv_team_name;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_team_name = itemView.findViewById(R.id.tv_team_name);

        }

        @Override
        public void setData(int position) {
            if (list == null) return;
            tv_team_name.setText(list.get(position));




        }
    }
}
