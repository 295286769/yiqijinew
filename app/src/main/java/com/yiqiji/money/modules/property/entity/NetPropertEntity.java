package com.yiqiji.money.modules.property.entity;


import com.yiqiji.frame.core.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by dansakai on 2017/3/22.
 */

public class NetPropertEntity implements Serializable {
    private String itemtype;//资产分类
    private String assetid;//id
    private String itemname;//名称
    private String marktext;//标记
    private String itemcateicon;//图片
    private String assetamount;//本金
    private String todayprofit;//日收益
    private String totalprofit;//累计收益
    private String yieldrate;//年华
    private String dividendmethod;//汇款方式
    private String interestdate;//气息日期
    private String deadline;//天数

    private String asssetlog;//还款计划信息


    public String getItemtype() {
        return itemtype;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
    }

    public String getAssetid() {
        return assetid;
    }

    public void setAssetid(String assetid) {
        this.assetid = assetid;
    }

    public String getMarktext() {
        return marktext;
    }

    public void setMarktext(String marktext) {
        this.marktext = marktext;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemcateicon() {
        return itemcateicon;
    }

    public void setItemcateicon(String itemcateicon) {
        this.itemcateicon = itemcateicon;
    }

    public String getAssetamount() {
        return assetamount;
    }

    public void setAssetamount(String assetamount) {
        this.assetamount = assetamount;
    }

    public String getTodayprofit() {
        return todayprofit;
    }

    public void setTodayprofit(String todayprofit) {
        this.todayprofit = todayprofit;
    }

    public String getTotalprofit() {
        return totalprofit;
    }

    public void setTotalprofit(String totalprofit) {
        this.totalprofit = totalprofit;
    }

    public String getYieldrate() {
        return yieldrate;
    }

    public void setYieldrate(String yieldrate) {
        this.yieldrate = yieldrate;
    }

    public String getDividendmethod() {
        return dividendmethod;
    }

    public void setDividendmethod(String dividendmethod) {
        this.dividendmethod = dividendmethod;
    }

    public String getInterestdate() {
        return interestdate;
    }

    public void setInterestdate(String interestdate) {
        this.interestdate = interestdate;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getAsssetlog() {
        return asssetlog;
    }

    public void setAsssetlog(String asssetlog) {
        this.asssetlog = asssetlog;
    }

    public static NetPropertEntity parceEntity(String string) throws JSONException{
        NetPropertEntity entity = new NetPropertEntity();
        JSONObject jo_data = new JSONObject(string);
        entity.setItemtype(jo_data.optString("itemtype"));
        entity.setAssetid(jo_data.optString("assetid"));
        entity.setItemname(jo_data.optString("itemname"));
        entity.setMarktext(jo_data.optString("marktext"));
        entity.setAssetamount(jo_data.optString("assetamount"));
        entity.setItemcateicon(jo_data.optString("itemcateicon"));
        entity.setAsssetlog(jo_data.optString("asssetlog"));

        if (!StringUtils.isEmpty(jo_data.optString("attach"))) {
            JSONObject jo_attach = new JSONObject(jo_data.optString("attach"));
            entity.setTodayprofit(jo_attach.optString("todayprofit"));
            entity.setTotalprofit(jo_attach.optString("totalprofit"));
            entity.setYieldrate(jo_attach.optString("yieldrate"));
            entity.setInterestdate(jo_attach.optString("interestdate"));
            entity.setDeadline(jo_attach.optString("deadline"));
            entity.setDividendmethod(jo_attach.optString("dividendmethod"));
        }

        return entity;
    }

}
