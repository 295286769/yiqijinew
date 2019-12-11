package com.yiqiji.money.modules.community.travel.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.community.travel.holder.RaidersChildHolder;
import com.yiqiji.money.modules.community.travel.holder.RaidersChildImageHolder;
import com.yiqiji.money.modules.community.travel.holder.RaidersParentHolder;
import com.yiqiji.money.modules.community.travel.model.RaidersMultiItem;
import com.yiqiji.money.modules.community.travel.uitl.ConstantTravel;

/**
 * Created by ${huangweishui} on 2017/8/4.
 * address huang.weishui@71dai.com
 */
public class RaidersAdapter extends BaseMultiItemQuickAdapter<RaidersMultiItem, BaseViewHolder> {
    public RaidersAdapter(Context context) {
        super(null);
        mContext = context;
        addItemType(ConstantTravel.HOTDESTINATIONS_TITLT, R.layout.activity_activity_raiders_item_parent_layout, RaidersParentHolder.class);
        addItemType(ConstantTravel.HOTDESTINATIONS_CONTENT, R.layout.activity_activity_raiders_item_child_layout, RaidersChildHolder.class);
        addItemType(ConstantTravel.HOTDESTINATIONS_CONTENT_IMAGE, R.layout.activity_activity_raiders_item_child_image_layout, RaidersChildImageHolder.class);
    }

    @Override
    protected void convert(BaseViewHolder helper, RaidersMultiItem item) {
        int viewtype = item.getItemType();
        switch (viewtype) {
            case ConstantTravel.HOTDESTINATIONS_TITLT:
                ((RaidersParentHolder) helper).bindData(this,item);
                break;
            case ConstantTravel.HOTDESTINATIONS_CONTENT:
                ((RaidersChildHolder) helper).bindData(item);
                break;
            case ConstantTravel.HOTDESTINATIONS_CONTENT_IMAGE:
                ((RaidersChildImageHolder) helper).bindData(mContext, item);
                break;
        }
    }
}
