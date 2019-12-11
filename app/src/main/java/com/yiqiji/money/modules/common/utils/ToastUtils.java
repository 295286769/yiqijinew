package com.yiqiji.money.modules.common.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * 土司提示信息工具类 Created by ${huangweishui} on 2016/9/18. address
 * huang.weishui@71dai.com
 */
public class ToastUtils {
	public static void DiyToast(Context context, String message) {
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER, 0, 0);
		toast.show();
	}
}
