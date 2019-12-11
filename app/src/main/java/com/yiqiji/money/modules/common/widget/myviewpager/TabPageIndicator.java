/*
 * Copyright (C) 2011 The Android Open Source Project Copyright (C) 2011 Jake Wharton Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy
 * of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yiqiji.money.modules.common.widget.myviewpager;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.view.DateView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * This widget implements the dynamic action bar tab behavior that can change
 * across different configurations or circumstances.
 */
public class TabPageIndicator extends HorizontalScrollView implements PageIndicator {
    /**
     * Tie text used when no title is provided by the adapter.
     */
    private static final CharSequence EMPTY_TITLE = "";
    private float screenWith;

    /**
     * Interface for a callback when the selected tab has been reselected.
     */
    public interface OnTabReselectedListener {
        /**
         * Callback when the selected tab has been reselected.
         *
         * @param position Position of the current center item.
         */
        void onTabReselected(int position);
    }

    private Runnable mTabSelector;

    private final OnClickListener mTabClickListener = new OnClickListener() {
        public void onClick(View view) {
            DateView dateView = (DateView) view;
            final int oldSelected = mViewPager.getCurrentItem();
            final int newSelected = dateView.getIndex();
            mViewPager.setCurrentItem(newSelected);
            // if (/* oldSelected == newSelected && */mTabReselectedListener !=
            // null) {
            // mTabReselectedListener.onTabReselected(newSelected);
            //
            // PagerAdapter adapter = mViewPager.getAdapter();
            // for (int i = 0; i < adapter.getCount(); i++) {
            // if (newSelected == i) {
            // dateView.setIsSelect(true);
            // } else {
            // if (mTabLayout != null && mTabLayout.getChildAt(i) != null) {
            // ((DateView) mTabLayout.getChildAt(i)).setIsSelect(false);
            // }
            //
            // }
            // }
            // }
        }
    };

    private final IcsLinearLayout mTabLayout;

    private ViewPager mViewPager;
    private OnPageChangeListener mListener;

    private int mMaxTabWidth;
    private int mSelectedTabIndex;

    private OnTabReselectedListener mTabReselectedListener;

    public TabPageIndicator(Context context) {
        this(context, null);
    }

    public TabPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        screenWith = XzbUtils.getPhoneScreen((Activity) context).widthPixels;
        setHorizontalScrollBarEnabled(false);

        mTabLayout = new IcsLinearLayout(context, R.attr.vpiTabPageIndicatorStyle);
        // mTabLayout.setDividerDrawable(context.getResources().getDrawable(
        // R.drawable.arrow));
        addView(mTabLayout, new ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
    }

    public void setOnTabReselectedListener(OnTabReselectedListener listener) {
        mTabReselectedListener = listener;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
        setFillViewport(lockedExpanded);
        if (mTabLayout != null) {
            final int childCount = mTabLayout.getChildCount();
            if (childCount > 1 && (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
                if (childCount > 2) {
                    mMaxTabWidth = (int) (MeasureSpec.getSize(widthMeasureSpec) * 0.4f);
                } else {
                    mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
                }
            } else {
                mMaxTabWidth = -1;
            }

            final int oldWidth = getMeasuredWidth();
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            final int newWidth = getMeasuredWidth();

            if (lockedExpanded && oldWidth != newWidth) {
                // Recenter the tab display if we're at a new (scrollable) size.
                setCurrentItem(mSelectedTabIndex);
            }
        }

    }

    private void animateToTab(final int position) {
        if (mTabLayout != null) {
            final View tabView = mTabLayout.getChildAt(position);
            if (mTabSelector != null) {
                removeCallbacks(mTabSelector);
            }
            mTabSelector = new Runnable() {
                public void run() {
                    if (tabView == null) {
                        return;
                    }
                    final int scrollPos = tabView.getLeft() - (getWidth() - tabView.getWidth()) / 2;
                    smoothScrollTo(scrollPos, 0);
                    mTabSelector = null;
                }
            };
            post(mTabSelector);
        }

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mTabSelector != null) {
            // Re-post the selector we saved
            post(mTabSelector);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
    }

    private void addTab(int index, CharSequence text, int iconResId) {
        final DateView dateView = new DateView(getContext());
        dateView.mIndex = index;
        dateView.setFocusable(true);
        dateView.setOnClickListener(mTabClickListener);
        dateView.setText(text.toString());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        layoutParams.width = (int) (screenWith / 3);
        layoutParams.setMargins(10, 0, 10, 0);
        if (mTabLayout != null) {
            mTabLayout.addView(dateView, layoutParams);
        }

        // mTabLayout.addView(tabView, layoutParams);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // if (mListener != null) {
        // mListener.onPageScrollStateChanged(arg0);
        // }
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // if (mListener != null) {
        // mListener.onPageScrolled(arg0, arg1, arg2);
        // }
    }

    @Override
    public void onPageSelected(int arg0) {
        setCurrentItem(arg0);
        // if (mListener != null) {
        // mListener.onPageSelected(arg0);
        // }
        if (mTabReselectedListener != null) {
            mTabReselectedListener.onTabReselected(arg0);
        }
        // if (mTabLayout != null) {
        // int child = mTabLayout.getChildCount();
        // for (int i = 0; i < child; i++) {
        // DateView dateView = (DateView) mTabLayout.getChildAt(i);
        // final int newSelected = dateView.getIndex();
        // if (i == arg0) {
        //
        // dateView.setIsSelect(true);
        // } else {
        // dateView.setIsSelect(false);
        // }
        // }
        //
        // }
    }


    @Override
    public void setViewPager(ViewPager view) {
        if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }
        PagerAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        view.setOnPageChangeListener(this);
        notifyDataSetChanged();
    }


    public void notifyDataSetChanged() {
//        setPagerNotify();
        if (mTabLayout != null) {
            mTabLayout.removeAllViews();
        }

        PagerAdapter adapter = mViewPager.getAdapter();
        IconPagerAdapter iconAdapter = null;
        if (adapter instanceof IconPagerAdapter) {
            iconAdapter = (IconPagerAdapter) adapter;
        }
        final int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            CharSequence title = adapter.getPageTitle(i);
            if (title == null) {
                title = EMPTY_TITLE;
            }
            int iconResId = 0;
            if (iconAdapter != null) {
                iconResId = iconAdapter.getIconResId(i);
            }
            addTab(i, title, iconResId);
        }
        if (mSelectedTabIndex > count) {
            mSelectedTabIndex = count - 1;
        }
        try {
            setCurrentItem(mSelectedTabIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        requestLayout();
    }

    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            // throw new IllegalStateException("ViewPager has not been bound.");
            return;
        }
        mSelectedTabIndex = item;
        mViewPager.setCurrentItem(item);
        final int tabCount = mTabLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            final View child = mTabLayout.getChildAt(i);
            final boolean isSelected = (i == item);
            ((DateView) child).setSelected(isSelected);
            if (isSelected) {
                DateView dateView = (DateView) child;
                dateView.setIsSelect(true, getResources().getColor(R.color.context_color));
                dateView.setText(mViewPager.getAdapter().getPageTitle(i).toString());
                animateToTab(item);
            } else {
                DateView dateView = (DateView) child;
                dateView.setIsSelect(false, getResources().getColor(R.color.secondary_text));
            }
        }
    }

    public void setAnimateToTab(int item) {
        animateToTab(item);
    }

    public void setSelectedTabIndex(int index) {
        mSelectedTabIndex = index;
    }

    public int getCurrentIndex() {
        if (mViewPager == null) {
            return 0;
        }
        return mViewPager.getCurrentItem();
    }

    public int getcount() {
        if (mTabLayout == null) {
            return 0;
        }
        return mTabLayout.getChildCount();
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mListener = listener;
    }

    private int x;
    private int y;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        // switch (ev.getAction()) {
        // case MotionEvent.ACTION_DOWN:
        // x = (int) ev.getRawX();
        // y = (int) ev.getRawY();
        // return false;
        // break;
        // case MotionEvent.ACTION_MOVE:
        // int delay_x = (int) ev.getRawX() - x;
        // int delay_y = (int) ev.getRawY() - y;
        // if (Math.abs(delay_x) > Math.abs(delay_y)) {
        // return false;
        // }
        // break;
        //
        // default:
        // break;
        // }
        return false;
    }

    // private class TabView extends TextView {
    // private int mIndex;
    //
    // public TabView(Context context) {
    // super(context, null, R.attr.vpiTabPageIndicatorStyle);
    // }
    //
    // @Override
    // public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    //
    // // Re-measure if we went beyond our maximum size.
    // if (mMaxTabWidth > 0 && getMeasuredWidth() > mMaxTabWidth) {
    // super.onMeasure(MeasureSpec.makeMeasureSpec(mMaxTabWidth,
    // MeasureSpec.EXACTLY), heightMeasureSpec);
    // }
    // }
    //
    // public int getIndex() {
    // return mIndex;
    // }
    // }
}
