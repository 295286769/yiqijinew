package com.yiqiji.money.modules.book.detailinfo.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by leichi on 2017/6/12.
 */

public class BookInfoListMultipItem implements MultiItemEntity {

    public enum ViewType {
        BILL,                      //账本
        BILL_DIARY,                //日记
        BILL_PAYMENT,              //交款
        BILL_SETTLEMENT,           //结算操作
        BILL_DATE,                 //日期
        BILL_RENOVATION,           //装修
        BILL_RENOVATION_DIARY,     //装修日志
        BILL_EMPTY,                //账单为空
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


    public ViewType getItemTypeEnum() {
        return getViewType(viewType.ordinal());
    }

    public static ViewType getViewType(int ordinal) {
        for (ViewType viewType : ViewType.values()) {
            if (viewType.ordinal() == ordinal) {
                return viewType;
            }
        }
        return null;
    }

}
