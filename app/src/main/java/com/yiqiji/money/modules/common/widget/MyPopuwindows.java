package com.yiqiji.money.modules.common.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.yiqiji.money.R;

import java.lang.reflect.Method;

public class MyPopuwindows extends PopupWindow {
    private Context context;

    public MyPopuwindows(Context context, final View view) {
        this.context = context;

        setContentView(view);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        // Drawable drawable =
        // context.getResources().getDrawable(R.drawable.popuwindows_color);
        // setBackgroundDrawable(drawable);
        ColorDrawable cd = new ColorDrawable(0x000000);
        setBackgroundDrawable(cd);
        backgroundAlpha(0.8f);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setAnimationStyle(R.style.enter_exit);
        setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onDismissListener!=null){
                    onDismissListener.onDismiss();
                }
                dismiss();
            }
        });

        setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });


        setTouchInterceptor(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.v(MyPopuwindows.class.getName(),"eventAction="+event.getAction());
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    MyPopuwindows.this.dismiss();
                    return true;
                }
                return false;
            }
        });
    }

    OnDismissListener onDismissListener;
    public void setInnerOnDismissListener(OnDismissListener onDismissListener){
        this.onDismissListener=onDismissListener;
    }

    public void setWithAndHeight() {
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.MATCH_PARENT);
    }

    public void setWithAndHeightList() {
        setWidth(LayoutParams.WRAP_CONTENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        setAnimationStyle(R.style.mumber_bnote_list);
    }

    public void setOutSize() {
        setFocusable(false);
        setOutsideTouchable(false);
        ColorDrawable cd = new ColorDrawable(99000000);
        setBackgroundDrawable(cd);

    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        ((Activity) context).getWindow().setAttributes(lp);
    }

    public void setStype() {
        setAnimationStyle(R.style.hide_display_stype);
    }

    /**
     * popuwindows点击外部区域事件向下传递
     *
     * @param popupWindow
     * @param touchModal
     */
    public void setPopupWindowTouchModal(PopupWindow popupWindow, boolean touchModal) {
        if (null == popupWindow) {
            return;
        }
        Method method;
        try {

            method = PopupWindow.class.getDeclaredMethod("setTouchModal", boolean.class);
            method.setAccessible(true);
            method.invoke(popupWindow, touchModal);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
