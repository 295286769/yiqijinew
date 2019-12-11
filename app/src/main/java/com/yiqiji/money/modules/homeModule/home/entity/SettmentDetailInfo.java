package com.yiqiji.money.modules.homeModule.home.entity;

import com.yiqiji.money.modules.common.db.DailycostEntity;

public class SettmentDetailInfo {
	private int code;
	private DailycostEntity data;
	private String msg;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public DailycostEntity getData() {
		return data;
	}

	public void setData(DailycostEntity data) {
		this.data = data;
	}

}
