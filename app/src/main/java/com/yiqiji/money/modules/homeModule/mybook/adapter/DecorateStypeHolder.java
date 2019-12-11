package com.yiqiji.money.modules.homeModule.mybook.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.homeModule.mybook.entity.RenovationStypeInfo;
import com.yiqiji.money.modules.homeModule.mybook.view.RenovationStypeItemView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${huangweishui} on 2017/8/2.
 * address huang.weishui@71dai.com
 */
public class DecorateStypeHolder extends BaseViewHolder {
    @BindView(R.id.renovationStypeItemView)
    RenovationStypeItemView renovationStypeItemView;

    public DecorateStypeHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void bindData(RenovationStypeInfo renovationStypeInfo) {
        renovationStypeItemView.setStypeContent(renovationStypeInfo.getContent());
    }
}
