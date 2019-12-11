package com.yiqiji.money.modules.myModule.login.activity;

import android.annotation.SuppressLint;
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

import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.plication.MyApplicaction;
import com.yiqiji.money.modules.common.request.ApiService;
import com.yiqiji.money.modules.common.request.RetrofitInstance;
import com.yiqiji.money.modules.common.utils.AnimationUtil;
import com.yiqiji.money.modules.common.utils.RegexUtil;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/9/22.
 */
@SuppressLint("NewApi")
public class ForgetLoginPasswordActivity extends BaseActivity implements View.OnClickListener {
    private EditText ed_phone_number;
    private EditText ed_msg_code;
    private Button bt_get_msg_code;

    private Button btn_comfirm;
    private ApiService apiService;
    private String mtokenid; // 短信验证码token
    private static final int TIME_COUNT = 60;
    private int remaining = TIME_COUNT;
    private ImageView iv_phone_clean;


    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            timeCount();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplicaction.getInstence().addActivity(this);
        setContentView(R.layout.activity_forget_password);
        apiService = RetrofitInstance.get().create(ApiService.class);
        initTitle("忘记密码");
        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub
        iv_phone_clean = (ImageView) findViewById(R.id.iv_phone_clean);
        iv_phone_clean.setOnClickListener(this);

        ed_phone_number = (EditText) findViewById(R.id.ed_phone_number_ac_retrieve_pwd);
        ed_phone_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_phone_number.setCursorVisible(true);
            }
        });
        ed_msg_code = (EditText) findViewById(R.id.ed_msg_code_ac_retrieve_pwd);

        bt_get_msg_code = (Button) findViewById(R.id.btn_forget_login_send_msg_code);

        btn_comfirm = (Button) findViewById(R.id.btn_comfirm_ac_retrieve_pwd);
        bt_get_msg_code.setOnClickListener(this);
        btn_comfirm.setOnClickListener(this);
        ed_phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (RegexUtil.isMobileNO(s.toString())) {
                    // 验证手机是否注册过
                    bt_get_msg_code.setBackground(getResources().getDrawable(R.drawable.login_button_blue));
                    bt_get_msg_code.setClickable(true);
                } else {
                    bt_get_msg_code.setBackground(getResources().getDrawable(R.drawable.send_out_message));
                    bt_get_msg_code.setClickable(false);
                }

                if (s.length() > 0) {
                    iv_phone_clean.setVisibility(View.VISIBLE);
                } else {
                    iv_phone_clean.setVisibility(View.GONE);
                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        // 输入短信验证码
        ed_msg_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (s.length() > 0) {
                    btn_comfirm.setBackground(getResources().getDrawable(R.drawable.login_button_blue));
                } else {
                    btn_comfirm.setBackground(getResources().getDrawable(R.drawable.send_out_message));

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

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

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        String phone = ed_phone_number.getText().toString();
        // String imgeCode = ed_img_code.getText().toString();
        switch (v.getId()) {
            case R.id.btn_comfirm_ac_retrieve_pwd: // 下一步

                String msgCode = ed_msg_code.getText().toString();
                if (!RegexUtil.isMobileNO(phone)) {
                    AnimationUtil.shake(ed_phone_number);
                    showToast("手机号格式不正确");
                    return;
                }
                if (TextUtils.isEmpty(msgCode)) {
                    AnimationUtil.shake(ed_msg_code);
                    showToast("验证码格式不正确");
                    return;
                }

                onloginByMessageCode(phone, mtokenid, msgCode);

                break;
            case R.id.btn_forget_login_send_msg_code: // 获取验证码

                if (TextUtils.isEmpty(phone)) {
                    AnimationUtil.shake(ed_phone_number);
                    return;
                }
                ed_msg_code.setText("");
                String mobile = ed_phone_number.getText().toString();

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
            case R.id.iv_phone_clean:
                ed_phone_number.setText("");
                iv_phone_clean.setVisibility(View.GONE);

                break;

            default:
                break;
        }
    }

    /**
     * @param mobile
     * @param mtokenid 短信验证码mtokenid、获取验证码时候返回的
     */
    private void onloginByMessageCode(String mobile, String mtokenid, String mcode) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("mobile", mobile);
        hashMap.put("mtokenid", mtokenid);
        hashMap.put("mcode", mcode);

        CommonFacade.getInstance().exec(Constants.VERIFY_BY_MESSAGE_CODE, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog(true);
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                JSONObject object = (JSONObject) o;
                String tokenid = object.getString("tokenid");
                Intent in = new Intent(ForgetLoginPasswordActivity.this, SettingPawordActivity.class);
                in.putExtra("tokenid", tokenid);
                startActivity(in);
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


//        HashMap<String, String> hashMap = XzbUtils.mapParmas();
//        String params[] = {"mobile", "mtokenid", "mcode", "plat", "deviceid", "appver", "osver", "machine"};
//
//        String[] valus = new String[]{mobile, mtokenid, mcode, hashMap.get("plat"), hashMap.get("deviceid"),
//                hashMap.get("appver"), hashMap.get("osver"), hashMap.get("machine")};
//
//        String sign = StringUtils.setSign(valus, params);
//        hashMap.put("sign", sign);
//        hashMap.put("mobile", mobile);
//        hashMap.put("mtokenid", mtokenid);
//        hashMap.put("mcode", mcode);
//
//        apiService.verifyByMessageCode(hashMap).enqueue(new BaseCallBack<ResponseBody>(ForgetLoginPasswordActivity.this, true) {
//            @Override
//            public void onResponse(Response<ResponseBody> arg0, Retrofit arg1) {
//                super.onResponse(arg0, arg1);
//                try {
//                    JSONObject object = new JSONObject(arg0.body().string());
//                    if (object.getInt("code") == 0) {
//                        String tokenid = object.getString("tokenid");
//                        Intent in = new Intent(ForgetLoginPasswordActivity.this, SettingPawordActivity.class);
//                        in.putExtra("tokenid", tokenid);
//                        startActivity(in);
//
//                    } else {
//                        showToast(object.getString("msg"));
//                    }
//                } catch (Exception e) {
//                    // TODO: handle
//                    // exception
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
                ed_msg_code.requestFocus();
                mtokenid = object.getString("mtokenid");
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
//
//        apiService.sendMsgCode(hashMap).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Response<ResponseBody> arg0, Retrofit arg1) {
//                // TODO Auto-generated method stub
//
//                try {
//                    JSONObject object = new JSONObject(arg0.body().string());
//                    if (object.getInt("code") == 0) {
//                        ed_msg_code.requestFocus();
//                        mtokenid = object.getString("mtokenid");
//                        showToast("短信验证码已发送，请注意查收");
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

}
