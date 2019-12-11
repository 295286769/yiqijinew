package com.yiqiji.money.modules.community.decoration.viewholder;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.community.decoration.model.NearBookMultiItem;
import com.yiqiji.money.modules.community.travel.model.DecorateListMultiItem;
import com.yiqiji.money.modules.community.view.MultipleImageItemBookView;
import com.yiqiji.money.modules.community.model.BookCellModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dansakai on 2017/8/1.
 */

public class MultiImageItemViewHolder extends BaseViewHolder {
    @BindView(R.id.multiple_image_book_view)
    MultipleImageItemBookView multipleImageItemBookView;

    public MultiImageItemViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void bindViewData(DecorateListMultiItem item) {
        multipleImageItemBookView.setData((BookCellModel) item.getData());
    }

    public void bindViewData(NearBookMultiItem item) {
        multipleImageItemBookView.setData((BookCellModel) item.getData());
    }
}
