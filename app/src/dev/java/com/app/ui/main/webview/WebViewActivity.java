package com.app.ui.main.webview;

import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.ui.main.ToolbarFragment;

public class WebViewActivity extends AppBaseActivity {

    ToolbarFragment toolbarFragment;

    ProgressBar pb_data;

    public String getTitles() {
        if (getIntent().getExtras() != null)
            return getIntent().getExtras().getString(DATA1);
        return "";
    }

    public String getWebUrl() {
        if (getIntent().getExtras() != null)
            return getIntent().getExtras().getString(DATA);
        return "";
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_web_view;
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();
        pb_data = findViewById(R.id.pb_data);

        setupToolbar();
        setupWebView();

    }

    private void updateProgressBar(int progress) {
        pb_data.setProgress(progress);
        if (progress < 100) {
            updateViewVisibitity(pb_data, View.VISIBLE);
        } else {
            updateViewVisibitity(pb_data, View.GONE);
        }

    }

    private void setupToolbar() {
        Fragment toolbar = getFm().findFragmentById(R.id.fragment_toolbar);
        if (toolbar != null && toolbar instanceof AppBaseFragment) {
            toolbarFragment = (ToolbarFragment) toolbar;
            toolbarFragment.initializeComponent();
            toolbarFragment.setToolbarView(this);
            toolbarFragment.setLeftTitle(getTitles());
        }
    }

    private void setupWebView() {
        final WebView wv_terms_conditions = findViewById(R.id.web_view);
        wv_terms_conditions.setBackgroundColor(0x00000000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            wv_terms_conditions.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            wv_terms_conditions.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        wv_terms_conditions.getSettings().setJavaScriptEnabled(true);
        wv_terms_conditions.getSettings().setDomStorageEnabled(true);
        wv_terms_conditions.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        wv_terms_conditions.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wv_terms_conditions.setBackground(getResources().getDrawable(R.drawable.bgforallimage));

        wv_terms_conditions.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if (errorCode == -2) {
                    wv_terms_conditions.loadData("<html>Oops your internet seems to be on power nap.<br/>Please check your internet settings</html>","text/html",null);
                    showNetWorkErrorMessage();
                }
            }
        });
        wv_terms_conditions.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                updateProgressBar(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if(toolbarFragment!=null && isValidString(title) && !isValidString(getTitles())){
                    toolbarFragment.setLeftTitle(title);
                }
            }
        });
        wv_terms_conditions.loadUrl(getWebUrl());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }
}
