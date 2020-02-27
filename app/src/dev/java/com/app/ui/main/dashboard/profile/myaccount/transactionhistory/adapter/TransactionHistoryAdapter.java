package com.app.ui.main.dashboard.profile.myaccount.transactionhistory.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseRecycleAdapter;
import com.app.appbase.HeaderDecorationAdapter;
import com.app.model.TransactionHistoryModel;
import com.app.ui.MyApplication;
import com.utilities.DeviceScreenUtil;

import java.util.List;

/**
 * Created by Vishnu Gupta on 7/1/19.
 */
public class TransactionHistoryAdapter extends AppBaseRecycleAdapter implements HeaderDecorationAdapter {
    Context context;
    List<TransactionHistoryModel> list;

    int defaultBottomMargin;

    private boolean loadMore = false;

    public TransactionHistoryAdapter(Context context, List<TransactionHistoryModel> list) {
        this.context = context;
        this.list = list;
        defaultBottomMargin = DeviceScreenUtil.getInstance().convertDpToPixel(5.0f);
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
            return new ViewHolder(inflateLayout(R.layout.item_transaction_history_adapter));
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

    @Override
    public long getHeaderId(int position) {
        if (loadMore && position == list.size()) {
            return -1;
        }
        return MyApplication.convertToDateOnlyTime(list.get(position).getCreated() * 1000);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new ViewHolderHeader(inflateLayout(R.layout.item_transaction_header_adapter));

    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((ViewHolderHeader) holder).setData(position);
    }

    private class ViewHolderHeader extends BaseViewHolder {

        TextView tv_date;

        public ViewHolderHeader(View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
        }

        @Override
        public void setData(int position) {
            if (loadMore && position == list.size()) {
                tv_date.setText("");
            } else {
                TransactionHistoryModel transactionHistoryModel = list.get(position);
                tv_date.setText(transactionHistoryModel.getCreatedDateText());
            }

        }
    }

    private class ViewHolder extends BaseViewHolder {

        CardView cv_data;
        TextView tv_amount;
        TextView tv_transaction_type;
        TextView tv_info;
        ImageView iv_info;

        LinearLayout ll_transaction_detail;
        TextView tv_transaction_id;
        TextView tv_transaction_date;
        TextView tv_transaction_team_description;
        TextView tv_transaction_team_name;

        public ViewHolder(View itemView) {
            super(itemView);
            cv_data = itemView.findViewById(R.id.cv_data);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_transaction_type = itemView.findViewById(R.id.tv_transaction_type);
            iv_info = itemView.findViewById(R.id.iv_info);
            tv_info = itemView.findViewById(R.id.tv_info);
            ll_transaction_detail = itemView.findViewById(R.id.ll_transaction_detail);
            tv_transaction_id = itemView.findViewById(R.id.tv_transaction_id);
            tv_transaction_date = itemView.findViewById(R.id.tv_transaction_date);
            tv_transaction_team_name = itemView.findViewById(R.id.tv_transaction_team_name);
            tv_transaction_team_description = itemView.findViewById(R.id.tv_transaction_team_description);

        }

        @Override
        public void setData(int position) {
            iv_info.setTag(position);
            tv_info.setTag(position);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) cv_data.getLayoutParams();
            if (position == (list.size() - 1)) {
                layoutParams.bottomMargin = defaultBottomMargin;
            } else {
                layoutParams.bottomMargin = defaultBottomMargin;
            }
            cv_data.setLayoutParams(layoutParams);

            TransactionHistoryModel transactionHistoryModel = list.get(position);
            if (transactionHistoryModel == null) {
                tv_amount.setText("");
                tv_transaction_type.setText("");
                tv_transaction_team_description.setText("");
                updateViewVisibitity(ll_transaction_detail, View.GONE);
                iv_info.setOnClickListener(null);
                tv_info.setOnClickListener(null);
            } else {
                iv_info.setOnClickListener(this);
                tv_info.setOnClickListener(this);
                String suffix = "";
                if (transactionHistoryModel.isCredit()) {
                    suffix = "+ ";
                } else {
                    suffix = "- ";
                }
                tv_amount.setText(suffix + ((AppBaseActivity) context).currency_symbol +
                        transactionHistoryModel.getAmountText());
                tv_transaction_type.setText(transactionHistoryModel.getTypeText());

                tv_transaction_id.setText(transactionHistoryModel.getTransaction_id());
                tv_transaction_date.setText(transactionHistoryModel.getCreatedText());
                tv_transaction_team_name.setText("");
                tv_transaction_team_description.setText("Description : "+transactionHistoryModel.getDescription());
                if (transactionHistoryModel.isOpened()) {
                    updateViewVisibitity(ll_transaction_detail, View.VISIBLE);
                    iv_info.setImageResource(R.drawable.uparrow_3x);
                } else {
                    updateViewVisibitity(ll_transaction_detail, View.GONE);
                    iv_info.setImageResource(R.drawable.arrow_down2_3x);
                }
            }


        }

        @Override
        public void onClick(View v) {
            performItemClick((Integer) v.getTag(), v);
        }
    }
}
