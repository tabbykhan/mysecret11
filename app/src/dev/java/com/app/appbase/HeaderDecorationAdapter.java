package com.app.appbase;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public interface HeaderDecorationAdapter<VH extends RecyclerView.ViewHolder> {


    long getHeaderId(int position);


    VH onCreateHeaderViewHolder(ViewGroup parent);


    void onBindHeaderViewHolder(VH holder, int position);


    int getItemCount();
}
