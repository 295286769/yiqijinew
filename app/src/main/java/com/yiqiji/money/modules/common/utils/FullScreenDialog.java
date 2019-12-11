package com.yiqiji.money.modules.common.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.yiqiji.money.R;

/**
 * Created by Administrator on 2017/3/24.
 */
public class FullScreenDialog extends Dialog {
    Context mContext;

    public FullScreenDialog(Context context) {
        super(context, R.style.Transparent);
        this.mContext=context;


    }
    public void showDialog(int layoutResID) {
        setContentView(layoutResID);
        // 设置触摸对话框意外的地方取消对话框
        show();
    }

    public void showDialog(View layoutResID) {
        setContentView(layoutResID);
        show();
    }




    @Override
    public void show() {
        super.show();
        setCanceledOnTouchOutside(true);
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity= Gravity.BOTTOM;
        layoutParams.width= WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height= WindowManager.LayoutParams.WRAP_CONTENT;

        getWindow().getDecorView().setPadding(0, 0, 0, 0);

        getWindow().setAttributes(layoutParams);

    }
}
