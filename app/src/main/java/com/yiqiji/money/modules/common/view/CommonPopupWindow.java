package com.yiqiji.money.modules.common.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Created by whl on 16/9/25.
 */
public class CommonPopupWindow extends PopupWindow {
	private Activity mActivity;
	private LayoutInflater inflater;
	public View view;

	public CommonPopupWindow(Activity activity, int layoutInflater) {
		mActivity = activity;
		inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(layoutInflater, null);
		setCommonPopupWindow(activity, view, 0);
	}

	public CommonPopupWindow(Activity activity) {
		mActivity = activity;
	}

	/**
	 * @param activity
	 *            上下文
	 * @param popupwindowLayout
	 *            popupwindow所要显示的样式
	 * @param animationStyle
	 *            popupwindow动画弹出方式
	 */
	public CommonPopupWindow(Activity activity, int popupwindowLayout, int animationStyle) {
		super(activity);
		mActivity = activity;
		inflater = LayoutInflater.from(mActivity);
		view = inflater.inflate(popupwindowLayout, null);
		setCommonPopupWindow(activity, view, animationStyle);
	}

	public CommonPopupWindow(Activity activity, View view, int animationStyle) {
		super(activity);
		setCommonPopupWindow(activity, view, animationStyle);

	}

	public void setCommonPopupWindow(Activity activity, View view, int animationStyle) {
		mActivity = activity;
		this.view = view;
		// 重新编排布局，重新分配空间
		this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
				| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		// 再设置模式，和Activity的一样，覆盖。
		// this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		// 设置弹出窗体需要软键盘，
		this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		// 设置ApplayPopupWindow的View
		this.setContentView(view);
		// 设置ApplayPopupWindow弹出窗体的宽
		this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		// 设置ApplayPopupWindow弹出窗体的高
		this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
		// 设置ApplayPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(animationStyle);

		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0000000);
		// 设置ApplayPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// 设置点击窗口外边窗口消失
		this.setOutsideTouchable(true);
		this.update();

	}

}
