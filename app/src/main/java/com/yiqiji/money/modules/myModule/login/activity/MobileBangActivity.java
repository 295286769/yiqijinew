package com.yiqiji.money.modules.myModule.login.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.request.ApiService;
import com.yiqiji.money.modules.common.request.RetrofitInstance;
import com.yiqiji.money.modules.common.utils.AnimationUtil;
import com.yiqiji.money.modules.common.utils.RegexUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.homeModule.home.activity.HomeActivity;
import com.yiqiji.money.modules.homeModule.home.perecenter.BookListPerecenter;
import com.yiqiji.money.modules.homeModule.mybook.activity.ChooseBookActivity;
import com.yiqiji.money.modules.myModule.manager.LoginManager;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/7.
 */
@SuppressLint("NewApi")
public class MobileBangActivity extends BaseActivity implements View.OnClickListener {

    private EditText ed_phone_number;
    private EditText ed_erification_code_login;
    private Button btn_login;
    private ApiService apiService;
    private static final int TIME_COUNT = 60;
    private int remaining = TIME_COUNT;
    private Button bt_get_msg_code;

    private String openid;
    private String profile_image_url;
    private String screen_name;
    private String type;
    //为True则说明是更换手机号
    private boolean changeMobile;

    private LinearLayout ll_login_password;
    private View view_log_password;
    private EditText ed_pwd_ac_login;
    private ImageView iv_phone_clean;

    public static void openActivity(Context mContext, String openid, String screen_name, String profile_image_url, String type) {
        Intent intent = new Intent(mContext, MobileBangActivity.class);
        intent.putExtra("openid", openid);
        intent.putExtra("screen_name", screen_name);
        intent.putExtra("profile_image_url", profile_image_url);
        intent.putExtra("type", type);
        mContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mobile_bang);
        initTitle("绑定手机号");
        apiService = RetrofitInstance.get().create(ApiService.class);
        openid = getIntent().getStringExtra("openid");
        profile_image_url = getIntent().getStringExtra("profile_image_url");
        screen_name = getIntent().getStringExtra("screen_name");
        type = getIntent().getStringExtra("type");
        changeMobile = getIntent().getBooleanExtra("changeMobile", false);

        initView();
    }


    private void initView() {
        iv_phone_clean = (ImageView) findViewById(R.id.iv_phone_clean);
        iv_phone_clean.setOnClickListener(this);

        ed_pwd_ac_login = (EditText) findViewById(R.id.ed_pwd_ac_login);
        ll_login_password = (LinearLayout) findViewById(R.id.ll_login_password);
        view_log_password = (View) findViewById(R.id.view_log_password);

        ed_phone_number = (EditText) findViewById(R.id.ed_phone_number_ac_login);
        //验证码
        ed_erification_code_login = (EditText) findViewById(R.id.ed_erification_code_login);

        bt_get_msg_code = (Button) findViewById(R.id.bt_get_msg_code);
        bt_get_msg_code.setOnClickListener(this);

        btn_login = (Button) findViewById(R.id.bt_login);
        btn_login.setOnClickListener(this);
        ed_phone_number.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (RegexUtil.isMobileNO(s.toString())) {

                    //更换手机号的时候不验证手机是否注册
                    if (!changeMobile) {
                        // 验证手机是否注册过
                        checkMobile(s.toString());
                    }
                    bt_get_msg_code.setBackground(getResources().getDrawable(R.drawable.login_button_blue));
                    bt_get_msg_code.setClickable(true);
                } else {

                    if (s.length() > 0) {
                        iv_phone_clean.setVisibility(View.VISIBLE);
                    } else {
                        iv_phone_clean.setVisibility(View.GONE);
                    }
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

    private boolean isShowPasw = false;

    @Override
    public void onClick(View v) {
        String mobile = ed_phone_number.getText().toString();
        String mcode = ed_erification_code_login.getText().toString();
        String userpass = ed_pwd_ac_login.getText().toString();
        switch (v.getId()) {


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
                bt_get_msg_code.setBackground(getResources().getDrawable(R.drawable.send_out_message));
                bt_get_msg_code.setClickable(false);
                timeCount();
                getMsgCode(mobile);

                break;
            case R.id.bt_login:

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

                if (changeMobile) {
                    onChgmobile(mobile, mtokenid, mcode);
                } else {
                    oauthBound(type, openid, screen_name, profile_image_url, mobile, userpass, mtokenid, mcode);
                }

                break;

            case R.id.iv_phone_clean:
                ed_phone_number.setText("");
                iv_phone_clean.setVisibility(View.GONE);
                bt_get_msg_code.setBackground(getResources().getDrawable(R.drawable.send_out_message));
                break;
        }
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


    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            timeCount();
        }
    };

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

//
//        HashMap<String, String> hashMap = XzbUtils.mapParmas();
//        String[] params = new String[]{"mobile", "plat", "deviceid", "appver", "osver", "machine"};
//
//        String[] valus = new String[]{mobile, hashMap.get("plat"), hashMap.get("deviceid"), hashMap.get("appver"),
//                hashMap.get("osver"), hashMap.get("machine")};
//
//        String sign = StringUtils.setSign(valus, params);
//        hashMap.put("sign", sign);
//        hashMap.put("mobile", mobile);
//
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


    /**
     * 第三方登录没有绑定调用这个
     * 密码要MD5
     */
    private void oauthBound(String type, String openid, String nickname, String avatar, String mobile, String userpass, String mtokenid, String mcode) {
        userpass = StringUtils.MD5(userpass);
        userpass = StringUtils.reverse1(userpass);

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("type", type);
        hashMap.put("openid", openid);
        hashMap.put("nickname", nickname);
        hashMap.put("avatar", avatar);
        hashMap.put("mobile", mobile);
        hashMap.put("userpass", userpass);

        hashMap.put("mtokenid", mtokenid);
        hashMap.put("mcode", mcode);
        LoginManager.oauthBoundPhone(this, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                showToast("登录成功");
                BookListPerecenter.initBooks(new ViewCallBack<BooksDbInfo>() {
                    @Override
                    public void onSuccess(BooksDbInfo booksDbInfo) throws Exception {
                        super.onSuccess(booksDbInfo);
                        if (booksDbInfo != null) {
                            HomeActivity.openActivity(MobileBangActivity.this, booksDbInfo);
//                                finish();
                            return;
                        } else {
                            ChooseBookActivity.openActivity(MobileBangActivity.this);
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

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                showToast(simleMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissDialog();
            }
        });

//        CommonFacade.getInstance().exec(Constants.OAUTHBOUND, hashMap, new ViewCallBack() {
//            @Override
//            public void onStart() {
//                super.onStart();
//                showLoadingDialog();
//            }
//
//            @Override
//            public void onSuccess(Object o) throws Exception {
//                super.onSuccess(o);
//                showToast("登录成功");
//                BookListPerecenter.initBooks(new ViewCallBack<BooksDbInfo>() {
//                    @Override
//                    public void onSuccess(BooksDbInfo booksDbInfo) throws Exception {
//                        super.onSuccess(booksDbInfo);
//                        if (booksDbInfo != null) {
//                            HomeActivity.openActivity(MobileBangActivity.this, booksDbInfo);
////                                finish();
//                            return;
//                        } else {
//                            ChooseBookActivity.openActivity(MobileBangActivity.this);
//                            finish();
//                        }
//                    }
//
//                    @Override
//                    public void onFailed(SimpleMsg simleMsg) {
//                        super.onFailed(simleMsg);
//                        showToast(simleMsg);
//                    }
//                });
//
//            }
//
//            @Override
//            public void onFailed(SimpleMsg simleMsg) {
//                super.onFailed(simleMsg);
//                showToast(simleMsg);
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                dismissDialog();
//            }
//        });

//
//        HashMap<String, String> hashMap = XzbUtils.mapParmas();
//        String[] params = new String[]{"type", "openid", "nickname", "avatar", "mobile", "userpass", "mtokenid", "mcode", "plat", "deviceid", "appver", "osver", "machine"};
//
//        String[] valus = new String[]{type, openid, nickname, avatar, mobile, userpass, mtokenid, mcode, hashMap.get("plat"), hashMap.get("deviceid"), hashMap.get("appver"),
//                hashMap.get("osver"), hashMap.get("machine")};
//
//        String sign = StringUtils.setSign(valus, params);
//        hashMap.put("sign", sign);
//
//        hashMap.put("type", type);
//        hashMap.put("openid", openid);
//        hashMap.put("nickname", nickname);
//        hashMap.put("avatar", avatar);
//        hashMap.put("mobile", mobile);
//        hashMap.put("userpass", userpass);
//
//        hashMap.put("mtokenid", mtokenid);
//        hashMap.put("mcode", mcode);
//
//        apiService.onOauthBound(hashMap).enqueue(new BaseCallBack<ResponseBody>(MobileBangActivity.this, true) {
//            @Override
//            public void onResponse(Response<ResponseBody> arg0, Retrofit arg1) {
//                super.onResponse(arg0, arg1);
//
//                try {
//                    JSONObject object = new JSONObject(arg0.body().string());
//                    //{"uid":153,"tokenid":"zu_58c0bdc50d291_v1.0","code":0}
//                    if (object.getInt("code") == 0) {
//                        showToast("登录成功");
//                        LoginConfig.getInstance().setTokenId(object.getString("tokenid"));
//                        LoginConfig.getInstance().setUserid(object.getString("uid"));
//                        if (isShowPasw) {
//                            //新用户跳转到选则账本
//                            Intent intent = new Intent(MobileBangActivity.this, ChooseBookActivity.class);
//                            startActivity(intent);
//                        } else {
//                            IntentUtils.startActivityClearTop(MobileBangActivity.this, LoginActivity.ClassName, LoginActivity.IntentType);
//                        }
//                    } else if (object.getInt("code") == 57002) {
//                        finish();
//                        showToast(object.getString("msg"));
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


    /**
     * 更换手机号
     *
     * @param mobile
     * @param mtokenid
     * @param mcode
     */
    private void onChgmobile(final String mobile, String mtokenid, String mcode) {

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("mobile", mobile);
        hashMap.put("mtokenid", mtokenid);
        hashMap.put("mcode", mcode);
        CommonFacade.getInstance().exec(Constants.CHGMOBILE, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog(true);
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                LoginConfig.getInstance().setMobile(mobile);
                showToast("更换手机号成功");
                Intent intent = new Intent(MobileBangActivity.this, AccountActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                showToast(simleMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissDialog();
            }
        });

//
//        HashMap<String, String> hashMap = XzbUtils.mapParmas();
//        String[] params = new String[]{"mobile", "mtokenid", "mcode", "plat", "deviceid", "appver", "osver", "machine"};
//
//        String[] valus = new String[]{mobile, mtokenid, mcode, hashMap.get("plat"), hashMap.get("deviceid"), hashMap.get("appver"),
//                hashMap.get("osver"), hashMap.get("machine")};
//
//        String sign = StringUtils.setSign(valus, params);
//        hashMap.put("sign", sign);
//        hashMap.put("mobile", mobile);
//        hashMap.put("mtokenid", mtokenid);
//        hashMap.put("mcode", mcode);
//
//        apiService.onChaMobile(hashMap).enqueue(new BaseCallBack<ResponseBody>(this, true) {
//            @Override
//            public void onResponse(Response<ResponseBody> arg0, Retrofit arg1) {
//                // TODO Auto-generated method stub
//                super.onResponse(arg0, arg1);
//                try {
//                    JSONObject object = new JSONObject(arg0.body().string());
//                    if (object.getInt("code") == 0) {
//                        LoginConfig.getInstance().setMobile(mobile);
//                        showToast("更换手机号成功");
//                        Intent intent = new Intent(MobileBangActivity.this, AccountActivity.class);
//                        startActivity(intent);
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
//                super.onFailure(arg0);
//                // TODO Auto-generated method stub
//            }
//        });
    }


    /**
     * 检测手机号是否注册过
     *
     * @param mobile
     */
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
                    view_log_password.setVisibility(View.VISIBLE);
                    ed_pwd_ac_login.requestFocus();
                    isShowPasw = true;
                } else {
                    ll_login_password.setVisibility(View.GONE);
                    view_log_password.setVisibility(View.GONE);
                    isShowPasw = false;
                }

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
//
//        String[] params = new String[]{"mobile", "plat", "deviceid", "appver", "osver", "machine"};
//
//        String[] valus = new String[]{mobile, hashMap.get("plat"), hashMap.get("deviceid"), hashMap.get("appver"),
//                hashMap.get("osver"), hashMap.get("machine")};
//
//        String sign = StringUtils.setSign(valus, params);
//        hashMap.put("sign", sign);
//        hashMap.put("mobile", mobile);
//
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


}
