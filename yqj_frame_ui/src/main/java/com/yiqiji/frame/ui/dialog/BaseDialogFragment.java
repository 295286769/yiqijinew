package com.yiqiji.frame.ui.dialog;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


import com.yiqiji.frame.core.system.DensityHelper;
import com.yiqiji.frame.ui.R;

import butterknife.ButterKnife;


/**
 * Created by mark.wu on 16/6/29.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class BaseDialogFragment extends DialogFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Window window = dialog.getWindow();
        setWindowStyle(window);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return onKeyBack();
                }
                return false;
            }
        });
        return dialog;
    }


    protected void setWindowStyle(Window window) {
        WindowManager.LayoutParams windowparams = window.getAttributes();
        windowparams.width = (int)(DensityHelper.getWidthOfTheScreen(getActivity())*0.7);
        windowparams.height =ViewGroup.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setWindowAnimations(R.style.anim_push_bottom);
    }

    public void show(Context context) {
        show(((Activity)context).getFragmentManager(), getClass().getSimpleName());
    }

    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        return super.onCreateAnimator(transit, enter, nextAnim);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public boolean onKeyBack(){
        return false;
    }
}
