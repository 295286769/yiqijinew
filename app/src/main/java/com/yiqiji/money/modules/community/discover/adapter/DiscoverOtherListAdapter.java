package com.yiqiji.money.modules.community.discover.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.community.discover.model.DiscoverMultipleItem;
import com.yiqiji.money.modules.community.discover.viewholder.ItemBookMultipleImageViewHolder;
import com.yiqiji.money.modules.community.discover.viewholder.ItemBookSingleImageViewHolder;
import com.yiqiji.money.modules.community.discover.viewholder.ItemBookSingleTextViewHolder;
import com.yiqiji.money.modules.community.discover.viewholder.ItemBookTextImageViewHolder;
import com.yiqiji.money.modules.community.discover.viewholder.ItemTitleViewHolder;

/**
 * Created by leichi on 2017/7/31.
 */

public class DiscoverOtherListAdapter extends BaseMultiItemQuickAdapter<DiscoverMultipleItem, BaseViewHolder> {


    public DiscoverOtherListAdapter(Context context) {
        super(null);
        addItemType(DiscoverMultipleItem.ViewType.TITLE.ordinal(), R.layout.adapter_item_title_view, ItemTitleViewHolder.class);
        addItemType(DiscoverMultipleItem.ViewType.BOOK_MULTIPLE_IMAGE.ordinal(), R.layout.adapter_item_multiple_image_book_view, ItemBookMultipleImageViewHolder.class);
        addItemType(DiscoverMultipleItem.ViewType.BOOK_TEXT_IMAGE.ordinal(), R.layout.adapter_item_book_image_text_view, ItemBookTextImageViewHolder.class);
        addItemType(DiscoverMultipleItem.ViewType.BOOK_SINGLE_IMAGE.ordinal(), R.layout.adapter_item_book_single_image_view, ItemBookSingleImageViewHolder.class);
        addItemType(DiscoverMultipleItem.ViewType.BOOK_SINGLE_TEXT.ordinal(), R.layout.adapter_item_book_single_text_view, ItemBookSingleTextViewHolder.class);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, DiscoverMultipleItem item) {
        DiscoverMultipleItem.ViewType viewType = item.getItemViewType();
        switch (viewType) {
            case TITLE:
                ((ItemTitleViewHolder) helper).bindViewData(item);
                break;
            case BOOK_TEXT_IMAGE:
                ((ItemBookTextImageViewHolder) helper).bindViewData(item);
                break;
            case BOOK_SINGLE_IMAGE:
                ((ItemBookSingleImageViewHolder) helper).bindViewData(item);
                break;
            case BOOK_SINGLE_TEXT:
                ((ItemBookSingleTextViewHolder) helper).bindViewData(item);
                break;
            case BOOK_MULTIPLE_IMAGE:
                ((ItemBookMultipleImageViewHolder) helper).bindViewData(item);
                break;
        }
    }
}
