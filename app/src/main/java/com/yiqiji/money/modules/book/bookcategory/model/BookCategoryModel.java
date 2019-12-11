package com.yiqiji.money.modules.book.bookcategory.model;

import java.io.Serializable;

/**
 * 账本的字段结构模型
 */
public class BookCategoryModel implements Serializable{

    public BookCategory expendCategory;                  //账本支出分类  BookCategory.billtype=1;
    public BookCategory incomeCategory;                  //账本收入分类  BookCategory.billtype=0;
    public String accountbookid;                         //账本Id(本地新加字段，方便外部使用)
    public boolean isSyced;                              //是否已经同步过
}
