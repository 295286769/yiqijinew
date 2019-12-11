package com.yiqiji.money.modules.common.entity;

/**
 * Created by whl on 16/9/24.
 */
public class CapitalBean {
    private String type;
    private String time;
    private String money;

    public CapitalBean(String type, String time, String money) {
        this.type = type;
        this.time = time;
        this.money = money;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
