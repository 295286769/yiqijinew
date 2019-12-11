package com.yiqiji.money.modules.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;


public class MyLinealayout extends LinearLayout {

	public MyLinealayout(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public MyLinealayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public MyLinealayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	private int y;
	private int deleY;

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			y = (int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:

			break;
		case MotionEvent.ACTION_UP:

			break;

		default:
			deleY = (int) ev.getRawY() - y;
			SwipeLayout view = (SwipeLayout) getChildAt(0);
			if (view != null && Math.abs(deleY) > 30) {
				view.setCanMove(false);
			}
			break;
		}// TODO Auto-gene
		return super.dispatchTouchEvent(ev);
	}

	// @Override
	// public boolean onInterceptTouchEvent(MotionEvent ev) {
	// switch (ev.getAction()) {
	// case MotionEvent.ACTION_DOWN:
	// y = (int) ev.getRawY();
	// break;
	// case MotionEvent.ACTION_MOVE:
	// deleY = (int) ev.getRawY() - y;
	// SwipeLayout view = (SwipeLayout) getChildAt(0);
	// if (view != null && Math.abs(deleY) < 30) {
	// view.setCanMove(false);
	// }
	// break;
	// case MotionEvent.ACTION_UP:
	//
	// break;
	//
	// default:
	// break;
	// }// TODO Auto-generated method stub
	// return super.onInterceptTouchEvent(ev);
	// }

}
