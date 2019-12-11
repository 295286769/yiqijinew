package com.yiqiji.money.modules.homeModule.home.wegit;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * Created by ${huangweishui} on 2017/6/27.
 * address huang.weishui@71dai.com
 */
public class NestedExpandaleListView extends ExpandableListView {
    public NestedExpandaleListView(Context context) {
        this(context, null);
    }

    public NestedExpandaleListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedExpandaleListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

                MeasureSpec.AT_MOST);

        //将重新计算的高度传递回去
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
