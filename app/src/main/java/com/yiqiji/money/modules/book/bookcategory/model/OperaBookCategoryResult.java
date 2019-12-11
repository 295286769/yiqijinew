package com.yiqiji.money.modules.book.bookcategory.model;

import java.io.Serializable;

/**
 * Created by leichi on 2017/5/28.
 */

public class OperaBookCategoryResult implements Serializable{
    public int mInstruct;                              //操作指令（1:编辑，5：添加）
    public int operaHierarchy;                         //操作的层级（1：group,2:child）
    public IBaseBookCategory baseBookCategory;         //操作后的分类对象

}
