package com.yiqiji.money.modules.book.bookcategory.model;

import java.io.Serializable;

/**
 * Created by leichi on 2017/5/16.
 * 自定义字段操作的返回数据结构
 */

public class SyncCategoryResult implements Serializable {
    public String pkid;                               //本地的临时id
    public String accountbookid;                      //账本的id
    public String categoryid;                         //分类的id

}
