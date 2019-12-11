package com.yiqiji.money.modules.common.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class FragmentViewpager extends ViewPager {

	public FragmentViewpager(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public FragmentViewpager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		switch (arg0.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) arg0.getRawX();
			break;
		case MotionEvent.ACTION_UP:
			deleX = (int) (arg0.getRawX() - downX);
			if (Math.abs(deleX) > 50) {
				return true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			deleX = (int) (arg0.getRawX() - downX);
			if (Math.abs(deleX) > 50) {
				return true;
			}
			break;

		default:
			break;
		}
		return super.onTouchEvent(arg0);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		switch (arg0.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) arg0.getRawX();
			break;
		case MotionEvent.ACTION_MOVE:
			deleX = (int) (arg0.getRawX() - downX);
			if (Math.abs(deleX) > 50) {
				return true;
			}
			break;

		default:
			break;
		}
		return super.onInterceptTouchEvent(arg0);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) ev.getRawX();
			break;
		case MotionEvent.ACTION_MOVE:
			deleX = (int) (ev.getRawX() - downX);
			if (Math.abs(deleX) > 50) {
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			deleX = (int) (ev.getRawX() - downX);
			if (Math.abs(deleX) > 50) {
				return true;
			}
			break;

		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	private int downX;
	private int deleX;

}
