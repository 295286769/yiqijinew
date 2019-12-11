package com.yiqiji.money.modules.common.control;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.yiqiji.money.R;

/**
 * Created by dansakai on 2017/3/16.
 */

public class RelativeLayoutWithCorner extends RelativeLayout {
    private GradientDrawable gradientDrawable;

    public RelativeLayoutWithCorner(Context context) {
        super(context);
        init();
    }

    public RelativeLayoutWithCorner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RelativeLayoutWithCorner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        setBackgroundResource(R.drawable.activity_card_bg);
        gradientDrawable = (GradientDrawable) getBackground();
    }

    public void setBackgroundDrawable(int color) {
        gradientDrawable.setColor(color);
    }
}
