package com.yiqiji.money.modules.common.request;

public class ResultException extends RuntimeException {
	private int errCode = 0;

	public ResultException(int errCode, String msg) {
		super(msg);
		this.errCode = errCode;
	}

	public int getErrCode() {
		return errCode;
	}
}
