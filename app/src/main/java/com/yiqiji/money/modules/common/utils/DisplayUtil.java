package com.yiqiji.money.modules.common.utils;

import android.content.Context;

import com.yiqiji.money.modules.common.plication.MyApplicaction;

public class DisplayUtil {
	private static Context context;

	public static void getInstance() {
		context = MyApplicaction.getContext();

	}

	public static int getWidth() {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	public static int getHeight() {
		return context.getResources().getDisplayMetrics().heightPixels;
	}


}


