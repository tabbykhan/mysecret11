package com.app.ui.main.dashboard.profile.playerinfo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.appbase.AppBaseModel;
import com.app.model.StateModel;
import com.app.model.UserModel;
import com.app.model.webrequestmodel.UpdateProfileRequestModel;
import com.app.model.webresponsemodel.StatesResponseModel;
import com.app.model.webresponsemodel.VerifyOtpResponseModel;
import com.app.ui.dialogs.MyDatePickerDialog;
import com.app.ui.dialogs.SelectStateDialog;
import com.app.ui.dialogs.selection.DataDialog;
import com.app.ui.main.ToolbarFragment;
import com.medy.retrofitwrapper.WebRequest;
import com.utilities.DatePickerUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by Vishnu Gupta on 7/1/19.
 */
public class PlayerInfoActivity extends AppBaseActivity {

    ToolbarFragment toolbarFragment;

    TextView tv_name;
    TextView tv_phone;

    ImageView iv_player;
    ProgressBar pb_image;

    TextView et_email;
    TextView tv_email_verify;
    TextView et_mobile;
    TextView tv_mobile_verify;
    EditText et_f_name;
    EditText et_l_name;
    View view_last_name;
    LinearLayout ll_lastname;

    TextView et_dob;
    LinearLayout ll_address_detail;
    EditText et_address_line_1;
    EditText et_address_line_2;
    TextView et_state;
    EditText et_city;
    View view_city;
    LinearLayout ll_city;
    LinearLayout ll_pincode;

    EditText et_pincode;

    TextView tv_update;

    List<StateModel> stateModels = new ArrayList<>();

    public boolean openFromContest() {
        return getIntent().getExtras() != null && getIntent().getExtras().getBoolean(DATA, false);
    }


    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_playerinfo;
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

        tv_name = findViewById(R.id.tv_name);
        tv_phone = findViewById(R.id.tv_phone);
        iv_player = findViewById(R.id.iv_player);
        pb_image = findViewById(R.id.pb_image);
        et_email = findViewById(R.id.et_email);
        tv_email_verify = findViewById(R.id.tv_email_verify);
        et_mobile = findViewById(R.id.et_mobile);
        tv_mobile_verify = findViewById(R.id.tv_mobile_verify);
        et_f_name = findViewById(R.id.et_f_name);
        et_l_name = findViewById(R.id.et_l_name);
        view_last_name = findViewById(R.id.view_last_name);
        ll_lastname = findViewById(R.id.ll_lastname);
        et_dob = findViewById(R.id.et_dob);
        ll_address_detail = findViewById(R.id.ll_address_detail);
        et_address_line_1 = findViewById(R.id.et_address_line_1);
        et_address_line_2 = findViewById(R.id.et_address_line_2);
        et_state = findViewById(R.id.et_state);
        et_city = findViewById(R.id.et_city);
        view_city = findViewById(R.id.view_city);
        ll_city = findViewById(R.id.ll_city);
        ll_pincode = findViewById(R.id.ll_pincode);
        et_pincode = findViewById(R.id.et_pincode);
        tv_update = findViewById(R.id.tv_update);

        et_dob.setOnClickListener(this);
        et_state.setOnClickListener(this);
        tv_update.setOnClickListener(this);
        tv_email_verify.setOnClickListener(this);
        tv_mobile_verify.setOnClickListener(this);


        if (openFromContest()) {
            updateViewVisibitity(view_last_name, View.GONE);
            updateViewVisibitity(ll_lastname, View.GONE);
            updateViewVisibitity(ll_address_detail, View.GONE);
            updateViewVisibitity(view_city, View.GONE);
            updateViewVisibitity(ll_city, View.GONE);
            updateViewVisibitity(ll_pincode, View.GONE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUserData();
    }

    public void updateUserData() {
        UserModel userModel = getUserModel();
        if (userModel == null) {
            tv_name.setText("");
            tv_phone.setText("");
            iv_player.setImageResource(R.drawable.no_image);
            updateViewVisibitity(pb_image, View.GONE);
            updateViewVisibitity(tv_email_verify, View.GONE);
            updateViewVisibitity(tv_mobile_verify, View.GONE);

            et_email.setText("");
            et_mobile.setText("");
            et_f_name.setText("");
            et_l_name.setText("");
            et_address_line_1.setText("");
            et_address_line_2.setText("");
            et_city.setText("");
            et_pincode.setText("");
            et_dob.setText("");
            et_dob.setTag(null);
            et_state.setText("");
            et_state.setTag(null);

        } else {
            tv_name.setText(userModel.getFullName());
            tv_phone.setText(userModel.getFullMobile());
            loadImage(this,
                    iv_player, pb_image, userModel.getImage(), R.drawable.no_image);

            et_email.setText(userModel.getEmail());
            et_mobile.setText(userModel.getPhone());
            et_f_name.setText(userModel.getFirstname());
            et_l_name.setText(userModel.getLastname());
            et_address_line_1.setText(userModel.getAddressline1());
            et_address_line_2.setText(userModel.getAddressline2());
            et_city.setText(userModel.getCity().getName());
            et_pincode.setText(userModel.getPincode());
            et_dob.setText(userModel.getDobText());
            if (userModel.getDob() == 0) {
                et_dob.setTag(null);
            } else {
                Calendar instance = Calendar.getInstance();
                instance.setTimeInMillis(userModel.getDob() * 1000);
                et_dob.setTag(instance);
            }

            StateModel state = userModel.getState();
            if (state != null) {
                et_state.setText(state.getName());
                et_state.setTag(state);
            } else {
                et_state.setText("");
                et_state.setTag(null);
            }

            if (isValidString(userModel.getPhone())) {
                updateViewVisibitity(tv_mobile_verify, View.GONE);
            } else {
                updateViewVisibitity(tv_mobile_verify, View.VISIBLE);
            }

            if (isValidString(userModel.getEmail())) {
                updateViewVisibitity(tv_email_verify, View.GONE);
            } else {
                updateViewVisibitity(tv_email_verify, View.VISIBLE);
            }

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_email_verify:
            case R.id.tv_mobile_verify:
                Bundle bundle = new Bundle();
                bundle.putBoolean(DATA, true);
                goToVerificationActivity(bundle);
                break;
            case R.id.tv_update:
                callUpdate();
                break;

            case R.id.et_dob:
                showDobPicker();
                break;

            case R.id.et_state:
                if (stateModels.size() == 0) {
                    callGetStates();
                } else {
                    showStatesDialog();
                }

                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    private void showDobPicker() {
        hideKeyboard();
        Calendar now = Calendar.getInstance();
        now.add(Calendar.YEAR, -18);

//        DatePickerDialog datePicker = DatePickerUtil.getDatePicker(this, et_dob, new DatePickerUtil.OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(Calendar calendar) {
//                et_dob.setTag(calendar);
//
//                Date date = calendar.getTime();
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(AppBaseModel.DATE_MMMDDYYYY);
//                String sel_date = simpleDateFormat.format(date);
//                et_dob.setText(sel_date);
//
//            }
//        });

        MyDatePickerDialog datePicker = DatePickerUtil.getDatePicker("SELECT DATE",this, et_dob, new DatePickerUtil.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Calendar calendar) {
                et_dob.setTag(calendar);

                Date date = calendar.getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(AppBaseModel.DATE_DDMMYYYY);
                String sel_date = simpleDateFormat.format(date);
                et_dob.setText(sel_date);

            }
        });
        datePicker.setMaxDate(now.getTimeInMillis());
        datePicker.show(getFm(),datePicker.getClass().getSimpleName());
    }

    private void showStatesDialog() {
        hideKeyboard();
        final SelectStateDialog selectStateDialog = new SelectStateDialog();
        selectStateDialog.setDataList(stateModels);
        selectStateDialog.setOnItemSelectedListeners(new DataDialog.OnItemSelectedListener() {
            @Override
            public void onItemSelectedListener(int position) {
                selectStateDialog.dismiss();
                et_state.setTag(stateModels.get(position));
                et_state.setText(stateModels.get(position).getName());
            }
        });
        selectStateDialog.show(getFm(), selectStateDialog.getClass().getSimpleName());

    }

    private void callGetStates() {
        displayProgressBar(false, "Wait...");
        getWebRequestHelper().getStates(COUNTRY_ID_VALUE, this);
    }


    private void callUpdate() {
        UserModel userModel = getUserModel();
        if (userModel == null) return;
        String fname = et_f_name.getText().toString().trim();
        String lname = et_l_name.getText().toString().trim();
        long dob = 0;
        if (et_dob.getTag() != null) {
            dob = (((Calendar) et_dob.getTag()).getTimeInMillis()) / 1000;
        }
        String addressline1 = et_address_line_1.getText().toString().trim();
        String addressline2 = et_address_line_2.getText().toString().trim();
        long state = 0;
        if (et_state.getTag() != null) {
            state = ((StateModel) et_state.getTag()).getId();
        }

        String city = et_city.getText().toString().trim();
        String pincode = et_pincode.getText().toString().trim();

        if (fname.isEmpty()) {
            showErrorMsg("Please enter first name.");
            return;
        }
        if (dob == 0) {
            showErrorMsg("Please select Date Of Birth.");
            return;
        }

        if (state == 0) {
            showErrorMsg("Please select State.");
            return;
        }


        UpdateProfileRequestModel requestModel = new UpdateProfileRequestModel();
        requestModel.firstname = fname;
        requestModel.lastname = lname;
        requestModel.dob = dob;
        requestModel.addressline1 = addressline1;
        requestModel.addressline2 = addressline2;
        requestModel.country = 0;
        requestModel.state = state;
        requestModel.city = city;
        requestModel.pincode = pincode;

        requestModel.email = userModel.getEmail();
        requestModel.country_mobile_code = userModel.getCountry_mobile_code();
        requestModel.phone = userModel.getPhone();

        displayProgressBar(false, "Wait...");
        getWebRequestHelper().updateProfile(requestModel, this);
    }


    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        dismissProgressBar();
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_GET_STATES:
                handleStatesResponse(webRequest);
                break;
            case ID_UPDATE_PROFILE:
                handleUpdateProfile(webRequest);
                break;
        }

    }

    private void handleStatesResponse(WebRequest webRequest) {
        StatesResponseModel responsePojo = webRequest.getResponsePojo(StatesResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            List<StateModel> data = responsePojo.getData();
            stateModels.clear();
            if (data != null && data.size() > 0) {
                stateModels.addAll(data);
            }
            if (isFinishing()) return;
            showStatesDialog();

        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }

    }

    private void handleUpdateProfile(WebRequest webRequest) {
        VerifyOtpResponseModel responsePojo = webRequest.getResponsePojo(VerifyOtpResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            UserModel data = responsePojo.getData();
            if (data == null) return;
            if (isFinishing()) return;
            getUserPrefs().updateLoggedInUser(data);
            showCustomToast(responsePojo.getMessage());
            setResult();
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }
    }

    private void setResult() {
        setResult(Activity.RESULT_OK);
        supportFinishAfterTransition();
    }

}
