package com.app.ui.main.notifications.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseRecycleAdapter;
import com.app.appbase.HeaderDecorationAdapter;
import com.app.model.NotificationModel;
import com.app.ui.MyApplication;
import com.customviews.ReadMoreTextView;
import com.utilities.DeviceScreenUtil;

import java.util.List;

/**
 * Created by Vishnu Gupta on 7/1/19.
 */
public class NotificationsAdapter extends AppBaseRecycleAdapter implements HeaderDecorationAdapter {
    Context context;
    List<NotificationModel> list;

    private int imageHeight;
    private int defaultBottomMargin;
    private boolean loadMore = false;

    public NotificationsAdapter(Context context, List<NotificationModel> list) {
        this.context = context;
        this.list = list;
        int width = DeviceScreenUtil.getInstance().getWidth() - DeviceScreenUtil.getInstance().convertDpToPixel(20.0f);
        imageHeight = Math.round(width * 0.416f);
        defaultBottomMargin = DeviceScreenUtil.getInstance().convertDpToPixel(3.0f);
    }

    @Override
    public BaseViewHolder getViewHolder() {
        return new ViewHolder(inflateLayout(R.layout.item_notifications_adapter));
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
    public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        if (VIEW_TYPE_DATA == viewType) {
            return new ViewHolder(inflateLayout(R.layout.item_notifications_adapter));
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
                NotificationModel transactionHistoryModel = list.get(position);
                tv_date.setText(transactionHistoryModel.getCreatedDateText());
            }
        }
    }

    private class ViewHolder extends BaseViewHolder implements ReadMoreTextView.ReadMoreTextViewListener {

        private CardView cv_data;
        private TextView tv_title;
        private ReadMoreTextView tv_message;
        private TextView tv_date;
        private ImageView iv_image;
        private RelativeLayout rl_image;
        private ProgressBar pb_image;

        public ViewHolder(View itemView) {
            super(itemView);
            cv_data = itemView.findViewById(R.id.cv_data);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_message = itemView.findViewById(R.id.tv_message);
            tv_date = itemView.findViewById(R.id.tv_date);
            iv_image = itemView.findViewById(R.id.iv_image);
            rl_image = itemView.findViewById(R.id.rl_image);
            pb_image = itemView.findViewById(R.id.pb_image);


            RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) iv_image.getLayoutParams();
            layoutParams1.height = imageHeight;
            iv_image.setLayoutParams(layoutParams1);

        }

        @Override
        public void setData(int position) {
            if (list == null) return;

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) cv_data.getLayoutParams();
            if (position == (list.size() - 1)) {
                layoutParams.bottomMargin = getLastItemBottomMargin();
            } else {
                layoutParams.bottomMargin = defaultBottomMargin;
            }
            cv_data.setLayoutParams(layoutParams);


            tv_message.setTag(position);
            tv_message.setReadMoreTextViewListener(this);
            NotificationModel notificationModel = list.get(position);
            tv_title.setText(notificationModel.getTitle());
            tv_message.readMore = notificationModel.isReadMore();
            tv_message.setText(notificationModel.getNotification(), TextView.BufferType.NORMAL);
            tv_date.setText(notificationModel.getCreatedTimeText());

            if (isValidString(notificationModel.getImage_large())) {
                updateViewVisibitity(rl_image, View.VISIBLE);
                ((AppBaseActivity) context).loadImage(context, iv_image, pb_image,
                        notificationModel.getImage_large(),
                        R.drawable.logo_3x, -1);
            } else {
                updateViewVisibitity(rl_image, View.GONE);
            }
            if (notificationModel.isSendByAdmin()) {
                updateViewVisibitity(tv_title, View.VISIBLE);
            } else {
                updateViewVisibitity(tv_title, View.GONE);
            }

        }

        @Override
        public void onReadMoreChange(ReadMoreTextView textView) {
            int position = Integer.parseInt(textView.getTag().toString());
            list.get(position).setReadMore(textView.readMore);
            if (textView.readMore && getRecyclerView() != null) {
                getRecyclerView().scrollToPosition(position);
            }
        }


    }
}
