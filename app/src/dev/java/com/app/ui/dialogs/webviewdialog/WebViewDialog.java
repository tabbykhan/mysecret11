package com.app.ui.dialogs.webviewdialog;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.R;
import com.app.appbase.AppBaseDialogFragment;
import com.base.BaseFragment;
import com.utilities.DeviceScreenUtil;


/**
 * @author Manish Kumar
 * @since 20/10/18
 */

public class WebViewDialog extends AppBaseDialogFragment {


    WebView web_view;
    ImageView iv_close;

    String data;

    public void setData(String data) {
        this.data = data;
    }

    public static WebViewDialog getInstance(Bundle bundle) {
        WebViewDialog messageDialog = new WebViewDialog();
        messageDialog.setArguments(bundle);
        return messageDialog;
    }


    @Override
    public int getLayoutResourceId() {
        return R.layout.dialog_web_view;
    }

    @Override
    public int getFragmentContainerResourceId(BaseFragment baseFragment) {
        return 0;
    }


    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        WindowManager.LayoutParams wlmp = dialog.getWindow().getAttributes();
        wlmp.gravity = Gravity.CENTER;
        wlmp.height = DeviceScreenUtil.getInstance().getHeight(0.60f);
        wlmp.width = DeviceScreenUtil.getInstance().getWidth(0.98f);
        wlmp.dimAmount = 0.8f;
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
    }


    @Override
    public void initializeComponent() {
        super.initializeComponent();
        web_view = getView().findViewById(R.id.web_view);
        iv_close = getView().findViewById(R.id.iv_close);
        setupWebView();
        iv_close.setOnClickListener(this);
    }

    private void setupWebView() {
        web_view.setBackgroundColor(0x00000000);
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

        web_view.loadDataWithBaseURL("", data, "text/html", "UTF-8", "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
        }
    }
}
