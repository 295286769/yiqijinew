package com.yiqiji.money.modules.homeModule.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.inteface.ShowOrHind;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.money.modules.common.view.calenderview.DatePickerController;
import com.yiqiji.money.modules.common.view.calenderview.DayPickerView;
import com.yiqiji.money.modules.common.view.calenderview.SimpleMonthAdapter;

import java.util.Calendar;
import java.util.Date;


public class CalendarActivity extends BaseActivity implements DatePickerController, OnClickListener, ShowOrHind {
    private ImageView back;
    private DayPickerView pickView;
    private ImageView back_original;
    private TextView today;
    private RelativeLayout listHeight;
    private int day;
    private int month;
    private int year;
    private LinearLayoutManager layoutManager;
    private Date record_date;
    private int type;
    private long todayPosition;
    private int totay;
    private String bookName = "日常账本";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initView();
    }

    private void initView() {
        type = getIntent().getIntExtra("type", 0);
        record_date = (Date) getIntent().getExtras().getSerializable("date");
        pickView = (DayPickerView) findViewById(R.id.pickView);
        back_original = (ImageView) findViewById(R.id.back_original);
        back = (ImageView) findViewById(R.id.back);
        today = (TextView) findViewById(R.id.today);
        listHeight = (RelativeLayout) findViewById(R.id.listHeight);

        day = Integer.parseInt(DateUtil.formatTheDateToMM_dd(record_date, 2));
        month = Integer.parseInt(DateUtil.formatTheDateToMM_dd(record_date, 3));
        year = Integer.parseInt(DateUtil.formatTheDateToMM_dd(record_date, 5));
        todayPosition = 30 * 12;
        month = (30 - (Calendar.getInstance().get(Calendar.YEAR) - year)) * 12
                + (month - (Calendar.getInstance().get(Calendar.MONTH) + 1));

        pickView.setShowOrHind(CalendarActivity.this);
        pickView.setPosition(month, record_date, todayPosition);
        totay = month;
        pickView.setController(CalendarActivity.this);

        back_original.setOnClickListener(this);
        today.setOnClickListener(this);
        back.setOnClickListener(this);
        if (month - 1 > todayPosition) {// 向上
            back_original.setVisibility(View.VISIBLE);
            back_original.setImageResource(R.drawable.aircraft);
        } else if (month + 1 < todayPosition) {
            back_original.setVisibility(View.VISIBLE);
            back_original.setImageResource(R.drawable.aircraft_bottom);
        }
    }


    @SuppressWarnings("static-access")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        layoutManager = (LinearLayoutManager) pickView.getLayoutManager();

        View view = layoutManager.findViewByPosition(0);
        if (view != null) {

            layoutManager.scrollToPositionWithOffset(month, 0);

        }

    }

    @Override
    public int getMaxYear() {
        // TODO Auto-generated method stub
        return 2100;
    }

    @Override
    public void onDayOfMonthSelected(int year, int month, int day) {
        Date date = DateUtil.stringToDate(year + "-" + month + "-" + day);
        String dateString = year + "年" + month + "月" + day + "日";
        Intent intent = new Intent();
        intent.putExtra("dateString", dateString);
        Bundle bundle = new Bundle();
        bundle.putSerializable("date", date);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onDateRangeSelected(SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays) {

    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.today:
                Date date = new Date();
                String dateString = DateUtil.formatTheDateToMM_dd(date, 5) + "年" + DateUtil.formatTheDateToMM_dd(date, 3) + "月" + DateUtil.formatTheDateToMM_dd(date, 2) + "日";
                Intent intent = new Intent();
                intent.putExtra("dateString", dateString);
                Bundle bundle = new Bundle();
                bundle.putSerializable("date", date);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.back_original:
                if (layoutManager != null) {

                    layoutManager.scrollToPositionWithOffset((int) todayPosition, 0);
                    totay = (int) todayPosition;
                    back_original.setVisibility(View.GONE);

                }

                break;

            default:
                break;
        }

    }

    @Override
    public void show(boolean b) {
        if (back_original != null) {
            back_original.setVisibility(View.VISIBLE);
            if (b) {// 往下
                back_original.setVisibility(View.VISIBLE);
                back_original.setImageResource(R.drawable.aircraft_bottom);
            } else {
                back_original.setImageResource(R.drawable.aircraft);
            }

        }

    }

    @Override
    public void hide() {
        if (back_original != null) {
            back_original.setVisibility(View.GONE);
        }

    }

}
