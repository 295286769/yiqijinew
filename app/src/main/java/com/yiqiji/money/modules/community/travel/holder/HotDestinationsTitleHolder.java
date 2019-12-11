package com.yiqiji.money.modules.community.travel.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.community.model.BookCellModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${huangweishui} on 2017/8/3.
 * address huang.weishui@71dai.com
 */
public class HotDestinationsTitleHolder extends BaseViewHolder {
    @BindView(R.id.titleName)
    TextView titleName;
    @BindView(R.id.ll_more)
    LinearLayout ll_more;

    public HotDestinationsTitleHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void bindData(BookCellModel bookCellModel, int size) {
        ll_more.setVisibility(View.GONE);
        titleName.setText(bookCellModel.getTitle() + "(" + size + ")");
    }
}
