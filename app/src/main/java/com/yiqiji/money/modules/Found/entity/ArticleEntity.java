package com.yiqiji.money.modules.Found.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dansakai on 2017/3/20.
 * 发现文章实体
 */

public class ArticleEntity implements Serializable {
    private String articleid;//文章id
    private String articletitle;//文章标题
    private String articleimg;//图片
    private String articlestatus;//状态
    private String articletime;//时间
    private String articletext;//文章内容
    private String articleurl;//详细页展示的Url

    public String getArticleurl() {
        return articleurl;
    }

    public void setArticleurl(String articleurl) {
        this.articleurl = articleurl;
    }

    public String getArticleid() {
        return articleid;
    }

    public void setArticleid(String articleid) {
        this.articleid = articleid;
    }

    public String getArticletitle() {
        return articletitle;
    }

    public void setArticletitle(String articletitle) {
        this.articletitle = articletitle;
    }

    public String getArticleimg() {
        return articleimg;
    }

    public void setArticleimg(String articleimg) {
        this.articleimg = articleimg;
    }

    public String getArticlestatus() {
        return articlestatus;
    }

    public void setArticlestatus(String articlestatus) {
        this.articlestatus = articlestatus;
    }

    public String getArticletime() {
        return articletime;
    }

    public void setArticletime(String articletime) {
        this.articletime = articletime;
    }

    public String getArticletext() {
        return articletext;
    }

    public void setArticletext(String articletext) {
        this.articletext = articletext;
    }

    public static List<ArticleEntity> parceLis(String string) throws JSONException {
        List<ArticleEntity> articleEntities = new ArrayList<>();
        JSONArray array = new JSONArray(string);
        JSONObject object = null;
        ArticleEntity entity = null;
        if (array != null && array.length() > 0) {
            for (int i = 0; i < array.length(); i++) {
                object = array.getJSONObject(i);
                entity = new ArticleEntity();
                entity.setArticleid(object.optString("articleid"));
                entity.setArticleimg(object.optString("articleimg"));
                entity.setArticlestatus(object.optString("articlestatus"));
                entity.setArticletime(object.optString("articletime"));
                entity.setArticletext(object.optString("articletext"));
                entity.setArticletitle(object.optString("articletitle"));
                entity.setArticleurl(object.optString("articleurl"));
                articleEntities.add(entity);
            }
        }
        return articleEntities;
    }
}
