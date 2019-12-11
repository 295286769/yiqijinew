package com.yiqiji.money.modules.homeModule.mybook.entity;

/**
 * Created by ${huangweishui} on 2017/8/2.
 * address huang.weishui@71dai.com
 */
public class AddressInfo {
    private String id;//账本id
    private String houseid = "";//房子id
    private String housecity = "";//所在城市
    private String housename;//小区名字
    private String housetype = "";//户型
    private String housesquare = "";//房子面积
    private String housestyle = "";//装修风格
    private String houseway = "";//装修方式
    private String company = "";//装修公司

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHouseid() {
        return houseid;
    }

    public void setHouseid(String houseid) {
        this.houseid = houseid;
    }

    public String getHousecity() {
        return housecity;
    }

    public void setHousecity(String housecity) {
        this.housecity = housecity;
    }

    public String getHousename() {
        return housename;
    }

    public void setHousename(String housename) {
        this.housename = housename;
    }

    public String getHousetype() {
        return housetype;
    }

    public void setHousetype(String housetype) {
        this.housetype = housetype;
    }

    public String getHousesquare() {
        return housesquare;
    }

    public void setHousesquare(String housesquare) {
        this.housesquare = housesquare;
    }

    public String getHousestyle() {
        return housestyle;
    }

    public void setHousestyle(String housestyle) {
        this.housestyle = housestyle;
    }

    public String getHouseway() {
        return houseway;
    }

    public void setHouseway(String houseway) {
        this.houseway = houseway;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
