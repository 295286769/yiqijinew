package com.yiqiji.money.modules.community.travel.model;

import java.util.List;

/**
 * Created by ${huangweishui} on 2017/8/2.
 * address huang.weishui@71dai.com
 */
public class GuideIteminfo {
    /**
     * title : 游走最纯粹的净土，稻城五日行
     * text : ''
     * img : http://cloud.test.yiqijiba.com/advert/1501234782310196.jpg
     * imglist : ["http://cloud.test.yiqijiba.com/advert/1501234782310196.jpg"]
     * url : ziniuapp://money/accountbook/67039/share/
     * type : 1
     * viewcount : 1
     * follownum : 0
     * commentcount : 0
     */

    private String title;
    private String text;
    private String img;
    private String url;
    private String type;
    private int viewcount;
    private int follownum;
    private int commentcount;
    private List<String> imglist;

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
}
