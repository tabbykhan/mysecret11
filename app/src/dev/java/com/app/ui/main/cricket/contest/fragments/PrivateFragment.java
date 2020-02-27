package com.app.ui.main.cricket.contest.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.model.webresponsemodel.PrivateContestFreeResponseModel;
import com.app.model.webresponsemodel.PrivateContestSettingResponseModel;
import com.app.ui.main.cricket.contest.ContestActivity;
import com.app.ui.main.cricket.contest.makeyourcontest.createcontest.CreatePrivateContestActivity;
import com.medy.retrofitwrapper.WebRequest;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrivateFragment extends AppBaseFragment {

    private LinearLayout ll_ticketing;
    private LinearLayout ll_create_contest;
    private LinearLayout ll_invite_code;
    private LinearLayout ll_enter_invite_code;
    private TextView tv_choose_wining_breakup;
    private TextView tv_create_contest;
    private TextView tv_invite_code;
    private ImageView iv_add;
    private ImageView iv_invite;
    private EditText et_invite_code;
    private TextView tv_join_contest;
    private EditText et_contest_name;
    private EditText et_total_winning_amount;
    private EditText et_contest_size;
    private Switch switch_btn;
    private TextView tv_min_contest;
    private TextView tv_per_team_amount;
    private TextView tv_max_pool_amount;
    ProgressBar pb_playing;
    private String total_winning_amount = "";
    private String total_contest_size = "";
    private String entry_fee = "";

    private Handler handler = new Handler();

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            try {
                total_winning_amount = et_total_winning_amount.getText().toString();
                total_contest_size = et_contest_size.getText().toString();
                getPrivateContestEntryFee();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    };


    private TextWatcher winning_amount = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            tv_choose_wining_breakup.setClickable(false);
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable, 1000);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private TextWatcher contest_size = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            tv_choose_wining_breakup.setClickable(false);
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable, 1000);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_private;
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();
        tv_choose_wining_breakup = getView().findViewById(R.id.tv_choose_wining_breakup);
        tv_choose_wining_breakup.setOnClickListener(this);
        iv_add = getView().findViewById(R.id.iv_add);
        tv_create_contest = getView().findViewById(R.id.tv_create_contest);
        iv_invite = getView().findViewById(R.id.iv_invite);
        tv_invite_code = getView().findViewById(R.id.tv_invite_code);
        ll_create_contest = getView().findViewById(R.id.ll_create_contest);
        ll_enter_invite_code = getView().findViewById(R.id.ll_enter_invite_code);

        et_invite_code = getView().findViewById(R.id.et_invite_code);
        tv_join_contest = getView().findViewById(R.id.tv_join_contest);
        tv_join_contest.setOnClickListener(this);

        et_contest_name = getView().findViewById(R.id.et_contest_name);
        et_total_winning_amount = getView().findViewById(R.id.et_total_winning_amount);
        et_contest_size = getView().findViewById(R.id.et_contest_size);
        switch_btn = getView().findViewById(R.id.switch_btn);
        tv_min_contest = getView().findViewById(R.id.tv_min_contest);
        tv_per_team_amount = getView().findViewById(R.id.tv_per_team_amount);
        pb_playing = getView().findViewById(R.id.pb_playing);
        tv_max_pool_amount = getView().findViewById(R.id.tv_max_pool_amount);

        ll_ticketing = getView().findViewById(R.id.ll_ticketing);
        ll_ticketing.setOnClickListener(this);
        ll_invite_code = getView().findViewById(R.id.ll_invite_code);
        ll_invite_code.setOnClickListener(this);

        chnageTeamInfo(false);
        getPrivateContestSettings();

        et_total_winning_amount.addTextChangedListener(winning_amount);
        et_contest_size.addTextChangedListener(contest_size);

        updateViewVisibitity(pb_playing, View.GONE);
        tv_choose_wining_breakup.setClickable(false);

    }

    private void getPrivateContestSettings() {
        getWebRequestHelper().getPrivateContestSettings(this);
    }

    private void getPrivateContestEntryFee() {
        tv_per_team_amount.setText("");
        entry_fee = "";
        tv_choose_wining_breakup.setClickable(false);
        if (total_contest_size.isEmpty())
            return;
        if (total_winning_amount.isEmpty())
            return;
        if (total_contest_size.equalsIgnoreCase("0") || total_contest_size.equalsIgnoreCase("1")) {
            return;
        }
        updateViewVisibitity(pb_playing, View.VISIBLE);
        getWebRequestHelper().getPrivateContestEntryFee(total_contest_size, total_winning_amount, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_choose_wining_breakup:
                CallCreateContest();
                break;
            case R.id.ll_ticketing:
                chnageTeamInfo(true);
                break;
            case R.id.ll_invite_code:
                chnageTeamInfo(false);
                break;
            case R.id.tv_join_contest:
                gotoJoinContest();
                break;
        }
    }

    private void gotoJoinContest() {
        String invite_code = et_invite_code.getText().toString().trim();
        if(invite_code.isEmpty()){
            showErrorMsg("Please enter invite code.");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(PRIVATE_SLUG,invite_code);
        if(getActivity()==null)return;
        ((AppBaseActivity)getActivity()).goToJoinPrivateContestActivity(bundle);
    }


    private void CallCreateContest() {
        String contest_name = et_contest_name.getText().toString().trim();
        total_winning_amount = et_total_winning_amount.getText().toString().trim();
        total_contest_size = et_contest_size.getText().toString().trim();
        boolean multiple_team = switch_btn.isChecked();

        if (!isValidString(total_winning_amount)) {
            return;
        }

        if (!isValidString(total_contest_size)) {
            return;
        }

        if (!isValidString(entry_fee)) {
            return;
        }

//        if (contest_name.isEmpty()) {
//            showErrorMsg("Please give contest name.");
//            return;
//        }

        Bundle bundle = new Bundle();
        bundle.putString(CONTEST_SIZE, total_contest_size);
        bundle.putString(PRIZE_POOL, total_winning_amount);
        bundle.putString(ENTER_AMOUNT, entry_fee);
        bundle.putString(CONTEST_NAME, contest_name);
        bundle.putBoolean(MULTIPLE_CONTEST, multiple_team);
        goToCreateContestActivity(bundle);
    }

    private void chnageTeamInfo(boolean info) {
        if(info) {
            iv_add.setActivated(true);
            tv_create_contest.setActivated(true);
            iv_invite.setActivated(false);
            tv_invite_code.setActivated(false);
            updateViewVisibitity(ll_create_contest, View.VISIBLE);
            updateViewVisibitity(ll_enter_invite_code, View.GONE);
        } else {
            iv_add.setActivated(false);
            tv_create_contest.setActivated(false);
            iv_invite.setActivated(true);
            tv_invite_code.setActivated(true);
            updateViewVisibitity(ll_create_contest, View.GONE);
            updateViewVisibitity(ll_enter_invite_code, View.VISIBLE);
        }
    }



    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_GET_PRIVATE_CONTEST_SETTING:
                handleContestSettingResponse(webRequest);
                break;
            case ID_GET_PRIVATE_CONTEST_ENTRY_FEE:
                handleContestEnertFreeResponse(webRequest);
                break;


        }

    }


    private void handleContestSettingResponse(WebRequest webRequest) {
        PrivateContestSettingResponseModel responsePojo = webRequest.getResponsePojo(PrivateContestSettingResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            tv_min_contest.setText("Min 2 - Max " + responsePojo.getData().getPRIVATE_CONTEST_MAX_CONTEST_SIZE());
            tv_max_pool_amount.setText("Max " + responsePojo.getData().getPRIVATE_CONTEST_MAX_PRIZE_POOL());
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }

    }

    private void handleContestEnertFreeResponse(WebRequest webRequest) {
        PrivateContestFreeResponseModel responsePojo = webRequest.getResponsePojo(PrivateContestFreeResponseModel.class);
        updateViewVisibitity(pb_playing, View.GONE);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            tv_choose_wining_breakup.setClickable(true);
            entry_fee = responsePojo.getData().getEntry_fees();
            tv_per_team_amount.setText(entry_fee);
        } else {
            if (isFinishing()) return;
            tv_choose_wining_breakup.setClickable(false);
            entry_fee = "";
            tv_per_team_amount.setText(entry_fee);
            showErrorMsg(responsePojo.getMessage());
        }
    }


    private void goToCreateContestActivity(Bundle bundle) {
        Intent intent = new Intent(getActivity(), CreatePrivateContestActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent,ContestActivity.REQUEST_CREATE_PRIVATE_CONTEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ContestActivity.REQUEST_CREATE_PRIVATE_CONTEST){
            if(resultCode== Activity.RESULT_OK){
                Bundle extras = data.getExtras();
                if(getActivity()==null)return;
                ((AppBaseActivity)getActivity()).goToSharePrivateContestDialog(extras);
            }
        }
    }
}
