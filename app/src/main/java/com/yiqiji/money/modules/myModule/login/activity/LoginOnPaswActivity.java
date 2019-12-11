package com.yiqiji.money.modules.myModule.login.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.request.ApiService;
import com.yiqiji.money.modules.common.request.RetrofitInstance;
import com.yiqiji.money.modules.common.utils.AnimationUtil;
import com.yiqiji.money.modules.common.utils.RegexUtil;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.homeModule.home.activity.HomeActivity;
import com.yiqiji.money.modules.homeModule.home.perecenter.BookListPerecenter;
import com.yiqiji.money.modules.homeModule.mybook.activity.ChooseBookActivity;
import com.yiqiji.money.modules.myModule.manager.LoginManager;

/**
 * Created by Administrator on 2017/3/10.
 * 使用密码登录
 */
@SuppressLint("NewApi")
public class LoginOnPaswActivity extends BaseActivity implements View.OnClickListener {
    private ApiService apiService;
    private TextView tv_forget_password;
    private EditText ed_phone_number_ac_login;
    private EditText ed_pwd_ac_login;
    private Button bt_login;
    private ImageView iv_phone_clean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_onpasw);
        initTitle("加入一起记");
        apiService = RetrofitInstance.get().create(ApiService.class);
        initView();
    }

    private void initView() {

        ed_phone_number_ac_login = (EditText) findViewById(R.id.et_phone_number_ac_login);
        ed_pwd_ac_login = (EditText) findViewById(R.id.ed_pwd_ac_login);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_login.setOnClickListener(this);
        tv_forget_password = (TextView) findViewById(R.id.tv_forget_password);
        tv_forget_password.setOnClickListener(this);
        iv_phone_clean = (ImageView) findViewById(R.id.iv_phone_clean);
        iv_phone_clean.setOnClickListener(this);

        ed_phone_number_ac_login.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (RegexUtil.isMobileNO(s.toString())) {
                    ed_pwd_ac_login.requestFocus();
                }

                if (s.length() > 0) {
                    iv_phone_clean.setVisibility(View.VISIBLE);
                } else {
                    iv_phone_clean.setVisibility(View.GONE);
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

        ed_phone_number_ac_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_phone_number_ac_login.setCursorVisible(true);
            }
        });

        ed_pwd_ac_login.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub


                if (s.length() > 0) {
                    bt_login.setBackground(getResources().getDrawable(R.drawable.login_button_blue));
                } else {
                    bt_login.setBackground(getResources().getDrawable(R.drawable.send_out_message));
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
        switch (v.getId()) {
            case R.id.bt_login://帐号密码登录
                String mobile = ed_phone_number_ac_login.getText().toString();
                String userpass = ed_pwd_ac_login.getText().toString();

                if (!RegexUtil.isMobileNO(mobile)) {
                    AnimationUtil.shake(ed_phone_number_ac_login);
                    showToast("手机号格式不正确");
                    return;
                }

                if (!RegexUtil.isPasw(userpass)) {
                    AnimationUtil.shake(ed_pwd_ac_login);
                    showToast("密码格式有误,必须为6-18位数字或字母组合");
                    return;
                }
                XzbUtils.hidePointInUmg(mContext, Constants.HIDE_ACCOUNT_LOGIN);
                onLogin(mobile, userpass);

                break;

            case R.id.tv_forget_password:
                Intent intent = new Intent(LoginOnPaswActivity.this, ForgetLoginPasswordActivity.class);
                startActivity(intent);

                break;

            case R.id.iv_phone_clean:
                ed_phone_number_ac_login.setText("");
                iv_phone_clean.setVisibility(View.GONE);
                break;

        }
    }


    /**
     * 账号密码登录
     *
     * @param username
     * @param userpass
     */
    private void onLogin(String username, String userpass) {
        LoginManager.toLoginPassword(username, userpass, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog(true);
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
                            HomeActivity.openActivity(LoginOnPaswActivity.this, booksDbInfo);
                            return;
                        } else {
                            ChooseBookActivity.openActivity(LoginOnPaswActivity.this);
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

    }


}
