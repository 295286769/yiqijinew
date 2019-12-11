package com.yiqiji.money.modules.common.entity.eventbean;

import com.yiqiji.money.modules.common.entity.Books;

public class BooksBeanEvent {
	private Books mBooks;

	public BooksBeanEvent(Books msg) {
		// TODO Auto-generated constructor stub
		mBooks = msg;
	}

	public Books getBooks() {
		return mBooks;
	}
}
