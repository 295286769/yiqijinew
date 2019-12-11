package com.yiqiji.money.modules.common.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 用于post表单提交json
 *
 * @author Administrator
 */
public class PostBill implements Parcelable {
    /**
     * accountbooktype : 1 memberlist :
     * [{"amount":"700.00","status":0,"memberid"
     * :"199","ctime":1.483432091591718E9
     * ,"type":0},{"amount":"200.00","status":0
     * ,"memberid":"202","ctime":1.48343210628774E9
     * ,"type":1},{"amount":"300.00",
     * "status":0,"memberid":"203","ctime":1.483432106287802E9
     * ,"type":1},{"amount"
     * :"200.00","status":0,"memberid":"247","ctime":1.483432106287828E9
     * ,"type":1}] tradetime : 1483432091 pkid :
     * 168BA83A-B78D-404C-8143-5FF29C0A43DF accountbookid : 205 cateid : 90
     * action : edit billctime : 1483432091 billclear : 0 billamount : 700
     * billtype : 1 remark : accountnumber : billid : 92
     */

    private String accountbookid;// 账本ID
    private int accountbooktype;// 账本类型:1.多人账本;0.单人账本
    private String accountnumber;// 资产账户ID,默认为0
    private String billamount;// 账单金额
    private int billtype;// 账单类型:// 0.收入;1.支出;2.转账;3结算  4交款
    private int billclear;// 结算状态:0.未结算;1.已结算
    private String cateid;// 一级分类ID
    private String subcateid;// 二级分类ID
    private int billctime;// 记账时间
    private int tradetime;// 账单发生时间
    private String remark;// 账单备注
    private List<MemberlistBean> memberlist;// 多人账单，成员分摊费用, 见下表
    private String pkid;// 唯一ID
    private String billid;// 服务端账单ID,当编辑和删除时必传
    private String billbrand;// 装修品牌

    public PostBill() {

    }

    protected PostBill(Parcel in) {
        accountbookid = in.readString();
        accountbooktype = in.readInt();
        accountnumber = in.readString();
        billamount = in.readString();
        billtype = in.readInt();
        billclear = in.readInt();
        cateid = in.readString();
        subcateid = in.readString();
        billctime = in.readInt();
        tradetime = in.readInt();
        remark = in.readString();
        memberlist = in.createTypedArrayList(MemberlistBean.CREATOR);
        pkid = in.readString();
        billid = in.readString();
        action = in.readString();
        billimg = in.readString();
        address = in.readString();
        billbrand = in.readString();
    }

    public static final Creator<PostBill> CREATOR = new Creator<PostBill>() {
        @Override
        public PostBill createFromParcel(Parcel in) {
            return new PostBill(in);
        }

        @Override
        public PostBill[] newArray(int size) {
            return new PostBill[size];
        }
    };

    public String getBillimg() {
        return billimg;
    }

    public void setBillimg(String billimg) {
        this.billimg = billimg;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String action;// 操作方法:add(新增),edit(编辑),del(删除)
    private String billimg;
    private String address;

    public String getSubcateid() {
        return subcateid;
    }

    public void setSubcateid(String subcateid) {
        this.subcateid = subcateid;
    }

    /**
     * amount : 700.00 status : 0 memberid : 199 ctime : 1.483432091591718E9
     * type : 0
     */

    public int getAccountbooktype() {
        return accountbooktype;
    }

    public void setAccountbooktype(int accountbooktype) {
        this.accountbooktype = accountbooktype;
    }

    public int getTradetime() {
        return tradetime;
    }

    public void setTradetime(int tradetime) {
        this.tradetime = tradetime;
    }

    public String getPkid() {
        return pkid;
    }

    public void setPkid(String pkid) {
        this.pkid = pkid;
    }

    public String getAccountbookid() {
        return accountbookid;
    }

    public void setAccountbookid(String accountbookid) {
        this.accountbookid = accountbookid;
    }

    public String getCateid() {
        return cateid;
    }

    public void setCateid(String cateid) {
        this.cateid = cateid;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getBillctime() {
        return billctime;
    }

    public void setBillctime(int billctime) {
        this.billctime = billctime;
    }

    public int getBillclear() {
        return billclear;
    }

    public void setBillclear(int billclear) {
        this.billclear = billclear;
    }

    public String getBillamount() {
        return billamount;
    }

    public void setBillamount(String billamount) {
        this.billamount = billamount;
    }

    public int getBilltype() {
        return billtype;
    }

    public void setBilltype(int billtype) {
        this.billtype = billtype;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber;
    }

    public String getBillid() {
        return billid;
    }

    public void setBillid(String billid) {
        this.billid = billid;
    }

    public List<MemberlistBean> getMemberlist() {
        return memberlist;
    }

    public void setMemberlist(List<MemberlistBean> memberlist) {
        this.memberlist = memberlist;
    }

    public String getBillbrand() {
        return billbrand;
    }

    public void setBillbrand(String billbrand) {
        this.billbrand = billbrand;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(accountbookid);
        dest.writeInt(accountbooktype);
        dest.writeString(accountnumber);
        dest.writeString(billamount);
        dest.writeInt(billtype);
        dest.writeInt(billclear);
        dest.writeString(cateid);
        dest.writeString(subcateid);
        dest.writeInt(billctime);
        dest.writeInt(tradetime);
        dest.writeString(remark);
        dest.writeTypedList(memberlist);
        dest.writeString(pkid);
        dest.writeString(billid);
        dest.writeString(action);
        dest.writeString(billimg);
        dest.writeString(address);
        dest.writeString(billbrand);
    }

    public static class MemberlistBean implements Parcelable {

        private String amount;// 成员费用
        private int status;// 结算状态:0.未结算;1.已结算
        private String memberid;// 账本成员ID
        private long ctime;// 账单结算创建时间
        private int type;// 账单类型:0.收入;1.支出;2.转账

        public MemberlistBean() {

        }

        protected MemberlistBean(Parcel in) {
            amount = in.readString();
            status = in.readInt();
            memberid = in.readString();
            ctime = in.readLong();
            type = in.readInt();
        }

        public static final Creator<MemberlistBean> CREATOR = new Creator<MemberlistBean>() {
            @Override
            public MemberlistBean createFromParcel(Parcel in) {
                return new MemberlistBean(in);
            }

            @Override
            public MemberlistBean[] newArray(int size) {
                return new MemberlistBean[size];
            }
        };

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMemberid() {
            return memberid;
        }

        public void setMemberid(String memberid) {
            this.memberid = memberid;
        }

        public long getCtime() {
            return ctime;
        }

        public void setCtime(long ctime) {
            this.ctime = ctime;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        @Override
        public int describeContents() {
//            private String amount;// 成员费用
//            private int status;// 结算状态:0.未结算;1.已结算
//            private String memberid;// 账本成员ID
//            private long ctime;// 账单结算创建时间
//            private int type;// 账单类型:0.收入;1.支出;2.转账
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

            dest.writeString(amount);
            dest.writeInt(status);
            dest.writeString(memberid);
            dest.writeLong(ctime);
            dest.writeInt(type);
        }
    }
}
