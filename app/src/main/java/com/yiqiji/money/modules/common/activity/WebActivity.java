package com.yiqiji.money.modules.common.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.utils.ToastUtils;
import com.yiqiji.money.modules.common.widget.MyProgressBar;

public class WebActivity extends BaseActivity {
    private WebView webView;
    private MyProgressBar progressBar;
    private String url;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");

        if (url != null) {
            if (url.indexOf(":") < 0) {
                url = "http://" + url;
            }
        }
        if (url == null) {
            ToastUtils.DiyToast(this, "没有网址");
            progressBar.setVisibility(View.GONE);
            return;
        }
        initView();
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.webView);
        progressBar = (MyProgressBar) findViewById(R.id.progressBar);
        initTitle(title);
        initWeb();
    }

    private void initWeb() {

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptCanOpenWindowsAutomatically(true);// 设置js可以直接打开窗口，如window.open()，默认为false
        settings.setJavaScriptEnabled(true);// 是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        settings.setBuiltInZoomControls(true);
        settings.setSavePassword(false);
        settings.setDomStorageEnabled(true);
        settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);// 是否使用缓存
        settings.setDomStorageEnabled(true);// DOM Storage
        //关闭这些接口防止恶意攻击
        webView.removeJavascriptInterface("searchBoxJavaBridge_");
        webView.removeJavascriptInterface("accessibilityTraversal");
        webView.removeJavascriptInterface("accessibility");
        webView.loadUrl(url);

        // webview默认是使用系统流浪器打开，这里为true时使用webview打开
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {//https证书验证异常的处理方式
                handler.cancel();// 默认的处理方式，WebView变成空白页
//                handler.proceed();//接受证书
                //handleMessage(Message msg); 其他处理
            }
        });
        // 监听页面是否加载完成
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
//                    progressBar.setVisibility(View.GONE);
                } else {
//                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }
        });

    }

    /*
     * (non-Javadoc) 监听回退事件
     */
    @SuppressLint("NewApi")
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();// 返回
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

}
