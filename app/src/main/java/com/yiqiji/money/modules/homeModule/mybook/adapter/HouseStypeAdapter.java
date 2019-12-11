package com.yiqiji.money.modules.homeModule.mybook.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.homeModule.mybook.entity.HouseStypeInfo;
import com.yiqiji.money.modules.homeModule.mybook.entity.HouseStypeMulty;

/**
 * Created by ${huangweishui} on 2017/8/1.
 * address huang.weishui@71dai.com
 */
public class HouseStypeAdapter extends BaseMultiItemQuickAdapter<HouseStypeMulty, BaseViewHolder> {

    public HouseStypeAdapter(Context context) {
        super(null);
        mContext = context;
        addItemType(100, R.layout.activity_housestype_item_layout, HouseStypeHolder.class);//户型
        addItemType(101, R.layout.activity_renovationstype_item_layout, RenovationStypeHolder.class);//装修方式
    }

    @Override
    protected void convert(BaseViewHolder helper, HouseStypeMulty item) {
        int type = item.getItemType();
        HouseStypeInfo houseStypeInfo = (HouseStypeInfo) item.getData();
        switch (type) {//100户型101装修方式
            case 100:
                ((HouseStypeHolder) helper).bindData(mContext, houseStypeInfo);
                break;
            case 101:
                ((RenovationStypeHolder) helper).bindData(mContext, houseStypeInfo);
                break;
        }
    }
}
