package com.yiqiji.money.modules.homeModule.home.entity;

import java.util.List;

/**
 * Created by ${huangweishui} on 2017/7/10.
 * address huang.weishui@71dai.com
 */
public class StaticsBillInfo {
    /**
     * totalamount : 300.00
     * categoryid : 354
     * categorytitle : 景点门票
     * categoryicon : http://static.test.yiqijiba.com/accountbook/category/new/12/354@2x.png
     * percentage : 0.19
     * list : null
     */

    private String totalamount;
    private String categoryid;
    private String categorytitle;
    private String categoryicon;
    private double percentage;
    private List<CatelistBeanChild> list;

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getCategorytitle() {
        return categorytitle;
    }

    public void setCategorytitle(String categorytitle) {
        this.categorytitle = categorytitle;
    }

    public String getCategoryicon() {
        return categoryicon;
    }

    public void setCategoryicon(String categoryicon) {
        this.categoryicon = categoryicon;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public List<CatelistBeanChild> getList() {
        return list;
    }

    public void setList(List<CatelistBeanChild> list) {
        this.list = list;
    }
}
