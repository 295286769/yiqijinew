package com.yiqiji.money.modules.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ToggleButton;

import com.yiqiji.money.R;

/**
 * Created by ${huangweishui} on 2017/7/31.
 * address huang.weishui@71dai.com
 */
public class CommonToggleButton extends ToggleButton {
    public CommonToggleButton(Context context) {
        this(context, null);
    }

    public CommonToggleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundResource(R.drawable.selector_toggler);
        setTextOff("");
        setTextOn("");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
