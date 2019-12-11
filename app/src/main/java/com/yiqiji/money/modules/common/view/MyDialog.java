package com.yiqiji.money.modules.common.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.yiqiji.money.R;

public class MyDialog extends Dialog {

	private Window window = null;
	private Context context;
	private boolean isCloseOutSide;
	private boolean isDialogNeedToShowInTheBottom;
	private boolean isNeedDialogShowFullWidth;
	private float fHeightF;
	private int fHeightExtra;

	public MyDialog(Context context) {
		super(context, R.style.My_CustomDialog);
		this.context = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		window = getWindow(); // 得到对话框
	}

	/**
     * 
     */
	public MyDialog(Context context, int theme) {
		// TODO Auto-generated constructor stub
		super(context, theme);
		this.context = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		window = getWindow(); // 得到对话框
	}

	public void isNeedDialogShowFullWidth(boolean needDialogShowFullWidth) {
		this.isNeedDialogShowFullWidth = needDialogShowFullWidth;
	}

	public void showDialog(int layoutResID) {
		setContentView(layoutResID);
		// 设置触摸对话框意外的地方取消对话框
		isCloseOutside(isCloseOutSide);
		show();
	}

	public void showDialog(View layoutResID) {
		isCloseOutside(isCloseOutSide);
		setContentView(layoutResID);
		show();
	}

	public void isCloseOutside(boolean isCanCloseOutside) {
		// 设置触摸对话框意外的地方取消对话框
		isCloseOutSide = isCanCloseOutside;
		setCanceledOnTouchOutside(isCloseOutSide);
	}

	public void isNeedDialogShowInBottom(boolean isNeedDialogShowInTheBottom) {
		this.isDialogNeedToShowInTheBottom = isNeedDialogShowInTheBottom;
	}

	public void setHeightFloat(float f) {
		fHeightF = f;
	}

	public void setHeightExtra(int extra) {
		fHeightExtra = extra;
	}

	@Override
	public void show() {
		if (isShowing()) {
			return;
		}
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		int width = displayMetrics.widthPixels;
		// int higth = displayMetrics.heightPixels;
		// window.setWindowAnimations(R.style.noDialogTheme); // 设置窗口弹出动画
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.height = LinearLayout.LayoutParams.WRAP_CONTENT;
		if (isNeedDialogShowFullWidth) {
			wl.width = width;
		} else {
			wl.width = (int) (width * 0.9);
		}
		if (fHeightF > 0f) {
			wl.height = (int) (wl.width * fHeightF) + fHeightExtra;
		} else {
			wl.height = LinearLayout.LayoutParams.WRAP_CONTENT;
		}
		if (isDialogNeedToShowInTheBottom) {
			window.setGravity(Gravity.BOTTOM);
		}
		window.setAttributes(wl);
		window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		super.show();
	}
}
