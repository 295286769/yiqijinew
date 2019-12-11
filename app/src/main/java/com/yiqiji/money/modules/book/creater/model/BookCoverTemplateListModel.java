package com.yiqiji.money.modules.book.creater.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by leichi on 2017/6/27.
 * 模板本列表
 */
public class BookCoverTemplateListModel implements Serializable {
    public List<BookCoverModel> single;         //非AA账本
    public List<BookCoverModel> multiple;       //AA账本
}
