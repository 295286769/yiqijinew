package com.yiqiji.money.modules.common.entity;

/**
 * Created by Administrator on 2016/9/19.
 */
public class RecyBean {
    private int label;
    private String name;
    private String money;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public RecyBean(int label, String name, String money) {
        this.label = label;
        this.name = name;
        this.money = money;

    }
    public RecyBean() {

    }
}
