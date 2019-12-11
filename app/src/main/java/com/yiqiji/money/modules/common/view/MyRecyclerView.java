package com.yiqiji.money.modules.common.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.yiqiji.money.modules.common.utils.DividerItemDecoration;

public class MyRecyclerView extends RecyclerView implements Pullable {

	private boolean isCanScroll = true;
	private boolean canPullDown = true;
	private boolean canPullUp = true;
	private boolean isLast = false;

	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}

	public void setCanPullDown(boolean canPullDown) {
		this.canPullDown = canPullDown;
	}

	public void setCanPullUp(boolean canPullUp) {
		this.canPullUp = canPullUp;
	}

	public MyRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		// isCanScroll = true;
	}

	public MyRecyclerView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		// isCanScroll = true;
	}


	private final int iOffY = 0;
	private int down = 0;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			down = (int) event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			int move = (int) event.getY() - down;
			if (!isCanScroll) {
				return true;
			}
			break;

		default:

			break;
		}

		return super.onTouchEvent(event);

	}

	public boolean isCanScroll() {
		return isCanScroll;
	}

	public void setCanScroll(boolean isCanScroll) {
		this.isCanScroll = isCanScroll;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if (!isCanScroll) {
			return false;
		}

		return super.onInterceptTouchEvent(arg0);
	}

	@Override
	public boolean canPullDown() {
		if (canPullDown) {
			if (getChildCount() == 0) {
				// 没有item的时候也可以下拉刷新
				return true;
			} else if (((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition() == 0
					&& getChildAt(0).getTop() >= 0) {
				// 滑到ListView的顶部了
				return true;
			} else
				return false;
		}
		return false;
	}

	@Override
	public boolean canPullUp() {
		if (canPullUp) {

			if (getChildCount() == 0) {
				// 没有item的时候也可以上拉加载
				return true;
			} else if (((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition() == (getAdapter()
					.getItemCount() - 1)) {
				// 滑到底部了
				if (getChildAt(((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition()
						- ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition()) != null
						&& getChildAt(
								((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition()
										- ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition())
								.getBottom() <= getMeasuredHeight())
					return true;
			}
		}

		return false;
	}

	public void setPidding(Context context, LayoutManager layoutManager) {
		DividerItemDecoration decor = new DividerItemDecoration(context, LinearLayoutManager.VERTICAL);

		addItemDecoration(decor);
	}

//	@Override
//	protected void onMeasure(int widthSpec, int heightSpec) {
//		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
//				MeasureSpec.AT_MOST);
//		super.onMeasure(widthSpec, expandSpec);
//	}

}
