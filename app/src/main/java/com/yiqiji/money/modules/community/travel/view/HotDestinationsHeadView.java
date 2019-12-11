package com.yiqiji.money.modules.community.travel.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.community.travel.model.HotPlace;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import huangshang.com.yiqiji_imageloadermaniger.ImageLoaderManager;

/**
 * Created by ${huangweishui} on 2017/8/2.
 * address huang.weishui@71dai.com
 */
public class HotDestinationsHeadView extends RelativeLayout {
    @BindView(R.id.hotDestinationsHeadImageView)
    HotDestinationsHeadImageView hotDestinationsHeadImageView;//背景
    @BindView(R.id.tv_city)
    TextView tvCity;//城市
    @BindView(R.id.im_return)
    ImageView im_return;//返回键
    @BindView(R.id.destinationGuideView)
    DestinationGuideView destinationGuideView;//指南
    private Context mContext;

    public HotDestinationsHeadView(Context context) {
        this(context, null);
    }

    public HotDestinationsHeadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HotDestinationsHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
        onPress();
    }


    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_hot_destinations_headview_layout, this, true);
        ButterKnife.bind(this, view);

    }

    private void onPress() {
        im_return.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) mContext).finish();
            }
        });
    }

    public void setHotDestinationsHeadImageView(String image_url) {
        if (hotDestinationsHeadImageView != null) {
            ImageLoaderManager.loadImage(mContext, image_url, hotDestinationsHeadImageView);
        }
    }

    public void setCity(String city) {
        if (tvCity != null) {
            tvCity.setText(city);
        }
    }

    public void setDestinationGuideView(List<HotPlace> hotPlaces, String city) {
        destinationGuideView.setDataInfo(hotPlaces, city);
    }
}
