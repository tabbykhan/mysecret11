package com.app.appbase;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.R;
import com.app.model.CustomIconModel;
import com.app.model.PlayingHistoryModel;
import com.app.model.ReferEarnModel;
import com.app.model.UserModel;
import com.app.preferences.UserPrefs;
import com.awss3.S3BucketHelper;
import com.base.BaseFragment;
import com.medy.retrofitwrapper.WebRequest;
import com.medy.retrofitwrapper.WebRequestErrorDialog;
import com.medy.retrofitwrapper.WebServiceResponseListener;
import com.rest.WebRequestConstants;
import com.rest.WebRequestHelper;


public abstract class AppBaseFragment extends BaseFragment
        implements WebServiceResponseListener, WebRequestConstants, UserPrefs.UserPrefsListener{

    public String currency_symbol = "";
    private Dialog alertDialogProgressBar;
    private WebRequestErrorDialog errorMessageDialog;


    @Override
    public void initializeComponent() {
        currency_symbol = getResources().getString(R.string.currency_symbol) + " ";
    }


    @Override
    public void onWebRequestCall(WebRequest webRequest) {
        if (getActivity() != null) {
            ((AppBaseActivity) getActivity()).onWebRequestCall(webRequest);
        }
    }

    public void onPageSelected() {

    }

    @Override
    public void onWebRequestPreResponse(WebRequest webRequest) {

    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        if (getActivity() != null) {
            ((AppBaseActivity) getActivity()).onWebRequestResponse(webRequest);
        }
    }

    public void displayProgressBar(boolean isCancellable) {
        displayProgressBar(isCancellable, "");
    }

    public void displayProgressBar(boolean isCancellable, String loaderMsg) {
        dismissProgressBar();
        if (getActivity() == null) return;
        alertDialogProgressBar = new Dialog(getActivity(),
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
            if (getActivity() != null && alertDialogProgressBar != null && alertDialogProgressBar.isShowing()) {
                alertDialogProgressBar.dismiss();
            }
        } catch (Exception ignore) {

        }
    }

    public void showErrorMsg(String msg) {

        if (isValidActivity() && isVisible()) {
            if (errorMessageDialog == null) {
                errorMessageDialog = new WebRequestErrorDialog(getActivity(), msg) {
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
            try {
                errorMessageDialog.setMsg(msg);
                errorMessageDialog.show();
            } catch (Exception ignore) {

            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismissProgressBar();
    }


    public WebRequestHelper getWebRequestHelper() {
        if (getActivity() == null) return null;
        return ((AppBaseActivity) getActivity()).getWebRequestHelper();
    }

    public S3BucketHelper getS3Helper() {
        if (getActivity() == null) return null;
        return ((AppBaseActivity) getActivity()).getS3Helper();
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

    public UserPrefs getUserPrefs() {
        if (getActivity() != null && getActivity().getApplication() != null && getActivity().getApplication() instanceof AppBaseApplication) {
            return ((AppBaseApplication) getActivity().getApplication()).getUserPrefs();
        }
        return null;
    }

    public UserModel getUserModel() {
        if (getActivity() != null && getActivity().getApplication() != null && getActivity().getApplication() instanceof AppBaseApplication) {
            return ((AppBaseApplication) getActivity().getApplication()).getUserModel();
        }
        return null;
    }

    public ReferEarnModel getReferEarnModel() {
        if (getActivity() != null && getActivity().getApplication() != null && getActivity().getApplication() instanceof AppBaseApplication) {
            return ((AppBaseApplication) getActivity().getApplication()).getReferEarnModel();
        }
        return null;
    }

    public PlayingHistoryModel getPlayingHistoryModel() {
        if (getActivity() != null && getActivity().getApplication() != null && getActivity().getApplication() instanceof AppBaseApplication) {
            return ((AppBaseApplication) getActivity().getApplication()).getPlayingHistoryModel();
        }
        return null;
    }


    public void goToWebViewActivity(Bundle bundle) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            ((AppBaseActivity) getActivity()).goToWebViewActivity(bundle);
        }
    }

}
