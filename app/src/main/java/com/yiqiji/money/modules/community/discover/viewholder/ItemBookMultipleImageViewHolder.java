package com.yiqiji.money.modules.community.discover.viewholder;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.community.discover.model.DiscoverMultipleItem;
import com.yiqiji.money.modules.community.view.MultipleImageItemBookView;
import com.yiqiji.money.modules.community.model.BookCellModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by leichi on 2017/7/31.
 */

public class ItemBookMultipleImageViewHolder extends BaseViewHolder{

    @BindView(R.id.multiple_image_book_view)
    MultipleImageItemBookView multipleImageBookView;

    public ItemBookMultipleImageViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void bindViewData(DiscoverMultipleItem item){
        multipleImageBookView.setData((BookCellModel) item.getData());
    }

}
