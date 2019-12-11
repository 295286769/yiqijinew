package com.yiqiji.money.modules.myModule.login.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.request.ApiService;
import com.yiqiji.money.modules.common.request.RetrofitInstance;
import com.yiqiji.money.modules.common.utils.AnimationUtil;
import com.yiqiji.money.modules.common.utils.IntentUtils;
import com.yiqiji.money.modules.common.utils.RegexUtil;
import com.yiqiji.frame.core.utils.StringUtils;

import org.json.JSONObject;

import java.util.HashMap;

public class SettingPawordActivity extends BaseActivity implements View.OnClickListener {
    private EditText ed_new_pwd;
    private EditText ed_comfirm;
    private Button btn_setting_paword_retrieve_pwd;
    private ApiService apiService;

    private String phoneNumber;
    private String mtokenid;
    private String mcode;
    private boolean isRegister;
    private String tokenid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting_paword);
        initTitle("重置密码");

        apiService = RetrofitInstance.get().create(ApiService.class);
        ed_new_pwd = (EditText) findViewById(R.id.ed_new_pwd_ac_retrieve_pwd);
        ed_comfirm = (EditText) findViewById(R.id.ed_comfirm_pwd_ac_retrieve_pwd);
        btn_setting_paword_retrieve_pwd = (Button) findViewById(R.id.btn_setting_paword_retrieve_pwd);
        btn_setting_paword_retrieve_pwd.setOnClickListener(this);
        tokenid = getIntent().getStringExtra("tokenid");

        ed_comfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (s.length() > 0) {
                    btn_setting_paword_retrieve_pwd.setBackgroundResource(R.drawable.login_button_blue);

                } else {
                    btn_setting_paword_retrieve_pwd.setBackgroundResource(R.drawable.send_out_message);

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ed_new_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_new_pwd.setCursorVisible(true);
            }
        });

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_setting_paword_retrieve_pwd:
                final String new_pwd = ed_new_pwd.getText().toString();
                final String pwd_comfirm = ed_comfirm.getText().toString();

                if (!RegexUtil.isPasw(new_pwd)) {
                    AnimationUtil.shake(ed_comfirm);
                    showToast("密码格式有误,必须为6-18位数字或字母组合");
                    return;
                }

                if (!new_pwd.equals(pwd_comfirm)) {
                    showToast("两次输入的密码不一致");
                    return;
                }
                onResetPwdBecauseLost(new_pwd, pwd_comfirm, tokenid);

                break;

            default:
                break;
        }

    }


    private void onResetPwdBecauseLost(String new_pwd, String pwd_comfirm, String tokenid) {
        new_pwd = StringUtils.MD5(new_pwd);
        new_pwd = StringUtils.reverse1(new_pwd);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("userpass1", new_pwd);
        hashMap.put("userpass2", new_pwd);
        hashMap.put("tokenid", tokenid);
        CommonFacade.getInstance().exec(Constants.RESET_PWD, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog(true);
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                JSONObject object = (JSONObject) o;
                showToast("设置密码成功");
                if (object.getInt("relogin") == 1) {// 需要重新登录
                    LoginConfig.getInstance().setTokenId("");
                    IntentUtils.startActivityOnLogin(SettingPawordActivity.this, IntentUtils.LoginIntentType.SETTING);
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
                dismissDialog();
            }
        });
//
//        HashMap<String, String> hashMap = XzbUtils.mapParmas();
//        String[] params = new String[]{"userpass1", "userpass2", "tokenid", "plat", "deviceid", "appver", "osver",
//                "machine"};
//
//        String[] valus = new String[]{new_pwd, new_pwd, tokenid, hashMap.get("plat"),
//                hashMap.get("deviceid"), hashMap.get("appver"), hashMap.get("osver"), hashMap.get("machine")};
//
//        String sign = StringUtils.setSign(valus, params);
//        hashMap.put("sign", sign);
//        hashMap.put("userpass1", new_pwd);
//        hashMap.put("userpass2", new_pwd);
//        hashMap.put("tokenid", tokenid);
//        apiService.resetPwdBecauseLost(hashMap).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Response<ResponseBody> arg0, Retrofit arg1) {
//                // object{"msg":false,"code":10000,"exception":"error tokenid"}
//                try {
//                    JSONObject object = new JSONObject(arg0.body().string());
//                    if (object.getInt("code") == 0) {
//                        if (object.getInt("relogin") == 1) {// 需要重新登录
//                            Activity activity = SettingPawordActivity.this;
//                            activity.startActivity(new Intent(activity, LoginActivity.class));
//                            activity.finish();
//                        }
//                        showToast("修改密码成功");
//                    } else {
//                        showMyDialog("失败了", object.getString("msg"), "知道了", null, null, null);
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
//            }
//        });
    }
}
