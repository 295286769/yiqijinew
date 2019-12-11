package com.yiqiji.frame.core.facade;

/***
 * 全局错误信息的封装类
 * 
 * @author aeeiko
 * 
 */
public class SimpleMsg {
	private int errCode;
	private String errMsg = "";

	public SimpleMsg(int errCode, String errMsg) {
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
}
