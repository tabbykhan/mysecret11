package com.app.ui.dialogs.paymentdialog;

import android.app.Dialog;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseDialogFragment;
import com.app.model.UserModel;
import com.app.model.webrequestmodel.DepositAmountRequestModel;
import com.app.model.webresponsemodel.DepositWalletResponseModel;
import com.app.model.webresponsemodel.WalletResponseModel;
import com.app.ui.MyApplication;
import com.base.BaseFragment;
import com.google.gson.Gson;
import com.medy.retrofitwrapper.WebRequest;
import com.rest.WebServices;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;


public class PaymentDialog extends AppBaseDialogFragment {

    private static final String TAG = PaymentDialog.class.getSimpleName();
    private String RETURN_URL = "";
    private String NOTIFY_URL = "";

    private DepositAmountRequestModel depositAmountRequestModel;
    private String walletRechargeResponse;
    private PaymentSuccessListener paymentSuccessListener;

    ImageView iv_back;
    TextView tv_title_left;
    WebView web_view;


    public void setPaymentSuccessListener(PaymentSuccessListener paymentSuccessListener) {
        this.paymentSuccessListener = paymentSuccessListener;
    }

    public void setDepositAmountRequestModel(DepositAmountRequestModel depositAmountRequestModel) {
        this.depositAmountRequestModel = depositAmountRequestModel;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.dialog_payment;
    }

    @Override
    public int getFragmentContainerResourceId(BaseFragment baseFragment) {
        return -1;
    }


    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        WindowManager.LayoutParams wlmp = dialog.getWindow().getAttributes();
        wlmp.gravity = Gravity.CENTER;
        wlmp.height = WindowManager.LayoutParams.MATCH_PARENT;
        wlmp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlmp.dimAmount = 0.0f;

        setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();

        iv_back = getView().findViewById(R.id.iv_back);
        tv_title_left = getView().findViewById(R.id.tv_title_left);
        web_view = getView().findViewById(R.id.web_view);

        iv_back.setOnClickListener(this);
        setupWebViewsettings();
        callWalletRecharge();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                dismiss();
                break;
        }
    }


    private void setupWebViewsettings() {
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.getSettings().setSupportMultipleWindows(false);
        web_view.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        web_view.getSettings().setDomStorageEnabled(true);

        web_view.addJavascriptInterface(new ResponseHandleInterface(), ResponseHandleInterface.NAME);

        web_view.setWebViewClient(new WebViewClient() {

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (url.equals(WebServices.WalletRecharge())) {
                    return generateResponseFromString();
                }
                return super.shouldInterceptRequest(view, url);
            }


            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.equals(WebServices.WalletRecharge())) {
                    return generateResponseFromString();
                }

                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if (errorCode == -2 && getActivity()!=null) {
                    ((AppBaseActivity) getActivity()).showNetWorkErrorMessage();
                    dismiss();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                MyApplication.getInstance().printLog(TAG, "onPageFinished = " + url);
                if (url.equals(WebServices.WalletRecharge())) {
                    view.loadUrl("javascript:window." + ResponseHandleInterface.NAME +
                            ".handleResponse2(document.getElementById('notify_url').value);");
                    view.loadUrl("javascript:window." + ResponseHandleInterface.NAME +
                            ".handleResponse1(document.getElementById('return_url').value);");
                } else if (url.equals(RETURN_URL)) {
                    view.loadUrl("javascript:window." + ResponseHandleInterface.NAME +
                            ".handleResponse(document.getElementsByTagName('body')[0].innerText);");
                }
            }
        });
    }

    private WebResourceResponse generateResponseFromString() {


        InputStream inputStream = new ByteArrayInputStream(walletRechargeResponse.getBytes(Charset.forName("UTF-8")));

        return new WebResourceResponse(
                "text/html",
                "utf-8",
                inputStream);
    }

    private void startPayment() {
        UserModel userModel = getUserModel();
        if (userModel != null) {
            web_view.loadUrl(WebServices.WalletRecharge());
        }
    }

    private void callWalletRecharge() {
        displayProgressBar(false);
        getWebRequestHelper().walletRecharge(depositAmountRequestModel, this);
    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        dismissProgressBar();
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_DEPOSIT_AMOUNT:
                handleWalletRechargeResponse(webRequest);
                break;
        }

    }


    private void handleWalletRechargeResponse(WebRequest webRequest) {
        try {
            WalletResponseModel responsePojo = webRequest.getResponsePojo(WalletResponseModel.class);
            if (responsePojo != null && responsePojo.isError()) {
                dismiss();
                return;
            }else {
                walletRechargeResponse = webRequest.getResponseString();
                if (isValidString(walletRechargeResponse)) {
                    startPayment();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    class ResponseHandleInterface {

        public static final String NAME = "RESPONSE_HANDLER";

        public ResponseHandleInterface() {
        }

        @SuppressWarnings("unused")
        @JavascriptInterface
        public void handleResponse(String response) {
            MyApplication.getInstance().printLog(TAG, response);
            DepositWalletResponseModel responseModel = new Gson().fromJson(response, DepositWalletResponseModel.class);
            if (paymentSuccessListener != null)
                paymentSuccessListener.onPaymentResponse(responseModel);
        }

        @JavascriptInterface
        public void handleResponse1(String response) {
            RETURN_URL = response;
            MyApplication.getInstance().printLog(TAG, "RETURN_URL=" + RETURN_URL);
        }

        @JavascriptInterface
        public void handleResponse2(String response) {
            NOTIFY_URL = response;
            MyApplication.getInstance().printLog(TAG, "NOTIFY_URL=" + NOTIFY_URL);
        }
    }


    public interface PaymentSuccessListener {
        void onPaymentResponse(DepositWalletResponseModel responseModel);
    }

    @Override
    public boolean handleOnBackPress() {
        if (web_view.canGoBack()) {
            web_view.goBack();
            return true;
        }
        return false;
    }
}
