package com.yiqiji.money.modules.community.decoration.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by dansakai on 2017/8/4.
 */

public class NearBookMultiItem implements MultiItemEntity {

    public static final int TITLE = 1;//title
    public static final int DEFAUT_BOOKS = 2;//默认小区下的相关账本
    public static final int THREE_IMG_HASTITLE = 3;//三张图有标题
    public static final int SING_SMALL_IMG = 4;//单张小图
    public static final int TEXT_NO_IMG = 5;//无标题

    private int item_type;
    private Object object;

    @Override
    public int getItemType() {
        return item_type;
    }

    public void setItemType(int type) {
        this.item_type = type;
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
