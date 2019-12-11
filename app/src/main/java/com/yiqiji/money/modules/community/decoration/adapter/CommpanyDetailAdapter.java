package com.yiqiji.money.modules.community.decoration.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.community.decoration.model.CommpanyUserBookEntity;
import com.yiqiji.money.modules.community.decoration.viewholder.ItemDecorateCommpanyViewHolder;
import com.yiqiji.money.modules.community.model.BookCellModel;

/**
 * Created by dansakai on 2017/8/3.
 */

public class CommpanyDetailAdapter extends BaseQuickAdapter<BookCellModel,ItemDecorateCommpanyViewHolder> {

    public CommpanyDetailAdapter() {
        super(R.layout.adapter_item_commpany_rack_view);
    }

    @Override
    protected void convert(ItemDecorateCommpanyViewHolder helper, BookCellModel item) {
        helper.bindViewData(item);
    }
}
