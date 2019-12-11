package com.yiqiji.money.modules.myModule.login.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.socialize.UMShareAPI;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.homeModule.home.activity.HomeActivity;
import com.yiqiji.money.modules.homeModule.home.perecenter.BookListPerecenter;
import com.yiqiji.money.modules.homeModule.mybook.activity.ChooseBookActivity;
import com.yiqiji.money.modules.myModule.login.entity.FinishLoginInfo;
import com.yiqiji.money.modules.myModule.login.entity.ThirdLoginInfo;
import com.yiqiji.money.modules.myModule.manager.LoginManager;
import com.yiqiji.money.modules.myModule.manager.ThirdLoginListenerPecenter;
import com.yiqiji.money.modules.myModule.manager.ThirdLoginMessaePerecenter;

import de.greenrobot.event.EventBus;

/**
 * Created by ${huangweishui} on 2017/6/29.
 * address huang.weishui@71dai.com
 */
public class LoginBaseActivity extends BaseActivity implements View.OnClickListener, ThirdLoginMessaePerecenter {
    private LinearLayout wx_login;
    private LinearLayout qq_login;
    private TextView phone_login;
    private String tokenid;
    private ThirdLoginListenerPecenter thirdLoginListenerPecenter = new ThirdLoginListenerPecenter();

    public static void openActivity(Context context) {
        Intent intent = new Intent(context, LoginBaseActivity.class);
        context.startActivity(intent);
//        ((Activity) context).finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_base);
        EventBus.getDefault().register(this);
        thirdLoginListenerPecenter.ThirdLoginBaseMessaePerecenter(this);
        tokenid = LoginConfig.getInstance().getTokenId();
        initView();
    }

    private void initView() {
        wx_login = (LinearLayout) findViewById(R.id.wx_login);
        qq_login = (LinearLayout) findViewById(R.id.qq_login);
        phone_login = (TextView) findViewById(R.id.phone_login);
        wx_login.setOnClickListener(this);
        qq_login.setOnClickListener(this);
        phone_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wx_login:
                XzbUtils.hidePointInUmg(mContext, Constants.HIDE_WEIXIN_LOGIN);

                LoginManager.WXLogin(this, thirdLoginListenerPecenter);
                break;
            case R.id.qq_login:
                XzbUtils.hidePointInUmg(mContext, Constants.HIDE_QQ_LOGIN);

                LoginManager.QQLogin(this, thirdLoginListenerPecenter);
                break;
            case R.id.phone_login:
                LoginActivity.openActivity(this);
                break;
        }
    }

//    @Override
//    public void thirdLoginInfo(String openid, String profile_image_url, String screen_name, String share_media_name) {
//        oauthCheck(openid, share_media_name, profile_image_url, screen_name);
//    }

    /**
     * 第三方登录授权检测接口
     *
     * @param openid
     * @param type
     */
    private void oauthCheck(final String openid, final String profile_image_url, final String screen_name, final String type) {

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
                    MobileBangActivity.openActivity(LoginBaseActivity.this, openid, screen_name, profile_image_url, type);
                } else {
                    BookListPerecenter.initBooks(new ViewCallBack<BooksDbInfo>() {
                        @Override
                        public void onSuccess(BooksDbInfo booksDbInfo) throws Exception {
                            super.onSuccess(booksDbInfo);
                            if (booksDbInfo != null) {
//                                boolean isHomeCreate = LoginConfig.getInstance().getetHomecreate();
//                                if (isHomeCreate) {
//                                    finish();
//                                } else {
//                                    HomeActivity.openActivity(LoginBaseActivity.this, booksDbInfo);
//                                }
                                HomeActivity.openActivity(LoginBaseActivity.this, booksDbInfo);
//                                finish();
                                return;
                            } else {
                                ChooseBookActivity.openActivity(LoginBaseActivity.this);
                                finish();
                            }
                        }

                        @Override
                        public void onFailed(SimpleMsg simleMsg) {
                            super.onFailed(simleMsg);
                            showToast(simleMsg);
                        }
                    });
                }

//                else {
//                    IntentUtils.startActivityClearTop(LoginBaseActivity.this, ClassName, IntentType);
//                }
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                dismissDialog();
                super.onFailed(simleMsg);
            }
        });
//
    }


    @Override
    public void thirdLoginInfo(String openid, String profile_image_url, String screen_name, String share_media_name) {
        oauthCheck(openid, profile_image_url, screen_name, share_media_name);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    public void onEventMainThread(FinishLoginInfo finishLoginInfo) {
        finish();
    }

    //返回的时候关闭，避免内存溢出
    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
        EventBus.getDefault().unregister(this);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (LoginConfig.getInstance().getIsOutLogin()) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                moveTaskToBack(true);
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }
}
