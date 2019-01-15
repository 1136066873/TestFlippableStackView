package com.haier.demo.testflippablestackview;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.haier.demo.testflippablestackview.helper.webviewhelper.FullscreenHolder;
import com.haier.demo.testflippablestackview.helper.webviewhelper.IWebPageView;
import com.haier.demo.testflippablestackview.helper.webviewhelper.ImageClickInterface;
import com.haier.demo.testflippablestackview.helper.webviewhelper.MyWebChromeClient;

/**
 * Created by Nate on 2016/7/22.
 */
public class WebViewActivity extends AppCompatActivity implements View.OnClickListener , IWebPageView {

    private WebView mWebView;
    private ProgressBar mProgressBar;
    private FrameLayout video_fullView;
    private ImageView iv_close_webview;
/*    private String url = "https://www.haier.com/cn/ehaier/";*/
    private String url = "https://sv.baidu.com/videoui/page/videoland?pd=bjh&context={%22nid%22:%2212675652973615205794%22,%22sourceFrom%22:%22bjh%22}&fr=bjhauthor&type=video";
    private static final String CURRENT_BANNER_ADVERTISING_LINK = "current_banner_advertising_link";
    private MyWebChromeClient mWebChromeClient;
    private FullscreenHolder videoFullView;

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //设置切换动画
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        //init view
        video_fullView = findViewById(R.id.video_fullView);
        iv_close_webview = findViewById(R.id.iv_close_webview);
        iv_close_webview.setOnClickListener(this);
        mWebView= findViewById(R.id.web_view);
        mProgressBar= findViewById(R.id.web_view_progress_bar);

        //get intent data
        if (getIntent().getExtras() != null) {
           // url = getIntent().getExtras().getString(CURRENT_BANNER_ADVERTISING_LINK);
        }

        //init WebView Settings
        mProgressBar.setVisibility(View.VISIBLE);
        WebSettings ws = mWebView.getSettings();
        // 网页内容的宽度是否可大于WebView控件的宽度
        ws.setLoadWithOverviewMode(false);
        // 保存表单数据
        ws.setSaveFormData(true);
        // 是否应该支持使用其屏幕缩放控件和手势缩放
        ws.setSupportZoom(true);
        ws.setBuiltInZoomControls(true);
        ws.setDisplayZoomControls(false);
        // 启动应用缓存
        ws.setAppCacheEnabled(true);
        // 设置缓存模式
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        // setDefaultZoom  api19被弃用
        // 设置此属性，可任意比例缩放。
        ws.setUseWideViewPort(true);
        // 不缩放
        mWebView.setInitialScale(100);
        // 告诉WebView启用JavaScript执行。默认的是false。
        ws.setJavaScriptEnabled(true);
        //  页面加载好以后，再放开图片
        ws.setBlockNetworkImage(false);
        // 使用localStorage则必须打开
        ws.setDomStorageEnabled(true);
        // 排版适应屏幕
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        // WebView是否新窗口打开(加了后可能打不开网页)
        ws.setSupportMultipleWindows(true);

        // webview从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        /** 设置字体默认缩放大小(改变网页字体大小,setTextSize  api14被弃用)*/
        ws.setTextZoom(100);

        mWebChromeClient = new MyWebChromeClient(this);
        mWebView.setWebChromeClient(mWebChromeClient);
        // 与js交互
        mWebView.addJavascriptInterface(new ImageClickInterface(this), "injectedObject");
        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return handleLongImage();
            }
        });

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

    @Override
    public void hindProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showWebView() {
        mWebView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hindWebView() {
        mWebView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void startProgress(int newProgress) {
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setProgress(newProgress);
        if (newProgress == 100) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void addImageClickListener() {

    }

    @Override
    public void fullViewAddView(View view) {
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        videoFullView = new FullscreenHolder(WebViewActivity.this);
        videoFullView.addView(view);
        decor.addView(videoFullView);
    }

    @Override
    public void showVideoFullView() {
        videoFullView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hindVideoFullView() {
        videoFullView.setVisibility(View.GONE);
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

    public FrameLayout getVideoFullView() {
        return videoFullView;
    }

    /**
     * 长按图片事件处理
     */
    private boolean handleLongImage() {
        final WebView.HitTestResult hitTestResult = mWebView.getHitTestResult();
        // 如果是图片类型或者是带有图片链接的类型
        if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                hitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
            // 弹出保存图片的对话框
            new AlertDialog.Builder(WebViewActivity.this)
                    .setItems(new String[]{"查看大图", "保存图片到相册"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String picUrl = hitTestResult.getExtra();
                            //获取图片
                            Log.e("picUrl", picUrl);
                            switch (which) {
                                case 0:
                                    break;
                                case 1:
                                    break;
                                default:
                                    break;
                            }
                        }
                    })
                    .show();
            return true;
        }
        return false;
    }


    @Override
    public void finish() {
        super.finish();
        //设置切换动画
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }
}
