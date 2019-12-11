package com.yiqiji.money.modules.property.activity;

import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshWebView;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.utils.DBManager;
import com.yiqiji.money.modules.property.entity.AddPropertyItemEntity;
import com.yiqiji.money.modules.property.entity.FundEntity;
import com.yiqiji.money.modules.property.entity.PropertyItemEntity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dansakai on 2017/3/13.
 * 股票详细页
 */

public class StockDetailActivity extends BaseActivity implements View.OnClickListener {
    private WebView mWebView;
    private PullToRefreshWebView mPullWebView;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
    private String baseUrl = "https://gupiao.baidu.com/stock/";

    private TextView tv_title;
    private ImageView iv_back;
    private TextView tv_attention;

    private PropertyItemEntity entity;
    private FundEntity funentity;//自选
    private boolean isOption = false;
    private boolean isAtte = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_stock_detail);
        initView();
        changeData();
        loadUrl();
        setLastUpdateTime();
    }

    private void changeData() {
        if (isOption) {
            tv_title.setText(funentity.getName());
            if (!isAtte) {
                tv_attention.setText("添加关注");
            } else {
                tv_attention.setText("取消关注");
            }
            baseUrl += (funentity.getCode() + ".html");
        } else {
            tv_title.setText("资产详情");
            tv_attention.setText("编辑");
            baseUrl += (entity.getStockcode() + ".html");
        }
    }

    private void initView() {
        isOption = getIntent().getBooleanExtra("isOption", false);
        if (!isOption) {
            entity = (PropertyItemEntity) getIntent().getSerializableExtra("entity");
        } else {
            funentity = (FundEntity) getIntent().getSerializableExtra("fundEntity");
            isAtte = funentity.isAttention();
        }

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_attention = (TextView) findViewById(R.id.tv_attention);
        tv_attention.setVisibility(View.VISIBLE);
        tv_attention.setOnClickListener(this);
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
                String requestUrl = url;
                if (url.contains("bdstockmain://activity/main")) {
                    loadUrl();
                } else {
                    view.loadUrl(url);
                    return true;
                }
                return true;
            }

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.cancel();
            }

            public void onPageFinished(WebView view, String url) {
                mPullWebView.onPullDownRefreshComplete();
                setLastUpdateTime();
            }
        });
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
            case R.id.tv_attention://跳转到编辑页
                if (isOption) {
                    DBManager db = new DBManager(this);
                    if (isAtte) {
                        isAtte = false;
                        tv_attention.setText("添加关注");
                        db.delete(funentity);
                    } else {
                        isAtte = true;
                        tv_attention.setText("取消关注");
                        db.addSingle(funentity);
                    }

                } else {
                    Intent intent = new Intent(StockDetailActivity.this, PropertyNewFundActivity.class);
                    AddPropertyItemEntity addEntity = new AddPropertyItemEntity();
                    addEntity.setAssetid(entity.getAssetid());
                    addEntity.setStockcode(entity.getStockcode());
                    addEntity.setCurrentprice(entity.getCurrentprice());
                    addEntity.setStocknum(entity.getStocknum());
                    addEntity.setItemtypeId(entity.getItemtype());
                    addEntity.setItemname(entity.getItemname());
                    intent.putExtra("addproEntity", addEntity);
                    intent.putExtra("isEdit", true);
                    startActivityForResult(intent, 1002);
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1002) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
