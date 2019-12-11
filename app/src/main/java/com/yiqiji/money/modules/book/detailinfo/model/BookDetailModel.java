package com.yiqiji.money.modules.book.detailinfo.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by leichi on 2017/6/13.
 */

public class BookDetailModel implements Serializable {

    public String accountbookid;
    public String accountbooktitle;
    public String userid;
    public String deviceid;
    public String accountbookcate;
    public String accountbooktype;
    public String accountbookbudget;
    public String accountbookstatus;
    public String accountbookcount;
    public String accountbookbgimg;
    public String isclear;
    public String accountbookctime;
    public String accountbookutime;
    public String accountbookltime;
    public String firsttime;//记账最早时间
    public String username;                                    //账本的创建者
    public String memberid;                                    //账本创建者对应的成员ID
    public ArrayList<String> accountbookdesc;                  //账本描述
    public String commenttotal;                                //评论总数
    public String isfollow;                                    //0 是未关注，1是关注
    public String follownum;                                   //关注数
    public String viewcount;                                   //浏览数
    public String subscribeid;                                 //订阅id


}
