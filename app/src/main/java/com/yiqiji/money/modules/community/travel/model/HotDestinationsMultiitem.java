package com.yiqiji.money.modules.community.travel.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by ${huangweishui} on 2017/8/3.
 * address huang.weishui@71dai.com
 */
public class HotDestinationsMultiitem implements MultiItemEntity {
    private int viewtype;
    private Object data;

    @Override
    public int getItemType() {
        return viewtype;
    }

    public void setItemType(int viewtype) {
        this.viewtype = viewtype;
    }


    @Override
    public Object getData() {
        return data;
    }

    @Override
    public void setData(Object o) {
        this.data = o;
    }
}
