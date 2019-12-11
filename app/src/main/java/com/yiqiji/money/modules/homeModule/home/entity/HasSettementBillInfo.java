package com.yiqiji.money.modules.homeModule.home.entity;

import java.util.List;

import com.yiqiji.money.modules.common.db.DailycostEntity;

public class HasSettementBillInfo {
	private int code;
	private String msg;
	private List<DailycostEntity> data;

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

	public List<DailycostEntity> getData() {
		return data;
	}

	public void setData(List<DailycostEntity> data) {
		this.data = data;
	}

}
