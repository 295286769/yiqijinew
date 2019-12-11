package com.yiqiji.money.modules.common.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class SwipeLayoutExpend extends LinearLayout {

	private ViewDragHelper viewDragHelper;
	private View contentView;
	private View actionView;
	private int dragDistance;
	private final double AUTO_OPEN_SPEED_LIMIT = 400.0;
	private int draggedX;
	private static int x;
	private int item_positions = -1;

	public void setItem_positions(int item_positions) {
		this.item_positions = item_positions;
	}

	public int getItem_positions() {
		return item_positions;
	}

	public SwipeLayoutExpend(Context context) {
		this(context, null);
	}

	public SwipeLayoutExpend(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public SwipeLayoutExpend(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		viewDragHelper = ViewDragHelper.create(this, new DragHelperCallback());
	}

	@Override
	protected void onFinishInflate() {
		contentView = getChildAt(0);
		actionView = getChildAt(1);
		actionView.setVisibility(GONE);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		dragDistance = actionView.getMeasuredWidth();
	}

	private class DragHelperCallback extends ViewDragHelper.Callback {

		/**
		 * 尝试捕获子view，一定要返回true
		 * 
		 * @param View
		 *            child 尝试捕获的view
		 * @param int pointerId 指示器id？ 这里可以决定哪个子view可以拖动
		 */
		@Override
		public boolean tryCaptureView(View view, int i) {

			return view == contentView || view == actionView;
		}

		@Override
		public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
			draggedX = left;
			if (changedView == contentView) {
				actionView.offsetLeftAndRight(dx);
			} else {
				contentView.offsetLeftAndRight(dx);
			}
			if (actionView.getVisibility() == View.GONE) {
				actionView.setVisibility(View.VISIBLE);
			}
			postInvalidate();
		}

		/**
		 * 处理水平方向上的拖动
		 * 
		 * @param View
		 *            child 被拖动到view
		 * @param int left 移动到达的x轴的距离
		 * @param int dx 建议的移动的x距离
		 */
		@Override
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			if (child == contentView) {
				final int leftBound = getPaddingLeft();
				final int minLeftBound = -leftBound - dragDistance;
				final int newLeft = Math.min(Math.max(minLeftBound, left), 0);
				// postInvalidate();
				return newLeft;
			} else {
				final int minLeftBound = getPaddingLeft() + contentView.getMeasuredWidth() - dragDistance;
				final int maxLeftBound = getPaddingLeft() + contentView.getMeasuredWidth() + getPaddingRight();
				final int newLeft = Math.min(Math.max(left, minLeftBound), maxLeftBound);
				// postInvalidate();
				return newLeft;
			}

		}

		@Override
		public int getViewHorizontalDragRange(View child) {
			return dragDistance;
		}

		@Override
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			super.onViewReleased(releasedChild, xvel, yvel);
			boolean settleToOpen = false;
			if (xvel > AUTO_OPEN_SPEED_LIMIT) {
				settleToOpen = false;
			} else if (xvel < -AUTO_OPEN_SPEED_LIMIT) {
				settleToOpen = true;
			} else if (draggedX <= -dragDistance / 2) {
				settleToOpen = true;
			} else if (draggedX > -dragDistance / 2) {
				settleToOpen = false;
			}

			int settleDestX = settleToOpen ? -dragDistance : 0;
			if (settleDestX != 0) {
				x = settleDestX;
			}
			if (draggedX != 0 && dragDistance == 0) {
				settleDestX = 0;

			}
			if (draggedX == 0 && dragDistance == 0) {
			}
			if (dragDistance != 0 && draggedX != 0 && draggedX == -dragDistance && settleToOpen) {
				settleDestX = 0;
			}

			viewDragHelper.smoothSlideViewTo(contentView, settleDestX, 0);
			ViewCompat.postInvalidateOnAnimation(SwipeLayoutExpend.this);
			clickItem.onClick(SwipeLayoutExpend.this, draggedX, xvel);
		}
	}

	public void setSmooth(boolean isShow) {
		if (!isShow) {
			viewDragHelper.smoothSlideViewTo(contentView, 0, 0);
			ViewCompat.postInvalidateOnAnimation(SwipeLayoutExpend.this);

		} else {
			if (x != 0) {
				viewDragHelper.smoothSlideViewTo(contentView, dragDistance, 0);
				ViewCompat.postInvalidateOnAnimation(SwipeLayoutExpend.this);
			}

		}
	}

	public void setSmooth() {
		// viewDragHelper.smoothSlideViewTo(contentView, 0, 0);
		actionView.setVisibility(View.GONE);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		if (viewDragHelper.shouldInterceptTouchEvent(ev)) {
			return true;
		}

		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		viewDragHelper.processTouchEvent(event);
		return true;
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
		if (viewDragHelper.continueSettling(true)) {
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	private OnClickItem clickItem;

	public void setOnClickItem(OnClickItem clickItem) {
		this.clickItem = clickItem;
	}

	public interface OnClickItem {
		void onClick(View view, float draggedX, float xvel);
	}

}
