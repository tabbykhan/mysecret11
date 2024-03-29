package com.app.ui.dialogs.selection;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.app.appbase.AppBaseDialogFragment;
import com.base.BaseFragment;
import com.customviews.TypefaceTextView;
import com.utilities.ItemClickSupport;

import java.util.List;

/**
 * Created by ubuntu on 27/3/18.
 */

public class DataDialog<T> extends AppBaseDialogFragment {

    private static final String TAG = DataDialog.class.getSimpleName();
    private RecyclerView recycler_view;
    private TypefaceTextView tv_title;

    private String title;


    private List<T> dataList;
    private OnItemSelectedListener onItemSelectedListeners;

    @Override
    public int getLayoutResourceId() {
        return R.layout.dialog_data;
    }


    @Override
    public void initializeComponent() {
        recycler_view = getView().findViewById(R.id.recycler_view);
        tv_title = getView().findViewById(R.id.tv_title);
        if (title != null)
            tv_title.setText(title);
        if (dataList != null && dataList.size() > 0)
            initializeRecyclerView();
    }

    @Override
    public int getFragmentContainerResourceId(BaseFragment baseFragment) {
        return -1;
    }


    private void initializeRecyclerView() {
        recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler_view.setAdapter(new DataAdapter(dataList));
        ItemClickSupport.addTo(recycler_view).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                onItemSelectedListeners.onItemSelectedListener(position);
            }
        });
    }

    public OnItemSelectedListener getOnItemSelectedListeners() {
        return onItemSelectedListeners;
    }

    public void setOnItemSelectedListeners(OnItemSelectedListener onItemSelectedListeners) {
        this.onItemSelectedListeners = onItemSelectedListeners;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LayoutInflater inflate = LayoutInflater.from(getActivity());
        View layout = inflate.inflate(getLayoutResourceId(), null);

        dialog.setContentView(layout);
        WindowManager.LayoutParams wlmp = dialog.getWindow().getAttributes();
        wlmp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlmp.width = WindowManager.LayoutParams.MATCH_PARENT;


        dialog.setTitle(null);
        setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public interface OnItemSelectedListener {
        void onItemSelectedListener(int position);
    }
}
