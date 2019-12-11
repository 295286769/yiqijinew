package com.yiqiji.money.modules.common.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.yiqiji.money.R;

public class LoadingDialog extends Dialog {
	private static int default_width = 160;
	private static int default_height = 120;

	public LoadingDialog(Context context) {
		this(context, R.layout.dialog_layout, R.style.DialogTheme);
	}

	public LoadingDialog(Context context, int layout, int style) {
		this(context, default_width, default_height, layout, style);
	}

	public LoadingDialog(Context context, int width, int height, int layout, int style) {
		super(context, style);
		setContentView(layout);
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		float density = getDensity(context);
		params.width = (int) (width * density);
		params.height = (int) (height * density);
		params.gravity = Gravity.CENTER;
		window.setAttributes(params);

	}

	/**
	 * 
	 * 
	 * @param context
	 * @return
	 */
	public float getDensity(Context context) {
		Resources res = context.getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		return dm.density;
	}

}
