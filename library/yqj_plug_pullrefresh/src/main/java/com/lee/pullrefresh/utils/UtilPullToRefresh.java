package com.lee.pullrefresh.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ListView;

import com.lee.pullrefresh.ui.PullToRefreshBase;

public class UtilPullToRefresh {

    private static SimpleDateFormat mDateFormat = new SimpleDateFormat(
            "MM-dd HH:mm");

    public static void refreshComplete(PullToRefreshBase mPullView) {
        mPullView.onPullDownRefreshComplete();
        mPullView.onPullUpRefreshComplete();
        setLastUpdateTime(mPullView);
    }

    private static void setLastUpdateTime(PullToRefreshBase mPullView) {
        String text = mDateFormat.format(new Date(System.currentTimeMillis()));
        mPullView.setLastUpdatedLabel(text);
    }

    public static void clearListViewStyle(ListView mListView) {
        mListView.setVerticalScrollBarEnabled(false);
        mListView.setFooterDividersEnabled(false);
        mListView.setScrollingCacheEnabled(false);
        mListView.setCacheColorHint(0);
        mListView.setFadingEdgeLength(0);
        mListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }
}
