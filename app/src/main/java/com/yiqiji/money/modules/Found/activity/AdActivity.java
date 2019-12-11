package com.yiqiji.money.modules.Found.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshWebView;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.Found.entity.BannerAdEntity;
import com.yiqiji.money.modules.Found.view.BaseWebView;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.utils.ShareUtil;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.homeModule.home.activity.GroupMembersActivity;
import com.yiqiji.money.modules.homeModule.home.activity.PaymentDetailsActivity;
import com.yiqiji.money.modules.homeModule.mybook.activity.BooksListActivity;
import com.yiqiji.money.modules.myModule.my.activity.QuestionReturnActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dansakai on 2017/3/21.
 * 广告
 */

public class AdActivity extends BaseActivity implements View.OnClickListener {

    private WebView mWebView;
    private PullToRefreshWebView mPullWebView;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
    private String baseUrl = "https://gupiao.baidu.com/stock/";

    private TextView tv_title;
    private ImageView iv_back;
    private BannerAdEntity AdEntity = null;

    //分享
    private String[] shareText = new String[]{"新浪微博", "微信", "朋友圈", "QQ", "复制链接"};
    private int[] share_icon = new int[]{R.drawable.weibo_icon, R.drawable.chart_icon, R.drawable.frend, R.drawable.qq, R.drawable.copy_icon};
    private SHARE_MEDIA[] share_medias = new SHARE_MEDIA[]{SHARE_MEDIA.SINA, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_stock_detail);
        initView();
        loadUrl();
    }

    private void initView() {
        AdEntity = (BannerAdEntity) getIntent().getSerializableExtra("AdEntity");
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        mPullWebView = (PullToRefreshWebView) findViewById(com.lee.pullrefresh.R.id.pull_webview);//new PullToRefreshWebView(this);
        mPullWebView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<WebView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<WebView> refreshView) {
                loadUrl();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<WebView> refreshView) {
            }
        });

        mWebView = mPullWebView.getRefreshableView();
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDefaultTextEncodingName("gb2312");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent intent = null;
                if (BaseWebView.overrideUrlLoading(AdActivity.this,url)) {
                    return true;
                }
                if (url.contains("ziniuapp://money/accountbook/create")) {
                    intent = new Intent(AdActivity.this, BooksListActivity.class);
                    startActivity(intent);
                } else if (url.contains("ziniuapp://money/share/invite")) {//邀请
                    ShareUtil.shareMeth(AdActivity.this, tv_title, shareText, share_icon, share_medias, LoginConfig.share_url, LoginConfig.share_recommend_friend_title, LoginConfig.share_recommend_friend_text, UIHelper.getDisplayWidth(AdActivity.this), true, null, R.drawable.icon);

                } else if (url.contains("ziniuapp://money/accountbook") && url.contains("member")) {
                    String[] mAccountbookid = url.split("/");
                    GroupMembersActivity.startActivity(mContext, mAccountbookid[4]);
                } else if (url.contains("ziniuapp://money/accountbook") && url.contains("bill")) {
                    String[] mAccountbookid = url.split("/");
                    intent = new Intent(AdActivity.this, PaymentDetailsActivity.class);
                    intent.putExtra("billid", mAccountbookid[4]);
                    intent.putExtra("mAccountbookid", mAccountbookid[2]);
                    startActivity(intent);

                } else if (url.contains("ziniuapp://money/accountbook") && url.contains("clear")) {
                    String[] mAccountbookid = url.split("/");
                    intent = new Intent(AdActivity.this, PaymentDetailsActivity.class);
                    intent.putExtra("billid", mAccountbookid[4]);
                    intent.putExtra("mAccountbookid", mAccountbookid[2]);
                    startActivity(intent);

                } else if (url.contains("ziniuapp://money/comment")) {
                    Uri uri = Uri.parse("market://details?id=" + AdActivity.this.getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(AdActivity.this, "无法启动市场 !", Toast.LENGTH_SHORT).show();
                    }

                } else if (url.contains("ziniuapp://money/feedback")) {
                    intent = new Intent(AdActivity.this, QuestionReturnActivity.class);
                    startActivity(intent);

                } else if (url.contains("ziniuapp://money/share")) {//分享
                    XzbUtils.hidePointInUmg(mContext, Constants.HIDE_FOUND_SHARE);
                    Uri uri = Uri.parse(url);
                    ShareUtil.shareMeth(mContext, mPullWebView, shareText, share_icon, share_medias, uri.getQueryParameter("url"), uri.getQueryParameter("title"), uri.getQueryParameter("text"), UIHelper.getDisplayWidth((Activity) mContext), true, uri.getQueryParameter("img"), 0);
                } else {
                    view.loadUrl(url);
                    return true;
                }

                return true;
            }

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.cancel(); // Ignore SSL certificate errors
            }

            public void onPageFinished(WebView view, String url) {
                mPullWebView.onPullDownRefreshComplete();
                setLastUpdateTime();
            }
        });

        if (AdEntity != null) {
            tv_title.setText(AdEntity.getTitle());
            baseUrl = AdEntity.getUrl();
        }
    }

    private void setLastUpdateTime() {
        String text = formatDateTime(System.currentTimeMillis());
        mPullWebView.setLastUpdatedLabel(text);
    }

    private String formatDateTime(long time) {
        if (0 == time) {
            return "";
        }

        return mDateFormat.format(new Date(time));
    }


    private void loadUrl() {
        mWebView.loadUrl(baseUrl);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
        }
    }
}
