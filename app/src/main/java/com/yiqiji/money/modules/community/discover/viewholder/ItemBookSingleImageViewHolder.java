package com.yiqiji.money.modules.community.discover.viewholder;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.community.travel.model.DecorateListMultiItem;
import com.yiqiji.money.modules.community.view.BigImageItemView;
import com.yiqiji.money.modules.community.discover.model.DiscoverMultipleItem;
import com.yiqiji.money.modules.community.model.BookCellModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by leichi on 2017/7/31.
 */

public class ItemBookSingleImageViewHolder extends BaseViewHolder{

    @BindView(R.id.book_singe_image)
    BigImageItemView bookSingeImage;

    public ItemBookSingleImageViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void bindViewData(DiscoverMultipleItem item){
        bookSingeImage.setData((BookCellModel) item.getData());
    }

    public void bindViewData(DecorateListMultiItem item){
        bookSingeImage.setData((BookCellModel) item.getData());
    }

}
