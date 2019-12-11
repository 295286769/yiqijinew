package com.yiqiji.money.modules.common.entity.eventbean;

import com.yiqiji.money.modules.common.entity.Books.CatelistBean.BookListModel;

public class BookListModelBeanEvent {
	private BookListModel mBookListModel;
	private int tag = 0;// 0：账本列表 1：账本确定类发出的消息

	public BookListModelBeanEvent(BookListModel msg, int tag) {
		// TODO Auto-generated constructor stub
		mBookListModel = msg;
		this.tag = tag;
	}

	public BookListModel getBookListModel() {
		return mBookListModel;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

}
