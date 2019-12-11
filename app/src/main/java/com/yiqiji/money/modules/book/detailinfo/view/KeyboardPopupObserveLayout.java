package com.yiqiji.money.modules.book.detailinfo.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by leichi on 2017/6/15.
 */

public class KeyboardPopupObserveLayout extends RelativeLayout implements View.OnLayoutChangeListener {

    int keyHeight=0;

    public KeyboardPopupObserveLayout(Context context) {
        super(context);
        init();
    }

    public KeyboardPopupObserveLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KeyboardPopupObserveLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
          //addOnLayoutChangeListener(this);
    }



    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > keyHeight)){
            Toast.makeText(getContext(), "监听到软键盘弹起...", Toast.LENGTH_SHORT).show();
        }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > keyHeight)){
            Toast.makeText(getContext(), "监听到软件盘关闭...", Toast.LENGTH_SHORT).show();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isShow=((Activity)getContext()).getWindow().getAttributes().softInputMode== WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED;
                Log.v("Keyboard","onLayoutChange()"+isShow);
            }
        },500);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.v("Keyboard","onLayout()"+l+"   "+t+"   "+r+"    "+b);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.v("Keyboard","onSizeChanged()"+w+"   "+h+"   "+oldw+"    "+oldh);
    }
}
