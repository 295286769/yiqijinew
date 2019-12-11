package com.yiqiji.money.modules.book.view;

import android.content.Context;
import android.view.View;

import com.yiqiji.money.modules.common.control.CustomAlertDialog;

/**
 * Created by xiaolong on 2017/7/21.
 */

public class WarnAlertDialog {

    CustomAlertDialog customAlertDialog;
    String title = "提示";
    String message = "操作提示";
    String yesText = "确定";
    String cannelText = "取消";
    Context mContext;

    public enum ClickType{
        OK,
        CANCEL

    }
    public WarnAlertDialog(Context context) {
        this.mContext = context;
        initDialog();
    }

    private void initDialog() {
        customAlertDialog = new CustomAlertDialog(mContext);
        customAlertDialog.setTitle(title);
        customAlertDialog.setMessage(message);
        customAlertDialog.setRightButton(yesText, okClickListener);
        customAlertDialog.setLeftButton(cannelText, cancelClickListener);
        customAlertDialog.setCancelable(false);
    }

    View.OnClickListener okClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            customAlertDialog.dismiss();
            if (onButtonOnClickListener != null) {
                onButtonOnClickListener.onClick(ClickType.OK);
            }
        }
    };

    View.OnClickListener cancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            customAlertDialog.dismiss();
            if (onButtonOnClickListener != null) {
                onButtonOnClickListener.onClick(ClickType.CANCEL);
            }
        }
    };

    OnButtonOnClickListener onButtonOnClickListener;
    public interface OnButtonOnClickListener{
        void onClick(ClickType type);
    }


    public void setButtonOnClickListener(OnButtonOnClickListener clickListener){
        onButtonOnClickListener=clickListener;
    }


    public void showWarnAlertDialog(String message) {
        customAlertDialog.setMessage(message);
        customAlertDialog.show();
    }
}
