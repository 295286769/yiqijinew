package com.yiqiji.money.modules.Found.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.lee.pullrefresh.utils.UtilPullToRefresh;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.Found.adapter.CommentAdapter;
import com.yiqiji.money.modules.Found.entity.ArticleEntity;
import com.yiqiji.money.modules.Found.entity.CommentEntity;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.adapter.MergeAdapter;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.utils.FaceConversionUtil;
import com.yiqiji.frame.core.utils.LogUtil;
import com.yiqiji.money.modules.common.utils.ShareUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.myModule.login.activity.LoginBaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * reated by dansakai on 2017/3/21.
 * 发现文章详情页
 */

public class ArticleDetailActivity extends BaseActivity implements View.OnTouchListener, View.OnClickListener {

    private ImageView iv_share;
    private ImageView iv_back;
    private TextView tv_title;

    private LinearLayout ll_bottom;
    private ImageView btn_face;
    private Button btn_send;
    private EditText et_sendmessage;
    private View ll_facechoose;


    private PullToRefreshListView pullToRefreshListView;
    private ListView listView;
    private CommentAdapter commentAdapter;
    private List<CommentEntity> commentLis = new ArrayList<>();
    private MergeAdapter adapter;

    private View headView;
    private WebView wb_content;
    private TextView tv_commentNm;

    private ArticleEntity articleEntity = null;
    private int page = 1;
    private String articleId;

    //分享
    private String[] shareText = new String[]{"新浪微博", "微信", "朋友圈", "QQ", "复制链接"};
    private int[] share_icon = new int[]{R.drawable.weibo_icon, R.drawable.chart_icon, R.drawable.frend, R.drawable.qq, R.drawable.copy_icon};
    private SHARE_MEDIA[] share_medias = new SHARE_MEDIA[]{SHARE_MEDIA.SINA, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        initView();
        loadComment();
    }

    /**
     * 加载评论数据
     */
    private void loadComment() {
        HashMap<String, String> map = new HashMap<>();
        if (articleEntity != null) {
            map.put("id", articleId);
            map.put("page", String.valueOf(page));
        } else {
            return;
        }

        CommonFacade.getInstance().exec(Constants.GET_FOUND_COMMENT, map, new ViewCallBack() {

            @Override
            public void onFinish() {
                UtilPullToRefresh.refreshComplete(pullToRefreshListView);
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                UIHelper.showShortToast(mContext, simleMsg.getErrMsg(), 500);
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                JSONObject jo_main = (JSONObject) o;
                List<CommentEntity> temLis = null;
                try {
                    temLis = CommentEntity.parceLis(jo_main.optString("data"));
                } catch (JSONException e) {
                    LogUtil.log_error(null, e);
                }
                boolean hasMoreData = true;
                if (page == 1) {
                    commentLis.clear();
                }
                if (!StringUtils.isEmptyList(temLis)) {
                    commentLis.addAll(temLis);
                    hasMoreData = true;
                } else {
                    hasMoreData = false;
                }

                pullToRefreshListView.setHasMoreData(hasMoreData);
                if (!StringUtils.isEmptyList(commentLis)) {
                    tv_commentNm.setVisibility(View.VISIBLE);
                    tv_commentNm.setText("评论 (" + jo_main.optInt("total") + ")");
                } else {//暂无评论
                    tv_commentNm.setVisibility(View.GONE);
                }
                commentAdapter.notifyDataSetChanged();
                ll_bottom.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initView() {

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        articleEntity = (ArticleEntity) getIntent().getSerializableExtra("articleEntity");

        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        btn_face = (ImageView) findViewById(R.id.btn_face);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        et_sendmessage = (EditText) findViewById(R.id.et_sendmessage);
        final int[] length = new int[1];
        et_sendmessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Html.fromHtml(s.toString()).toString().length() > 300) {
                    UIHelper.showShortToast(mContext, "您最多只能输入300字", 500);
                    String str = s.toString().substring(length[0], s.length());
                    int start = str.lastIndexOf("<");
                    if (start != -1) {
                        if (str.length() - start < FaceConversionUtil.LENGTHSTR
                                .length()) {
                            s.delete(start, s.length());
                        }
                    } else {
                        s.delete(length[0], s.length());
                    }
                } else {
                    length[0] = s.length();
                }
            }
        });
        ll_facechoose = findViewById(R.id.ll_facechoose);


        iv_share = (ImageView) findViewById(R.id.iv_share);
        iv_share.setVisibility(View.VISIBLE);
        iv_share.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("详情");

        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView);
        pullToRefreshListView.setScrollLoadEnabled(true);
        pullToRefreshListView.setPullLoadEnabled(false);
        pullToRefreshListView.setHasMoreData(false);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                loadComment();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                loadComment();
            }
        });

        listView = pullToRefreshListView.getRefreshableView();
        listView.setOnTouchListener(this);
        listView.setDivider(null);

        headView = LayoutInflater.from(this).inflate(R.layout.include_article_foundhead, null);
        wb_content = (WebView) headView.findViewById(R.id.wb_content);
        tv_commentNm = (TextView) headView.findViewById(R.id.tv_commentNm);
        tv_commentNm.setVisibility(View.GONE);

        if (articleEntity != null) {
            articleId = articleEntity.getArticleid();
            wb_content.loadUrl(articleEntity.getArticleurl());
        }

        adapter = new MergeAdapter();
        commentAdapter = new CommentAdapter(this, commentLis);
        adapter.addView(headView, true);
        adapter.addAdapter(commentAdapter);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_share://分享
                //todo 分享
                XzbUtils.hidePointInUmg(mContext, Constants.HIDE_FOUND_SHARE);
                ShareUtil.shareMeth(mContext, headView, shareText, share_icon, share_medias, articleEntity.getArticleurl(), articleEntity.getArticletitle(), articleEntity.getArticletext(), UIHelper.getDisplayWidth((Activity) mContext), true, articleEntity.getArticleimg(), 0);
                break;
            case R.id.btn_send:
                String tokenId = LoginConfig.getInstance().getTokenId();
                if (!TextUtils.isEmpty(tokenId)) {//登录了
                    publishComment(et_sendmessage.getText().toString());
                } else {//跳转到登录页
                    showSimpleAlertDialog("", "您还没有登录，现在去登录么？", "确定", "取消", false, true,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dismissDialog();
                                    LoginBaseActivity.openActivity(ArticleDetailActivity.this);
//                                    IntentUtils.startActivityOnLogin(ArticleDetailActivity.this, IntentUtils.LoginIntentType.OTHER);
                                }
                            },
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dismissDialog();
                                }
                            });
                }
                if (ll_facechoose.getVisibility() == View.VISIBLE) {
                    ll_facechoose.setVisibility(View.GONE);
                    btn_face.setBackgroundResource(R.drawable.comment_face);
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
        }
    }


    private void publishComment(String content) {
        HashMap<String, String> map = new HashMap<>();
        map.put("articleid", articleId);
        if (content.trim().equals("")) {
            UIHelper.showShortToast(mContext, "请输入内容", 500);
            return;
        }
        map.put("content", content);

        CommonFacade.getInstance().exec(Constants.FOUND_COMMENT, map, new ViewCallBack() {
            @Override
            public void onStart() {
                showLoadingDialog();
            }

            @Override
            public void onFinish() {
                dismissDialog();
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                et_sendmessage.setText("");
                pullToRefreshListView.doPullRefreshing(true, 500);
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                UIHelper.showShortToast(mContext, simleMsg.getErrMsg(), 500);
            }
        });

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        UIHelper.hideSoftInputMethod(et_sendmessage);
        if (ll_facechoose.getVisibility() == View.VISIBLE) {
            ll_facechoose.setVisibility(View.GONE);
            btn_face.setBackgroundResource(R.drawable.comment_face);
        }
        return false;
    }

    // 用于友盟分享
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
