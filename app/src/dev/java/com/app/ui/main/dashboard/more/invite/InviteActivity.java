package com.app.ui.main.dashboard.more.invite;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.ConstantsFlavor;
import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.model.ReferEarnModel;
import com.app.model.UserModel;
import com.app.model.webresponsemodel.ReferEarnResponseModel;
import com.app.ui.MyApplication;
import com.app.ui.main.ToolbarFragment;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.medy.retrofitwrapper.WebRequest;
import com.rest.WebServices;


/**
 * Created by Vishnu Gupta on 7/1/19.
 */
public class InviteActivity extends AppBaseActivity {

    ToolbarFragment toolbarFragment;

    ProgressBar pb_data;
    ImageView refer_image;
    TextView tv_refer_title;
    TextView tv_refer_subtitle;
    TextView tv_how_work;
    TextView tv_rule_of_fairplay;
    TextView tv_refer_code;
//    TextView tv_copy_code;
    ImageView iv_message;
    ImageView iv_whatsapp;
    LinearLayout ll_whatsapp;
    ImageView iv_facebook;
    ImageView iv_more;
    TextView tv_more;

    CardView cv_my_teams;
    TextView tv_total_joined_friends;
    TextView tv_total_bonus;


    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_invite_friends;
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();
        Fragment toolbar = getFm().findFragmentById(R.id.fragment_toolbar);
        if (toolbar instanceof AppBaseFragment) {
            toolbarFragment = (ToolbarFragment) toolbar;
            toolbarFragment.initializeComponent();
            toolbarFragment.setToolbarView(this);
        }

        pb_data = findViewById(R.id.pb_data);
        updateViewVisibitity(pb_data, View.GONE);
        refer_image = findViewById(R.id.refer_image);
        tv_refer_title = findViewById(R.id.tv_refer_title);
        tv_refer_subtitle = findViewById(R.id.tv_refer_subtitle);
        tv_how_work = findViewById(R.id.tv_how_work);
        tv_rule_of_fairplay = findViewById(R.id.tv_rule_of_fairplay);
        tv_refer_code = findViewById(R.id.tv_refer_code);
//        tv_copy_code = findViewById(R.id.tv_copy_code);
        iv_message = findViewById(R.id.iv_message);
        iv_whatsapp = findViewById(R.id.iv_whatsapp);
        ll_whatsapp = findViewById(R.id.ll_whatsapp);
        iv_facebook = findViewById(R.id.iv_facebook);
        iv_more = findViewById(R.id.iv_more);
        tv_more = findViewById(R.id.tv_more);

        cv_my_teams = findViewById(R.id.cv_my_teams);
        tv_total_joined_friends = findViewById(R.id.tv_total_joined_friends);
        tv_total_bonus = findViewById(R.id.tv_total_bonus);

        tv_how_work.setOnClickListener(this);
        tv_rule_of_fairplay.setOnClickListener(this);
//        tv_copy_code.setOnClickListener(this);
        iv_message.setOnClickListener(this);
        iv_whatsapp.setOnClickListener(this);
        ll_whatsapp.setOnClickListener(this);
        iv_facebook.setOnClickListener(this);
        iv_more.setOnClickListener(this);
        tv_more.setOnClickListener(this);
        cv_my_teams.setOnClickListener(this);

        setupUi();
        setupData();
        callGetReferEarn();

    }


    private void setupUi() {
        if(ConstantsFlavor.type != ConstantsFlavor.Type.MySecreate) {
            refer_image.post(new Runnable() {
                @Override
                public void run() {
                    int width = refer_image.getWidth();
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) refer_image.getLayoutParams();
                    layoutParams.height = Math.round(width * 0.416f);
                    refer_image.setLayoutParams(layoutParams);
                }
            });
        }

    }

    private void setupData() {
        ReferEarnModel referEarnModel = getReferEarnModel();
        if (referEarnModel == null) {
            tv_refer_title.setText("");
            tv_refer_subtitle.setText("");
            refer_image.setImageResource(R.drawable.logo_3x);
            updateViewVisibitity(cv_my_teams, View.GONE);
        } else {
            tv_refer_title.setText(referEarnModel.getTitle());
            tv_refer_subtitle.setText(referEarnModel.getSubtitle());
            loadImage(this, refer_image, null, referEarnModel.getImage(), R.drawable.logo_3x, 400);
            if (referEarnModel.getTeam_count() == 0) {
                updateViewVisibitity(cv_my_teams, View.GONE);
            } else {
                tv_total_joined_friends.setText(referEarnModel.getTeam_count() + " Friends joined!");
                tv_total_bonus.setText(currency_symbol + referEarnModel.getTeamEarnText());
                updateViewVisibitity(cv_my_teams, View.VISIBLE);
            }
        }
        UserModel userModel = getUserModel();
        if (userModel != null) {
            tv_refer_code.setText(userModel.getReferral_code());
        } else {
            tv_refer_code.setText("");
        }
    }

    public String getShareContent() {
        ReferEarnModel referEarnModel = getReferEarnModel();
        if (referEarnModel != null) {
            return String.format(referEarnModel.getShare_content(), getUserModel().getReferral_code());
        }
        return "";
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_how_work: {
                Bundle bundle = new Bundle();
                bundle.putString(DATA1, "How It Works");
                bundle.putString(DATA, WebServices.ReferHowItWorks());
                goToWebViewActivity(bundle);
            }
            break;
            case R.id.tv_rule_of_fairplay: {
                Bundle bundle = new Bundle();
                bundle.putString(DATA1, "Rules Of FairPlay");
                bundle.putString(DATA, WebServices.ReferFairPlay());
                goToWebViewActivity(bundle);
            }
            break;
//            case R.id.tv_copy_code: {
//                boolean b = copyToClipboard(getUserModel().getReferral_code());
//                if (b) {
//                    showCustomToast("Copied successfully.");
//                }
//            }
//            break;
            case R.id.iv_message: {
                if (isValidString(getShareContent())) {
                    shareContent(this, getShareContent(), SHARE_BY_SMS);
                }
            }
            break;
            case R.id.ll_whatsapp:
            case R.id.iv_whatsapp: {
                if (isValidString(getShareContent())) {
                    shareContent(this, getShareContent(), SHARE_BY_WHATSAPP);
                }
            }
            break;
            case R.id.iv_facebook: {
                if (isValidString(getShareContent())) {
                    if (ShareDialog.canShow(ShareLinkContent.class)) {
                        ShareLinkContent content = new ShareLinkContent.Builder()
                                .setQuote(getShareContent())
                                .setContentUrl(Uri.parse("https://theteam11.com"))
                                .build();
                        ShareDialog shareDialog = new ShareDialog(this);
                        shareDialog.show(content);
                    }
                }
            }
            break;
            case R.id.tv_more:
            case R.id.iv_more: {
                if (isValidString(getShareContent())) {
                    shareContent(this, getShareContent(), SHARE_BY_OTHERS);
                }
            }
            break;
            case R.id.cv_my_teams: {
                goToInviteDetailActivity(null);
            }
            break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }


    private void goToInviteDetailActivity(Bundle bundle) {
        Intent intent = new Intent(this, InviteDetailActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (isFinishing()) return;
        startActivity(intent);
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }


    private void callGetReferEarn() {
        updateViewVisibitity(pb_data, View.VISIBLE);
        getWebRequestHelper().getReferEarn(this);
    }


    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        updateViewVisibitity(pb_data, View.GONE);
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_REFER_EARN:
                handleReferEarnResponse(webRequest);
                break;
        }
    }

    private void handleReferEarnResponse(WebRequest webRequest) {
        ReferEarnResponseModel responsePojo = webRequest.getResponsePojo(ReferEarnResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            ReferEarnModel data = responsePojo.getData();
            if (data == null) return;
            if (isFinishing()) return;
            MyApplication.getInstance().saveReferEarnInPrefs(data);
            setupData();
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }
    }


}
