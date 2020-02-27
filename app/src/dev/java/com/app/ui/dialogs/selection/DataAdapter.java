package com.app.ui.dialogs.selection;

import android.view.View;
import android.widget.TextView;

import com.R;
import com.app.appbase.AppBaseRecycleAdapter;

import java.util.List;

/**
 * Created by ubuntu on 27/3/18.
 */

public class DataAdapter<T> extends AppBaseRecycleAdapter {

    private static final String TAG = DataAdapter.class.getSimpleName();
    private List<T> dataList;

    public DataAdapter(List<T> data) {
        isForDesign = false;
        this.dataList = data;
    }

    @Override
    public BaseViewHolder getViewHolder() {
        return new ViewHolder(inflateLayout(R.layout.item_data_adapter));
    }

    @Override
    public int getDataCount() {
        return dataList == null ? 0 : dataList.size();
    }


    private class ViewHolder extends BaseViewHolder {
        private TextView tv_item;
        private View ll_view;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_item = itemView.findViewById(R.id.tv_item);
            ll_view = itemView.findViewById(R.id.ll_view);

        }

        @Override
        public void setData(int position) {
            if (dataList == null) return;
            String s = dataList.get(position).toString();
            tv_item.setText(s);

        }

    }
}