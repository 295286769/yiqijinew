package com.yiqiji.money.modules.myModule.login.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.entity.eventbean.UserInfoChangeEvent;
import com.yiqiji.money.modules.common.request.ApiService;
import com.yiqiji.money.modules.common.request.RetrofitInstance;
import com.yiqiji.money.modules.myModule.my.wegit.MyMaxLengthWatcher;

import java.util.HashMap;

import de.greenrobot.event.EventBus;

public class SettingUserNameActivity extends BaseActivity {
    private ApiService apiService;
    private EditText et_user_name;
    private String userName;
    InputMethodManager inputMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        userName = getIntent().getStringExtra("username");
        setContentView(R.layout.activity_setting_user_name);
        et_user_name = (EditText) findViewById(R.id.et_user_name);
        et_user_name.setText(userName);
        et_user_name.setSelection(userName.length());// 将光标移至文字末尾

        inputMgr = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);// 调用此方法才能自动打开输入法软键盘
        apiService = RetrofitInstance.get().create(ApiService.class);
        initView();
        initTitle("修改昵称", "保存", new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.layout_title_view_right_btn) {

                    String username = et_user_name.getText().toString().trim();
                    if (TextUtils.isEmpty(username)) {
                        showToast("请输入昵称");
                        return;
                    }

                    if (username.equals(userName)) {
                        outkeKyboard();
                        finish();
                        return;
                    }

                    if (username.length() > 10) {
                        showToast("用户名不能超过10个字符");
                        return;
                    }
                    setUsername(username);
                }

                if (v.getId() == R.id.layout_title_view_return) {
                    outkeKyboard();
                    finish();
                }
            }
        });

        et_user_name.addTextChangedListener(new MyMaxLengthWatcher(10, et_user_name, "用户名不能超过10个字符"));

    }

    private void initView() {
//        MyTitleLayout titleLayout = (MyTitleLayout) findViewById(R.id.my_title);
//        titleLayout.setTitle("修改昵称");
//        titleLayout.showRightBtn("保存");
//        titleLayout.setBackgroudColor(getResources().getColor(R.color.main_back), getResources()
//                .getColor(R.color.white));
//        titleLayout.setListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                if (v.getId() == R.id.layout_title_view_right_btn) {
//
//                    String username = et_user_name.getText().toString();
//                    if (TextUtils.isEmpty(username)) {
//                        showToast("请输入昵称");
//                        return;
//                    }
//                    setUsername(username);
//                }
//                if (v.getId() == R.id.layout_title_view_return) {
//                    finish();
//                }
//            }
//        });
    }

    private void setUsername(final String username) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("username", username);

        CommonFacade.getInstance().exec(Constants.USERPASS_SET_USERNAME, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog(true);
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                LoginConfig.getInstance().setUserName(username);
                LoginConfig.getInstance().setIssetnick(1);
                inputMgr.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // 关闭软件盘
                setResult(RESULT_OK);
                showToast("修改昵称成功");
                EventBus.getDefault().post(new UserInfoChangeEvent());
                finish();
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
//        String params[] = {"username", "tokenid", "plat", "deviceid", "appver", "osver", "machine"};
//
//        String[] valus = new String[]{username, hashMap.get("tokenid"), hashMap.get("plat"), hashMap.get("deviceid"),
//                hashMap.get("appver"), hashMap.get("osver"), hashMap.get("machine")};
//
//        String sign = StringUtils.setSign(valus, params);
//        hashMap.put("sign", sign);
//        hashMap.put("username", username);
//        apiService.setUserName(hashMap).enqueue(new BaseCallBack<ResponseBody>(this, true) {
//            @Override
//            public void onResponse(Response<ResponseBody> arg0, Retrofit arg1) {
//                // TODO Auto-generated method stub
//                super.onResponse(arg0, arg1);
//                try {
//                    JSONObject object = new JSONObject(arg0.body().string());
//                    if (object.getInt("code") == 0) {
//                        LoginConfig.getInstance().setUserName(username);
//                        setResult(RESULT_OK);
//                        finish();
//                    } else if (object.getInt("code") == 20010) {
//
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

    private boolean outkeyboard = true;

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        outkeyboard = false;
    }

    private void outkeKyboard() {
        if (outkeyboard) {
            inputMgr.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // 关闭软件盘
        }
    }

}
