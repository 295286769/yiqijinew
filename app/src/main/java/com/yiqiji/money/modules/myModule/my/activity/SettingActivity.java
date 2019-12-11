package com.yiqiji.money.modules.myModule.my.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.activity.WebActivity;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.db.DailycostContract.DtBooksColumns;
import com.yiqiji.money.modules.common.db.DailycostEntity;
import com.yiqiji.money.modules.common.db.DbInterface;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.utils.DownUrlUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.myModule.login.activity.LoginBaseActivity;

import java.util.List;

@SuppressLint("HandlerLeak")
public class SettingActivity extends BaseActivity implements OnClickListener {
    private RelativeLayout rlExportDt;
    private RelativeLayout rl_setting_about;
    private RelativeLayout rl_setting_service;
    private Button rl_my_sign_out_login;
    private TextView tv_setting_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);
        rlExportDt = (RelativeLayout) findViewById(R.id.rlExportDt);
        rlExportDt.setOnClickListener(this);
        rl_setting_about = (RelativeLayout) findViewById(R.id.rl_setting_about);
        rl_setting_about.setOnClickListener(this);
        initTitle("设置");
        rl_setting_service = (RelativeLayout) findViewById(R.id.rl_setting_service);
        rl_setting_service.setOnClickListener(this);
        rl_my_sign_out_login = (Button) findViewById(R.id.rl_my_sign_out_login);
        tv_setting_version = (TextView) findViewById(R.id.tv_setting_version);
        tv_setting_version.setText(XzbUtils.getVersion(this));
        ;

        String tokenId = LoginConfig.getInstance().getTokenId();

        if (TextUtils.isEmpty(tokenId)) {
            rl_my_sign_out_login.setVisibility(View.GONE);
        } else {
            rl_my_sign_out_login.setVisibility(View.VISIBLE);
        }

        findViewById(R.id.rl_my_sign_out_login).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                getNoSubmitData();
            }
        });
    }

    private void getNoSubmitData() {
        new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated method stub

                String where = DtBooksColumns.ISSYNCHRONIZATION + "=?";
                String[] selectionArgs = new String[]{"false"};

                DownUrlUtil.seachUnSys(SettingActivity.this, null, mHandler);
                List<DailycostEntity> data = DbInterface.queryDailycostEntitys(where, selectionArgs, null, null, null,
                        null);

                Message msg = new Message();
                msg.what = 0;
                msg.obj = data;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    List<DailycostEntity> data = (List<DailycostEntity>) msg.obj;

                    if (data.size() > 0) {
                        showToast("数据未同步，请向下拉动首页进行同步");
                    } else {
                        showSimpleAlertDialog("提示", "您确定要退出么？", "确定", "取消", false, true, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String uuid = StringUtils.getUUID();//先从新获取deviceid后重新提交账本
                                // 此id用于生成用户唯一标识
                                LoginConfig.getInstance().setDeviceid(uuid);
                                //必须在之前清除
                                LoginConfig.getInstance().setTokenId("");
                                // BaserClassMode.addBooks(SettingActivity.this, mHandler);
                                LoginConfig.getInstance().setIssetnick(0);
                                LoginConfig.getInstance().setIssetavatar(0);
                                LoginConfig.getInstance().setUserinfojson("");
                                showToast("退出成功");
                                LoginBaseActivity.openActivity(SettingActivity.this);
//                                IntentUtils.startActivityOnLogin(SettingActivity.this, IntentUtils.LoginIntentType.SETTING);
                                LoginConfig.getInstance().setIsOutLogin(true);
                                dismissDialog();
                                finish();
                            }
                        }, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dismissDialog();
                            }
                        });

                    }
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = null;

        switch (v.getId()) {
            case R.id.rlExportDt://导出数据
                ExportDtActivity.open(this);
                break;
            case R.id.rl_setting_about:

                intent = new Intent(SettingActivity.this, AboutXzbActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_setting_service:

                Intent in = new Intent(SettingActivity.this, WebActivity.class);
                in.putExtra("url", Constants.BASE_URL + "/" + "agreement.html");
                in.putExtra("title", "服务协议");
                startActivity(in);

                break;
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }

    }

}
