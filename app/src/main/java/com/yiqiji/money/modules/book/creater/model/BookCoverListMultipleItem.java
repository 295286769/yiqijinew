package com.yiqiji.money.modules.book.creater.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yiqiji.money.modules.book.bookcategory.model.BookCategoryListMultipleItem;

/**
 * Created by leichi on 2017/6/27.
 */

public class BookCoverListMultipleItem implements MultiItemEntity{


    private ViewType viewType;
    private Object data;

    public enum ViewType{
        BOOK_AA_TYPE_TITLE,                 //需要AA的标题
        BOOK_NO_AA_TYPE_TITLE,              //不需要AA的标题
        BOOK_COVER_MODEL,                   //账本书皮数据
    }


    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
    }

    @Override
    public int getItemType() {
        return viewType.ordinal();
    }


    public ViewType getViewType() {
        return viewType;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public void setData(Object o) {
        data=o;
    }
}
