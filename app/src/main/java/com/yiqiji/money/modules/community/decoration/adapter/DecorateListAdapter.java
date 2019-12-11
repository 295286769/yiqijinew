package com.yiqiji.money.modules.community.decoration.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.community.decoration.ActivityUtil;
import com.yiqiji.money.modules.community.travel.model.DecorateListMultiItem;
import com.yiqiji.money.modules.community.decoration.viewholder.ItemDecorationCpViewHolder;
import com.yiqiji.money.modules.community.decoration.viewholder.MultiImageItemViewHolder;
import com.yiqiji.money.modules.community.discover.model.TitleModel;
import com.yiqiji.money.modules.community.discover.viewholder.ItemBookSingleImageViewHolder;
import com.yiqiji.money.modules.community.discover.viewholder.ItemBookSingleTextViewHolder;
import com.yiqiji.money.modules.community.discover.viewholder.ItemBookTextImageViewHolder;
import com.yiqiji.money.modules.community.discover.viewholder.ItemBookrackViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dansakai on 2017/8/1.
 */

public class DecorateListAdapter extends BaseMultiItemQuickAdapter<DecorateListMultiItem, BaseViewHolder> {

    private Context mContext;

    public DecorateListAdapter(Context context) {
        super(null);
        this.mContext = context;
        addItemType(DecorateListMultiItem.TITLE, R.layout.adapter_item_title_view, ItemTitleViewHolder.class);
        addItemType(DecorateListMultiItem.SING_BIG_IMG, R.layout.adapter_item_book_single_image_view, ItemBookSingleImageViewHolder.class);
        addItemType(DecorateListMultiItem.SING_SMALL_IMG, R.layout.adapter_item_book_image_text_view, ItemBookTextImageViewHolder.class);
        addItemType(DecorateListMultiItem.THREE_IMG, R.layout.adapter_item_book_rack_view, ItemBookrackViewHolder.class);
        addItemType(DecorateListMultiItem.TEXT_NO_IMG, R.layout.adapter_item_book_single_text_view, ItemBookSingleTextViewHolder.class);
        addItemType(DecorateListMultiItem.DECORAT_SINGLE_IMG, R.layout.adapter_item_decorate_commpany, ItemDecorationCpViewHolder.class);
        addItemType(DecorateListMultiItem.THREE_IMG_HASTITLE, R.layout.adapter_item_multiple_image_book_view, MultiImageItemViewHolder.class);
    }

    @Override
    protected void convert(BaseViewHolder helper, DecorateListMultiItem item) {
        switch (item.getItemType()) {
            case DecorateListMultiItem.TITLE:
                ((ItemTitleViewHolder)helper).bindViewData(item);
                break;
            case DecorateListMultiItem.SING_BIG_IMG:
                ((ItemBookSingleImageViewHolder)helper).bindViewData(item);
                break;
            case DecorateListMultiItem.SING_SMALL_IMG:
                ((ItemBookTextImageViewHolder)helper).bindViewData(item);
                break;
            case DecorateListMultiItem.THREE_IMG:
                ((ItemBookrackViewHolder)helper).bindViewData(item);
                break;
            case DecorateListMultiItem.TEXT_NO_IMG:
                ((ItemBookSingleTextViewHolder)helper).bindViewData(item);
                break;
            case DecorateListMultiItem.DECORAT_SINGLE_IMG:
                ((ItemDecorationCpViewHolder)helper).bindViewData(item);
                break;
            case DecorateListMultiItem.THREE_IMG_HASTITLE:
                ((MultiImageItemViewHolder)helper).bindViewData(item);
                break;
        }
    }

    protected class ItemTitleViewHolder extends BaseViewHolder {

        @BindView(R.id.titleName)
        TextView titleName;
        @BindView(R.id.ll_more)
        LinearLayout llMore;

        View contentView;

        public ItemTitleViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            contentView = view;
        }

        public void bindViewData(DecorateListMultiItem item) {
            final TitleModel titleModel = (TitleModel) item.getData();
            titleName.setText(titleModel.titleName);
            llMore.setVisibility(titleModel.type == 1 ? View.VISIBLE : View.GONE);
            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (titleModel.type) {
                        case 1://装修公司
                            ActivityUtil.startCommpanyList(mContext);
                            break;
                    }
                }
            });

        }
    }
}
