package com.app.ui.main.cricket.contest.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseRecycleAdapter;
import com.app.model.ContestCategoryModel;
import com.utilities.ItemClickSupport;

import java.util.List;

/**
 * Created by Vishnu Gupta on 7/1/19.
 */
public class ContestCategoryAdapter extends AppBaseRecycleAdapter implements Filterable {
    Context context;
    List<ContestCategoryModel> list;

    public ContestCategoryAdapter(Context context, List<ContestCategoryModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public BaseViewHolder getViewHolder() {
        return new ViewHolder(inflateLayout(R.layout.item_constest_category_adapter));
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    @Override
    public int getDataCount() {
        return list == null ? 0 : list.size();
    }

    public int getLastItemBottomMargin() {
        return 0;
    }

    public boolean isViewMoreEnable(long catId) {
        return false;
    }

    private class ViewHolder extends BaseViewHolder {

        CardView cv_data;
        ImageView iv_contest_cat;
        ImageView iv_discount_image;
        ProgressBar pb_iv_contest_cat;
        TextView tv_contest_cat_name;
        TextView tv_contest_cat_des;
        TextView tv_view_more;
        RecyclerView recycler_view;
        ContestAdapter contestAdapter;

        public ViewHolder(View itemView) {
            super(itemView);
            cv_data = itemView.findViewById(R.id.cv_data);
            pb_iv_contest_cat = itemView.findViewById(R.id.pb_iv_contest_cat);
            iv_discount_image = itemView.findViewById(R.id.iv_discount_image);
            updateViewVisibitity(pb_iv_contest_cat, View.GONE);
            iv_contest_cat = itemView.findViewById(R.id.iv_contest_cat);
            tv_contest_cat_name = itemView.findViewById(R.id.tv_contest_cat_name);
            tv_contest_cat_des = itemView.findViewById(R.id.tv_contest_cat_des);
            tv_view_more = itemView.findViewById(R.id.tv_view_more);
            recycler_view = itemView.findViewById(R.id.recycler_view);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recycler_view.setLayoutManager(layoutManager);
            recycler_view.setNestedScrollingEnabled(false);
            //recycler_view.setNestedScrollingEnabled(true);
        }

        @Override
        public void setData(int position) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) cv_data.getLayoutParams();
            /*if (position == (list.size() - 1)) {
                layoutParams.bottomMargin = getLastItemBottomMargin();
            } else {
                layoutParams.bottomMargin = 0;
            }*/
            cv_data.setLayoutParams(layoutParams);


            tv_view_more.setTag(position);
            recycler_view.setTag(position);
            final ContestCategoryModel contestCategoryModel = list.get(position);
            if (contestCategoryModel != null) {
                tv_view_more.setOnClickListener(this);
                tv_contest_cat_name.setText(contestCategoryModel.getName());
                tv_contest_cat_des.setText(contestCategoryModel.getDescription());
                ((AppBaseActivity) context).loadImage(context, iv_contest_cat, pb_iv_contest_cat,
                        contestCategoryModel.getImage(),
                        R.mipmap.ic_launcher_round);

                if(contestCategoryModel.isDiscounted()){
                    int[] discountImageSizeForCategory = contestCategoryModel.getDiscountImageSizeForCategory();
                    LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) iv_discount_image.getLayoutParams();
                    layoutParams1.width=discountImageSizeForCategory[0];
                    layoutParams1.height=discountImageSizeForCategory[1];
                    iv_discount_image.setLayoutParams(layoutParams1);

                    ((AppBaseActivity) context).loadImage(context, iv_discount_image, null,
                            contestCategoryModel.getDiscount_image(),
                            R.drawable.logo);
                    updateViewVisibitity(iv_discount_image,View.VISIBLE);
                }else{
                    updateViewVisibitity(iv_discount_image,View.GONE);
                }

                contestAdapter = new ContestAdapter(context, contestCategoryModel.getContests(),
                        isViewMoreEnable(contestCategoryModel.getId())){
                    @Override
                    public boolean isDiscountedCategory() {
                        return contestCategoryModel.isDiscounted();
                    }
                };
                recycler_view.setAdapter(contestAdapter);
                ItemClickSupport.addTo(recycler_view).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        performChildItemClick((Integer) recycler_view.getTag(), position, v);
                    }
                });
                if (contestCategoryModel.getContests().size() > 3) {
                    updateViewVisibitity(tv_view_more, View.VISIBLE);
                    if (isViewMoreEnable(contestCategoryModel.getId())) {
                        tv_view_more.setText("< View less");
                    } else {
                        tv_view_more.setText("View " + (contestCategoryModel.getContests().size() - 3) + " more >");
                    }
                } else {
                    updateViewVisibitity(tv_view_more, View.GONE);
                }


            } else {
                tv_view_more.setOnClickListener(null);
                updateViewVisibitity(tv_view_more, View.GONE);
                iv_contest_cat.setImageResource(R.mipmap.ic_launcher_round);
                tv_contest_cat_name.setText("");
                tv_contest_cat_des.setText("");
                recycler_view.setAdapter(null);
            }
        }

        @Override
        public void onClick(View v) {
            performItemClick((Integer) v.getTag(), v);
        }
    }


}
