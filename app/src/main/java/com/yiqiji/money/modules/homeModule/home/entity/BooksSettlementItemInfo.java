package com.yiqiji.money.modules.homeModule.home.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class BooksSettlementItemInfo implements Parcelable {
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public static final Creator<BooksSettlementItemInfo> CREATOR = new Creator<BooksSettlementItemInfo>() {
		public BooksSettlementItemInfo createFromParcel(Parcel in) {
			return new BooksSettlementItemInfo(in);
		}

		public BooksSettlementItemInfo[] newArray(int size) {
			return new BooksSettlementItemInfo[size];
		}
	};

	private BooksSettlementItemInfo(Parcel in) {
		memberid = in.readString();
		username = in.readString();
		usericon = in.readString();
		spentamount = in.readString();
		payamount = in.readString();
		receivable = in.readString();
		richman = in.readString();
		touser = in.readString();
		touserid = in.readString();
		tousericon = in.readString();
		userid = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(memberid);
		dest.writeString(username);
		dest.writeString(usericon);
		dest.writeString(spentamount);
		dest.writeString(payamount);
		dest.writeString(receivable);
		dest.writeString(richman);
		dest.writeString(touser);
		dest.writeString(touserid);
		dest.writeString(tousericon);
		dest.writeString(userid);

	}

	private String memberid;// 账本成员ID
	private String userid;// 账本成员用户ID
	private String username;// 成员昵称
	private String usericon; // 成员头像
	private String spentamount; // 累计消费金额
	private String payamount; // 应付款金额
	private String receivable; // 应收款金额
	private String richman; // 是否是财主，0.否，1.是

	private String touser; // 123qqwe 向 财主1 支付 15.00
	private String touserid; // touser的memberID
	private String tousericon; // touser的icon

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getTouserid() {
		return touserid;
	}

	public void setTouserid(String touserid) {
		this.touserid = touserid;
	}

	public String getTousericon() {
		return tousericon;
	}

	public void setTousericon(String tousericon) {
		this.tousericon = tousericon;
	}

	public String getMemberid() {
		return memberid;
	}

	public void setMemberid(String memberid) {
		this.memberid = memberid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsericon() {
		return usericon;
	}

	public void setUsericon(String usericon) {
		this.usericon = usericon;
	}

	public String getSpentamount() {
		return spentamount;
	}

	public void setSpentamount(String spentamount) {
		this.spentamount = spentamount;
	}

	public String getPayamount() {
		return payamount;
	}

	public void setPayamount(String payamount) {
		this.payamount = payamount;
	}

	public String getReceivable() {
		return receivable;
	}

	public void setReceivable(String receivable) {
		this.receivable = receivable;
	}

	public String getRichman() {
		return richman;
	}

	public void setRichman(String richman) {
		this.richman = richman;
	}

}
