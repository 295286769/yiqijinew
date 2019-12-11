package com.yiqiji.money.modules.homeModule.home.entity;

import com.yiqiji.money.modules.common.db.BooksDbInfo;

import java.util.List;

/**
 * Created by ${huangweishui} on 2016/12/29. address huang.weishui@71dai.com
 */
public class BooksListInfo {

	private BooksCatelistInfo catelist;

	private List<BooksDbInfo> accountbook;
	private int code;
	private String msg;

	public BooksCatelistInfo getCatelist() {
		return catelist;
	}

	public void setCatelist(BooksCatelistInfo catelist) {
		this.catelist = catelist;
	}

	public List<BooksDbInfo> getAccountbook() {
		return accountbook;
	}

	public void setAccountbook(List<BooksDbInfo> accountbook) {
		this.accountbook = accountbook;
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

}
