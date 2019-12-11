package com.yiqiji.money.modules.Found.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dansakai on 2017/3/20.
 * 发现头部滚动广告实体
 */

public class BannerAdEntity implements Serializable{
    private int type;//类型
    private String title;//标题
    private String img;//图片地址
    private String url;//图片关联url
    private String text;//文案

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static List<BannerAdEntity> parceLis(String string) throws JSONException{
        List<BannerAdEntity> adlist = new ArrayList<>();
        JSONArray array = new JSONArray(string);
        JSONObject object = null;
        BannerAdEntity entity = null;
        if (array != null && array.length() > 0) {
            for (int i = 0;i < array.length() ;i++) {
                entity = new BannerAdEntity();
                object = array.getJSONObject(i);
                entity.setType(object.optInt("type"));
                entity.setImg(object.optString("img"));
                entity.setUrl(object.optString("url"));
                entity.setText(object.optString("text"));
                entity.setTitle(object.optString("title"));
                adlist.add(entity);
            }
        }
        return adlist;
    }
}
