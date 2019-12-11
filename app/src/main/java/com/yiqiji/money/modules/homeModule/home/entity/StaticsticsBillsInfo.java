package com.yiqiji.money.modules.homeModule.home.entity;

/**
 * Created by ${huangweishui} on 2017/6/27.
 * address huang.weishui@71dai.com
 */
public class StaticsticsBillsInfo {
    private String where;
    private String[] whereStrings;
    private String groupBy;
    private String having;
    private String orderBy;
    private String limit;

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String[] getWhereStrings() {
        return whereStrings;
    }

    public void setWhereStrings(String[] whereStrings) {
        this.whereStrings = whereStrings;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public String getHaving() {
        return having;
    }

    public void setHaving(String having) {
        this.having = having;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }
}
