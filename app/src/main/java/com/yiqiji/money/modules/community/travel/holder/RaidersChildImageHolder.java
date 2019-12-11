package com.yiqiji.money.modules.community.travel.holder;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.Found.view.TabHSView;
import com.yiqiji.money.modules.community.travel.adapter.RaidersScorllerAdapter;
import com.yiqiji.money.modules.community.travel.model.RaidersMultiItem;
import com.yiqiji.money.modules.community.travel.model.RaiderspPhotosInfo;
import com.yiqiji.money.modules.community.travel.travelinterface.BaseScorllerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${huangweishui} on 2017/8/4.
 * address huang.weishui@71dai.com
 */
public class RaidersChildImageHolder extends BaseViewHolder {
    @BindView(R.id.tabHSView)
    TabHSView tabHSView;
    BaseScorllerAdapter raidersScorllerAdapter;

    public RaidersChildImageHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);

    }

    public void bindData(Context context, RaidersMultiItem item) {
        List<RaiderspPhotosInfo> urls = (List<RaiderspPhotosInfo>) item.getData();
//        if (urls == null || urls.size() == 0) {
//        }
        raidersScorllerAdapter = new RaidersScorllerAdapter(context, urls);
        tabHSView.setAdapter(raidersScorllerAdapter);
    }
}
