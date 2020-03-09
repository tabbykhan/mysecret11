package com.MLM;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.MLM.Adapter.LeadBoardAdapter;
import com.R;

public class SeriesLeaderBoard extends Activity {

    RecyclerView recycler_view;
    LeadBoardAdapter leadBoardAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.series_leader_board);
        initUI();
    }

    private void initUI() {
        recycler_view = findViewById(R.id.recycler_view);
        leadBoardAdapter = new LeadBoardAdapter();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(leadBoardAdapter);
    }
    public void backBtn(View view) {
        onBackPressed();
    }
}
