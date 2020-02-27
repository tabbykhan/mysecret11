package com.app.ui.main.dashboard.profile.editavatar.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.cardview.widget.CardView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseRecycleAdapter;

import java.util.List;

/**
 * Created by Vishnu Gupta on 7/1/19.
 */
public class AvatarAdapter extends AppBaseRecycleAdapter {
    Context context;
    List<String> list;

    String selectedItem = "";

    public AvatarAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public BaseViewHolder getViewHolder() {
        return new ViewHolder(inflateLayout(R.layout.item_edit_avatar_adapter));
    }

    @Override
    public int getDataCount() {
        return list == null ? 0 : list.size();
    }

    public int getItemSize() {
        return 0;
    }

    public String getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(int position) {
        int previousPosition = -1;
        if (selectedItem != null) {
            previousPosition = list.indexOf(selectedItem);
        }
        selectedItem = list.get(position);
        if (previousPosition > -1) {
            notifyItemChanged(previousPosition);
        }
        notifyItemChanged(position);
    }

    private class ViewHolder extends BaseViewHolder {

        CardView cv_data;
        RelativeLayout rl_avatar;
        ImageView iv_player;
        ImageView iv_player_selected;
        ProgressBar pb_image;


        public ViewHolder(View itemView) {
            super(itemView);
            cv_data = itemView.findViewById(R.id.cv_data);
            rl_avatar = itemView.findViewById(R.id.rl_avatar);
            iv_player = itemView.findViewById(R.id.iv_player);
            iv_player_selected = itemView.findViewById(R.id.iv_player_selected);
            pb_image = itemView.findViewById(R.id.pb_image);

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) cv_data.getLayoutParams();
            layoutParams.width = getItemSize();
            layoutParams.height = getItemSize();
            cv_data.setLayoutParams(layoutParams);
        }

        @Override
        public void setData(int position) {
            String s = list.get(position);
            if (s == null) {
                updateViewVisibitity(iv_player_selected, View.GONE);
                updateViewVisibitity(pb_image, View.GONE);
                iv_player.setImageResource(R.drawable.no_image);
                rl_avatar.setSelected(false);
            } else {
                if (s.equals(selectedItem)) {
                    rl_avatar.setSelected(true);
                    updateViewVisibitity(iv_player_selected, View.VISIBLE);
                } else {
                    rl_avatar.setSelected(false);
                    updateViewVisibitity(iv_player_selected, View.GONE);
                }

                ((AppBaseActivity) context).loadImage(context, iv_player, pb_image, s,
                        R.drawable.no_image);
            }


        }

    }
}
