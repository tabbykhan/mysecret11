package com.app.ui.main.dashboard.more.invite;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.model.ReferEarnModel;
import com.app.model.UserModel;
import com.app.model.webresponsemodel.ReferEarnDetailResponseModel;
import com.app.ui.MyApplication;
import com.app.ui.main.ToolbarFragment;
import com.app.ui.main.dashboard.more.invite.adapter.InviteDetailAdapter;
import com.medy.retrofitwrapper.WebRequest;
import com.tooltip.Tooltip;
import com.utilities.DeviceScreenUtil;

import java.util.ArrayList;
import java.util.List;


public class InviteDetailActivity extends AppBaseActivity {
    ToolbarFragment toolbarFragment;

    TextView tv_earn_by_friend;
    ImageView iv_info;
    TextView tv_team_earn;
    TextView tv_total_joined_friends;
    ProgressBar pb_earn;
    TextView tv_received_earn;
    TextView tv_total_earn;
    RecyclerView recycler_view;
    TextView tv_invite_more;

    InviteDetailAdapter adapter;

    List<UserModel> referUserData = new ArrayList<>();


    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_invite_detail;
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

        tv_earn_by_friend = findViewById(R.id.tv_earn_by_friend);
        iv_info = findViewById(R.id.iv_info);
        tv_team_earn = findViewById(R.id.tv_team_earn);
        tv_total_joined_friends = findViewById(R.id.tv_total_joined_friends);
        pb_earn = findViewById(R.id.pb_earn);
        tv_received_earn = findViewById(R.id.tv_received_earn);
        tv_total_earn = findViewById(R.id.tv_total_earn);
        recycler_view = findViewById(R.id.recycler_view);
        tv_invite_more = findViewById(R.id.tv_invite_more);

        iv_info.setOnClickListener(this);
        tv_invite_more.setOnClickListener(this);


        initializeRecyclerView();
        setupData();
        callGetReferEarnDetail();

    }

    public String getShareContent() {
        ReferEarnModel referEarnModel = getReferEarnModel();
        if (referEarnModel != null) {
            return String.format(referEarnModel.getShare_content(), getUserModel().getReferral_code());
        }
        return "";
    }

    private void setupData() {
        ReferEarnModel referEarnModel = getReferEarnModel();
        if (referEarnModel == null) {
            tv_earn_by_friend.setText(currency_symbol + "0");
            tv_team_earn.setText(currency_symbol + "0");
            tv_received_earn.setText(currency_symbol + "0");
            tv_total_earn.setText(currency_symbol + "0");
            tv_total_joined_friends.setText("0 Friends joined!");
            pb_earn.setMax(100);
            pb_earn.setProgress(0);
        } else {
            tv_team_earn.setText(currency_symbol + referEarnModel.getTeamEarnText());
            tv_total_joined_friends.setText(referEarnModel.getTeam_count() + " Friends joined!");
            pb_earn.setMax(Math.round(referEarnModel.getTeam_earn()));
            tv_total_earn.setText(currency_symbol + referEarnModel.getTeamEarnText());

            tv_earn_by_friend.setText(currency_symbol + referEarnModel.getTotalReceivedAmountText());
            tv_received_earn.setText(currency_symbol + referEarnModel.getTotalReceivedAmountText());
            pb_earn.setProgress(Math.round(referEarnModel.getTotal_received_amount()));
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }


    private void initializeRecyclerView() {
        adapter = new InviteDetailAdapter(this, referUserData) {
            @Override
            public int getLastItemBottomMargin() {
                return tv_invite_more.getHeight() + DeviceScreenUtil.getInstance().convertDpToPixel(12);
            }
        };
        recycler_view.setLayoutManager(getVerticalLinearLayoutManager());
        recycler_view.setAdapter(adapter);
    }

    @Override

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_info: {
                showTobeEarnTooltip();
            }

            break;
            case R.id.tv_invite_more: {
                if (isValidString(getShareContent())) {
                    shareContent(this, getShareContent(), SHARE_BY_OTHERS);
                }
            }

            break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    private void showTobeEarnTooltip() {
        new Tooltip.Builder(iv_info)
                .setMaxWidth(DeviceScreenUtil.getInstance().getWidth(0.90f))
                .setGravity(Gravity.BOTTOM)
                .setArrowEnabled(true)
                .setText("Cash bonus will be added to your MySecret11 account only once the " +
                        "winner has been declared for the matches your friends have joined cash contest in")
                .setTextColor(getResources().getColor(R.color.colorWhite))
                .setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                .setCancelable(true)
                .setPadding(getResources().getDimensionPixelSize(R.dimen.dp3))
                .setCornerRadius(getResources().getDimension(R.dimen.dp3)).show();
    }

    private void callGetReferEarnDetail() {
        displayProgressBar(false);
        getWebRequestHelper().getReferEarnDetail(this);
    }


    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        dismissProgressBar();
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_REFER_EARN_DEATIL:
                handleReferEarnDetailResponse(webRequest);
                break;
        }
    }

    private void handleReferEarnDetailResponse(WebRequest webRequest) {
        ReferEarnDetailResponseModel responsePojo = webRequest.getResponsePojo(ReferEarnDetailResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            ReferEarnDetailResponseModel.DataBean data = responsePojo.getData();
            if (data == null) return;
            if (isFinishing()) return;
            ReferEarnModel refer_data = data.getRefer_data();
            if (refer_data != null)
                MyApplication.getInstance().saveReferEarnInPrefs(refer_data);
            referUserData.clear();
            List<UserModel> user_refer_data = data.getUser_refer_data();
            if (user_refer_data != null && user_refer_data.size() > 0) {
                referUserData.addAll(user_refer_data);
            }
            setupData();
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }
    }

}
