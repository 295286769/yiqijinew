package com.yiqiji.money.modules.common.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class SignRecylerView extends RecyclerView {
	private int start_x;
	private int start_y;

	public SignRecylerView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public SignRecylerView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public SignRecylerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		switch (arg0.getAction()) {
		case MotionEvent.ACTION_DOWN:
			start_x = (int) arg0.getRawX();
			start_y = (int) arg0.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			int delet_x = (int) (arg0.getRawX() - start_x);
			int delet_y = (int) (arg0.getRawY() - start_y);
			if (Math.abs(delet_x) > 50) {
				return false;
			}
			break;
		case MotionEvent.ACTION_UP:

			break;

		default:
			break;
		}
		return super.onInterceptTouchEvent(arg0);
	}
	// @Override
	// public boolean onTouchEvent(MotionEvent arg0) {
	// switch (arg0.getAction()) {
	// case MotionEvent.ACTION_DOWN:
	// start_x = (int) arg0.getRawX();
	// start_y = (int) arg0.getRawY();
	// break;
	// case MotionEvent.ACTION_MOVE:
	// int delet_x = (int) (arg0.getRawX() - start_x);
	// int delet_y = (int) (arg0.getRawY() - start_y);
	// if (Math.abs(delet_x) > 0 && Math.abs(delet_y) < 20) {
	// return false;
	// }
	// break;
	// case MotionEvent.ACTION_UP:
	//
	// break;
	//
	// default:
	// break;
	// }
	// return super.onTouchEvent(arg0);
	//
	// }

}
