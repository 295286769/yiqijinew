package com.yiqiji.money.modules.common.entity.eventbean;

import com.yiqiji.money.modules.common.entity.SuggestionItemLocation;

/**
 * Created by ${huangweishui} on 2017/3/20.
 * address huang.weishui@71dai.com
 */
public class SuggestionItem {

    private String name;
    private SuggestionItemLocation location;
    private String uid;
    private String city;
    private String district;
    private String business;
    private String cityid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SuggestionItemLocation getLocation() {
        return location;
    }

    public void setLocation(SuggestionItemLocation location) {
        this.location = location;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }
}
