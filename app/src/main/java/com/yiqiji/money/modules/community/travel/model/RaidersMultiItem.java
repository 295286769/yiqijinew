package com.yiqiji.money.modules.community.travel.model;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yiqiji.money.modules.community.travel.uitl.ConstantTravel;

/**
 * Created by ${huangweishui} on 2017/8/4.
 * address huang.weishui@71dai.com
 */
public class RaidersMultiItem extends AbstractExpandableItem<RaidersMultiItem> implements MultiItemEntity {
    private int viewtype;
    private Object data;

    @Override
    public int getLevel() {
        if (viewtype == ConstantTravel.HOTDESTINATIONS_TITLT) {
            return 0;
        } else {
            return 1;
        }
    }

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
