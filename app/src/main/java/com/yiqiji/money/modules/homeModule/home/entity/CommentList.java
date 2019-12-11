package com.yiqiji.money.modules.homeModule.home.entity;

import java.util.List;

public class CommentList {
	private int code;
	private List<CommentListItem> data;
	private String msg;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<CommentListItem> getData() {
		return data;
	}

	public void setData(List<CommentListItem> data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
