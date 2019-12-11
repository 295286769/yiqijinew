package com.yiqiji.money.modules.homeModule.home.entity;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.yiqiji.money.modules.common.db.BillMemberInfo;

public class BillListInfoItem implements Parcelable {
	public BillListInfoItem() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public static final Creator<BillListInfoItem> CREATOR = new Creator<BillListInfoItem>() {
		public BillListInfoItem createFromParcel(Parcel in) {
			return new BillListInfoItem(in);
		}

		public BillListInfoItem[] newArray(int size) {
			return new BillListInfoItem[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(billid);
		dest.writeString(accountbookid);
		dest.writeString(userid);
		dest.writeString(username);
		dest.writeString(usericon);
		dest.writeString(billamount);
		dest.writeString(billtype);
		dest.writeString(billcateid);
		dest.writeString(billsubcateid);
		dest.writeString(billcatename);
		dest.writeString(billclear);
		dest.writeString(tradetime);
		dest.writeString(billctime);
		dest.writeString(billmark);
		dest.writeString(accountnumber);
		dest.writeString(billcount);
		dest.writeString(billcateicon);
		dest.writeString(billsubcatename);
		dest.writeString(billsubcateicon);
		dest.writeString(deviceid);
		dest.writeString(billstatus);
		dest.writeList(memberlist);

	}

	@SuppressWarnings("unchecked")
	private BillListInfoItem(Parcel in) {
		billid = in.readString();
		accountbookid = in.readString();
		userid = in.readString();
		username = in.readString();
		usericon = in.readString();
		billamount = in.readString();
		billtype = in.readString();
		billcateid = in.readString();
		billsubcateid = in.readString();
		billcatename = in.readString();
		billclear = in.readString();
		tradetime = in.readString();
		billctime = in.readString();
		billmark = in.readString();
		accountnumber = in.readString();
		billcount = in.readString();
		billcateicon = in.readString();
		billsubcatename = in.readString();
		billsubcateicon = in.readString();
		deviceid = in.readString();
		billstatus = in.readString();
		memberlist = in.readArrayList(BillMemberInfo.class.getClassLoader());

	}

	private String billid;// 账单ID
	private String accountbookid;// 账本ID
	private String userid;// 用户id
	private String username;// 用户名称
	private String usericon;// 用户头像
	private String billamount;// // 入账金额
	private String billtype;// 入账类型:0.收入,1.支出,2.转账
	private String billcateid; // 账单一级分类
	private String billsubcateid; // 账单二级分类
	private String billcatename; // 账单一级分类名称
	private String billclear; // 是否结算
	private String tradetime; // 账单发生时间
	private String billctime; // 当前时间
	private String billmark; // 账单备注
	private String accountnumber; // 关联资产ID，
	private String billcount; // 账单消费人数
	private String billcateicon; // 账单一级分类图片
	private String billsubcatename; //
	private String billsubcateicon; //
	private String deviceid; //
	private String billstatus; //
	private List<BillMemberInfo> memberlist;

	public String getBillstatus() {
		return billstatus;
	}

	public void setBillstatus(String billstatus) {
		this.billstatus = billstatus;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getUsericon() {
		return usericon;
	}

	public void setUsericon(String usericon) {
		this.usericon = usericon;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBillcatename() {
		return billcatename;
	}

	public void setBillcatename(String billcatename) {
		this.billcatename = billcatename;
	}

	public List<BillMemberInfo> getMemberlist() {
		return memberlist;
	}

	public void setMemberlist(List<BillMemberInfo> memberlist) {
		this.memberlist = memberlist;
	}

	public String getBillcateicon() {
		return billcateicon;
	}

	public void setBillcateicon(String billcateicon) {
		this.billcateicon = billcateicon;
	}

	public String getBillsubcatename() {
		return billsubcatename;
	}

	public void setBillsubcatename(String billsubcatename) {
		this.billsubcatename = billsubcatename;
	}

	public String getBillsubcateicon() {
		return billsubcateicon;
	}

	public void setBillsubcateicon(String billsubcateicon) {
		this.billsubcateicon = billsubcateicon;
	}

	public String getBillid() {
		return billid;
	}

	public void setBillid(String billid) {
		this.billid = billid;
	}

	public String getAccountbookid() {
		return accountbookid;
	}

	public void setAccountbookid(String accountbookid) {
		this.accountbookid = accountbookid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getBillamount() {
		return billamount;
	}

	public void setBillamount(String billamount) {
		this.billamount = billamount;
	}

	public String getBilltype() {
		return billtype;
	}

	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}

	public String getBillcateid() {
		return billcateid;
	}

	public void setBillcateid(String billcateid) {
		this.billcateid = billcateid;
	}

	public String getBillsubcateid() {
		return billsubcateid;
	}

	public void setBillsubcateid(String billsubcateid) {
		this.billsubcateid = billsubcateid;
	}

	public String getBillclear() {
		return billclear;
	}

	public void setBillclear(String billclear) {
		this.billclear = billclear;
	}

	public String getTradetime() {
		return tradetime;
	}

	public void setTradetime(String tradetime) {
		this.tradetime = tradetime;
	}

	public String getBillctime() {
		return billctime;
	}

	public void setBillctime(String billctime) {
		this.billctime = billctime;
	}

	public String getBillmark() {
		return billmark;
	}

	public void setBillmark(String billmark) {
		this.billmark = billmark;
	}

	public String getAccountnumber() {
		return accountnumber;
	}

	public void setAccountnumber(String accountnumber) {
		this.accountnumber = accountnumber;
	}

	public String getBillcount() {
		return billcount;
	}

	public void setBillcount(String billcount) {
		this.billcount = billcount;
	}

}
