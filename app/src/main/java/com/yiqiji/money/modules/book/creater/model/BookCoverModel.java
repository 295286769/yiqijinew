package com.yiqiji.money.modules.book.creater.model;

import java.io.Serializable;

/**
 * Created by leichi on 2017/6/27.
 */

public class BookCoverModel implements Serializable {

    public String categoryid;
    public String categorytitle;                    //账本标题
    public String categorydesc;                     //账本描述
    public String categorytype;                     //账本类型
    public String categoryicon;                     //账本封面
    public String parentid;
    public String classid;
    public String status;
    public String isclear;                          //
    public String accountbookbgimg;                 //账本背景

}
