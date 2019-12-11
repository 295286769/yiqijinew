package com.yiqiji.money.modules.common.control;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.utils.UIHelper;

import java.util.Calendar;

/**
 * 类说明 : 新三联滚轮日期选择Dialog
 */
public class CustomDatePickerDialog extends Dialog {
    private Context mContext;
    private LayoutInflater inflater;
    private LayoutParams lp;
    private LinearLayoutForDatePicker.DatePickListener mListener;
    private LinearLayoutForDatePicker datePicker;

    public CustomDatePickerDialog(Context context,
                                  final LinearLayoutForDatePicker.DatePickListener listener) {
        super(context, R.style.ActionSheetDialogStyle);
        this.mContext = context;
        this.mListener = listener;

        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(
                R.layout.control_data_layer_dialog, null);
        layout.setMinimumWidth(UIHelper.getDisplayWidth((Activity) mContext));
        datePicker = (LinearLayoutForDatePicker) layout
                .findViewById(R.id.llDatePicker);
        datePicker.setDatePickListener(mListener);
        setContentView(layout);

        // 设置window属性
        lp = getWindow().getAttributes();
        lp.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);// 设置是否点击对话框外部取消对话框
    }

    public Calendar getCurrentCalendar() {
        return datePicker.getCurrentCalendar();
    }

    /**
     * 设置当前的日历对象
     *
     * @param calendar 日历
     * @return
     */
    @Deprecated
    public CustomDatePickerDialog setCurrentCalendar(Calendar calendar) {
        datePicker.setCurrentCalendar(calendar);
        return this;
    }

    /**
     * 设置当前选中年月日
     * month 传进来是真是月份(1-12)
     * 没有day的话，写0
     */
    public CustomDatePickerDialog setCurrentSelectTime(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);//，如果转Calendar，需要减一(0-11)
        calendar.set(Calendar.DATE, day);
        datePicker.setCurrentCalendar(calendar);
        return this;
    }

    /**
     * 设置当前选中小时和分钟
     */
    public CustomDatePickerDialog setCurrentSelectTime(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);//，
        datePicker.setCurrentCalendar(calendar);
        return this;
    }

    /**
     * 设置当前小时和分钟
     */
    public CustomDatePickerDialog setHourMinuteRange(int minHour, int maxHour, int minMinute, int maxMinute) {
        datePicker.setHourMinuteRange(minHour, maxHour, minMinute, maxMinute);
        return this;
    }

    /**
     * 设置只有一个滚轮的记账提醒方式选择
     */
    public CustomDatePickerDialog setItemTextRange(String[] items,int curPos) {
        datePicker.setItemTextRange(items,curPos);
        return this;
    }

    /**
     * 设置标题
     *
     * @param title
     * @return
     */
    public CustomDatePickerDialog setTitle(String title) {
        datePicker.setTitle(title);
        return this;
    }

    /**
     * 设置dialog年份范围 [minYear,maxYear],年和月没有范围
     */
    public CustomDatePickerDialog setYearRange(int minYear, int maxYear) {
        datePicker.setYearRange(minYear, maxYear);
        return this;
    }

    /**
     * 选择年和月
     */
    public CustomDatePickerDialog setYearMonthDayRange(int minYear,
                                                       int maxYear, int minMonth, int maxMonth) {
        datePicker.setHourMinuteRange(minYear, maxYear, minMonth, maxMonth);
        return this;
    }

    /**
     * 设置年月日范围
     */
    public CustomDatePickerDialog setYearMonthDayRange(int minYear,
                                                       int maxYear, int minMonth, int maxMonth, int minDay, int maxDay) {
        datePicker.setYearMonthDayRange(minYear, maxYear, minMonth, maxMonth,
                minDay, maxDay);
        return this;
    }

    @Override
    public void dismiss() {
        if (!((Activity) mContext).isFinishing() && isShowing()) {
            super.dismiss();
        }
    }

    public void showDialog() {
        show();
    }

    @Override
    public void show() {
        if (datePicker != null && datePicker.getVisibility() != View.VISIBLE) {
            datePicker.setVisibility(View.VISIBLE);
        }
        if (!((Activity) mContext).isFinishing() && !isShowing()) {
            super.show();
        }
    }
}
