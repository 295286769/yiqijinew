package com.yiqiji.money.modules.common.entity;

public class JsonData {
	private int code;
	private String msg;
	private int total;
	private int page;
	private int pagemax;
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
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPagemax() {
		return pagemax;
	}
	public void setPagemax(int pagemax) {
		this.pagemax = pagemax;
	}
}
