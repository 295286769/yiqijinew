package com.yiqiji.money.modules.property.entity;

import java.io.Serializable;

/**
 * Created by dansakai on 2017/3/14.
 */

public class FundEntity implements Serializable {
    public int _id;
    public String userId;//用户名
    public String name;//名称
    public String code;//代码
    public String curPrice;//当前价格
    public String upDegr;//涨幅

    private boolean isAttention;//是否关注

    public String getUpDegr() {
        return upDegr;
    }

    public void setUpDegr(String upDegr) {
        this.upDegr = upDegr;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCurPrice() {
        return curPrice;
    }

    public void setCurPrice(String curPrice) {
        this.curPrice = curPrice;
    }

    public boolean isAttention() {
        return isAttention;
    }

    public void setAttention(boolean attention) {
        isAttention = attention;
    }
}
