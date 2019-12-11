package com.yiqiji.money.modules.property.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dansakai on 2017/3/11.
 */

public class PropertyTransEntity implements Serializable{

    private String assetid;//账户ID
    private String itemname;//账户名
    private String itemicon;//账户icon
    private String marktext;

    public String getAssetid() {
        return assetid;
    }

    public void setAssetid(String assetid) {
        this.assetid = assetid;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemicon() {
        return itemicon;
    }

    public void setItemicon(String itemicon) {
        this.itemicon = itemicon;
    }

    public String getMarktext() {
        return marktext;
    }

    public void setMarktext(String marktext) {
        this.marktext = marktext;
    }

    public static List<PropertyTransEntity> parceList(String string) throws JSONException{
        List<PropertyTransEntity> lists = new ArrayList<>();
        PropertyTransEntity entity = null;
        JSONObject obj = null;
        JSONArray array = new JSONArray(string);
        if(array != null){
            if ( array.length() > 0) {
                for (int i= 0;i< array.length();i++) {
                    entity = new PropertyTransEntity();
                    obj = array.optJSONObject(i);
                    entity.setAssetid(obj.optString("assetid"));
                    entity.setItemname(obj.optString("itemname"));
                    entity.setItemicon(obj.optString("itemicon"));
                    entity.setMarktext(obj.optString("marktext"));
                    lists.add(entity);
                }
            }
//            entity = new PropertyTransEntity();
//            entity.setAssetid("-1");
//            entity.setItemname("新建账户");
//            lists.add(entity);
        }


        return lists;
    }
}
