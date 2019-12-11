package com.yiqiji.money.modules.book.bookcategory.model;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by leichi on 2017/5/17.
 */

public class BookCategoryListMultipleItem extends AbstractExpandableItem<BookCategoryListMultipleItem> implements MultiItemEntity{

    public enum ViewType{
        GROUP,
        CHILD,
        ADD_BUTTON,
        CUTLINE
    }

    private ViewType viewType;
    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setItemType(ViewType viewType) {
        this.viewType = viewType;
    }

    @Override
    public int getItemType() {
        return viewType.ordinal();
    }

    public static ViewType getViewType(int ordinal){
        for (ViewType viewType:ViewType.values()){
            if(viewType.ordinal()==ordinal){
                return viewType;
            }
        }
        return null;
    }

    @Override
    public int getLevel() {
        if(viewType==ViewType.GROUP||viewType==ViewType.CUTLINE){
            return 0;
        }
        return 1;
    }
}
