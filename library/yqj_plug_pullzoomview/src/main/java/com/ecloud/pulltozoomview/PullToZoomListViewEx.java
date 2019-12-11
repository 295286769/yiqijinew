package com.ecloud.pulltozoomview;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.ecloud.pulltozoomview.demo.R;

/**
 * Author: ZhuWenWu Version V1.0 Date: 2014/11/7 18:01. Description:
 * Modification History: Date Author Version Description
 * ------------------------
 * ----------------------------------------------------------- 2014/11/7
 * ZhuWenWu 1.0 1.0 Why & What is modified:
 */
public class PullToZoomListViewEx extends PullToZoomBase<ListView> implements
        AbsListView.OnScrollListener {
    private static final String TAG = PullToZoomListViewEx.class
            .getSimpleName();
    private FrameLayout mHeaderContainer;
    private int mHeaderHeight;
    private ScalingRunnable mScalingRunnable;
    /***/
    OnListViewScrollListenner onListViewScrollListenner;

    public void setOnListViewScrollListenner(
            OnListViewScrollListenner onListViewScrollListenner) {
        this.onListViewScrollListenner = onListViewScrollListenner;
    }

    public int getmHeaderHeight() {
        return mHeaderHeight;
    }

    /***/

    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float paramAnonymousFloat) {
            float f = paramAnonymousFloat - 1.0F;
            return 1.0F + f * (f * (f * (f * f)));
        }
    };

    public PullToZoomListViewEx(Context context) {
        this(context, null);
    }

    public PullToZoomListViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRootView.setOnScrollListener(this);
        mScalingRunnable = new ScalingRunnable();
    }

    public void setHeaderView(View headerView, View zoomView) {
        if (headerView != null) {
            this.mHeaderView = headerView;

        }
        if (zoomView != null) {
            this.mZoomView = zoomView;

        }
        updateHeaderView();
    }

    /**
     * 更新HeaderView 先移除-->再添加zoomView、HeaderView -->然后添加到listView的head 如果要兼容API
     * 9,需要修改此处逻辑，API 11以下不支持动态添加header
     */
    private void updateHeaderView() {
        if (mHeaderContainer != null) {
            mRootView.removeHeaderView(mHeaderContainer);
            mHeaderContainer.removeAllViews();
            if (mZoomView != null) {
                mHeaderContainer.addView(mZoomView);
            }
            if (mHeaderView != null) {
                mHeaderContainer.addView(mHeaderView);
            }
            mHeaderHeight = mHeaderContainer.getHeight();
            // Log.e(TAG,
            // "updateHeaderView  width = "+mHeaderContainer.getWidth()+" height = "+mHeaderContainer.getHeight()+" mHeaderHeight = "+mHeaderHeight);
            mRootView.addHeaderView(mHeaderContainer);
        }
    }

    public void setAdapter(ListAdapter adapter) {
        mRootView.setAdapter(adapter);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mRootView.setOnItemClickListener(listener);
    }

    /**
     * 创建listView 如果要兼容API9,需要修改此处
     *
     * @param context 上下文
     * @param attrs   AttributeSet
     * @return ListView
     */
    @Override
    protected ListView createRootView(Context context, AttributeSet attrs) {
        ListView listview = new ListView(context, attrs);
        /**
         * 添加一个id，防止一下崩溃
         * 详见：http://stackoverflow.com/questions/24297279/wrong-state
         * -class-expecting-view-state-but java.lang.RuntimeException: Unable to
         * start activity
         * ComponentInfo{cn.yangche51.app/cn.yangche51.app.modules
         * .common.A_MainActivity}: java.lang.RuntimeException: Unable to start
         * activity
         * ComponentInfo{cn.yangche51.app/cn.yangche51.app.modules.home.
         * activity.A_HomeNewActivity}: java.lang.IllegalArgumentException:
         * Wrong state class, expecting View State but received class
         * android.widget.AbsListView$SavedState instead. This usually happens
         * when two views of different type have the same id in the same
         * hierarchy. This view's id is id/mPullToZoomListViewEx. Make sure
         * other views do not use the same id.
         */
        listview.setId(R.id.listView_ID);
        return listview;
    }

    /**
     * 重置动画，自动滑动到顶部
     */
    @Override
    protected void smoothScrollToTop() {
        Log.d(TAG, "smoothScrollToTop --> ");
        mScalingRunnable.startAnimation(200L);
    }

    /**
     * zoomView动画逻辑
     *
     * @param newScrollValue 手指Y轴移动距离值
     */
    @Override
    protected void pullHeaderToZoom(int newScrollValue) {
        Log.d(TAG, "pullHeaderToZoom --> newScrollValue = " + newScrollValue);
        Log.d(TAG, "pullHeaderToZoom --> mHeaderHeight = " + mHeaderHeight);
        if (mScalingRunnable != null && !mScalingRunnable.isFinished()) {
            mScalingRunnable.abortAnimation();
        }

        ViewGroup.LayoutParams localLayoutParams = mHeaderContainer
                .getLayoutParams();
        localLayoutParams.height = Math.abs(newScrollValue) + mHeaderHeight;

        mHeaderContainer.setLayoutParams(localLayoutParams);
        // Log.e(TAG,
        // "pullHeaderToZoom  newScrollValue = "+newScrollValue+" mHeaderHeight = "+mHeaderHeight);
        // Log.e(TAG,
        // "pullHeaderToZoom  width = "+mHeaderContainer.getWidth()+" height = "+mHeaderContainer.getHeight());
    }

    @Override
    protected boolean isReadyForPullStart() {
        return isFirstItemVisible();
    }

    private boolean isFirstItemVisible() {
        final Adapter adapter = mRootView.getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            return true;
        } else {
            /**
             * This check should really just be:
             * mRootView.getFirstVisiblePosition() == 0, but PtRListView
             * internally use a HeaderView which messes the positions up. For
             * now we'll just add one to account for it and rely on the inner
             * condition which checks getTop().
             */
            if (mRootView.getFirstVisiblePosition() <= 1) {
                final View firstVisibleChild = mRootView.getChildAt(0);
                if (firstVisibleChild != null) {
                    return firstVisibleChild.getTop() >= mRootView.getTop();
                }
            }
        }

        return false;
    }

    @Override
    public void handleStyledAttributes(TypedArray a) {
        mHeaderContainer = new FrameLayout(getContext());
        // Log.e(TAG,
        // "handleStyledAttributes start width = "+mHeaderContainer.getWidth()+" height = "+mHeaderContainer.getHeight());
        // if (mZoomView != null) {
        // mHeaderContainer.addView(mZoomView);
        // }
        // if (mHeaderView != null) {
        // mHeaderContainer.addView(mHeaderView);
        // }
        //
        // mRootView.addHeaderView(mHeaderContainer);
        // Log.e(TAG,
        // "handleStyledAttributes end width = "+mHeaderContainer.getWidth()+" height = "+mHeaderContainer.getHeight());
    }

    /**
     * 设置HeaderView高度
     *
     * @param width  宽
     * @param height 高
     */
    public void setHeaderViewSize(int width, int height) {
        if (mHeaderContainer != null) {
            Object localObject = mHeaderContainer.getLayoutParams();
            if (localObject == null) {
                localObject = new AbsListView.LayoutParams(width, height);
            }
            ((ViewGroup.LayoutParams) localObject).width = width;
            ((ViewGroup.LayoutParams) localObject).height = height;
            mHeaderContainer
                    .setLayoutParams((ViewGroup.LayoutParams) localObject);
            mHeaderHeight = height;

            // Log.e(TAG,
            // "setHeaderViewSize set width = "+mHeaderContainer.getWidth()+" height = "+mHeaderContainer.getHeight()+" mHeaderHeight = "+mHeaderHeight);
        }
    }

    public void setHeaderLayoutParams(AbsListView.LayoutParams layoutParams) {
        if (mHeaderContainer != null) {
            mHeaderContainer.setLayoutParams(layoutParams);
            mHeaderHeight = layoutParams.height;
            // Log.e(TAG,
            // "setHeaderLayoutParams set width = "+mHeaderContainer.getWidth()+" height = "+mHeaderContainer.getHeight()+" mHeaderHeight = "+mHeaderHeight);
        }
    }

    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2,
                            int paramInt3, int paramInt4) {
        super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
        Log.d(TAG, "onLayout --> ");
        if (mHeaderHeight == 0 && mHeaderContainer != null) {
            mHeaderHeight = mHeaderContainer.getHeight();
            // Log.e(TAG,
            // "onLayout  width = "+mHeaderContainer.getWidth()+" height = "+mHeaderContainer.getHeight());
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.d(TAG, "onScrollStateChanged --> ");
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (mZoomView != null && !isHideHeader() && isPullToZoomEnabled()) {
            float f = mHeaderHeight - mHeaderContainer.getBottom();
            // Log.d(TAG, "onScroll --> f = " + f);
            // Log.e(TAG, "onScroll --> f = " +
            // f+" mHeaderHeight = "+mHeaderHeight+" mHeaderContainer.getBottom() = "+mHeaderContainer.getBottom());
            if (isParallax()) {
                if (onListViewScrollListenner != null) {
                    onListViewScrollListenner.onScroll((int) f,
                            firstVisibleItem, visibleItemCount, totalItemCount);
                }
                if ((f > 0.0F) && (f < mHeaderHeight)) {
                    int i = (int) (0.65D * f);
                    mHeaderContainer.scrollTo(0, -i);
                } else if (mHeaderContainer.getScrollY() != 0) {
                    mHeaderContainer.scrollTo(0, 0);
                }
            }
        }
    }

    class ScalingRunnable implements Runnable {
        protected long mDuration;
        protected boolean mIsFinished = true;
        protected float mScale;
        protected long mStartTime;

        ScalingRunnable() {
        }

        public void abortAnimation() {
            mIsFinished = true;
        }

        public boolean isFinished() {
            return mIsFinished;
        }

        public void run() {
            if (mZoomView != null) {
                float f2;
                ViewGroup.LayoutParams localLayoutParams;
                if ((!mIsFinished) && (mScale > 1.0D)) {
                    float f1 = ((float) SystemClock.currentThreadTimeMillis() - (float) mStartTime)
                            / (float) mDuration;
                    f2 = mScale
                            - (mScale - 1.0F)
                            * PullToZoomListViewEx.sInterpolator
                            .getInterpolation(f1);
                    localLayoutParams = mHeaderContainer.getLayoutParams();
                    Log.d(TAG, "ScalingRunnable --> f2 = " + f2);
                    if (f2 > 1.0F) {
                        localLayoutParams.height = ((int) (f2 * mHeaderHeight));
                        mHeaderContainer.setLayoutParams(localLayoutParams);
                        post(this);
                        return;
                    }
                    mIsFinished = true;
                }
            }
        }

        public void startAnimation(long paramLong) {
            if (mZoomView != null) {
                mStartTime = SystemClock.currentThreadTimeMillis();
                mDuration = paramLong;
                mScale = ((float) (mHeaderContainer.getBottom()) / mHeaderHeight);
                mIsFinished = false;
                post(this);
            }
        }
    }

    /**
     * 防止onRestoreInstanceState调用错误 具体错误查看方法：createRootView
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        try {
            super.onRestoreInstanceState(state);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
