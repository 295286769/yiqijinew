package com.yiqiji.money.modules.Found.view;

import android.content.Context;
import android.net.http.SslError;
import android.util.AttributeSet;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yiqiji.money.modules.book.detailinfo.activity.BookSubscribeActivity;
import com.yiqiji.money.modules.common.config.RequsterTag;

/**
 * Created by leichi on 2017/6/27.
 */

public class BaseWebView extends WebView {

    Context mContext;

    public BaseWebView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        super.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (overrideUrlLoading(mContext, url)) {
                    return true;
                }
                if (webViewClient != null) {
                    return webViewClient.shouldOverrideUrlLoading(view, url);
                }
                return super.shouldOverrideUrlLoading(view, url);
            }


            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                if (webViewClient != null) {
                    webViewClient.onReceivedSslError(view, handler, error);
                } else {
                    super.onReceivedSslError(view, handler, error);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (webViewClient != null) {
                    webViewClient.onPageFinished(view, url);
                } else {
                    super.onPageFinished(view, url);
                }
            }
        });
    }


    WebViewClient webViewClient;

    public void setWebViewClient(WebViewClient webViewClient) {
        this.webViewClient = webViewClient;
    }


    public static boolean overrideUrlLoading(Context context, String url) {
        if (url.contains("ziniuapp://money/accountbook") && url.contains("share")) {
            String[] mAccountbookid = url.split("/");
            String bookId = mAccountbookid[4];
            BookSubscribeActivity.open(context, bookId, RequsterTag.RENOVATIONACCOUNTBOOKCATE);//RequsterTag.RENOVATIONACCOUNTBOOKCATE　　零时作为订阅账本的账本类型，在账本预览请求接口的字段为３
            return true;
        }
        return false;
    }
}
