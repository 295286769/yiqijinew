package com.yiqiji.money.modules.community.travel.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.yiqiji.money.R;

/**
 * Created by ${huangweishui} on 2017/8/2.
 * address huang.weishui@71dai.com
 */
public class HotDestinationsHeadImageView extends ImageView {
    private Context mContext;
    private float with_height_pesent_iamge;

    public HotDestinationsHeadImageView(Context context) {
        this(context, null);
    }

    public HotDestinationsHeadImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HotDestinationsHeadImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initattibute(attrs);
    }

    private void initattibute(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.ImageViewWithHeightStyleable);
        if (typedArray != null) {
            with_height_pesent_iamge = typedArray.getFloat(R.styleable.ImageViewWithHeightStyleable_with_height_pesent_iamge, 1);
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int with = getMeasuredWidth();
        int height = 0;
        if (with_height_pesent_iamge == 1) {
            height = getMeasuredHeight();
        } else {
            height = (int) (with / with_height_pesent_iamge);
        }

        setMeasuredDimension(with, height);


    }
}
