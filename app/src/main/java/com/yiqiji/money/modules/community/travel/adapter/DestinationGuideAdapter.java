package com.yiqiji.money.modules.community.travel.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.community.travel.holder.DestinationGuideHolder;
import com.yiqiji.money.modules.community.travel.model.HotPlace;

/**
 * Created by ${huangweishui} on 2017/8/3.
 * address huang.weishui@71dai.com
 */
public class DestinationGuideAdapter extends BaseQuickAdapter<HotPlace, DestinationGuideHolder> {

    public DestinationGuideAdapter(Context context) {
        super(R.layout.view_hot_destination_guide_view_layout);
        mContext = context;
    }

    @Override
    protected void convert(DestinationGuideHolder helper, HotPlace item) {
        helper.bindData(mContext, item);
    }
}
