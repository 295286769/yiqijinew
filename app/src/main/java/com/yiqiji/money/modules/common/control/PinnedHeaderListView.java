package com.yiqiji.money.modules.common.control;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ListAdapter;
import android.widget.ListView;

/*
 * 头部悬浮的ListView
 * 
 * A ListView that maintains a header pinned at the top of the list. The
 * pinned header can be pushed up and dissolved as needed.
 *
 */
public class PinnedHeaderListView extends ListView {

    public boolean isDispatchPinnedLayer() {
        return isDispatchPinnedLayer;
    }

    public void setDispatchPinnedLayer(boolean dispatchPinnedLayer) {
        isDispatchPinnedLayer = dispatchPinnedLayer;
    }

    /**
     * Adapter interface. The list adapter must implement this interface.
     */
    public interface PinnedHeaderAdapter {

        /**
         * Pinned header state: don't show the header.
         */
        public static final int PINNED_HEADER_GONE = 0;

        /**
         * Pinned header state: show the header at the top of the list.
         */
        public static final int PINNED_HEADER_VISIBLE = 1;

        /**
         * Pinned header state: show the header. If the header extends beyond
         * the bottom of the first shown element, push it up and clip.
         */
        public static final int PINNED_HEADER_PUSHED_UP = 2;

        /**
         * Computes the desired state of the pinned header for the given
         * position of the first visible list item. Allowed return values are
         * {@link #PINNED_HEADER_GONE}, {@link #PINNED_HEADER_VISIBLE} or
         * {@link #PINNED_HEADER_PUSHED_UP}.
         */
        int getPinnedHeaderState(int position);

        /**
         * Configures the pinned header view to match the first visible list
         * item.
         *
         * @param header   pinned header view.
         * @param position position of the first visible list item.
         * @param alpha    fading of the header view, between 0 and 255.
         */
        void configurePinnedHeader(View header, int position, int alpha);
    }

    private static final int MAX_ALPHA = 255;// 透明度

    private PinnedHeaderAdapter mAdapter;// 适配器
    private View mHeaderView;// 头部视图
    private View mTouchTarget;
    public boolean mHeaderViewVisible;// 头部是否显示

    private int mHeaderViewWidth;// 头部视图的宽度

    private int mHeaderViewHeight;// 头部视图的高度

    private final Rect mTouchRect = new Rect();
    int mTranslateY;
    private MotionEvent mDownEvent;
    private final PointF mTouchPoint = new PointF();
    private int mTouchSlop;

    private boolean isDispatchPinnedLayer = true;//是否分发事件给悬浮层

    public PinnedHeaderListView(Context context) {
        this(context,null);
    }

    public PinnedHeaderListView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public PinnedHeaderListView(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    /**
     * 设置头部视图后，把该视图绘制到ListView中
     */
    public void setPinnedHeaderView(View view) {
        mHeaderView = view;
        // Disable vertical fading when the pinned header is present
        // TODO change ListView to allow separate measures for top and bottom
        // fading edge;
        // in this particular case we would like to disable the top, but not the
        // bottom edge.
        if (mHeaderView != null) {
            setFadingEdgeLength(0);
        }
        requestLayout();
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        mAdapter = (PinnedHeaderAdapter) adapter;
    }

    /**
     * 计算mHeaderView的宽度和高度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeaderView != null) {
            measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
            mHeaderViewWidth = mHeaderView.getMeasuredWidth();
            mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        }
    }

    /**
     * 根据布局设置mHeaderView的位置
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mHeaderView != null) {
            mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
            configureHeaderView(getFirstVisiblePosition());
        }
    }

    /**
     * 根据第一个显示的项的位置，配置mHeaderView状态和位置
     */
    public void configureHeaderView(int position) {
        if (mHeaderView == null) {
            return;
        }

        int state = mAdapter.getPinnedHeaderState(position);

        switch (state) {
            // 隐藏 mHeaderView
            case PinnedHeaderAdapter.PINNED_HEADER_GONE: {
                mHeaderViewVisible = false;
                break;
            }
            // 显示 mHeaderView，如果头部视图有偏移，则恢复
            case PinnedHeaderAdapter.PINNED_HEADER_VISIBLE: {
                mAdapter.configurePinnedHeader(mHeaderView, position, MAX_ALPHA);
                if (mHeaderView.getTop() != 0) {
                    mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
                }
                mHeaderViewVisible = true;
                break;
            }
            // 当Section项显示在第二项或者快要成为listview显示的第一项，推动mHeaderView向上移动
            case PinnedHeaderAdapter.PINNED_HEADER_PUSHED_UP: {
                View firstView = getChildAt(0);// 头部第一个View
                int bottom = firstView.getBottom();// 第一个view的底部到父布局顶部的高度
                // int itemHeight = firstView.getHeight();//第一个view高度
                int headerHeight = mHeaderView.getHeight();// 头部视图的高度
                int y;
                int alpha;
                // 如果 第一个布局底部到福布局顶部的高度未被头部视图覆盖，则依然显示顶部布局，如果覆盖了，则计算出覆盖量
                if (bottom < headerHeight) {
                    y = (bottom - headerHeight);
                    alpha = MAX_ALPHA * (headerHeight + y) / headerHeight;
                } else {
                    y = 0;
                    alpha = MAX_ALPHA;
                }
                mAdapter.configurePinnedHeader(mHeaderView, position, alpha);
                // 如果发生偏移，偏移量改变，则重新布局顶部布局
                if (mHeaderView.getTop() != y) {
                    mHeaderView.layout(0, y, mHeaderViewWidth, mHeaderViewHeight
                            + y);
                }
                mHeaderViewVisible = true;
                break;
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mHeaderViewVisible) {
            drawChild(canvas, mHeaderView, getDrawingTime());
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // 如果部分发给悬浮层，则不需进行下面操作
        if(!isDispatchPinnedLayer) {
            return super.dispatchTouchEvent(ev);
        }

        final float x = ev.getX();
        final float y = ev.getY();
        final int action = ev.getAction();

        if (action == MotionEvent.ACTION_DOWN
                && mTouchTarget == null && mHeaderView != null
                && isPinnedViewTouched(mHeaderView, x, y)) { // create touch target

            mTouchPoint.x = x;
            mTouchPoint.y = y;
            mTouchTarget = mHeaderView;

            mDownEvent = MotionEvent.obtain(ev);
        }

        if (mTouchTarget != null) {
            if (isPinnedViewTouched(mTouchTarget, x, y)) { // forward event to pinned view
                mTouchTarget.dispatchTouchEvent(ev);
            }

            if (action == MotionEvent.ACTION_UP) { // perform onClick on pinned view
                super.dispatchTouchEvent(ev);
                clearTouchTarget();

            } else if (action == MotionEvent.ACTION_CANCEL) { // cancel
                clearTouchTarget();

            } else if (action == MotionEvent.ACTION_MOVE) {
                if (Math.abs(y - mTouchPoint.y) > mTouchSlop) {
                    // cancel sequence on touch target
                    MotionEvent event = MotionEvent.obtain(ev);
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    mTouchTarget.dispatchTouchEvent(event);
                    event.recycle();
                    super.dispatchTouchEvent(mDownEvent);
                    super.dispatchTouchEvent(ev);
                    // provide correct sequence to super class for further handling
                    clearTouchTarget();
                }
            }
            return true;
        }

        return super.dispatchTouchEvent(ev);
    }

    private boolean isPinnedViewTouched(View view, float x, float y) {
        view.getHitRect(mTouchRect);
        mTouchRect.top += mTranslateY;
        mTouchRect.bottom += mTranslateY + getPaddingTop();
        mTouchRect.left += getPaddingLeft();
        mTouchRect.right -= getPaddingRight();
        return mTouchRect.contains((int) x, (int) y);
    }

    private void clearTouchTarget() {
        mTouchTarget = null;
        if (mDownEvent != null) {
            mDownEvent.recycle();
            mDownEvent = null;
        }
    }

}
