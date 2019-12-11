package com.yiqiji.money.modules.book.bookcategory.model;

/**
 * Created by leichi on 2017/5/25.
 * 分类的操作类型
 */

public class BookCategoryOperationType {
    public static final int TO_EDIT = 1;                           //编辑
    public static final int TO_DISABLE = 2;                        //停用
    public static final int TO_DELETE = 3;                         //删除
    public static final int TO_ENABLE = 4;                         //启用
    public static final int TO_ADD = 5;                            //添加


    private int operationType=0;

    public int getOperationType() {
        return operationType;
    }

    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }
}
