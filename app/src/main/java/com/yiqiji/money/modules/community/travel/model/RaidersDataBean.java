package com.yiqiji.money.modules.community.travel.model;

import java.util.List;

/**
 * Created by ${huangweishui} on 2017/8/3.
 * address huang.weishui@71dai.com
 */
public class RaidersDataBean {
    private List<RaidersDataItemBean> guide;
    private List<RaidersDataItemBean> route;
    private List<RaidersDataItemBean> point;
    private List<RaidersDataItemBean> food;
    private List<RaidersDataItemBean> tips;

    public List<RaidersDataItemBean> getGuide() {
        return guide;
    }

    public void setGuide(List<RaidersDataItemBean> guide) {
        this.guide = guide;
    }

    public List<RaidersDataItemBean> getRoute() {
        return route;
    }

    public void setRoute(List<RaidersDataItemBean> route) {
        this.route = route;
    }

    public List<RaidersDataItemBean> getPoint() {
        return point;
    }

    public void setPoint(List<RaidersDataItemBean> point) {
        this.point = point;
    }

    public List<RaidersDataItemBean> getFood() {
        return food;
    }

    public void setFood(List<RaidersDataItemBean> food) {
        this.food = food;
    }

    public List<RaidersDataItemBean> getTips() {
        return tips;
    }

    public void setTips(List<RaidersDataItemBean> tips) {
        this.tips = tips;
    }

}
