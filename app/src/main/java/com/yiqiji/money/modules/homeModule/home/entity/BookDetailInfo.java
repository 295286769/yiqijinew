package com.yiqiji.money.modules.homeModule.home.entity;

import java.util.List;

import com.yiqiji.money.modules.common.db.BooksDbMemberInfo;

public class BookDetailInfo {
	private int code;
	private String msg;
	private String accountbookid;// 账本id
	private String accountbooktitle;// 账本名称
	private String userid;// 账本创建人uid
	private String accountbookcate;// 账本分类id
	private String accountbookcatename;// 账本分类名称

	private String accountbooktype;// 是否是多人账本
	private String accountbookbudget;// 账本预算费
	private String accountbookstatus; // 账本状态
	private String accountbookcount; // 账本人数
	private String isclear; // 是否需要结算
	private String sorttype;// 排序类型：1.按月，0.按天
	private String myuid; // 当前操作人uid
	private String accountbookctime; // 账本创建时间
	private String accountbookutime; // 账本更新时间

	private String payamount; // 本月支出/累计支出
	private String receivable; // 本月收入/累计收入
	private String spentdiff; // 本月结余/累计结余
	private String myspent; // 我的消费/我的支出
	private String budgetdiff; // 预算差额(当值大于0，表示预算剩余；当值小于0，表示预算超支)
	private String myspentdiff; // 我需付（当值小于0）/我应收（当值大于0）/已结清（当值等于0）
	private String deviceid; //

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	private List<BooksDbMemberInfo> member;

	public List<BooksDbMemberInfo> getMember() {
		return member;
	}

	public void setMember(List<BooksDbMemberInfo> member) {
		this.member = member;
	}

	public String getAccountbookcatename() {
		return accountbookcatename;
	}

	public void setAccountbookcatename(String accountbookcatename) {
		this.accountbookcatename = accountbookcatename;
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

	public String getSpentdiff() {
		return spentdiff;
	}

	public void setSpentdiff(String spentdiff) {
		this.spentdiff = spentdiff;
	}

	public String getMyspent() {
		return myspent;
	}

	public void setMyspent(String myspent) {
		this.myspent = myspent;
	}

	public String getBudgetdiff() {
		return budgetdiff;
	}

	public void setBudgetdiff(String budgetdiff) {
		this.budgetdiff = budgetdiff;
	}

	public String getMyspentdiff() {
		return myspentdiff;
	}

	public void setMyspentdiff(String myspentdiff) {
		this.myspentdiff = myspentdiff;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getAccountbookid() {
		return accountbookid;
	}

	public void setAccountbookid(String accountbookid) {
		this.accountbookid = accountbookid;
	}

	public String getAccountbooktitle() {
		return accountbooktitle;
	}

	public void setAccountbooktitle(String accountbooktitle) {
		this.accountbooktitle = accountbooktitle;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getAccountbookcate() {
		return accountbookcate;
	}

	public void setAccountbookcate(String accountbookcate) {
		this.accountbookcate = accountbookcate;
	}

	public String getAccountbooktype() {
		return accountbooktype;
	}

	public void setAccountbooktype(String accountbooktype) {
		this.accountbooktype = accountbooktype;
	}

	public String getAccountbookbudget() {
		return accountbookbudget;
	}

	public void setAccountbookbudget(String accountbookbudget) {
		this.accountbookbudget = accountbookbudget;
	}

	public String getAccountbookstatus() {
		return accountbookstatus;
	}

	public void setAccountbookstatus(String accountbookstatus) {
		this.accountbookstatus = accountbookstatus;
	}

	public String getAccountbookcount() {
		return accountbookcount;
	}

	public void setAccountbookcount(String accountbookcount) {
		this.accountbookcount = accountbookcount;
	}

	public String getIsclear() {
		return isclear;
	}

	public void setIsclear(String isclear) {
		this.isclear = isclear;
	}

	public String getSorttype() {
		return sorttype;
	}

	public void setSorttype(String sorttype) {
		this.sorttype = sorttype;
	}

	public String getMyuid() {
		return myuid;
	}

	public void setMyuid(String myuid) {
		this.myuid = myuid;
	}

	public String getAccountbookctime() {
		return accountbookctime;
	}

	public void setAccountbookctime(String accountbookctime) {
		this.accountbookctime = accountbookctime;
	}

	public String getAccountbookutime() {
		return accountbookutime;
	}

	public void setAccountbookutime(String accountbookutime) {
		this.accountbookutime = accountbookutime;
	}

}
