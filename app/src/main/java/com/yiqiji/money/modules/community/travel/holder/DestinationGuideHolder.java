package com.yiqiji.money.modules.community.travel.holder;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.community.travel.model.HotPlace;
import com.yiqiji.money.modules.community.travel.view.DestinationGuideItemView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${huangweishui} on 2017/8/3.
 * address huang.weishui@71dai.com
 */
public class DestinationGuideHolder extends BaseViewHolder {
    @BindView(R.id.destinationGuideItemView)
    DestinationGuideItemView destinationGuideItemView;

    public DestinationGuideHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void bindData(Context context, HotPlace hotPlace) {
        destinationGuideItemView.setDataInfo(hotPlace.getTitle(), hotPlace.getImg());
    }
}
