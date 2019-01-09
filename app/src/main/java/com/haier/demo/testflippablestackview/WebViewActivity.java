package com.haier.demo.testflippablestackview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * Created by Nate on 2016/7/22.
 */
public class WebViewActivity extends AppCompatActivity implements View.OnClickListener {

    private WebView mWebView;
    private ProgressBar mProgressBar;
    private ImageView iv_close_webview;
    private String url = "https://www.haier.com/cn/ehaier/";
    private static final String CURRENT_BANNER_ADVERTISING_LINK = "current_banner_advertising_link";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //设置切换动画
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        iv_close_webview = findViewById(R.id.iv_close_webview);
        iv_close_webview.setOnClickListener(this);

        if (getIntent().getExtras() != null) {
            url = getIntent().getExtras().getString(CURRENT_BANNER_ADVERTISING_LINK);
        }

        mWebView= findViewById(R.id.web_view);
        mProgressBar= findViewById(R.id.web_view_progress_bar);

        WebSettings mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setSupportZoom(true);
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setDisplayZoomControls(false);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient());

        mWebView.loadUrl(url);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_close_webview:
                this.finish();
                break;
        }

    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                if (mProgressBar.getVisibility() == View.GONE) {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
                mProgressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    class WebViewClient extends android.webkit.WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void finish() {
        super.finish();
        //设置切换动画
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }
}
