package com.yiqiji.money.modules.myModule.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.entity.UserInfo;
import com.yiqiji.money.modules.common.request.ApiService;
import com.yiqiji.money.modules.common.request.RetrofitInstance;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.view.MyTitleLayout;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/8.
 */
public class ChangeActivity extends BaseActivity implements View.OnClickListener {
    private ApiService apiService;
    private MyTitleLayout my_title;
    private TextView tv_change_mobile;
    private Button bt_change_mobile;
    private ImageView iv_change_phone;
    private UserInfo userInfo;
    private TextView tv_change_text;
    private String ChangeType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        apiService = RetrofitInstance.get().create(ApiService.class);
        initView();
        userInfo = (UserInfo) getIntent().getParcelableExtra("userInfo");
        ChangeType = getIntent().getStringExtra("ChangeType");

        if (ChangeType.equals("phone")) {
            initTitle("绑定手机号码");
            bt_change_mobile.setText("更换手机号");
            iv_change_phone.setImageResource(R.drawable.change_phone);

            tv_change_mobile.setText(StringUtils.getStarsMobile(LoginConfig.getInstance().getMobile()));
            tv_change_text.setText("已绑定手机号");

        } else if (ChangeType.equals("wx")) {
            initTitle("绑定微信号");
            iv_change_phone.setImageResource(R.drawable.change_weixi);
            tv_change_text.setText("已绑定微信号");
            bt_change_mobile.setText("解除绑定");
            tv_change_mobile.setText(userInfo.getWxname());

        } else if (ChangeType.equals("qq")) {
            initTitle("绑定QQ号");
            iv_change_phone.setImageResource(R.drawable.change_qq);
            tv_change_text.setText("已绑定QQ号");
            bt_change_mobile.setText("解除绑定");
            tv_change_mobile.setText(userInfo.getQqname());

        } else if (ChangeType.equals("wb")) {
            initTitle("绑定微博号");
            iv_change_phone.setImageResource(R.drawable.change_weibo);
            tv_change_text.setText("已绑定微博号");
            bt_change_mobile.setText("解除绑定");
            tv_change_mobile.setText(userInfo.getWbname());
        }


    }

    private void initView() {
        iv_change_phone = (ImageView) findViewById(R.id.iv_change_phone);
        tv_change_mobile = (TextView) findViewById(R.id.tv_change_mobile);
        bt_change_mobile = (Button) findViewById(R.id.bt_change_mobile);
        tv_change_text = (TextView) findViewById(R.id.tv_change_text);
        bt_change_mobile.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_change_mobile:
                if (ChangeType.equals("phone")) {
                    Intent intent = new Intent(ChangeActivity.this, MobileChangeActivity.class);
                    startActivity(intent);
                } else if (ChangeType.equals("wx")) {
                    showDialog(userInfo.getWxid(), ChangeType, "确定要解除绑定当前的微信号吗");

                } else if (ChangeType.equals("qq")) {
                    showDialog(userInfo.getQqid(), ChangeType, "确定要解除绑定当前的QQ吗");

                } else if (ChangeType.equals("wb")) {
                    showDialog(userInfo.getWbid(), ChangeType, "确定要解除绑定当前的微博号吗");
                }

                break;
        }
    }


    private void showDialog(final String openid, final String ChangeType, final String title) {
        showSimpleAlertDialog("提示", title, "确定", "取消", false, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //解除绑定
                onUnbound(openid, ChangeType);
                dismissDialog();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });
    }

    /**
     * 解除绑定第三方
     *
     * @param openid
     * @param type
     */
    private void onUnbound(String openid, String type) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("openid", openid);
        hashMap.put("type", type);

        CommonFacade.getInstance().exec(Constants.UNBOUND, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog(true);
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                showToast("解除绑定成功");
                String tokenId = LoginConfig.getInstance().getTokenId();
                Intent intent = new Intent(ChangeActivity.this, AccountActivity.class);
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

//        HashMap<String, String> hashMap = XzbUtils.mapParmas();
//        String params[] = {openid, type, "tokenid", "plat", "deviceid", "appver", "osver", "machine"};
//
//        String[] valus = new String[]{openid, type, hashMap.get("tokenid"), hashMap.get("plat"), hashMap.get("deviceid"),
//                hashMap.get("appver"), hashMap.get("osver"), hashMap.get("machine")};
//
//        String sign = StringUtils.setSign(valus, params);
//        hashMap.put("sign", sign);
//        hashMap.put("openid", openid);
//        hashMap.put("type", type);
//
//        apiService.onUnbound(hashMap).enqueue(new BaseCallBack<ResponseBody>(this, true) {
//            @Override
//            public void onResponse(Response<ResponseBody> arg0, Retrofit arg1) {
//                super.onResponse(arg0, arg1);
//
//                try {
//                    JSONObject object = new JSONObject(arg0.body().string());
//
//                    if (object.getInt("code") == 0) {
//                        showToast("解除绑定成功");
//                        String tokenId = LoginConfig.getInstance().getTokenId();
//
//                        Intent intent = new Intent(ChangeActivity.this, AccountActivity.class);
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
//            }
//        });

    }


}
