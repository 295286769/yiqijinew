package com.yiqiji.money.modules.homeModule.home.entity;

import android.database.Cursor;

import com.yiqiji.money.modules.common.db.DailycostEntity;

import java.util.List;

public class TotalBalance {
    public static final String BILLTYPE = "billtype"; // 类型（开销/收入）

    public static final String SUM = "SUM(" + DailycostEntity.BILLAMOUNT + ")"; // 类型（开销/收入）

    public TotalBalance() {

    }

    public TotalBalance(Cursor cursor) {
        this.billtype = cursor.getString(cursor.getColumnIndex(BILLTYPE));
        this.total_balance = cursor.getDouble(cursor.getColumnIndex(SUM));
        this.moths = cursor.getInt(cursor.getColumnIndex(DailycostEntity.MOTH));
        this.day = cursor.getInt(cursor.getColumnIndex(DailycostEntity.DAY));
        this.content = cursor.getString(cursor.getColumnIndex(DailycostEntity.BILLCATENAME));
        // this.content = CostType.getCostType(
        // cursor.getInt(cursor.getColumnIndex(DailycostEntity.COSTTYPE)))
        // .getName();
        this.accountbooktype = cursor.getString(cursor.getColumnIndex(DailycostEntity.ACCOUNTBOOKTYPE));
        this.isClear = cursor.getString(cursor.getColumnIndex(DailycostEntity.BILLCLEAR));
        this.billsubcatename = cursor.getString(cursor.getColumnIndex(DailycostEntity.BILLSUBCATENAME));
        this.billcateicon = cursor.getString(cursor.getColumnIndex(DailycostEntity.BILLCATEICON));
        this.billcateid = cursor.getString(cursor.getColumnIndex(DailycostEntity.BILLCATEID));
        this.billsubcateid = cursor.getString(cursor.getColumnIndex(DailycostEntity.BILLSUBCATEID));
        this.billsubcateicon = cursor.getString(cursor.getColumnIndex(DailycostEntity.BILLSUBCATEICON));
        this.tridetime = Long.parseLong(cursor.getString(cursor.getColumnIndex(DailycostEntity.TRADETIME)));
        this.counts = cursor.getCount();
    }

    private String billtype;
    private double total_balance;
    private int moths;
    private int day;
    private String content;
    private String accountbooktype;// 0：单人账本 1：多人账本
    private String isClear;// 0：需要结算 1：不需要结算
    private String billsubcatename;
    private String billcateicon;
    private String billcateid;
    private String billsubcateid = "0";
    private String billsubcateicon;
    private boolean isSelect = false;
    private List<DailycostEntity> dailycostEntities;
    private List<TotalBalance> totalBalances;
    private int counts;
    private long tridetime;
    private String percentage;

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public long getTridetime() {
        return tridetime;
    }

    public void setTridetime(long tridetime) {
        this.tridetime = tridetime;
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public List<TotalBalance> getTotalBalances() {
        return totalBalances;
    }

    public void setTotalBalances(List<TotalBalance> totalBalances) {
        this.totalBalances = totalBalances;
    }

    public List<DailycostEntity> getDailycostEntities() {
        return dailycostEntities;
    }

    public void setDailycostEntities(List<DailycostEntity> dailycostEntities) {
        this.dailycostEntities = dailycostEntities;
    }

    public String getBillcateicon() {
        return billcateicon;
    }

    public void setBillcateicon(String billcateicon) {
        this.billcateicon = billcateicon;
    }

    public String getBillcateid() {
        return billcateid;
    }

    public void setBillcateid(String billcateid) {
        this.billcateid = billcateid;
    }

    public String getBillsubcatename() {
        return billsubcatename;
    }

    public void setBillsubcatename(String billsubcatename) {
        this.billsubcatename = billsubcatename;
    }

    public String getIsClear() {
        return isClear;
    }

    public void setIsClear(String isClear) {
        this.isClear = isClear;
    }

    public String getAccountbooktype() {
        return accountbooktype;
    }

    public void setAccountbooktype(String accountbooktype) {
        this.accountbooktype = accountbooktype;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getBilltype() {
        return billtype;
    }

    public void setBilltype(String billtype) {
        this.billtype = billtype;
    }

    public double getTotal_balance() {
        return total_balance;
    }

    public void setTotal_balance(double total_balance) {
        this.total_balance = total_balance;
    }

    public int getMoths() {
        return moths;
    }

    public void setMoths(int moths) {
        this.moths = moths;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBillsubcateid() {
        return billsubcateid;
    }

    public void setBillsubcateid(String billsubcateid) {
        this.billsubcateid = billsubcateid;
    }

    public String getBillsubcateicon() {
        return billsubcateicon;
    }

    public void setBillsubcateicon(String billsubcateicon) {
        this.billsubcateicon = billsubcateicon;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
