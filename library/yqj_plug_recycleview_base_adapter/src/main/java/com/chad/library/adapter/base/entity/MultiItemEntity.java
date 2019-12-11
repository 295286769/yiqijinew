package com.chad.library.adapter.base.entity;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public interface MultiItemEntity {

    int getItemType();

    Object getData();

    void setData(Object o);
}
