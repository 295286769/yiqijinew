package com.yiqiji.money.modules.community.discover.viewholder;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.community.travel.model.DecorateListMultiItem;
import com.yiqiji.money.modules.community.discover.model.DiscoverMultipleItem;
import com.yiqiji.money.modules.community.view.BookrackView;
import com.yiqiji.money.modules.community.model.BookCellModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by leichi on 2017/8/1.
 */
public class ItemBookrackViewHolder extends BaseViewHolder {

    @BindView(R.id.bookrack_view)
    BookrackView bookrackView;

    public ItemBookrackViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void bindViewData(DiscoverMultipleItem item) {
        bookrackView.setViewData((List<BookCellModel>) item.getData());
    }
    public void bindViewData(DecorateListMultiItem item) {
        bookrackView.setViewData((List<BookCellModel>) item.getData());
    }
}
