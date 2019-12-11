package com.yiqiji.money.modules.community.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.yiqiji.money.modules.common.utils.UIHelper;

/**
 * Created by ${huangweishui} on 2017/7/28.
 * address huang.weishui@71dai.com
 */
public class DiscoverHSItemImageView extends ImageView {
    private Context mContext;
    private int sreenWidth;
    private float withScreenPesent;//宽度占屏幕的%
    private float withHeightPesent;//高度占宽度的%

    public DiscoverHSItemImageView(Context context) {
        this(context, null);
    }

    public DiscoverHSItemImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DiscoverHSItemImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        sreenWidth = UIHelper.getDisplayWidth((Activity) mContext);
        initWithAndHeighPesent();
    }

    private void initWithAndHeighPesent() {
        withScreenPesent = 2.5f;
        withHeightPesent = 2;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int imageMargin = sreenWidth / 151 * 3;
        int imageWidth = (int) (sreenWidth / withScreenPesent);
        int imageHeight = (int) (imageWidth / withHeightPesent);
        setMeasuredDimension(imageWidth, imageHeight);
    }

    /**
     * @param withScreenPesent//宽度和屏幕的比例
     * @param withHeightPesent//宽度和高度的比例
     */
    public void setWithAndHeigtPesent(float withScreenPesent, float withHeightPesent) {
        this.withHeightPesent = withHeightPesent;
        this.withScreenPesent = withScreenPesent;
        postInvalidate();

    }
}
