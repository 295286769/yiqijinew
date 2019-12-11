package com.yiqiji.money.modules.community.decoration.viewholder;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.community.decoration.view.CommpanyrackView;
import com.yiqiji.money.modules.community.decoration.view.UserBookCardView;
import com.yiqiji.money.modules.community.model.BookCellModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dansakai on 2017/8/3.
 */

public class ItemDecorateCommpanyViewHolder extends BaseViewHolder {
    @BindView(R.id.user_card)
    UserBookCardView userBookCardView;

    public ItemDecorateCommpanyViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void bindViewData(BookCellModel cellModel) {
        userBookCardView.setData(cellModel);
    }
}
