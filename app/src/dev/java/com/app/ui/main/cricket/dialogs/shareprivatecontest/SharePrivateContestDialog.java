package com.app.ui.main.cricket.dialogs.shareprivatecontest;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseDialogFragment;
import com.app.model.MatchModel;
import com.app.model.webresponsemodel.ShareCodeResponseModel;
import com.app.ui.MatchTimerListener;
import com.app.ui.MyApplication;
import com.base.BaseFragment;
import com.medy.retrofitwrapper.WebRequest;

import static com.app.appbase.AppBaseActivity.SHARE_BY_OTHERS;
import static com.app.appbase.AppBaseActivity.SHARE_BY_WHATSAPP;

public class SharePrivateContestDialog extends AppBaseDialogFragment implements MatchTimerListener {


    ImageView iv_close;

    TextView tv_title;
    ImageView refer_image;
    TextView tv_refer_code;
    TextView tv_copy;
    LinearLayout ll_whatsapp;
    TextView tv_more;


    ImageView iv_team1;
    ProgressBar pb_image1;
    TextView tv_match_name;
    TextView tv_match_name_sec;
    TextView tv_team1_name;
    TextView tv_team2_name;
    LinearLayout ll_timer_lay;
    TextView tv_timer_time;
    TextView tv_match_squad;
    LinearLayout ll_match_status;
    TextView tv_status;
    ImageView iv_team2;
    ProgressBar pb_image2;

    View view_seprator;
    LinearLayout ll_joined_contest;
    ShareCodeResponseModel responsePojo;
    public static SharePrivateContestDialog getInstance(Bundle bundle) {
        SharePrivateContestDialog playerEventsDialog = new SharePrivateContestDialog();
        if (bundle != null)
            playerEventsDialog.setArguments(bundle);
        return playerEventsDialog;
    }

    public MatchModel getMatchModel() {
        return MyApplication.getInstance().getSelectedMatch();
    }

    private String getSlugName() {
        Bundle extras = getArguments();
        if (extras != null) {
            return extras.getString(DATA, "");
        }
        return "";
    }
    @Override
    public int getLayoutResourceId() {
        return R.layout.dialog_share_private_contest;
    }

    @Override
    public int getFragmentContainerResourceId(BaseFragment baseFragment) {
        return 0;
    }


    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        WindowManager.LayoutParams wlmp = dialog.getWindow().getAttributes();
        wlmp.height = WindowManager.LayoutParams.MATCH_PARENT;
        wlmp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlmp.gravity = Gravity.BOTTOM;
        wlmp.dimAmount = 0.3f;

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();

        iv_close = getView().findViewById(R.id.iv_close);
        tv_title = getView().findViewById(R.id.tv_title);

        refer_image = getView().findViewById(R.id.refer_image);
        tv_refer_code = getView().findViewById(R.id.tv_refer_code);
        tv_copy = getView().findViewById(R.id.tv_copy);
        ll_whatsapp = getView().findViewById(R.id.ll_whatsapp);
        tv_more = getView().findViewById(R.id.tv_more);

        iv_team1 = getView().findViewById(R.id.iv_team1);
        pb_image1 = getView().findViewById(R.id.pb_image1);
        tv_match_name = getView().findViewById(R.id.tv_match_name);
        tv_match_name_sec = getView().findViewById(R.id.tv_match_name_sec);
        tv_team1_name = getView().findViewById(R.id.tv_team1_name);
        tv_team2_name = getView().findViewById(R.id.tv_team2_name);
        ll_timer_lay = getView().findViewById(R.id.ll_timer_lay);
        tv_timer_time = getView().findViewById(R.id.tv_timer_time);
        tv_match_squad = getView().findViewById(R.id.tv_match_squad);
        ll_match_status = getView().findViewById(R.id.ll_match_status);
        tv_status = getView().findViewById(R.id.tv_status);
        iv_team2 = getView().findViewById(R.id.iv_team2);
        pb_image2 = getView().findViewById(R.id.pb_image2);
        view_seprator = getView().findViewById(R.id.view_seprator);
        ll_joined_contest = getView().findViewById(R.id.ll_joined_contest);


        iv_close.setOnClickListener(this);
        tv_copy.setOnClickListener(this);
        ll_whatsapp.setOnClickListener(this);
        tv_more.setOnClickListener(this);

        updateViewVisibitity(ll_match_status, View.GONE);
        updateViewVisibitity(view_seprator, View.GONE);
        updateViewVisibitity(ll_joined_contest, View.GONE);

        callShareTeam();

        setupUi();
        setMatchData();

    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().addMatchTimerListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        MyApplication.getInstance().removeMatchTimerListener(this);
    }

    private void setMatchData() {
        if (getMatchModel() != null) {
            tv_match_name.setText(getMatchModel().getSeries().getName());
            tv_team1_name.setText(getMatchModel().getTeam1().getName(1));
            tv_team2_name.setText(getMatchModel().getTeam2().getName(1));
            tv_match_name_sec.setText(getMatchModel().getGametype().getName());

            if(getMatchModel().getTeam1().getImage() != null)
                ((AppBaseActivity) getActivity()).loadImage(getActivity(), iv_team1, pb_image1, getMatchModel().getTeam1().getImage(),R.mipmap.ic_launcher_round);
            if(getMatchModel().getTeam2().getImage() != null)
                ((AppBaseActivity) getActivity()).loadImage(getActivity(), iv_team2, pb_image2, getMatchModel().getTeam2().getImage(),R.mipmap.ic_launcher_round);
            tv_timer_time.setText(getMatchModel().getRemainTimeText());
            tv_timer_time.setTextColor(getResources().getColor(getMatchModel()
                    .getTimerColor()));

            if(getMatchModel().isInPlayingSquadUpdated()){
                updateViewVisibitity(tv_match_squad,View.VISIBLE);
                tv_match_squad.setActivated(true);
            }else{
                updateViewVisibitity(tv_match_squad,View.INVISIBLE);
            }
        }
    }
    private void callShareTeam() {
        displayProgressBar(false);
        getWebRequestHelper().shareContest(getSlugName(), this);
    }


    private void setupUi() {
        refer_image.post(new Runnable() {
            @Override
            public void run() {
                int width = refer_image.getWidth();
                CardView.LayoutParams layoutParams = (CardView.LayoutParams) refer_image.getLayoutParams();
                layoutParams.height = Math.round(width * 0.416f);
                refer_image.setLayoutParams(layoutParams);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.ll_whatsapp:
                if (isValidString(responsePojo.getMessage())) {
                    ((AppBaseActivity)getActivity()).shareContent(getActivity(), responsePojo.getMessage(), SHARE_BY_WHATSAPP);
                }
                break;
            case R.id.tv_more:
                if (isValidString(responsePojo.getMessage())) {
                    ((AppBaseActivity)getActivity()).shareContent(getActivity(), responsePojo.getMessage(), SHARE_BY_OTHERS);
                }
                break;
            case R.id.tv_copy:
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("invite_code", tv_refer_code.getText().toString().trim());
                ((AppBaseActivity)getActivity()).showCustomToast("Copied to clipboard");
                clipboard.setPrimaryClip(clip);
                break;
        }
    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_SHARE_CONTEST:
                handleShareCodeResponse(webRequest);
                break;
        }

    }

    private void handleShareCodeResponse(WebRequest webRequest) {
        responsePojo = webRequest.getResponsePojo(ShareCodeResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            tv_refer_code.setText(responsePojo.getData());
            if(responsePojo.getImage() != null)
            ((AppBaseActivity) getActivity()).loadImage(getActivity(), refer_image, null, responsePojo.getImage(),R.mipmap.ic_launcher_round);

        } else {
            if (getActivity() == null || getDialog() == null || !getDialog().isShowing()) return;
            ((AppBaseActivity) getActivity()).showErrorMsg(responsePojo.getMessage());
        }

    }

    @Override
    public void onMatchTimeUpdate() {
        setMatchData();
    }
}
