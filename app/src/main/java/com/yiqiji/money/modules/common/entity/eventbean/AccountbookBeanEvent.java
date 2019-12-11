package com.yiqiji.money.modules.common.entity.eventbean;

import com.yiqiji.money.modules.common.entity.Books.AccountbookBean;

public class AccountbookBeanEvent {
	private int tag;// 0:

	private AccountbookBean mAccountbookBean;

	public AccountbookBeanEvent(AccountbookBean msg) {
		// TODO Auto-generated constructor stub
		mAccountbookBean = msg;
	}

	public AccountbookBean getAccountbookBean() {
		return mAccountbookBean;
	}
}