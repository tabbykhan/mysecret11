package com.app.ui.main.dashboard.more.invite.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseRecycleAdapter;
import com.app.model.UserModel;
import com.utilities.DeviceScreenUtil;

import java.util.List;


/**
 * Created by Vishnu Gupta on 7/1/19.
 */
public class InviteDetailAdapter extends AppBaseRecycleAdapter {

    Context context;
    List<UserModel> list;

    public InviteDetailAdapter(Context context, List<UserModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public BaseViewHolder getViewHolder() {
        return new ViewHolder(inflateLayout(R.layout.item_invite_detail_adapter));
    }

    public int getLastItemBottomMargin() {
        return 0;
    }

    @Override
    public int getDataCount() {
        return list == null ? 0 : list.size();
    }

    private class ViewHolder extends BaseViewHolder {

        CardView cv_data;
        ImageView iv_user;
        TextView tv_user_name;
        ProgressBar pb_earn;
        TextView tv_received_earn;
        TextView tv_total_earn;


        public ViewHolder(View itemView) {
            super(itemView);
            cv_data = itemView.findViewById(R.id.cv_data);
            iv_user = itemView.findViewById(R.id.iv_user);
            tv_user_name = itemView.findViewById(R.id.tv_user_name);
            pb_earn = itemView.findViewById(R.id.pb_earn);
            tv_received_earn = itemView.findViewById(R.id.tv_received_earn);
            tv_total_earn = itemView.findViewById(R.id.tv_total_earn);
        }

        @Override
        public void setData(int position) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) cv_data.getLayoutParams();
            if (position == (list.size() - 1)) {
                layoutParams.bottomMargin = getLastItemBottomMargin();
            } else {
                layoutParams.bottomMargin = DeviceScreenUtil.getInstance().convertDpToPixel(2.0f);
            }
            cv_data.setLayoutParams(layoutParams);


            UserModel userModel = list.get(position);
            if (userModel == null) {
                iv_user.setImageResource(R.drawable.no_image);
                tv_user_name.setText("");
                pb_earn.setMax(100);
                pb_earn.setProgress(0);
                tv_received_earn.setText("");
                tv_total_earn.setText("");
            } else {
                tv_user_name.setText(userModel.getTeam_name());
                pb_earn.setMax(Math.round(userModel.getUsed_refferal_amount()));
                pb_earn.setProgress(Math.round(userModel.getReceived_referral_amount()));
                tv_received_earn.setText(((AppBaseActivity) context).currency_symbol + userModel.getReceivedReferralAmountText());
                tv_total_earn.setText(((AppBaseActivity) context).currency_symbol + userModel.getUserReferralAmountText());

                ((AppBaseActivity) context).loadImage(context, iv_user, null, userModel.getImage(),
                        R.drawable.no_image);
            }

        }
        
    }
}
