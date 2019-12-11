package com.yiqiji.money.modules.myModule.login.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
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
import com.yiqiji.money.modules.common.utils.AnimationUtil;
import com.yiqiji.money.modules.common.utils.IntentUtils;
import com.yiqiji.money.modules.common.utils.RegexUtil;
import com.yiqiji.frame.core.utils.StringUtils;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/9/22.
 */
public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private EditText ed_old_pwd;
    // private EditText ed_img_code;
    private EditText ed_new_pwd;
    private EditText ed_confirm;
    private Button btn_comfirm;
    private ApiService apiService;
    private String imgCodeTokenid;
    // private ImageView iv_code;
    private EditText[] eds;
    private ImageView iv_show_old_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitry_reset_password);
        apiService = RetrofitInstance.get().create(ApiService.class);
        initTitle( "修改密码");
        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub
        eds = new EditText[3];
        ed_old_pwd = (EditText) findViewById(R.id.ed_old_pwd_ac_reset_pwd);
        ed_old_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_old_pwd.setCursorVisible(true);
            }
        });

        ed_new_pwd = (EditText) findViewById(R.id.ed_new_pwd_ac_reset_pwd);
        ed_confirm = (EditText) findViewById(R.id.ed_confirm_pwd_ac_reset_pwd);
        // ed_img_code = findEditTextById(R.id.ed_img_code_ac_reset_pwd);

        eds[0] = ed_old_pwd;
        eds[1] = ed_new_pwd;
        eds[2] = ed_confirm;


        btn_comfirm = (Button) findViewById(R.id.btn_comfirm_ac_reset_pwd);

        iv_show_old_pwd = (ImageView) findViewById(R.id.iv_show_old_pwd_ac_reset_password);
        // iv_code = findImageViewById(R.id.iv_code_ac_reset_pwd);

        btn_comfirm.setOnClickListener(this);
        // iv_code.setOnClickListener(this);

        iv_show_old_pwd.setOnTouchListener(new OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ed_old_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        break;
                    case MotionEvent.ACTION_UP:
                        ed_old_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        break;
                    default:
                        break;
                }

                return true;
            }
        });

        ((TextView) findViewById(R.id.ed_confirm_pwd_ac_reset_pwd)).addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                String old_pwd = ed_old_pwd.getText().toString();
                String new_pwd = ed_new_pwd.getText().toString();
                String pwd_comfirm = ed_confirm.getText().toString();

                if (new_pwd.equals(pwd_comfirm) && !TextUtils.isEmpty(old_pwd)) {
                    btn_comfirm.setBackgroundResource(R.drawable.login_button_blue);


                } else {
                    btn_comfirm.setBackgroundResource(R.drawable.send_out_message);

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

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            // case R.id.iv_code_ac_reset_pwd:
            // getImgCode();
            // break;
            case R.id.btn_comfirm_ac_reset_pwd:

                String new_pwd = ed_new_pwd.getText().toString();
                String pwd_comfirm = ed_confirm.getText().toString();

                String olduserpass = ed_old_pwd.getText().toString();

                if (!RegexUtil.isPasw(olduserpass)) {
                    AnimationUtil.shake(ed_old_pwd);
                    showToast("旧密码格式有误,必须为6-18位数字或字母组合");
                    return;
                }


                if (!RegexUtil.isPasw(new_pwd)) {
                    AnimationUtil.shake(ed_new_pwd);
                    showToast("新密码格式有误,必须为6-18位数字或字母组合");
                    return;
                }


                if (!new_pwd.equals(pwd_comfirm)) {
                    showToast("两次输入的密码不一致");
                    return;
                }

                onResetPwdByOldPwd(new_pwd, pwd_comfirm, olduserpass);

                break;
            default:
                break;
        }
    }


    private void onResetPwdByOldPwd(String new_pwd, String pwd_comfirm, String olduserpass) {
        new_pwd = StringUtils.MD5(new_pwd);
        new_pwd = StringUtils.reverse1(new_pwd);

        pwd_comfirm = StringUtils.MD5(pwd_comfirm);
        pwd_comfirm = StringUtils.reverse1(pwd_comfirm);

        olduserpass = StringUtils.MD5(olduserpass);
        olduserpass = StringUtils.reverse1(olduserpass);


        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("userpass1", new_pwd);
        hashMap.put("userpass2", pwd_comfirm);
        hashMap.put("olduserpass", olduserpass);

        CommonFacade.getInstance().exec(Constants.RESET_PWD_BY_OLD_PWD, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog(true);
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);

                JSONObject object = (JSONObject) o;
                showToast("修改密码成功");
                if (object.getInt("relogin") == 1) {// 需要重新登录
                    LoginConfig.getInstance().setTokenId("");
                    IntentUtils.startActivityOnLogin(ResetPasswordActivity.this, IntentUtils.LoginIntentType.SETTING);
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


//        HashMap<String, String> hashMap = XzbUtils.mapParmas();
//        String params[] = {"userpass1", "userpass2", "olduserpass", "tokenid", "plat", "deviceid", "appver",
//                "osver", "machine"};
//
//        String[] valus = new String[]{new_pwd, pwd_comfirm, olduserpass, hashMap.get("tokenid"),
//                hashMap.get("plat"), hashMap.get("deviceid"), hashMap.get("appver"), hashMap.get("osver"),
//                hashMap.get("machine")};
//
//        String sign = StringUtils.setSign(valus, params);
//        hashMap.put("sign", sign);
//        hashMap.put("userpass1", new_pwd);
//        hashMap.put("userpass2", pwd_comfirm);
//        hashMap.put("olduserpass", olduserpass);
//
//        apiService.resetPwdByOldPwd(hashMap).enqueue(new BaseCallBack<ResponseBody>(this, true) {
//            @Override
//            public void onResponse(Response<ResponseBody> arg0, Retrofit arg1) {
//                super.onResponse(arg0, arg1);
//                try {
//                    JSONObject object = new JSONObject(arg0.body().string());
//                    if (object.getInt("code") == 0) {
//                        if (object.getInt("relogin") == 1) {// 需要重新登录
//                            LoginConfig.getInstance().setTokenId("");
//                            IntentUtils.startActivityOnLogin(ResetPasswordActivity.this, IntentUtils.LoginIntentType.SETTING);
//                        }
//                    } else {
//                        showMyDialog("失败了", object.getString("msg"), "知道了", null, null, null);
//                    }
//                } catch (Exception e) {
//                    // TODO: handle exception
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
