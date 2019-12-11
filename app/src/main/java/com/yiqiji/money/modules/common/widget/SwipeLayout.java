package com.yiqiji.money.modules.common.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 
 */
public class SwipeLayout extends LinearLayout {

	private ViewDragHelper viewDragHelper;
	private View contentView;
	private View actionView;
	private int dragDistance;
	private final double AUTO_OPEN_SPEED_LIMIT = 800.0;
	private int draggedX;
	private static int x;
	private int item_positions = -1;
	private int dymove = 0;
	private boolean canMove = true;

	public void setCanMove(boolean canMove) {
		this.canMove = canMove;
	}

	public void setItem_positions(int item_positions) {
		this.item_positions = item_positions;
	}

	public int getItem_positions() {
		return item_positions;
	}

	public SwipeLayout(Context context) {
		this(context, null);
	}

	public SwipeLayout(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
		public int clampViewPositionVertical(View child, int top, int dy) {
			dymove = dy;
			return 0;
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
			int position = Integer.parseInt(SwipeLayout.this.getTag().toString());
			int settleDestX = settleToOpen ? -dragDistance : 0;
			if (settleDestX != 0) {
				x = settleDestX;
				setItem_positions(Integer.parseInt(SwipeLayout.this.getTag().toString()));
			}
			if (draggedX != 0 && dragDistance == 0) {
				settleDestX = 0;
				position = -1;
			}
			//
			if (draggedX == 0 && dragDistance == 0) {
				position = -1;
			}
			if (dragDistance != 0 && draggedX != 0 && draggedX == -dragDistance && settleToOpen) {
				settleDestX = 0;
			}

			viewDragHelper.smoothSlideViewTo(contentView, settleDestX, 0);
			ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);

			clickItem.onClick(SwipeLayout.this, position, draggedX, xvel, dymove,
					Integer.parseInt(SwipeLayout.this.getTag().toString()));

			dymove = 0;

		}
	}

	public void setSmooth(boolean isShow) {
		if (!isShow) {
			viewDragHelper.smoothSlideViewTo(contentView, 0, 0);
			ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);

		} else {
			if (x != 0) {
				viewDragHelper.smoothSlideViewTo(contentView, dragDistance, 0);
				ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
			}

		}
	}

	public void setSmooth() {
		// viewDragHelper.smoothSlideViewTo(contentView, 0, 0);
		actionView.setVisibility(View.GONE);
	}

	// private boolean settleToOpen = false;
	// private float xv = 0;

	// public void setSmooth(boolean isShow) {
	//
	// int settleDestX = settleToOpen ? -dragDistance : 0;
	// if (isShow) {
	// // if (dragDistance > 0) {
	// // Log.i("tag", "============" + settleDestX);
	// viewDragHelper.smoothSlideViewTo(contentView, settleDestX, 0);
	// // } else if (dragDistance == 0 && settleToOpen) {
	// // if (xv < 0) {
	// // viewDragHelper.smoothSlideViewTo(contentView, -x, 0);
	// // } else {
	// // viewDragHelper.smoothSlideViewTo(contentView, x, 0);
	// // }
	// //
	// // } else if (settleToOpen && dragDistance < 0) {
	// // viewDragHelper.smoothSlideViewTo(contentView, settleDestX, 0);
	// // }
	//
	// } else {
	// viewDragHelper.smoothSlideViewTo(contentView, 0, 0);
	// }
	//
	// ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
	// }
	private int deley = 0;
	private int y = 0;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		if (/* viewDragHelper.shouldInterceptTouchEvent(ev) && */canMove) {

		}

		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (canMove) {
			viewDragHelper.processTouchEvent(event);
		}

		return canMove;
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
		void onClick(View view, int position, float draggedX, float xvel, int yvel, int positionIndex);
	}
}
