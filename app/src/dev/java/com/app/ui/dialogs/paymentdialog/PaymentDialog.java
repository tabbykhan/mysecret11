package com.app.ui.dialogs.paymentdialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
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
import com.app.ui.dialogs.ConfirmationDialog;
import com.base.BaseFragment;
import com.google.gson.Gson;
import com.medy.retrofitwrapper.WebRequest;
import com.rest.WebServices;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


public class PaymentDialog extends AppBaseDialogFragment {

    private static final String TAG = PaymentDialog.class.getSimpleName();
    ImageView iv_back;
    TextView tv_title_left;
    WebView web_view;
    private String RETURN_URL = "return_wallet_recharge";
    private String NOTIFY_URL = "";
    private DepositAmountRequestModel depositAmountRequestModel;
    private String walletRechargeResponse;
    private PaymentSuccessListener paymentSuccessListener;

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
        //setWeb_view();
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

    private void setWeb_view() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            web_view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            web_view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.getSettings().setDomStorageEnabled(true);
        web_view.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        web_view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        web_view.addJavascriptInterface(new ResponseHandleInterface(), ResponseHandleInterface.NAME);
        web_view.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.i("shule override", request.getUrl().toString());
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("string override", url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.i("onPageStarted", url);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                MyApplication.getInstance().printLog(TAG, "onPageFinished = " + url);
                if (url.equals(WebServices.cashfree_wallet())) {
                    view.loadUrl("javascript:window." + ResponseHandleInterface.NAME + ".handleResponse2(document.getElementById('notify_url').value);");
                    view.loadUrl("javascript:window." + ResponseHandleInterface.NAME + ".handleResponse1(document.getElementById('return_url').value);");
                } else if (url.equals(RETURN_URL)) {
                    view.loadUrl("javascript:window." + ResponseHandleInterface.NAME + ".handleResponse(document.getElementsByTagName('body')[0].innerText);");
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if (errorCode == -2) {
                    web_view.loadData("<html>Oops your internet seems to be on power nap.<br/>Please " + "check your internet settings</html>", "text/html", null);
                    dismiss();
                }

                //
            }
        });

        web_view.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                // displayProgressBar(false);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.i("onPageStarted", title);
            }
        });

    }


    private void setupWebViewsettings() {
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.getSettings().setSupportMultipleWindows(false);
        web_view.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        web_view.getSettings().setDomStorageEnabled(true);

        web_view.addJavascriptInterface(new ResponseHandleInterface(), ResponseHandleInterface.NAME);

        web_view.setWebViewClient(new WebViewClient() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                Log.i("shouldInterceptRequest", url);
                if (url.equals(WebServices.cashfree_wallet())) {
                    return generateResponseFromString();
                }
                return super.shouldInterceptRequest(view, url);
            }


            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                Log.i("shouldIntercept 1", url);
                if (url.equals(WebServices.cashfree_wallet())) {
                    return generateResponseFromString();
                }

                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if (errorCode == -2 && getActivity() != null) {
                    ((AppBaseActivity) getActivity()).showNetWorkErrorMessage();
                    dismiss();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                MyApplication.getInstance().printLog(TAG, "onPageFinished = " + url);

                if (url.equals(WebServices.cashfree_wallet())) {
                    Log.i("onPageFinished", url);
                    view.loadUrl("javascript:window." + ResponseHandleInterface.NAME + ".handleResponse2(document.getElementById('notify_url').value);");
                    view.loadUrl("javascript:window." + ResponseHandleInterface.NAME + ".handleResponse1(document.getElementById('return_url').value);");
                } else if (url.contains(RETURN_URL)) {
                    Log.i("onPageFinished else", url);
                    showResultData("Deposit added successfully...!");
                    view.loadUrl("javascript:window." + ResponseHandleInterface.NAME + ".handleResponse(document.getElementsByTagName('body')[0].innerText);");
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private WebResourceResponse generateResponseFromString() {
        WebResourceResponse resourceResponse;

        InputStream inputStream = new ByteArrayInputStream(walletRechargeResponse.getBytes(StandardCharsets.UTF_8));
        Log.i("Web response", inputStream.toString());
        resourceResponse = new WebResourceResponse("text/html", "utf-8", inputStream);
        Log.i("resource response", resourceResponse.getReasonPhrase());
        return resourceResponse;
    }

    private void startPayment() {
        UserModel userModel = getUserModel();
        if (userModel != null) {
            Log.i("add amount 2", "1");
            //  web_view.loadUrl(WebServices.WalletRecharge());

            // web_view.loadUrl(WebServices.wallet_recharge());
            web_view.loadData(walletRechargeResponse, "text/html", "UTF-8");

        }
    }

    private void callWalletRecharge() {
        displayProgressBar(false);
        // getWebRequestHelper().walletRecharge(depositAmountRequestModel, this);
        getWebRequestHelper().walletAddBalance(depositAmountRequestModel, this);
    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        dismissProgressBar();
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412)
            return;
        switch (webRequest.getWebRequestId()) {
            case ID_DEPOSIT_AMOUNT:
                handleWalletRechargeResponse(webRequest);
                break;
        }

    }


    private void handleWalletRechargeResponse(WebRequest webRequest) {
        try {

            WalletResponseModel responsePojo = webRequest.getResponsePojo(WalletResponseModel.class);
            if (responsePojo != null) {
                Log.i("add amount", webRequest.getResponseString());
                walletRechargeResponse = webRequest.getResponseString();
                if (isValidString(walletRechargeResponse)) {
                    Log.i("add amount 1", "1");
                    startPayment();
                }
            } else if(webRequest.getResponseString()!=null){
                walletRechargeResponse = webRequest.getResponseString();
                if (isValidString(walletRechargeResponse)) {
                    Log.i("add amount 3", "1");
                    startPayment();
                }

            }else{
                dismiss();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean handleOnBackPress() {
        if (web_view.canGoBack()) {
            web_view.goBack();
            return true;
        }
        return false;
    }

    private void showResultData(String resulte) {
        Bundle bundle = new Bundle();
        bundle.putString(MESSAGE, resulte);
        bundle.putString(POS_BTN, "Ok");
        //  bundle.putString(NEG_BTN, "NO");
        ConfirmationDialog instance = ConfirmationDialog.getInstance(bundle);
        instance.setOnClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        dismiss();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        dismiss();
                        break;
                }
            }
        });
        instance.show(getChildFm(), instance.getClass().getSimpleName());
    }

    public interface PaymentSuccessListener {
        void onPaymentResponse(DepositWalletResponseModel responseModel);
    }

    class ResponseHandleInterface {

        public static final String NAME = "RESPONSE_HANDLER";

        public ResponseHandleInterface() {
        }

        @SuppressWarnings("unused")
        @JavascriptInterface
        public void handleResponse(String response) {
            Log.i("handleResponse-", response);
            MyApplication.getInstance().printLog(TAG, response);
            DepositWalletResponseModel responseModel = new Gson().fromJson(response, DepositWalletResponseModel.class);
            if (paymentSuccessListener != null)
                paymentSuccessListener.onPaymentResponse(responseModel);
        }

        @JavascriptInterface
        public void handleResponse1(String response) {
            Log.i("handleResponse 1-", response);
            RETURN_URL = response;
            MyApplication.getInstance().printLog(TAG, "RETURN_URL=" + RETURN_URL);
        }

        @JavascriptInterface
        public void handleResponse2(String response) {
            Log.i("handleResponse 2-", response);
            NOTIFY_URL = response;
            MyApplication.getInstance().printLog(TAG, "NOTIFY_URL=" + NOTIFY_URL);
        }
    }
}
