package com.yiqiji.money.modules.homeModule.mybook.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by ${huangweishui} on 2017/8/1.
 * address huang.weishui@71dai.com
 */
public class HouseStypeMulty implements MultiItemEntity {
    private Object object;
    private int type;

    @Override
    public int getItemType() {
        return type;
    }

    public void setItemType(int type) {
        this.type = type;
    }

    @Override
    public Object getData() {
        return object;
    }

    @Override
    public void setData(Object o) {
        object = o;
    }
}
