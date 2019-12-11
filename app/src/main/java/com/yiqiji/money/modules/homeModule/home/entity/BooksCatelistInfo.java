package com.yiqiji.money.modules.homeModule.home.entity;

import com.yiqiji.money.modules.common.entity.MyBooksListInfo;

import java.util.List;

public class BooksCatelistInfo {
	private List<MyBooksListInfo> single;
	private List<MyBooksListInfo> multiple;

	public List<MyBooksListInfo> getSingle() {
		return single;
	}

	public void setSingle(List<MyBooksListInfo> single) {
		this.single = single;
	}

	public List<MyBooksListInfo> getMultiple() {
		return multiple;
	}

	public void setMultiple(List<MyBooksListInfo> multiple) {
		this.multiple = multiple;
	}

}
