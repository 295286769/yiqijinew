package com.yiqiji.money.modules.myModule.my.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.control.CustomDatePickerDialog;
import com.yiqiji.money.modules.common.control.LinearLayoutForDatePicker;
import com.yiqiji.money.modules.common.control.LinearLayoutForItem;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.money.modules.myModule.my.adapter.SelDataEntity;
import com.yiqiji.money.modules.myModule.my.adapter.SelectDataAdapter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dansakai on 2017/6/28.
 * 选择日期（导出账本）
 */

public class SelectDataActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_item)
    LinearLayoutForItem llItem;
    @BindView(R.id.tvStime)
    TextView tvStime;
    @BindView(R.id.tvEtime)
    TextView tvEtime;
    @BindView(R.id.ll_data)
    LinearLayout llData;

    private List<SelDataEntity> mList = new ArrayList<>();
    private SelectDataAdapter adapter;
    private String[] titles = {"所有时间", "本月", "上个月", "今年", "自定义周期"};
    private CustomDatePickerDialog mDatePicker;
    private String limit;//期限类型

    private String startTime, endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_data);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("选择期限");
        String[] stats = DateUtil.getTodayDate().split("-");
        tvStime.setText(stats[0]+"年"+stats[1]+"月"+stats[2]+"日");
        String[] eds = DateUtil.getLastMonthDate().split("-");
        tvEtime.setText(eds[0]+"年"+eds[1]+"月"+eds[2]+"日");

        limit = getIntent().getStringExtra("limit");
        startTime = DateUtil.getTodayDate();
        endTime = DateUtil.getLastMonthDate();

        SelDataEntity entity = null;
        for (int i = 0; i < titles.length; i++) {
            if (i == Integer.parseInt(limit)) {
                entity = new SelDataEntity(titles[i], String.valueOf(i), 1);
            } else {
                entity = new SelDataEntity(titles[i], String.valueOf(i), 0);
            }
            mList.add(entity);
        }

        adapter = new SelectDataAdapter(mContext, mList);
        llItem.setOnItemClickLisntener(new LinearLayoutForItem.OnItemClickLisntener() {
            @Override
            public void onItemClick(int position) {
                for (int i = 0; i < mList.size(); i++) {
                    if (i == position) {
                        if (position == 4) {
                            llData.setVisibility(View.VISIBLE);
                        } else {
                            llData.setVisibility(View.GONE);
                        }
                        mList.get(i).setIsCheck(1);
                    } else {
                        mList.get(i).setIsCheck(0);
                    }
                }
                llItem.setAdapter(adapter);
            }
        });
        llItem.setAdapter(adapter);
    }

    /**
     * 显示隐藏日期布局
     */
    public void setVisibleSelData(int param) {
        llData.setVisibility(param);
    }

    /**
     * 开启当前activity
     */
    public static void open(Context context, int request_code, String limit) {
        Intent intent = new Intent(context, SelectDataActivity.class);
        intent.putExtra("limit", limit);
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, request_code);
        }
    }

    @OnClick({R.id.iv_back, R.id.rl_sTime, R.id.rl_eTime})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back://返回
                Intent intent = new Intent();
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).getIsCheck() == 1) {
                        intent.putExtra("SelData", mList.get(i));
                        if (mList.get(i).getLimit().equals("4")) {
                            intent.putExtra("sTime", tvStime.getText().toString());
                            intent.putExtra("eTime", tvEtime.getText().toString());
                            try {
                                if (DateUtil.compareTime(endTime, startTime)) {
                                    showToast("选择的结束时间要大于开始时间哦");
                                    return;
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.rl_sTime://选择开始时间
                mDatePicker = new CustomDatePickerDialog(mContext,
                        new LinearLayoutForDatePicker.DatePickListener() {

                            @Override
                            public void DatePick(int leftValue, int middleValue, int rightValue, String item) {
                                String mValue = "";
                                if (String.valueOf(middleValue).length() == 1) {
                                    mValue = "0" + String.valueOf(middleValue);
                                }
                                String rValue = "";
                                if (String.valueOf(rightValue).length() == 1) {
                                    rValue = "0" + String.valueOf(rightValue);
                                }
                                startTime = leftValue + "-" + mValue + "-" + rValue;
                                tvStime.setText(leftValue + "年" + mValue + "月" + rValue + "日");
                                mDatePicker.dismiss();
                            }

                        });
                mDatePicker
                        .setTitle("请开始时间")
                        .setYearMonthDayRange(1990, 2020, 1, 12, 1, 31);

                String[] datas = DateUtil.getTodayDate().split("-");

                if (datas != null && datas.length == 3) {
                    mDatePicker.setCurrentSelectTime(Integer.parseInt(datas[0]), Integer.parseInt(datas[1]), Integer.parseInt(datas[2]));
                }
                mDatePicker.showDialog();
                break;
            case R.id.rl_eTime://选择结束时间
                mDatePicker = new CustomDatePickerDialog(mContext,
                        new LinearLayoutForDatePicker.DatePickListener() {

                            @Override
                            public void DatePick(int leftValue, int middleValue, int rightValue, String item) {
                                String mValue = "";
                                if (String.valueOf(middleValue).length() == 1) {
                                    mValue = "0" + String.valueOf(middleValue);
                                }
                                String rValue = "";
                                if (String.valueOf(rightValue).length() == 1) {
                                    rValue = "0" + String.valueOf(rightValue);
                                }
                                endTime = leftValue + "-" + mValue + "-" + rValue;
                                tvEtime.setText(leftValue + "年" + mValue + "月" + rValue + "日");
                                mDatePicker.dismiss();
                            }

                        });
                mDatePicker
                        .setTitle("请结束时间")
                        .setYearMonthDayRange(1990, 2020, 1, 12, 1, 31);

                String[] dast = DateUtil.getLastMonthDate().split("-");

                if (dast != null && dast.length == 3) {
                    mDatePicker.setCurrentSelectTime(Integer.parseInt(dast[0]), Integer.parseInt(dast[1]), Integer.parseInt(dast[2]));
                }

                mDatePicker.showDialog();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDatePicker != null) {
            mDatePicker.dismiss();
        }
    }
}
