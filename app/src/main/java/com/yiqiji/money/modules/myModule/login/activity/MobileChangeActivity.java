package com.yiqiji.money.modules.myModule.login.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.request.ApiService;
import com.yiqiji.money.modules.common.request.RetrofitInstance;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/8.
 */
@SuppressLint("NewApi")
public class MobileChangeActivity extends BaseActivity implements View.OnClickListener {
    private TextView ed_phone_number;
    private EditText ed_erification_code_login;
    private Button btn_login;
    private ApiService apiService;
    private static final int TIME_COUNT = 60;
    private int remaining = TIME_COUNT;
    private Button bt_get_msg_code;
    private ImageView iv_phone_clean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_change);
        iv_phone_clean = (ImageView) findViewById(R.id.iv_phone_clean);
        iv_phone_clean.setOnClickListener(this);
        initTitle("更换手机号");
        apiService = RetrofitInstance.get().create(ApiService.class);

        initView();

    }

    private void initView() {
        ed_phone_number = (TextView) findViewById(R.id.ed_phone_number_ac_login);
        ed_phone_number.setText(LoginConfig.getInstance().getMobile());
        //验证码
        ed_erification_code_login = (EditText) findViewById(R.id.ed_erification_code_login);

        bt_get_msg_code = (Button) findViewById(R.id.bt_get_msg_code);
        bt_get_msg_code.setOnClickListener(this);

        btn_login = (Button) findViewById(R.id.bt_login);
        btn_login.setOnClickListener(this);


        ed_erification_code_login.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (s.length() > 0) {

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

        ed_erification_code_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_erification_code_login.setCursorVisible(true);
            }
        });
    }


    @Override
    public void onClick(View v) {
        String mobile = ed_phone_number.getText().toString();
        String mcode = ed_erification_code_login.getText().toString();

        switch (v.getId()) {
            case R.id.bt_get_msg_code:
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
                onVerifycode(mobile, mtokenid, mcode);
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
//                        mtokenid = object.getString("mtokenid");
//
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


    private void onVerifycode(String mobile, String mtokenid, String mcode) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("mobile", mobile);
        hashMap.put("mtokenid", mtokenid);
        hashMap.put("mcode", mcode);


        CommonFacade.getInstance().exec(Constants.VERIFYCODE, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog(true);
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                Intent intent = new Intent(MobileChangeActivity.this, MobileBangActivity.class);
                intent.putExtra("changeMobile", true);
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
//        apiService.onVerifycode(hashMap).enqueue(new BaseCallBack<ResponseBody>(this, true) {
//            @Override
//            public void onResponse(Response<ResponseBody> arg0, Retrofit arg1) {
//                // TODO Auto-generated method stub
//                super.onResponse(arg0, arg1);
//                try {
//                    JSONObject object = new JSONObject(arg0.body().string());
//                    if (object.getInt("code") == 0) {
//                        Intent intent = new Intent(MobileChangeActivity.this, MobileBangActivity.class);
//                        intent.putExtra("changeMobile", true);
//                        startActivity(intent);
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


}
