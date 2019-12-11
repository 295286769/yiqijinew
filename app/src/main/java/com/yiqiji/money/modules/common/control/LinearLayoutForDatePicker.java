package com.yiqiji.money.modules.common.control;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.utils.StringUtils;

import java.util.Calendar;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

/**
 * 类说明 : 三联滚轮DatePick控件
 *
 * @author xueyc
 * @date 2016-2-1
 */
public class LinearLayoutForDatePicker extends LinearLayout implements
        View.OnClickListener, OnWheelChangedListener {

    private LayoutParams lp;
    private Context context;
    private DatePickListener mListener;
    private Calendar mCurrentCalendar;// 当前日历
    private int mMaxYear, mMinYear, mMaxMonth, mMinMonth, mMaxDay, mMinDay;// 最大年份,最小年份
    private WheelView mViewYear;// 年
    private WheelView mViewMonth;// 月
    private WheelView mViewDay;// 日
    private Button btn_confirm;// 确定按钮
    private TextView tvTitle;// 显示title
    private boolean hasDay = true;// 是否存在天
    private boolean isHourAndMinute = false;
    private boolean isSingleItem = false;
    private View layout;

    public LinearLayoutForDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.setOrientation(VERTICAL);
        this.context = context;
        this.lp = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        this.setBackgroundResource(R.color.white);

        if (layout == null) {
            layout = LayoutInflater.from(context).inflate(
                    R.layout.control_data_picker, null);
        }

        tvTitle = (TextView) layout.findViewById(R.id.tvTitle);
        tvTitle.setText("请选择新车上路时间");

        btn_confirm = (Button) layout.findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);

        mViewYear = (WheelView) layout.findViewById(R.id.id_province);
        mViewYear.setVisibleItems(7);

        mViewMonth = (WheelView) layout.findViewById(R.id.id_city);
        mViewMonth.setVisibleItems(7);

        mViewDay = (WheelView) layout.findViewById(R.id.id_district);
        mViewDay.setVisibleItems(7);

        this.mMinYear = 1990;
        this.mMaxYear = 2050;
        this.mCurrentCalendar = Calendar.getInstance();// 初始化日历对象
        mViewYear.addChangingListener(this);
        mViewMonth.addChangingListener(this);
        mViewDay.addChangingListener(this);
        this.addView(layout);
    }

    public Calendar getCurrentCalendar() {
        return this.mCurrentCalendar;
    }

    /**
     * 设置当前的日历对象
     *
     * @param calendar 日历
     * @return
     */
    public void setCurrentCalendar(Calendar calendar) {
        this.mCurrentCalendar = calendar;
    }

    public void setItemTextRange(String[] items,int curItem) {
        mViewMonth.setVisibility(View.GONE);
        mViewDay.setVisibility(View.GONE);
        isHourAndMinute = true;
        isSingleItem = true;
        this.mViewYear.setViewAdapter(new ArrayWheelAdapter<>(
                this.getContext(), items));
        this.mViewYear.setCurrentItem(curItem);
    }


    /**
     * 设置当前选中的id
     */

    /**
     * 设置标题
     *
     * @param title
     * @return
     */
    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    /**
     * 设置dialog年份范围 [minYear,maxYear],年和月没有范围
     *
     * @param minYear 最小年份
     * @param maxYear 最大年份
     * @return
     */
    public void setYearRange(int minYear, int maxYear) {
        mMinYear = minYear;
        mMaxYear = maxYear;
        updateYear();
    }

    /**
     * 设置年月日范围
     *
     * @param minYear  最小日期对应的年
     * @param maxYear  最大日期对应的年
     * @param minMonth 最小日期对应的月
     * @param maxMonth 最大日期对应的月
     * @param minDay   最小日期对应的日
     * @param maxDay   最大日期对应的日
     * @return
     */
    public void setYearMonthDayRange(int minYear, int maxYear, int minMonth,
                                     int maxMonth, int minDay, int maxDay) {
        hasDay = true;
        mViewDay.setVisibility(View.VISIBLE);
        mMinYear = minYear;
        mMaxYear = maxYear;
        mMinMonth = minMonth;
        mMaxMonth = maxMonth;
        mMinDay = minDay;
        mMaxDay = maxDay;
        updateYear();
    }

    /**
     * 选择年和月
     */
    public void setYearMonthRange(int minYear, int maxYear, int minMonth,
                                  int maxMonth) {
        hasDay = false;
        mViewDay.removeChangingListener(this);
        mViewDay.setVisibility(View.GONE);
        mMinYear = minYear;
        mMaxYear = maxYear;
        mMinMonth = minMonth;
        mMaxMonth = maxMonth;
        updateYear();
    }

    /**
     * 选择小时和分钟
     */
    public void setHourMinuteRange(int minHour, int maxHour, int minMinute, int maxMinuite) {
        isHourAndMinute = true;
        isSingleItem = false;
        mViewDay.setVisibility(View.GONE);
        mMinYear = minHour;
        mMaxYear = maxHour;
        mMinMonth = minMinute;
        mMaxMonth = maxMinuite;
        upDataHour();
    }

    /**
     * 更新小时显示
     */
    private void upDataHour() {
        // 获取当前选中年
        Calendar curCalendar = this.mCurrentCalendar;// 当前日期保存了当前年月日
        int hour = curCalendar.get(Calendar.HOUR_OF_DAY);
        this.mViewYear.setViewAdapter(new NumericWheelAdapter(
                this.getContext(), this.mMinYear, this.mMaxYear));
        this.mViewYear.setCurrentItem(hour);

        updateMinute();
    }

    /**
     * 更新分钟显示
     */
    private void updateMinute() {
        // 获取当前选中年
        Calendar curCalendar = this.mCurrentCalendar;// 当前日期保存了当前年月日
        int minute = curCalendar.get(Calendar.MINUTE);
        this.mViewMonth.setViewAdapter(new NumericWheelAdapter(
                this.getContext(), this.mMinMonth, this.mMaxMonth));
        this.mViewMonth.setCurrentItem(minute);
    }

    /**
     * 更新年，年只执行一次
     */
    private void updateYear() {
        // 获取当前选中年
        Calendar curCalendar = this.mCurrentCalendar;// 当前日期保存了当前年月日
        int year = curCalendar.get(Calendar.YEAR);
        this.mViewYear.setViewAdapter(new NumericWheelAdapter(
                this.getContext(), this.mMinYear, this.mMaxYear, 1));
        this.mViewYear.setCurrentItem(this.mMaxYear - year);

        updateMonth();
    }

    /**
     * 更新月份
     */
    void updateMonth() {
        // 当只有一个年份时，设置最大年份和最小年份的范围
        if (mViewYear.getCurrentItem() == 0 && mViewYear.getCurrentItem() == mMaxYear - mMinYear) {
            mViewMonth.setViewAdapter(new NumericWheelAdapter(
                    this.getContext(), mMinMonth + 1, mMaxMonth + 1));
        } else if (mViewYear.getCurrentItem() == 0) {// 最大
            mViewMonth.setViewAdapter(new NumericWheelAdapter(
                    this.getContext(), 1, mMaxMonth + 1));
        } else if (mViewYear.getCurrentItem() == mMaxYear - mMinYear) {// 如果选中年份最小，则需设置年份范围
            mViewMonth.setViewAdapter(new NumericWheelAdapter(
                    this.getContext(), mMinMonth + 1, 12));
        } else {
            mViewMonth.setViewAdapter(new NumericWheelAdapter(
                    this.getContext(), 1, 12));
            mViewMonth.setCurrentItem(mCurrentCalendar.get(Calendar.MONTH));
        }

        mViewMonth.setCurrentItem(mCurrentCalendar.get(Calendar.MONTH));

        // 如果没有日期选择的话，则不需要显示日期
        updateDays();

    }

    /**
     * 根据年月更新day,正常情况
     */
    void updateDays() {
        if (!hasDay) {
            return;
        }
        Calendar calendar = Calendar.getInstance();
        int selectYear = StringUtils.toInt(mViewYear.getSelectedItemText());
        int selectMonth = StringUtils.toInt(mViewMonth.getSelectedItemText());
        calendar.set(Calendar.YEAR, selectYear);//当前年份
        calendar.set(Calendar.MONTH, selectMonth - 1);//当前月份-1
        int maxDay = calendar.getActualMaximum(Calendar.DATE);// 获取该年月下最大的day
        // 如果年设置成minYear,月设置成minMonth,则需要设置最小日
        if (mMinYear == mMaxYear && mMinDay == mMaxDay) {
            mViewDay.setViewAdapter(new NumericWheelAdapter(this.getContext(),
                    mMinDay, Math.min(mMaxDay, maxDay)));
            mViewDay.setCurrentItem(mCurrentCalendar.get(Calendar.DATE) - mMinDay);
        } else if (selectYear == mMinYear && selectMonth == mMinMonth + 1) {//最小年，最小月
            mViewDay.setViewAdapter(new NumericWheelAdapter(this.getContext(),
                    mMinDay, maxDay));// 设置日期的范围为1-最大的月
            mViewDay.setCurrentItem(mCurrentCalendar.get(Calendar.DATE) - mMinDay);
        } else if (selectYear == mMaxYear && selectMonth == mMaxMonth + 1) {// 最大年，最大月
            mViewDay.setViewAdapter(new NumericWheelAdapter(this.getContext(),
                    1, Math.min(maxDay, mMaxDay)));// 设置日期的范围为1-最大的月
            mViewDay.setCurrentItem(-1 + Math.min(Math.min(maxDay, mMaxDay), 1 + mViewDay.getCurrentItem()), true);// 日期会变化，28，29，30，31都会出现，防止出现数组越界
            mCurrentCalendar.set(Calendar.DATE, -1 + Math.min(Math.min(maxDay, mMaxDay), 1 + mViewDay.getCurrentItem()) + mMinDay);
        } else { // 选中正常年份
            mViewDay.setViewAdapter(new NumericWheelAdapter(this.getContext(), 1, maxDay));// 设置日期的范围为1-最大的月
            mViewDay.setCurrentItem(mCurrentCalendar.get(Calendar.DATE) - 1);// 日期会变化，28，29，30，31都会出现，防止出现数组越界
        }
    }

    /**
     * 确定按钮点击事件
     */
    public interface DatePickListener {
        public void DatePick(int leftValue, int middleValue, int rightValue,String itemText);
    }

    public void setDatePickListener(DatePickListener datePickListener) {
        mListener = datePickListener;
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (!isHourAndMinute) {
            if (wheel == mViewYear) {
                int year = StringUtils.toInt(mViewYear.getSelectedItemText());
                mCurrentCalendar.set(Calendar.YEAR, year);
                updateMonth();
            } else if (wheel == mViewMonth) {
                int month = StringUtils.toInt(mViewMonth.getSelectedItemText());
                mCurrentCalendar.set(Calendar.MONTH, month - 1);
                updateDays();
            } else if (wheel == mViewDay) {
                int day = StringUtils.toInt(mViewDay.getSelectedItemText());
                mCurrentCalendar.set(Calendar.DATE, day);
            }
        }
    }

        @Override
        public void onClick (View v){
            switch (v.getId()) {
                case R.id.btn_confirm:// 确定按钮点击
                    Calendar calendar = Calendar.getInstance();
                    if (!isHourAndMinute) {//不是小时和分钟
                        calendar.set(Calendar.YEAR, this.mMinYear + this.mViewYear.getCurrentItem());// 2000+i
                        calendar.set(Calendar.MONTH, this.mViewMonth.getCurrentItem());// j
                        calendar.set(Calendar.DATE, 1 + this.mViewDay.getCurrentItem());// k+1
                        this.mCurrentCalendar = calendar;
                        this.setVisibility(View.GONE);
                        if (this.mListener != null) {
                            NumericWheelAdapter yearAdapter = ((NumericWheelAdapter) mViewYear
                                    .getViewAdapter());
                            NumericWheelAdapter monthAdapter = ((NumericWheelAdapter) mViewMonth
                                    .getViewAdapter());
                            NumericWheelAdapter dayAdapter = ((NumericWheelAdapter) mViewDay
                                    .getViewAdapter());
                            int year = StringUtils.toInt(
                                    yearAdapter.getItemText(mViewYear.getCurrentItem())
                                            .toString(), 0);
                            int month = StringUtils.toInt(
                                    monthAdapter.getItemText(mViewMonth.getCurrentItem())
                                            .toString(), 0);
                            int day = 1;
                            if (hasDay) {
                                day = StringUtils.toInt(
                                        dayAdapter.getItemText(mViewDay.getCurrentItem())
                                                .toString(), 0);
                            }

                            this.mListener.DatePick(year, month, day,"");
                        }
                    } else {
                        if (!isSingleItem) {
                            calendar.set(Calendar.HOUR_OF_DAY, this.mViewYear.getCurrentItem());
                            calendar.set(Calendar.MINUTE, this.mViewMonth.getCurrentItem());
                            this.mCurrentCalendar = calendar;
                            this.setVisibility(View.GONE);
                            if (this.mListener != null) {
                                NumericWheelAdapter yearAdapter = ((NumericWheelAdapter) mViewYear
                                        .getViewAdapter());
                                NumericWheelAdapter monthAdapter = ((NumericWheelAdapter) mViewMonth
                                        .getViewAdapter());
                                int hour = StringUtils.toInt(
                                        yearAdapter.getItemText(mViewYear.getCurrentItem())
                                                .toString(), 0);
                                int minute = StringUtils.toInt(
                                        monthAdapter.getItemText(mViewMonth.getCurrentItem())
                                                .toString(), 0);
                                this.mListener.DatePick(hour, minute, 0,"");
                            }
                        } else {//
                            this.setVisibility(View.GONE);
                            if (this.mListener != null) {
                                ArrayWheelAdapter yearAdapter = ((ArrayWheelAdapter) mViewYear
                                        .getViewAdapter());
                                this.mListener.DatePick(0, 0, 0,yearAdapter.getItemText(mViewYear.getCurrentItem())
                                        .toString());
                            }
                        }

                    }

                    break;
            }
        }
    }
