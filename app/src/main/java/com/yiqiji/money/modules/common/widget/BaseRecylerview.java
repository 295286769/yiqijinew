package com.yiqiji.money.modules.common.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by ${huangweishui} on 2017/6/21.
 * address huang.weishui@71dai.com
 */
public class BaseRecylerview extends RecyclerView {
    public BaseRecylerview(Context context) {
        this(context, null);
    }

    public BaseRecylerview(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseRecylerview(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
//        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
//                MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, heightSpec);

    }
}
