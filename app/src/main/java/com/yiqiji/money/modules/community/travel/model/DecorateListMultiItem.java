package com.yiqiji.money.modules.community.travel.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by dansakai on 2017/8/1.
 */

public class DecorateListMultiItem implements MultiItemEntity {

    public static final int TITLE = 1;//热门共享
    public static final int SING_BIG_IMG = 2;//一个大图
    public static final int THREE_IMG = 3;//三张小图(无标题)
    public static final int DECORAT_SINGLE_IMG = 4;//装修公司item
    public static final int THREE_IMG_HASTITLE = 5;//三张图有标题
    public static final int SING_SMALL_IMG = 6;//单张小图
    public static final int TEXT_NO_IMG = 7;//无标题

    private int itemType;
    private Object object;

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    @Override
    public Object getData() {
        return object;
    }

    @Override
    public void setData(Object o) {
        this.object = o;
    }
}
