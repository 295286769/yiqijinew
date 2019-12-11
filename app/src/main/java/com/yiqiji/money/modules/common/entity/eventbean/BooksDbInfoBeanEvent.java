package com.yiqiji.money.modules.common.entity.eventbean;

import com.yiqiji.money.modules.common.db.BooksDbInfo;

public class BooksDbInfoBeanEvent {
	private int tag = 0;// 0：账本列表 1：账本确定类发出的消息
	private BooksDbInfo mBooksDbInfo;

	public BooksDbInfoBeanEvent(BooksDbInfo msg, int tag) {
		// TODO Auto-generated constructor stub
		mBooksDbInfo = msg;
		this.tag = tag;
	}

	public BooksDbInfo getBooksDbInfo() {
		return mBooksDbInfo;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}
}
