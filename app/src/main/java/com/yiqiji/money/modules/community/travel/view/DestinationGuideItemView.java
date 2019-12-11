package com.yiqiji.money.modules.community.travel.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiqiji.money.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import huangshang.com.yiqiji_imageloadermaniger.ImageLoaderManager;

/**
 * Created by ${huangweishui} on 2017/8/2.
 * address huang.weishui@71dai.com
 */
public class DestinationGuideItemView extends LinearLayout {
    @BindView(R.id.tv_guide_item_content)
    TextView tvGuideItemContent;
    @BindView(R.id.im_guide_item_image)
    ImageView imGuideItemImage;
    private Context mContext;

    public DestinationGuideItemView(Context context) {
        this(context, null);
    }

    public DestinationGuideItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DestinationGuideItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_hot_destination_guide_item_layout, this, true);
        ButterKnife.bind(this, view);
    }

    public void setDataInfo(String content, String url) {
        if (tvGuideItemContent != null) {
            tvGuideItemContent.setText(content);
        }
        if (imGuideItemImage != null) {
            ImageLoaderManager.loadImage(mContext, url, imGuideItemImage);
        }

    }
}
