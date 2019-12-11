package com.yiqiji.money.modules.homeModule.home.entity;

import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.common.db.DailycostEntity;

import java.util.List;

public class BillInfo {
    private String myincome;// 累计收入
    private String myspent;// 累计消费
    private String myspentdiff;// 我需付（当值小于0）/我应收（当值大于0）/已结清（当值等于0）
    private String billcount;//记账笔数
    private String billdays;//记账天数
    private List<DailycostEntity> list;
    private BooksDbInfo bookdetail;

    public BooksDbInfo getBookdetail() {
        return bookdetail;
    }

    public void setBookdetail(BooksDbInfo bookdetail) {
        this.bookdetail = bookdetail;
    }

    public List<DailycostEntity> getList() {
        return list;
    }

    public void setList(List<DailycostEntity> list) {
        this.list = list;
    }

    public String getMyspent() {
        return myspent;
    }

    public void setMyspent(String myspent) {
        this.myspent = myspent;
    }

    public String getMyspentdiff() {
        return myspentdiff;
    }

    public void setMyspentdiff(String myspentdiff) {
        this.myspentdiff = myspentdiff;
    }

    public String getMyincome() {
        return myincome;
    }

    public void setMyincome(String myincome) {
        this.myincome = myincome;
    }

    public String getBillcount() {
        return billcount;
    }

    public void setBillcount(String billcount) {
        this.billcount = billcount;
    }

    public String getBilldays() {
        return billdays;
    }

    public void setBilldays(String billdays) {
        this.billdays = billdays;
    }
}
