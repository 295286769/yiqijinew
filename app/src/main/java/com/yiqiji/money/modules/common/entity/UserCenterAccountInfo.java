package com.yiqiji.money.modules.common.entity;

import java.io.Serializable;


import android.os.Parcel;
import android.os.Parcelable;

public class UserCenterAccountInfo extends JsonData implements Serializable, Parcelable {

    private static final long serialVersionUID = 4953275092288338285L;

    private String interesttotal;// 历史收益总计
    private String capitalbalance;// 本金余额
    private String interestbalance;// 收益余额
    private String investingtotal;// 在投本金总额
    private String investingtotal1;// 定活宝（赚呗）在投本金总和
    private String investingtotal3;// 定期在投本金总和
    private String hasautoinvest;// 是否开启了智能投资，1为开启，0为未开启
    private String xzbbalance;// 薪智宝账户余额
    private String egoldbalance;// e贝账户余额
    private String scorebalance; // 积分余额
    private String todayinterest; // 今日收益
    private String futureinterest; // 待收收益（主要为定期产品的待收收益额）（赚呗、活期、薪智宝的不包含在内）
    private String jointype; // 智能投资加入类型，1为定额，2为全额，0表示无类型（用户还未加入过）
    private String firstcharge; // 是否处于第一次充值后状态（判断是否提示用户去投资界面），1为是，0为否
    private String hasxzb; // 是否薪智宝账号
    private String xzbstatus;// 是否已经激活薪智宝，1为已经激活，0为未激活
    private String usercouponnum; // 优惠券数量

    public UserCenterAccountInfo() {
        this.interesttotal = "";
        this.capitalbalance = "";
        this.interestbalance = "";
        this.investingtotal = "";
        this.investingtotal1 = "";
        this.investingtotal3 = "";
        this.hasautoinvest = "0";
        this.xzbbalance = "";
        this.egoldbalance = "";
        this.scorebalance = "";
        this.todayinterest = "";
        this.futureinterest = "";
        this.jointype = "0";
        this.firstcharge = "";
        this.hasxzb = "0";
        this.xzbstatus = "0";
        this.usercouponnum = "0";
    }

    public UserCenterAccountInfo(Parcel source) {
        this.interesttotal = source.readString();
        this.capitalbalance = source.readString();
        this.interestbalance = source.readString();
        this.investingtotal = source.readString();
        this.investingtotal1 = source.readString();
        this.investingtotal3 = source.readString();
        this.hasautoinvest = source.readString();
        this.xzbbalance = source.readString();
        this.egoldbalance = source.readString();
        this.scorebalance = source.readString();
        this.todayinterest = source.readString();
        this.futureinterest = source.readString();
        this.jointype = source.readString();
        this.firstcharge = source.readString();
        this.hasxzb = source.readString();
        this.xzbstatus = source.readString();
        this.usercouponnum = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(interesttotal);
        dest.writeString(capitalbalance);
        dest.writeString(interestbalance);
        dest.writeString(investingtotal);
        dest.writeString(investingtotal1);
        dest.writeString(investingtotal3);
        dest.writeString(hasautoinvest);
        dest.writeString(xzbbalance);
        dest.writeString(egoldbalance);
        dest.writeString(scorebalance);
        dest.writeString(todayinterest);
        dest.writeString(futureinterest);
        dest.writeString(jointype);
        dest.writeString(firstcharge);
        dest.writeString(hasxzb);
        dest.writeString(xzbstatus);
        dest.writeString(usercouponnum);

    }

    public static final Creator<UserCenterAccountInfo> CREATOR = new Creator<UserCenterAccountInfo>() {

        @Override
        public UserCenterAccountInfo [] newArray(int size) {
            return new UserCenterAccountInfo[size];
        }

        @Override
        public UserCenterAccountInfo createFromParcel(Parcel source) {
            return new UserCenterAccountInfo(source);
        }
    };

//    public static UserCenterAccountInfo getUserCenterAccountInfo(String data) {
//        try {
//            return JSONObject.parseObject(data, UserCenterAccountInfo.class);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return null;
//        }
//    }

    public String getInteresttotal() {
        return interesttotal;
    }

    public void setInteresttotal(String interesttotal) {
        this.interesttotal = interesttotal;
    }

    public String getCapitalbalance() {
        return capitalbalance;
    }

    public void setCapitalbalance(String capitalbalance) {
        this.capitalbalance = capitalbalance;
    }

    public String getInterestbalance() {
        return interestbalance;
    }

    public void setInterestbalance(String interestbalance) {
        this.interestbalance = interestbalance;
    }

    public String getInvestingtotal() {
        return investingtotal;
    }

    public void setInvestingtotal(String investingtotal) {
        this.investingtotal = investingtotal;
    }

    public String getInvestingtotal1() {
        return investingtotal1;
    }

    public void setInvestingtotal1(String investingtotal1) {
        this.investingtotal1 = investingtotal1;
    }

    public String getInvestingtotal3() {
        return investingtotal3;
    }

    public void setInvestingtotal3(String investingtotal3) {
        this.investingtotal3 = investingtotal3;
    }

    public String getHasautoinvest() {
        return hasautoinvest;
    }

    public void setHasautoinvest(String hasautoinvest) {
        this.hasautoinvest = hasautoinvest;
    }

    public String getXzbbalance() {
        return xzbbalance;
    }

    public void setXzbbalance(String xzbbalance) {
        this.xzbbalance = xzbbalance;
    }

    public String getEgoldbalance() {
        return egoldbalance;
    }

    public void setEgoldbalance(String egoldbalance) {
        this.egoldbalance = egoldbalance;
    }

    public String getScorebalance() {
        return scorebalance;
    }

    public void setScorebalance(String scorebalance) {
        this.scorebalance = scorebalance;
    }

    public String getTodayinterest() {
        return todayinterest;
    }

    public void setTodayinterest(String todayinterest) {
        this.todayinterest = todayinterest;
    }

    public String getFutureinterest() {
        return futureinterest;
    }

    public void setFutureinterest(String futureinterest) {
        this.futureinterest = futureinterest;
    }

    public String getJointype() {
        return jointype;
    }

    public void setJointype(String jointype) {
        this.jointype = jointype;
    }

    public String getFirstcharge() {
        return firstcharge;
    }

    public void setFirstcharge(String firstcharge) {
        this.firstcharge = firstcharge;
    }

    public String getHasxzb() {
        return hasxzb;
    }

    public void setHasxzb(String hasxzb) {
        this.hasxzb = hasxzb;
    }

    public String getXzbstatus() {
        return xzbstatus;
    }

    public void setXzbstatus(String xzbstatus) {
        this.xzbstatus = xzbstatus;
    }

    public String getUsercouponnum() {
        return usercouponnum;
    }

    public void setUsercouponnum(String usercouponnum) {
        this.usercouponnum = usercouponnum;
    }
}
