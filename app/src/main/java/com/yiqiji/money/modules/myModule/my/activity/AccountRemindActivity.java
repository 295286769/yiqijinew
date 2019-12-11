package com.yiqiji.money.modules.myModule.my.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.control.CustomDatePickerDialog;
import com.yiqiji.money.modules.common.control.LinearLayoutForDatePicker;
import com.yiqiji.money.modules.common.utils.SPUtils;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.myModule.common.RemindReceiver;

import java.util.Calendar;
import java.util.TimeZone;


/**
 * Created by dansakai on 2017/5/15.
 * 记账提醒
 */

public class AccountRemindActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivBack;
    private TextView tvTitle;
    private RelativeLayout rlRemind;
    private ToggleButton tgBtn;
    private LinearLayout ll_remind;
    private RelativeLayout rlRemindTime;
    private TextView tvTime;
    private RelativeLayout rlRemindType;
    private TextView tvType;

    private CustomDatePickerDialog mDatePicker, methodPicker;

    private boolean isRemind = true;//是否提醒

    private Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountmind);
        initView();
    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("记账提醒");
        rlRemind = (RelativeLayout) findViewById(R.id.rl_remind);
        rlRemind.setOnClickListener(this);
        isRemind = (boolean) SPUtils.getParam(LoginConfig.getInstance().getUserid() + "isRemind", true);

        tgBtn = (ToggleButton) findViewById(R.id.tg_btn);
        tgBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!XzbUtils.areNotificationsEnabled(mContext)) {
                        showSimpleAlertDialog(null, "请在手机的“设置→一起记→通知”选项中，允许向您推送记账消息", "现在设置", "我知道了", false, false, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                startActivity(intent);
                                dismissDialog();
                                tgBtn.setChecked(false);
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dismissDialog();
                                tgBtn.setChecked(false);
                            }
                        });
                    } else {
                        ll_remind.setVisibility(View.VISIBLE);
                        String key = LoginConfig.getInstance().getUserid() + "isRemind";
                        SPUtils.setParam(key, true);
                        startRemind();
                    }

                } else {
                    ll_remind.setVisibility(View.GONE);
                    String key = LoginConfig.getInstance().getUserid() + "isRemind";
                    SPUtils.setParam(key, false);
                    stopRemind();
                }
            }
        });
        ll_remind = (LinearLayout) findViewById(R.id.ll_remind);
        rlRemindTime = (RelativeLayout) findViewById(R.id.rl_remindTime);
        rlRemindTime.setOnClickListener(this);
        tvTime = (TextView) findViewById(R.id.tv_time);
        rlRemindType = (RelativeLayout) findViewById(R.id.rl_remindType);
        rlRemindType.setOnClickListener(this);
        tvType = (TextView) findViewById(R.id.tv_type);
        String remindTime = (String) SPUtils.getParam(LoginConfig.getInstance().getUserid() + "remindTime", "19:30");
        tvTime.setText(remindTime);
        String remindType = (String) SPUtils.getParam(LoginConfig.getInstance().getUserid() + "remindType", "每天");
        tvType.setText(remindType);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRemind) {
            if (!XzbUtils.areNotificationsEnabled(mContext)) {
                tgBtn.setChecked(false);
            } else {
                tgBtn.setChecked(true);
                ll_remind.setVisibility(View.VISIBLE);
                startRemind();
            }
        } else {
            tgBtn.setChecked(false);
            ll_remind.setVisibility(View.GONE);
            stopRemind();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_remindTime://提醒时间
                // 创建并且显示对话框
                if (mDatePicker == null) {
                    String[] datas = tvTime.getText().toString().split(":");

                    mDatePicker = new CustomDatePickerDialog(mContext,
                            new LinearLayoutForDatePicker.DatePickListener() {

                                @Override
                                public void DatePick(int leftValue, int middleValue, int rightValue, String item) {
                                    String hour, minute;
                                    if (leftValue < 10) {
                                        hour = "0" + leftValue;
                                    } else {
                                        hour = leftValue + "";
                                    }
                                    if (middleValue < 10) {
                                        minute = "0" + middleValue;
                                    } else {
                                        minute = middleValue + "";
                                    }
                                    tvTime.setText(hour + ":" + minute);
                                    SPUtils.setParam(LoginConfig.getInstance().getUserid() + "remindTime", hour + ":" + minute);
                                    SPUtils.setParam(LoginConfig.getInstance().getUserid() + "remindDay", System.currentTimeMillis());
                                    mDatePicker.dismiss();
                                    startRemind();
                                }
                            });
                    mDatePicker
                            .setTitle("请选择提醒时间")
                            .setCurrentSelectTime(Integer.parseInt(datas[0]), Integer.parseInt(datas[1]))
                            .setHourMinuteRange(00, 23, 00, 59);
                }

                mDatePicker.showDialog();
                break;
            case R.id.rl_remindType://提醒方式
                // 创建并且显示对话框
                if (methodPicker == null) {
                    methodPicker = new CustomDatePickerDialog(mContext,
                            new LinearLayoutForDatePicker.DatePickListener() {

                                @Override
                                public void DatePick(int leftValue, int middleValue, int rightValue, String itemtetx) {
                                    tvType.setText(itemtetx);
                                    SPUtils.setParam(LoginConfig.getInstance().getUserid() + "remindType", itemtetx);
                                    SPUtils.setParam(LoginConfig.getInstance().getUserid() + "remindDay", System.currentTimeMillis());
                                    methodPicker.dismiss();
                                    startRemind();
                                }
                            });
                    methodPicker
                            .setTitle("请选择提醒方式")
                            .setItemTextRange(new String[]{"每天", "每周", "智能"}, getCurPos(tvType.getText().toString()));
                }

                methodPicker.showDialog();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDatePicker != null && mDatePicker.isShowing()) {
            mDatePicker.dismiss();
            mDatePicker = null;
        }
        if (methodPicker != null && methodPicker.isShowing()) {
            methodPicker.dismiss();
            methodPicker = null;
        }
    }

    private int getCurPos(String type) {
        int pos = 0;
        switch (type) {
            case "每天":
                pos = 0;
                break;
            case "每周":
                pos = 1;
                break;
            case "智能":
                pos = 2;
                break;
        }
        return pos;
    }

    /**
     * 开启提醒
     */
    private void startRemind() {

        String time = (String) SPUtils.getParam(LoginConfig.getInstance().getUserid() + "remindTime", "19:30");

        String[] times = time.split(":");

        mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis((Long) SPUtils.getParam(LoginConfig.getInstance().getUserid() + "remindDay", System.currentTimeMillis()));
        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        mCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[0]));
        mCalendar.set(Calendar.MINUTE, Integer.parseInt(times[1]));
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);
        Intent intent = new Intent(mContext, RemindReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        int day;
        if (SPUtils.getParam(LoginConfig.getInstance().getUserid() + "remindType", "每天").equals("每周")) {
            day = 7;
        } else {
            day = 1;
        }

//        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(),
//                AlarmManager.INTERVAL_DAY, pi);
//        am.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(),  pi);
        am.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), (1000 * 60 * 60 * 24 * day), pi);

    }


    /**
     * 关闭提醒
     */
    private void stopRemind() {

        Intent intent = new Intent(mContext, RemindReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(mContext, 0,
                intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        //取消警报
        am.cancel(pi);
    }
}
