package com.yiqiji.money.modules.book.view;

import android.app.Dialog;
import android.support.v4.app.Fragment;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.utils.LoadingDialog;

/**
 * Created by leichi on 2017/5/24.
 */

public class BaseFragment extends Fragment{

    protected Dialog managedDialog;//通用的dialog

    /***
     * 加载数据dialog
     */
    public void showLoadingDialog() {
            this.dismissDialog();
            LoadingDialog loadingDialog = new LoadingDialog(getActivity(), R.layout.dialog_layout, R.style.DialogTheme);
            this.managedDialog = loadingDialog;
            managedDialog.setCancelable(false);
            loadingDialog.show();
    }


    /***
     * dismiss所有正在显示的对话框
     */
    public void dismissDialog() {
            if (this.managedDialog != null && this.managedDialog.isShowing()) {
                this.managedDialog.dismiss();
            }
            this.managedDialog = null;
    }


}
