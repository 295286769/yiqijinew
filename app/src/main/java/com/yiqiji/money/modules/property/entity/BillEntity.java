package com.yiqiji.money.modules.property.entity;


import com.yiqiji.frame.core.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dansakai on 2017/3/24.
 * 资产关联账单实体
 */

public class BillEntity implements Serializable{
    public final static int TYPE_FIRST = 0;// 第一级 账单总流水
    public final static int TYPE_SECOND = 1;// 第二级 账单明细

    private int type;
    private boolean isColse;
    //第一级
    private String date;//时间
    private String totalpay;//流入
    private String totalinc;//流出

    //第二级
    private String billcateicon;//图片
    private String billcatename;//标题
    private String billctime;//时间
    private String billamount;//金额

    public boolean isColse() {
        return isColse;
    }

    public void setColse(boolean colse) {
        isColse = colse;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotalpay() {
        return totalpay;
    }

    public void setTotalpay(String totalpay) {
        this.totalpay = totalpay;
    }

    public String getTotalinc() {
        return totalinc;
    }

    public void setTotalinc(String totalinc) {
        this.totalinc = totalinc;
    }

    public String getBillcateicon() {
        return billcateicon;
    }

    public void setBillcateicon(String billcateicon) {
        this.billcateicon = billcateicon;
    }

    public String getBillcatename() {
        return billcatename;
    }

    public void setBillcatename(String billcatename) {
        this.billcatename = billcatename;
    }

    public String getBillctime() {
        return billctime;
    }

    public void setBillctime(String billctime) {
        this.billctime = billctime;
    }

    public String getBillamount() {
        return billamount;
    }

    public void setBillamount(String billamount) {
        this.billamount = billamount;
    }

    public static List<BillEntity> parceLis(String string) throws JSONException{
        List<BillEntity> billEntities = new ArrayList<>();
        if (StringUtils.isEmpty(string)) {
            return billEntities;
        } else {
            JSONArray array = new JSONArray(string);
            if (array != null && array.length() > 0) {
                BillEntity entity = null;
                JSONArray arrs = null;
                JSONObject object = null;
                JSONObject obj = null;
                for (int i = 0;i < array.length();i++) {
                    object = array.optJSONObject(i);
                    entity = new BillEntity();
                    entity.setType(TYPE_FIRST);
                    entity.setDate(object.optString("date"));
                    entity.setTotalpay(object.optString("totalpay"));
                    entity.setTotalinc(object.optString("totalinc"));
                    entity.setColse(true);
                    billEntities.add(entity);
                    if (!StringUtils.isEmpty(object.optString("list"))) {
                        arrs = new JSONArray(object.optString("list"));
                        if (arrs != null && arrs.length() > 0) {
                            for (int j = 0;j < arrs.length();j++) {
                                entity = new BillEntity();
                                obj = arrs.optJSONObject(j);
                                entity.setType(TYPE_SECOND);
                                entity.setColse(true);
                                entity.setBillcateicon(obj.optString("billcateicon"));
                                entity.setBillcatename(obj.optString("billcatename"));
                                entity.setBillctime(obj.optString("billctime"));
                                entity.setBillamount(obj.optString("billamount"));
                                billEntities.add(entity);
                            }

                        }
                    }
                }
            }
        }
        return billEntities;
    }
}
