package com.yiqiji.money.modules.book.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

import com.yiqiji.money.R;

/**
 * Created by leichi on 2017/6/29.
 */

public class AdvancedCardView extends CardView {

    float height_width_ratio;

    public AdvancedCardView(Context context) {
        super(context);
    }

    public AdvancedCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray t = getContext().obtainStyledAttributes(attrs,
                R.styleable.AdvancedCardView);
        height_width_ratio = t.getFloat(R.styleable.AdvancedCardView_height_width_ratio, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (height_width_ratio != 0) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) (width * height_width_ratio);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}
