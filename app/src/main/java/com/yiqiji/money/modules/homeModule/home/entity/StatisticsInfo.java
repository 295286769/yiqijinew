package com.yiqiji.money.modules.homeModule.home.entity;

import java.util.List;

/**
 * Created by ${huangweishui} on 2017/7/5.
 * address huang.weishui@71dai.com
 */
public class StatisticsInfo {
    private double totalamount;
    private List<StaticsBillInfo> list;

    public List<StaticsBillInfo> getList() {
        return list;
    }

    public void setList(List<StaticsBillInfo> list) {
        this.list = list;
    }

    public double getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(double totalamount) {
        this.totalamount = totalamount;
    }
}
