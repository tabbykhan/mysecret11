package com.MLM;

import android.content.Intent;
import android.view.View;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.ui.main.ToolbarFragment;

public class MLMDashboard  extends AppBaseActivity {

    ToolbarFragment toolbarFragment;


    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_m_l_m_dashboard;
    }

    public void teamView(View view) {
        startActivity(new Intent(this , TeamView.class));
    }


    @Override
    public void initializeComponent() {
        super.initializeComponent();
       /* toolbarFragment = findViewById(R.id.topBar);
        Fragment toolbar = getA().findFragmentById(R.id.topBar);
        if (toolbar != null && toolbar instanceof AppBaseFragment) {
            toolbarFragment = (ToolbarFragment) toolbar;
            toolbarFragment.initializeComponent();
            toolbarFragment.setToolbarView(this);
        }*/

    }

    public void treeView(View view) {
        startActivity(new Intent(this , TeamView.class));
    }

    public void makePayment(View view) {
        startActivity(new Intent(this , MakePayment.class));
    }

    public void backBtn(View view) {
        onBackPressed();
    }
}
