package com.yiqiji.money.modules.community.travel.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.community.model.BookCellModel;
import com.yiqiji.money.modules.community.travel.holder.HotDestinationsContentHolder;
import com.yiqiji.money.modules.community.travel.holder.HotDestinationsTitleHolder;
import com.yiqiji.money.modules.community.travel.model.HotDestinationsMultiitem;
import com.yiqiji.money.modules.community.travel.uitl.ConstantTravel;

/**
 * Created by ${huangweishui} on 2017/8/3.
 * address huang.weishui@71dai.com
 */
public class HotDestinationsAdapter extends BaseMultiItemQuickAdapter<HotDestinationsMultiitem, BaseViewHolder> {
    public HotDestinationsAdapter(Context context) {
        super(null);
        mContext = context;
        addItemType(ConstantTravel.HOTDESTINATIONS_TITLT, R.layout.adapter_item_title_view, HotDestinationsTitleHolder.class);
        addItemType(ConstantTravel.HOTDESTINATIONS_CONTENT, R.layout.adapter_item_multiple_image_book_view, HotDestinationsContentHolder.class);
    }

    @Override
    protected void convert(BaseViewHolder helper, HotDestinationsMultiitem item) {
        int type = item.getItemType();
        BookCellModel bookCellModel = (BookCellModel) item.getData();
        switch (type) {
            case ConstantTravel.HOTDESTINATIONS_TITLT://标题

                ((HotDestinationsTitleHolder) helper).bindData(bookCellModel, getItemCount() - 2);
                break;
            case ConstantTravel.HOTDESTINATIONS_CONTENT://内容
                ((HotDestinationsContentHolder) helper).bindData(bookCellModel);
                break;
        }
    }
}
