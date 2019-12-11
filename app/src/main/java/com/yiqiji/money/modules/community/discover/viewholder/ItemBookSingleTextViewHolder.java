package com.yiqiji.money.modules.community.discover.viewholder;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.community.decoration.model.NearBookMultiItem;
import com.yiqiji.money.modules.community.travel.model.DecorateListMultiItem;
import com.yiqiji.money.modules.community.discover.model.DiscoverMultipleItem;
import com.yiqiji.money.modules.community.view.DiscoverItemBookNoImageView;
import com.yiqiji.money.modules.community.model.BookCellModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by leichi on 2017/7/31.
 */

public class ItemBookSingleTextViewHolder extends BaseViewHolder{
    @BindView(R.id.book_singe_text)
    DiscoverItemBookNoImageView bookSingeText;

    public ItemBookSingleTextViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void bindViewData(DiscoverMultipleItem item){
        bookSingeText.setDiscoverItemBookNoImage((BookCellModel) item.getData());
    }

    public void bindViewData(DecorateListMultiItem item){
        bookSingeText.setDiscoverItemBookNoImage((BookCellModel) item.getData());
    }

    public void bindViewData(NearBookMultiItem item){
        bookSingeText.setDiscoverItemBookNoImage((BookCellModel) item.getData());
    }
}
