package com.yiqiji.money.modules.community.decoration.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.community.decoration.ActivityUtil;
import com.yiqiji.money.modules.community.decoration.activity.DecorateNearBookActivity;
import com.yiqiji.money.modules.community.decoration.model.NearBookMultiItem;
import com.yiqiji.money.modules.community.decoration.viewholder.MultiImageItemViewHolder;
import com.yiqiji.money.modules.community.discover.model.TitleModel;
import com.yiqiji.money.modules.community.discover.viewholder.ItemBookSingleTextViewHolder;
import com.yiqiji.money.modules.community.discover.viewholder.ItemBookTextImageViewHolder;
import com.yiqiji.money.modules.community.model.BookCellModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dansakai on 2017/8/4.
 */

public class DecorateNearAdapter extends BaseMultiItemQuickAdapter<NearBookMultiItem, BaseViewHolder> {

    private Context mContext;

    public DecorateNearAdapter(Context context) {
        super(null);
        this.mContext = context;
        addItemType(NearBookMultiItem.TITLE, R.layout.adapter_item_near_title_view, ItemTitleViewHolder.class);
        addItemType(NearBookMultiItem.THREE_IMG_HASTITLE, R.layout.adapter_item_multiple_image_book_view, MultiImageItemViewHolder.class);
        addItemType(NearBookMultiItem.SING_SMALL_IMG, R.layout.adapter_item_book_image_text_view, ItemBookTextImageViewHolder.class);
        addItemType(NearBookMultiItem.TEXT_NO_IMG, R.layout.adapter_item_book_single_text_view, ItemBookSingleTextViewHolder.class);
        addItemType(NearBookMultiItem.DEFAUT_BOOKS, R.layout.adapter_item_defaut_book, ItemDefautBookViewHolder.class);
    }

    @Override
    protected void convert(BaseViewHolder helper, NearBookMultiItem item) {
        switch (item.getItemType()) {
            case NearBookMultiItem.TITLE:
                ((ItemTitleViewHolder) helper).bindViewData(item);
                break;
            case NearBookMultiItem.DEFAUT_BOOKS:
                ((ItemDefautBookViewHolder) helper).bindViewData(item);
                break;
            case NearBookMultiItem.THREE_IMG_HASTITLE:
                ((MultiImageItemViewHolder) helper).bindViewData(item);
                break;
            case NearBookMultiItem.SING_SMALL_IMG:
                ((ItemBookTextImageViewHolder) helper).bindViewData(item);
                break;
            case NearBookMultiItem.TEXT_NO_IMG:
                ((ItemBookSingleTextViewHolder) helper).bindViewData(item);
                break;

        }
    }

    class ItemTitleViewHolder extends BaseViewHolder {

        @BindView(R.id.deafault_house)
        TextView deafaultHouse;
        @BindView(R.id.tv_change)
        TextView tvChange;

        View contentView;

        public ItemTitleViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            contentView = view;
        }

        public void bindViewData(NearBookMultiItem item) {
            final TitleModel titleModel = (TitleModel) item.getData();
            deafaultHouse.setText("默认小区： " + titleModel.titleName);
            tvChange.setVisibility(titleModel.type == 0 ? View.VISIBLE : View.GONE);
            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (titleModel.type) {
                        case 0://修改默认小区
                            ActivityUtil.startChangeHouseInfoActivity(mContext, DecorateNearBookActivity.REQUEST_CODE);
                            break;
                    }
                }
            });

        }
    }

    class ItemDefautBookViewHolder extends BaseViewHolder {
        @BindView(R.id.rc_grid)
        RecyclerView recyclerView;

        CommpanyDetailAdapter adapter;

        public ItemDefautBookViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            adapter = new CommpanyDetailAdapter();
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
            recyclerView.setAdapter(adapter);
        }

        public void bindViewData(NearBookMultiItem item) {
            List<BookCellModel> cellModels = (List<BookCellModel>) item.getData();
            if (!StringUtils.isEmptyList(cellModels)) {
                adapter.setDataList(cellModels);
                adapter.notifyDataSetChanged();
            } else {
                adapter.setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.empty_view, null));
            }
        }
    }

}
