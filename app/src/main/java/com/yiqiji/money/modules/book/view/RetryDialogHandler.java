package com.yiqiji.money.modules.book.view;

import android.content.Context;
import android.view.View;

import com.yiqiji.money.modules.common.control.CustomAlertDialog;

/**
 * Created by leichi on 2017/5/26.
 */

public class RetryDialogHandler {

    CustomAlertDialog customAlertDialog;
    String title = "提示";
    String message = "请求服务器失败";
    String yesText = "重试";
    String cannelText = "退出";
    View.OnClickListener mOkclickListener;
    View.OnClickListener mCancelclickListener;
    Context mContext;

    public RetryDialogHandler(Context context) {
        this.mContext = context;
        initDialog();
    }

    private void initDialog() {
        customAlertDialog = new CustomAlertDialog(mContext);
        customAlertDialog.setTitle(title);
        customAlertDialog.setMessage(message);
        customAlertDialog.setRightButton(yesText, retryclickListener);
        customAlertDialog.setLeftButton(cannelText, exitclickListener);
        customAlertDialog.setCancelable(false);
    }

    public void setRetryclickListener(View.OnClickListener mOkclickListener) {
        this.mOkclickListener = mOkclickListener;
    }

    public void setExitlickListener(View.OnClickListener mCancelclickListener) {
        this.mCancelclickListener = mCancelclickListener;
    }


    View.OnClickListener retryclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            customAlertDialog.dismiss();
            if (mOkclickListener != null) {
                mOkclickListener.onClick(v);
            }
        }
    };


    View.OnClickListener exitclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            customAlertDialog.dismiss();
            if (mCancelclickListener != null) {
                mCancelclickListener.onClick(v);
            }
        }
    };


    public void showRetryDialog() {
        customAlertDialog.show();
    }

    public void showRetryDialog(String message) {
        customAlertDialog.setMessage(message);
        customAlertDialog.show();
    }
}
