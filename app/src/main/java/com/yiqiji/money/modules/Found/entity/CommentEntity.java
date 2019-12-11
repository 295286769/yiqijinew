package com.yiqiji.money.modules.Found.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dansakai on 2017/3/21.
 * 评论实体
 */

public class CommentEntity implements Serializable {
    private String commentid;//评论id
    private String content;//内容
    private String articleid;//文章id
    private String ctime;//评论时间
    private String userid;
    private String usericon;
    private String username;

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getArticleid() {
        return articleid;
    }

    public void setArticleid(String articleid) {
        this.articleid = articleid;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsericon() {
        return usericon;
    }

    public void setUsericon(String usericon) {
        this.usericon = usericon;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static List<CommentEntity> parceLis(String string) throws JSONException{
        List<CommentEntity> entities = new ArrayList<>();
        JSONArray array = new JSONArray(string);
        JSONObject object = null;
        CommentEntity entity = null;
        if (array != null && array.length() > 0) {
            for (int i = 0;i< array.length() ;i++) {
                object = array.getJSONObject(i);
                entity = new CommentEntity();
                entity.setArticleid(object.optString("articleid"));
                entity.setCommentid(object.optString("commentid"));
                entity.setContent(object.optString("content"));
                entity.setCtime(object.optString("ctime"));
                entity.setUsericon(object.optString("usericon"));
                entity.setUserid(object.optString("userid"));
                entity.setUsername(object.optString("username"));
                entities.add(entity);
            }
        }
        return entities;
    }
}
