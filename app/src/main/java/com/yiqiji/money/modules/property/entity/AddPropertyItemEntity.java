package com.yiqiji.money.modules.property.entity;

import java.io.Serializable;

/**
 * Created by dansakai on 2017/3/8.
 */

public class AddPropertyItemEntity implements Serializable{

    private String assetid;//
    private String itemtypeId;//子资产分类id
    private String itemname;//资产名称
    private String categoryname;//子资产cate
    private String categoryid;//子资产所在资产类目的id
    private String categoryicon;//图标

    private String marktext;//备注信息
    private String assetamount;//余额


    //银行卡 信用卡
    private String bankid;//银行卡ID
    private String bankicon;//银行卡图标
    private String bankname;//银行卡名称
    private String creditlimit;//信用卡额度
    private String billday;//账单日
    private String repayday;//还款日
    //股票
    private String stockcode;//股票代码
    private String stocknum;//持股数量
    private String currentprice;//每股价格

    public String getStockcode() {
        return stockcode;
    }

    public void setStockcode(String stockcode) {
        this.stockcode = stockcode;
    }

    public String getStocknum() {
        return stocknum;
    }

    public void setStocknum(String stocknum) {
        this.stocknum = stocknum;
    }

    public String getCurrentprice() {
        return currentprice;
    }

    public void setCurrentprice(String currentprice) {
        this.currentprice = currentprice;
    }

    public String getCreditlimit() {
        return creditlimit;
    }

    public void setCreditlimit(String creditlimit) {
        this.creditlimit = creditlimit;
    }

    public String getBillday() {
        return billday;
    }

    public void setBillday(String billday) {
        this.billday = billday;
    }

    public String getRepayday() {
        return repayday;
    }

    public void setRepayday(String repayday) {
        this.repayday = repayday;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getBankicon() {
        return bankicon;
    }

    public void setBankicon(String bankicon) {
        this.bankicon = bankicon;
    }

    public String getBankid() {
        return bankid;
    }

    public void setBankid(String bankid) {
        this.bankid = bankid;
    }

    public String getAssetid() {
        return assetid;
    }

    public void setAssetid(String assetid) {
        this.assetid = assetid;
    }

    public String getItemtypeId() {
        return itemtypeId;
    }

    public void setItemtypeId(String itemtypeId) {
        this.itemtypeId = itemtypeId;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getCategoryicon() {
        return categoryicon;
    }

    public void setCategoryicon(String categoryicon) {
        this.categoryicon = categoryicon;
    }

    public String getMarktext() {
        return marktext;
    }

    public void setMarktext(String marktext) {
        this.marktext = marktext;
    }

    public String getAssetamount() {
        return assetamount;
    }

    public void setAssetamount(String assetamount) {
        this.assetamount = assetamount;
    }
}
