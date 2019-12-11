package com.yiqiji.money.modules.homeModule.mybook.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.homeModule.mybook.entity.RenovationStypeInfo;

/**
 * Created by ${huangweishui} on 2017/8/2.
 * address huang.weishui@71dai.com
 */
public class RenovationStypeAdapter extends BaseQuickAdapter<RenovationStypeInfo, DecorateStypeHolder> {
    private Context mContext;

    public RenovationStypeAdapter(Context context) {
        super(R.layout.activity_renovation_stype_item_layout);
        mContext = context;
    }

    @Override
    protected void convert(DecorateStypeHolder helper, RenovationStypeInfo item) {
        helper.bindData(item);
    }
}
