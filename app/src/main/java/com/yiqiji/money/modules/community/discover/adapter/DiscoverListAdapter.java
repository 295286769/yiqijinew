package com.yiqiji.money.modules.community.discover.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.Found.adapter.TabHScorllerAdapter;
import com.yiqiji.money.modules.Found.entity.TabListModel;
import com.yiqiji.money.modules.Found.view.BaseWebView;
import com.yiqiji.money.modules.Found.view.TabHSView;
import com.yiqiji.money.modules.common.activity.WebActivity;
import com.yiqiji.money.modules.community.discover.model.DiscoverMultipleItem;
import com.yiqiji.money.modules.community.discover.model.DiscoverMultipleItem.ViewType;
import com.yiqiji.money.modules.community.discover.viewholder.ItemBookMultipleImageViewHolder;
import com.yiqiji.money.modules.community.discover.viewholder.ItemBookSingleImageViewHolder;
import com.yiqiji.money.modules.community.discover.viewholder.ItemBookSingleTextViewHolder;
import com.yiqiji.money.modules.community.discover.viewholder.ItemBookTextImageViewHolder;
import com.yiqiji.money.modules.community.discover.viewholder.ItemBookrackViewHolder;
import com.yiqiji.money.modules.community.discover.viewholder.ItemTitleViewHolder;
import com.yiqiji.money.modules.community.travel.travelinterface.BaseOnItemClickListener;
import com.yiqiji.money.modules.community.travel.travelinterface.BaseScorllerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by leichi on 2017/7/31.
 */

public class DiscoverListAdapter extends BaseMultiItemQuickAdapter<DiscoverMultipleItem, BaseViewHolder> {


    public DiscoverListAdapter(Context context) {
        super(null);
        addItemType(ViewType.BOOK_RACK.ordinal(), R.layout.adapter_item_book_rack_view, ItemBookrackViewHolder.class);
        addItemType(ViewType.TITLE.ordinal(), R.layout.adapter_item_title_view, ItemTitleViewHolder.class);
        addItemType(ViewType.BOOK_TEXT_IMAGE.ordinal(), R.layout.adapter_item_book_image_text_view, ItemBookTextImageViewHolder.class);
        addItemType(ViewType.BOOK_SINGLE_IMAGE.ordinal(), R.layout.adapter_item_book_single_image_view, ItemBookSingleImageViewHolder.class);
        addItemType(ViewType.BOOK_SINGLE_TEXT.ordinal(), R.layout.adapter_item_book_single_text_view, ItemBookSingleTextViewHolder.class);
        addItemType(ViewType.BOOK_MULTIPLE_IMAGE.ordinal(), R.layout.adapter_item_multiple_image_book_view, ItemBookMultipleImageViewHolder.class);
        addItemType(ViewType.TAB_LIST.ordinal(), R.layout.adapter_tab_list_view, ItemTabListViewHolder.class);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, DiscoverMultipleItem item) {
        ViewType viewType = item.getItemViewType();
        switch (viewType) {
            case TITLE:
                ((ItemTitleViewHolder) helper).bindViewData(item);
                break;
            case BOOK_SINGLE_IMAGE:
                ((ItemBookSingleImageViewHolder) helper).bindViewData(item);
                break;
            case BOOK_TEXT_IMAGE:
                ((ItemBookTextImageViewHolder) helper).bindViewData(item);
                break;
            case BOOK_RACK:
                ((ItemBookrackViewHolder) helper).bindViewData(item);
                break;
            case BOOK_SINGLE_TEXT:
                ((ItemBookSingleTextViewHolder) helper).bindViewData(item);
                break;
            case TAB_LIST:
                ((ItemTabListViewHolder) helper).bindViewData(item);
                break;
            case BOOK_MULTIPLE_IMAGE:
                ((ItemBookMultipleImageViewHolder) helper).bindViewData(item);
                break;
        }
    }

    public class ItemTabListViewHolder extends BaseViewHolder {

        @BindView(R.id.th_tabHSView)
        TabHSView th_tabHSView;
        BaseScorllerAdapter adapter;
        Context context;

        public ItemTabListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            context = view.getContext();
        }

        public void bindViewData(DiscoverMultipleItem item) {
            adapter = new TabHScorllerAdapter(context, (TabListModel) item.getData());
            th_tabHSView.setAdapter(adapter);
            th_tabHSView.setOnItemClick(new BaseOnItemClickListener<TabListModel.TabModel>() {
                @Override
                public void onItemClick(TabListModel.TabModel tabModel) {
                    if (BaseWebView.overrideUrlLoading(context, tabModel.url)) {
                        return;
                    }
                    Intent in = new Intent(context, WebActivity.class);
                    in.putExtra("url", tabModel.url);
                    in.putExtra("title", tabModel.title);
                    context.startActivity(in);
                }
            });
        }
    }

}


