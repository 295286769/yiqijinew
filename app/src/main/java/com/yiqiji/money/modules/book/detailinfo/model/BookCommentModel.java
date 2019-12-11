package com.yiqiji.money.modules.book.detailinfo.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by leichi on 2017/6/15.
 */

public class BookCommentModel implements Serializable{
    public String commentid;                  //评论id
    public String content;                    //评论内容
    public String accountbookid;              //账本id
    public String billid;                     //账单id
    public String memberid;                   //成员id
    public String ctime;                      //评论时间
    public String topnodeid;                  //父评论的id
    public String childnodeid;                //
    public String userid;                     //评论人的id
    public String usericon;                   //评论人的头像
    public String username;                   //评论人的名字
    public String tousername;                 //发送评论对象的名称
    public String isreply;                    //对于获取这个评论的用户来说，1代表可以对此评论就进行回复，其他则不可以
    public List<BookCommentModel> child;      //子评论列表
    public boolean isChild;                   //是否是子评论
}
