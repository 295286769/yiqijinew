package com.yiqiji.money.modules.book.detailinfo.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by leichi on 2017/6/13.
 */

public class BookBillModel implements Serializable {

    public String myspent;
    public String billcount;
    public BookDetailModel bookdetail;
    public List<BookBillItemModel> list;
    public List<BookBillItemModel> daily;
}
