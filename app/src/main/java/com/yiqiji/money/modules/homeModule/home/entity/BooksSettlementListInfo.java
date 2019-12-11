package com.yiqiji.money.modules.homeModule.home.entity;

import java.util.List;

public class BooksSettlementListInfo {
	private int code;
	private List<BooksSettlementItemInfo> data;
	private String msg;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<BooksSettlementItemInfo> getData() {
		return data;
	}

	public void setData(List<BooksSettlementItemInfo> data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
