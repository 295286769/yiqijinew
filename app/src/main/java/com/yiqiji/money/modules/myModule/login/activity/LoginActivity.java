package com.yiqiji.money.modules.myModule.login.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.request.ApiService;
import com.yiqiji.money.modules.common.request.RetrofitInstance;
import com.yiqiji.money.modules.common.utils.AnimationUtil;
import com.yiqiji.money.modules.common.utils.IntentUtils;
import com.yiqiji.money.modules.common.utils.RegexUtil;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.view.MyTitleLayout;
import com.yiqiji.money.modules.homeModule.home.activity.HomeActivity;
import com.yiqiji.money.modules.homeModule.home.perecenter.BookListPerecenter;
import com.yiqiji.money.modules.homeModule.mybook.activity.ChooseBookActivity;
import com.yiqiji.money.modules.myModule.login.entity.ThirdLoginInfo;
import com.yiqiji.money.modules.myModule.manager.LoginManager;
import com.yiqiji.money.modules.myModule.manager.ThirdLoginListenerPecenter;
import com.yiqiji.money.modules.myModule.manager.ThirdLoginMessaePerecenter;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/21.
 */
@SuppressLint("NewApi")
public class LoginActivity extends BaseActivity implements View.OnClickListener, ThirdLoginMessaePerecenter {
    private EditText ed_phone_number;
    private EditText ed_pwd_ac_login;
    private EditText ed_erification_code_login;
    private Button btn_login;
    private ApiService apiService;
    private MyTitleLayout my_title;
    private LinearLayout ll_login_password;
    private View view_log;
    private static final int TIME_COUNT = 60;
    private int remaining = TIME_COUNT;
    private Button bt_get_msg_code;

    private LinearLayout ll_login_weichar;
    private LinearLayout ll_login_qq;
    private LinearLayout ll_login_weibo;

    private ImageView iv_phone_clean;
    private TextView tv_login_password;

    public static String ClassName;
    public static IntentUtils.LoginIntentType IntentType;
    private ThirdLoginListenerPecenter thirdLoginListenerPecenter = new ThirdLoginListenerPecenter();

    public static void openActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
//        intent.putExtra("ClassName", IntentUtils.getClassName((Activity) context));
        context.startActivity(intent);
//        ((Activity) context).finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initTitle("加入一起记", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IntentType == IntentUtils.LoginIntentType.SETTING) {
                    moveTaskToBack(true);
                } else {
                    finish();
                }
            }
        });

        apiService = RetrofitInstance.get().create(ApiService.class);
        IntentType = (IntentUtils.LoginIntentType) getIntent().getSerializableExtra("IntentType");
        ClassName = getIntent().getStringExtra("ClassName");
        thirdLoginListenerPecenter.ThirdLoginBaseMessaePerecenter(this);
        initView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ed_phone_number.setText("");
        ed_pwd_ac_login.setText("");
        ed_erification_code_login.setText("");
    }

    private void initView() {
        //如果是setting和start跳转过来的则隐藏返回键
        if (IntentType == IntentUtils.LoginIntentType.SETTING || IntentType == IntentUtils.LoginIntentType.START) {
            setReturnBtnVisiable(false);
        } else {
            setReturnBtnVisiable(true);
        }

        tv_login_password = (TextView) findViewById(R.id.tv_login_password);
        tv_login_password.setOnClickListener(this);

        iv_phone_clean = (ImageView) findViewById(R.id.iv_phone_clean);
        iv_phone_clean.setOnClickListener(this);

        ll_login_weichar = (LinearLayout) findViewById(R.id.ll_login_weichar);
        ll_login_qq = (LinearLayout) findViewById(R.id.ll_login_qq);
        ll_login_weibo = (LinearLayout) findViewById(R.id.ll_login_weibo);

        ll_login_weichar.setOnClickListener(this);
        ll_login_qq.setOnClickListener(this);
        ll_login_weibo.setOnClickListener(this);

        ed_phone_number = (EditText) findViewById(R.id.ed_phone_number_ac_login);

        ed_pwd_ac_login = (EditText) findViewById(R.id.ed_pwd_ac_login);

        bt_get_msg_code = (Button) findViewById(R.id.bt_get_msg_code);
        bt_get_msg_code.setOnClickListener(this);

        ll_login_password = (LinearLayout) findViewById(R.id.ll_login_password);
        view_log = findViewById(R.id.view_log);

        //验证码
        ed_erification_code_login = (EditText) findViewById(R.id.ed_erification_code_login);

        btn_login = (Button) findViewById(R.id.bt_login);
        btn_login.setOnClickListener(this);


        ed_phone_number.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (RegexUtil.isMobileNO(s.toString())) {
                    // 验证手机是否注册过
                    checkMobile(s.toString());
                    bt_get_msg_code.setBackground(getResources().getDrawable(R.drawable.login_button_blue));
                    bt_get_msg_code.setClickable(true);

                }

                if (s.length() > 0) {
                    iv_phone_clean.setVisibility(View.VISIBLE);
                } else {
                    iv_phone_clean.setVisibility(View.GONE);
                }

                if (s.length() < 11) {
                    bt_get_msg_code.setBackground(getResources().getDrawable(R.drawable.send_out_message));
                    bt_get_msg_code.setClickable(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        ed_phone_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_phone_number.setCursorVisible(true);
            }
        });


        ed_erification_code_login.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (s.length() > 0) {
                    // 验证手机是否注册过
                    btn_login.setBackground(getResources().getDrawable(R.drawable.login_button_blue));
                } else {
                    btn_login.setBackground(getResources().getDrawable(R.drawable.send_out_message));

                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = null;
        String mobile = ed_phone_number.getText().toString();
        String userpass = ed_pwd_ac_login.getText().toString();
        String mcode = ed_erification_code_login.getText().toString();
        switch (v.getId()) {
            case R.id.bt_login://验证码登录
                if (!RegexUtil.isMobileNO(mobile)) {
                    AnimationUtil.shake(ed_phone_number);
                    showToast("手机号格式不正确");
                    return;
                }

                if (isShowPasw) {
                    if (!RegexUtil.isPasw(userpass)) {
                        AnimationUtil.shake(ed_pwd_ac_login);
                        showToast("密码格式有误,必须为6-18位数字或字母组合");
                        return;
                    }
                }

                if (TextUtils.isEmpty(mcode)) {
                    AnimationUtil.shake(ed_erification_code_login);
                    return;
                }

                XzbUtils.hidePointInUmg(mContext, Constants.HIDE_PHONE_CODE_LOGIN);
                onLoginByMessageCode(mobile, userpass, mcode, mtokenid);
                break;
            case R.id.bt_get_msg_code:

                if (!RegexUtil.isMobileNO(mobile)) {
                    AnimationUtil.shake(ed_phone_number);
                    showToast("手机号格式不正确");
                    return;
                }
                if (isShowPasw) {
                    if (!RegexUtil.isPasw(userpass)) {
                        AnimationUtil.shake(ed_pwd_ac_login);
                        showToast("密码格式有误,必须为6-18位数字或字母组合");
                        return;
                    }
                }
                ed_erification_code_login.setText("");
                //如果发送过消息则回收
                if (remaining < 60) {
                    handler.removeMessages(10086);
                }
                remaining = 60;
                timeCount();
                bt_get_msg_code.setBackground(getResources().getDrawable(R.drawable.send_out_message));
                bt_get_msg_code.setClickable(false);
                getMsgCode(mobile);
                break;

            case R.id.ll_login_qq://qq登录
                XzbUtils.hidePointInUmg(mContext, Constants.HIDE_QQ_LOGIN);
//                UMShareAPI.get(this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, umAuthListener);
                LoginManager.QQLogin(this, thirdLoginListenerPecenter);
//                LoginManager.thirdLogin(this, SHARE_MEDIA.QQ, thirdLoginListenerPecenter);

                break;
            case R.id.ll_login_weichar://微信
                XzbUtils.hidePointInUmg(mContext, Constants.HIDE_WEIXIN_LOGIN);
//                UMShareAPI.get(this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
//                LoginManager.thirdLogin(this, SHARE_MEDIA.QQ, thirdLoginListenerPecenter);
                LoginManager.WXLogin(this, thirdLoginListenerPecenter);
                break;
            case R.id.ll_login_weibo://微博
                XzbUtils.hidePointInUmg(mContext, Constants.HIDE_WEIBO_LOGIN);
//                UMShareAPI.get(this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.SINA, umAuthListener);
                LoginManager.SNWBLogin(this, thirdLoginListenerPecenter);
                break;

            case R.id.iv_phone_clean:
                ed_phone_number.setText("");
                iv_phone_clean.setVisibility(View.GONE);
                bt_get_msg_code.setBackground(getResources().getDrawable(R.drawable.send_out_message));
                break;
            case R.id.tv_login_password:
                intent = new Intent(LoginActivity.this, LoginOnPaswActivity.class);
                startActivityForResult(intent, RequsterTag.STARTACTIVITYREREQUST);
                break;
        }
    }

    private void onLoginByMessageCode(final String mobile, String userpass, String mcode, String mtokenid) {
        showLoadingDialog();
        LoginManager.toLogin(mobile, userpass, mcode, mtokenid, new ViewCallBack() {
            @Override
            public void onSuccess(Object o) throws Exception {
                dismissDialog();
                BookListPerecenter.initBooks(new ViewCallBack<BooksDbInfo>() {
                    @Override
                    public void onSuccess(BooksDbInfo booksDbInfo) throws Exception {
                        super.onSuccess(booksDbInfo);
                        if (booksDbInfo != null) {
//                            boolean isHomeCreate = LoginConfig.getInstance().getetHomecreate();
//                            if (isHomeCreate) {
//                                EventBus.getDefault().post(new FinishLoginInfo());
//                                finish();
//                            } else {
//                                HomeActivity.openActivity(LoginActivity.this, booksDbInfo);
//                            }
                            HomeActivity.openActivity(LoginActivity.this, booksDbInfo);
//                            finish();
                            return;
                        } else {
                            ChooseBookActivity.openActivity(LoginActivity.this);
                            finish();
                        }
                    }

                    @Override
                    public void onFailed(SimpleMsg simleMsg) {
                        super.onFailed(simleMsg);
                        showToast(simleMsg);
                    }
                });
//                if (isShowPasw) {//这是一个新用户
//                    Intent intent = new Intent(LoginActivity.this, LoginOnSettingName.class);
//                    startActivity(intent);
//
//                } else {
                //所有登录：登录成功后 MineFragment.SEND_MESSAGE设置为true，先获取个人信息后，再发送一个消息给首页接收，避免后台数据没合并
//                IntentUtils.startActivityClearTop(LoginActivity.this, ClassName, IntentType);
//                }
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                dismissDialog();
                showToast(simleMsg.getErrMsg());
            }
        });

//        HashMap<String, String> hashMap = XzbUtils.mapParmas();
//        String params[] = {"mobile", "userpass", "mtokenid", "mcode", "plat", "deviceid", "appver", "osver", "machine"};
//
//        String[] valus = new String[]{mobile, userpass, mtokenid, mcode, hashMap.get("plat"),
//                hashMap.get("deviceid"), hashMap.get("appver"), hashMap.get("osver"), hashMap.get("machine")};
//
//        String sign = StringUtils.setSign(valus, params);
//        hashMap.put("sign", sign);
//        hashMap.put("mobile", mobile);
//        hashMap.put("userpass", userpass);
//        hashMap.put("mcode", mcode);
//        hashMap.put("mtokenid", mtokenid);
//        apiService.loginByMessageCode(hashMap).enqueue(new BaseCallBack<ResponseBody>(this, true) {
//            @Override
//            public void onResponse(Response<ResponseBody> arg0, Retrofit arg1) {
//                super.onResponse(arg0, arg1);
//                try {
//                    JSONObject object = new JSONObject(arg0.body().string());
//                    //{ "tokenid": "zu_58be635d82e14_v1.0","uid": 147, "code": 0}
//                    if (object.getInt("code") == 0) {
//
//                        LoginConfig.getInstance().setTokenId(object.getString("tokenid"));
//                        if (isShowPasw) {
//                            //这是一个新用户
//                            Intent intent = new Intent(LoginActivity.this, LoginOnSettingName.class);
//                            startActivity(intent);
//                        } else {
//                            //所有登录：登录成功后 MineFragment.SEND_MESSAGE设置为true，先获取个人信息后，再发送一个消息给首页接收，避免后台数据没合并
//                            IntentUtils.startActivityClearTop(LoginActivity.this, ClassName, IntentType);
//                        }
//                    } else {
//                        showToast(object.getString("msg"));
//                    }
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable arg0) {
//                super.onFailure(arg0);
//            }
//        });
    }

    private String mtokenid;

    private void getMsgCode(String mobile) {


        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("mobile", mobile);
        CommonFacade.getInstance().exec(Constants.SEND_MSG_COD, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                JSONObject object = (JSONObject) o;
                mtokenid = object.getString("mtokenid");
                ed_erification_code_login.requestFocus();
                showToast("短信验证码已发送，请注意查收");
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                showToast(simleMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();

            }
        });

//        HashMap<String, String> hashMap = XzbUtils.mapParmas();
//        String[] params = new String[]{"mobile", "plat", "deviceid", "appver", "osver", "machine"};
//
//        String[] valus = new String[]{mobile, hashMap.get("plat"), hashMap.get("deviceid"), hashMap.get("appver"),
//                hashMap.get("osver"), hashMap.get("machine")};
//
//        String sign = StringUtils.setSign(valus, params);
//        hashMap.put("sign", sign);
//        hashMap.put("mobile", mobile);
//        apiService.sendMsgCode(hashMap).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Response<ResponseBody> arg0, Retrofit arg1) {
//                // TODO Auto-generated method stub
//
//                try {
//                    JSONObject object = new JSONObject(arg0.body().string());
//                    if (object.getInt("code") == 0) {
//                        mtokenid = object.getString("mtokenid");
//                        ed_erification_code_login.requestFocus();
//                        showToast("短信验证码已发送，请注意查收");
//
//                    } else {
//                        showToast(object.getString("msg"));
//                    }
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable arg0) {
//                // TODO Auto-generated method stub
//            }
//        });
    }

    private void timeCount() {
        if (remaining == 0) {
            bt_get_msg_code.setText("获取验证码");
            remaining = TIME_COUNT;
            bt_get_msg_code.setClickable(true);
            bt_get_msg_code.setBackground(getResources().getDrawable(R.drawable.login_button_blue));
        } else {
            bt_get_msg_code.setText(remaining + "s");
            handler.sendEmptyMessageDelayed(10086, 1000);
        }
        remaining--;
    }


    private boolean isShowPasw = false;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            timeCount();
        }
    };

    private void checkMobile(String mobile) {


        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("mobile", mobile);

        CommonFacade.getInstance().exec(Constants.CHECKMOBILE, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                JSONObject object = (JSONObject) o;
                // "isReg":1 // 0.未注册；1.已注册；

                if (object.getInt("isReg") == 0) {
                    ll_login_password.setVisibility(View.VISIBLE);
                    view_log.setVisibility(View.VISIBLE);
                    ed_pwd_ac_login.requestFocus();
                    isShowPasw = true;
                } else {
                    ll_login_password.setVisibility(View.GONE);
                    view_log.setVisibility(View.GONE);
                    isShowPasw = false;
                }

            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
//        HashMap<String, String> hashMap = XzbUtils.mapParmas();
//
//        String[] params = new String[]{"mobile", "plat", "deviceid", "appver", "osver", "machine"};
//
//        String[] valus = new String[]{mobile, hashMap.get("plat"), hashMap.get("deviceid"), hashMap.get("appver"),
//                hashMap.get("osver"), hashMap.get("machine")};
//
//        String sign = StringUtils.setSign(valus, params);
//        hashMap.put("sign", sign);
//        hashMap.put("mobile", mobile);

//        apiService.onCheckmobile(hashMap).enqueue(new BaseCallBack<ResponseBody>(this, false) {
//            @Override
//            public void onResponse(Response<ResponseBody> arg0, Retrofit arg1) {
//                // TODO Auto-generated method stub
//                super.onResponse(arg0, arg1);
//                try {
//                    JSONObject object = new JSONObject(arg0.body().string());
//                    if (object.getInt("code") == 0) {
//
//                        // "isReg":1 // 0.未注册；1.已注册；
//                        if (object.getInt("isReg") == 0) {
//                            ll_login_password.setVisibility(View.VISIBLE);
//                            view_log.setVisibility(View.VISIBLE);
//                            ed_pwd_ac_login.requestFocus();
//                            isShowPasw = true;
//                        } else {
//                            ll_login_password.setVisibility(View.GONE);
//                            view_log.setVisibility(View.GONE);
//                            isShowPasw = false;
//                        }
//                    }
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable arg0) {
//                super.onFailure(arg0);
//            }
//        });
    }


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
                    MobileBangActivity.openActivity(LoginActivity.this, openid, screen_name, profile_image_url, type);
//                    Intent intent = new Intent(LoginActivity.this, MobileBangActivity.class);
//                    intent.putExtra("openid", openid);
//                    intent.putExtra("screen_name", screen_name);
//                    intent.putExtra("profile_image_url", profile_image_url);
//                    intent.putExtra("type", type);
//                    startActivity(intent);
                } else {
                    IntentUtils.startActivityClearTop(LoginActivity.this, ClassName, IntentType);
                }
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                dismissDialog();
                super.onFailed(simleMsg);
            }
        });
//        HashMap<String, String> hashMap = XzbUtils.mapParmas();
//
//        String[] params = new String[]{"openid", "type", "plat", "deviceid", "appver", "osver", "machine"};
//
//        String[] valus = new String[]{openid, type, hashMap.get("plat"), hashMap.get("deviceid"), hashMap.get("appver"),
//                hashMap.get("osver"), hashMap.get("machine")};
//
//        String sign = StringUtils.setSign(valus, params);
//        hashMap.put("sign", sign);
//        hashMap.put("openid", openid);
//        hashMap.put("type", type);
//        apiService.onCheck(hashMap).enqueue(new BaseCallBack<ResponseBody>(this, true) {
//            @Override
//            public void onResponse(Response<ResponseBody> arg0, Retrofit arg1) {
//                // TODO Auto-generated method stub
//                super.onResponse(arg0, arg1);
//                try {
//                    JSONObject object = new JSONObject(arg0.body().string());
//                    // {"isReg":0,"code":0}
//                    if (object.getInt("code") == 0) {
//                        //绑定成功
//                        //当uid为0，表示需要用户绑定手机号， 当uid为1，表示已经绑定直接跳转登录成功；
//                        if (object.getInt("uid") == 0) {
//                            Intent intent = new Intent(LoginActivity.this, MobileBangActivity.class);
//                            intent.putExtra("openid", openid);
//                            intent.putExtra("screen_name", screen_name);
//                            intent.putExtra("profile_image_url", profile_image_url);
//                            intent.putExtra("type", type);
//                            startActivity(intent);
//                        } else {
//                            LoginConfig.getInstance().setTokenId(object.getString("tokenid"));
//                            IntentUtils.startActivityClearTop(LoginActivity.this, ClassName, IntentType);
//                        }
//                    }
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable arg0) {
//                super.onFailure(arg0);
//            }
//        });
    }


    private String openid;
    private String profile_image_url;
    private String screen_name;


    //请求个人信息回调
    private UMAuthListener umAuthListener = new UMAuthListener() {

        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

            if (platform.equals(SHARE_MEDIA.QQ)) {
                openid = data.get("unionid");

                profile_image_url = data.get("profile_image_url");
                screen_name = data.get("screen_name");
                oauthCheck(openid, "qq");

            } else if (platform.equals(SHARE_MEDIA.SINA)) {
                openid = data.get("id");
                profile_image_url = data.get("profile_image_url");
                screen_name = data.get("screen_name");
                oauthCheck(openid, "wb");

            } else if (platform.equals(SHARE_MEDIA.WEIXIN)) {
                openid = data.get("unionid");
                profile_image_url = data.get("profile_image_url");
                screen_name = data.get("screen_name");
                oauthCheck(openid, "wx");

            }

        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (resultCode == RequsterTag.STARTACTIVITYRESULT) {
            finish();
        }

    }

    //返回的时候关闭，避免内存溢出
    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (IntentType == IntentUtils.LoginIntentType.SETTING) {
//                moveTaskToBack(true);
//            } else {
//                finish();
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public void thirdLoginInfo(String openid, String profile_image_url, String screen_name, String share_media_name) {
        oauthCheck(openid, share_media_name);
    }
}
