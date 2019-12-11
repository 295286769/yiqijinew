package com.yiqiji.money.modules.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {

	public MyScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	private float xDistance, yDistance, xLast, yLast;
	private int startX;
	private int startY;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = (int) ev.getX();
			startY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:

			int dX = (int) (ev.getX() - startX);
			int dY = (int) (ev.getY() - startX);
			if (Math.abs(dX) > Math.abs(dY)) {// 左右滑动
				return false;
			} else {// 上下滑动
				return true;
			}
		case MotionEvent.ACTION_UP:
			break;
		}

		return super.onInterceptTouchEvent(ev);
	}

	public interface ScrollViewListener {

		void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy);

	}

	private ScrollViewListener scrollViewListener = null;

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		if (scrollViewListener != null) {
			scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
		}
	}

	public void setScrollViewListener(ScrollViewListener scrollViewListener) {
		this.scrollViewListener = scrollViewListener;
	}

}
