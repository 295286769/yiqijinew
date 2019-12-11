package com.yiqiji.money.modules.common.entity;

import com.yiqiji.money.modules.common.db.BooksDbInfo;

public class OuterBooksDbInfo {
	private int code;
	private String msg;
	private BooksDbInfo data;

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

	public BooksDbInfo getData() {
		return data;
	}

	public void setData(BooksDbInfo data) {
		this.data = data;
	}

}
