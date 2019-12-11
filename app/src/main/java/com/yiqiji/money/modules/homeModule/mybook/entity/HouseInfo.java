package com.yiqiji.money.modules.homeModule.mybook.entity;

/**
 * Created by ${huangweishui} on 2017/8/7.
 * address huang.weishui@71dai.com
 */
public class HouseInfo {

    /**
     * data : {"houseid":"1","housename":"华阳镇嘉美地","housetype":"","housesquare":"136㎡","housestyle":"欧式田园","houseway":"半包","housecity":"北京市","company":"","housectime":"1466480220","accountbookid":"77280"}
     * code : 0
     */

    private HouseDataInfo data;
    private int code;

    public HouseDataInfo getData() {
        return data;
    }

    public void setData(HouseDataInfo data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
