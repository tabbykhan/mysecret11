package com.app.ui.main.dashboard.profile.myaccount.withdrawlhistory.adapter;

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
import com.app.model.WithdrawHistoryModel;
import com.app.ui.MyApplication;
import com.utilities.DeviceScreenUtil;

import java.util.List;

/**
 * Created by Vishnu Gupta on 7/1/19.
 */
public class WithdrawHistoryAdapter extends AppBaseRecycleAdapter implements HeaderDecorationAdapter {
    private final int defaultBottomMargin;
    Context context;
    List<WithdrawHistoryModel> list;

    private boolean loadMore = false;


    public WithdrawHistoryAdapter(Context context, List<WithdrawHistoryModel> list) {
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
            return new ViewHolder(inflateLayout(R.layout.item_withdraw_history_adapter));
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
        return MyApplication.convertToDateOnlyTime(list.get(position).getCreated_at() * 1000);
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
                WithdrawHistoryModel withdrawHistoryModel = list.get(position);
                tv_date.setText(withdrawHistoryModel.getCreatedDateText());
            }

        }
    }

    private class ViewHolder extends BaseViewHolder {

        CardView cv_data;
        TextView tv_amount;
        TextView tv_transaction_status;
        TextView tv_reason;
        ImageView iv_info;

        LinearLayout ll_transaction_detail;
        TextView tv_transaction_id;
        TextView tv_transaction_date;
        TextView tv_transaction_team_name;

        public ViewHolder(View itemView) {
            super(itemView);
            cv_data = itemView.findViewById(R.id.cv_data);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_transaction_status = itemView.findViewById(R.id.tv_transaction_status);
            tv_reason = itemView.findViewById(R.id.tv_reason);
            iv_info = itemView.findViewById(R.id.iv_info);
            ll_transaction_detail = itemView.findViewById(R.id.ll_transaction_detail);
            tv_transaction_id = itemView.findViewById(R.id.tv_transaction_id);
            tv_transaction_date = itemView.findViewById(R.id.tv_transaction_date);
            tv_transaction_team_name = itemView.findViewById(R.id.tv_transaction_team_name);

        }

        @Override
        public void setData(int position) {
            iv_info.setTag(position);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) cv_data.getLayoutParams();
            if (position == (list.size() - 1)) {
                layoutParams.bottomMargin = getLastItemBottomMargin();
            } else {
                layoutParams.bottomMargin = defaultBottomMargin;
            }
            cv_data.setLayoutParams(layoutParams);

            WithdrawHistoryModel withdrawHistoryModel = list.get(position);
            if (withdrawHistoryModel == null) {
                tv_amount.setText("");
                tv_transaction_status.setText("");
                tv_transaction_status.setTextColor(context.getResources().getColor(R.color.colorWhite));
                updateViewVisibitity(ll_transaction_detail, View.GONE);
                updateViewVisibitity(tv_reason, View.GONE);
                iv_info.setOnClickListener(null);
            } else {
                iv_info.setOnClickListener(this);
                tv_reason.setText(withdrawHistoryModel.getReason());
                tv_amount.setText(((AppBaseActivity) context).currency_symbol +
                        withdrawHistoryModel.getAmountText());
                tv_transaction_status.setText(withdrawHistoryModel.getStatusText());
                if (withdrawHistoryModel.isPending()) {
                    updateViewVisibitity(tv_reason, View.GONE);
                    tv_transaction_status.setTextColor(context.getResources().getColor(R.color.colorOrange));
                } else if (withdrawHistoryModel.isRejected()) {
                    updateViewVisibitity(tv_reason, View.VISIBLE);
                    tv_transaction_status.setTextColor(context.getResources().getColor(R.color.colorRed));
                } else if (withdrawHistoryModel.isCompleted()) {
                    updateViewVisibitity(tv_reason, View.GONE);
                    tv_transaction_status.setTextColor(context.getResources().getColor(R.color.colorActivateGreen));
                } else {
                    updateViewVisibitity(tv_reason, View.GONE);
                    tv_transaction_status.setTextColor(context.getResources().getColor(R.color.colorWhite));
                }

                tv_transaction_id.setText(withdrawHistoryModel.getTransaction_id());
                tv_transaction_date.setText(withdrawHistoryModel.getCreatedText());
                tv_transaction_team_name.setText("");
                if (withdrawHistoryModel.isOpened()) {
                    updateViewVisibitity(ll_transaction_detail, View.VISIBLE);
                    iv_info.setImageResource(R.drawable.uparrow_3x);
                } else {
                    updateViewVisibitity(ll_transaction_detail, View.GONE);
                    iv_info.setImageResource(R.drawable.info_3x);
                }
            }


        }

        @Override
        public void onClick(View v) {
            performItemClick((Integer) v.getTag(), v);
        }
    }
}
