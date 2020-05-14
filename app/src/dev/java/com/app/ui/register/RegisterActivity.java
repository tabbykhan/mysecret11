package com.app.ui.register;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ConstantsFlavor;
import com.CoustomControl.AppCommon;
import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.appbase.AppBaseResponseModel;
import com.app.appbase.AutoCompleteHelper;
import com.app.model.ReferralSettingsModel;
import com.app.model.UserModel;
import com.app.model.webrequestmodel.NewUserRequestModel;
import com.app.model.webrequestmodel.SocialLoginRequestModel;
import com.app.model.webrequestmodel.TeamNameRequestModel;
import com.app.model.webresponsemodel.NewUserResponseModel;
import com.app.model.webresponsemodel.ReferSettingResponseModel;
import com.app.model.webresponsemodel.TeamSuggestedResponseModel;
import com.app.model.webresponsemodel.VerifyOtpResponseModel;
import com.app.ui.MyApplication;
import com.app.ui.main.ToolbarFragment;
import com.app.ui.main.dashboard.DashboardActivityNew;
import com.app.ui.optverifiction.OtpVerifyActivity;
import com.app.ui.register.adapter.ExploreAdapter;
import com.customviews.TypefaceEditText;
import com.google.gson.Gson;
import com.medy.retrofitwrapper.WebRequest;
import com.rest.WebServices;
import com.sociallogin.FacebookLoginHandler;
import com.sociallogin.GplusLoginHandler;
import com.sociallogin.SocialData;
import com.sociallogin.SocialLoginListener;
import com.sociallogin.SocialPermissionErrorDialog;
import com.utilities.ItemClickSupport;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Manish Kumar
 * @since 3/10/18
 */

public class RegisterActivity extends AppBaseActivity {


    ToolbarFragment toolbarFragment;
    RelativeLayout rl_fb_btn;
    RelativeLayout rl_gplus_btn;
    EditText et_name;
    EditText et_email;
    EditText et_mobile;
    EditText et_refer_code;
  //  EditText et_userId;
    TypefaceEditText et_password;
    CheckBox cb_terms;
    TextView tv_terms;
    TextView tv_register;
    TextView tv_login;
    TextView tv_referal_msg;
    CheckBox cb_password_show_hide;
    private AutoCompleteHelper autoCompleteHelper;
    private RecyclerView recycler_view;
    EditText et_team_name;
    private ExploreAdapter adapter;
    boolean filte_info = false;

    SocialLoginListener fbLoginListener = new SocialLoginListener() {
        @Override
        public void socialLoginSuccess(SocialData socialData) {
            callSocialLogin(socialData);
        }

        @Override
        public void fbLoginPermissionError() {
            showFbLoginErrorDialog();
        }
    };

    SocialLoginListener gplusLoginListener = new SocialLoginListener() {
        @Override
        public void socialLoginSuccess(SocialData socialData) {
            callSocialLogin(socialData);
        }

        @Override
        public void fbLoginPermissionError() {

        }
    };


    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_register;
    }


    @Override
    public void initializeComponent() {
        super.initializeComponent();
        socialLogout();

        Fragment toolbar = getFm().findFragmentById(R.id.fragment_toolbar);
        if (toolbar instanceof AppBaseFragment) {
            toolbarFragment = (ToolbarFragment) toolbar;
            toolbarFragment.initializeComponent();
        }

        rl_fb_btn = findViewById(R.id.rl_fb_btn);
        rl_gplus_btn = findViewById(R.id.rl_gplus_btn);

        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_mobile = findViewById(R.id.et_mobile);
        et_refer_code = findViewById(R.id.et_refer_code);
        et_password = findViewById(R.id.et_password);
     //   et_userId = findViewById(R.id.et_userId);
        cb_terms = findViewById(R.id.cb_terms);
        tv_terms = findViewById(R.id.tv_terms);
        tv_register = findViewById(R.id.tv_register);
        tv_login = findViewById(R.id.tv_login);
        tv_referal_msg = findViewById(R.id.tv_referal_msg);
        cb_password_show_hide = findViewById(R.id.cb_password_show_hide);
        tv_referal_msg.setText("");

        tv_register.setOnClickListener(this);
        rl_fb_btn.setOnClickListener(this);
        rl_gplus_btn.setOnClickListener(this);


        setupSignInButton();
        setupTermsButton();
        callGetSettings();

        cb_password_show_hide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                et_password.setCustomFont(getString(R.string.app_font_regular));

            }
        });

        et_team_name = findViewById(R.id.et_team_name);

        et_team_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               // updateViewVisibitity(pb_search, View.VISIBLE);
            }

            @Override
            public void afterTextChanged(final Editable s) {
                final String s1 = s.toString().trim();
                if (s1.length() > 0) {
                    if(filte_info){
                        filte_info =false;
                        return;
                    }
                    autoCompleteHelper.getFilter().filter(s1);
//                    updateViewVisibitity(iv_close, View.VISIBLE);
//                    updateViewVisibitity(pb_search, View.GONE);
                } else {
                    //updateViewVisibitity(iv_close, View.GONE);
                    adapter.cleanSearch();
                    updateViewVisibitity(recycler_view,View.GONE);
                    //updateViewVisibitity(recycler_view,View.VISIBLE);
                }

            }
        });
        autoCompleteHelper = new AutoCompleteHelper(this) {
            @Override
            public WebRequest getWebRequest(String text) {
                return callApi(text);
            }

            @Override
            public AppBaseResponseModel parseResponse(WebRequest webRequest) {
                try {
                    TeamSuggestedResponseModel responseModel = webRequest.getResponsePojo(TeamSuggestedResponseModel.class);
                    return responseModel;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        autoCompleteHelper.setResultAvailableListener(new AutoCompleteHelper.ResultAvailableListener() {
            @Override
            public void onResultAvailable(AppBaseResponseModel appBaseResponseModel) {
                if (appBaseResponseModel != null) {
                    TeamSuggestedResponseModel responseModel = (TeamSuggestedResponseModel) appBaseResponseModel;
                    if (!responseModel.isError()) {
                        List<String> data = responseModel.getData();
                        if(et_team_name.getText().toString().trim().length()==0)
                            return;
                        if (data != null) {
                            if (adapter != null)
                                adapter.updatelist(data);
                            if(data.size()>0) {
                                updateViewVisibitity(recycler_view, View.VISIBLE);
                            }else {
                                updateViewVisibitity(recycler_view,View.GONE);
                            }
                        }
                    }
                }
            }
        });
        initializeRecyclerView();
    }

    private void initializeRecyclerView() {
        recycler_view = findViewById(R.id.recycler_view);
        adapter = new ExploreAdapter(this);
        recycler_view.setLayoutManager(getFullHeightLinearLayoutManager());
        recycler_view.setAdapter(adapter);
        recycler_view.setNestedScrollingEnabled(true);
        ItemClickSupport.addTo(recycler_view).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                String branchModel = adapter.getItem(position);
                if (branchModel == null) return;
                filte_info = true;
                et_team_name.setText(branchModel.toString());
                adapter.cleanSearch();
            }
        });
    }
    private WebRequest callApi(String text) {

        if (text == null) {
            return null;
        }
        TeamNameRequestModel requestModel = new TeamNameRequestModel();
        requestModel.reference = text;

        return getWebRequestHelper().searchSuggesName(requestModel, this);
    }

    private void socialLogout() {
        MyApplication.getInstance().getFacebookLoginHandler().callLogout();
        MyApplication.getInstance().getGplusLoginHandler().callLogout();
    }

    private void setupSignInButton() {
        String signUp = "Log In";
        tv_login.setMovementMethod(LinkMovementMethod.getInstance());
        Pattern termsAndConditionsMatcher = Pattern.compile(signUp);
        Matcher m1 = termsAndConditionsMatcher.matcher(tv_login.getText().toString());
        if (m1.find()) {
            URLSpan urlSpan = new URLSpan(m1.group(0)) {
                @Override
                public void onClick(View widget) {
                    onBackPressed();
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setColor(getResources().getColor(R.color.colorWhite));
                    ds.setUnderlineText(false);
                }
            };
            SpannableString string = SpannableString.valueOf(tv_login.getText());
           string.setSpan(urlSpan, m1.start(), m1.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private void setupTermsButton() {
        tv_terms.setMovementMethod(LinkMovementMethod.getInstance());

        String terms = "T&Cs";
        Pattern compile = Pattern.compile(terms);
        Matcher m1 = compile.matcher(tv_terms.getText().toString());
        if (m1.find()) {
            URLSpan urlSpan = new URLSpan(m1.group(0)) {
                @Override
                public void onClick(View widget) {
                    Bundle bundle = new Bundle();
                    bundle.putString(DATA1, "T&Cs");
                    bundle.putString(DATA, WebServices.GetTnc());
                    goToWebViewActivity(bundle);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setColor(getColorFromStyle(R.attr.app_link_text_color));
                    ds.setUnderlineText(false);
                }
            };
            SpannableString string = SpannableString.valueOf(tv_terms.getText());
            string.setSpan(urlSpan, m1.start(), m1.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        String privacy = "Privacy Policy";
        compile = Pattern.compile(privacy);
        m1 = compile.matcher(tv_terms.getText().toString());
        if (m1.find()) {
            URLSpan urlSpan = new URLSpan(m1.group(0)) {
                @Override
                public void onClick(View widget) {
                    Bundle bundle = new Bundle();
                    bundle.putString(DATA1, "Privacy Policy");
                    bundle.putString(DATA, WebServices.GetPrivacyPolicy());
                    goToWebViewActivity(bundle);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setColor(getColorFromStyle(R.attr.app_link_text_color));
                    ds.setUnderlineText(true);
                }
            };
            SpannableString string = SpannableString.valueOf(tv_terms.getText());
            string.setSpan(urlSpan, m1.start(), m1.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register:
                callRegister();
                break;

            case R.id.rl_fb_btn:
                MyApplication.getInstance().getFacebookLoginHandler().setSocialLoginListner(fbLoginListener);
                MyApplication.getInstance().getFacebookLoginHandler().callLoginWithRead(this,
                        Arrays.asList("public_profile", "email"));

                break;
            case R.id.rl_gplus_btn:
                MyApplication.getInstance().getGplusLoginHandler().setSocialLoginListner(gplusLoginListener);
                MyApplication.getInstance().getGplusLoginHandler().gPlusSignIn(this);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    private void showFbLoginErrorDialog() {
        SocialPermissionErrorDialog socialPermissionErrorDialog = new SocialPermissionErrorDialog();
        socialPermissionErrorDialog.setOnClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        MyApplication.getInstance().getFacebookLoginHandler().setSocialLoginListner(fbLoginListener);
                        MyApplication.getInstance().getFacebookLoginHandler().callLoginWithRead(RegisterActivity.this,
                                Arrays.asList("email"));
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        });
        socialPermissionErrorDialog.show(getFm(),
                socialPermissionErrorDialog.getClass().getSimpleName());
    }


    private void goToOtpVerifyActivity(Bundle bundle) {
        Intent intent = new Intent(this, OtpVerifyActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    private void goToDashboardActivity(Bundle bundle) {
        Intent intent = new Intent(this, DashboardActivityNew.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    private void callGetSettings() {
        displayProgressBar(false);
        getWebRequestHelper().getReferralSettings(this);
    }

    private void callRegister() {
        String name = et_name.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String phone = et_mobile.getText().toString().trim();
        String referral_code = et_refer_code.getText().toString().trim();
        String password = et_password.getText().toString();
        String team_name = et_team_name.getText().toString();
    //    String userId = et_userId.getText().toString();

        if(!team_name.isEmpty()){
            if (team_name.length() < 8 || team_name.length() > 21) {
                showErrorMsg("Team name should be atleast 8 to 20 char.");
                return;
            }

            if (!Pattern.matches(".*([a-zA-Z].*[0-9]|[0-9].*[a-zA-Z]).*", team_name)) {
                showErrorMsg("Team name should be one char and one number.");
                return;
            }
        }

        if (name.isEmpty()) {
            showErrorMsg("Please enter name.");
            return;
        }
        if (email.isEmpty()) {
            showErrorMsg("Please enter email address.");
            return;
        }
        if (phone.isEmpty()) {
            showErrorMsg("Please enter mobile number.");
            return;
        }
        if (password.isEmpty()) {
            showErrorMsg("Please enter password.");
            return;
        }
      /*  if (userId.isEmpty()) {
            showErrorMsg("Please enter UserID.");
            return;
        }*/

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showErrorMsg("Please enter valid email address.");
            return;
        }

        if (!Pattern.matches("[0-9]{10}", phone)) {
            showErrorMsg("Please enter valid mobile number.");
            return;
        }

        if (password.length() < 6) {
            showErrorMsg("Password should be atleast 6 char.");
            return;
        }
        if (!cb_terms.isChecked()) {
            showErrorMsg("Please Accept Terms and Privacy Policy.");
            return;
        }



        NewUserRequestModel requestModel = new NewUserRequestModel();
        requestModel.firstname = name;
        requestModel.email = email;
        requestModel.country_mobile_code = COUNTRY_MOBILE_CODE_VALUE;
        requestModel.phone = phone;
        requestModel.referral_code = referral_code;
        requestModel.password = password;
        requestModel.team_name = team_name;
       // requestModel.user_id = userId;

        displayProgressBar(false, "Wait...");
        getWebRequestHelper().newUser(requestModel, this);
        if(ConstantsFlavor.type == ConstantsFlavor.Type.vision){
            AppCommon.getInstance(this).setUserLogin(requestModel.user_id , requestModel.password);
        }
    }

    private void callSocialLogin(SocialData socialData) {
        if (!socialData.isValidEmail()) {
            socialLogout();
            showErrorMsg("Email Id not available in your facebook account");
            return;
        }

        SocialLoginRequestModel requestModel = new SocialLoginRequestModel();
        requestModel.email = socialData.getEmail();
        requestModel.firstname = socialData.getFirstName();
        requestModel.lastname = socialData.getLastName();
        requestModel.social_type = socialData.getLoginFrom();
        requestModel.social_id = socialData.getId();

        displayProgressBar(false, "Wait...");
        getWebRequestHelper().socialLogin(requestModel, this);
    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        dismissProgressBar();
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_NEW_USER:
                handleRegisterResponse(webRequest);
                break;
            case ID_SOCIAL_LOGIN:
                handleSocialLoginResponse(webRequest);
                break;
            case ID_REFERRAL_SETTINGS:
                handleReferralSettingsResponse(webRequest);
                break;
            case ID_TEAM_NAME:
                handleReferralSettingsResponse(webRequest);
                break;
        }

    }

    private void handleReferralSettingsResponse(WebRequest webRequest) {
        ReferSettingResponseModel responsePojo = webRequest.getResponsePojo(ReferSettingResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            ReferralSettingsModel data = responsePojo.getData();
            if (data == null || isFinishing()) return;
            String new_registration = data.getNEW_REGISTRATION();
            if (isValidString(new_registration)) {
                String msg = String.format(getResources().getString(R.string.no_referral_code), new_registration);
                tv_referal_msg.setText(msg);
            }

        }
    }

    private void handleRegisterResponse(WebRequest webRequest) {
        NewUserResponseModel responsePojo = webRequest.getResponsePojo(NewUserResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            NewUserResponseModel.DataBean data = responsePojo.getData();
            if (data == null) return;
            NewUserRequestModel requestModel = webRequest.getExtraData(DATA);
            if (requestModel == null) return;
            Bundle bundle = new Bundle();
            bundle.putString(VERIFY_FROM, "V");
            bundle.putString(OTP, data.getOtp());
            bundle.putString(PHONE, data.getPhone());
            bundle.putString(COUNTRY_MOBILE_CODE, data.getCountry_mobile_code());
            bundle.putString(DATA, new Gson().toJson(requestModel));
            goToOtpVerifyActivity(bundle);
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }

    }

    private void handleSocialLoginResponse(WebRequest webRequest) {
        VerifyOtpResponseModel responsePojo = webRequest.getResponsePojo(VerifyOtpResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            UserModel data = responsePojo.getData();
            if (data == null) return;
            getUserPrefs().saveLoggedInUser(data);
            //showCustomToast(responsePojo.getMessage());
            goToDashboardActivity(null);
        } else {
            if (isFinishing()) return;
            socialLogout();
            showErrorMsg(responsePojo.getMessage());
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GplusLoginHandler.RC_SIGN_IN && resultCode != Activity.RESULT_CANCELED) {
            MyApplication.getInstance().getGplusLoginHandler().onActivityResult(data);
        } else if (requestCode == FacebookLoginHandler.fbLoginRequestCode()) {
            MyApplication.getInstance().getFacebookLoginHandler().onActivityResult(requestCode,
                    resultCode, data);
        }
    }


    /*/**
     * name - Get Suggested Team Name
     * url - /get_suggested_team_name
     * method - POST
     * params - reference(mandatory)
     * Header Params - lang(mandatory), device-id(mandatory), devicetype(mandatory), deviceinfo(mandatory), appinfo(mandatory)*/

}
