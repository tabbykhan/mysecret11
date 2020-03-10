package com.MLM.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.R;

public class MakePaymentAdapter extends RecyclerView.Adapter<MakePaymentAdapter.MakePaymentHolder> {
    @NonNull
    @Override
    public MakePaymentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.make_payment_list_row, parent, false);
        return new MakePaymentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MakePaymentHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class MakePaymentHolder extends RecyclerView.ViewHolder {
        public MakePaymentHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
