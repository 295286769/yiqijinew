package com.yiqiji.money.modules.property.entity;

import java.io.Serializable;

/**
 * Created by dansakai on 2017/3/7.
 */

public class PropertyItemEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private int type; // 类型

    //第一级 资产大分类
    private String categoryid;//大分类id(1资金账户、2投资理财、3应收付款)
    private String category;//分类名
    private String categoryname;//分类名(+数量)
    private String totalamount;//总金额
    private int childNm;//大分类下面的小分类个数
    //第二季 资产小分类
    private String assetid;//
    private String itemtype;//资产小类id
    private String itemcateicon;//资产小类icon
    private String itemname;//资产小类名
    private String marktext;//标记
    private String assetamount;//资产小类资金
    private String todaydiff;//今日支出盈收情况

    private String assetctime;//时间戳 （信用卡需要显示）
    private String itemcatename;//信用卡借记卡需要显示这个字段作为mark
    private String profitamount;//投资理财总收益

    //股票
    private String stockcode;//股票代码
    private String stocknum;//持股数量
    private String currentprice;//每股价格

    //投资理财
    private String totalprofit;//累计收益
    private String yieldrate;//年华
    private String dividendmethod;//汇款方式
    private String interestdate;//气息日期
    private String deadline;//天数
    private String mark;
    private Attach attach;

    public static class Attach  implements Serializable {
        public String loanid;
        public String assetid;
        public String loantype;      // 借款类型：1借入，2.借出
        public String preinterest;  // 预期收益
        public String loandate;     // 借款日期
        public String remark;       // 借款备注
        public String isremind;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }

    public int getChildNm() {
        return childNm;
    }

    public void setChildNm(int childNm) {
        this.childNm = childNm;
    }

    public String getItemtype() {
        return itemtype;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
    }

    public String getItemcateicon() {
        return itemcateicon;
    }

    public void setItemcateicon(String itemcateicon) {
        this.itemcateicon = itemcateicon;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
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

    public String getTodaydiff() {
        return todaydiff;
    }

    public void setTodaydiff(String todaydiff) {
        this.todaydiff = todaydiff;
    }

    public String getAssetctime() {
        return assetctime;
    }

    public void setAssetctime(String assetctime) {
        this.assetctime = assetctime;
    }

    public String getItemcatename() {
        return itemcatename;
    }

    public void setItemcatename(String itemcatename) {
        this.itemcatename = itemcatename;
    }

    public String getProfitamount() {
        return profitamount;
    }

    public void setProfitamount(String profitamount) {

        this.profitamount = profitamount;
    }

    public String getAssetid() {
        return assetid;
    }

    public void setAssetid(String assetid) {

        this.assetid = assetid;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
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

    public Attach getAttach() {
        return attach;
    }
    public void setAttach(Attach attach) {
        this.attach = attach;
    }

}
