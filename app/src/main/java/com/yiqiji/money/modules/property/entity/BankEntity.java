package com.yiqiji.money.modules.property.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dansakai on 2017/3/11.
 */

public class BankEntity {

    private List<PropertyBanEntity> propertyBanEntities;//银行列表

    private List<PropertyBanEntity> hotList;//热门银行列表

    public List<PropertyBanEntity> getPropertyBanEntities() {
        return propertyBanEntities;
    }

    public void setPropertyBanEntities(List<PropertyBanEntity> propertyBanEntities) {
        this.propertyBanEntities = propertyBanEntities;
    }

    public List<PropertyBanEntity> getHotList() {
        return hotList;
    }

    public void setHotList(List<PropertyBanEntity> hotList) {
        this.hotList = hotList;
    }

    public static BankEntity parce(String string) throws JSONException {
        BankEntity entity = new BankEntity();
        JSONArray array = new JSONArray(string);
        JSONObject obj = null;
        PropertyBanEntity tempEntity = null;//
        String letter = "";//字母

        List<PropertyBanEntity> tempList = new ArrayList<>();//银行列表

        List<PropertyBanEntity> tempHotList = new ArrayList<>();//热门银行列表

        if (array != null && array.length() > 0) {
            for (int i = 0; i < array.length(); i++) {
                obj = array.optJSONObject(i);
                if (obj.optString("letters").equals(letter)) {
                    tempEntity = new PropertyBanEntity();
                    letter = obj.optString("letters");
                    tempEntity.setLetters(letter);
                    tempEntity.setBankid(obj.optString("bankid"));
                    tempEntity.setBankicon(obj.optString("bankicon"));
                    tempEntity.setBankname(obj.optString("bankname"));
                    tempEntity.setIshot(obj.optString("ishot"));
                    tempEntity.setIshasHead(false);
                    tempList.add(tempEntity);
                    if ("1".equals(obj.optString("ishot"))) {
                        tempHotList.add(tempEntity);
                    }
                } else {
                    tempEntity = new PropertyBanEntity();
                    letter = obj.optString("letters");
                    tempEntity.setLetters(letter);
                    tempEntity.setBankid(obj.optString("bankid"));
                    tempEntity.setBankicon(obj.optString("bankicon"));
                    tempEntity.setBankname(obj.optString("bankname"));
                    tempEntity.setIshot(obj.optString("ishot"));
                    tempEntity.setIshasHead(true);
                    tempList.add(tempEntity);
                    if ("1".equals(obj.optString("ishot"))) {
                        tempHotList.add(tempEntity);
                    }
                }
            }
            entity.setHotList(tempHotList);
            entity.setPropertyBanEntities(tempList);
        }
        return entity;
    }


}
