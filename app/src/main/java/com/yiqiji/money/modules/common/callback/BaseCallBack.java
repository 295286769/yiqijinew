package com.yiqiji.money.modules.common.callback;

import android.content.Context;
import android.os.Handler;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.utils.LoadingDialog;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public abstract class BaseCallBack<T> implements Callback<T> {
    private Context context;
    // private SweetAlertDialog sad;
    private LoadingDialog progressDlg;

    private static Handler mHandler = null;

    public LoadingDialog getProgressDlg() {

        return progressDlg;
    }

    public BaseCallBack(final Context context, Boolean boolean1) {
        this.context = context;
        if (boolean1) {
            if (progressDlg != null && progressDlg.isShowing()) {
                progressDlg.dismiss();
                progressDlg = null;
            }

            progressDlg = new LoadingDialog(context, R.layout.dialog_layout, R.style.DialogTheme);
            progressDlg.setCancelable(true);
            progressDlg.show();
        }
    }

    @Override
    public void onResponse(Response<T> arg0, Retrofit arg1) {
        if (progressDlg != null && progressDlg.isShowing()) {
            progressDlg.dismiss();

        }

    }

    @Override
    public void onFailure(Throwable arg0) {
        if (progressDlg != null && progressDlg.isShowing()) {
            progressDlg.dismiss();
        }
        if (arg0 != null && arg0.getMessage() != null) {
//            ToastUtils.DiyToast(context, arg0.getMessage());
        }
    }

}
