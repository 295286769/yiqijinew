package com.yiqiji.money.modules.common.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by whl on 16/9/24.
 */
public class CustomDialog extends Dialog {
    private Context mContext;

    public CustomDialog(Context context, int layoutId, int theme) {
        super(context, theme);
        this.mContext = context;
        setContentView(layoutId);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public CustomDialog(Context context, View view, int theme) {
        super(context, theme);
        this.mContext = context;
        setContentView(view);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private TextView tipTextView;

    public static void setDialogWidthFillParent(Activity mContex, CustomDialog dialog) {
        WindowManager windowManager = mContex.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); // 设置宽度
        dialog.getWindow().setAttributes(lp);
    }
}
