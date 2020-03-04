package com.app.appbase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.ui.main.webview.WebViewActivity;
import com.mlm.TreeView;
import com.R;
import com.app.model.AwsModel;
import com.app.model.ContestModel;
import com.app.model.CustomIconModel;
import com.app.model.PlayingHistoryModel;
import com.app.model.QuotationModel;
import com.app.model.ReferEarnModel;
import com.app.model.UserModel;
import com.app.model.webresponsemodel.QuotationsResponseModel;
import com.app.model.webresponsemodel.VerifyOtpResponseModel;
import com.app.preferences.UserPrefs;
import com.app.ui.checkuser.CheckUserActivity;
import com.app.ui.dialogs.ConfirmationDialog;
import com.app.ui.dialogs.ConfirmationLocationDialog;
import com.app.ui.dialogs.quotation.QuotationDialog;
import com.app.ui.forgotpassword.ForgotPasswordActivity;
import com.app.ui.forgotpassword.ForgotPasswordVerifyActivity;
import com.app.ui.login.LoginActivity;
import com.app.ui.main.ToolbarFragment;
import com.app.ui.main.addcash.AddCashActivity;
import com.app.ui.main.cricket.contest.ContestActivity;
import com.app.ui.main.cricket.contest.joinprivatecontest.JoinPrivateContestActivity;
import com.app.ui.main.cricket.dialogs.shareprivatecontest.SharePrivateContestDialog;
import com.app.ui.main.cricket.matchstats.MatchStatsActivity;
import com.app.ui.main.cricket.myteam.chooseteam.ChooseTeamActivity;
import com.app.ui.main.cricket.myteam.createteam.CreateTeamActivity;
import com.app.ui.main.cricket.teamstats.TeamStatsActivity;
import com.app.ui.main.dashboard.more.contestinvite.ContestInviteActivity;
import com.app.ui.main.dashboard.more.helpdesk.HelpDeskActivity;
import com.app.ui.main.dashboard.more.invite.InviteActivity;
import com.app.ui.main.dashboard.profile.MyProfileActivity;
import com.app.ui.main.dashboard.profile.MyProfileFragment;
import com.app.ui.main.dashboard.profile.myaccount.transactionhistory.TransactionHistoryActivity;
import com.app.ui.main.dashboard.profile.myaccount.withdrawlhistory.WithdrawHistoryActivity;
import com.app.ui.main.dashboard.profile.playerinfo.PlayerInfoActivity;
import com.app.ui.main.dashboard.profile.verification.VerificationActivity;
import com.app.ui.main.withdrawcash.WithdrawActivity;
import com.app.ui.optverifiction.OtpVerifyActivity;
import com.app.ui.register.RegisterActivity;
import com.app.ui.splash.SplashActivity;
import com.awss3.S3BucketHelper;
import com.base.BaseActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.crashlytics.android.Crashlytics;
import com.medy.retrofitwrapper.WebRequest;
import com.medy.retrofitwrapper.WebRequestErrorDialog;
import com.medy.retrofitwrapper.WebServiceException;
import com.medy.retrofitwrapper.WebServiceResponseListener;
import com.permissions.PermissionHelperNew;
import com.rest.WebRequestConstants;
import com.rest.WebRequestHelper;
import com.smsreceiver.SmsReceiver;

import java.util.ArrayList;
import java.util.List;


public abstract class AppBaseActivity extends BaseActivity
        implements WebServiceResponseListener, WebRequestConstants, SmsReceiver.SmsListener,
        UserPrefs.UserPrefsListener, ToolbarFragment.ToolbarFragmentListener {

    public static final int SHARE_BY_OTHERS = 0;
    public static final int SHARE_BY_WHATSAPP = 1;
    public static final int SHARE_BY_FACEBOOK = 2;
    public static final int SHARE_BY_SMS = 3;


    public static final int OTP_COUNTER = 1 * 60 * 1000;
    public String currency_symbol = "₹";
    ToolbarFragment toolbarFragment;
    private Dialog alertDialogProgressBar;
    private WebRequestErrorDialog errorMessageDialog;
    private SmsReceiver smsReceiver;

    public void registerSmsReceiver() {
        if (getSmsReceiver() != null) {
            IntentFilter filter = new IntentFilter();
            filter.setPriority(9999);
            filter.addAction(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
            registerReceiver(getSmsReceiver(), filter);
            printLog(SmsReceiver.class.getSimpleName(), "registerSmsReceiver");
        }
    }

    public void unregisterSmsReceiver() {
        if (getSmsReceiver() != null) {
            unregisterReceiver(getSmsReceiver());
            printLog(SmsReceiver.class.getSimpleName(), "unregisterSmsReceiver");
        }
    }

    @Override
    public void beforeSetContentView() {
        super.beforeSetContentView();
        setupActionBar();
    }

    @Override
    public void initializeComponent() {
        smsReceiver = new SmsReceiver(getResources().getString(R.string.sms_sender), this);
        currency_symbol = getResources().getString(R.string.currency_symbol) + " ";
        UserModel userModel = getUserModel();

        if (userModel == null) {
            Crashlytics.setUserIdentifier("");
            Crashlytics.setUserName("");
            Crashlytics.setUserEmail("");
        } else {
            Crashlytics.setUserIdentifier(String.valueOf(userModel.getSlug()));
            Crashlytics.setUserName(userModel.getFullName());
            Crashlytics.setUserEmail(userModel.getFullMobile());
        }


    }

    public void setupActionBar() {
        if ((this instanceof SplashActivity)) {
            setLightStatusBar(false);
            setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            setNavigationBarColor(getResources().getColor(R.color.splashStatusBarColor));
        } else if ((this instanceof CheckUserActivity) ||
                (this instanceof OtpVerifyActivity) ||
                (this instanceof LoginActivity) ||
                (this instanceof RegisterActivity) ||
                (this instanceof ForgotPasswordActivity) ||
                (this instanceof ForgotPasswordVerifyActivity)) {
            setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            setLightStatusBar(false);
            setNavigationBarColor(getResources().getColor(R.color.colorScreenBg));
        } else {
            setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            setLightStatusBar(false);
            setNavigationBarColor(getResources().getColor(R.color.colorScreenBg));

        }
    }

    public void setLightStatusBar() {
        setLightStatusBar(false);
        setWindowUnderStatusBar();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View view = getWindow().getDecorView();
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            setStatusBarColor(getResources().getColor(R.color.transparent_color));
            setNavigationBarColor(getResources().getColor(R.color.colorScreenBg));
        }
    }


    @Override
    public void onWebRequestCall(WebRequest webRequest) {
        ((AppBaseApplication) getApplication()).onWebRequestCall(webRequest);
    }

    @Override
    public void onWebRequestPreResponse(WebRequest webRequest) {

    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        ((AppBaseApplication) getApplication()).onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        showNetworkErrorDialog(webRequest);



        switch (webRequest.getWebRequestId()) {
            case ID_GET_PROFILE:
                handleGetProfileResponse(webRequest);
                break;
            case ID_GET_QUOTATIONS:
                handleQuotationsResponse(webRequest);
                break;
        }
    }

    public void showNetworkErrorDialog(WebRequest webRequest){
        if(webRequest==null)return;
        Exception webRequestException = webRequest.getWebRequestException();
        if(webRequestException !=null){
            if(webRequestException instanceof WebServiceException){
                WebServiceException webRequestException1 = (WebServiceException) webRequestException;
                if(webRequestException1.getCode()==WebServiceException.INTERNET_NOT_AVAILABLE){
                    showNetWorkErrorMessage();
                }
            }else if(webRequestException instanceof IllegalStateException){
                String message = webRequestException.getMessage();
                if(message.contains("Failed to connect to")){
                    showNetWorkErrorMessage();
                }
            }
        }
    }
    public void showNetWorkErrorMessage(){
        showErrorMsg("Oops your internet seems to be on power nap. Please check your internet settings");
    }

    public void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }

    public void setNavigationBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(color);
        }
    }

    public void setLightStatusBar(boolean isLight) {
        if (isLight) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    public void setLightNavigationBar(boolean isLight) {
        if (isLight) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            }
        }
    }

    public void setWindowUnderStatusBar() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public void onToolbarItemClick(View view) {

    }

    public void displayProgressBar(boolean isCancellable) {
        displayProgressBar(isCancellable, "");
    }

    public void displayProgressBar(boolean isCancellable, String loaderMsg) {
        dismissProgressBar();
        if (isFinishing()) return;
        alertDialogProgressBar = new Dialog(this,
                R.style.DialogWait);
        alertDialogProgressBar.setCancelable(isCancellable);
        alertDialogProgressBar
                .requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialogProgressBar.setContentView(R.layout.dialog_wait);
        TextView tv_loader_msg = alertDialogProgressBar.findViewById(R.id.tv_loader_msg);
        if (loaderMsg != null && !loaderMsg.trim().isEmpty()) {
            tv_loader_msg.setText(loaderMsg);
        } else {
            tv_loader_msg.setVisibility(View.GONE);
        }

        try {
            alertDialogProgressBar.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));
            alertDialogProgressBar.show();

        } catch (Exception ignore) {

        }
    }

    public void dismissProgressBar() {
        try {
            if (!isFinishing() && alertDialogProgressBar != null) {
                alertDialogProgressBar.dismiss();
            }
        } catch (Exception ignore) {

        }
    }


    public void showErrorMsg(String msg) {
        if (!isFinishing()) {
            if (errorMessageDialog == null) {
                errorMessageDialog = new WebRequestErrorDialog(this, msg) {
                    @Override
                    public int getLayoutResourceId() {
                        return R.layout.dialog_error;
                    }

                    @Override
                    public int getMessageTextViewId() {
                        return R.id.tv_message;
                    }

                    @Override
                    public int getDismissBtnTextViewId() {
                        return R.id.tv_ok;
                    }

                    @Override
                    public int getBackGroundDrawableResource() {
                        return R.drawable.bg_appblack;
                    }
                };
            } else if (errorMessageDialog.isShowing()) {
                errorMessageDialog.dismiss();
            }
            errorMessageDialog.setMsg(msg);
            errorMessageDialog.show();
        }


    }


    public WebRequestHelper getWebRequestHelper() {
        return AppBaseApplication.getInstance().getWebRequestHelper();
    }

    public SmsReceiver getSmsReceiver() {
        return smsReceiver;
    }


    @Override
    public void otpMessageReceived(String messageText) {
        Log.e("AAgkfjgfjg", messageText);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void userLoggedIn(UserModel userModel) {

    }

    @Override
    public void loggedInUserUpdate(UserModel userModel) {

    }

    @Override
    public void loggedInUserClear() {

    }

    @Override
    public void customIconUpdate(CustomIconModel customIconModel) {

    }

    public void updateUserInPrefs() {
        if (getApplication() != null && getApplication() instanceof AppBaseApplication) {
            ((AppBaseApplication) getApplication()).updateUserInPrefs();
        }
    }

    public UserPrefs getUserPrefs() {
        if (getApplication() != null && getApplication() instanceof AppBaseApplication) {
            return ((AppBaseApplication) getApplication()).getUserPrefs();
        }
        return null;
    }

    public UserModel getUserModel() {
        if (getApplication() != null && getApplication() instanceof AppBaseApplication) {
            return ((AppBaseApplication) getApplication()).getUserModel();
        }
        return null;
    }

    public ReferEarnModel getReferEarnModel() {
        if (getApplication() != null && getApplication() instanceof AppBaseApplication) {
            return ((AppBaseApplication) getApplication()).getReferEarnModel();
        }
        return null;
    }

    public PlayingHistoryModel getPlayingHistoryModel() {
        if (getApplication() != null && getApplication() instanceof AppBaseApplication) {
            return ((AppBaseApplication) getApplication()).getPlayingHistoryModel();
        }
        return null;
    }

    public boolean isPlayServiceUpdated() {
        if (getApplication() != null && getApplication() instanceof AppBaseApplication) {
            return ((AppBaseApplication) getApplication()).isPlayServiceUpdated();
        }
        return false;
    }

    public boolean isPlayServiceErrorUserResolvable() {
        if (getApplication() != null && getApplication() instanceof AppBaseApplication) {
            return ((AppBaseApplication) getApplication()).isPlayServiceErrorUserResolvable(this);
        }
        return false;
    }

    public void loadImage(Object mContext, ImageView imageView,
                          final ProgressBar pb_image, String imageUrl, int placeHolder) {
        this.loadImage(mContext, imageView, pb_image, imageUrl, placeHolder, placeHolder, placeHolder, 200);
    }

    public void loadImage(Object mContext, ImageView imageView,
                          final ProgressBar pb_image, String imageUrl, int placeHolder, int requireWidth) {
        this.loadImage(mContext, imageView, pb_image, imageUrl, placeHolder, placeHolder, placeHolder, requireWidth);
    }

    public void loadImage(Object mContext, ImageView imageView,
                          final ProgressBar pb_image, String imageUrl,
                          int placeHolder, int error, int fallBack, int requireWidth) {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            if (pb_image != null && pb_image.getVisibility() != View.INVISIBLE) {
                pb_image.setVisibility(View.INVISIBLE);
            }
            imageView.setImageResource(fallBack);
            return;
        }
        if (mContext == null) return;

        try {
            RequestManager requestManager = null;
            if (mContext instanceof Activity) {
                requestManager = Glide.with((Activity) mContext);
            } else if (mContext instanceof Fragment) {
                requestManager = Glide.with(((Fragment) mContext).getActivity());
            } else {
                requestManager = Glide.with((Context) mContext);
            }
            if (requestManager == null) return;
            RequestOptions options = new RequestOptions()
                    .placeholder(placeHolder)
                    .fallback(error);
            if (requireWidth > 0) {
                options = options.override(requireWidth);
            }

            options = options.error(error)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .priority(Priority.HIGH);

            if (pb_image != null && pb_image.getVisibility() != View.VISIBLE) {
                pb_image.setVisibility(View.VISIBLE);
            }
            requestManager.asBitmap().load(imageUrl).
                    apply(options).
                    listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            if (pb_image != null && pb_image.getVisibility() != View.INVISIBLE) {
                                pb_image.setVisibility(View.INVISIBLE);
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            if (pb_image != null && pb_image.getVisibility() != View.INVISIBLE) {
                                pb_image.setVisibility(View.INVISIBLE);
                            }
                            return false;
                        }
                    }).into(imageView);

        } catch (Exception e) {

        }

    }


    public boolean copyToClipboard(String text) {
        try {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData
                    .newPlainText(getResources().getString(R.string.app_name), text);
            clipboard.setPrimaryClip(clip);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void makeCall(final String number) {
        if (PermissionHelperNew.needCallPermission(this, new PermissionHelperNew.OnSpecificPermissionGranted() {
            @Override
            public void onPermissionGranted(boolean isGranted, boolean withNeverAsk, String permission, int requestCode) {
                if (requestCode == PermissionHelperNew.CALL_PERMISSION_REQUEST_CODE) {
                    if (isGranted) {
                        makeDirectCall(number);
                    } else {
                        if (withNeverAsk) {
                            PermissionHelperNew.showNeverAskAlert(AppBaseActivity.this, false, requestCode);
                        } else {
                            PermissionHelperNew.showSpecificDenyAlert(AppBaseActivity.this, permission, requestCode, false);
                        }
                    }
                }
            }
        })) ;
        if (PermissionHelperNew.hasCallPermission(this)) {
            makeDirectCall(number);
        }
    }

    @SuppressLint("MissingPermission")
    public void makeDirectCall(String number) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + number));
            startActivity(callIntent);
        } catch (ActivityNotFoundException e) {
            showCustomToast("No app found for call.");
        }
    }


    public void callGetProfile() {
        if (getUserModel() == null) return;
        getWebRequestHelper().getProfile(this);
    }

    private void handleGetProfileResponse(WebRequest webRequest) {
        VerifyOtpResponseModel responsePojo = webRequest.getResponsePojoSimple(VerifyOtpResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            UserModel data = responsePojo.getData();
            if (data == null) return;
            if (isFinishing()) return;
            getUserPrefs().updateLoggedInUser(data);
        }

    }

    private void handleQuotationsResponse(WebRequest webRequest) {
        QuotationsResponseModel responsePojo = webRequest.getResponsePojo(QuotationsResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            QuotationModel data = responsePojo.getQuotation();
            if (data == null || !data.isValidQuotation()) return;
            if (isFinishing()) return;
            try {
                QuotationDialog quotationDialog = QuotationDialog.getInstance(null);
                quotationDialog.setQuotationModel(data);
                quotationDialog.show(getFm(), quotationDialog.getClass().getSimpleName());
            } catch (Exception ignore) {

            }
        }
    }


    public S3BucketHelper getS3Helper() {
        if (getUserModel() != null && getUserModel().getAws() != null) {
            AwsModel aws = getUserModel().getAws();
            return new S3BucketHelper(this,
                    aws.getAWS_KEY(), aws.getAWS_SECRET(), aws.getAWS_REGION());
        }
        return null;
    }

    public void goToVerificationActivity(Bundle bundle) {
        Intent intent = new Intent(this, VerificationActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (isFinishing()) return;
        startActivity(intent);
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    public void goToAddCashActivity(Bundle bundle, int requestCode) {
        Intent intent = new Intent(this, AddCashActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (isFinishing()) return;
        if (requestCode > 0) {
            startActivityForResult(intent, requestCode);
        } else {
            startActivity(intent);
        }
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    public void goToWithdrawActivity(Bundle bundle) {
        UserModel userModel = getUserModel();
        if (userModel != null) {
            if (userModel.isWithdrawAvailable()) {
                Intent intent = new Intent(this, WithdrawActivity.class);
                if (bundle != null) {
                    intent.putExtras(bundle);
                }
                if (isFinishing()) return;
                startActivity(intent);
                overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
            } else {
                showConfirmationDialog();
                //goToVerificationActivity(null);
            }
        }
    }

    private void showConfirmationDialog() {
        Bundle bundle = new Bundle();
        bundle.putString(MESSAGE, "By joining this contest, you accept Imuons T&amp;C and confirm that you are not a resident of Assam, Odissa, Telangana, Nagaland or Sikkim.");
        bundle.putString(POS_BTN, "OK");
        bundle.putString(NEG_BTN, "NO");
        ConfirmationLocationDialog instance = ConfirmationLocationDialog.getInstance(bundle);
        instance.setOnClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        goToVerificationActivity(null);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        });
        instance.show(getFm(), instance.getClass().getSimpleName());
    }

    public void goToTransactionHistoryActivity(Bundle bundle) {
        Intent intent = new Intent(this, TransactionHistoryActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (isFinishing()) return;
        startActivity(intent);
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    public void goToWithdrawHistoryActivity(Bundle bundle) {
        Intent intent = new Intent(this, WithdrawHistoryActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (isFinishing()) return;
        startActivity(intent);
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }


    public boolean checkContestJoinAvailable(ContestModel contestModel) {
        if (contestModel == null) return false;
        UserModel userModel = getUserModel();
        if (userModel == null) return false;
        if (contestModel.getTotal_price() > 0 || contestModel.getEntry_fees() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            boolean isEmailAdded = isValidString(userModel.getEmail());
            boolean isPhoneAdded = isValidString(userModel.getPhone());
            boolean isDobUpdated = userModel.getDob() != 0;
            boolean stateUpdated = userModel.getState() != null;
            if (isEmailAdded && isPhoneAdded && isDobUpdated && stateUpdated) {
                return true;
            }
            if (!isEmailAdded) {
                stringBuilder.append("Update Email address.").append("\n").append("\n");
            }
            if (!isPhoneAdded) {
                stringBuilder.append("Update Mobile number.").append("\n").append("\n");
            }

            if (stringBuilder.length() == 0) {
                stringBuilder.append("\n");
            }
            if (userModel.getSettings() != null) {
                stringBuilder.append(userModel.getSettings().getPROFILE_UPDATE_MESSAGE());
            }

            showUpdateProfileDialog(stringBuilder.toString());
            return false;
        } else {
            return true;
        }
    }

    private void showUpdateProfileDialog(String msg) {
        Bundle bundle = new Bundle();
        bundle.putString(MESSAGE, msg);
        bundle.putString(POS_BTN, "GO AHEAD ");
        bundle.putString(NEG_BTN, "CANCEL");
        ConfirmationDialog instance = ConfirmationDialog.getInstance(bundle);
        instance.setOnClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        Bundle bundle1 = new Bundle();
                        bundle1.putBoolean(DATA, true);
                        goToPlayerInfoActivity(bundle1);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        });
        instance.show(getFm(), instance.getClass().getSimpleName());
    }

    ConfirmationDialog matchExpireDialog;

    public void showMatchExpireDialog() {
        try {
            if (matchExpireDialog != null && matchExpireDialog.getDialog() != null && matchExpireDialog.getDialog().isShowing()) {
                return;
            }
            String msg = "<html><b>The deadline has passed!</b><br/><br/>Check out the contests you've joined for this match</html>";
            Bundle bundle = new Bundle();
            bundle.putString(MESSAGE, msg);
            bundle.putString(POS_BTN, "OK");
            bundle.putString(NEG_BTN, "");
            matchExpireDialog = ConfirmationDialog.getInstance(bundle);
            matchExpireDialog.setOnClickListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                matchExpireDialog.dismiss();
                                finish();
                                break;
                        }
                    } catch (Exception e) {

                    }
                }
            });
            matchExpireDialog.show(getFm(), matchExpireDialog.getClass().getSimpleName());
        }catch (Exception e){

        }
    }


    public void goToPlayerInfoActivity(Bundle bundle) {
        Intent intent = new Intent(this, PlayerInfoActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (isFinishing()) return;
        startActivityForResult(intent, MyProfileFragment.REQUEST_UPDATE_PROFILE);
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    public void goToCreateTeamActivity(Bundle bundle) {
        Intent intent = new Intent(this, CreateTeamActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (isFinishing()) return;
        startActivityForResult(intent, ContestActivity.REQUEST_CREATE_TEAM);
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }


    public void goToJoinPrivateContestActivity(Bundle bundle) {
        Intent intent = new Intent(this, JoinPrivateContestActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    public void goToChooseTeamActivity(Bundle bundle) {
        Intent intent = new Intent(this, ChooseTeamActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, ContestActivity.REQUEST_JOIN_CONTEST);
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    public void goToMatchStatsActivity(Bundle bundle) {
        Intent intent = new Intent(this, MatchStatsActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    public void goToTeamStatsActivity(Bundle bundle) {
        Intent intent = new Intent(this, TeamStatsActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }


    public void goToSharePrivateContestDialog(Bundle bundle) {
        SharePrivateContestDialog intent = SharePrivateContestDialog.getInstance(bundle);

        if (isFinishing()) return;
        intent.show(getFm(), intent.getClass().getSimpleName());
    }



    public void gotoMyProfileActivity(Bundle bundle) {
        Intent intent = new Intent(this, MyProfileActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }



    public void goToInviteActivity(Bundle bundle) {
        Intent intent = new Intent(this, InviteActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    public void goToContestInviteActivity(Bundle bundle) {
        Intent intent = new Intent(this, ContestInviteActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    public void goToHelpDeskActivity(Bundle bundle) {
        Intent intent = new Intent(this, HelpDeskActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    public void goToWebViewActivity(Bundle bundle) {
        Intent intent = new Intent(this, WebViewActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (isFinishing()) return;
        startActivity(intent);
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }


    public void shareContent(Context context, String shareBodyText, int shareBy) {
        final String appName = context.getString(R.string.app_name);
        if (shareBy == SHARE_BY_OTHERS) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, appName);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
            try {
                context.startActivity(Intent.createChooser(shareIntent, "Share"));
            } catch (ActivityNotFoundException e) {
                showCustomToast("No Apps found please install app from PlayStore.");
            }
        } else if (shareBy == SHARE_BY_WHATSAPP) {
            if (!shareBySpecificApp(context, shareBodyText, "com.whatsapp")) {
                showCustomToast("WhatsApp not found. Please install fom PlayStore.");
            }
        } else if (shareBy == SHARE_BY_FACEBOOK) {
            if (!shareBySpecificApp(context, shareBodyText, "com.facebook.katana")) {
                showCustomToast("Facebook not found. Please install fom PlayStore.");
            }
        } else if (shareBy == SHARE_BY_SMS) {
            if (!shareBySms(context, shareBodyText)) {
                showCustomToast("No sms app found. Please install fom PlayStore.");
            }
        }
    }


    public boolean shareBySpecificApp(Context context, String shareBodyText, String shareByPackage) {
        String appName = context.getString(R.string.app_name);
        List<Intent> targetedShareIntents = new ArrayList<Intent>();
        Intent intent = new Intent();
        intent.setType("text/plain");
        intent.setAction(Intent.ACTION_SEND);
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        if (!resInfo.isEmpty()) {
            for (ResolveInfo resolveInfo : resInfo) {
                String packageName = resolveInfo.activityInfo.packageName;
                if (packageName.equals(shareByPackage)) {
                    Intent targetedShareIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    targetedShareIntent.setType("text/plain");
                    targetedShareIntent.setPackage(packageName);
                    targetedShareIntent.setClassName(
                            resolveInfo.activityInfo.packageName,
                            resolveInfo.activityInfo.name);
                    targetedShareIntent.putExtra(Intent.EXTRA_SUBJECT, appName);
                    targetedShareIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
                    targetedShareIntents.add(targetedShareIntent);
                }
            }
        }
        if (targetedShareIntents.size() > 0) {
            try {
                Intent chooserIntent = Intent.createChooser(targetedShareIntents.get(0), "Select an action");
                startActivity(chooserIntent);
                return true;
            } catch (ActivityNotFoundException e) {

            }
        }
        return false;

    }

    public boolean shareBySms(Context context, String text) {
        Uri uri = Uri.parse("sms:");
        Intent intent = new Intent();
        intent.setData(uri);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.putExtra("sms_body", text);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setAction(Intent.ACTION_SENDTO);
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context);
            if (defaultSmsPackageName != null) {
                intent.setPackage(defaultSmsPackageName);
            }
        } else {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setType("vnd.android-dir/mms-sms");
        }

        try {
            context.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {

        }
        return false;
    }


}
