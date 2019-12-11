package com.yiqiji.money.modules.community.travel.holder;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.view.LeftTextRightImageView;
import com.yiqiji.money.modules.community.travel.adapter.RaidersAdapter;
import com.yiqiji.money.modules.community.travel.model.RaidersDataItemBean;
import com.yiqiji.money.modules.community.travel.model.RaidersMultiItem;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${huangweishui} on 2017/8/4.
 * address huang.weishui@71dai.com
 */
public class RaidersParentHolder extends BaseViewHolder {
    @BindView(R.id.raiders_item)
    LeftTextRightImageView raiders_item;

    public RaidersParentHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void bindData(final RaidersAdapter raidersAdapter, final RaidersMultiItem item) {
        RaidersDataItemBean raidersDataItemBean = (RaidersDataItemBean) item.getData();
        raiders_item.setLeftText(raidersDataItemBean.getTitle());

        if (item.isExpanded()) {
            raiders_item.setArrowImage(R.drawable.raiders_up);
        } else {
            raiders_item.setArrowImage(R.drawable.raiders_down);
        }
        raiders_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (item.isExpanded()) {
                    raidersAdapter.collapse(position);
                } else {
                    raidersAdapter.expand(position);
                }
            }
        });
    }
}
