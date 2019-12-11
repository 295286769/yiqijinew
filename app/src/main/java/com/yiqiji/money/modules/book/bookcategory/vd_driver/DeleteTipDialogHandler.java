package com.yiqiji.money.modules.book.bookcategory.vd_driver;

import android.content.Context;
import android.view.View;

import com.yiqiji.money.modules.common.control.CustomAlertDialog;

/**
 * Created by leichi on 2017/5/31.
 */

public class DeleteTipDialogHandler {
    CustomAlertDialog customAlertDialog;
    String title = "确认删除分类";
    String yesText = "确定删除";
    String cannelText = "取消";
    View.OnClickListener mOkclickListener;
    View.OnClickListener mCancelclickListener;
    Context mContext;

    public DeleteTipDialogHandler(Context context) {
        this.mContext = context;
        initDialog();
    }

    public void initDialog() {
        customAlertDialog = new CustomAlertDialog(mContext);
        customAlertDialog.setTitle(title);
        customAlertDialog.setRightButton(yesText, retryclickListener);
        customAlertDialog.setLeftButton(cannelText, exitclickListener);
        customAlertDialog.setCancelable(false);
    }

    public void setConfirmDeleteclickListener(View.OnClickListener mOkclickListener) {
        this.mOkclickListener = mOkclickListener;
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


    public void showRetryDialog(boolean isGroup) {
        String message="";
        if(isGroup){
           message="删除分类后，该分类下的小分类也会同时被删除。该分类下原有账单仍保持不变。";
        }else {
            message="删除分类后，该分类下原有的账单仍保持不变。";
        }
        customAlertDialog.setMessage(message);
        customAlertDialog.show();
    }
}
