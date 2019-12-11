package com.yiqiji.money.modules.book.bookcategory.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by leichi on 2017/6/2.
 */

public class PopWindowContentView extends LinearLayout{

    public PopWindowContentView(Context context) {
        super(context);
    }

    public PopWindowContentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PopWindowContentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PopWindowContentView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(0, heightMeasureSpec);
    }
}
