package com.yiqiji.money.modules.community.travel.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.yiqiji.money.modules.community.travel.model.RaiderspPhotosInfo;
import com.yiqiji.money.modules.community.travel.travelinterface.BaseOnItemClickListener;
import com.yiqiji.money.modules.community.travel.travelinterface.BaseScorllerAdapter;
import com.yiqiji.money.modules.community.view.DiscoverHSItemView;

import java.util.List;

/**
 * Created by ${huangweishui} on 2017/8/4.
 * address huang.weishui@71dai.com
 */
public class RaidersScorllerAdapter implements BaseScorllerAdapter {
    Context mContext;
    List<RaiderspPhotosInfo> urls;
    ViewGroup contentView;

    public RaidersScorllerAdapter(Context context, List<RaiderspPhotosInfo> urls) {
        mContext = context;
        this.urls = urls;
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        contentView = viewGroup;
        contentView.removeAllViews();
        for (int i = 0; i < urls.size(); i++) {
            String url = urls.get(i).getImage_url();
            DiscoverHSItemView discoverHSItemView = new DiscoverHSItemView(mContext);
            discoverHSItemView.setImageWithHeight(2.4f, 1.42f);
            discoverHSItemView.diplayImage(url);
            viewGroup.addView(discoverHSItemView);
        }
        contentView.requestLayout();
    }

    @Override
    public void OnItemClickListener(BaseOnItemClickListener onItemClickListener) {

    }

    @Override
    public void OnEnvent(View view) {

    }

}
