package com.yiqiji.money.modules.community.discover.viewholder;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.community.decoration.model.NearBookMultiItem;
import com.yiqiji.money.modules.community.travel.model.DecorateListMultiItem;
import com.yiqiji.money.modules.community.view.TextAndImageItemView;
import com.yiqiji.money.modules.community.discover.model.DiscoverMultipleItem;
import com.yiqiji.money.modules.community.model.BookCellModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by leichi on 2017/7/31.
 */

public class ItemBookTextImageViewHolder extends BaseViewHolder{

    @BindView(R.id.book_text_image)
    TextAndImageItemView bookTextImage;

    public ItemBookTextImageViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void bindViewData(DiscoverMultipleItem item){
        bookTextImage.setData((BookCellModel) item.getData());
    }
    public void bindViewData(DecorateListMultiItem item){
        bookTextImage.setData((BookCellModel) item.getData());
    }

    public void bindViewData(NearBookMultiItem item) {
        bookTextImage.setData((BookCellModel) item.getData());
    }
}
