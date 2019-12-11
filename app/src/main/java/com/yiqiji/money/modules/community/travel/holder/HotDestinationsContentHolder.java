package com.yiqiji.money.modules.community.travel.holder;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.community.model.BookCellModel;
import com.yiqiji.money.modules.community.view.MultipleImageItemBookView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${huangweishui} on 2017/8/3.
 * address huang.weishui@71dai.com
 */
public class HotDestinationsContentHolder extends BaseViewHolder {
    @BindView(R.id.multiple_image_book_view)
    MultipleImageItemBookView multiple_image_book_view;

    public HotDestinationsContentHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void bindData(BookCellModel bookCellModel) {
        multiple_image_book_view.setData(bookCellModel);
    }
}
