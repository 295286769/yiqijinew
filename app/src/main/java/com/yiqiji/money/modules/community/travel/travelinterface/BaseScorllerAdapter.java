package com.yiqiji.money.modules.community.travel.travelinterface;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ${huangweishui} on 2017/8/4.
 * address huang.weishui@71dai.com
 */
public interface BaseScorllerAdapter {
    void initView(ViewGroup viewGroup);

    void OnItemClickListener(BaseOnItemClickListener onItemClickListener);

    void OnEnvent(View view);
}
