package com.yiqiji.money.modules.common.entity;

public class StatInfo {
	private int code;
	private StatInfoItem data;
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

	public StatInfoItem getData() {
		return data;
	}

	public void setData(StatInfoItem data) {
		this.data = data;
	}

}
