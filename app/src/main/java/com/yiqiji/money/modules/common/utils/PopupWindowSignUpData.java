package com.yiqiji.money.modules.common.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.widget.Button;
import android.widget.RadioGroup;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.view.CommonPopupWindow;

/**
 * Created by whl on 16/9/25.
 */
public class PopupWindowSignUpData implements RadioGroup.OnCheckedChangeListener {

	private CommonPopupWindow mApplayPopupWindow;
	private Activity mActivity;
	RadioGroup rg_host;

	@TargetApi(Build.VERSION_CODES.KITKAT)
	public void showPopupWindow() {
		if (mApplayPopupWindow == null) {
			// 实例化SelectPicPopupWindow
			mApplayPopupWindow = new CommonPopupWindow(mActivity, R.layout.dialog_cacpital);
		}
		mApplayPopupWindow.showAsDropDown(mActivity.findViewById(R.id.view_capital));
		mApplayPopupWindow.showAsDropDown(mActivity.findViewById(R.id.main_layout), 0, 0, Gravity.BOTTOM
				| Gravity.CENTER);

		/*
		 * View view =
		 * LayoutInflater.from(mActivity).inflate(R.layout.dd_sign_up_popupwindow
		 * , null); if (mApplayPopupWindow == null) { // 实例化SelectPicPopupWindow
		 * mApplayPopupWindow = new CommonPopupWindow(mActivity, view,
		 * R.style._AnimBottom); } // 显示窗口
		 * mApplayPopupWindow.showAtLocation(mActivity
		 * .findViewById(R.id.main_layout), Gravity.BOTTOM | Gravity.CENTER, 0,
		 * 0);
		 */

		rg_host = (RadioGroup) mApplayPopupWindow.view.findViewById(R.id.rg_host);
		Button dialog_capital_all = (Button) mApplayPopupWindow.view.findViewById(R.id.dialog_capital_all);
		Button dialog_capital_expenditure = (Button) mApplayPopupWindow.view
				.findViewById(R.id.dialog_capital_expenditure);
		Button dialog_capital_income = (Button) mApplayPopupWindow.view.findViewById(R.id.dialog_capital_income);
		rg_host.setOnCheckedChangeListener(this);
		rg_host.check(R.id.dialog_capital_all);

	}

	public PopupWindowSignUpData(Activity activity) {
		this.mActivity = activity;
	}

	/**
	 * dip转px
	 * 
	 * @param context
	 * @param dp
	 * @return
	 */
	public int Dp2Px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.dialog_capital_all:
			rg_host.check(R.id.dialog_capital_all);
			break;
		case R.id.dialog_capital_expenditure:
			rg_host.check(R.id.dialog_capital_expenditure);
			break;
		case R.id.dialog_capital_income:
			rg_host.check(R.id.dialog_capital_income);

			break;
		default:
			break;
		}
	}
}
