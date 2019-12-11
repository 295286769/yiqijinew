package com.yiqiji.money.modules.homeModule.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by ${huangweishui} on 2017/7/20.
 * address huang.weishui@71dai.com
 */
public class JournalImageView extends ImageView {
    public JournalImageView(Context context) {
        this(context, null);
    }

    public JournalImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JournalImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int with = getMeasuredWidth();
        int height = (int) (with * (330.0f / 606.0f));
        setMeasuredDimension(with, height);
    }
}
