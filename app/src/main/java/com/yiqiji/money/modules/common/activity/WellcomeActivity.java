package com.yiqiji.money.modules.common.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.socialize.UMShareAPI;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.homeModule.home.perecenter.BookListPerecenter;
import com.yiqiji.money.modules.homeModule.mybook.activity.BooksActivity;
import com.yiqiji.money.modules.homeModule.mybook.activity.ChooseBookActivity;
import com.yiqiji.money.modules.myModule.login.activity.MobileBangActivity;
import com.yiqiji.money.modules.myModule.login.entity.ThirdLoginInfo;
import com.yiqiji.money.modules.myModule.manager.LoginManager;
import com.yiqiji.money.modules.myModule.manager.ThirdLoginListenerPecenter;
import com.yiqiji.money.modules.myModule.manager.ThirdLoginMessaePerecenter;

public class WellcomeActivity extends BaseActivity implements OnClickListener, ThirdLoginMessaePerecenter {
    private ImageView ivGuide;
    private TextView tvWXLogin;
    private TextView tvQQLogin;
    private TextView tvUnLogin;
    private ThirdLoginListenerPecenter thirdLoginListenerPecenter = new ThirdLoginListenerPecenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        thirdLoginListenerPecenter.ThirdLoginBaseMessaePerecenter(this);
        setContentView(R.layout.activity_wellcom);
        initView();
    }

    private void initView() {
        ivGuide = (ImageView) findViewById(R.id.ivGuide);
        final int DrawableImage[] = {R.drawable.wellcom_one, R.drawable.wellcom_two, R.drawable.wellcom_three};

        final Handler handler = new Handler();
        final int[] i = {0};
        final int[] j = {1};
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Resources res = getApplicationContext().getResources();
                        TransitionDrawable out = new TransitionDrawable(new Drawable[]{res.getDrawable(DrawableImage[i[0]]), res.getDrawable(DrawableImage[j[0]])});
                        out.setCrossFadeEnabled(true);
                        ivGuide.setBackgroundDrawable(out);
                        out.startTransition(3000);
                        i[0]++;
                        j[0]++;
                        if (j[0] == DrawableImage.length) {
                            j[0] = 0;
                        }
                        if (i[0] == DrawableImage.length) {
                            i[0] = 0;
                        }
                        handler.postDelayed(this, 6000);
                    }
                });
            }
        }, 4000);
        tvWXLogin = (TextView) findViewById(R.id.tvWXLogin);
        tvWXLogin.setOnClickListener(this);
        tvQQLogin = (TextView) findViewById(R.id.tvQQLogin);
        tvQQLogin.setOnClickListener(this);
        tvUnLogin = (TextView) findViewById(R.id.tvUnLogin);
        tvUnLogin.setOnClickListener(this);
    }

    /**
     * 跳转到选择账本页面
     */
    private void skipChooseBook() {
        ChooseBookActivity.openActivity(this);
//        Intent intent = new Intent(WellcomeActivity.this, ChooseBookActivity.class);
//        startActivity(intent);
//        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvUnLogin://暂不登录
                skipChooseBook();
                break;
            case R.id.tvWXLogin://微信登录
                XzbUtils.hidePointInUmg(mContext, Constants.HIDE_WEIXIN_LOGIN);
                LoginManager.WXLogin(this, thirdLoginListenerPecenter);
//                UMShareAPI.get(this).getPlatformInfo(WellcomeActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
                break;
            case R.id.tvQQLogin://QQ登录
                XzbUtils.hidePointInUmg(mContext, Constants.HIDE_QQ_LOGIN);
//                UMShareAPI.get(this).getPlatformInfo(WellcomeActivity.this, SHARE_MEDIA.QQ, umAuthListener);
                LoginManager.QQLogin(this, thirdLoginListenerPecenter);
                break;
        }
    }

    private String openid;
    private String profile_image_url;
    private String screen_name;
    //请求个人信息回调
//    private UMAuthListener umAuthListener = new UMAuthListener() {
//
//        @Override
//        public void onStart(SHARE_MEDIA share_media) {
//
//        }
//
//        @Override
//        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
//
//            if (platform.equals(SHARE_MEDIA.QQ)) {
//                openid = data.get("unionid");
//
//                profile_image_url = data.get("profile_image_url");
//                screen_name = data.get("screen_name");
//                oauthCheck(openid, "qq");
//
//            } else if (platform.equals(SHARE_MEDIA.SINA)) {
//                openid = data.get("id");
//                profile_image_url = data.get("profile_image_url");
//                screen_name = data.get("screen_name");
//                oauthCheck(openid, "wb");
//
//            } else if (platform.equals(SHARE_MEDIA.WEIXIN)) {
//                openid = data.get("unionid");
//                profile_image_url = data.get("profile_image_url");
//                screen_name = data.get("screen_name");
//                oauthCheck(openid, "wx");
//
//            }
//
//        }
//
//        @Override
//        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
//        }
//
//        @Override
//        public void onCancel(SHARE_MEDIA platform, int action) {
//        }
//    };

    /**
     * 第三方登录授权检测接口
     *
     * @param openid
     * @param type
     */
    private void oauthCheck(final String openid, final String type) {

        showLoadingDialog();
        LoginManager.oauthCheck(openid, type, new ViewCallBack<ThirdLoginInfo>() {
            @Override
            public void onSuccess(ThirdLoginInfo o) throws Exception {
                dismissDialog();
                ThirdLoginInfo thirdLoginInfo = o;
                // {"isReg":0,"code":0}
                //绑定成功
                //当uid为0，表示需要用户绑定手机号， 当uid为1，表示已经绑定直接跳转登录成功；
                if (thirdLoginInfo.getUid().equals("0")) {
                    MobileBangActivity.openActivity(WellcomeActivity.this, openid, screen_name, profile_image_url, type);
//                    Intent intent = new Intent(WellcomeActivity.this, MobileBangActivity.class);
//                    intent.putExtra("openid", openid);
//                    intent.putExtra("screen_name", screen_name);
//                    intent.putExtra("profile_image_url", profile_image_url);
//                    intent.putExtra("type", type);
//                    startActivity(intent);
                } else {
                    BookListPerecenter.initBooks(new ViewCallBack<BooksDbInfo>() {
                        @Override
                        public void onSuccess(BooksDbInfo booksDbInfo) throws Exception {
                            super.onSuccess(booksDbInfo);
                            if (booksDbInfo != null) {
                                BooksActivity.openActivity(WellcomeActivity.this);
//                                finish();
                                return;
                            }
                            skipChooseBook();
                        }

                        @Override
                        public void onFailed(SimpleMsg simleMsg) {
                            super.onFailed(simleMsg);
                        }
                    });
//
                }
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                dismissDialog();
                super.onFailed(simleMsg);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    //返回的时候关闭，避免内存溢出
    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    @Override
    public void thirdLoginInfo(String openid, String profile_image_url, String screen_name, String share_media_name) {
        this.openid = openid;
        this.profile_image_url = profile_image_url;
        this.screen_name = screen_name;
        oauthCheck(openid, share_media_name);
    }
}
