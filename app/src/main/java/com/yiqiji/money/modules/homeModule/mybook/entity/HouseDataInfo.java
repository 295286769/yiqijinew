package com.yiqiji.money.modules.homeModule.mybook.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ${huangweishui} on 2017/8/7.
 * address huang.weishui@71dai.com
 */
public class HouseDataInfo implements Parcelable{
    /**
     * houseid : 1
     * housename : 华阳镇嘉美地
     * housetype :
     * housesquare : 136㎡
     * housestyle : 欧式田园
     * houseway : 半包
     * housecity : 北京市
     * company :
     * housectime : 1466480220
     * accountbookid : 77280
     */

    private String houseid;
    private String housename;
    private String housetype;
    private String housesquare;
    private String housestyle;
    private String houseway;
    private String housecity;
    private String company;
    private String housectime;
    private String accountbookid;

    protected HouseDataInfo(Parcel in) {
        houseid = in.readString();
        housename = in.readString();
        housetype = in.readString();
        housesquare = in.readString();
        housestyle = in.readString();
        houseway = in.readString();
        housecity = in.readString();
        company = in.readString();
        housectime = in.readString();
        accountbookid = in.readString();
    }

    public static final Creator<HouseDataInfo> CREATOR = new Creator<HouseDataInfo>() {
        @Override
        public HouseDataInfo createFromParcel(Parcel in) {
            return new HouseDataInfo(in);
        }

        @Override
        public HouseDataInfo[] newArray(int size) {
            return new HouseDataInfo[size];
        }
    };

    public String getHouseid() {
        return houseid;
    }

    public void setHouseid(String houseid) {
        this.houseid = houseid;
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

    public String getHousecity() {
        return housecity;
    }

    public void setHousecity(String housecity) {
        this.housecity = housecity;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getHousectime() {
        return housectime;
    }

    public void setHousectime(String housectime) {
        this.housectime = housectime;
    }

    public String getAccountbookid() {
        return accountbookid;
    }

    public void setAccountbookid(String accountbookid) {
        this.accountbookid = accountbookid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(houseid);
        dest.writeString(housename);
        dest.writeString(housetype);
        dest.writeString(housesquare);
        dest.writeString(housestyle);
        dest.writeString(houseway);
        dest.writeString(housecity);
        dest.writeString(company);
        dest.writeString(housectime);
        dest.writeString(accountbookid);
    }
}
