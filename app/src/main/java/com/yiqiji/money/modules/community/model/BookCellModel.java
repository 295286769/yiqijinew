package com.yiqiji.money.modules.community.model;

import com.yiqiji.money.modules.community.decoration.model.UserinfoBean;

import java.util.List;

/**
 * Created by leichi on 2017/7/27.
 */

public class BookCellModel {
    private String title;                   //标题
    private String text;                    //内容描述
    private String url;                     //跳转url
    private String type;                    //类型
    private int viewcount;                  //阅读数
    private int follownum;                  //关注数
    private int commentcount;               //评论数
    private String img;                     //账本第一张图片
    private List<String> imglist;           //账本的所有图片
    private UserinfoBean userinfo;          //用户信息

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getViewcount() {
        return viewcount;
    }

    public void setViewcount(int viewcount) {
        this.viewcount = viewcount;
    }

    public int getFollownum() {
        return follownum;
    }

    public void setFollownum(int follownum) {
        this.follownum = follownum;
    }

    public int getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(int commentcount) {
        this.commentcount = commentcount;
    }

    public List<String> getImglist() {
        return imglist;
    }

    public void setImglist(List<String> imglist) {
        this.imglist = imglist;
    }

    public UserinfoBean getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserinfoBean userinfo) {
        this.userinfo = userinfo;
    }
}
