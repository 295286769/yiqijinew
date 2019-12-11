package com.yiqiji.money.modules.property.entity;

import java.io.Serializable;

/**
 * Created by dansakai on 2017/3/10.
 * 银行实体类
 */

public class PropertyBanEntity implements Serializable {

    private int type;//类型

    private String letters;//字母


    private String color;//颜色
    private String bankicon;//银行icon
    private String bankname;//银行name
    private String bankid;//银行id
    private String ishot;//是否热门
    private boolean ishasHead;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLetters() {
        return letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBankicon() {
        return bankicon;
    }

    public void setBankicon(String bankicon) {
        this.bankicon = bankicon;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getBankid() {
        return bankid;
    }

    public void setBankid(String bankid) {
        this.bankid = bankid;
    }

    public String getIshot() {
        return ishot;
    }

    public void setIshot(String ishot) {
        this.ishot = ishot;
    }

    public boolean ishasHead() {
        return ishasHead;
    }

    public void setIshasHead(boolean ishasHead) {
        this.ishasHead = ishasHead;
    }
}
