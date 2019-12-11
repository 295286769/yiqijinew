/***********************************************************************************
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2014 Robin Chutaux
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 ***********************************************************************************/
package com.yiqiji.money.modules.common.view.calenderview;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.inteface.ShowOrHind;

import java.util.Calendar;
import java.util.Date;


public class DayPickerView extends RecyclerView {
    protected Context mContext;
    protected SimpleMonthAdapter mAdapter;
    private DatePickerController mController;
    protected int mCurrentScrollState = 0;
    protected long mPreviousScrollPosition;
    protected int mPreviousScrollState = 0;
    private TypedArray typedArray;
    private OnScrollListener onScrollListener;
    private int day;
    private ShowOrHind showOrHind;
    private Date record_date;
    private float screeHeight = 0;
    private Calendar calendar;
    private long currentDayPosition = 0;
    private String bookName = "日常账本";


    public void setShowOrHind(ShowOrHind showOrHind) {
        this.showOrHind = showOrHind;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public DayPickerView(Context context) {
        this(context, null);
    }

    public DayPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayPickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.DayPickerView);
            setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            screeHeight = Utils.getPhoneScreen((Activity) context).heightPixels;
            calendar = Calendar.getInstance();
            init(context);
        }
    }

    public void setController(DatePickerController mController) {
        this.mController = mController;

        setUpAdapter();
        setAdapter(mAdapter);
        // this.smoothScrollToPosition(240);
    }

    private int position;// 当前日历index

    public void setPosition(int position, Date date, long currentDayPosition) {
        this.currentDayPosition = currentDayPosition;
        this.position = position;
        this.record_date = date;

    }

    private int scrollPosition = 0;

    public void recordDate(Date date) {

    }

    public void init(Context paramContext) {
        setLayoutManager(new LinearLayoutManager(paramContext));
        mContext = paramContext;

        onScrollListener = new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                final SimpleMonthView child = (SimpleMonthView) recyclerView.getChildAt(0);
                if (child == null) {
                    return;
                }
                mPreviousScrollPosition = dy;
                mPreviousScrollState = mCurrentScrollState;
                LinearLayoutManager layoutManager = (LinearLayoutManager) DayPickerView.this.getLayoutManager();
                int firstPosition = layoutManager.findFirstVisibleItemPosition();
                int lastPosition = layoutManager.findLastVisibleItemPosition();
                if (currentDayPosition != 0
                        && (currentDayPosition > firstPosition + 2 || currentDayPosition < lastPosition - 2)) {
                    if (position > firstPosition + 2) {
                        showOrHind.show(true);// 往下
                    } else if (position < lastPosition - 2) {
                        showOrHind.show(false);// 往上
                    }

                } else {
                    showOrHind.hide();
                }

                View view = layoutManager.findViewByPosition(firstPosition);

                calendar.set(Integer.parseInt(Utils.formatTheDateToMM_dd(record_date, 5)),
                        Integer.parseInt(Utils.formatTheDateToMM_dd(record_date, 3)),
                        Integer.parseInt(Utils.formatTheDateToMM_dd(record_date, 2)));
                if (view != null) {
                    int mo = position + firstPosition - position;
                    calendar.add(Calendar.MONTH, firstPosition - position);
                    for (int i = 0; i < lastPosition - firstPosition; i++) {
                        scrollPosition = mo + i;
                        calendar.add(Calendar.MONTH, i);
                        int year = calendar.get(Calendar.YEAR);
                        int moth = calendar.get(Calendar.MONTH);


                    }

                }
            }
        };
        setUpListView();
    }

    protected void setUpAdapter() {
        if (mAdapter == null) {
            mAdapter = new SimpleMonthAdapter(getContext(), mController, typedArray, position, record_date,
                    currentDayPosition, showOrHind);

        }
        mAdapter.notifyDataSetChanged();
    }

    protected void setUpListView() {
        setVerticalScrollBarEnabled(false);
        setOnScrollListener(onScrollListener);
        setFadingEdgeLength(0);

    }

    public SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> getSelectedDays() {
        return mAdapter.getSelectedDays();

    }

    protected DatePickerController getController() {
        return mController;
    }

    protected TypedArray getTypedArray() {
        return typedArray;
    }

}