package com.yiqiji.money.modules.property.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dansakai on 2017/3/8.
 * 添加资产实体类
 */

public class AddPropertyEntity {

    private List<AddPropertyItemEntity> moneyItemEntities;//资金账户
    private List<AddPropertyItemEntity> invesrItemEntities;//投资理财
    private List<AddPropertyItemEntity> borrowItemEntities;//应收应付

    public List<AddPropertyItemEntity> getBorrowItemEntities() {
        return borrowItemEntities;
    }

    public void setBorrowItemEntities(List<AddPropertyItemEntity> borrowItemEntities) {
        this.borrowItemEntities = borrowItemEntities;
    }

    public List<AddPropertyItemEntity> getMoneyItemEntities() {
        return moneyItemEntities;
    }

    public void setMoneyItemEntities(List<AddPropertyItemEntity> moneyItemEntities) {
        this.moneyItemEntities = moneyItemEntities;
    }

    public List<AddPropertyItemEntity> getInvesrItemEntities() {
        return invesrItemEntities;
    }

    public void setInvesrItemEntities(List<AddPropertyItemEntity> invesrItemEntities) {
        this.invesrItemEntities = invesrItemEntities;
    }

    public static AddPropertyEntity parceList(String string) throws JSONException {
        AddPropertyEntity entity = new AddPropertyEntity();
        AddPropertyItemEntity childEntity = null;
        List<AddPropertyItemEntity> itemEntities;//资金账户

        JSONArray array = new JSONArray(string);
        JSONArray arr = null;
        JSONObject obj = null;
        if (array != null && array.length() > 0) {
            for (int i = 0; i < array.length(); i++) {
                itemEntities = new ArrayList<>();
                obj = array.optJSONObject(i);
                arr = new JSONArray(obj.optString("child"));
                if (arr != null && arr.length() > 0) {
                    for (int j = 0; j < arr.length(); j++) {
                        childEntity = new AddPropertyItemEntity();
                        obj = arr.optJSONObject(j);
                        childEntity.setItemtypeId(obj.optString("categoryid"));//资产id
                        childEntity.setCategoryid(obj.optString("parentid"));//分类id
                        childEntity.setCategoryname(obj.optString("categoryname"));
                        childEntity.setCategoryicon(obj.optString("categoryicon"));
                        itemEntities.add(childEntity);
                    }
                }

                if ("1".equals(obj.optString("parentid"))) {
                    entity.setMoneyItemEntities(itemEntities);
                } else if ("2".equals(obj.optString("parentid"))) {
                    entity.setInvesrItemEntities(itemEntities);
                } else if ("3".equals(obj.optString("parentid"))) {
                    entity.setBorrowItemEntities(itemEntities);
                }
            }
        }

        return entity;
    }


}
