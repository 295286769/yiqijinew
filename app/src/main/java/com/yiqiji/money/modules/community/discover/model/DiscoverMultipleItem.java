package com.yiqiji.money.modules.community.discover.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by leichi on 2017/7/31.
 */

public class DiscoverMultipleItem implements MultiItemEntity{

    private ViewType viewType;
    private Object data;

    public enum ViewType{
        TITLE,                               //标题布局
        BOOK_TEXT_IMAGE,                     //显示一个图片加文字的账本布局
        BOOK_RACK,                           //账本书架布局
        BOOK_SINGLE_IMAGE,                   //只显示图片的账本
        BOOK_SINGLE_TEXT,                    //只显示文字的账本
        BOOK_MULTIPLE_IMAGE,                 //多图片显示的账本
        TAB_LIST,                            //横向滚动TabView
    }

    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
    }

    @Override
    public int getItemType() {
        return viewType.ordinal();
    }

    public ViewType getItemViewType() {
        return viewType;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public void setData(Object o) {
          this.data=o;
    }
}
