package com.app.ui.main.dashboard.more.helpdesk;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.model.UserModel;
import com.app.model.webrequestmodel.CustomerEnquiryRequestModel;
import com.app.ui.dialogs.ConfirmationDialog;
import com.app.ui.main.ToolbarFragment;
import com.medy.retrofitwrapper.WebRequest;
import com.medy.retrofitwrapper.WebServiceBaseResponseModel;
import com.rest.WebRequestHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import test.jinesh.captchaimageviewlib.CaptchaImageView;

public class HelpDeskActivity extends AppBaseActivity {

    ToolbarFragment toolbarFragment;

    TextView tv_email;
    TextView tv_update_email;
    TextView tv_name;
    Spinner sp_query;
    EditText et_message;
    EditText et_captcha;
    CaptchaImageView iv_captcha;
    ImageView iv_captcha_refresh;
    TextView tv_submit;


    public String getTitles() {
        if (getIntent().getExtras() != null)
            return getIntent().getExtras().getString(DATA1);
        return "";
    }


    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_helpdesk;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupUserData();
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

        tv_email=findViewById(R.id.tv_email);
        tv_update_email=findViewById(R.id.tv_update_email);
        updateViewVisibitity(tv_update_email,View.INVISIBLE);

        tv_name=findViewById(R.id.tv_name);
        sp_query=findViewById(R.id.sp_query);
        et_message=findViewById(R.id.et_message);
        et_captcha=findViewById(R.id.et_captcha);
        iv_captcha=findViewById(R.id.iv_captcha);
        iv_captcha.setCaptchaLength(6);
        iv_captcha.setCaptchaType(CaptchaImageView.CaptchaGenerator.BOTH);
        iv_captcha.setIsDotNeeded(true);
        iv_captcha_refresh=findViewById(R.id.iv_captcha_refresh);
        tv_submit=findViewById(R.id.tv_submit);

        iv_captcha_refresh.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
    }

    private void setupUserData(){
        UserModel userModel = getUserModel();
        if(userModel!=null){
            tv_email.setText(userModel.getEmail());
            tv_name.setText(userModel.getFullName());

            if(isValidString(userModel.getEmail())){
                updateViewVisibitity(tv_update_email,View.GONE);
            }else{
                setupClickHereButton();
                updateViewVisibitity(tv_update_email,View.VISIBLE);
            }
        }else{
            tv_email.setText("");
            tv_name.setText("");
        }
    }

    private void setupClickHereButton() {
        String signUp = "Click here ";
        tv_update_email.setMovementMethod(LinkMovementMethod.getInstance());
        Pattern termsAndConditionsMatcher = Pattern.compile(signUp);
        Matcher m1 = termsAndConditionsMatcher.matcher(tv_update_email.getText().toString());
        if (m1.find()) {
            URLSpan urlSpan = new URLSpan(m1.group(0)) {
                @Override
                public void onClick(View widget) {
                    goToVerificationActivity(null);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setColor(getResources().getColor(R.color.colorRed));
                    ds.setUnderlineText(false);//there show text below line
                }
            };
            SpannableString string = SpannableString.valueOf(tv_update_email.getText());
            string.setSpan(urlSpan, m1.start(), m1.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.iv_captcha_refresh:
               iv_captcha.regenerate();
               break;

           case R.id.tv_submit:
               handleSubmitClick();
               break;
       }
    }

    private void handleSubmitClick(){

        String email = tv_email.getText().toString();
        if(!isValidString(email)){
            showErrorMsg("Please update your email address.");
            return;
        }

        int selectedItemPosition = sp_query.getSelectedItemPosition();
        if(selectedItemPosition==0){
            showErrorMsg("Please select query category.");
            return;
        }

        String subject = (String) sp_query.getSelectedItem();
        String message = et_message.getText().toString();
        if(!isValidString(message)){
            showErrorMsg("Please enter message");
            return;
        }

        String captcha=et_captcha.getText().toString();
        if(!isValidString(captcha)){
            showErrorMsg("Please enter human verification code");
            return;
        }
        try {
            String captchaCode = iv_captcha.getCaptchaCode();
            if (!captcha.trim().equals(captchaCode)) {
                showErrorMsg("Invalid human verification code");
                et_captcha.setText("");
                iv_captcha_refresh.performClick();
                return;
            }

            CustomerEnquiryRequestModel requestModel = new CustomerEnquiryRequestModel();
            requestModel.subject = subject;
            requestModel.message = message;
            WebRequestHelper webRequestHelper = getWebRequestHelper();
            if (webRequestHelper != null) {
                displayProgressBar(false);
                webRequestHelper.createCustomerEnquiry(requestModel, this);
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        dismissProgressBar();
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()){
            case ID_CREATE_CUSTOMER_ENQUIRY:
                handleCustomerEnquiryResponse(webRequest);
                break;
        }
    }

    private void handleCustomerEnquiryResponse(WebRequest webRequest){
        WebServiceBaseResponseModel baseResponsePojo = webRequest.getBaseResponsePojo();
        if(baseResponsePojo==null)return;
        if(baseResponsePojo.isError()){
            showErrorMsg(baseResponsePojo.getMessage());
            return;
        }
        showMessageDialog(baseResponsePojo.getMessage());
    }

    public void showMessageDialog(String msg) {
        try {

            Bundle bundle = new Bundle();
            bundle.putString(MESSAGE, msg);
            bundle.putString(POS_BTN, "OK");
            bundle.putString(NEG_BTN, "");
            final ConfirmationDialog confirmationDialog = ConfirmationDialog.getInstance(bundle);
            confirmationDialog.setOnClickListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                confirmationDialog.dismiss();
                                finish();
                                break;
                        }
                    } catch (Exception e) {

                    }
                }
            });
            confirmationDialog.show(getFm(), confirmationDialog.getClass().getSimpleName());
        }catch (Exception e){

        }
    }

}
