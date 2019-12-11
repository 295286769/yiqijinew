package com.yiqiji.money.modules.common.control;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yiqiji.money.R;

/**
 * 加载对话框,内部包括一个A_JDProgressBar和一个文案提示TextView
 */
public class A_LoadingDialog extends Dialog {

    private TextView tvLoadingText;// 加载框下面加了个显示文案的提示
    private Activity mActivity;

    public A_LoadingDialog(Context context) {
        super(context);
//        super(context, R.style.Dialog);
//        setContentView(R.layout.a_widget_loadingdialog);
//        tvLoadingText = (TextView) findViewById(R.id.tvLoadingText);
//        mActivity = (Activity) context;
    }

    @Override
    public void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
    }

    /**
     * 显示加载文案
     *
     * @param msg 加载文案
     */
    public void show(String msg) {
        tvLoadingText.setVisibility(View.VISIBLE);
        tvLoadingText.setText(msg);
        this.show();
    }

    @Override
    public void show() {
        if (!mActivity.isFinishing() && !isShowing()) {
            super.show();
        }
    }

}
