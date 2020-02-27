package com.app.ui.main.dashboard.more.contestinvite;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.model.GameModel;
import com.app.ui.MyApplication;
import com.app.ui.main.ToolbarFragment;

public class ContestInviteActivity extends AppBaseActivity {

    ToolbarFragment toolbarFragment;


    EditText et_invite_code;
    TextView tv_join_contest;


    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_contest_invite;
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();
        Fragment toolbar = getFm().findFragmentById(R.id.fragment_toolbar);
        if (toolbar != null && toolbar instanceof AppBaseFragment) {
            toolbarFragment = (ToolbarFragment) toolbar;
            toolbarFragment.initializeComponent();
            toolbarFragment.setToolbarView(this);
        }

        et_invite_code = findViewById(R.id.et_invite_code);
        tv_join_contest = findViewById(R.id.tv_join_contest);

        tv_join_contest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_join_contest:
                gotoJoinContest();
                break;
        }
    }

    private void gotoJoinContest() {
        String invite_code = et_invite_code.getText().toString().trim();
        if (invite_code.isEmpty()) {
            showErrorMsg("Please enter invite code.");
            return;
        }
        MyApplication.getInstance().setSelectedMatch(null);

        Bundle bundle = new Bundle();
        bundle.putString(PRIVATE_SLUG, invite_code);

        GameModel current_page = MyApplication.getInstance().getCurrentGame();
        if (current_page.isCricket()) {
            goToJoinPrivateContestActivity(bundle);
        }

    }
}
