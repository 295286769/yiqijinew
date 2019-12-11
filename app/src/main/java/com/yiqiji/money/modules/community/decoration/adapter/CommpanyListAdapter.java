package com.yiqiji.money.modules.community.decoration.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.community.decoration.model.DecorationcompanyBean;
import com.yiqiji.money.modules.community.decoration.viewholder.ItemDecorationCpViewHolder;

/**
 * Created by dansakai on 2017/8/2.
 */

public class CommpanyListAdapter extends BaseQuickAdapter<DecorationcompanyBean, ItemDecorationCpViewHolder> {

    public CommpanyListAdapter() {
        super(R.layout.adapter_item_decorate_commpany);
    }

    @Override
    protected void convert(ItemDecorationCpViewHolder helper, DecorationcompanyBean item) {
        helper.bindViewData(item);
    }
}
